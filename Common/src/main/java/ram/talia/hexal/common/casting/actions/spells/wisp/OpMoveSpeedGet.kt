package ram.talia.hexal.common.casting.actions.spells.wisp

import at.petrak.hexcasting.api.casting.ConstMediaAction
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.casting.CastingContext
import at.petrak.hexcasting.api.casting.iota.Iota
import ram.talia.hexal.api.spell.casting.IMixinCastingContext
import ram.talia.hexal.api.spell.mishaps.MishapNoWisp
import ram.talia.hexal.common.entities.TickingWisp

object OpMoveSpeedGet : ConstMediaAction {
    override val argc = 0

    @Suppress("CAST_NEVER_SUCCEEDS")
    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        val mCast = ctx as? IMixinCastingContext

        if (mCast == null || !mCast.hasWisp() || mCast.wisp !is TickingWisp)
            throw MishapNoWisp()

        return ((mCast.wisp as TickingWisp).currentMoveMultiplier * TickingWisp.BASE_MAX_SPEED_PER_TICK).asActionResult
    }
}