package ram.talia.hexal.common.casting.actions

import at.petrak.hexcasting.api.casting.ConstMediaAction
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.casting.CastingContext
import at.petrak.hexcasting.api.casting.iota.Iota
import ram.talia.hexal.api.spell.casting.IMixinCastingContext

object OpRemainingEvals : ConstMediaAction {
	override val argc = 0

	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		@Suppress("KotlinConstantConditions", "CAST_NEVER_SUCCEEDS")
		return (ctx as? IMixinCastingContext)?.remainingDepth()?.asActionResult ?: (-1).asActionResult
	}
}