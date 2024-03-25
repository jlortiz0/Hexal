package ram.talia.hexal.common.casting.actions

import at.petrak.hexcasting.api.casting.ConstMediaAction
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.casting.CastingContext
import at.petrak.hexcasting.api.casting.iota.Iota

object OpCurrentTick : ConstMediaAction {
	override val argc = 0

	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		return (ctx.world.gameTime).asActionResult
	}
}