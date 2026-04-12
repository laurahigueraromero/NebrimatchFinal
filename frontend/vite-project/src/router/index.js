import { createRouter, createWebHistory } from 'vue-router'
import ParaTi from '../views/ParaTi.vue'
import Login from '../views/Login.vue'

const routes = [
  { path: '/', component: Login },
  { path: '/para-ti', component: ParaTi },
  // Aquí es donde meterías el id dinámico para el chat más adelante
  // { path: '/chat/:id', component: Chat }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router