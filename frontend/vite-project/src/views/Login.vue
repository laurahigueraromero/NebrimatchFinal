<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router'; // Para poder saltar de página

const router = useRouter();

// Variables para el formulario
const email = ref('');
const password = ref('');
const error = ref(false);

const handleLogin = () => {
  // Simulamos una validación simple para el TFG
  if (email.value === 'alumno@nebrija.es' && password.value === '1234') {
    console.log("Login correcto");
    // Redirigimos al usuario a la página principal
    router.push('/para-ti');
  } else {
    error.value = true;
    setTimeout(() => error.value = false, 3000); // Quitamos el error a los 3 seg.
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

        <button type="submit" class="btn-login">Entrar</button>
      </form>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 90vh;
}

.login-card {
  background: white;
  padding: 40px;
  border-radius: 20px;
  box-shadow: 0 10px 25px rgba(0,0,0,0.1);
  width: 100%;
  max-width: 400px;
  text-align: center;
}

.logo {
  color: #d71820;
  font-size: 2.5rem;
  margin-bottom: 10px;
}

.subtitle {
  color: #666;
  margin-bottom: 30px;
}

.input-group {
  text-align: left;
  margin-bottom: 20px;
}

.input-group label {
  display: block;
  font-weight: bold;
  margin-bottom: 5px;
  color: #444;
}

.input-group input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 10px;
  box-sizing: border-box; /* Para que el padding no ensanche el input */
}

.btn-login {
  width: 100%;
  padding: 15px;
  background: #d71820;
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 1.1rem;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.3s;
}

.btn-login:hover {
  background: #b5141a;
}

.error-msg {
  color: #d71820;
  font-weight: bold;
  margin-bottom: 15px;
}
</style>