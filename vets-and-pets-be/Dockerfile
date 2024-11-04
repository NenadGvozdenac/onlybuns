# Use the official Node.js image as a base
FROM node:20

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json files to install dependencies
COPY package*.json ./

# Install all dependencies, including devDependencies, for building the app
RUN npm install

# Copy the rest of the application code
COPY . .

# Expose the port the app runs on
EXPOSE 3000

# Set the command to start the server using the built JavaScript files
CMD ["npm", "start"]