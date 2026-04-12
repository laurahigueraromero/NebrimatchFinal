import axios from 'axios'

// cliente axios centralizado
const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE || 'http://localhost:8080',
    headers: { 'Content-Type': 'application/json' },
    withCredentials: true,
})

/*
registrar(UsuarioRequest)       // crear cuenta
login(LoginRequest)             // autenticar
getById(Long id)                // ver perfil
actualizar(Long id, ...)        // editar perfil
buscarPorIdioma(String idioma)  // buscar usuarios por idioma
eliminar(Long id)

crearConversacion(Long usuario1Id, Long usuario2Id)  // iniciar chat
obtenerConversacionesPorUsuario(Long usuarioId)      // lista de chats
getById(Long id)

enviarMensaje(MensajeRequest)                        // guarda + WebSocket push
obtenerMensajesPorConversacion(Long conversacionId)  // historial de chat

crearMatch(Long usuario1Id, Long usuario2Id)
obtenerMatchesPorUsuario(Long usuarioId)
aceptarMatch(Long matchId)
rechazarMatch(Long matchId)



*/ 



export default api