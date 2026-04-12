import { createRouter, createWebHistory } from "vue-router";
import ParaTi from "../views/ParaTi.vue";
import Login from "../views/Login.vue";

const routes = [
  { path: "/", component: Login },
  { path: "/para-ti", component: ParaTi },
  { path: "/chats", component: () => import("../views/Chats.vue") },
  { path: "/perfil", component: () => import("../views/Perfil.vue") },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
