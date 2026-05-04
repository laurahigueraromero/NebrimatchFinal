<script setup>
import { columnasFooter } from "../data/datosFooter";
</script>

<template>
  <footer class="main-footer">
    <div class="footer-columns">
      <!-- Bucle de columnas -->
      <div
        class="footer-col"
        v-for="(columna, index) in columnasFooter"
        :key="index"
      >
        <h4>{{ columna.titulo }}</h4>
        <router-link
          v-for="enlace in columna.enlaces"
          :key="enlace.slug"
          :to="'/info/' + enlace.slug"
        >
          {{ enlace.nombre }}
        </router-link>
      </div>

      <!-- Columna estática de redes -->
      <div class="footer-col">
        <h4>Redes sociales</h4>
        <div class="social-icons">
          <a href="#" title="Instagram"
            ><i class="fa-brands fa-instagram"></i
          ></a>
          <a href="#" title="TikTok"><i class="fa-brands fa-tiktok"></i></a>
          <a href="#" title="YouTube"><i class="fa-brands fa-youtube"></i></a>
          <a href="#" title="X (Twitter)"
            ><i class="fa-brands fa-x-twitter"></i
          ></a>
          <a href="#" title="GitHub"><i class="fa-brands fa-github"></i></a>
        </div>
      </div>
    </div>

    <div class="footer-bottom-bar">
      <p>© 2026 NebriMatch - TFG Universidad Nebrija</p>
    </div>
  </footer>
</template>

<style scoped>
.main-footer {
  background: #000000; /* Fondo negro puro */
  border-top: 1px solid var(--border);
  padding: 60px 5% 20px 5%;
  margin-top: auto;
}

.footer-columns {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 40px;
  max-width: 1200px;
  margin: 0 auto;
  margin-bottom: 40px;
}

.footer-col h4 {
  color: white;
  font-size: 1.1rem;
  margin-bottom: 20px;
}

.footer-col a {
  display: block;
  color: var(--text-muted);
  text-decoration: none;
  margin-bottom: 12px;
  font-size: 0.95rem;
  transition: color 0.2s;
}

.footer-col a:hover {
  color: var(--primary); /* Rojo Nebrija al pasar el ratón */
}

.social-icons {
  display: flex;
  gap: 15px;
}

.social-icons a {
  font-size: 1.5rem;
  filter: grayscale(100%);
}
.social-icons a:hover {
  filter: grayscale(0%);
}

.footer-bottom-bar {
  border-top: 1px solid var(--border);
  padding-top: 20px;
  text-align: center;
  color: #666;
  font-size: 0.85rem;
}
</style>
