import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import '@/assets/css/global.css'

const app = createApp(App)

app.use(store)
app.use(router)
app.use(ElementPlus)

// 添加全局属性
app.config.globalProperties.$baseUrl = process.env.VUE_APP_BASEURL

app.mount('#app')
