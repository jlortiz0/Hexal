package ram.talia.hexal.common.casting.actions.spells.link

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPositiveIntUnder
import at.petrak.hexcasting.api.casting.iota.Iota
import ram.talia.hexal.api.linkable.ILinkable
import ram.talia.hexal.api.spell.mishaps.MishapNoLinked
import ram.talia.hexal.api.spell.mishaps.MishapNoWisp
import ram.talia.hexal.api.util.WispCastingEnvironment
import ram.talia.hexal.common.entities.BaseCastingWisp

class OpTransferAllowed(val setAllowed: Boolean) : SpellAction {
    override val argc = 1

    @Suppress("CAST_NEVER_SUCCEEDS")
    override fun execute(args: List<Iota>, ctx: CastingEnvironment): SpellAction.Result {
        val mCast = ctx as? WispCastingEnvironment
        val wispThis = mCast?.wisp ?: throw MishapNoWisp()

        val otherIndex = args.getPositiveIntUnder(0, OpSendIota.argc, wispThis.numLinked())
        val other = wispThis.getLinked(otherIndex) ?: throw MishapNoLinked(wispThis)

        return SpellAction.Result(
            Spell(wispThis, other, setAllowed),
            0,
            listOf()
        )
    }

    private data class Spell(val wispThis: BaseCastingWisp, val other: ILinkable, val setAllowed: Boolean) : RenderedSpell {
        override fun cast(ctx: CastingEnvironment) {
            if (setAllowed) {
                wispThis.removeFromBlackListTransferMedia(other)
                wispThis.addToWhiteListTransferMedia(other)
            } else {
                wispThis.addToBlackListTransferMedia(other)
                wispThis.removeFromWhiteListTransferMedia(other)
            }
        }
    }
}