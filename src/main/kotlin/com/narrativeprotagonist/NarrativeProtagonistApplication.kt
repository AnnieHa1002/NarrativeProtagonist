package com.narrativeprotagonist

import io.github.cdimascio.dotenv.dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaAuditing
@EnableJpaRepositories
@SpringBootApplication
class NarrativeProtagonistApplication

fun main(args: Array<String>) {
    // Load .env file before Spring Boot starts
    val dotenv = dotenv {
        ignoreIfMissing = true
        systemProperties = true
    }

    // Set environment variables as system properties for Spring
    dotenv.entries().forEach { entry ->
        System.setProperty(entry.key, entry.value)
    }

    runApplication<NarrativeProtagonistApplication>(*args)
}
