package ram.talia.hexal.common.casting.actions.spells.link

import at.petrak.hexcasting.api.casting.*
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import ram.talia.hexal.api.config.HexalConfig
import ram.talia.hexal.api.linkable.ILinkable
import ram.talia.hexal.api.linkable.LinkableRegistry
import ram.talia.hexal.api.spell.mishaps.MishapNoLinked

object OpUnlink : SpellAction {
	override val argc = 1

	override fun execute(args: List<Iota>, ctx: CastingEnvironment): SpellAction.Result {
		val linkThis = LinkableRegistry.linkableFromCastingEnvironment(ctx)

		val otherIndex = args.getPositiveIntUnder(0, OpSendIota.argc, linkThis.numLinked())
		val other = linkThis.getLinked(otherIndex) ?: throw MishapNoLinked(linkThis)

		return SpellAction.Result(
			Spell(linkThis, other),
			HexalConfig.server.unlinkCost,
			listOf(ParticleSpray.burst(other.getPosition(), 1.5))
		)
	}

	private data class Spell(val linkThis: ILinkable, val other: ILinkable) : RenderedSpell {
		override fun cast(ctx: CastingEnvironment) = linkThis.unlink(other)
	}
}