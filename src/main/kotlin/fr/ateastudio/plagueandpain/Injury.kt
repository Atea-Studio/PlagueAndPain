package fr.ateastudio.plagueandpain

import fr.ateastudio.plagueandpain.util.TaggedCondition

enum class Injury(
    override val tag: String,
    val char: Char,
    val displayName: String
) : TaggedCondition {
    BROKEN_LEG("broken_leg", '\uF100', "Broken Leg"),
    OPEN_WOUND("open_wound", '\uF101', "Open Wound");
    
    companion object {
        fun fromStoredValue(value: String?): Injury? {
            if (value == null) {
                return null
            }
            
            return entries.find { injury ->
                injury.tag.equals(value, true) || injury.name.equals(value, true)
            }
        }
    }
}