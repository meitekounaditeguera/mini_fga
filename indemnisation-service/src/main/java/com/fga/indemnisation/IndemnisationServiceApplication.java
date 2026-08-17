package com.fga.indemnisation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point d'entrée du microservice indemnisation-service.
 * Même architecture hexagonale que sinistre-service (presentation /
 * application / domain / infrastructure).
 */
@SpringBootApplication
public class IndemnisationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IndemnisationServiceApplication.class, args);
    }
}
