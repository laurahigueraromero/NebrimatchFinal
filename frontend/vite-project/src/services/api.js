import axios from 'axios'

// cliente axios centralizado
const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE || 'http://localhost:8080',
    headers: { 'Content-Type': 'application/json' },
    withCredentials: true,
})

// ── AUTH ────────────────────────────────────────────────── +
export const authService = {
    login: (email, password) =>
        api.post('/api/auth/login', { email, password }),
}

// ── USUARIOS ──────────────────────────────────────────────
export const usuarioService = {
    obtenerTodos: () =>
        api.get('/api/usuarios'),

    obtenerPorId: (id) =>
        api.get(`/api/usuarios/${id}`),

    obtenerPorEmail: (email) =>
        api.get(`/api/usuarios/email/${email}`),

    crear: (usuarioDTO) =>
        api.post('/api/usuarios', usuarioDTO),

    actualizar: (id, usuarioDTO) =>
        api.put(`/api/usuarios/${id}`, usuarioDTO),

    eliminar: (id) =>
        api.delete(`/api/usuarios/${id}`),

    buscarParaMatching: ({ ensenan, aprenden } = {}) =>
        api.get('/api/usuarios/matching', { params: { ensenan, aprenden } }),
}

// ── MATCHES ───────────────────────────────────────────────
export const matchService = {
    crearMatch: (usuario1Id, usuario2Id) =>
        api.post('/api/matches', null, { params: { usuario1Id, usuario2Id } }),

    obtenerPorId: (id) =>
        api.get(`/api/matches/${id}`),

    obtenerPorUsuario: (usuarioId) =>
        api.get(`/api/matches/usuario/${usuarioId}`),

    eliminar: (id) =>
        api.delete(`/api/matches/${id}`),
}

// ── CONVERSACIONES ────────────────────────────────────────
export const conversacionService = {
    crearConversacion: (usuario1Id, usuario2Id) =>
        api.post('/api/conversaciones', null, { params: { usuario1Id, usuario2Id } }),

    obtenerPorUsuario: (usuarioId) =>
        api.get(`/api/conversaciones/usuario/${usuarioId}`),

    obtenerPorId: (id) =>
        api.get(`/api/conversaciones/${id}`),

    obtenerMensajes: (id) =>
        api.get(`/api/conversaciones/${id}/mensajes`),

    obtenerUltimoMensaje: (id) =>
        api.get(`/api/conversaciones/${id}/mensajes/ultimo`),
}

// ── COMUNIDADES ───────────────────────────────────────────
export const comunidadService = {
    obtenerTodas: () =>
        api.get('/api/comunidades'),

    obtenerPorId: (id) =>
        api.get(`/api/comunidades/${id}`),

    obtenerPorCreador: (creadorId) =>
        api.get(`/api/comunidades/creador/${creadorId}`),

    buscar: (texto) =>
        api.get('/api/comunidades/buscar', { params: { texto } }),

    crear: (nombre, descripcion, creadorId, imagenComunidad = null) =>
        api.post('/api/comunidades', null, { params: { nombre, descripcion, creadorId, imagenComunidad } }),

    actualizar: (idComunidad, nombre, descripcion, idSolicitante, imagenComunidad = null) =>
        api.put(`/api/comunidades/${idComunidad}`, null, { params: { nombre, descripcion, idSolicitante, imagenComunidad } }),

    eliminar: (idComunidad, idSolicitante) =>
        api.delete(`/api/comunidades/${idComunidad}`, { params: { idSolicitante } }),
}

// ── MEMBRESÍA COMUNIDADES ─────────────────────────────────
export const membresiaService = {
    unirse: (comunidadId, usuarioId) =>
        api.post(`/api/comunidades/${comunidadId}/unirse`, null, { params: { usuarioId } }),

    salir: (comunidadId, usuarioId) =>
        api.delete(`/api/comunidades/${comunidadId}/salir`, { params: { usuarioId } }),

    esMiembro: (comunidadId, usuarioId) =>
        api.get(`/api/comunidades/${comunidadId}/esMiembro`, { params: { usuarioId } }),

    contarMiembros: (comunidadId) =>
        api.get(`/api/comunidades/${comunidadId}/miembros/count`),
}

export default api
