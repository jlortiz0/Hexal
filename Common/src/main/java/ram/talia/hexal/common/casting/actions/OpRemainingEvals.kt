package ram.talia.hexal.common.casting.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import ram.talia.hexal.api.spell.casting.IMixinCastingEnvironment

object OpRemainingEvals : ConstMediaAction {
	override val argc = 0

	override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
		@Suppress("KotlinConstantConditions", "CAST_NEVER_SUCCEEDS")
		return (ctx as? IMixinCastingEnvironment)?.remainingDepth()?.asActionResult ?: (-1).asActionResult
	}
}