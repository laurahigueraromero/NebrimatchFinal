<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import ThemeToggle from "../components/ThemeToggle.vue"; // Importamos la palanquita

const router = useRouter();

// Variable para alternar entre Iniciar Sesión (true) y Registro (false)
const isLogin = ref(true);

// --- ESTADOS PARA EL LOGIN ---
const email = ref("");
const password = ref("");
const error = ref(false);

// --- ESTADOS PARA EL REGISTRO ---
const regEmail = ref("");
const regPassword = ref("");
const regRole = ref(""); // Guardará 'estudiante' o 'mentor'
const regLanguages = ref([]); // Vue guardará aquí los lenguajes seleccionados en formato Array

// Lista de lenguajes disponibles para seleccionar
const lenguajesDisponibles = [
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

// Función para Iniciar Sesión
const handleLogin = () => {
  if (email.value === "alumno@nebrija.es" && password.value === "1234") {
    router.push("/comunidades");
  } else {
    error.value = true;
  }
};

// Función para Registrarse
const handleRegister = () => {
  console.log("Nuevo usuario registrado:", {
    email: regEmail.value,
    password: regPassword.value,
    rol: regRole.value,
    lenguajes: regLanguages.value,
  });

  router.push("/comunidades");
};
</script>

<template>
  <div class="login-page-wrapper">
    <!-- NUEVA CABECERA DEL LOGIN -->
    <header class="top-header">
      <router-link to="/" class="btn-volver">← Volver al inicio</router-link>
      <ThemeToggle />
    </header>

    <div class="login-container">
      <div class="login-card">
        <h1 class="logo">Nebri<span>Match</span></h1>

        <!-- TÍTULO DINÁMICO -->
        <p class="subtitle">
          {{
            isLogin
              ? "Inicia sesión para continuar"
              : "Crea tu cuenta paso a paso"
          }}
        </p>

        <!-- ========================================== -->
        <!-- FORMULARIO DE INICIO DE SESIÓN             -->
        <!-- ========================================== -->
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

          <p v-if="error" class="error-msg">
            Credenciales incorrectas. Intenta de nuevo.
          </p>

          <button type="submit" class="btn-primary">Entrar</button>
        </form>

        <!-- ========================================== -->
        <!-- FORMULARIO DE REGISTRO                     -->
        <!-- ========================================== -->
        <form v-else @submit.prevent="handleRegister" class="form-content">
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

          <!-- SELECCIÓN DE ROL -->
          <div class="input-group">
            <label>¿Qué vienes a hacer a NebriMatch?</label>
            <div class="role-selector">
              <label class="role-option">
                <input
                  v-model="regRole"
                  type="radio"
                  value="estudiante"
                  required
                />
                <span>Aprender (Estudiante)</span>
              </label>
              <label class="role-option">
                <input v-model="regRole" type="radio" value="mentor" required />
                <span>Enseñar (Mentor)</span>
              </label>
            </div>
          </div>

          <!-- SELECCIÓN DE LENGUAJES (Solo aparece si ya ha elegido un rol) -->
          <div class="input-group" v-if="regRole">
            <label class="highlight-label">
              ¿Qué lenguajes quieres
              {{ regRole === "mentor" ? "enseñar" : "aprender" }}?
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

          <button type="submit" class="btn-primary">Registrarme</button>
        </form>

        <!-- BOTÓN PARA CAMBIAR ENTRE LOGIN Y REGISTRO -->
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
.input-group input[type="password"] {
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
