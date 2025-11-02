package com.teady.budgetserver.application.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class OpenBankingCardDtoWithClient(
    val no: String?,
    val noList: List<String>?,
    val name: String?,
    val company: String?,
    val syncAt: String?,
) {
}

data class OpenBankingCardDtoLikeHeaderWithExternalServer(
    @JsonProperty("ApiNm") val apiNm: String,
    @JsonProperty("ApiSvcCd") val apiSvcCd: String,
    @JsonProperty("Tsymd") val tsymd: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")),
    @JsonProperty("Trtm") val trtm: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss")),
    @JsonProperty("Iscd") val iscd: String = "003298",
    @JsonProperty("FintechApsno") val fintechApsno: String = "001",
    @JsonProperty("IsTuno") val isTuno: String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")).take(20).padStart(20, '0'),
    @JsonProperty("AccessToken") val accessToken: String = "680449ba708fe232ed26b9a171116af0249ee55352006bb5170f666b069a69a1",
    @JsonProperty("Rsms") val rsms: String? = null,
    @JsonProperty("Rpcd") val rpcd: String? = null,
)

data class OpenBankingCardDtoWithExternalServer(
    @JsonProperty("Header") val header: OpenBankingCardDtoLikeHeaderWithExternalServer, // body 내 header
    @JsonProperty("Brdt") val brdt: String? = null, // 생년월일(yyyyMMdd)
    @JsonProperty("Cano") val cano: String? = null, // 카드번호
) {
}