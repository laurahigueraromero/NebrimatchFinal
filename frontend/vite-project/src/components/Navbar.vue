<script setup>
// Solo importamos el nuevo componente
import ThemeToggle from "./ThemeToggle.vue";
</script>

<template>
  <nav class="navbar">
    <div class="logo">Nebri<span>Match</span></div>

    <div class="links">
      <router-link to="/comunidades">Comunidades</router-link>
      <router-link to="/para-ti">Descubrir</router-link>
      <router-link to="/chats">Mensajes</router-link>
      <router-link to="/perfil">Mi Perfil</router-link>

      <!-- ENCHUFAMOS LA PALANQUITA AQUÍ -->
      <ThemeToggle class="toggle-spacing" />
    </div>
  </nav>
</template>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 40px;
  background-color: var(--bg-card);
  border-bottom: 1px solid var(--border);
  position: sticky;
  top: 0;
  z-index: 100;
}

.logo {
  font-size: 1.5rem;
  font-weight: bold;
  color: var(--primary);
}
.logo span {
  color: var(--text-main);
}

.links {
  display: flex;
  align-items: center;
}

.links a {
  margin-left: 25px;
  text-decoration: none;
  color: var(--text-muted);
  font-weight: 600;
  font-size: 0.95rem;
  transition: color 0.3s;
}

.links a:hover,
.router-link-active {
  color: var(--primary);
}

/* Espacio extra para separar la palanquita de los enlaces */
.toggle-spacing {
  margin-left: 30px;
}
</style>
