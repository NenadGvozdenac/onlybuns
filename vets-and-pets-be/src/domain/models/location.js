import mongoose from 'mongoose';
const { Schema } = mongoose;

// Define the Location schema
const LocationSchema = new Schema({
    longitude: {
      type: Number,
      required: true,
      min: -180,
      max: 180,
    },
    latitude: {
      type: Number,
      required: true,
      min: -90,
      max: 90,
    },
  });

export default LocationSchema;