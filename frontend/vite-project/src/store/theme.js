import { ref } from 'vue';

// Por defecto, la app empieza en modo oscuro (true)
export const isDarkMode = ref(true);

// Esta función cambia el estado y le pone/quita una clase al <body> de la web
export const toggleTheme = () => {
  isDarkMode.value = !isDarkMode.value;
  
  if (isDarkMode.value) {
    document.body.classList.remove('light-theme');
  } else {
    document.body.classList.add('light-theme');
  }
};