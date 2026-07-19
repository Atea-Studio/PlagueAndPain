package fr.ateastudio.plagueandpain

import fr.ateastudio.plagueandpain.util.TaggedCondition

enum class Disease(
    override val tag: String,
    val char: Char
) : TaggedCondition {
    COUGH("cough", '\uF200'),
    FEVER("fever", '\uF201'),
    PLAGUE("plague", '\uF202'),
    PNEUMONIA("pneumonia", '\uF203'),
    RABIES("rabies", '\uF204');
    
    val translationKey: String
        get() = "condition.plagueandpain.disease.$tag"
    
    companion object {
        fun fromStoredValue(value: String?): Disease? {
            if (value == null) {
                return null
            }
            
            return entries.find { disease ->
                disease.tag.equals(value, true) || disease.name.equals(value, true)
            }
        }
    }
}