<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick } from "vue";
import { Client } from "@stomp/stompjs";
import { conversacionService } from "../services/api";

const usuarioActual = JSON.parse(sessionStorage.getItem("usuario"));

// ── ESTADO ────────────────────────────────────────────────
const conversaciones = ref([]);   // [{ conv, otro, ultimoMsg }]
const conversacionActiva = ref(null);
const mensajes = ref([]);
const nuevoMensaje = ref("");
const cargando = ref(true);
const messagesEl = ref(null);
const busqueda = ref("");

// ── WEBSOCKET ─────────────────────────────────────────────
let stompClient = null;
let subscripcion = null;
let convIdPendiente = null;

// ── HELPERS ───────────────────────────────────────────────
function otroUsuario(conv) {
  return conv.usuario1Id === usuarioActual?.id
    ? { id: conv.usuario2Id, nombre: conv.usuario2Nombre }
    : { id: conv.usuario1Id, nombre: conv.usuario1Nombre };
}

function avatarUrl(nombre) {
  return `https://ui-avatars.com/api/?name=${encodeURIComponent(nombre)}&background=d71820&color=fff`;
}

function formatHora(fechaStr) {
  if (!fechaStr) return "";
  const fecha = new Date(fechaStr);
  const hoy = new Date();
  if (fecha.toDateString() === hoy.toDateString()) {
    return fecha.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" });
  }
  return fecha.toLocaleDateString([], { day: "2-digit", month: "2-digit" });
}

function scrollBottom() {
  nextTick(() => {
    if (messagesEl.value)
      messagesEl.value.scrollTop = messagesEl.value.scrollHeight;
  });
}

function subscribirA(convId) {
  subscripcion?.unsubscribe();
  subscripcion = stompClient.subscribe(
    `/topic/conversations/${convId}`,
    (frame) => {
      const msg = JSON.parse(frame.body);
      mensajes.value.push(msg);
      const item = conversaciones.value.find((c) => c.conv.id === convId);
      if (item) item.ultimoMsg = msg;
      scrollBottom();
    }
  );
}

// ── CICLO DE VIDA ─────────────────────────────────────────
onMounted(async () => {
  if (!usuarioActual) {
    cargando.value = false;
    return;
  }

  try {
    const res = await conversacionService.obtenerPorUsuario(usuarioActual.id);
    const convs = res.data;
    const ultimosMsgs = await Promise.all(
      convs.map((c) =>
        conversacionService
          .obtenerUltimoMensaje(c.id)
          .then((r) => r.data)
          .catch(() => null)
      )
    );
    conversaciones.value = convs.map((c, i) => ({
      conv: c,
      otro: otroUsuario(c),
      ultimoMsg: ultimosMsgs[i],
    }));
  } catch {
    // sin conversaciones
  } finally {
    cargando.value = false;
  }

  stompClient = new Client({
    brokerURL: import.meta.env.VITE_WS_URL || "ws://localhost:8080/ws-native",
    reconnectDelay: 5000,
    onConnect: () => {
      if (convIdPendiente) {
        subscribirA(convIdPendiente);
        convIdPendiente = null;
      }
    },
  });
  stompClient.activate();
});

onUnmounted(() => {
  subscripcion?.unsubscribe();
  stompClient?.deactivate();
});

// ── ACCIONES ──────────────────────────────────────────────
async function seleccionarConversacion(item) {
  if (conversacionActiva.value?.id === item.conv.id) return;
  conversacionActiva.value = item.conv;

  try {
    const res = await conversacionService.obtenerMensajes(item.conv.id);
    mensajes.value = res.data;
    scrollBottom();
  } catch {
    mensajes.value = [];
  }

  if (stompClient?.connected) {
    subscribirA(item.conv.id);
  } else {
    convIdPendiente = item.conv.id;
  }
}

function enviarMensaje() {
  const texto = nuevoMensaje.value.trim();
  if (!texto || !conversacionActiva.value) return;
  if (!stompClient?.connected) return;

  stompClient.publish({
    destination: `/app/chat.enviar/${conversacionActiva.value.id}`,
    body: JSON.stringify({
      emisorId: usuarioActual.id,
      contenido: texto,
    }),
  });

  nuevoMensaje.value = "";
}

const conversacionesFiltradas = computed(() =>
  conversaciones.value.filter((item) =>
    item.otro.nombre.toLowerCase().includes(busqueda.value.toLowerCase())
  )
);
</script>

<template>
  <div class="chats-page">
    <div v-if="!usuarioActual" class="sin-sesion">
      <p>Debes iniciar sesión para ver tus chats.</p>
    </div>

    <div v-else class="chat-layout">
      <!-- SIDEBAR -->
      <aside class="sidebar-chats">
        <div class="sidebar-header">
          <h2>Tus Chats</h2>
          <div class="search-box">
            <i class="fa-solid fa-magnifying-glass"></i>
            <input v-model="busqueda" type="text" placeholder="Buscar chat..." />
          </div>
        </div>

        <div class="contactos-list">
          <p v-if="cargando" class="estado-sidebar">Cargando...</p>
          <p v-else-if="conversaciones.length === 0" class="estado-sidebar">
            No tienes conversaciones aún.
          </p>

          <div
            v-for="item in conversacionesFiltradas"
            :key="item.conv.id"
            class="contacto-item"
            :class="{ activo: conversacionActiva?.id === item.conv.id }"
            @click="seleccionarConversacion(item)"
          >
            <div class="avatar-wrapper">
              <img :src="avatarUrl(item.otro.nombre)" alt="Avatar" class="avatar-img" />
            </div>
            <div class="contacto-info">
              <div class="contacto-top">
                <h4>{{ item.otro.nombre }}</h4>
                <span class="hora">{{ formatHora(item.ultimoMsg?.fechaEnvio) }}</span>
              </div>
              <p class="ultimo-mensaje">
                {{ item.ultimoMsg?.contenido || "Sin mensajes aún" }}
              </p>
            </div>
          </div>
        </div>
      </aside>

      <!-- ÁREA DE CHAT -->
      <main class="chat-area">
        <template v-if="conversacionActiva">
          <header class="chat-header">
            <div class="chat-header-info">
              <img
                :src="avatarUrl(conversaciones.find(c => c.conv.id === conversacionActiva.id)?.otro.nombre || '')"
                alt="Avatar"
                class="avatar-header"
              />
              <div>
                <h3>{{ conversaciones.find(c => c.conv.id === conversacionActiva.id)?.otro.nombre }}</h3>
              </div>
            </div>
            <div class="chat-header-actions">
              <button class="icon-btn" title="Agendar Mentoría">
                <i class="fa-regular fa-calendar-check"></i>
              </button>
            </div>
          </header>

          <div class="messages-container" ref="messagesEl">
            <p v-if="mensajes.length === 0" class="sin-mensajes">
              Sé el primero en escribir 👋
            </p>
            <div
              v-for="msg in mensajes"
              :key="msg.id"
              class="message-wrapper"
              :class="{ mine: msg.emisorId === usuarioActual.id, theirs: msg.emisorId !== usuarioActual.id }"
            >
              <div class="bubble">
                <p>{{ msg.contenido }}</p>
                <span class="msg-time">{{ formatHora(msg.fechaEnvio) }}</span>
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
          <p>Elige una conversación de la izquierda para empezar a hablar.</p>
        </div>
      </main>
    </div>
  </div>
</template>

<style scoped>
.chats-page {
  padding: 30px 5%;
  height: 85vh;
  display: flex;
  justify-content: center;
}

.sin-sesion {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  color: var(--text-muted);
}

.chat-layout {
  display: flex;
  width: 100%;
  max-width: 1200px;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
}

/* SIDEBAR */
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
.contactos-list::-webkit-scrollbar { width: 5px; }
.contactos-list::-webkit-scrollbar-thumb { background: var(--border); border-radius: 10px; }

.estado-sidebar {
  padding: 20px;
  color: var(--text-muted);
  font-size: 0.9rem;
  text-align: center;
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
.contacto-item:hover { background: rgba(215, 24, 32, 0.05); }
.contacto-item.activo {
  background: rgba(215, 24, 32, 0.1);
  border-left: 4px solid var(--primary);
}

.avatar-wrapper { position: relative; }
.avatar-img { width: 55px; height: 55px; border-radius: 50%; object-fit: cover; }

.contacto-info { flex: 1; overflow: hidden; }
.contacto-top {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  margin-bottom: 5px;
}
.contacto-top h4 { margin: 0; color: var(--text-main); font-size: 1.1rem; }
.hora { font-size: 0.8rem; color: var(--text-muted); }
.ultimo-mensaje {
  margin: 0;
  color: var(--text-muted);
  font-size: 0.9rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* CHAT AREA */
.chat-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: var(--bg-main);
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 30px;
  background: var(--bg-card);
  border-bottom: 1px solid var(--border);
}
.chat-header-info { display: flex; align-items: center; gap: 15px; }
.avatar-header { width: 50px; height: 50px; border-radius: 50%; }
.chat-header-info h3 { margin: 0; color: var(--text-main); }
.chat-header-actions { display: flex; gap: 15px; }
.icon-btn {
  background: transparent;
  border: none;
  color: var(--text-muted);
  font-size: 1.2rem;
  cursor: pointer;
  transition: color 0.2s;
}
.icon-btn:hover { color: var(--primary); }

.messages-container {
  flex: 1;
  padding: 30px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.messages-container::-webkit-scrollbar { width: 6px; }
.messages-container::-webkit-scrollbar-thumb { background: var(--border); border-radius: 10px; }

.sin-mensajes {
  text-align: center;
  color: var(--text-muted);
  margin: auto;
}

.message-wrapper { display: flex; width: 100%; }
.message-wrapper.mine { justify-content: flex-end; }
.message-wrapper.theirs { justify-content: flex-start; }

.bubble {
  max-width: 65%;
  padding: 15px 20px;
}
.bubble p { margin: 0; line-height: 1.5; font-size: 0.95rem; }
.msg-time { display: block; font-size: 0.75rem; margin-top: 8px; opacity: 0.7; text-align: right; }

.theirs .bubble {
  background: var(--bg-card);
  color: var(--text-main);
  border: 1px solid var(--border);
  border-radius: 20px 20px 20px 5px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
}
.mine .bubble {
  background: linear-gradient(135deg, var(--primary), #ff5e62);
  color: white;
  border-radius: 20px 20px 5px 20px;
  box-shadow: 0 4px 15px rgba(215, 24, 32, 0.3);
}

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
.chat-input-area input:focus { border-color: var(--primary); }

.btn-attach { background: transparent; border: none; color: var(--text-muted); font-size: 1.2rem; cursor: pointer; }
.btn-attach:hover { color: var(--text-main); }

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
.btn-send:hover { transform: scale(1.05); }

.no-chat-selected {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: var(--text-muted);
  text-align: center;
}
.icon-huge { font-size: 5rem; color: var(--border); margin-bottom: 20px; }
.no-chat-selected h2 { color: var(--text-main); margin-bottom: 10px; }
</style>
