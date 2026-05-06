package com.nebrimatch.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.nebrimatch.backend.model.Comunidad;
import com.nebrimatch.backend.model.Lenguaje;
import com.nebrimatch.backend.model.Usuario;
import com.nebrimatch.backend.repository.ComunidadRepository;
import com.nebrimatch.backend.repository.LenguajeRepository;
import com.nebrimatch.backend.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ComunidadRepository comunidadRepository;
    private final UsuarioRepository usuarioRepository;
    private final LenguajeRepository lenguajeRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private static final String[] LENGUAJES = {
        "Vue.js", "React", "Angular", "Java", "Python",
        "Node.js", "Spring Boot", "C++", "Go", "Docker"
    };

    private static final String[][] TECHS = {
        {"Vue.js",     "vuejs"},
        {"React",      "react"},
        {"Angular",    "angularjs"},
        {"Python",     "python"},
        {"Java",       "java"},
        {"Node.js",    "nodejs"},
        {"Spring",     "spring"},
        {"Docker",     "docker"},
        {"C++",        "cplusplus"},
        {"JavaScript", "javascript"}
    };

    @Override
    public void run(String... args) {
        sembrarLenguajes();
        sembrarComunidades();
    }

    private void sembrarLenguajes() {
        for (String nombre : LENGUAJES) {
            if (!lenguajeRepository.existsByNombre(nombre)) {
                Lenguaje l = new Lenguaje();
                l.setNombre(nombre);
                lenguajeRepository.save(l);
            }
        }
    }

    private void sembrarComunidades() {
        if (comunidadRepository.count() > 0) return;

        Usuario sistema = usuarioRepository.findByEmail("sistema@nebrimatch.com")
            .orElseGet(() -> {
                Usuario u = new Usuario();
                u.setNombreUsuario("Sistema");
                u.setEmail("sistema@nebrimatch.com");
                u.setPassword(passwordEncoder.encode("SistemaPass123!"));
                u.setDescripcion("Usuario del sistema");
                u.setLenguajesAEnsenar("");
                u.setLenguajesAAprender("");
                u.setNumeroMatches(0);
                return usuarioRepository.save(u);
            });

        for (int i = 1; i <= 50; i++) {
            String[] tech = TECHS[i % TECHS.length];
            int grupo = (int) Math.ceil((double) i / TECHS.length);

            Comunidad c = new Comunidad();
            c.setNombreComunidad("Comunidad " + tech[0] + " #" + grupo);
            c.setDescripcionComunidad("Espacio para expertos y alumnos de Nebrija interesados en dominar " + tech[0] + ".");
            c.setImagenComunidad("https://cdn.jsdelivr.net/gh/devicons/devicon/icons/" + tech[1] + "/" + tech[1] + "-original.svg");
            c.setCreadorComunidad(sistema);
            comunidadRepository.save(c);
        }
    }
}
