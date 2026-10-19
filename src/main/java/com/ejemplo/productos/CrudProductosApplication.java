package com.ejemplo.productos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

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
                    Map<String, Object> respuesta = restTemplate.getForObject(url, Map.class);
                    
                    if (respuesta != null && respuesta.containsKey("results")) {
                        List<Map<String, Object>> personajes = (List<Map<String, Object>>) respuesta.get("results");
                        
                        // Tomamos los primeros 6 personajes para convertirlos en "productos"
                        for (int i = 0; i < Math.min(6, personajes.size()); i++) {
                            Map<String, Object> p = personajes.get(i);
                            String nombre = (String) p.get("name");
                            String status = (String) p.get("status");
                            String especie = (String) p.get("species");
                            String imagen = (String) p.get("image");
                            
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