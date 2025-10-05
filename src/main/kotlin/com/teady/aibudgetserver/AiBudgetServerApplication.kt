package com.teady.aibudgetserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication(scanBasePackages = ["com.teady.aibudgetserver"])
@EnableJpaAuditing
class AiBudgetServerApplication

fun main(args: Array<String>) {
    runApplication<AiBudgetServerApplication>(*args)
}
