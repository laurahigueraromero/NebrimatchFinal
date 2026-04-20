<script setup>
import { listaComunidades } from "../data/comunidades"; // La misma fuente de datos
const props = defineProps(["id"]);

// Buscamos la comunidad correcta por ID
const comunidad = listaComunidades.find((c) => c.id === Number(props.id));
</script>

<template>
  <div v-if="comunidad" class="detalle-container">
    <button @click="$router.back()">← Volver</button>
    <div class="header">
      <img :src="comunidad.imagen" class="tech-logo" />
      <h1>{{ comunidad.nombre }}</h1>
    </div>
    <p class="stats">👥 {{ comunidad.miembros }} programadores unidos</p>
    <p class="desc">{{ comunidad.descripcion }}</p>
    <button class="btn-join">Unirme al repositorio</button>
  </div>
</template>

<style scoped>
.detalle-container {
  max-width: 600px;
  margin: 40px auto;
  text-align: center;
  background: white;
  padding: 40px;
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
}
.tech-logo {
  width: 120px;
  margin-bottom: 20px;
}
.btn-join {
  background: #d71820;
  color: white;
  border: none;
  padding: 15px 40px;
  border-radius: 10px;
  font-weight: bold;
  cursor: pointer;
  margin-top: 30px;
  width: 100%;
}
button {
  color: #d71820;
  border: none;
  background: none;
  cursor: pointer;
  font-weight: bold;
}
</style>
