<script setup>
import { ref, onMounted } from "vue";
import { comunidadService } from "../services/api";

const props = defineProps(["id"]);

const comunidad = ref(null);
const cargando = ref(true);
const error = ref(null);

onMounted(async () => {
  try {
    const respuesta = await comunidadService.obtenerPorId(props.id);
    comunidad.value = respuesta.data;
  } catch {
    error.value = "No se encontró la comunidad.";
  } finally {
    cargando.value = false;
  }
});
</script>

<template>
  <div class="detalle-container">
    <button @click="$router.back()">← Volver</button>

    <p v-if="cargando" class="estado">Cargando...</p>
    <p v-else-if="error" class="estado error">{{ error }}</p>

    <template v-else-if="comunidad">
      <div class="header">
        <img :src="comunidad.imagenComunidad" class="tech-logo" />
        <h1>{{ comunidad.nombreComunidad }}</h1>
      </div>
      <p class="desc">{{ comunidad.descripcionComunidad }}</p>
      <button class="btn-join">Unirme al repositorio</button>
    </template>
  </div>
</template>

<style scoped>
.detalle-container {
  max-width: 600px;
  margin: 40px auto;
  text-align: center;
  background: var(--bg-card);
  padding: 40px;
  border-radius: 20px;
  border: 1px solid var(--border);
  box-shadow: 0 10px 30px rgba(0,0,0,0.5);
}

.tech-logo {
  width: 120px;
  margin-bottom: 20px;
}

h1 {
  color: var(--text-main);
  margin-bottom: 10px;
}

.desc {
  color: var(--text-main);
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

.estado {
  color: var(--text-muted);
  margin-top: 20px;
}

.error {
  color: var(--primary);
}
</style>
