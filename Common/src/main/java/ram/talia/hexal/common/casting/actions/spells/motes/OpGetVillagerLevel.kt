package ram.talia.hexal.common.casting.actions.spells.motes

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import net.minecraft.world.entity.npc.Villager

object OpGetVillagerLevel : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
        val vill = args.getEntity(0, argc) as? Villager ?: throw MishapInvalidIota.ofType(args[0], args.size - 1, "entity.villager")
        return vill.villagerData.level.asActionResult
    }
}