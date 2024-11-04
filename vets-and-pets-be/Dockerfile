# Use the official Node.js image as a base
FROM node:20

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json files
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Install TypeScript globally and build the application
RUN npm install -g typescript && npm run build

# Expose the port the app runs on
EXPOSE 3000

# Set the command to start the server
CMD ["npm", "run", "dev"]
