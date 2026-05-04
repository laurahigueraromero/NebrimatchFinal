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
  background: var(--bg-card); /* Magia: Fondo de tarjeta oscuro */
  padding: 40px; 
  border-radius: 20px; 
  border: 1px solid var(--border); /* Borde sutil */
  box-shadow: 0 10px 30px rgba(0,0,0,0.5); 
}

.tech-logo { 
  width: 120px; 
  margin-bottom: 20px; 
}

h1 {
  color: var(--text-main); /* Forzamos título en blanco */
  margin-bottom: 10px;
}

.stats { 
  color: var(--text-muted); /* Gris clarito para los miembros */
  font-weight: 600;
}

.desc { 
  color: var(--text-main); /* Blanco para la descripción */
  line-height: 1.6;
  margin-top: 20px;
}

.btn-join { 
  background: var(--primary); 
  color: white; 
  border: none; 
  padding: 15px 40px; 
  border-radius: 10px; 
  font-weight: bold; 
  cursor: pointer; 
  margin-top: 30px; 
  width: 100%; 
  transition: transform 0.2s;
}

.btn-join:hover {
  transform: scale(1.02);
}

button { 
  color: var(--primary); 
  border: none; 
  background: none; 
  cursor: pointer; 
  font-weight: bold; 
}
</style>
