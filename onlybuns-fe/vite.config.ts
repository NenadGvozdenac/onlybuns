import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    host: true,  // Equivalent to '0.0.0.0'
    port: 5173,  // Explicitly define port
    strictPort: true,  // Ensure the app fails if the port is already in use
    watch: {
      usePolling: true,  // Useful for Docker environments to avoid file change issues
    },
  },
})
