<script setup>
import { ref, onMounted, computed } from "vue";

// --- ESTADO (Variables Reactivas) ---
const mentoresData = ref([]);
const loading = ref(true);
const currentIndex = ref(0);
const swipeDirection = ref("");

// --- COMPUTED (El "Zumo" - Mentor Actual) ---
const currentMentor = computed(() => {
  return mentoresData.value[currentIndex.value] || {};
});

// --- FUNCIONES DE LÓGICA ---
const handlePass = () => {
  swipeDirection.value = "swipe-right";
  avanzarCarta();
};

const handleMatch = () => {
  swipeDirection.value = "swipe-left";
  // Aquí es donde en el futuro llamaremos a la API de Spring Boot
  console.log("Match con: " + currentMentor.value.nombre);
  avanzarCarta();
};

const avanzarCarta = () => {
  setTimeout(() => {
    if (currentIndex.value < mentoresData.value.length - 1) {
      currentIndex.value++;
    } else {
      alert("¡Has visto a todos los mentores!");
      currentIndex.value = 0;
    }
    swipeDirection.value = "";
  }, 400);
};

// --- CARGA DE DATOS ---
onMounted(() => {
  // Simulamos una carga de 1 segundo para ver el "Loading"
  setTimeout(() => {
    mentoresData.value = [
      {
        id: 1,
        nombre: "Eric R.",
        especialidad: "Vue & Frontend",
        online: true,
        imagen:
          "https://ui-avatars.com/api/?name=Eric+R&background=d71820&color=fff&size=400",
      },
      {
        id: 2,
        nombre: "Gema M.",
        especialidad: "Java & Spring Boot",
        online: false,
        imagen:
          "https://ui-avatars.com/api/?name=Gema+M&background=007bff&color=fff&size=400",
      },
      {
        id: 3,
        nombre: "Dani García",
        especialidad: "Python & IA",
        online: true,
        imagen:
          "https://ui-avatars.com/api/?name=Dani+G&background=3776ab&color=fff&size=400",
      },
      {
        id: 4,
        nombre: "Laura Soler",
        especialidad: "Diseño UI/UX",
        online: false,
        imagen:
          "https://ui-avatars.com/api/?name=Laura+S&background=e91e63&color=fff&size=400",
      },
    ];
    loading.value = false;
  }, 1000);
});
</script>

<template>
  <div class="para-ti-page">
    <h1 class="title">NebriMatch TFG</h1>

    <div v-if="loading" class="loader">
      <p>Buscando mentores...</p>
    </div>

    <div v-else-if="mentoresData.length > 0" class="stack-container">
      <div class="card" :class="swipeDirection">
        <div class="card-image-container">
          <img :src="currentMentor.imagen" :alt="currentMentor.nombre" />
          <span v-if="currentMentor.online" class="badge-online">
            ● En línea
          </span>
        </div>

        <div class="card-info">
          <h2>{{ currentMentor.nombre }}</h2>
          <span class="specialty">{{ currentMentor.especialidad }}</span>
        </div>

        <div class="actions">
          <button @click="handlePass" class="btn-pass">✕</button>
          <button @click="handleMatch" class="btn-match">❤</button>
        </div>
      </div>
    </div>

    <div v-else class="empty-state">
      <p>No hay mentores disponibles en este momento.</p>
    </div>
  </div>
</template>

<style scoped>
.para-ti-page {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
  padding-bottom: 50px; /* Espacio extra abajo para que respire */
  box-sizing: border-box; /* Importante para el padding */
}

.title {
  color: #d71820;
  margin-bottom: 30px;
}

.stack-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  perspective: 1000px;
  height: auto;
}

/* --- CARTA --- */
.card {
  width: 340px;
  height: auto; 
  background: white;
  border-radius: 25px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition:
    transform 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275),
    opacity 0.3s;
}

.card-image-container {
  position: relative;
  width: 100%;
  height: 300px;
}

.card-image-container img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.card-info {
  padding: 15px;
  text-align: left;
}

.card-info h2 {
  margin: 0;
  font-size: 1.5rem;
  color: #222;
}

.specialty {
  color: #666;
  font-size: 0.9rem;
}

/* --- BADGE ONLINE --- */
.badge-online {
  position: absolute;
  top: 15px;
  right: 15px;
  background: #4caf50;
  color: white;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 0.75rem;
  font-weight: bold;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
  100% {
    transform: scale(1);
  }
}

/* --- ANIMACIONES DE SWIPE --- */
.swipe-left {
  transform: translateX(-150%) rotate(-30deg) !important;
  opacity: 0;
}

.swipe-right {
  transform: translateX(150%) rotate(30deg) !important;
  opacity: 0;
}

/* --- BOTONES --- */
.actions {
  display: flex;
  justify-content: space-evenly;
  padding-bottom: 20px;
}

button {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  transition:
    transform 0.2s,
    box-shadow 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}

button:hover {
  transform: scale(1.1);
}

.btn-pass {
  background: white;
  color: #d71820;
  border: 2px solid #d71820;
}

.btn-match {
  background: #d71820;
  color: white;
  box-shadow: 0 4px 15px rgba(215, 24, 32, 0.3);
}
</style>
