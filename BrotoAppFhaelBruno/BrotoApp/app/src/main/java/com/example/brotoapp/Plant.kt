package com.example.brotoapp


data class Plant(
    val id: Int,
    val name: String,
    val species: String,
    val wateringFrequencyDays: Int,
    val daysSinceWatered: Int
) {

    fun diasParaRegar() = wateringFrequencyDays - daysSinceWatered

    fun precisaRegar() = diasParaRegar() <= 0

    fun textoRega(): String {
        val dias = diasParaRegar()
        return when {
            dias < 0 -> "Atrasada há ${-dias} dia(s)"
            dias == 0 -> "Regar hoje"
            else -> "Próxima rega em $dias dia(s)"
        }
    }
}
