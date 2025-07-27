import axios from 'axios';

class MetricsCollector {
  constructor() {
    // In production/Docker, the metrics server runs on the same host
    this.metricsEndpoint = process.env.NODE_ENV === 'production' 
      ? 'http://localhost:9091' 
      : 'http://localhost:9091';
    this.isEnabled = true;
  }

  // Track page views
  async trackPageView(page) {
    if (!this.isEnabled) return;
    
    try {
      await axios.post(`${this.metricsEndpoint}/metrics/pageview`, { page });
    } catch (error) {
      console.warn('Failed to track page view:', error.message);
    }
  }

  // Track HTTP requests made by the frontend
  async trackHttpRequest(method, route, statusCode, duration) {
    if (!this.isEnabled) return;

    try {
      await axios.post(`${this.metricsEndpoint}/metrics/httprequest`, {
        method,
        route,
        status_code: statusCode,
        duration
      });
    } catch (error) {
      console.warn('Failed to track HTTP request:', error.message);
    }
  }

  // Update active users count
  async updateActiveUsers(count) {
    if (!this.isEnabled) return;

    try {
      await axios.post(`${this.metricsEndpoint}/metrics/activeusers`, { count });
    } catch (error) {
      console.warn('Failed to update active users:', error.message);
    }
  }

  // Disable metrics collection (useful for development)
  disable() {
    this.isEnabled = false;
  }

  // Enable metrics collection
  enable() {
    this.isEnabled = true;
  }
}

// Vue plugin
export default {
  install(app, options = {}) {
    const metrics = new MetricsCollector();
    
    // Make metrics available globally
    app.config.globalProperties.$metrics = metrics;
    app.provide('metrics', metrics);
    
    // Make metrics available on window for axios interceptors
    if (typeof window !== 'undefined') {
      window.app = app;
    }

    // Track route changes automatically
    app.mixin({
      beforeRouteEnter(to, from, next) {
        next(() => {
          metrics.trackPageView(to.path);
        });
      },
      beforeRouteUpdate(to, from) {
        metrics.trackPageView(to.path);
      }
    });

    // Track initial page load
    if (typeof window !== 'undefined') {
      window.addEventListener('load', () => {
        metrics.trackPageView(window.location.pathname);
      });
    }
  }
};

export { MetricsCollector };
