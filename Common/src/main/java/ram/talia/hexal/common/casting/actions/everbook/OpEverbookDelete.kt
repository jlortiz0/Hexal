package ram.talia.hexal.common.casting.actions.everbook

import at.petrak.hexcasting.api.casting.ConstMediaAction
import at.petrak.hexcasting.api.casting.casting.CastingContext
import at.petrak.hexcasting.api.casting.getPattern
import at.petrak.hexcasting.api.casting.iota.Iota
import ram.talia.hexal.xplat.IXplatAbstractions

object OpEverbookDelete : ConstMediaAction {
	override val argc = 1

	override val isGreat = true
	override val alwaysProcessGreatSpell = false
	override val causesBlindDiversion = false
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val key = args.getPattern(0, argc)

		IXplatAbstractions.INSTANCE.removeEverbookIota(ctx.caster, key)

		return listOf()
	}
}