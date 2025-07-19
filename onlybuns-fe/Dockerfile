# Use an official Node.js image as the base
FROM node:18-alpine

# Set working directory
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Make the startup script executable
RUN chmod +x /app/start.sh

# Expose the ports the app runs on
EXPOSE 5173 9091

# Start both the metrics server and the main application
CMD ["/bin/sh", "/app/start.sh"]
