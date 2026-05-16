<script setup>
import { ref, computed, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { comunidadService } from "../services/api";
import ComunidadCard from "../components/ComunidadCard.vue";
import { open, setPrefilledVariables } from "@typebot.io/js";
import "@typebot.io/js/web";

const route = useRoute();
const router = useRouter();

const searchQuery = ref("");
const comunidades = ref([]);
const cargando = ref(true);
const error = ref(null);
const solicitudEnviada = ref(false);

const usuario = JSON.parse(sessionStorage.getItem("usuario") || "{}");

onMounted(async () => {
  try {
    const respuesta = await comunidadService.obtenerTodas();
    comunidades.value = respuesta.data;
  } catch {
    error.value = "No se pudieron cargar las comunidades. ¿Está el servidor arrancado?";
  } finally {
    cargando.value = false;
  }

  setPrefilledVariables({
    usuario_id: String(usuario.id ?? ""),
    nombre_usuario: usuario.nombreUsuario ?? "",
  });

  if (route.query.solicitud === "enviada") {
    solicitudEnviada.value = true;
    setTimeout(() => { solicitudEnviada.value = false; }, 3000);
    router.replace({ path: "/comunidades" });
  }
});

function abrirSolicitud() {
  open();
}

const comunidadesFiltradas = computed(() =>
  comunidades.value.filter((c) =>
    c.nombreComunidad.toLowerCase().includes(searchQuery.value.toLowerCase()),
  ),
);
</script>

<template>
  <typebot-popup typebot="nebrimatch-gu1sl0o" api-host="https://typebot.co"></typebot-popup>
  <transition name="toast">
    <div v-if="solicitudEnviada" class="toast">Se ha enviado tu solicitud</div>
  </transition>
  <div class="comunidades-page">
    <h1>Comunidades de Programación</h1>
    <input
      v-model="searchQuery"
      type="text"
      placeholder="🔍 Buscar tecnología..."
      class="search-input"
    />
    <button class="btn-solicitar" @click="abrirSolicitud">
      + Solicitar crear comunidad
    </button>
    <p v-if="cargando" class="estado">Cargando comunidades...</p>
    <p v-else-if="error" class="estado error">{{ error }}</p>
    <div v-else class="container">
      <ComunidadCard
        v-for="c in comunidadesFiltradas"
        :key="c.id"
        :comunidad="c"
      />
    </div>
  </div>
</template>

<style scoped>
.comunidades-page {
  padding: 40px;
  text-align: center;
}
.search-input {
  width: 100%;
  max-width: 500px;
  padding: 15px;
  border-radius: 25px;
  border: 2px solid #ddd;
  margin-bottom: 30px;
  font-size: 1rem;
}
.container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}
.search-input {
  background-color: var(--bg-card);
  color: var(--text-main);
  border: 1px solid var(--border);
}
.estado {
  color: var(--text-muted);
  margin-top: 40px;
  font-size: 1rem;
}
.error {
  color: var(--primary);
}
.btn-solicitar {
  display: inline-block;
  margin-bottom: 24px;
  padding: 12px 28px;
  background: linear-gradient(to right, var(--primary), #b5141a);
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  transition: transform 0.2s;
}
.btn-solicitar:hover {
  transform: scale(1.03);
}
.toast {
  position: fixed;
  top: 32px;
  left: 50%;
  transform: translateX(-50%);
  background: var(--primary);
  color: #000;
  padding: 14px 28px;
  border-radius: 25px;
  font-size: 0.95rem;
  font-weight: 600;
  z-index: 9999;
  box-shadow: 0 4px 20px rgba(0,0,0,0.3);
}
.toast-enter-active, .toast-leave-active { transition: opacity 0.4s; }
.toast-enter-from, .toast-leave-to { opacity: 0; }
</style>
