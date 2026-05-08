<script setup>
import { ref, computed, onMounted } from "vue";
import { usuarioService, lenguajeService } from "../services/api";

// ── DATOS DEL USUARIO LOGUEADO ────────────────────────────
const usuario = ref(null);
const cargando = ref(true);
const error = ref(null);

// ── MODO EDICIÓN ──────────────────────────────────────────
const editando = ref(false);
const editDesc = ref("");
const editLenguajes = ref([]);
const lenguajesDisponibles = ref([]);
const guardando = ref(false);
const errorEdicion = ref("");

// ── MODAL SUGERENCIAS ─────────────────────────────────────
const perfilSeleccionado = ref(null);
const sugerencias = ref([
  {
    id: 1,
    nombre: "Rodrigo",
    rol: "Mentor",
    avatar: "https://ui-avatars.com/api/?name=Rodrigo&background=0D8ABC&color=fff",
    bio: "Senior Frontend Developer. Te ayudo a estructurar tu proyecto en Vue y React.",
    lenguajes: ["Vue.js", "React", "TypeScript"],
  },
  {
    id: 2,
    nombre: "Laura",
    rol: "Mentor",
    avatar: "https://ui-avatars.com/api/?name=Laura&background=4CAF50&color=fff",
    bio: "Experta en backend y arquitecturas de bases de datos.",
    lenguajes: ["Java", "Spring Boot", "SQL"],
  },
  {
    id: 3,
    nombre: "Carlos",
    rol: "Estudiante",
    avatar: "https://ui-avatars.com/api/?name=Carlos&background=FF9800&color=fff",
    bio: "Buscando compañero para montar un proyecto de IA.",
    lenguajes: ["Python", "C++"],
  },
]);

// ── COMPUTED ──────────────────────────────────────────────
const esProfesor = computed(() => usuario.value?.rol === "profesor");

const lenguajesUsuario = computed(() => {
  if (!usuario.value) return [];
  const str = esProfesor.value
    ? usuario.value.lenguajesAEnsenar
    : usuario.value.lenguajesAAprender;
  return str ? str.split(",").map((l) => l.trim()).filter(Boolean) : [];
});

const avatarUrl = computed(() =>
  usuario.value
    ? `https://ui-avatars.com/api/?name=${encodeURIComponent(usuario.value.nombreUsuario)}&background=d71820&color=fff&size=200`
    : ""
);

const rolDisplay = computed(() =>
  esProfesor.value ? "Mentor" : "Estudiante"
);

// ── CICLO DE VIDA ─────────────────────────────────────────
onMounted(async () => {
  const local = JSON.parse(sessionStorage.getItem("usuario"));
  if (!local) {
    error.value = "No has iniciado sesión.";
    cargando.value = false;
    return;
  }
  try {
    const [resUsuario, resLenguajes] = await Promise.all([
      usuarioService.obtenerPorId(local.id),
      lenguajeService.obtenerTodos(),
    ]);
    usuario.value = resUsuario.data;
    lenguajesDisponibles.value = resLenguajes.data;
    sessionStorage.setItem("usuario", JSON.stringify(resUsuario.data));
  } catch {
    error.value = "Error al cargar el perfil.";
  } finally {
    cargando.value = false;
  }
});

// ── EDICIÓN ───────────────────────────────────────────────
function abrirEdicion() {
  editDesc.value = usuario.value.descripcion || "";
  editLenguajes.value = [...lenguajesUsuario.value];
  errorEdicion.value = "";
  editando.value = true;
}

function cancelarEdicion() {
  editando.value = false;
}

async function guardarPerfil() {
  errorEdicion.value = "";
  guardando.value = true;
  const lenguajesStr = editLenguajes.value.join(",");
  try {
    const res = await usuarioService.actualizar(usuario.value.id, {
      descripcion: editDesc.value,
      lenguajesAEnsenar: esProfesor.value ? lenguajesStr : usuario.value.lenguajesAEnsenar,
      lenguajesAAprender: esProfesor.value ? usuario.value.lenguajesAAprender : lenguajesStr,
    });
    usuario.value = res.data;
    sessionStorage.setItem("usuario", JSON.stringify(res.data));
    editando.value = false;
  } catch {
    errorEdicion.value = "Error al guardar. Inténtalo de nuevo.";
  } finally {
    guardando.value = false;
  }
}

function abrirModal(perfil) {
  perfilSeleccionado.value = perfil;
}

function cerrarModal() {
  perfilSeleccionado.value = null;
}
</script>

<template>
  <div class="perfil-page">

    <p v-if="cargando" class="estado">Cargando perfil...</p>
    <p v-else-if="error" class="estado error">{{ error }}</p>

    <div v-else class="perfil-layout">

      <!-- COLUMNA IZQUIERDA: MI PERFIL -->
      <section class="main-profile">
        <div class="card mi-tarjeta">

          <!-- VISTA NORMAL -->
          <template v-if="!editando">
            <div class="header-tarjeta">
              <img :src="avatarUrl" alt="Mi Avatar" class="avatar-grande" />
              <div class="info-basica">
                <h1>{{ usuario.nombreUsuario }}</h1>
                <span class="badge-rol" :class="usuario.rol">{{ rolDisplay }}</span>
              </div>
            </div>

            <div class="bio-section">
              <h3>Sobre mí</h3>
              <p>{{ usuario.descripcion || "Todavía no has escrito nada sobre ti." }}</p>
            </div>

            <div class="tags-section">
              <h3>{{ esProfesor ? "Lenguajes que enseño" : "Lenguajes que quiero aprender" }}</h3>
              <div class="tags-container">
                <span v-if="lenguajesUsuario.length === 0" class="tag comp-tag">Sin lenguajes</span>
                <span v-for="lang in lenguajesUsuario" :key="lang" class="tag lang-tag">{{ lang }}</span>
              </div>
            </div>

            <button class="btn-editar" @click="abrirEdicion">Editar Perfil</button>
          </template>

          <!-- MODO EDICIÓN -->
          <template v-else>
            <div class="header-tarjeta">
              <img :src="avatarUrl" alt="Mi Avatar" class="avatar-grande" />
              <div class="info-basica">
                <h1>{{ usuario.nombreUsuario }}</h1>
                <span class="badge-rol" :class="usuario.rol">{{ rolDisplay }}</span>
              </div>
            </div>

            <div class="input-group">
              <label>Sobre mí</label>
              <textarea v-model="editDesc" rows="4" placeholder="Cuéntanos algo sobre ti..."></textarea>
            </div>

            <div class="input-group">
              <label class="highlight-label">
                {{ esProfesor ? "Lenguajes que enseño" : "Lenguajes que quiero aprender" }}
              </label>
              <div class="languages-grid">
                <label v-for="lang in lenguajesDisponibles" :key="lang" class="lang-checkbox">
                  <input v-model="editLenguajes" type="checkbox" :value="lang" />
                  <span>{{ lang }}</span>
                </label>
              </div>
            </div>

            <p v-if="errorEdicion" class="error-msg">{{ errorEdicion }}</p>

            <div class="botones-edicion">
              <button class="btn-cancelar" @click="cancelarEdicion">Cancelar</button>
              <button class="btn-guardar" :disabled="guardando" @click="guardarPerfil">
                {{ guardando ? "Guardando..." : "Guardar cambios" }}
              </button>
            </div>
          </template>

        </div>
      </section>

      <!-- COLUMNA DERECHA: SUGERENCIAS -->
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
              <p class="habilidades-preview">{{ perfil.lenguajes.slice(0, 2).join(", ") }}</p>
            </div>
          </div>
        </div>
      </section>
    </div>

    <!-- MODAL -->
    <div v-if="perfilSeleccionado" class="modal-overlay" @click.self="cerrarModal">
      <div class="modal-card card">
        <button class="btn-cerrar" @click="cerrarModal">✕</button>
        <img :src="perfilSeleccionado.avatar" class="avatar-grande center" />
        <h2 class="text-center">{{ perfilSeleccionado.nombre }}</h2>
        <div class="text-center mb-20">
          <span class="badge-rol" :class="perfilSeleccionado.rol.toLowerCase()">{{ perfilSeleccionado.rol }}</span>
        </div>
        <p class="modal-bio">{{ perfilSeleccionado.bio }}</p>
        <div class="tags-section">
          <h3>Lenguajes</h3>
          <div class="tags-container">
            <span v-for="lang in perfilSeleccionado.lenguajes" :key="lang" class="tag lang-tag">{{ lang }}</span>
          </div>
        </div>
        <button class="btn-primary mt-20">Enviar Mensaje a {{ perfilSeleccionado.nombre }}</button>
      </div>
    </div>

  </div>
</template>

<style scoped>
.perfil-page {
  padding: 40px 5%;
  min-height: 85vh;
}

.estado {
  text-align: center;
  color: var(--text-muted);
  margin-top: 60px;
  font-size: 1rem;
}
.estado.error { color: var(--primary); }

.perfil-layout {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 40px;
  max-width: 1200px;
  margin: 0 auto;
}

@media (max-width: 850px) {
  .perfil-layout { grid-template-columns: 1fr; }
}

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

.badge-rol {
  padding: 6px 15px;
  border-radius: 20px;
  font-weight: bold;
  font-size: 0.9rem;
  color: white;
}
.badge-rol.estudiante { background: #0d8abc; }
.badge-rol.profesor   { background: var(--primary); }
.badge-rol.mentor     { background: var(--primary); }

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

/* EDICIÓN */
.input-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.input-group label {
  font-weight: 600;
  color: var(--text-muted);
  font-size: 0.9rem;
}
.highlight-label { color: var(--primary) !important; }

.input-group textarea {
  width: 100%;
  padding: 12px;
  background: var(--bg-main);
  border: 1px solid var(--border);
  border-radius: 12px;
  color: var(--text-main);
  font-size: 1rem;
  resize: vertical;
  outline: none;
  transition: border-color 0.3s;
  box-sizing: border-box;
}
.input-group textarea:focus { border-color: var(--primary); }

.languages-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}
.lang-checkbox {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: var(--text-main);
  font-size: 0.9rem;
}
.lang-checkbox input[type="checkbox"] {
  accent-color: var(--primary);
  transform: scale(1.2);
}

.botones-edicion {
  display: flex;
  gap: 15px;
}
.btn-cancelar {
  flex: 1;
  padding: 15px;
  background: transparent;
  border: 2px solid var(--border);
  border-radius: 12px;
  color: var(--text-main);
  font-weight: bold;
  cursor: pointer;
  transition: border-color 0.3s;
}
.btn-cancelar:hover { border-color: var(--primary); color: var(--primary); }

.btn-guardar {
  flex: 2;
  padding: 15px;
  background: linear-gradient(to right, var(--primary), #b5141a);
  color: white;
  border: none;
  border-radius: 12px;
  font-weight: bold;
  cursor: pointer;
  transition: transform 0.2s;
}
.btn-guardar:hover:not(:disabled) { transform: scale(1.02); }
.btn-guardar:disabled { opacity: 0.6; cursor: not-allowed; }

.error-msg {
  color: var(--primary);
  font-size: 0.9rem;
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

/* SUGERENCIAS */
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
  transition: transform 0.2s, border-color 0.2s;
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

/* MODAL */
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(8px);
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
  from { transform: scale(0.9); opacity: 0; }
  to   { transform: scale(1);   opacity: 1; }
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
.btn-cerrar:hover { color: var(--primary); }

.text-center { text-align: center; }
.mb-20 { margin-bottom: 20px; }
.mt-20 { margin-top: 20px; }
.modal-bio { text-align: center; font-style: italic; }

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
