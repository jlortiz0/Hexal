package ram.talia.hexal.common.casting.actions.spells.motes

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVillager
import at.petrak.hexcasting.api.casting.iota.Iota

object OpGetVillagerLevel : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
        return args.getVillager(0, argc).villagerData.level.asActionResult
    }
}