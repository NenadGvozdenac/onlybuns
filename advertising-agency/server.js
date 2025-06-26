const express = require('express');
const amqp = require('amqplib');
const http = require('http');
const socketIo = require('socket.io');
const cors = require('cors');

const app = express();
const server = http.createServer(app);
const io = socketIo(server, {
  cors: {
    origin: "*",
    methods: ["GET", "POST"]
  }
});

app.use(cors());
app.use(express.json());
app.use(express.static('public'));

const PORT = process.env.PORT || 3001;
const RABBITMQ_URL = process.env.RABBITMQ_URL || 'amqp://guest:guest@localhost:5672';
const AGENCY_NAME = process.env.AGENCY_NAME || `Agency-${Math.floor(Math.random() * 1000)}`;

let connection;
let channel;
let receivedMessages = [];

// Connect to RabbitMQ
async function connectToRabbitMQ() {
  try {
    console.log(`[${AGENCY_NAME}] Connecting to RabbitMQ at ${RABBITMQ_URL}...`);
    connection = await amqp.connect(RABBITMQ_URL);
    channel = await connection.createChannel();
    
    const exchangeName = 'post_advertisements';
    
    // Declare fanout exchange
    await channel.assertExchange(exchangeName, 'fanout', { durable: true });
    
    // Create a unique queue for this agency instance
    const queueResult = await channel.assertQueue('', { exclusive: true });
    const queueName = queueResult.queue;
    
    // Bind queue to exchange
    await channel.bindQueue(queueName, exchangeName, '');
    
    console.log(`[${AGENCY_NAME}] Connected to RabbitMQ. Waiting for messages on queue: ${queueName}`);
    
    // Consume messages
    channel.consume(queueName, (message) => {
      if (message) {
        const content = JSON.parse(message.content.toString());
        const receivedAt = new Date().toISOString();
        
        const messageData = {
          ...content,
          receivedAt,
          agencyName: AGENCY_NAME
        };
        
        receivedMessages.unshift(messageData); // Add to beginning of array
        
        // Keep only last 100 messages
        if (receivedMessages.length > 100) {
          receivedMessages = receivedMessages.slice(0, 100);
        }
        
        console.log(`[${AGENCY_NAME}] Received advertisement:`, messageData);
        
        // Emit to all connected clients via WebSocket
        io.emit('newAdvertisement', messageData);
        
        channel.ack(message);
      }
    });
    
  } catch (error) {
    console.error(`[${AGENCY_NAME}] Failed to connect to RabbitMQ:`, error);
    // Retry connection after 5 seconds
    setTimeout(connectToRabbitMQ, 5000);
  }
}

// REST API endpoints
app.get('/api/messages', (req, res) => {
  res.json({
    agencyName: AGENCY_NAME,
    messages: receivedMessages,
    totalReceived: receivedMessages.length
  });
});

app.get('/api/status', (req, res) => {
  res.json({
    agencyName: AGENCY_NAME,
    connected: !!connection && !connection.connection.destroyed,
    totalMessages: receivedMessages.length
  });
});

// WebSocket connection
io.on('connection', (socket) => {
  console.log(`[${AGENCY_NAME}] Client connected to WebSocket`);
  
  // Send existing messages to newly connected client
  socket.emit('initialData', {
    agencyName: AGENCY_NAME,
    messages: receivedMessages
  });
  
  socket.on('disconnect', () => {
    console.log(`[${AGENCY_NAME}] Client disconnected from WebSocket`);
  });
});

// Graceful shutdown
process.on('SIGINT', async () => {
  console.log(`[${AGENCY_NAME}] Shutting down gracefully...`);
  if (channel) {
    await channel.close();
  }
  if (connection) {
    await connection.close();
  }
  process.exit(0);
});

// Start server
server.listen(PORT, () => {
  console.log(`[${AGENCY_NAME}] Advertising Agency server running on port ${PORT}`);
  connectToRabbitMQ();
});
