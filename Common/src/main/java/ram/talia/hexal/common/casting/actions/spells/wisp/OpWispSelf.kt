package ram.talia.hexal.common.casting.actions.spells.wisp

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import ram.talia.hexal.api.spell.mishaps.MishapNoWisp
import ram.talia.hexal.api.util.WispCastingEnvironment

object OpWispSelf : ConstMediaAction {
    override val argc = 0

    @Suppress("CAST_NEVER_SUCCEEDS")
    override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
        val mCast = ctx as? WispCastingEnvironment ?: throw MishapNoWisp()

        return mCast.wisp!!.asActionResult
    }
}