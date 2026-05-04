<script setup>
// Definimos qué datos necesita recibir este componente para funcionar
defineProps(["comunidad"]);
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
  background: var(--bg-card);
  border-radius: 15px;
  overflow: hidden;
  border: 1px solid var(--border);
  transition:
    transform 0.3s,
    border-color 0.3s;
  display: flex;
  flex-direction: column;
}

.comunidad-card:hover {
  transform: translateY(-8px);
  border-color: var(--primary);
}

.comunidad-card img {
  width: 100%;
  height: 160px;
  object-fit: cover;
  border-bottom: 1px solid var(--border);
}

.info {
  padding: 20px;
  text-align: center;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.info h3 {
  margin: 0 0 10px 0;
  color: var(--text-main);
  font-size: 1.2rem;
}

.info p {
  color: var(--text-muted);
  font-size: 0.9rem;
  margin: 0 0 15px 0;
}

.btn-ver {
  margin-top: auto;
  display: block;
  background: rgba(215, 24, 32, 0.1);
  color: var(--primary);
  text-decoration: none;
  font-weight: bold;
  padding: 10px;
  border-radius: 8px;
  transition:
    background 0.3s,
    color 0.3s;
}

.btn-ver:hover {
  background: var(--primary);
  color: white;
}
</style>
