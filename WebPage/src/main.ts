import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import piniaStore from './store'
import './index.css'
import ArcoVue from "@arco-design/web-vue"
// 额外引入图标库
import ArcoVueIcon from '@arco-design/web-vue/es/icon';
import '@arco-design/web-vue/dist/arco.css';
// 支持SVG
import 'virtual:svg-icons-register'

import { Message } from '@arco-design/web-vue';
const app = createApp(App)
Message._context = app._context;
app.use(router).use(ArcoVue).use(ArcoVueIcon).use(piniaStore).mount('#app')
