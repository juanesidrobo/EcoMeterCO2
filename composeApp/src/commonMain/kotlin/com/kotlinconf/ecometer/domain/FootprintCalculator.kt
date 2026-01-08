package com.kotlinconf.ecometer.domain

class FootprintCalculator {

    fun calculate(
        amount: Double,
        factor: EmissionFactor
    ): Double {
        return amount * factor.co2ePerUnit
    }
}
