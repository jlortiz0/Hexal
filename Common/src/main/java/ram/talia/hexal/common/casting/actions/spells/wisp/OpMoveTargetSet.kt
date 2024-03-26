package ram.talia.hexal.common.casting.actions.spells.wisp

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import ram.talia.hexal.api.spell.mishaps.MishapNoWisp
import ram.talia.hexal.api.util.WispCastingEnvironment
import ram.talia.hexal.common.entities.TickingWisp

object OpMoveTargetSet : ConstMediaAction {
	override val argc = 1

	override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
		val target = args.getVec3(0, argc)

		@Suppress("CAST_NEVER_SUCCEEDS")
		val mCast = ctx as? WispCastingEnvironment

		if (mCast == null || mCast.wisp !is TickingWisp)
			throw MishapNoWisp()

		(mCast.wisp as TickingWisp).setTargetMovePos(target)

		return listOf()
	}
}