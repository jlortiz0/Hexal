package ram.talia.hexal.common.casting.actions.spells.wisp

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import ram.talia.hexal.api.spell.mishaps.MishapNoWisp
import ram.talia.hexal.api.util.WispCastingEnvironment

object OpWispMedia : ConstMediaAction {
	override val argc = 0

	@Suppress("CAST_NEVER_SUCCEEDS")
	override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
		val mCast = ctx as? WispCastingEnvironment

		if (mCast == null)
			throw MishapNoWisp()

		return (mCast.wisp!!.media.toFloat() / MediaConstants.DUST_UNIT).asActionResult
	}
}