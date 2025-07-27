import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import metricsPlugin from './plugins/metrics.js'

const app = createApp(App)

app.use(router)
app.use(metricsPlugin)

app.mount('#app')
