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

// Message Queue configuration
const MESSAGE_QUEUE_URL = process.env.MESSAGE_QUEUE_URL || 'http://message-queue:4000';
const QUEUE_NAME = 'hospital-data';

// Function to send data to message queue
async function sendToMessageQueue(data) {
  try {
    const response = await fetch(`${MESSAGE_QUEUE_URL}/api/queue/${QUEUE_NAME}/send`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data)
    });
    
    if (response.ok) {
      console.log('Data sent to message queue successfully');
      return await response.json();
    } else {
      console.error('Failed to send data to message queue:', response.statusText);
    }
  } catch (error) {
    console.error('Error sending data to message queue:', error);
  }
}

// Endpoint to trigger sending all vets to message queue
app.post('/api/send-vets-to-queue', async (req, res) => {
  try {
    console.log('Fetching vets and sending to message queue...');
    const vets = await vet_service.getVets();
    
    // Send each vet to the message queue with a delay
    for (let i = 0; i < vets.length; i++) {
      const vet = vets[i];
      await sendToMessageQueue(vet);
      
      // Add delay between sends to simulate queue processing
      if (i < vets.length - 1) {
        await new Promise(resolve => setTimeout(resolve, 300));
      }
    }
    
    res.json({
      success: true,
      message: `Successfully sent ${vets.length} vets to message queue`,
      queueName: QUEUE_NAME
    });
  } catch (error) {
    console.error('Error sending vets to queue:', error);
    res.status(500).json({
      success: false,
      message: 'Failed to send vets to message queue',
      error: error.message
    });
  }
});

// Start the server
const server = app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
  console.log(`Message Queue URL: ${MESSAGE_QUEUE_URL}`);
  console.log(`Queue Name: ${QUEUE_NAME}`);
});