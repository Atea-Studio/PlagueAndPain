package fr.ateastudio.plagueandpain

import fr.ateastudio.plagueandpain.util.TaggedCondition

enum class Disease(
    override val tag: String,
    val char: Char,
    val displayName: String
) : TaggedCondition {
    COUGH("cough", '\uF200', "Cough"),
    FEVER("fever", '\uF201', "Fever"),
    PLAGUE("plague", '\uF202', "Plague"),
    PNEUMONIA("pneumonia", '\uF203', "Pneumonia"),
    RABIES("rabies", '\uF204', "Rabies");
    
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