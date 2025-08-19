package br.com.dillmann.fireflycompanion.android.home.extensions

import br.com.dillmann.fireflycompanion.business.preferences.Preferences.HomePeriod
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

fun HomePeriod.toDateRange(): Pair<LocalDate, LocalDate> =
    when (this) {
        HomePeriod.WEEK_SO_FAR -> weekSoFar()
        HomePeriod.LAST_WEEK -> lastWeek()
        HomePeriod.CURRENT_WEEK -> currentWeek()
        HomePeriod.MONTH_SO_FAR -> monthSoFar()
        HomePeriod.LAST_MONTH -> lastMonth()
        HomePeriod.CURRENT_MONTH -> currentMonth()
        HomePeriod.YEAR_SO_FAR -> yearSoFar()
        HomePeriod.LAST_YEAR -> lastYear()
        HomePeriod.CURRENT_YEAR -> currentYear()
        HomePeriod.ALL_TIME -> allTime()
    }

private fun weekSoFar(): Pair<LocalDate, LocalDate> {
    val now = LocalDate.now()
    val start = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    return start to now
}

private fun lastWeek(): Pair<LocalDate, LocalDate> {
    val now = LocalDate.now()
    val start = now.minusWeeks(1)
    return start to now
}

private fun currentWeek(): Pair<LocalDate, LocalDate> {
    val now = LocalDate.now()
    val start = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
    val end = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))
    return start to end
}

private fun monthSoFar(): Pair<LocalDate, LocalDate> {
    val now = LocalDate.now()
    val start = now.with(TemporalAdjusters.firstDayOfMonth())
    return start to now
}

private fun lastMonth(): Pair<LocalDate, LocalDate> {
    val lastMonth = LocalDate.now().minusMonths(1)
    val start = lastMonth.with(TemporalAdjusters.firstDayOfMonth())
    val end = lastMonth.with(TemporalAdjusters.lastDayOfMonth())
    return start to end
}

private fun currentMonth(): Pair<LocalDate, LocalDate> {
    val now = LocalDate.now()
    val start = now.with(TemporalAdjusters.firstDayOfMonth())
    val end = now.with(TemporalAdjusters.lastDayOfMonth())
    return start to end
}

private fun yearSoFar(): Pair<LocalDate, LocalDate> {
    val now = LocalDate.now()
    val start = now.with(TemporalAdjusters.firstDayOfYear())
    return start to now
}

private fun currentYear(): Pair<LocalDate, LocalDate> {
    val now = LocalDate.now()
    val start = now.with(TemporalAdjusters.firstDayOfYear())
    val end = now.with(TemporalAdjusters.lastDayOfYear())
    return start to end
}

private fun lastYear(): Pair<LocalDate, LocalDate> {
    val lastYear = LocalDate.now().minusYears(1)
    val start = lastYear.with(TemporalAdjusters.firstDayOfYear())
    val end = lastYear.with(TemporalAdjusters.lastDayOfYear())
    return start to end
}

private fun allTime(): Pair<LocalDate, LocalDate> =
    LocalDate.of(1900, 1, 1) to LocalDate.now()
