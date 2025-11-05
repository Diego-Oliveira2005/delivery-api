package com.deliverytech.delivery_api;

import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public void run(String... args) throws Exception {

        System.out.println(">>> [DataLoader] Populando banco de dados H2 com dados de teste...");

        // 1. Inserir Clientes
        Cliente c1 = new Cliente(null, "Ana Julia", "ana.j@email.com", "(11) 98888-1111", "Rua das Laranjeiras, 10", LocalDateTime.now(), true);
        Cliente c2 = new Cliente(null, "Bruno Costa", "bruno.costa@email.com", "(21) 97777-2222", "Av. Copacabana, 20", LocalDateTime.now(), true);
        Cliente c3 = new Cliente(null, "Carla Dias", "carla.d@email.com", "(31) 96666-3333", "Praça da Liberdade, 30", LocalDateTime.now(), true);

        clienteRepository.saveAll(List.of(c1, c2, c3));
        System.out.println(">>> [DataLoader] 3 Clientes salvos.");

        // 2. Inserir Restaurantes
        Restaurante r1 = new Restaurante(null, "Sabor da India", "Indiana", "Rua dos Timbiras, 100", "(11) 4444-5555", new BigDecimal("7.00"), new BigDecimal("4.8"), true);
        Restaurante r2 = new Restaurante(null, "O Rei do Pastel", "Lanches", "Av. Afonso Pena, 200", "(11) 5555-6666", new BigDecimal("3.00"), new BigDecimal("4.5"), true);

        restauranteRepository.saveAll(List.of(r1, r2));
        System.out.println(">>> [DataLoader] 2 Restaurantes salvos.");

        // 3. Inserir Produtos
        Produto p1 = new Produto(null, "Frango Tikka Masala", "Frango ao molho cremoso de especiarias", new BigDecimal("45.50"), "Prato Principal", true, r1.getId());
        Produto p2 = new Produto(null, "Samosa (2 unidades)", "Pastel indiano recheado com batata e ervilha", new BigDecimal("15.00"), "Entrada", true, r1.getId());
        Produto p3 = new Produto(null, "Pastel de Carne", "Pastel frito na hora com carne moída", new BigDecimal("8.00"), "Pastel", true, r2.getId());
        Produto p4 = new Produto(null, "Pastel de Queijo", "Pastel frito na hora com queijo mussarela", new BigDecimal("8.00"), "Pastel", true, r2.getId());
        Produto p5 = new Produto(null, "Caldo de Cana 500ml", "Caldo de cana puro", new BigDecimal("10.00"), "Bebida", true, r2.getId());

        produtoRepository.saveAll(List.of(p1, p2, p3, p4, p5));
        System.out.println(">>> [DataLoader] 5 Produtos salvos.");

        // 4. Inserir Pedidos
        Pedido ped1 = new Pedido(null, "PED-001", LocalDateTime.now().minusHours(1), StatusPedido.PENDENTE.name(), new BigDecimal("60.50"), "Sem pimenta no frango", c1.getId(), r1, "1x Frango Tikka Masala, 1x Samosa (2 unidades)");
        Pedido ped2 = new Pedido(null, "PED-002", LocalDateTime.now(), StatusPedido.CONFIRMADO.name(), new BigDecimal("26.00"), "Enviar maionese", c2.getId(), r2, "1x Pastel de Carne, 1x Pastel de Queijo, 1x Caldo de Cana 500ml");

        pedidoRepository.saveAll(List.of(ped1, ped2));
        System.out.println(">>> [DataLoader] 2 Pedidos salvos.");

        System.out.println(">>> [DataLoader] Carga de dados concluída.");
    }
}