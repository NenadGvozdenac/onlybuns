// app.js or server.js
import express from 'express';
import mongoose from 'mongoose';
import dotenv from 'dotenv';
import { WebSocketServer } from 'ws';
import swaggerUi from 'swagger-ui-express';

// Import the Swagger configuration
import swaggerConfig from './config/swaggerConfig.js';

// Import route handlers
import { getVets, getVetById, addVet, deleteAllVets } from './routes/vetRoutes.js'; 

import vet_service from './services/vet_service.js';

dotenv.config();

const app = express();
const PORT = process.env.PORT || 3000;
const MONGO_URI = process.env.MONGO_URI || 'mongodb://localhost:27017/test';

// Middleware for parsing JSON
app.use(express.json());

// Connect to MongoDB
mongoose
  .connect(MONGO_URI)
  .then(() => {
    console.log('Connected to MongoDB');
  })
  .catch((error) => {
    console.error('Error connecting to MongoDB:', error.message);
  });

// Swagger setup
const swaggerSpec = swaggerConfig();

// Serve Swagger API docs
app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerSpec));

// Basic route for testing
app.get('/', (_, res) => {
  res.send('Hello, Vets and Pets API is running!');
});

// Use the imported route handlers
app.get('/api/vets', getVets);
app.get('/api/vets/:id', getVetById);
app.post('/api/vets', addVet);
app.delete('/api/vets', deleteAllVets);

// Start the server
const server = app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});

// WebSocket server
const wsServer = new WebSocketServer({ server });

wsServer.on('connection', async (socket) => {
  console.log('Spring Boot application connected.');

  const vets = await vet_service.getVets();

  // Create a queue with all the vets to be sent
  const vetQueue = [...vets];

  // Function to process and send each vet from the queue
  const processQueue = () => {
    const vet = vetQueue.shift();
    socket.send(JSON.stringify(vet));
  };

  // Process the queue every 1000 milliseconds (1 second)
  const intervalId = setInterval(() => {
    if (vetQueue.length > 0) {
      processQueue();
    } else {
      console.log('All vets sent.');
      socket.close();
      clearInterval(intervalId);
    }
  }, 300);
});