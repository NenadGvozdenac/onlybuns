// src/swaggerConfig.js
import swaggerJsdoc from 'swagger-jsdoc';

const swaggerConfig = () => {
  const options = {
    definition: {
      openapi: '3.0.0',
      info: {
        title: 'Vets and Pets API',
        version: '1.0.0',
        description: 'API documentation for the Vets and Pets service',
      },
    },
    apis: ['./src/routes/vetRoutes.js'], // Update as per your project structure
  };

  return swaggerJsdoc(options);
};

export default swaggerConfig;