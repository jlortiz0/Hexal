package ram.talia.hexal.common.casting.actions.spells.link

import at.petrak.hexcasting.api.casting.*
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import ram.talia.hexal.api.linkable.LinkableRegistry

object OpGetLinked : ConstMediaAction {
	override val argc = 1

	override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
		val linkThis = LinkableRegistry.linkableFromCastingEnvironment(ctx)

		val linkedIndex = args.getPositiveInt(0, OpSendIota.argc)

		if (linkedIndex >= linkThis.numLinked())
			return null.asActionResult

		val other = linkThis.getLinked(linkedIndex) ?: return null.asActionResult

		return if (ctx.isVecInRange(other.getPosition())) other.asActionResult else null.asActionResult
	}
}