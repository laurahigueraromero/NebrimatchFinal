[ Vue 3 (frontend) ]
        ↕  HTTP / WebSocket
[ Controller ]   ← recibe la petición y devuelve la respuesta
        ↕
[  Service  ]    ← contiene toda la lógica de negocio
        ↕
[ Repository ]   ← habla con la base de datos
        ↕
[  MySQL BD  ]   ← almacena los datos



Controller — Es el portero. Recibe la petición HTTP que llega desde Vue (GET, POST, PUT, DELETE), la redirige a quien corresponde y devuelve la respuesta en JSON. No toma ninguna decisión ni toca la base de datos, solo enruta.

Service — Es el cerebro. Aquí vive toda la lógica del proyecto: comprobar si un usuario ya existe antes de crearlo, evitar conversaciones duplicadas al hacer match, decidir qué datos se devuelven y cuáles no. Es la capa más importante y la que más código propio tendrá.

Repository — Es el traductor. Su único trabajo es hablar con la base de datos. Le pides un usuario por email y él genera y ejecuta la consulta SQL. En la mayoría de casos no escribes nada, Spring lo genera solo a partir del nombre del método.

Model / Entity — Es el plano. Representa exactamente cómo es una tabla en MySQL — cada clase es una tabla, cada campo es una columna. No hace nada por sí solo, es solo la definición de la estructura.

DTO — Es el mensajero. Es el objeto que viaja hacia el exterior, hacia Vue. Existe para controlar exactamente qué datos salen — por ejemplo, mandas el nombre y el email del usuario pero nunca la contraseña. Hay un DTO por cada cosa que entra o sale de la API.



Equivalencias Node.js → Spring Boot

server.js   ==> NebrimatchApplication.java
config/database.js ==> application.properties
routes/ ==> @RestController
Lógica en controllers ==> @Service
Queries SQL manuales  ==> JpaRepository / @Query
socket.io   ==> WebSocket + STOMP
res.json(datos) ==> return datosDTO
res.status(404)  ==> throw new ResponseStatusException(HttpStatus.NOT_FOUND)






Caso: Vue pide el perfil de un usuario ==>


1. Vue hace:
   GET http://localhost:8080/api/usuarios/5

2. UsuarioController recibe la petición:
   @GetMapping("/{id}") → llama a usuarioService.findById(5)

3. UsuarioService procesa:
   → llama a usuarioRepository.findById(5)
   → si no existe, lanza error 404
   → si existe, convierte Usuario → UsuarioDTO

4. UsuarioRepository consulta MySQL:
   SELECT * FROM usuario WHERE id = 5

5. La respuesta sube por las capas:
   MySQL → Repository → Service → Controller → Vue

6. Vue recibe JSON:
   { "id": 5, "nombreUsuario": "laura", "email": "laura@nebrija.es", ... }


   flujo crear un match ==>

   1. Vue hace:
   POST http://localhost:8080/api/conversaciones
   body: { usuario1Id: 3, usuario2Id: 7 }

2. ConversacionController recibe el body como DTO

3. ConversacionService:
   → comprueba si ya existe una conversación entre esos dos usuarios
   → si existe, devuelve la que ya hay (evita duplicados)
   → si no existe, crea una nueva

4. ConversacionRepository:
   → SELECT para comprobar si existe
   → INSERT si hay que crearla

5. Vue recibe la conversación creada y redirige al chat





