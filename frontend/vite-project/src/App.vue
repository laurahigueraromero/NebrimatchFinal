<script setup>
import { useRoute } from "vue-router";
import { computed } from "vue";
import Navbar from "./components/Navbar.vue";
import Footer from "./components/Footer.vue";

const route = useRoute();

// Computada que devuelve 'true' si estamos en una página donde NO queremos el menú de arriba
const esPaginaExterna = computed(() => {
  return route.path === '/' || 
         route.path === '/login' || 
         route.path.startsWith('/info/');
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

/* DEFINIMOS LAS VARIABLES DEL MODO OSCURO PARA TODA LA APP */
:root {
  --bg-main: #121212;        /* Fondo principal de la app */
  --bg-card: #1e1e1e;        /* Fondo de las tarjetas y cajas */
  --text-main: #ffffff;      /* Texto principal */
  --text-muted: #a0a0a0;     /* Texto secundario/descripciones */
  --primary: #d71820;        /* Rojo Nebrija */
  --primary-hover: #ff5e62;  /* Rojo claro para brillos */
  --border: rgba(255, 255, 255, 0.08); /* Bordes sutiles */
}

* {
  font-family: "Montserrat", sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  box-sizing: border-box;
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