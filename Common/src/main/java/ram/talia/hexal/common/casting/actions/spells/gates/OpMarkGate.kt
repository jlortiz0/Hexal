package ram.talia.hexal.common.casting.actions.spells.gates

import at.petrak.hexcasting.api.mod.HexTags
import at.petrak.hexcasting.api.casting.*
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapImmuneEntity
import net.minecraft.world.entity.Entity
import ram.talia.hexal.api.config.HexalConfig
import ram.talia.hexal.api.getGate
import ram.talia.hexal.api.spell.iota.GateIota

object OpMarkGate : SpellAction {
    override val argc = 2

    override fun execute(args: List<Iota>, ctx: CastingEnvironment): Triple<RenderedSpell, Int, List<ParticleSpray>> {
        val gate = args.getGate(0, argc)
        val entity = args.getEntity(1, argc)
        ctx.assertEntityInRange(entity)

        if (!entity.canChangeDimensions() || entity.type.`is`(HexTags.Entities.CANNOT_TELEPORT))
            throw MishapImmuneEntity(entity)


        return Triple(
            Spell(gate, entity),
            HexalConfig.server.markGateCost,
            listOf(ParticleSpray.cloud(entity.position(), 1.0))
        )
    }

    private class Spell(val gate: GateIota, val entity: Entity) : RenderedSpell {
        override fun cast(ctx: CastingEnvironment) {
            gate.mark(entity)
        }
    }
}