<script setup>
import Navbar from './components/Navbar.vue'
import Footer from './components/Footer.vue' // 1. Importas el Footer
</script>

<template>
  <div class="app-wrapper">
    <Navbar />
    
    <main class="main-content">
      <router-view />
    </main>

    <Footer /> </div>
</template>

<style>
/* 3. Lógica para que el Footer siempre esté al fondo si hay poco contenido */
.app-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.main-content {
  flex: 1; /* Esto empuja al footer hacia abajo */
}

/* Colores Globales */
body {
  margin: 0;
  background-color: #f5f5f5; /* Gris suave de fondo */
  color: #222; /* Texto principal */
}
</style>