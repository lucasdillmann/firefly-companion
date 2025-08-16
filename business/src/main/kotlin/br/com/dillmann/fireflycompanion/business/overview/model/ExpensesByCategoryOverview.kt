package br.com.dillmann.fireflycompanion.business.overview.model

import java.io.Serializable
import java.math.BigDecimal

data class ExpensesByCategoryOverview(
    val name: String,
    val amount: BigDecimal,
) : Serializable
