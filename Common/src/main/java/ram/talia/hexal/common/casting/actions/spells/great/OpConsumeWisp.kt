package ram.talia.hexal.common.casting.actions.spells.great

import at.petrak.hexcasting.api.misc.ManaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import ram.talia.hexal.api.HexalAPI
import ram.talia.hexal.api.spell.casting.MixinCastingContextInterface
import ram.talia.hexal.common.entities.BaseWisp
import ram.talia.hexal.common.entities.IMediaEntity
import kotlin.math.ln
import kotlin.math.min

object OpConsumeWisp : SpellOperator {
	const val COST_FOR_OWN = ManaConstants.SHARD_UNIT
	const val COST_FOR_OTHERS_PER_MEDIA = 1.5

	override val argc = 1

	override val isGreat = true

	override fun execute(args: List<SpellDatum<*>>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>>? {
		val consumed = args.getChecked<IMediaEntity<*>>(0, argc)

		HexalAPI.LOGGER.info("consuming $consumed")

		ctx.assertEntityInRange(consumed.get())

		HexalAPI.LOGGER.info("$consumed in range")

		val cost = when (consumed.fightConsume(ctx.caster)) {
			true  -> COST_FOR_OWN
			false -> (COST_FOR_OTHERS_PER_MEDIA * consumed.media).toInt()
		}

		HexalAPI.LOGGER.info("cost to consume $consumed is $cost")

		return Triple(
			Spell(consumed),
			cost,
			listOf(ParticleSpray.burst(consumed.get().position(), 1.0, (ln(10.0) * 14 * ln(consumed.media/10.0 + 1)).toInt()))
		)
	}

	private data class Spell(val consumed: IMediaEntity<*>) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			HexalAPI.LOGGER.info("cast method of Spell of OpConsumeWisp triggered targeting $consumed")

			@Suppress("CAST_NEVER_SUCCEEDS")
			val mCast = ctx as? MixinCastingContextInterface

			if (mCast != null && mCast.wisp != null)
				mCast.wisp.media += 19 * consumed.media / 20

			HexalAPI.LOGGER.info("discarding $consumed")
			consumed.get().discard()
		}
	}
}