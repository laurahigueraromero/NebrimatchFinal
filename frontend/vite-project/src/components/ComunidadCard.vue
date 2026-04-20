<script setup>
// Definimos qué datos necesita recibir este componente para funcionar
defineProps(['comunidad']);
</script>

<template>
  <div class="comunidad-card">
    <img :src="comunidad.imagen" alt="Imagen comunidad" />
    <div class="info">
      <h3>{{ comunidad.nombre }}</h3>
      <p>{{ comunidad.miembros }} miembros</p>
      <router-link :to="'/comunidades/' + comunidad.id" class="btn-ver">
        Ver detalles
      </router-link>
    </div>
  </div>
</template>

<style scoped>
.comunidad-card {
  background: white;
  border-radius: 15px;
  overflow: hidden;
  box-shadow: 0 4px 15px rgba(0,0,0,0.1);
  transition: transform 0.3s;
}
.comunidad-card:hover { transform: translateY(-5px); }
.comunidad-card img { width: 100%; height: 150px; object-fit: cover; }
.info { padding: 15px; text-align: center; }
.btn-ver {
  display: inline-block;
  margin-top: 10px;
  color: #d71820;
  text-decoration: none;
  font-weight: bold;
}
</style>