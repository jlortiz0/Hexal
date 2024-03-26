package ram.talia.hexal.common.casting.actions.spells.link

import at.petrak.hexcasting.api.casting.*
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.SpellCircleContext
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import ram.talia.hexal.api.spell.casting.IMixinCastingEnvironment
import ram.talia.hexal.api.spell.mishaps.MishapNoLinked
import ram.talia.hexal.api.spell.mishaps.MishapNonPlayer
import ram.talia.hexal.xplat.IXplatAbstractions

object OpOpenTransmit : ConstMediaAction {
	override val argc = 1

	@Suppress("CAST_NEVER_SUCCEEDS")
	override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
		val mCtx = ctx as? IMixinCastingEnvironment

		if ((ctx is CircleCastEnv) || mCtx?.hasWisp() == true)
			throw MishapNonPlayer()

		val playerLinkable = IXplatAbstractions.INSTANCE.getLinkstore(ctx.caster)

		if (playerLinkable.numLinked() == 0)
			throw MishapNoLinked(playerLinkable)

		val index = args.getPositiveIntUnder(0, argc, playerLinkable.numLinked())

		IXplatAbstractions.INSTANCE.setPlayerTransmittingTo(ctx.caster, index)

		return listOf()
	}
}