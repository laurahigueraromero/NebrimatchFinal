<script setup>
import { ref } from "vue";

// 1. TUS DATOS (El perfil principal)
const miPerfil = ref({
  nombre: "Espe",
  rol: "Estudiante",
  avatar:
    "https://ui-avatars.com/api/?name=Espe&background=d71820&color=fff&size=200",
  bio: "Estudiante de ingeniería en la Nebrija. Apasionada por el diseño de interfaces y buscando dominar el frontend para mi TFG.",
  lenguajes: ["Vue.js", "JavaScript", "HTML/CSS"],
  competencias: ["Diseño UX/UI", "Figma", "Trabajo en equipo"],
});

// 2. DATOS FALSOS PARA LA PASARELA (Sugerencias)
const sugerencias = ref([
  {
    id: 1,
    nombre: "Rodrigo",
    rol: "Mentor",
    avatar:
      "https://ui-avatars.com/api/?name=Rodrigo&background=0D8ABC&color=fff",
    bio: "Senior Frontend Developer. Te ayudo a estructurar tu proyecto en Vue y React.",
    lenguajes: ["Vue.js", "React", "TypeScript"],
    competencias: ["Arquitectura", "Clean Code"],
  },
  {
    id: 2,
    nombre: "Laura",
    rol: "Mentor",
    avatar:
      "https://ui-avatars.com/api/?name=Laura&background=4CAF50&color=fff",
    bio: "Experta en backend y arquitecturas de bases de datos.",
    lenguajes: ["Java", "Spring Boot", "SQL"],
    competencias: ["Bases de datos", "APIs REST"],
  },
  {
    id: 3,
    nombre: "Carlos",
    rol: "Estudiante",
    avatar:
      "https://ui-avatars.com/api/?name=Carlos&background=FF9800&color=fff",
    bio: "Buscando compañero para montar un proyecto de IA.",
    lenguajes: ["Python", "C++"],
    competencias: ["Data Science", "Machine Learning"],
  },
]);

// 3. ESTADO DEL MODAL
const perfilSeleccionado = ref(null);

const abrirModal = (perfil) => {
  perfilSeleccionado.value = perfil;
};

const cerrarModal = () => {
  perfilSeleccionado.value = null;
};
</script>

<template>
  <div class="perfil-page">
    <div class="perfil-layout">
      <section class="main-profile">
        <div class="card mi-tarjeta">
          <div class="header-tarjeta">
            <img :src="miPerfil.avatar" alt="Mi Avatar" class="avatar-grande" />
            <div class="info-basica">
              <h1>{{ miPerfil.nombre }}</h1>
              <span class="badge-rol" :class="miPerfil.rol.toLowerCase()">{{
                miPerfil.rol
              }}</span>
            </div>
          </div>

          <div class="bio-section">
            <h3>Sobre mí</h3>
            <p>{{ miPerfil.bio }}</p>
          </div>

          <div class="tags-section">
            <h3>
              {{
                miPerfil.rol === "Mentor"
                  ? "Lenguajes que enseño"
                  : "Lenguajes que quiero aprender"
              }}
            </h3>
            <div class="tags-container">
              <span
                v-for="lang in miPerfil.lenguajes"
                :key="lang"
                class="tag lang-tag"
                >{{ lang }}</span
              >
            </div>
          </div>

          <div class="tags-section">
            <h3>Competencias</h3>
            <div class="tags-container">
              <span
                v-for="comp in miPerfil.competencias"
                :key="comp"
                class="tag comp-tag"
                >{{ comp }}</span
              >
            </div>
          </div>

          <button class="btn-editar">Editar Perfil</button>
        </div>
      </section>

      <section class="sugerencias-sidebar">
        <h2>Perfiles que quizás te interesen</h2>

        <div class="pasarela">
          <div
            v-for="perfil in sugerencias"
            :key="perfil.id"
            class="sugerencia-card"
            @click="abrirModal(perfil)"
          >
            <img :src="perfil.avatar" alt="Avatar" class="avatar-pequeno" />
            <div class="sugerencia-info">
              <h4>{{ perfil.nombre }}</h4>
              <p class="rol-text">{{ perfil.rol }}</p>
              <p class="habilidades-preview">
                {{ perfil.lenguajes.slice(0, 2).join(", ") }}
              </p>
            </div>
          </div>
        </div>
      </section>
    </div>

    <div
      v-if="perfilSeleccionado"
      class="modal-overlay"
      @click.self="cerrarModal"
    >
      <div class="modal-card card">
        <button class="btn-cerrar" @click="cerrarModal">✕</button>

        <img :src="perfilSeleccionado.avatar" class="avatar-grande center" />
        <h2 class="text-center">{{ perfilSeleccionado.nombre }}</h2>
        <div class="text-center mb-20">
          <span
            class="badge-rol"
            :class="perfilSeleccionado.rol.toLowerCase()"
            >{{ perfilSeleccionado.rol }}</span
          >
        </div>

        <p class="modal-bio">{{ perfilSeleccionado.bio }}</p>

        <div class="tags-section">
          <h3>Lenguajes</h3>
          <div class="tags-container">
            <span
              v-for="lang in perfilSeleccionado.lenguajes"
              :key="lang"
              class="tag lang-tag"
              >{{ lang }}</span
            >
          </div>
        </div>

        <div class="tags-section">
          <h3>Competencias</h3>
          <div class="tags-container">
            <span
              v-for="comp in perfilSeleccionado.competencias"
              :key="comp"
              class="tag comp-tag"
              >{{ comp }}</span
            >
          </div>
        </div>

        <button class="btn-primary mt-20">
          Enviar Mensaje a {{ perfilSeleccionado.nombre }}
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.perfil-page {
  padding: 40px 5%;
  min-height: 85vh;
}

/* LAYOUT A DOS COLUMNAS */
.perfil-layout {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 40px;
  max-width: 1200px;
  margin: 0 auto;
}

/* Para móviles, una columna debajo de otra */
@media (max-width: 850px) {
  .perfil-layout {
    grid-template-columns: 1fr;
  }
}

/* ESTILOS DE TARJETAS COMUNES */
.card {
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
}

.mi-tarjeta {
  display: flex;
  flex-direction: column;
  gap: 25px;
}

.header-tarjeta {
  display: flex;
  align-items: center;
  gap: 25px;
  border-bottom: 1px solid var(--border);
  padding-bottom: 25px;
}

.avatar-grande {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
  border: 4px solid var(--primary);
}

.avatar-grande.center {
  margin: 0 auto 15px auto;
  display: block;
}

.info-basica h1 {
  margin: 0 0 10px 0;
  color: var(--text-main);
  font-size: 2.5rem;
}

/* ETIQUETAS DE ROL */
.badge-rol {
  padding: 6px 15px;
  border-radius: 20px;
  font-weight: bold;
  font-size: 0.9rem;
  color: white;
}
.badge-rol.estudiante {
  background: #0d8abc;
}
.badge-rol.mentor {
  background: var(--primary);
}

h3 {
  color: var(--text-main);
  margin: 0 0 10px 0;
  font-size: 1.2rem;
}
p {
  color: var(--text-muted);
  line-height: 1.6;
  margin: 0;
}

/* ETIQUETAS (TAGS) */
.tags-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
.tag {
  padding: 8px 15px;
  border-radius: 10px;
  font-size: 0.9rem;
  font-weight: 600;
}
.lang-tag {
  background: rgba(215, 24, 32, 0.1);
  color: var(--primary);
  border: 1px solid var(--primary);
}
.comp-tag {
  background: var(--bg-main);
  color: var(--text-muted);
  border: 1px solid var(--border);
}

.btn-editar {
  background: transparent;
  color: var(--text-main);
  border: 2px solid var(--border);
  padding: 15px;
  border-radius: 12px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s;
  margin-top: 10px;
}
.btn-editar:hover {
  border-color: var(--primary);
  color: var(--primary);
}

/* --- COLUMNA DERECHA (PASARELA) --- */
.sugerencias-sidebar h2 {
  color: var(--text-main);
  font-size: 1.5rem;
  margin-bottom: 20px;
}

.pasarela {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.sugerencia-card {
  display: flex;
  align-items: center;
  gap: 15px;
  background: var(--bg-card);
  padding: 15px;
  border-radius: 15px;
  border: 1px solid var(--border);
  cursor: pointer;
  transition:
    transform 0.2s,
    border-color 0.2s;
}
.sugerencia-card:hover {
  transform: translateX(-5px);
  border-color: var(--primary);
}

.avatar-pequeno {
  width: 60px;
  height: 60px;
  border-radius: 50%;
}
.sugerencia-info h4 {
  margin: 0;
  color: var(--text-main);
  font-size: 1.1rem;
}
.rol-text {
  font-size: 0.85rem;
  font-weight: bold;
  color: var(--primary);
  margin: 3px 0;
}
.habilidades-preview {
  font-size: 0.85rem;
  color: var(--text-muted);
}

/* --- MODAL DIFUMINADO (El efecto mágico) --- */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(8px); /* ESTO HACE EL EFECTO DIFUMINADO */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  padding: 20px;
}

.modal-card {
  width: 100%;
  max-width: 500px;
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 20px;
  animation: popIn 0.3s ease;
}

@keyframes popIn {
  0% {
    transform: scale(0.9);
    opacity: 0;
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}

.btn-cerrar {
  position: absolute;
  top: 20px;
  right: 20px;
  background: transparent;
  border: none;
  color: var(--text-muted);
  font-size: 1.5rem;
  cursor: pointer;
}
.btn-cerrar:hover {
  color: var(--primary);
}

.text-center {
  text-align: center;
}
.mb-20 {
  margin-bottom: 20px;
}
.mt-20 {
  margin-top: 20px;
}

.modal-bio {
  text-align: center;
  font-style: italic;
}

.btn-primary {
  width: 100%;
  padding: 15px;
  background: linear-gradient(to right, var(--primary), #b5141a);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 1.1rem;
  font-weight: bold;
  cursor: pointer;
}
</style>
