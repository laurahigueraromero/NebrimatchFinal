<script setup>
import { ref, computed } from "vue";
import { listaComunidades } from "../data/comunidades"; // Importamos la lista real
import ComunidadCard from "../components/ComunidadCard.vue";

const searchQuery = ref("");

const comunidadesFiltradas = computed(() => {
  return listaComunidades.filter((c) =>
    c.nombre.toLowerCase().includes(searchQuery.value.toLowerCase()),
  );
});
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
    <div class="container">
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
</style>
