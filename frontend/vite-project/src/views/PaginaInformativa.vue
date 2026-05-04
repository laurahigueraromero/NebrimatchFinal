<script setup>
import { computed } from "vue";
import { contenidoPaginas } from "../data/datosFooter";

// Recibimos el "slug" desde la URL (ej: "condiciones" o "cookies")
const props = defineProps(["slug"]);

// Buscamos los datos exactos en nuestro objeto
const info = computed(() => contenidoPaginas[props.slug]);
</script>

<template>
  <div class="legal-page">
    <header class="landing-header">
      <router-link to="/" class="logo">Nebri<span>Match</span></router-link>
      <router-link to="/" class="btn-volver">Volver al inicio</router-link>
    </header>

    <main class="legal-content" v-if="info">
      <h1>{{ info.titulo }}</h1>
      <p class="fecha">Última actualización: {{ info.fecha }}</p>

      <div class="texto-legal">
        <!-- Bucle automático para los párrafos -->
        <div
          v-for="(bloque, index) in info.bloques"
          :key="index"
          class="bloque"
        >
          <h2>{{ bloque.subtitulo }}</h2>
          <p>{{ bloque.texto }}</p>
        </div>
      </div>
    </main>

    <main class="legal-content" v-else>
      <h1>Página no encontrada</h1>
      <p>La información que buscas no existe.</p>
    </main>
  </div>
</template>

<style scoped>
/* Exactamente el mismo CSS que pusimos antes en Privacidad.vue */
.legal-page {
  background: #111111;
  min-height: 100vh;
  color: white;
  display: flex;
  flex-direction: column;
}
.landing-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 5%;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}
.logo {
  font-size: 1.5rem;
  font-weight: bold;
  color: #d71820;
  text-decoration: none;
}
.logo span {
  color: white;
}
.btn-volver {
  color: #aaa;
  text-decoration: none;
  font-weight: bold;
  transition: color 0.3s;
}
.btn-volver:hover {
  color: white;
}
.legal-content {
  max-width: 800px;
  margin: 0 auto;
  padding: 60px 20px;
  flex: 1;
}
h1 {
  font-size: 2.5rem;
  margin-bottom: 10px;
}
.fecha {
  color: #666;
  margin-bottom: 40px;
  font-style: italic;
}
.bloque h2 {
  color: #d71820;
  margin-top: 40px;
  margin-bottom: 15px;
  font-size: 1.5rem;
}
.bloque p {
  color: #ccc;
  line-height: 1.8;
  font-size: 1.05rem;
  margin-bottom: 20px;
}
</style>
