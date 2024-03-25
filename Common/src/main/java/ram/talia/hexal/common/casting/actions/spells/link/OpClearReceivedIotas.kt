package ram.talia.hexal.common.casting.actions.spells.link

import at.petrak.hexcasting.api.casting.ConstMediaAction
import at.petrak.hexcasting.api.casting.casting.CastingContext
import at.petrak.hexcasting.api.casting.iota.Iota
import ram.talia.hexal.api.linkable.LinkableRegistry

object OpClearReceivedIotas : ConstMediaAction {
    override val argc = 0

    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        LinkableRegistry.linkableFromCastingContext(ctx).clearReceivedIotas()
        return emptyList()
    }
}