<script setup>
import { isDarkMode, toggleTheme } from "../store/theme.js";
</script>

<template>
  <div class="theme-switch" @click="toggleTheme" title="Cambiar tema">
    <div class="switch-track" :class="{ 'is-light': !isDarkMode }">
      <div class="switch-thumb"></div>
    </div>
    <span class="icon">{{ isDarkMode ? "🌙" : "☀️" }}</span>
  </div>
</template>

<style scoped>
.theme-switch {
  display: flex;
  align-items: center;
  cursor: pointer;
  gap: 8px;
}

.switch-track {
  width: 46px;
  height: 24px;
  background-color: #333;
  border-radius: 30px;
  position: relative;
  transition: background-color 0.3s;
}

.switch-track.is-light {
  background-color: #ddd;
}

.switch-thumb {
  width: 18px;
  height: 18px;
  background-color: white;
  border-radius: 50%;
  position: absolute;
  top: 3px;
  left: 3px;
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.switch-track.is-light .switch-thumb {
  transform: translateX(22px);
}

.icon {
  font-size: 1.2rem;
}
</style>
