package com.ejemplo.crud.repository;

import com.ejemplo.crud.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
