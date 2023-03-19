package ram.talia.hexal.common.casting.actions

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getPositiveDouble
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.EntityIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.common.casting.operators.selectors.OpGetEntitiesBy
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import ram.talia.hexal.api.getEntityType

class OpGetEntitiesByDyn(val negate: Boolean) : ConstMediaAction {
    override val argc = 3
    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        val type = args.getEntityType(0, argc)
        val pos = args.getVec3(1, argc)
        val radius = args.getPositiveDouble(2, argc)
        ctx.assertVecInRange(pos)

        val aabb = AABB(pos.add(Vec3(-radius, -radius, -radius)), pos.add(Vec3(radius, radius, radius)))
        val entitiesGot = ctx.world.getEntities(null, aabb) {
            OpGetEntitiesBy.isReasonablySelectable(ctx, it)
                    && it.distanceToSqr(pos) <= radius * radius
                    && ((it.type == type) != negate) //
        }.sortedBy { it.distanceToSqr(pos) }
        return entitiesGot.map(::EntityIota).asActionResult
    }
}