package ram.talia.hexal.common.casting.actions

import at.petrak.hexcasting.api.casting.ConstMediaAction
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.casting.CastingContext
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import net.minecraft.world.level.LightLayer

object OpGetLightLevel : ConstMediaAction {
	override val argc = 1

	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val pos = args.getBlockPos(0, argc)

		return ctx.world.getBrightness(LightLayer.BLOCK, pos).asActionResult
	}
}