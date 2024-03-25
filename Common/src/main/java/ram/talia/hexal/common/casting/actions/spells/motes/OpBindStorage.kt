package ram.talia.hexal.common.casting.actions.spells.motes

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.phys.Vec3
import ram.talia.hexal.api.config.HexalConfig
import ram.talia.hexal.api.mediafieditems.MediafiedItemManager
import ram.talia.hexal.api.spell.casting.IMixinCastingEnvironment
import ram.talia.hexal.common.blocks.BlockMediafiedStorage
import ram.talia.hexal.common.blocks.entity.BlockEntityMediafiedStorage
import ram.talia.hexal.xplat.IXplatAbstractions

class OpBindStorage(val isTemporaryBinding: Boolean) : SpellAction {
    override val argc = 1

    override fun execute(args: List<Iota>, ctx: CastingEnvironment): Triple<RenderedSpell, Int, List<ParticleSpray>> {
        val pos = args.getBlockPos(0, argc)

        ctx.assertVecInRange(pos)

        val storage = ctx.world.getBlockState(pos).block

        return Triple(
            Spell(if (storage is BlockMediafiedStorage) pos else null, isTemporaryBinding),
            if (isTemporaryBinding) HexalConfig.server.bindTemporaryStorageCost else HexalConfig.server.bindStorageCost,
            listOf(ParticleSpray.burst(Vec3.atCenterOf(pos), 1.5))
        )
    }

    @Suppress("CAST_NEVER_SUCCEEDS")
    private data class Spell(val pos: BlockPos?, val isTemporaryBinding: Boolean) : RenderedSpell {
        override fun cast(ctx: CastingEnvironment) {
            if (pos == null) {
                MediafiedItemManager.setBoundStorage(ctx.caster, null)
                return
            }

            if (!ctx.canEditBlockAt(pos) || !IXplatAbstractions.INSTANCE.isInteractingAllowed(ctx.world, pos, Direction.UP, ctx.castingHand, ctx.caster))
                return

            val storage = ctx.world.getBlockEntity(pos) as? BlockEntityMediafiedStorage ?: return

            if (isTemporaryBinding)
                (ctx as IMixinCastingEnvironment).setTemporaryBoundStorage(storage.uuid)
            else
                MediafiedItemManager.setBoundStorage(ctx.caster, storage.uuid)
        }
    }
}