<template>
  <nav class="navbar">
    <div class="logo">NebriMatch</div>
    <div class="links">
      <router-link to="/para-ti">Descubrir</router-link>
      
      <router-link to="/comunidades">Comunidades</router-link> 
      
      <router-link to="/chats">Mensajes</router-link>
      <router-link to="/perfil">Mi Perfil</router-link>
    </div>
  </nav>
</template>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  padding: 1rem 2rem;
  background: white;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}
.links a {
  margin-left: 20px;
  text-decoration: none;
  color: #666;
  font-weight: bold;
}
.router-link-active {
  color: #d71820 !important; /* El color de Nebrija para la página activa */
}
</style>