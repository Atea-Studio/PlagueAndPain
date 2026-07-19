package fr.ateastudio.plagueandpain.util

enum class ConditionSeverity {
    MILD,
    MODERATE,
    SEVERE,
    CRITICAL;
    
    companion object {
        fun fromProgress(progress: Double): ConditionSeverity {
            return when {
                progress >= 75.0 -> CRITICAL
                progress >= 50.0 -> SEVERE
                progress >= 25.0 -> MODERATE
                else -> MILD
            }
        }
    }
}
