<script setup>
import { ref, onMounted, computed } from "vue";
import { comunidadService, membresiaService } from "../services/api";

const props = defineProps(["id"]);

const comunidad = ref(null);
const cargando = ref(true);
const error = ref(null);
const miembros = ref(0);
const esMiembro = ref(false);
const cargandoBoton = ref(false);

const usuarioActual = JSON.parse(localStorage.getItem("usuario"));

onMounted(async () => {
  try {
    const [resComunidad, resMiembros] = await Promise.all([
      comunidadService.obtenerPorId(props.id),
      membresiaService.contarMiembros(props.id),
    ]);
    comunidad.value = resComunidad.data;
    miembros.value = resMiembros.data;

    if (usuarioActual) {
      const resMembresia = await membresiaService.esMiembro(props.id, usuarioActual.id);
      esMiembro.value = resMembresia.data;
    }
  } catch {
    error.value = "No se encontró la comunidad.";
  } finally {
    cargando.value = false;
  }
});

async function toggleMembresia() {
  if (!usuarioActual) {
    alert("Debes iniciar sesión para unirte a una comunidad.");
    return;
  }
  cargandoBoton.value = true;
  try {
    if (esMiembro.value) {
      await membresiaService.salir(props.id, usuarioActual.id);
      esMiembro.value = false;
      miembros.value--;
    } else {
      await membresiaService.unirse(props.id, usuarioActual.id);
      esMiembro.value = true;
      miembros.value++;
    }
  } catch (e) {
    alert(e.response?.data || "Ha ocurrido un error.");
  } finally {
    cargandoBoton.value = false;
  }
}

const textoBoton = computed(() => {
  if (cargandoBoton.value) return "Cargando...";
  return esMiembro.value ? "Salir de la comunidad" : "Unirme al repositorio";
});
</script>

<template>
  <div class="detalle-container">
    <button @click="$router.back()">← Volver</button>

    <p v-if="cargando" class="estado">Cargando...</p>
    <p v-else-if="error" class="estado error">{{ error }}</p>

    <template v-else-if="comunidad">
      <div class="header">
        <img :src="comunidad.imagenComunidad" class="tech-logo" />
        <h1>{{ comunidad.nombreComunidad }}</h1>
      </div>
      <p class="stats">👥 {{ miembros }} programadores unidos</p>
      <p class="desc">{{ comunidad.descripcionComunidad }}</p>
      <button
        class="btn-join"
        :class="{ salir: esMiembro }"
        :disabled="cargandoBoton"
        @click="toggleMembresia"
      >
        {{ textoBoton }}
      </button>
    </template>
  </div>
</template>

<style scoped>
.detalle-container {
  max-width: 600px;
  margin: 40px auto;
  text-align: center;
  background: var(--bg-card);
  padding: 40px;
  border-radius: 20px;
  border: 1px solid var(--border);
  box-shadow: 0 10px 30px rgba(0,0,0,0.5);
}

.tech-logo {
  width: 120px;
  margin-bottom: 20px;
}

h1 {
  color: var(--text-main);
  margin-bottom: 10px;
}

.stats {
  color: var(--text-muted);
  font-weight: 600;
}

.desc {
  color: var(--text-main);
  line-height: 1.6;
  margin-top: 20px;
}

.btn-join {
  background: var(--primary);
  color: white;
  border: none;
  padding: 15px 40px;
  border-radius: 10px;
  font-weight: bold;
  cursor: pointer;
  margin-top: 30px;
  width: 100%;
  transition: transform 0.2s, background 0.2s;
}

.btn-join:hover:not(:disabled) {
  transform: scale(1.02);
}

.btn-join.salir {
  background: transparent;
  border: 2px solid var(--primary);
  color: var(--primary);
}

.btn-join:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

button:not(.btn-join) {
  color: var(--primary);
  border: none;
  background: none;
  cursor: pointer;
  font-weight: bold;
}

.estado {
  color: var(--text-muted);
  margin-top: 20px;
}

.error {
  color: var(--primary);
}
</style>
