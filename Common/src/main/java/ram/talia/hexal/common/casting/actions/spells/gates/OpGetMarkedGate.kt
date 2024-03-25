package ram.talia.hexal.common.casting.actions.spells.gates

import at.petrak.hexcasting.api.casting.ConstMediaAction
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.casting.CastingContext
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import ram.talia.hexal.api.getGate

object OpGetMarkedGate : ConstMediaAction {
    override val argc = 2

    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        val gate = args.getGate(0, argc)
        val entity = args.getEntity(1, argc)
        return gate.isMarked(entity).asActionResult
    }
}