package ram.talia.hexal.common.casting.actions.everbook

import at.petrak.hexcasting.api.casting.*
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNoAkashicRecord
import at.petrak.hexcasting.api.casting.mishaps.MishapOthersName
import at.petrak.hexcasting.common.blocks.akashic.BlockAkashicRecord
import net.minecraft.world.phys.Vec3
import ram.talia.hexal.api.spell.mishaps.MishapIllegalInterworldIota
import ram.talia.hexal.xplat.IXplatAbstractions

object OpEverbookWrite : ConstMediaAction {
	override val argc = 2

	override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
		val pos = args.getBlockPos(0, argc)
		val key = args.getPattern(1, argc)

		ctx.assertVecInRange(Vec3.atCenterOf(pos))

		val record = ctx.world.getBlockState(pos).block
		if (record !is BlockAkashicRecord) {
			throw MishapNoAkashicRecord(pos)
		}

		val iota = record.lookupPattern(pos, key, ctx.world) ?: NullIota()

		val trueName = MishapOthersName.getTrueNameFromDatum(iota, ctx.caster)
		if (trueName != null)
			throw MishapOthersName(trueName)
		val illegalInterworldIota = MishapIllegalInterworldIota.getFromNestedIota(iota)
		if (illegalInterworldIota != null)
			throw MishapIllegalInterworldIota(illegalInterworldIota)

		IXplatAbstractions.INSTANCE.setEverbookIota(ctx.caster, key, iota)

		return listOf()
	}
}