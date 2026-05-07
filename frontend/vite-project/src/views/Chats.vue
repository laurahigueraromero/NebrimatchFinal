<script setup>
import { ref, computed } from "vue";

// --- DATOS FALSOS DE PRUEBA ---
const contactos = ref([
  {
    id: 1,
    nombre: "Rodrigo",
    rol: "Mentor",
    avatar:
      "https://ui-avatars.com/api/?name=Rodrigo&background=0D8ABC&color=fff",
    online: true,
    mensajes: [
      {
        id: 1,
        texto:
          "¡Hola Espe! He visto en tu perfil que buscas ayuda con Vue.js para el TFG.",
        mio: false,
        hora: "10:30",
      },
      {
        id: 2,
        texto:
          "¡Hola Rodrigo! Sí, estoy montando la interfaz y me vendría genial algún consejo sobre componentes.",
        mio: true,
        hora: "10:35",
      },
      {
        id: 3,
        texto:
          "Perfecto. Pásame tu repositorio cuando quieras y organizamos una videollamada para revisarlo.",
        mio: false,
        hora: "10:38",
      },
    ],
  },
  {
    id: 2,
    nombre: "Carlos",
    rol: "Estudiante",
    avatar:
      "https://ui-avatars.com/api/?name=Carlos&background=FF9800&color=fff",
    online: false,
    mensajes: [
      {
        id: 1,
        texto: "¿Qué tal llevas la parte de la base de datos?",
        mio: false,
        hora: "Ayer",
      },
    ],
  },
  {
    id: 3,
    nombre: "Ana (IA Lab)",
    rol: "Comunidad",
    avatar: "https://ui-avatars.com/api/?name=IA&background=4CAF50&color=fff",
    online: true,
    mensajes: [
      {
        id: 1,
        texto: "He subido un nuevo dataset al Drive del grupo.",
        mio: false,
        hora: "Lunes",
      },
      {
        id: 2,
        texto: "¡Gracias Ana! Le echo un ojo esta tarde.",
        mio: true,
        hora: "Lunes",
      },
    ],
  },
]);

// Estado para saber con quién estamos hablando
const contactoActivo = ref(contactos.value[0]);
const nuevoMensaje = ref("");

// Función para cambiar de chat
const seleccionarContacto = (contacto) => {
  contactoActivo.value = contacto;
};

// Función para simular el envío de un mensaje
const enviarMensaje = () => {
  if (nuevoMensaje.value.trim() === "") return;

  const horaActual = new Date().toLocaleTimeString([], {
    hour: "2-digit",
    minute: "2-digit",
  });

  contactoActivo.value.mensajes.push({
    id: Date.now(),
    texto: nuevoMensaje.value,
    mio: true,
    hora: horaActual,
  });

  nuevoMensaje.value = ""; // Limpiamos el input
};
</script>

<template>
  <div class="chats-page">
    <div class="chat-layout">
      <aside class="sidebar-chats">
        <div class="sidebar-header">
          <h2>Tus Matches</h2>
          <div class="search-box">
            <i class="fa-solid fa-magnifying-glass"></i>
            <input type="text" placeholder="Buscar chat..." />
          </div>
        </div>

        <div class="contactos-list">
          <div
            v-for="contacto in contactos"
            :key="contacto.id"
            class="contacto-item"
            :class="{ activo: contactoActivo.id === contacto.id }"
            @click="seleccionarContacto(contacto)"
          >
            <div class="avatar-wrapper">
              <img :src="contacto.avatar" alt="Avatar" class="avatar-img" />
              <span v-if="contacto.online" class="status-dot"></span>
            </div>

            <div class="contacto-info">
              <div class="contacto-top">
                <h4>{{ contacto.nombre }}</h4>
                <span class="hora">{{
                  contacto.mensajes[contacto.mensajes.length - 1].hora
                }}</span>
              </div>
              <p class="ultimo-mensaje">
                {{ contacto.mensajes[contacto.mensajes.length - 1].texto }}
              </p>
            </div>
          </div>
        </div>
      </aside>

      <main class="chat-area">
        <template v-if="contactoActivo">
          <header class="chat-header">
            <div class="chat-header-info">
              <img
                :src="contactoActivo.avatar"
                alt="Avatar"
                class="avatar-header"
              />
              <div>
                <h3>{{ contactoActivo.nombre }}</h3>
                <span class="rol-badge">{{ contactoActivo.rol }}</span>
              </div>
            </div>
            <div class="chat-header-actions">
              <button class="icon-btn" title="Agendar Mentoría">
                <i class="fa-regular fa-calendar-check"></i>
              </button>
              <button class="icon-btn" title="Opciones">
                <i class="fa-solid fa-ellipsis-vertical"></i>
              </button>
            </div>
          </header>

          <div class="messages-container">
            <div
              v-for="msg in contactoActivo.mensajes"
              :key="msg.id"
              class="message-wrapper"
              :class="{ mine: msg.mio, theirs: !msg.mio }"
            >
              <div class="bubble">
                <p>{{ msg.texto }}</p>
                <span class="msg-time">{{ msg.hora }}</span>
              </div>
            </div>
          </div>

          <div class="chat-input-area">
            <button class="btn-attach">
              <i class="fa-solid fa-paperclip"></i>
            </button>
            <input
              v-model="nuevoMensaje"
              @keyup.enter="enviarMensaje"
              type="text"
              placeholder="Escribe un mensaje..."
            />
            <button class="btn-send" @click="enviarMensaje">
              <i class="fa-solid fa-paper-plane"></i>
            </button>
          </div>
        </template>

        <div v-else class="no-chat-selected">
          <i class="fa-regular fa-comments icon-huge"></i>
          <h2>Selecciona un chat</h2>
          <p>Elige un match de la izquierda para empezar a hablar.</p>
        </div>
      </main>
    </div>
  </div>
</template>

<style scoped>
.chats-page {
  padding: 30px 5%;
  height: 85vh; /* Ocupa el alto de la pantalla sin hacer scroll en la página entera */
  display: flex;
  justify-content: center;
}

.chat-layout {
  display: flex;
  width: 100%;
  max-width: 1200px;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: 20px;
  overflow: hidden; /* Corta lo que sobresale para mantener los bordes redondos */
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
}

/* --- SIDEBAR (Lista de contactos) --- */
.sidebar-chats {
  width: 350px;
  border-right: 1px solid var(--border);
  display: flex;
  flex-direction: column;
  background: var(--bg-card);
}

.sidebar-header {
  padding: 25px 20px;
  border-bottom: 1px solid var(--border);
}

.sidebar-header h2 {
  color: var(--text-main);
  margin: 0 0 15px 0;
  font-size: 1.6rem;
}

.search-box {
  display: flex;
  align-items: center;
  background: var(--bg-main);
  padding: 10px 15px;
  border-radius: 12px;
  border: 1px solid var(--border);
}

.search-box i {
  color: var(--text-muted);
  margin-right: 10px;
}
.search-box input {
  background: transparent;
  border: none;
  color: var(--text-main);
  width: 100%;
  outline: none;
}

.contactos-list {
  flex: 1;
  overflow-y: auto;
}
.contactos-list::-webkit-scrollbar {
  width: 5px;
}
.contactos-list::-webkit-scrollbar-thumb {
  background: var(--border);
  border-radius: 10px;
}

.contacto-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 20px;
  cursor: pointer;
  border-bottom: 1px solid var(--border);
  transition: background 0.2s;
}

.contacto-item:hover {
  background: rgba(215, 24, 32, 0.05);
}
.contacto-item.activo {
  background: rgba(215, 24, 32, 0.1);
  border-left: 4px solid var(--primary);
}

.avatar-wrapper {
  position: relative;
}
.avatar-img {
  width: 55px;
  height: 55px;
  border-radius: 50%;
  object-fit: cover;
}
.status-dot {
  position: absolute;
  bottom: 2px;
  right: 2px;
  width: 14px;
  height: 14px;
  background: #4caf50;
  border: 2px solid var(--bg-card);
  border-radius: 50%;
}

.contacto-info {
  flex: 1;
  overflow: hidden;
}
.contacto-top {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  margin-bottom: 5px;
}
.contacto-top h4 {
  margin: 0;
  color: var(--text-main);
  font-size: 1.1rem;
}
.hora {
  font-size: 0.8rem;
  color: var(--text-muted);
}
.ultimo-mensaje {
  margin: 0;
  color: var(--text-muted);
  font-size: 0.9rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis; /* Puntos suspensivos si es largo */
}

/* --- AREA DE CHAT (Derecha) --- */
.chat-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: var(--bg-main); /* Un tono más oscuro que el sidebar */
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 30px;
  background: var(--bg-card);
  border-bottom: 1px solid var(--border);
}

.chat-header-info {
  display: flex;
  align-items: center;
  gap: 15px;
}
.avatar-header {
  width: 50px;
  height: 50px;
  border-radius: 50%;
}
.chat-header-info h3 {
  margin: 0 0 5px 0;
  color: var(--text-main);
}
.rol-badge {
  font-size: 0.8rem;
  color: var(--primary);
  font-weight: bold;
  background: rgba(215, 24, 32, 0.1);
  padding: 4px 10px;
  border-radius: 10px;
}

.chat-header-actions {
  display: flex;
  gap: 15px;
}
.icon-btn {
  background: transparent;
  border: none;
  color: var(--text-muted);
  font-size: 1.2rem;
  cursor: pointer;
  transition: color 0.2s;
}
.icon-btn:hover {
  color: var(--primary);
}

/* LOS MENSAJES (Burbujas Asimétricas) */
.messages-container {
  flex: 1;
  padding: 30px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.messages-container::-webkit-scrollbar {
  width: 6px;
}
.messages-container::-webkit-scrollbar-thumb {
  background: var(--border);
  border-radius: 10px;
}

.message-wrapper {
  display: flex;
  width: 100%;
}
.message-wrapper.mine {
  justify-content: flex-end;
}
.message-wrapper.theirs {
  justify-content: flex-start;
}

.bubble {
  max-width: 65%;
  padding: 15px 20px;
  position: relative;
}

.bubble p {
  margin: 0;
  line-height: 1.5;
  font-size: 0.95rem;
}
.msg-time {
  display: block;
  font-size: 0.75rem;
  margin-top: 8px;
  opacity: 0.7;
  text-align: right;
}

/* Burbuja del Contacto */
.theirs .bubble {
  background: var(--bg-card);
  color: var(--text-main);
  border: 1px solid var(--border);
  border-radius: 20px 20px 20px 5px; /* El pico apunta abajo a la izquierda */
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
}

/* Burbuja Mía (Roja) */
.mine .bubble {
  background: linear-gradient(135deg, var(--primary), #ff5e62);
  color: white;
  border-radius: 20px 20px 5px 20px; /* El pico apunta abajo a la derecha */
  box-shadow: 0 4px 15px rgba(215, 24, 32, 0.3);
}

/* INPUT DE TEXTO FLOTANTE */
.chat-input-area {
  padding: 20px 30px;
  background: var(--bg-card);
  border-top: 1px solid var(--border);
  display: flex;
  align-items: center;
  gap: 15px;
}

.chat-input-area input {
  flex: 1;
  padding: 15px 25px;
  border-radius: 30px;
  border: 1px solid var(--border);
  background: var(--bg-main);
  color: var(--text-main);
  font-size: 1rem;
  outline: none;
  transition: border-color 0.3s;
}
.chat-input-area input:focus {
  border-color: var(--primary);
}

.btn-attach {
  background: transparent;
  border: none;
  color: var(--text-muted);
  font-size: 1.2rem;
  cursor: pointer;
}
.btn-attach:hover {
  color: var(--text-main);
}

.btn-send {
  background: var(--primary);
  color: white;
  border: none;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  font-size: 1.2rem;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: transform 0.2s;
  box-shadow: 0 4px 10px rgba(215, 24, 32, 0.3);
}
.btn-send:hover {
  transform: scale(1.05);
}

/* Pantalla Vacía */
.no-chat-selected {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: var(--text-muted);
  text-align: center;
}
.icon-huge {
  font-size: 5rem;
  color: var(--border);
  margin-bottom: 20px;
}
.no-chat-selected h2 {
  color: var(--text-main);
  margin-bottom: 10px;
}
</style>
