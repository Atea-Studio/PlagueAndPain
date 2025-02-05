package fr.ateastudio.plagueandpain.util

import org.bukkit.inventory.ItemStack
import xyz.xenondevs.nova.util.item.novaItem
import xyz.xenondevs.nova.world.item.NovaItem

fun ItemStack.getItemId(): String {
    if (this.novaItem is NovaItem) {
        return this.novaItem!!.id.toString()
    }
    return this.type.key.toString()
}