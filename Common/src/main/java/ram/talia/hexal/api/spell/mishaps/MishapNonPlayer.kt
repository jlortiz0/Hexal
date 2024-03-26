package ram.talia.hexal.api.spell.mishaps

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.pigment.FrozenPigment
import net.minecraft.network.chat.Component
import net.minecraft.world.item.DyeColor

class MishapNonPlayer : Mishap() {
	override fun accentColor(ctx: CastingEnvironment, errorCtx: Context): FrozenPigment = dyeColor(DyeColor.LIGHT_BLUE)

	override fun errorMessage(ctx: CastingEnvironment, errorCtx: Context): Component = error("non_player", actionName(errorCtx.name))

	override fun execute(ctx: CastingEnvironment, errorCtx: Context, stack: MutableList<Iota>) {
		ctx.mishapEnvironment.blind(20 * 60)
	}
}