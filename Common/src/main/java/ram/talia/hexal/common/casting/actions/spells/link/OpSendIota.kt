package ram.talia.hexal.common.casting.actions.spells.link

import at.petrak.hexcasting.api.casting.*
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import ram.talia.hexal.api.HexalAPI
import ram.talia.hexal.api.config.HexalConfig
import ram.talia.hexal.api.linkable.ILinkable
import ram.talia.hexal.api.linkable.LinkableRegistry
import ram.talia.hexal.api.spell.mishaps.MishapNoLinked

object OpSendIota : SpellAction {
	override val argc = 2

	override fun execute(args: List<Iota>, ctx: CastingEnvironment): SpellAction.Result {
		val linkThis = LinkableRegistry.linkableFromCastingEnvironment(ctx)

		val linkedIndex = args.getPositiveIntUnder(0, linkThis.numLinked(), argc)
		val iota = args[1]

		val other = linkThis.getLinked(linkedIndex) ?: throw MishapNoLinked(linkThis)

		return SpellAction.Result(
			Spell(other, iota),
			HexalConfig.server.sendIotaCost,
			listOf()
		)
	}

	private data class Spell(val other: ILinkable, val iota: Iota) : RenderedSpell {
		override fun cast(ctx: CastingEnvironment) {
			HexalAPI.LOGGER.debug("sending {} to {}", iota, other)
			other.receiveIota(LinkableRegistry.linkableFromCastingEnvironment(ctx), iota)
		}
	}
}