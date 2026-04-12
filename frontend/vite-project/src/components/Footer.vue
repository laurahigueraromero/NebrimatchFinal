<template>
  <footer class="main-footer">
    <div class="footer-content">
      <div class="footer-section">
        <h3 class="footer-logo">Nebri<span>Match</span></h3>
        <p>Conectando mentores y alumnos de la Universidad Nebrija.</p>
      </div>

      <div class="footer-section">
        <h4>TFG 2026</h4>
        <p>© Todos los derechos reservados</p>
      </div>
    </div>
    <div class="footer-bottom">
      <p>Desarrollado con Vue 3 + Spring Boot</p>
    </div>
  </footer>
</template>

<style scoped>
.main-footer {
  background-color: #ffffff;
  border-top: 1px solid #eee;
  padding: 40px 20px 20px 20px;
  color: #444;
  margin-top: auto; /* Esto ayuda a que se quede abajo */
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 20px;
}

.footer-section {
  flex: 1;
  min-width: 250px;
}

.footer-logo {
  color: #d71820; /* Rojo Nebrija */
  font-size: 1.5rem;
  margin-bottom: 10px;
}

.footer-logo span {
  color: #222;
  font-weight: 300;
}

.footer-section h4 {
  margin-bottom: 15px;
  text-transform: uppercase;
  font-size: 0.9rem;
  letter-spacing: 1px;
}

.footer-bottom {
  text-align: center;
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
  font-size: 0.8rem;
  color: #999;
}
</style>