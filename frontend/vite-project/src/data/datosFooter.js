// 1. Objeto para construir el Footer automáticamente
export const columnasFooter = [
  {
    titulo: "Legal",
    enlaces: [
      { nombre: "Privacidad", slug: "privacidad" },
      { nombre: "Condiciones de Uso", slug: "condiciones" },
      { nombre: "Política de Cookies", slug: "cookies" },
    ],
  },
  {
    titulo: "NebriMatch",
    enlaces: [
      { nombre: "Sobre el proyecto", slug: "sobre-proyecto" },
      { nombre: "Portal de empleo", slug: "empleo" },
      { nombre: "Contacto", slug: "contacto" },
    ],
  },
];

// 2. Objeto con el contenido de cada página
export const contenidoPaginas = {
  privacidad: {
    titulo: "Política de Privacidad",
    fecha: "Mayo 2026",
    bloques: [
      {
        subtitulo: "1. Datos recopilados",
        texto: "Solo guardamos tu correo institucional y tu lenguaje favorito.",
      },
      {
        subtitulo: "2. Uso de datos",
        texto: "Se usan exclusivamente para el algoritmo de NebriMatch.",
      },
    ],
  },
  condiciones: {
    titulo: "Condiciones de Uso",
    fecha: "Mayo 2026",
    bloques: [
      {
        subtitulo: "1. Aceptación",
        texto:
          "Al usar esta app, te comprometes a ser respetuoso con la comunidad.",
      },
      {
        subtitulo: "2. Suspensión",
        texto: "Cualquier mal uso conllevará la expulsión de la plataforma.",
      },
    ],
  },
  cookies: {
    titulo: "Política de Cookies",
    fecha: "Mayo 2026",
    bloques: [
      {
        subtitulo: "1. ¿Qué son?",
        texto:
          "Usamos cookies técnicas para que no tengas que loguearte cada vez.",
      },
    ],
  },
  "sobre-proyecto": {
    titulo: "Sobre NebriMatch",
    fecha: "Curso 2025/2026",
    bloques: [
      {
        subtitulo: "El Origen",
        texto:
          "NebriMatch nace como Trabajo de Fin de Grado para conectar el talento de programación en la Universidad Nebrija.",
      },
    ],
  },
  empleo: {
    titulo: "Portal de Empleo",
    fecha: "Próximamente",
    bloques: [
      {
        subtitulo: "En construcción",
        texto: "Pronto publicaremos ofertas junior aquí.",
      },
    ],
  },
  contacto: {
    titulo: "Contacto",
    fecha: "Soporte",
    bloques: [
      { subtitulo: "¿Dudas?", texto: "Escríbenos a soporte@nebrimatch.es" },
    ],
  },
};
