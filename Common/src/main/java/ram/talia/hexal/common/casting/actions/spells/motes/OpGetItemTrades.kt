package ram.talia.hexal.common.casting.actions.spells.motes

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import net.minecraft.world.entity.npc.Villager
import ram.talia.hexal.api.spell.iota.ItemTypeIota

object OpGetItemTrades : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
        val villager = args.getEntity(0, argc) as? Villager ?: throw MishapInvalidIota.ofType(args[0], args.size - 1, "entity.villager")

        villager.updateSpecialPrices(ctx.caster)
        villager.tradingPlayer = ctx.caster

        val result = villager.offers.map { offer ->
            // map each MerchantOffer to [[[desiredItem0, count], [desiredItem1, count]], [returnedItem, count]]
            val costList = mutableListOf(ListIota(listOf(ItemTypeIota(offer.costA.item), DoubleIota(offer.costA.count.toDouble()))))
            if (!offer.costB.isEmpty)
                costList.add(ListIota(listOf(ItemTypeIota(offer.costB.item), DoubleIota(offer.costB.count.toDouble()))))

            val offerList = listOf(
                    ListIota(costList as List<Iota>),
                    ListIota(listOf(ItemTypeIota(offer.result.item), DoubleIota(offer.result.count.toDouble())))
            )
            ListIota(offerList)
        }

        villager.stopTrading()

        return result.asActionResult
    }
}