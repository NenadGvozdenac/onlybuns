import express from 'express';
import mongoose from 'mongoose';
import dotenv from 'dotenv';

import vet_controller from './controllers/vet_controller.js';

// Load environment variables from .env file
dotenv.config();

const app = express();
const PORT = process.env.PORT || 3000;
const MONGO_URI = process.env.MONGO_URI || 'mongodb://localhost:27017/test';

// Middleware for parsing JSON
app.use(express.json());

// Connect to MongoDB with updated options
mongoose
  .connect(MONGO_URI)
  .then(() => {
    console.log('Connected to MongoDB');
  })
  .catch((error) => {
    console.error('Error connecting to MongoDB:', error.message);
  });

// Basic route for testing
app.get('/', (_, res) => {
  res.send('Hello, Vets and Pets API is running!');
});

app.post('/api/vets', (req, res) => {
  vet_controller.addVet(req, res);
});

app.get('/api/vets', (req, res) => {
  vet_controller.getVets(req, res);
});

app.get('/api/vets/:id', (req, res) => {
  vet_controller.getVetById(req, res);
});

app.delete('/api/vets', (req, res) => {
  vet_controller.deleteAllVets(req, res);
});

// Start the server
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
