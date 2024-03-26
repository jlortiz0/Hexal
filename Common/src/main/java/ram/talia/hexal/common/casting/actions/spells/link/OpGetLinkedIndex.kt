package ram.talia.hexal.common.casting.actions.spells.link

import at.petrak.hexcasting.api.casting.*
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import ram.talia.hexal.api.linkable.LinkableRegistry

object OpGetLinkedIndex : ConstMediaAction {
	override val argc = 1

	override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
		val linkThis = LinkableRegistry.linkableFromCastingEnvironment(ctx)
		val linkOther = LinkableRegistry.linkableFromIota(args[0], ctx.world) ?: return (-1).asActionResult

		return linkThis.getLinkedIndex(linkOther).asActionResult
	}
}