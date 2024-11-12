import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    host: true,  // Equivalent to '0.0.0.0'
    port: 5173,  // Explicitly define port
    strictPort: true,  // Ensure the app fails if the port is already in use
    watch: {
      usePolling: true,  // Useful for Docker environments to avoid file change issues
    },
  },
})
