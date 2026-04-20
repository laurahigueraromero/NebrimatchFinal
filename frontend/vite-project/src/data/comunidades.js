// Definimos las tecnologías y sus iconos de DevIcon
const techs = [
  { name: 'Vue.js', slug: 'vuejs' },
  { name: 'React', slug: 'react' },
  { name: 'Angular', slug: 'angularjs' },
  { name: 'Python', slug: 'python' },
  { name: 'Java', slug: 'java' },
  { name: 'Node.js', slug: 'nodejs' },
  { name: 'Spring', slug: 'spring' },
  { name: 'Docker', slug: 'docker' },
  { name: 'C++', slug: 'cplusplus' },
  { name: 'JavaScript', slug: 'javascript' }
];

export const listaComunidades = [];

for (let i = 1; i <= 50; i++) {
  const tech = techs[i % techs.length];
  listaComunidades.push({
    id: i,
    nombre: `Comunidad ${tech.name} #${Math.ceil(i / techs.length)}`,
    miembros: Math.floor(Math.random() * 500) + 20,
    // Usamos el CDN de DevIcon para los logos reales
    imagen: `https://cdn.jsdelivr.net/gh/devicons/devicon/icons/${tech.slug}/${tech.slug}-original.svg`,
    descripcion: `Espacio para expertos y alumnos de Nebrija interesados en dominar ${tech.name}.`
  });
}