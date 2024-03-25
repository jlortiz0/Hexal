@file:Suppress("CAST_NEVER_SUCCEEDS")

package ram.talia.hexal.common.casting.actions.spells.motes

import at.petrak.hexcasting.api.casting.ConstMediaAction
import at.petrak.hexcasting.api.casting.casting.CastingContext
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.asActionResult
import ram.talia.hexal.api.asActionResult
import ram.talia.hexal.api.getMoteOrItemType
import ram.talia.hexal.api.mediafieditems.MediafiedItemManager
import ram.talia.hexal.api.spell.casting.IMixinCastingContext
import ram.talia.hexal.api.spell.mishaps.MishapNoBoundStorage

object OpGetContainedMotes : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        val item = args.getMoteOrItemType(0, argc) ?: return null.asActionResult

        val storage = (ctx as IMixinCastingContext).boundStorage ?: return null.asActionResult
        if (!MediafiedItemManager.isStorageLoaded(storage))
            throw MishapNoBoundStorage(ctx.caster.position(), "storage_unloaded")

        val results = item.map({itemIota ->
            itemIota.record?.let { MediafiedItemManager.getItemRecordsMatching(storage, it) }
        }, {
            MediafiedItemManager.getItemRecordsMatching(storage, it)
        }) ?: return null.asActionResult

        return results.asActionResult
    }
}