package ram.talia.hexal.common.casting.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import net.minecraft.world.item.Items
import ram.talia.hexal.api.asActionResult

object OpTypeItemHeld : ConstMediaAction {
    override val argc = 0

    override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
        return ctx.getHeldItemToOperateOn { it.item != Items.AIR }?.takeUnless { it.stack?.item == Items.AIR }?.stack?.item?.asActionResult
            ?: null.asActionResult
    }
}