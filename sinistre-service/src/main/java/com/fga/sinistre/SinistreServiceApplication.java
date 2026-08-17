package com.fga.sinistre;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point d'entrée du microservice sinistre-service.
 *
 * Ce microservice suit l'architecture hexagonale (4 couches) :
 *  - presentation   : les controllers REST et les DTOs (le "port d'entrée")
 *  - application    : les use cases / orchestrateurs (ce qu'on veut faire)
 *  - domain         : les règles métier pures, sans dépendance technique
 *  - infrastructure : les adaptateurs techniques (JPA, PostgreSQL...)
 */
@SpringBootApplication
public class SinistreServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SinistreServiceApplication.class, args);
    }
}
