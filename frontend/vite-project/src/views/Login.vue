<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import ThemeToggle from "../components/ThemeToggle.vue";
import { authService, usuarioService, lenguajeService } from "../services/api";

const router = useRouter();

const isLogin = ref(true);

// --- ESTADOS PARA EL LOGIN ---
const email = ref("");
const password = ref("");
const error = ref("");
const cargandoLogin = ref(false);

// --- ESTADOS PARA EL REGISTRO ---
const regNombre = ref("");
const regEmail = ref("");
const regPassword = ref("");
const regRole = ref("");
const regLanguages = ref([]);
const errorReg = ref("");
const cargandoReg = ref(false);

const lenguajesDisponibles = ref([]);

onMounted(async () => {
  try {
    const res = await lenguajeService.obtenerTodos();
    lenguajesDisponibles.value = res.data;
  } catch {
    // Si falla, usamos la lista local como fallback
    lenguajesDisponibles.value = [
      "Vue.js",
      "React",
      "Angular",
      "Java",
      "Python",
      "Node.js",
      "Spring Boot",
      "C++",
      "Go",
      "Docker",
    ];
  }
});

async function handleLogin() {
  error.value = "";

  // ==========================================
  // BYPASS DE DESARROLLO (BORRAR LUEGO)
  // ==========================================
  if (email.value === "admin@nebrija.es" && password.value === "1234") {
    // Simulamos que el backend nos ha devuelto un usuario válido
    sessionStorage.setItem(
      "usuario",
      JSON.stringify({ nombre: "Espe Dev", rol: "estudiante" }),
    );
    router.push("/comunidades");
    return; // Cortamos la función aquí para que no ejecute el try/catch de abajo
  }
  // ==========================================

  cargandoLogin.value = true;
  try {
    const res = await authService.login(email.value, password.value);
    sessionStorage.setItem("usuario", JSON.stringify(res.data));
    router.push("/comunidades");
  } catch {
    error.value = "Credenciales incorrectas. Intenta de nuevo.";
  } finally {
    cargandoLogin.value = false;
  }
}

async function handleRegister() {
  errorReg.value = "";
  if (!regRole.value) {
    errorReg.value = "Selecciona un rol.";
    return;
  }
  if (regLanguages.value.length === 0) {
    errorReg.value = "Selecciona al menos un lenguaje.";
    return;
  }

  cargandoReg.value = true;
  const lenguajesStr = regLanguages.value.join(",");

  try {
    const res = await usuarioService.crear({
      nombreUsuario: regNombre.value,
      email: regEmail.value,
      password: regPassword.value,
      descripcion: "",
      lenguajesAEnsenar: regRole.value === "profesor" ? lenguajesStr : "",
      lenguajesAAprender: regRole.value === "estudiante" ? lenguajesStr : "",
      rol: regRole.value,
    });
    sessionStorage.setItem("usuario", JSON.stringify(res.data));
    router.push("/comunidades");
  } catch (e) {
    errorReg.value =
      e.response?.data || "Error al registrarse. Inténtalo de nuevo.";
  } finally {
    cargandoReg.value = false;
  }
}
</script>

<template>
  <div class="login-page-wrapper">
    <div
      style="
        background-color: #ff9800;
        color: white;
        text-align: center;
        padding: 10px;
        font-weight: bold;
        font-size: 0.9rem;
      "
    >
      🛠️ MODO DEV: Usa "admin@nebrija.es" y clave "1234" para entrar sin base de
      datos.
    </div>
    <header class="top-header">
      <router-link to="/" class="btn-volver">← Volver al inicio</router-link>
      <ThemeToggle />
    </header>

    <div class="login-container">
      <div class="login-card">
        <h1 class="logo">Nebri<span>Match</span></h1>

        <p class="subtitle">
          {{
            isLogin
              ? "Inicia sesión para continuar"
              : "Crea tu cuenta paso a paso"
          }}
        </p>

        <form v-if="isLogin" @submit.prevent="handleLogin" class="form-content">
          <div class="input-group">
            <label>Correo institucional</label>
            <input
              v-model="email"
              type="email"
              placeholder="alumno@nebrija.es"
              required
            />
          </div>

          <div class="input-group">
            <label>Contraseña</label>
            <input
              v-model="password"
              type="password"
              placeholder="••••••••"
              required
            />
          </div>

          <p v-if="error" class="error-msg">{{ error }}</p>

          <button type="submit" class="btn-primary" :disabled="cargandoLogin">
            {{ cargandoLogin ? "Entrando..." : "Entrar" }}
          </button>
        </form>

        <form v-else @submit.prevent="handleRegister" class="form-content">
          <div class="input-group">
            <label>Nombre de usuario</label>
            <input
              v-model="regNombre"
              type="text"
              placeholder="tu_nombre"
              required
            />
          </div>

          <div class="input-group">
            <label>Correo electrónico</label>
            <input
              v-model="regEmail"
              type="email"
              placeholder="tu@nebrija.es"
              required
            />
          </div>

          <div class="input-group">
            <label>Contraseña</label>
            <input
              v-model="regPassword"
              type="password"
              placeholder="Mínimo 8 caracteres"
              required
            />
          </div>

          <div class="input-group">
            <label>¿Qué vienes a hacer a NebriMatch?</label>
            <div class="role-selector">
              <label class="role-option">
                <input v-model="regRole" type="radio" value="estudiante" />
                <span>Aprender (Estudiante)</span>
              </label>
              <label class="role-option">
                <input v-model="regRole" type="radio" value="profesor" />
                <span>Enseñar (Mentor)</span>
              </label>
            </div>
          </div>

          <div class="input-group" v-if="regRole">
            <label class="highlight-label">
              ¿Qué lenguajes quieres
              {{ regRole === "profesor" ? "enseñar" : "aprender" }}?
            </label>
            <div class="languages-grid">
              <label
                v-for="lenguaje in lenguajesDisponibles"
                :key="lenguaje"
                class="lang-checkbox"
              >
                <input
                  v-model="regLanguages"
                  type="checkbox"
                  :value="lenguaje"
                />
                <span>{{ lenguaje }}</span>
              </label>
            </div>
          </div>

          <p v-if="errorReg" class="error-msg">{{ errorReg }}</p>

          <button type="submit" class="btn-primary" :disabled="cargandoReg">
            {{ cargandoReg ? "Registrando..." : "Registrarme" }}
          </button>
        </form>

        <div class="toggle-section">
          <p v-if="isLogin">
            ¿No tienes cuenta?
            <span class="toggle-link" @click="isLogin = false"
              >Regístrate aquí</span
            >
          </p>
          <p v-else>
            ¿Ya tienes cuenta?
            <span class="toggle-link" @click="isLogin = true"
              >Inicia sesión</span
            >
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* Estructura para añadir la cabecera */
.login-page-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.top-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 40px;
}

.btn-volver {
  color: var(--text-muted);
  text-decoration: none;
  font-weight: bold;
  font-size: 1rem;
  transition: color 0.3s;
}

.btn-volver:hover {
  color: var(--primary);
}

/* Resto del diseño del Login/Registro */
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  flex: 1;
  padding: 20px;
}

.login-card {
  background: var(--bg-card);
  padding: 40px;
  border-radius: 20px;
  border: 1px solid var(--border);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
  width: 100%;
  max-width: 450px;
  text-align: center;
}

.logo {
  color: var(--primary);
  font-size: 2.5rem;
  margin-bottom: 5px;
}
.logo span {
  color: var(--text-main);
}
.subtitle {
  color: var(--text-muted);
  margin-bottom: 30px;
  font-size: 0.95rem;
}

.form-content {
  text-align: left;
}

.input-group {
  margin-bottom: 20px;
}

.input-group label {
  display: block;
  font-weight: 600;
  margin-bottom: 8px;
  color: var(--text-muted);
  font-size: 0.9rem;
}

.input-group input[type="email"],
.input-group input[type="password"],
.input-group input[type="text"] {
  width: 100%;
  padding: 15px;
  background: var(--bg-main);
  border: 1px solid var(--border);
  border-radius: 12px;
  color: var(--text-main);
  font-size: 1rem;
  transition: all 0.3s;
  outline: none;
}

.input-group input:focus {
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(215, 24, 32, 0.2);
}

/* ROLES */
.role-selector {
  display: flex;
  gap: 15px;
}
.role-option {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 10px;
  background: var(--bg-main);
  padding: 12px;
  border-radius: 10px;
  border: 1px solid var(--border);
  cursor: pointer;
  transition: border-color 0.3s;
}
.role-option:hover {
  border-color: var(--primary);
}
.role-option input[type="radio"] {
  accent-color: var(--primary);
  transform: scale(1.2);
}
.role-option span {
  color: var(--text-main);
  font-size: 0.9rem;
}

/* LENGUAJES */
.highlight-label {
  color: var(--primary) !important;
  font-size: 0.95rem !important;
}
.languages-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
  max-height: 150px;
  overflow-y: auto;
  padding-right: 5px;
}
.languages-grid::-webkit-scrollbar {
  width: 6px;
}
.languages-grid::-webkit-scrollbar-thumb {
  background: var(--border);
  border-radius: 10px;
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

/* BOTONES Y ENLACES */
.btn-primary {
  width: 100%;
  padding: 15px;
  margin-top: 10px;
  background: linear-gradient(to right, var(--primary), #b5141a);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 1.1rem;
  font-weight: bold;
  cursor: pointer;
  transition: transform 0.2s;
}
.btn-primary:hover {
  transform: scale(1.02);
}

.error-msg {
  color: var(--primary-hover);
  font-size: 0.9rem;
  margin-bottom: 15px;
  text-align: center;
}

.toggle-section {
  margin-top: 25px;
  font-size: 0.9rem;
  color: var(--text-muted);
}
.toggle-link {
  color: var(--primary);
  font-weight: bold;
  cursor: pointer;
  margin-left: 5px;
}
.toggle-link:hover {
  text-decoration: underline;
}
</style>
