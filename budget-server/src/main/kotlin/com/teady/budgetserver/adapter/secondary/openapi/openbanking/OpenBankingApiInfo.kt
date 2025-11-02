package com.teady.budgetserver.adapter.secondary.openapi.openbanking

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)

annotation class OpenBankingApiInfo(
    val apiNm: String,
    val apiSvcCd: String
)