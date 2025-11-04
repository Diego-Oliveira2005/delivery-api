package com.deliverytech.delivery_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery_api.entity.Produto;

import java.util.List;


@Repository
public interface ProdutoRepository extends JpaRepository <Produto, Long> {
    // buscar produto por restaurante ID
     List<Produto> findByRestauranteId(Long restauranteId);
    
}
