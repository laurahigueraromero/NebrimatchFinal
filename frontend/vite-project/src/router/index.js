import { createRouter, createWebHistory } from "vue-router";
import ParaTi from "../views/ParaTi.vue";
import Login from "../views/Login.vue";

const routes = [
  { path: "/", component: Login },
  { path: "/para-ti", component: ParaTi },
  { path: "/chats", component: () => import("../views/Chats.vue") },
  { path: "/perfil", component: () => import("../views/Perfil.vue") },
  {
    path: "/comunidades",
    component: () => import("../views/Comunidades.vue"),
  },
  {
    path: "/comunidades/:id",
    component: () => import("../views/DetalleComunidad.vue"),
    props: true, // Esto permite recibir el ID como una prop
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
