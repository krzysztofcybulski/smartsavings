package me.kcybulski.smartsavings

import java.time.LocalDate

import static java.time.LocalDate.ofEpochDay

class TestData {

    static LocalDate randomDay() {
        return ofEpochDay(20000)
    }

}
