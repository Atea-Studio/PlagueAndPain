package fr.ateastudio.plagueandpain

import fr.ateastudio.plagueandpain.util.TaggedCondition

enum class Injury(
    override val tag: String,
    val char: Char
) : TaggedCondition {
    BROKEN_LEG("broken_leg", '\uF100'),
    OPEN_WOUND("open_wound", '\uF101');
    
    val translationKey: String
        get() = "condition.plagueandpain.injury.$tag"
    
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