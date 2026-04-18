package com.nebrimatch.backend.controller;

// anotaciones de restcontroller, requestmapping (url) y requiredallconstructor

@RestController
@RequestMapping('/api/comunidades')
@RequiredArgsConstructor
public class ComunidadController {
    
// llamamos a comunidadservice

private final ComunidadService comunidadService;

// GET ==>

      // GET /api/comunidades
    @GetMapping
    public ResponseEntity<List<ComunidadDTO>> obtenerTodas() {
        return ResponseEntity.ok(comunidadService.obtenerTodasLasComunidades());
    }

    // buscar por id==>

  @GetMapping("/{id}")
public ResponseEntity<ComunidadDTO> obtenerComunidadPorId(@PathVariable Long id){
    return ResponseEntity.ok(comunidadService.obtenerComunidadPorId(id));
}

// buscar por creador

@GetMapping("/creador/{creadorId}")
public ResponseEntity<List<ComunidadDTO>> obtenerPorCreador(@PathVariable Long creadorId){
    return ResponseEntity.ok(comunidadService.obtenerComunidadPorCreador(creadorId));
}

//POST ==>

//crear comunidad

@PostMapping
public ResponseEntity<ComunidadDTO> crearComunidad(
        @RequestParam String nombre,
        @RequestParam String descripcion,
        @RequestParam(required = false) String imagenComunidad,
        @RequestParam Long creadorId) {
    return ResponseEntity.status(201).body(
        comunidadService.crearComunidad(nombre, descripcion, imagenComunidad, creadorId)
    );
}

//DELETE ==> para borrar comunidad

@DeleteMapping("/{idComunidad}")
public ResponseEntity <Void> eliminarComunidad(
    @PathVariable Long idComunidad,
    @RequestParam Long idSolicitante
    // Filtros, búsquedas, opcionales
){

    comunidadService.eliminarComunidad(idComunidad, idSolicitante);
    return ResponseEntity.noContent().build();
}

// PUT ==> actualizar comunidad

@PutMapping("/{idComunidad}")
public ResponseEntity<ComunidadDTO> actualizarComunidad(
    @PathVariable Long idComunidad,
    @RequestParam String nombre,
    @RequestParam String descripcion,
    @RequestParam(required = false) String imagenComunidad,
    @RequestParam Long idSolicitante
){
    return ResponseEntity.ok(
        comunidadService.actualizarComunidad(idComunidad, nombre, descripcion, imagenComunidad, idSolicitante)
    );
}

// GET ==> buscar comunidades por texto

@GetMapping("/buscar")
public ResponseEntity<List<ComunidadDTO>> buscarComunidades(@RequestParam String texto){
    return ResponseEntity.ok(comunidadService.buscarComunidades(texto));
}

}
