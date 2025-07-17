const express = require('express');
const { WebSocketServer } = require('ws');
const http = require('http');

const app = express();
const PORT = process.env.PORT || 4000;

app.use(express.json());

// Simple message queue implementation
class MessageQueue {
    constructor() {
        this.queues = new Map(); // queueName -> messages[]
        this.consumers = new Map(); // queueName -> WebSocket connections[]
    }

    // Add message to queue
    enqueue(queueName, message) {
        if (!this.queues.has(queueName)) {
            this.queues.set(queueName, []);
        }
        
        const queue = this.queues.get(queueName);
        queue.push({
            id: Date.now() + Math.random(),
            timestamp: new Date().toISOString(),
            data: message
        });

        console.log(`Message added to queue '${queueName}':`, message);
        
        // Immediately send to consumers if any are connected
        this.processQueue(queueName);
    }

    // Process queue and send messages to consumers
    processQueue(queueName) {
        const queue = this.queues.get(queueName);
        const consumers = this.consumers.get(queueName) || [];

        if (!queue || queue.length === 0 || consumers.length === 0) {
            return;
        }

        // Send messages to all connected consumers
        consumers.forEach(consumer => {
            if (consumer.readyState === consumer.OPEN) {
                while (queue.length > 0) {
                    const message = queue.shift();
                    try {
                        consumer.send(JSON.stringify(message.data));
                        console.log(`Message sent to consumer from queue '${queueName}'`);
                        
                        // Add delay between messages to simulate processing
                        setTimeout(() => {}, 300);
                    } catch (error) {
                        console.error('Error sending message to consumer:', error);
                        // Put message back in queue if sending failed
                        queue.unshift(message);
                        break;
                    }
                }
            }
        });

        // Remove disconnected consumers
        const activeConsumers = consumers.filter(consumer => 
            consumer.readyState === consumer.OPEN
        );
        this.consumers.set(queueName, activeConsumers);
    }

    // Add consumer (WebSocket connection) to queue
    addConsumer(queueName, consumer) {
        if (!this.consumers.has(queueName)) {
            this.consumers.set(queueName, []);
        }
        
        this.consumers.get(queueName).push(consumer);
        console.log(`Consumer added to queue '${queueName}'`);
        
        // Process any pending messages
        this.processQueue(queueName);
    }

    // Remove consumer from queue
    removeConsumer(queueName, consumer) {
        const consumers = this.consumers.get(queueName);
        if (consumers) {
            const index = consumers.indexOf(consumer);
            if (index > -1) {
                consumers.splice(index, 1);
                console.log(`Consumer removed from queue '${queueName}'`);
            }
        }
    }

    // Get queue status
    getQueueStatus(queueName) {
        const queue = this.queues.get(queueName) || [];
        const consumers = this.consumers.get(queueName) || [];
        
        return {
            queueName,
            messagesInQueue: queue.length,
            activeConsumers: consumers.filter(c => c.readyState === c.OPEN).length,
            totalMessages: queue.length
        };
    }

    // Get all queues status
    getAllQueuesStatus() {
        const status = {};
        for (const queueName of this.queues.keys()) {
            status[queueName] = this.getQueueStatus(queueName);
        }
        return status;
    }
}

const messageQueue = new MessageQueue();

// REST API endpoints for producers to send messages
app.post('/api/queue/:queueName/send', (req, res) => {
    const { queueName } = req.params;
    const message = req.body;
    
    messageQueue.enqueue(queueName, message);
    
    res.json({
        success: true,
        message: 'Message added to queue',
        queueName,
        queueStatus: messageQueue.getQueueStatus(queueName)
    });
});

// Get queue status
app.get('/api/queue/:queueName/status', (req, res) => {
    const { queueName } = req.params;
    res.json(messageQueue.getQueueStatus(queueName));
});

// Get all queues status
app.get('/api/queues/status', (req, res) => {
    res.json(messageQueue.getAllQueuesStatus());
});

// Health check
app.get('/health', (req, res) => {
    res.json({ 
        status: 'healthy', 
        timestamp: new Date().toISOString(),
        queues: messageQueue.getAllQueuesStatus()
    });
});

// Create HTTP server
const server = http.createServer(app);

// WebSocket server for consumers
const wsServer = new WebSocketServer({ 
    server,
    path: '/ws'
});

wsServer.on('connection', (socket, request) => {
    console.log('New WebSocket connection established');
    
    // Extract queue name from URL query parameters
    const url = new URL(request.url, `http://${request.headers.host}`);
    const queueName = url.searchParams.get('queue') || 'default';
    
    console.log(`Consumer connected to queue: ${queueName}`);
    
    // Add consumer to the specified queue
    messageQueue.addConsumer(queueName, socket);
    
    socket.on('close', () => {
        console.log(`Consumer disconnected from queue: ${queueName}`);
        messageQueue.removeConsumer(queueName, socket);
    });
    
    socket.on('error', (error) => {
        console.error('WebSocket error:', error);
        messageQueue.removeConsumer(queueName, socket);
    });
    
    // Send welcome message
    socket.send(JSON.stringify({
        type: 'welcome',
        message: `Connected to message queue: ${queueName}`,
        queueStatus: messageQueue.getQueueStatus(queueName)
    }));
});

// Start the server
server.listen(PORT, () => {
    console.log(`Message Queue Server is running on port ${PORT}`);
    console.log(`WebSocket endpoint: ws://localhost:${PORT}/ws?queue=<queueName>`);
    console.log(`REST API endpoint: http://localhost:${PORT}/api/queue/<queueName>/send`);
    console.log(`Health check: http://localhost:${PORT}/health`);
});

// Graceful shutdown
process.on('SIGTERM', () => {
    console.log('SIGTERM received, shutting down gracefully');
    server.close(() => {
        console.log('Message Queue Server closed');
    });
});

module.exports = app;
