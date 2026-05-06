<script setup>
import { useRoute } from "vue-router";
import { computed } from "vue";
import Navbar from "./components/Navbar.vue";
import Footer from "./components/Footer.vue";

const route = useRoute();

// Computada que devuelve 'true' si estamos en una página donde NO queremos el menú de arriba
const esPaginaExterna = computed(() => {
  return (
    route.path === "/" ||
    route.path === "/login" ||
    route.path.startsWith("/info/")
  );
});
</script>

<template>
  <div class="app-wrapper">
    <!-- El Navbar solo sale si estamos dentro de la aplicación -->
    <Navbar v-if="!esPaginaExterna" />

    <main class="main-content">
      <router-view />
    </main>

    <!-- El Footer AHORA se muestra siempre, en todas las páginas -->
    <Footer />
  </div>
</template>

<style>
@import url("https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap");

/* 1. MODO OSCURO (Por defecto) */
:root {
  --bg-main: #121212;
  --bg-card: #1e1e1e;
  --text-main: #ffffff;
  --text-muted: #a0a0a0;
  --primary: #d71820;
  --primary-hover: #ff5e62;
  --border: rgba(255, 255, 255, 0.08);
}

/* 2. MODO CLARO (Se activa con la palanquita) */
body.light-theme {
  --bg-main: #f5f5f5; /* Fondo gris súper clarito */
  --bg-card: #ffffff; /* Tarjetas blancas puras */
  --text-main: #222222; /* Texto casi negro para leer bien */
  --text-muted: #666666; /* Gris oscuro para descripciones */
  --primary: #d71820; /* El rojo Nebrija se queda igual */
  --primary-hover: #b5141a;
  --border: rgba(0, 0, 0, 0.1); /* Bordes grises sutiles */
}

/* El resto de estilos globales se quedan igual */
* {
  font-family: "Montserrat", sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  box-sizing: border-box;
  transition:
    background-color 0.3s ease,
    color 0.3s ease,
    border-color 0.3s ease; /* Transición súper suave al cambiar de modo */
}

body {
  margin: 0;
  background-color: var(--bg-main);
  color: var(--text-main);
}

.app-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.main-content {
  flex: 1;
}
</style>
