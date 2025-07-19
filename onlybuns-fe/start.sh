#!/bin/sh
echo "Starting metrics server..."
node metrics-server.js &
METRICS_PID=$!
echo "Metrics server started with PID $METRICS_PID"
echo "Starting Vite dev server..."
npm run dev -- --host
