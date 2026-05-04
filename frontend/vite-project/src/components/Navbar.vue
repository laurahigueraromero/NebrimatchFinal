<template>
  <nav class="navbar">
    <div class="logo">Nebri<span>Match</span></div>
    <div class="links">
      <router-link to="/comunidades">Comunidades</router-link>
      <router-link to="/para-ti">Descubrir</router-link>
      <router-link to="/chats">Mensajes</router-link>
      <router-link to="/perfil">Mi Perfil</router-link>
    </div>
  </nav>
</template>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 40px;
  background-color: #0a0a0a; /* Muy oscuro para diferenciarlo del fondo */
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

.links a {
  margin-left: 25px;
  text-decoration: none;
  color: var(--text-muted);
  font-weight: 600;
  font-size: 0.95rem;
  transition: color 0.3s;
}

.links a:hover {
  color: var(--text-main);
}

/* El enlace en el que estamos actualmente se pone rojo */
.router-link-active {
  color: var(--primary) !important;
}
</style>