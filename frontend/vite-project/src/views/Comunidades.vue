<script setup>
import { ref, computed, onMounted } from "vue";
import { comunidadService } from "../services/api";
import ComunidadCard from "../components/ComunidadCard.vue";

const searchQuery = ref("");
const comunidades = ref([]);
const cargando = ref(true);
const error = ref(null);

onMounted(async () => {
  try {
    const respuesta = await comunidadService.obtenerTodas();
    comunidades.value = respuesta.data;
  } catch {
    error.value = "No se pudieron cargar las comunidades. ¿Está el servidor arrancado?";
  } finally {
    cargando.value = false;
  }
});

const comunidadesFiltradas = computed(() =>
  comunidades.value.filter((c) =>
    c.nombreComunidad.toLowerCase().includes(searchQuery.value.toLowerCase()),
  ),
);
</script>

<template>
  <div class="comunidades-page">
    <h1>Comunidades de Programación</h1>
    <input
      v-model="searchQuery"
      type="text"
      placeholder="🔍 Buscar tecnología..."
      class="search-input"
    />
    <p v-if="cargando" class="estado">Cargando comunidades...</p>
    <p v-else-if="error" class="estado error">{{ error }}</p>
    <div v-else class="container">
      <ComunidadCard
        v-for="c in comunidadesFiltradas"
        :key="c.id"
        :comunidad="c"
      />
    </div>
  </div>
</template>

<style scoped>
.comunidades-page {
  padding: 40px;
  text-align: center;
}
.search-input {
  width: 100%;
  max-width: 500px;
  padding: 15px;
  border-radius: 25px;
  border: 2px solid #ddd;
  margin-bottom: 30px;
  font-size: 1rem;
}
.container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}
.search-input {
  background-color: var(--bg-card);
  color: var(--text-main);
  border: 1px solid var(--border);
}
.estado {
  color: var(--text-muted);
  margin-top: 40px;
  font-size: 1rem;
}
.error {
  color: var(--primary);
}
</style>
