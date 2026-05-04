<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { authService } from '../services/api';

const router = useRouter();

const email = ref('');
const password = ref('');
const error = ref(false);
const cargando = ref(false);

const handleLogin = async () => {
  error.value = false;
  cargando.value = true;
  try {
    const { data: usuario } = await authService.login(email.value, password.value);
    localStorage.setItem('usuario', JSON.stringify(usuario));
    router.push('/comunidades');
  } catch {
    error.value = true;
    setTimeout(() => error.value = false, 3000);
  } finally {
    cargando.value = false;
  }
};
</script>

<template>
  <div class="login-container">
    <div class="login-card">
      <h1 class="logo">NebriMatch</h1>
      <p class="subtitle">Inicia sesión para encontrar a tu mentor</p>

      <form @submit.prevent="handleLogin">
        <div class="input-group">
          <label>Email Institucional</label>
          <input 
            v-model="email" 
            type="email" 
            placeholder="ejemplo@nebrija.es" 
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

        <p v-if="error" class="error-msg">⚠️ Credenciales incorrectas</p>

        <button type="submit" class="btn-login" :disabled="cargando">
          {{ cargando ? 'Entrando...' : 'Entrar' }}
        </button>
      </form>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
}

.login-card {
  background: var(--bg-card);
  padding: 50px 40px;
  border-radius: 20px;
  border: 1px solid var(--border);
  box-shadow: 0 15px 35px rgba(0,0,0,0.5);
  width: 100%;
  max-width: 420px;
  text-align: center;
}

.logo {
  color: var(--primary);
  font-size: 2.5rem;
  margin-bottom: 10px;
}
.logo span { color: var(--text-main); }

.subtitle {
  color: var(--text-muted);
  margin-bottom: 30px;
}

.input-group {
  text-align: left;
  margin-bottom: 20px;
}

.input-group label {
  display: block;
  font-weight: 600;
  margin-bottom: 8px;
  color: var(--text-muted);
  font-size: 0.9rem;
}

.input-group input {
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

.btn-login {
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

.btn-login:hover {
  transform: scale(1.02);
}

.error-msg {
  color: var(--primary-hover);
  font-size: 0.9rem;
  margin-bottom: 15px;
}
</style>