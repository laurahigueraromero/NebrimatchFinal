<script setup>
// ¡Qué limpio queda esto ahora!
</script>

<template>
  <div class="landing-page">
    <!-- CABECERA DE LA LANDING -->
    <header class="landing-header">
      <div class="logo">Nebri<span>Match</span></div>
      <router-link to="/login" class="btn-login-outline"
        >Iniciar Sesión</router-link
      >
    </header>

    <main class="main-content">
      <!-- SECCIÓN HERO -->
      <section class="hero-section">
        <h1 class="title">Haz Match con tu futuro código.</h1>
        <p class="subtitle">
          Únete a la mayor comunidad de programadores de la Universidad Nebrija.
          Encuentra a tu mentor, únete a proyectos y sube de nivel.
        </p>
        <router-link to="/login" class="btn-primary">Empezar ahora</router-link>
      </section>

      <!-- SECCIÓN DE CARACTERÍSTICAS -->
      <section class="info-section">
        <h2 class="section-title">¿Cómo funciona NebriMatch?</h2>

        <div class="cards-grid">
          <div class="info-card">
            <div class="icon">🎯</div>
            <h3>El Tinder del Código</h3>
            <p>
              Haz swipe entre perfiles de mentores y alumnos. Filtra por
              lenguajes como Vue, Java o Python y encuentra a tu compañero ideal
              de programación.
            </p>
          </div>

          <div class="info-card">
            <div class="icon">💬</div>
            <h3>Chat en Tiempo Real</h3>
            <p>
              ¿Hubo Match? ¡Genial! Se abrirá un chat privado para que podáis
              compartir repositorios de GitHub, resolver dudas y organizar
              mentorías.
            </p>
          </div>

          <div class="info-card">
            <div class="icon">🌐</div>
            <h3>Comunidades Tech</h3>
            <p>
              No estás solo. Únete a más de 50 comunidades temáticas exclusivas
              de la universidad para debatir sobre IA, Ciberseguridad o
              Desarrollo Web.
            </p>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<style scoped>
/* Estilos Generales Landing */
.landing-page {
  background: linear-gradient(135deg, #111111 0%, #000000 100%);
  display: flex;
  flex-direction: column;
  color: white;
  /* Quitamos el min-height: 100vh de aquí porque ahora lo gestiona App.vue */
}

/* Header */
.landing-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 5%;
}
.logo {
  font-size: 2rem;
  font-weight: bold;
  color: var(--primary);
}
.logo span {
  color: white;
}
.btn-login-outline {
  padding: 10px 25px;
  background: transparent;
  color: white;
  border: 2px solid white;
  border-radius: 30px;
  text-decoration: none;
  font-weight: bold;
  transition: all 0.3s;
}
.btn-login-outline:hover {
  background: white;
  color: #1a1a1a;
}

/* Hero */
.hero-section {
  text-align: center;
  padding: 80px 20px 100px 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.title {
  font-size: 4.5rem;
  font-weight: 800;
  margin-bottom: 20px;
  max-width: 900px;
  line-height: 1.1;
  background: linear-gradient(to right, var(--primary), #ff5e62);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}
.subtitle {
  font-size: 1.2rem;
  color: #aaa;
  max-width: 600px;
  margin-bottom: 40px;
  line-height: 1.6;
}
.btn-primary {
  padding: 15px 40px;
  background: linear-gradient(to right, var(--primary), #b5141a);
  color: white;
  text-decoration: none;
  font-size: 1.2rem;
  font-weight: bold;
  border-radius: 30px;
  transition: transform 0.2s;
  box-shadow: 0 4px 15px rgba(215, 24, 32, 0.4);
}
.btn-primary:hover {
  transform: scale(1.05);
}

/* Cajitas */
.info-section {
  padding: 40px 5% 100px 5%;
  text-align: center;
}
.section-title {
  font-size: 2.5rem;
  margin-bottom: 50px;
  color: #fff;
}
.cards-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
  max-width: 1200px;
  margin: 0 auto;
}
.info-card {
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.1);
  padding: 40px 30px;
  border-radius: 20px;
  text-align: left;
  transition:
    transform 0.3s,
    background 0.3s;
}
.info-card:hover {
  transform: translateY(-10px);
  background: rgba(255, 255, 255, 0.05);
  border-color: rgba(215, 24, 32, 0.5);
}
.icon {
  font-size: 3rem;
  margin-bottom: 20px;
}
.info-card h3 {
  font-size: 1.5rem;
  margin-bottom: 15px;
  color: #fff;
}
.info-card p {
  color: #aaa;
  line-height: 1.6;
}
</style>
