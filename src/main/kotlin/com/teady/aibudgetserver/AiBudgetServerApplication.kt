package com.teady.aibudgetserver

import jakarta.annotation.PreDestroy
import org.flywaydb.core.Flyway
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.stereotype.Component
import javax.sql.DataSource

@SpringBootApplication(scanBasePackages = ["com.teady.aibudgetserver"])
@EnableJpaAuditing
class AiBudgetServerApplication

fun main(args: Array<String>) {
    runApplication<AiBudgetServerApplication>(*args)
}

@Component
@Profile("dev")
class FlywayCleanupOnShutdown(
    private val dataSource: DataSource
) {
    @PreDestroy
    fun cleanupDatabase() {

        val flyway = Flyway.configure()
            .dataSource(dataSource)
            .cleanDisabled(false)
            .load()

        flyway.clean()
    }
}