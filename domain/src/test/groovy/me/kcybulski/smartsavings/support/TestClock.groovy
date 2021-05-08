package me.kcybulski.smartsavings.support

import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

import static java.time.Instant.now
import static java.time.ZoneId.systemDefault

class TestClock extends Clock {

    private ZoneId zoneId = systemDefault()
    private Instant time = now()

    void setTime(Instant time) {
        this.time = time
    }

    void setTime(LocalDate date) {
        this.time = date.atStartOfDay(getZone()).toInstant()
    }

    @Override
    ZoneId getZone() {
        return zoneId
    }

    @Override
    Clock withZone(ZoneId zone) {
        this.zoneId = zone
        return this
    }

    @Override
    Instant instant() {
        return time
    }

}
