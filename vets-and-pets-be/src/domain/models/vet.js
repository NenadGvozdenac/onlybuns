import mongoose, { model } from 'mongoose';
const { Schema } = mongoose;

import LocationSchema from './location.js';

const VetSchema = new Schema({
    name: {
        type: String,
        required: true,
        trim: true,
    },
    description: {
        type: String,
        trim: true,
    },
    location: {
        type: LocationSchema,
        required: true,
    },
});

export default VetSchema;