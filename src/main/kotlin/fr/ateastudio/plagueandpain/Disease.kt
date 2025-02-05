package fr.ateastudio.plagueandpain

enum class Disease(val char: Char, val progressByTick: Double, val tag: String) {
    COUGH('\uF200', 0.1,"cough"),
    FEVER('\uF201', 0.1,"fever"),
    PLAGUE('\uF202', 0.1,"plague"),
    PNEUMONIA('\uF203', 0.1,"pneumonia"),
    RABIES('\uF204', 0.1,"rabies");
    
    companion object {
        fun fromTag(tag: String?): Disease? {
            if (tag == null) return null
            return Disease.entries.find { it.tag.equals(tag, true) }
        }
    }
}