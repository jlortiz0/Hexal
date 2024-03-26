@file:Suppress("CAST_NEVER_SUCCEEDS")

package ram.talia.hexal.common.casting.actions.spells.motes

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.asActionResult
import ram.talia.hexal.api.asActionResult
import ram.talia.hexal.api.mediafieditems.MediafiedItemManager
import ram.talia.hexal.api.spell.casting.IMixinCastingEnvironment
import ram.talia.hexal.api.spell.mishaps.MishapNoBoundStorage

object OpGetContainedItemTypes : ConstMediaAction {
    override val argc = 0

    override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
        val storage = (ctx as IMixinCastingEnvironment).boundStorage ?: return null.asActionResult
        if (!MediafiedItemManager.isStorageLoaded(storage))
            throw MishapNoBoundStorage(ctx.caster?.position() ?: ctx.mishapSprayPos(), "storage_unloaded")

        return MediafiedItemManager.getAllContainedItemTypes(storage)?.toList()?.asActionResult ?: null.asActionResult
    }
}