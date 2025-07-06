package br.com.dillmann.fireflycompanion.business.core.datetime

import java.time.LocalDate

fun LocalDate.atStartOfMonth(): LocalDate =
    withDayOfMonth(1)

fun LocalDate.atEndOfMonth(): LocalDate = 
    withDayOfMonth(month.length(isLeapYear))
