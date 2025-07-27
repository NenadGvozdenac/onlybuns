# Use the official Node.js image
FROM node:18-alpine

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json (if available)
COPY package*.json ./

# Install dependencies
RUN npm install --production

# Copy the rest of the application code
COPY . .

# Expose the port the app runs on
EXPOSE 4000

# Create a non-root user to run the application
RUN addgroup -g 1001 -S nodejs
RUN adduser -S messagequeue -u 1001

# Change ownership of the app directory to the nodejs user
RUN chown -R messagequeue:nodejs /app
USER messagequeue

# Define the command to run the application
CMD ["npm", "start"]
