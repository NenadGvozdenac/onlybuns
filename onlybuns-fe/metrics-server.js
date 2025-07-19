import express from 'express';
import { register, collectDefaultMetrics, Counter, Histogram, Gauge } from 'prom-client';

const app = express();
const PORT = 9091; // Different port for metrics

// Collect default metrics (CPU, memory, etc.)
collectDefaultMetrics();

// Custom metrics for the frontend
const httpRequestsTotal = new Counter({
  name: 'frontend_http_requests_total',
  help: 'Total number of HTTP requests made by the frontend',
  labelNames: ['method', 'route', 'status_code']
});

const httpRequestDuration = new Histogram({
  name: 'frontend_http_request_duration_seconds',
  help: 'Duration of HTTP requests in seconds',
  labelNames: ['method', 'route'],
  buckets: [0.1, 0.5, 1, 2, 5]
});

const activeUsers = new Gauge({
  name: 'frontend_active_users',
  help: 'Number of active users on the frontend'
});

const pageViews = new Counter({
  name: 'frontend_page_views_total',
  help: 'Total number of page views',
  labelNames: ['page']
});

// Health check endpoint
app.get('/health', (req, res) => {
  res.status(200).json({ status: 'healthy', timestamp: new Date().toISOString() });
});

// Metrics endpoint
app.get('/metrics', async (req, res) => {
  try {
    res.set('Content-Type', register.contentType);
    const metrics = await register.metrics();
    res.end(metrics);
  } catch (error) {
    res.status(500).end(error.message);
  }
});

// API endpoint to increment page views (can be called from frontend)
app.post('/metrics/pageview', express.json(), (req, res) => {
  const { page } = req.body;
  if (page) {
    pageViews.inc({ page });
  }
  res.status(200).json({ success: true });
});

// API endpoint to update active users
app.post('/metrics/activeusers', express.json(), (req, res) => {
  const { count } = req.body;
  if (typeof count === 'number') {
    activeUsers.set(count);
  }
  res.status(200).json({ success: true });
});

// API endpoint to record HTTP request metrics
app.post('/metrics/httprequest', express.json(), (req, res) => {
  const { method, route, status_code, duration } = req.body;
  if (method && route && status_code) {
    httpRequestsTotal.inc({ method, route, status_code });
  }
  if (method && route && duration) {
    httpRequestDuration.observe({ method, route }, duration);
  }
  res.status(200).json({ success: true });
});

app.listen(PORT, '0.0.0.0', () => {
  console.log(`Frontend metrics server running on port ${PORT}`);
});
