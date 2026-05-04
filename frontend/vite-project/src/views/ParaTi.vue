<script setup>
import { ref, computed } from "vue";

// Nuestros datos de prueba simulando el backend
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
]);

const currentIndex = ref(0);
const swipeDirection = ref(null); // Guardará 'left' o 'right' para la animación CSS

// Variable inteligente para saber qué tarjeta toca mostrar
const currentMentor = computed(() => mentoresData.value[currentIndex.value]);

// Función para manejar el clic en los botones
const handleSwipe = (direction) => {
  swipeDirection.value = direction; // Dispara la clase CSS

  // Esperamos 300ms a que termine la animación antes de cambiar de usuario
  setTimeout(() => {
    currentIndex.value++;
    swipeDirection.value = null; // Reiniciamos la posición para la siguiente tarjeta
  }, 300);
};

// Función para reiniciar cuando se acaban las tarjetas (ideal para probar en el TFG)
const resetCards = () => {
  currentIndex.value = 0;
};
</script>

<template>
  <div class="para-ti-page">
    <h1 class="page-title">NebriMatch TFG</h1>

    <!-- Contenedor de la Tarjeta (Solo se muestra si hay mentores en la lista) -->
    <div class="card-container" v-if="currentMentor">
      <div
        class="swipe-card"
        :class="{
          'swipe-left': swipeDirection === 'left',
          'swipe-right': swipeDirection === 'right',
        }"
      >
        <!-- MITAD SUPERIOR (ROJA) -->
        <div class="card-top">
          <!-- Etiqueta de estado -->
          <span v-if="currentMentor.enLinea" class="badge-online"
            >● En línea</span
          >
          <span v-else class="badge-offline">● Desconectado</span>

          <!-- Iniciales Gigantes -->
          <div class="initials">{{ currentMentor.iniciales }}</div>
        </div>

        <!-- MITAD INFERIOR (OSCURA) -->
        <div class="card-bottom">
          <div class="info-texto">
            <h2>{{ currentMentor.nombre }}</h2>
            <p>{{ currentMentor.rol }}</p>
          </div>

          <!-- Botones de Acción -->
          <div class="action-buttons">
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

    <!-- Pantalla cuando te quedas sin tarjetas -->
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
  min-height: 80vh;
}

.page-title {
  color: var(--primary);
  margin-bottom: 40px;
  font-weight: 800;
  font-size: 2rem;
  text-align: center;
}

.card-container {
  position: relative;
  width: 100%;
  max-width: 380px; /* Ancho de la tarjeta */
  height: 550px; /* Alto de la tarjeta */
}

/* LA TARJETA EN SÍ */
.swipe-card {
  width: 100%;
  height: 100%;
  background: var(--bg-card); /* Fondo oscuro */
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.4);
  display: flex;
  flex-direction: column;
  border: 1px solid var(--border);
  transition:
    transform 0.3s ease,
    opacity 0.3s ease; /* Transición suave para el swipe */
}

/* Animaciones CSS controladas por JavaScript */
.swipe-left {
  transform: translateX(-200px) rotate(-15deg);
  opacity: 0;
}
.swipe-right {
  transform: translateX(200px) rotate(15deg);
  opacity: 0;
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

/* Botón X (Cruzar) */
.btn-reject {
  background: transparent;
  border: 2px solid var(--primary);
  color: var(--primary);
}
.btn-reject:hover {
  background: rgba(215, 24, 32, 0.1);
  transform: scale(1.1);
}

/* Botón Corazón (Match) */
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
