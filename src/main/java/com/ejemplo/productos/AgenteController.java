package com.ejemplo.productos;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class AgenteController {

    private final ChatClient chatClient;
    private final ProductoTools productoTools;

    public AgenteController(ChatClient.Builder chatClientBuilder, ProductoTools productoTools) {
        this.chatClient = chatClientBuilder.build();
        this.productoTools = productoTools;
    }

    @GetMapping
    public ResponseEntity<String> chatearConAgente(@RequestParam String mensaje) {
        try {
            String respuesta = chatClient.prompt()
                    .user(mensaje)
                    .tools(productoTools)
                    .call()
                    .content();
            return ResponseEntity.ok(respuesta);
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("No se pudo conectar con Ollama en http://localhost:11434. "
                            + "Inicia Ollama y verifica que el modelo configurado esté disponible.");
        }
    }
}
