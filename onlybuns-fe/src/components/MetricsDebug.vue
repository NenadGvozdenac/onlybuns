<template>
  <div class="metrics-debug">
    <h3>Metrics Debug Panel</h3>
    <div class="metrics-actions">
      <button @click="testPageView">Test Page View</button>
      <button @click="testHttpRequest">Test HTTP Request</button>
      <button @click="updateUsers">Update Active Users</button>
    </div>
    <div class="metrics-info">
      <p><strong>Metrics Endpoint:</strong> http://localhost:9091/metrics</p>
      <p><strong>Health Check:</strong> http://localhost:9091/health</p>
    </div>
  </div>
</template>

<script>
import { inject } from 'vue';
import apiClient from '@/services/apiClient';

export default {
  name: 'MetricsDebug',
  setup() {
    const metrics = inject('metrics');

    const testPageView = () => {
      metrics.trackPageView('/test-page');
      console.log('Page view tracked: /test-page');
    };

    const testHttpRequest = async () => {
      try {
        // Make a test request using the instrumented axios client
        await apiClient.get('/api/test');
      } catch (error) {
        console.log('Test HTTP request completed (may have failed, but metrics should be tracked)');
      }
    };

    const updateUsers = () => {
      const randomUserCount = Math.floor(Math.random() * 100) + 1;
      metrics.updateActiveUsers(randomUserCount);
      console.log(`Active users updated: ${randomUserCount}`);
    };

    return {
      testPageView,
      testHttpRequest,
      updateUsers
    };
  }
};
</script>

<style scoped>
.metrics-debug {
  border: 1px solid #ccc;
  padding: 20px;
  margin: 20px;
  border-radius: 5px;
  background-color: #f9f9f9;
}

.metrics-actions {
  margin: 15px 0;
}

.metrics-actions button {
  margin-right: 10px;
  padding: 8px 16px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.metrics-actions button:hover {
  background-color: #0056b3;
}

.metrics-info {
  margin-top: 15px;
  font-size: 14px;
  color: #666;
}
</style>
