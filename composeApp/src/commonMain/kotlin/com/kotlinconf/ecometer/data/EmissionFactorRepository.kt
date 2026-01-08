package com.kotlinconf.ecometer.data

import com.kotlinconf.ecometer.domain.Category
import com.kotlinconf.ecometer.domain.EmissionFactor
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
//import ecometer.composeapp.generated.resources.Res

class EmissionFactorRepository {

    // Función suspendida para mantener la arquitectura asíncrona (buena práctica)
    suspend fun getFactors(): List<EmissionFactor> {
        // Simulamos un pequeño delay como si leyera disco (opcional)
        // delay(100)

        // Retornamos los datos directos. ¡Adiós error de Contexto!
        return HARDCODED_FACTORS
    }

    suspend fun getFactorById(id: String): EmissionFactor? {
        return HARDCODED_FACTORS.find { it.id == id }
    }
}
