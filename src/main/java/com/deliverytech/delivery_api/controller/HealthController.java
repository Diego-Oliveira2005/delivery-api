package com.deliverytech.delivery_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Monitoramento (Health)", description = "Endpoints para verificar a saúde da aplicação") // ATIVIDADE 2.2
public class HealthController {

    @GetMapping("/health")
    @Operation(summary = "Verificar status básico da aplicação") // ATIVIDADE 2.2
    public Map<String, String> health() {
        return Map.of(
                "status", "UP",
                "timestamp", LocalDateTime.now().toString(),
                "service", "Delivery API",
                "javaVersion", System.getProperty("java.version")
        );
    }

    @GetMapping("/info")
    @Operation(summary = "Verificar informações da build da aplicação") // ATIVIDADE 2.2
    public AppInfo info() {
        return new AppInfo(
                "Delivery Tech API",
                "1.0.0",
                "Diego Oliveira",
                "JDK 21",
                "Spring Boot 3.2.x" // (A versão no seu POM é 3.4.7)
        );
    }

    // Record para demonstrar recurso do Java 14+ (disponível no JDK 21)
    public record AppInfo(
            String application,
            String version,
            String developer,
            String javaVersion,
            String framework
    ) {}
}