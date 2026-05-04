import { createRouter, createWebHistory } from "vue-router";
import ParaTi from "../views/ParaTi.vue";
import Login from "../views/Login.vue";

const routes = [
  // Páginas externas (sin menú)
  { path: "/", component: () => import("../views/Landing.vue") },
  { path: "/login", component: Login },
  
  // La ruta dinámica para todas las páginas del Footer (Privacidad, Condiciones, etc.)
  { 
    path: "/info/:slug", 
    component: () => import("../views/PaginaInformativa.vue"),
    props: true 
  },

  // Páginas internas de la App (con menú)
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
    props: true,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;