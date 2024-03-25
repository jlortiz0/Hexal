package ram.talia.hexal.common.casting.actions.everbook

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPattern
import at.petrak.hexcasting.api.casting.iota.Iota
import ram.talia.hexal.xplat.IXplatAbstractions

object OpEverbookDelete : ConstMediaAction {
	override val argc = 1

	override val isGreat = true
	override val alwaysProcessGreatSpell = false
	override val causesBlindDiversion = false
	override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
		val key = args.getPattern(0, argc)

		IXplatAbstractions.INSTANCE.removeEverbookIota(ctx.caster, key)

		return listOf()
	}
}