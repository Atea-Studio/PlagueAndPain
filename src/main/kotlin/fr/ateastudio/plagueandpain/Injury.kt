package fr.ateastudio.plagueandpain

enum class Injury(val char: Char, val progressByTick: Double, val tag: String) {
    BROKEN_LEG('\uF100', 0.1,"broken_leg"),
    OPEN_WOUND('\uF101',0.1,"open_wound");
    
    companion object {
        fun fromTag(tag: String?): Injury? {
            if (tag == null) return null
            return Injury.entries.find { it.tag.equals(tag, true) }
        }
    }
}