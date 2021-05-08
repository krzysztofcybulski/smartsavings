package me.kcybulski.smartsavings.domain

import me.kcybulski.smartsavings.support.TimeSupport
import spock.lang.Specification

import java.time.LocalDate

class FrequenciesSpec extends Specification implements TimeSupport {

    def 'should return days for every day investment'() {
        given:
            LocalDate since = today.minusDays(3)
        when:
            List<LocalDate> days = EveryDay.INSTANCE.buyingDaysBetween(since, today)
        then:
            days == [since, since.plusDays(1), since.plusDays(2), today]
    }

    def 'should return days for every week investment'() {
        given:
            LocalDate since = today.minusWeeks(2)
        when:
            List<LocalDate> days = EveryWeek.INSTANCE.buyingDaysBetween(since, today)
        then:
            days == [since, since.plusWeeks(1), today]
    }

    def 'should return days for every month investment'() {
        given:
            LocalDate since = today.minusMonths(2)
        when:
            List<LocalDate> days = EveryMonth.INSTANCE.buyingDaysBetween(since, today)
        then:
            days == [since, since.plusMonths(1), today]
    }
}
