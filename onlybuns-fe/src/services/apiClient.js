import axios from 'axios';

// Create a custom axios instance
const apiClient = axios.create({
  baseURL: process.env.VUE_APP_API_BASE_URL || 'http://localhost:8080',
  timeout: 10000,
});

// Request interceptor to track outgoing requests
apiClient.interceptors.request.use(
  (config) => {
    config.metadata = { startTime: Date.now() };
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to track request completion and metrics
apiClient.interceptors.response.use(
  (response) => {
    const endTime = Date.now();
    const duration = (endTime - response.config.metadata.startTime) / 1000;
    
    // Track the request metrics if metrics are available
    if (window && window.app && window.app.config.globalProperties.$metrics) {
      const metrics = window.app.config.globalProperties.$metrics;
      metrics.trackHttpRequest(
        response.config.method.toUpperCase(),
        response.config.url,
        response.status,
        duration
      );
    }
    
    return response;
  },
  (error) => {
    const endTime = Date.now();
    const duration = error.config?.metadata?.startTime 
      ? (endTime - error.config.metadata.startTime) / 1000 
      : 0;
    
    // Track failed requests too
    if (window && window.app && window.app.config.globalProperties.$metrics) {
      const metrics = window.app.config.globalProperties.$metrics;
      metrics.trackHttpRequest(
        error.config?.method?.toUpperCase() || 'UNKNOWN',
        error.config?.url || 'unknown',
        error.response?.status || 0,
        duration
      );
    }
    
    return Promise.reject(error);
  }
);

export default apiClient;
