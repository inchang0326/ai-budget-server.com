package com.teady.budgetserver

import jakarta.annotation.PreDestroy
import org.flywaydb.core.Flyway
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.stereotype.Component
import javax.sql.DataSource

@SpringBootApplication(scanBasePackages = ["com.teady.budgetserver"])
@EnableJpaAuditing
@EnableFeignClients
class BudgetServerApplication

fun main(args: Array<String>) {
    runApplication<com.teady.budgetserver.BudgetServerApplication>(*args)
}

@Component
@Profile("local")
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