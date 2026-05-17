<script setup>
import { ref, computed, onMounted } from "vue";
import { usuarioService, matchService } from "../services/api";

const usuarioActual = JSON.parse(sessionStorage.getItem("usuario"));

const usuarios = ref([]);
const currentIndex = ref(0);
const swipeDirection = ref(null);
const isTopCardHovered = ref(false);
const cargando = ref(true);
const mensajeMatch = ref("");

onMounted(async () => {
  if (!usuarioActual) { cargando.value = false; return; }
  try {
    const res = await usuarioService.obtenerTodos();
    usuarios.value = res.data.filter(
      (u) => u.id !== usuarioActual.id && u.nombreUsuario !== "Sistema"
    );
  } catch {
    // sin usuarios
  } finally {
    cargando.value = false;
  }
});

function iniciales(nombre) {
  return nombre ? nombre.slice(0, 2).toUpperCase() : "??";
}

function rolDisplay(rol) {
  return rol === "profesor" ? "Mentor" : "Estudiante";
}

function lenguajesDisplay(u) {
  const str = u.rol === "profesor" ? u.lenguajesAEnsenar : u.lenguajesAAprender;
  return str || "Sin lenguajes";
}

const getDepthCardStyle = (index) => {
  const offset = index - currentIndex.value;
  if (offset < 0) return { display: "none" };
  if (offset === 0) return { zIndex: 10, transform: "scale(1) translateY(0)", opacity: 1, filter: "blur(0px)" };
  if (offset > 0 && offset <= 2) {
    return {
      zIndex: 8 - offset,
      transform: `scale(${1 - offset * 0.06}) translateY(${offset * 25}px)`,
      opacity: 1 - offset * 0.3,
      filter: `blur(${offset * 2}px)`,
      pointerEvents: "none",
    };
  }
  return { display: "none" };
};

async function handleSwipe(direction, usuario) {
  isTopCardHovered.value = false;
  swipeDirection.value = direction;

  if (direction === "right" && usuario && usuarioActual) {
    try {
      await matchService.crearMatch(usuarioActual.id, usuario.id);
      mensajeMatch.value = `¡Match con ${usuario.nombreUsuario}! Ya podéis hablar en Chats.`;
      setTimeout(() => (mensajeMatch.value = ""), 3000);
    } catch (e) {
      const msg = e.response?.data;
      if (msg && msg.includes("Ya existe")) {
        mensajeMatch.value = "Ya tienes match con este usuario.";
        setTimeout(() => (mensajeMatch.value = ""), 2000);
      }
    }
  }

  setTimeout(() => {
    currentIndex.value++;
    swipeDirection.value = null;
  }, 300);
}

const resetCards = () => {
  isTopCardHovered.value = false;
  currentIndex.value = 0;
};
</script>

<template>
  <div class="para-ti-page">
    <h1 class="page-title">Descubrir Perfiles</h1>

    <p v-if="mensajeMatch" class="mensaje-match">{{ mensajeMatch }}</p>

    <div v-if="cargando" class="estado">Cargando perfiles...</div>

    <div v-else-if="usuarios.length === 0" class="empty-state">
      <div class="empty-icon">👻</div>
      <h2>No hay perfiles disponibles</h2>
      <p>Vuelve más tarde cuando haya más usuarios registrados.</p>
    </div>

    <template v-else>
      <div
        class="stack-container"
        :class="{ 'is-fanning': isTopCardHovered }"
        v-if="currentIndex < usuarios.length"
      >
        <div
          v-for="(usuario, index) in usuarios"
          :key="usuario.id"
          class="swipe-card"
          :class="{
            'is-top': index === currentIndex,
            'swipe-left': index === currentIndex && swipeDirection === 'left',
            'swipe-right': index === currentIndex && swipeDirection === 'right',
          }"
          :style="getDepthCardStyle(index)"
          @mouseenter="index === currentIndex ? (isTopCardHovered = true) : null"
          @mouseleave="index === currentIndex ? (isTopCardHovered = false) : null"
        >
          <div class="card-top">
            <span class="badge-rol">{{ rolDisplay(usuario.rol) }}</span>
            <div class="initials">{{ iniciales(usuario.nombreUsuario) }}</div>
          </div>

          <div class="card-bottom">
            <div class="info-texto">
              <h2>{{ usuario.nombreUsuario }}</h2>
              <p>{{ lenguajesDisplay(usuario) }}</p>
              <p class="descripcion" v-if="usuario.descripcion">{{ usuario.descripcion }}</p>
            </div>

            <div class="action-buttons" v-if="index === currentIndex">
              <button class="btn-reject" @click="handleSwipe('left', usuario)" title="Pasar">
                <span class="icon">✕</span>
              </button>
              <button class="btn-match" @click="handleSwipe('right', usuario)" title="¡Match!">
                <span class="icon">♥</span>
              </button>
            </div>
          </div>
        </div>
      </div>

      <div class="empty-state" v-else>
        <div class="empty-icon">👻</div>
        <h2>¡No hay más perfiles!</h2>
        <p>Has visto a todos los usuarios disponibles.</p>
        <button class="btn-reset" @click="resetCards">Volver a empezar</button>
      </div>
    </template>
  </div>
</template>

<style scoped>
.para-ti-page {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px 20px;
  min-height: 85vh;
}

.page-title {
  color: var(--text-main);
  margin-bottom: 20px;
  font-weight: 800;
  font-size: 2rem;
  text-align: center;
}

.estado {
  color: var(--text-muted);
  margin-top: 40px;
}

.mensaje-match {
  background: rgba(215, 24, 32, 0.1);
  border: 1px solid var(--primary);
  color: var(--primary);
  padding: 12px 24px;
  border-radius: 12px;
  font-weight: bold;
  margin-bottom: 20px;
  text-align: center;
}

.stack-container {
  position: relative;
  width: 100%;
  max-width: 380px;
  height: 550px;
  perspective: 1200px;
}

.stack-container::before,
.stack-container::after {
  content: "";
  position: absolute;
  top: 0;
  width: 100%;
  height: 100%;
  border-radius: 20px;
  border: 1px solid var(--border);
  opacity: 0;
  filter: blur(8px);
  transition: all 0.4s ease-out;
  z-index: 9;
  pointer-events: none;
}
.stack-container::before {
  background: linear-gradient(to bottom, var(--primary) 55%, var(--bg-card) 45%);
}
.stack-container::after {
  background: linear-gradient(to bottom, var(--primary) 55%, var(--bg-card) 45%);
}
.stack-container.is-fanning::before {
  opacity: 0.5;
  transform: translateX(-160px) rotateY(-35deg) scale(0.95);
}
.stack-container.is-fanning::after {
  opacity: 0.5;
  transform: translateX(160px) rotateY(35deg) scale(0.95);
}

.swipe-card {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: var(--bg-card);
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.4);
  display: flex;
  flex-direction: column;
  border: 1px solid var(--border);
  transition: transform 0.3s ease, opacity 0.3s ease, filter 0.3s ease;
  backface-visibility: hidden;
}

.swipe-left  { transform: translateX(-250px) rotate(-15deg) !important; opacity: 0 !important; }
.swipe-right { transform: translateX(250px) rotate(15deg) !important;   opacity: 0 !important; }

.card-top {
  background-color: var(--primary);
  height: 55%;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
}

.badge-rol {
  position: absolute;
  top: 20px;
  right: 20px;
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: bold;
  color: white;
  background-color: rgba(0,0,0,0.25);
}

.initials {
  font-size: 7rem;
  color: white;
  font-weight: 300;
  letter-spacing: -2px;
}

.card-bottom {
  height: 45%;
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
}

.info-texto { text-align: center; width: 100%; }
.info-texto h2 { color: var(--text-main); margin: 0 0 6px 0; font-size: 1.8rem; }
.info-texto p { color: var(--text-muted); margin: 0; font-size: 0.95rem; }
.descripcion { font-size: 0.85rem !important; margin-top: 4px !important; }

.action-buttons { display: flex; gap: 30px; width: 100%; justify-content: center; }

.btn-reject,
.btn-match {
  width: 70px;
  height: 70px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  font-size: 2rem;
  transition: transform 0.2s, box-shadow 0.2s, background-color 0.2s;
  outline: none;
}
.btn-reject { background: transparent; border: 2px solid var(--primary); color: var(--primary); }
.btn-reject:hover { background: rgba(215, 24, 32, 0.1); transform: scale(1.1); }
.btn-match { background: var(--primary); border: none; color: white; box-shadow: 0 4px 15px rgba(215, 24, 32, 0.4); }
.btn-match:hover { transform: scale(1.1); }

.empty-state {
  text-align: center;
  margin-top: 60px;
  background: var(--bg-card);
  padding: 50px;
  border-radius: 20px;
  border: 1px solid var(--border);
}
.empty-icon { font-size: 4rem; margin-bottom: 20px; }
.empty-state h2 { color: var(--text-main); margin-bottom: 10px; }
.empty-state p { color: var(--text-muted); margin-bottom: 30px; }

.btn-reset {
  background: var(--primary);
  color: white;
  border: none;
  padding: 12px 25px;
  border-radius: 10px;
  cursor: pointer;
  font-weight: bold;
  font-size: 1rem;
  transition: transform 0.2s;
}
.btn-reset:hover { transform: scale(1.05); }
</style>
