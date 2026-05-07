<script setup>
import { ref } from "vue";

// --- DATOS FALSOS ---
const mentoresData = ref([
  {
    id: 1,
    iniciales: "ER",
    nombre: "Eric R.",
    rol: "Vue & Frontend",
    enLinea: true,
  },
  {
    id: 2,
    iniciales: "AL",
    nombre: "Ana L.",
    rol: "Java & Spring Boot",
    enLinea: false,
  },
  {
    id: 3,
    iniciales: "JP",
    nombre: "Juan P.",
    rol: "Python & IA",
    enLinea: true,
  },
  {
    id: 4,
    iniciales: "CM",
    nombre: "Carlos M.",
    rol: "DevOps & Docker",
    enLinea: true,
  },
  {
    id: 5,
    iniciales: "LM",
    nombre: "Laura M.",
    rol: "Bases de Datos",
    enLinea: true,
  },
  {
    id: 6,
    iniciales: "SP",
    nombre: "Sergio P.",
    rol: "React Native",
    enLinea: false,
  },
]);

const currentIndex = ref(0);
const swipeDirection = ref(null);

// Variable para controlar si el ratón está sobre la carta de arriba
const isTopCardHovered = ref(false);

// Función que calcula el estilo de la pila de profundidad (el efecto que ya teníamos)
const getDepthCardStyle = (index) => {
  const offset = index - currentIndex.value;

  if (offset < 0) return { display: "none" }; // Ya pasaron

  // Carta Principal (Índice Z más alto)
  if (offset === 0) {
    return {
      zIndex: 10,
      transform: "scale(1) translateY(0)",
      opacity: 1,
      filter: "blur(0px)",
    };
  }

  // Cartas de reserva (Pila de profundidad detrás)
  if (offset > 0 && offset <= 2) {
    return {
      zIndex: 8 - offset, // zIndex 7 y 6 (detrás del efecto de abanico que será zIndex 9)
      transform: `scale(${1 - offset * 0.06}) translateY(${offset * 25}px)`,
      opacity: 1 - offset * 0.3,
      filter: `blur(${offset * 2}px)`,
      pointerEvents: "none",
    };
  }

  return { display: "none" };
};

// Función para deslizar
const handleSwipe = (direction) => {
  // Cuando deslizamos, quitamos el efecto de abanico inmediatamente
  isTopCardHovered.value = false;
  swipeDirection.value = direction;

  setTimeout(() => {
    currentIndex.value++;
    swipeDirection.value = null;
  }, 300);
};

const resetCards = () => {
  isTopCardHovered.value = false;
  currentIndex.value = 0;
};
</script>

<template>
  <div class="para-ti-page">
    <h1 class="page-title">Descubrir Mentores</h1>

    <div
      class="stack-container"
      :class="{ 'is-fanning': isTopCardHovered }"
      v-if="currentIndex < mentoresData.length"
    >
      <div
        v-for="(mentor, index) in mentoresData"
        :key="mentor.id"
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
          <span v-if="mentor.enLinea" class="badge-online">● En línea</span>
          <span v-else class="badge-offline">● Desconectado</span>
          <div class="initials">{{ mentor.iniciales }}</div>
        </div>

        <div class="card-bottom">
          <div class="info-texto">
            <h2>{{ mentor.nombre }}</h2>
            <p>{{ mentor.rol }}</p>
          </div>

          <div class="action-buttons" v-if="index === currentIndex">
            <button
              class="btn-reject"
              @click="handleSwipe('left')"
              title="Pasar"
            >
              <span class="icon">✕</span>
            </button>
            <button
              class="btn-match"
              @click="handleSwipe('right')"
              title="¡Match!"
            >
              <span class="icon">♥</span>
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="empty-state" v-else>
      <div class="empty-icon">👻</div>
      <h2>¡No hay más perfiles!</h2>
      <p>Has visto a todos los mentores disponibles por hoy.</p>
      <button class="btn-reset" @click="resetCards">Volver a empezar</button>
    </div>
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
  margin-bottom: 40px;
  font-weight: 800;
  font-size: 2rem;
  text-align: center;
}

/* ==========================================================================
   1. CONTENEDOR DE LA BARAJA (STACK)
   Explicación TFG: Añadimos 'perspective' para que las rotaciones 3D 
   (el efecto de barajado) se vean con profundidad real.
   ==========================================================================
*/
.stack-container {
  position: relative;
  width: 100%;
  max-width: 380px;
  height: 550px;
  perspective: 1200px; /* Clave para el efecto 3D del barajado */
}

/* ==========================================================================
   2. EXPLICACIÓN TFG: EFECTO DE BARALADO/ABANICO (HOVER)
   Usamos Pseudo-elementos (::before y ::after) para crear las dos cartas 
   fantasma laterales sin tener que crear divs extra en el HTML.
   Están ocultas (opacity 0) por defecto.
   ==========================================================================
*/
.stack-container::before,
.stack-container::after {
  content: "";
  position: absolute;
  top: 0;
  width: 100%;
  height: 100%;
  border-radius: 20px;
  border: 1px solid var(--border);
  opacity: 0; /* Ocultas por defecto */
  filter: blur(8px); /* Difuminadas */
  transition: all 0.4s ease-out; /* Animación suave al entrar/salir */
  z-index: 9; /* Detrás de la carta principal (10), pero delante de la pila de reserva (7,6) */
  pointer-events: none;
}

/* Diseñamos el fondo de las cartas fantasma (rojo arriba, oscuro abajo) */
.stack-container::before {
  background: linear-gradient(
    to bottom,
    var(--primary) 55%,
    var(--bg-card) 45%
  );
}
.stack-container::after {
  background: linear-gradient(
    to bottom,
    var(--primary) 55%,
    var(--bg-card) 45%
  );
}

/* --- ACTIVACIÓN DEL EFECTO HOVER (Clase .is-fanning) --- */

/* Carta de Abanico IZQUIERDA */
.stack-container.is-fanning::before {
  opacity: 0.5; /* Transparentes */
  /* Giramos 35 grados hacia la izquierda y la movemos 160px a la izquierda */
  transform: translateX(-160px) rotateY(-35deg) scale(0.95);
}

/* Carta de Abanico DERECHA */
.stack-container.is-fanning::after {
  opacity: 0.5;
  /* Giramos 35 grados hacia la derecha y la movemos 160px a la derecha */
  transform: translateX(160px) rotateY(35deg) scale(0.95);
}

/* ==========================================================================
   3. ESTILOS DE LA CARTA (SWIPE-CARD)
   La carta principal y la pila de reserva detrás.
   ==========================================================================
*/
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
  /* Aseguramos transiciones suaves para el movimiento, tamaño, opacidad y blur */
  transition:
    transform 0.3s ease,
    opacity 0.3s ease,
    filter 0.3s ease;
  backface-visibility: hidden; /* Mejora el rendimiento de animaciones 3D */
}

/* Animaciones de Swipe (Accept/Reject) */
.swipe-left {
  transform: translateX(-250px) rotate(-15deg) !important;
  opacity: 0 !important;
}
.swipe-right {
  transform: translateX(250px) rotate(15deg) !important;
  opacity: 0 !important;
}

/* PARTE ROJA */
.card-top {
  background-color: var(--primary);
  height: 55%;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
}

.badge-online,
.badge-offline {
  position: absolute;
  top: 20px;
  right: 20px;
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: bold;
  color: white;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}
.badge-online {
  background-color: #4caf50;
}
.badge-offline {
  background-color: #9e9e9e;
}

.initials {
  font-size: 7rem;
  color: white;
  font-weight: 300;
  letter-spacing: -2px;
}

/* PARTE OSCURA (Información) */
.card-bottom {
  height: 45%;
  padding: 30px 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
}

.info-texto {
  text-align: center;
  width: 100%;
}
.info-texto h2 {
  color: var(--text-main);
  margin: 0 0 8px 0;
  font-size: 1.8rem;
}
.info-texto p {
  color: var(--text-muted);
  margin: 0;
  font-size: 1rem;
}

/* BOTONES REDONDOS */
.action-buttons {
  display: flex;
  gap: 30px;
  width: 100%;
  justify-content: center;
}
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
  transition:
    transform 0.2s,
    box-shadow 0.2s,
    background-color 0.2s;
  outline: none;
}
.btn-reject {
  background: transparent;
  border: 2px solid var(--primary);
  color: var(--primary);
}
.btn-reject:hover {
  background: rgba(215, 24, 32, 0.1);
  transform: scale(1.1);
}
.btn-match {
  background: var(--primary);
  border: none;
  color: white;
  box-shadow: 0 4px 15px rgba(215, 24, 32, 0.4);
}
.btn-match:hover {
  transform: scale(1.1);
  background: var(--primary-hover);
}

/* PANTALLA VACÍA */
.empty-state {
  text-align: center;
  margin-top: 60px;
  background: var(--bg-card);
  padding: 50px;
  border-radius: 20px;
  border: 1px solid var(--border);
}
.empty-icon {
  font-size: 4rem;
  margin-bottom: 20px;
}
.empty-state h2 {
  color: var(--text-main);
  margin-bottom: 10px;
}
.empty-state p {
  color: var(--text-muted);
  margin-bottom: 30px;
}

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
.btn-reset:hover {
  transform: scale(1.05);
}
</style>
