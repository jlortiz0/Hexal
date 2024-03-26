package ram.talia.hexal.api.spell.mishaps

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.pigment.FrozenPigment
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.EnchantmentHelper

class MishapNoWisp : Mishap() {
	override fun accentColor(ctx: CastingEnvironment, errorCtx: Context): FrozenPigment = dyeColor(DyeColor.LIGHT_BLUE)

	override fun errorMessage(ctx: CastingEnvironment, errorCtx: Context): Component = error("no_wisp", actionName(errorCtx.name))

	private inline fun dropAll(player: Player, stacks: MutableList<ItemStack>, filter: (ItemStack) -> Boolean = { true }) {
		for (index in stacks.indices) {
			val item = stacks[index]
			if (!item.isEmpty && filter(item)) {
				player.drop(item, true, false)
				stacks[index] = ItemStack.EMPTY
			}
		}
	}

	override fun execute(ctx: CastingEnvironment, errorCtx: Context, stack: MutableList<Iota>) {
		// FIXME: should yeet all items not just staff
		// mishap environment doesn't provide a good way to do this
		ctx.mishapEnvironment.dropHeldItems()
	}
}