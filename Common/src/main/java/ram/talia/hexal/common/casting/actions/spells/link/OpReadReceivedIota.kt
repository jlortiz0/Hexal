package ram.talia.hexal.common.casting.actions.spells.link

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import ram.talia.hexal.api.linkable.LinkableRegistry

object OpReadReceivedIota : ConstMediaAction {
	override val argc = 0

	override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
		return listOf(LinkableRegistry.linkableFromCastingEnvironment(ctx).nextReceivedIota())
	}
}