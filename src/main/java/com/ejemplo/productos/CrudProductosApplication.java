package com.ejemplo.productos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;

@SpringBootApplication
public class CrudProductosApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrudProductosApplication.class, args);
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    CommandLineRunner importarDeApi(ProductoRepository repo, RestTemplate restTemplate) {
        return args -> {
            if (repo.count() == 0) {
                try {
                    // Consumimos la API pública de Rick and Morty
                    String url = "https://rickandmortyapi.com/api/character?page=1";
                    JsonNode respuesta = restTemplate.getForObject(url, JsonNode.class);
                    
                    if (respuesta != null && respuesta.path("results").isArray()) {
                        JsonNode personajes = respuesta.path("results");
                        
                        // Tomamos los primeros 6 personajes para convertirlos en "productos"
                        for (int i = 0; i < Math.min(6, personajes.size()); i++) {
                            JsonNode p = personajes.get(i);
                            String nombre = p.path("name").asText();
                            String status = p.path("status").asText();
                            String especie = p.path("species").asText();
                            String imagen = p.path("image").asText();
                            
                            String descripcion = "Estado: " + status + " | Especie: " + especie;
                            double precio = 15.00 + (i * 10); // Precio ficticio
                            int stock = 10 + (i * 3);       // Stock ficticio
                            
                            repo.save(new Producto(nombre, descripcion, precio, stock, imagen));
                        }
                    }
                } catch (Exception e) {
                    System.out.println("No se pudo conectar a la API externa (revisa tu internet): " + e.getMessage());
                }
            }
        };
    }
}
