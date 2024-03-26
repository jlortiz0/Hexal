package ram.talia.hexal.forge

import at.petrak.hexcasting.api.HexAPI
import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.hex.HexActions
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.resources.ResourceLocation
import ram.talia.hexal.common.casting.Patterns

object OtherPatterns {
	@JvmField
	val REVEAL = patternOf(HexActions.PRINT)

	@JvmField
	val COMPASS = patternOf(HexAPI.modLoc("entity_pos/foot"))

	@JvmField
	val NOOP = PatternIota(HexPattern.fromAngles("", HexDir.SOUTH_EAST))
	@JvmField
	val DROP = PatternIota(HexPattern.fromAngles("a", HexDir.SOUTH_EAST))
	@JvmField
	val SWAP = patternOf(HexAPI.modLoc("swap"))

	@JvmField
	val EQUALITY = patternOf(HexAPI.modLoc("equals"))
	@JvmField
	val INEQUALITY = patternOf(HexAPI.modLoc("not_equals"))
	@JvmField
	val AUGERS = patternOf(HexActions.IF)

	@JvmField
	val NULLARY = patternOf(HexAPI.modLoc("const/null"))

	@JvmField
	val ZERO = PatternIota(HexPattern.fromAngles("aqaa", HexDir.EAST))
	@JvmField
	val ONE = PatternIota(HexPattern.fromAngles("aqaaw", HexDir.EAST))
	@JvmField
	val FOUR = PatternIota(HexPattern.fromAngles("aqaawaa", HexDir.EAST))

	@JvmField
	val GEMINIS_DISINTEGRATION = patternOf(HexAPI.modLoc("duplicate"))
	@JvmField
	val FLOCKS_DISINTEGRATION = patternOf(HexActions.SPLAT)

	@JvmField
	val SELECTION_DISTILLATION = patternOf(HexActions.INDEX)

	@JvmField
	val HERMES = patternOf(HexActions.EVAL)

	@JvmField
	val INTRO = PatternIota(HexPattern.fromAngles("qqq", HexDir.WEST))
	@JvmField
	val RETRO = PatternIota(HexPattern.fromAngles("eee", HexDir.EAST))



	@JvmField
	val TIMEKEEPER = Patterns.CURRENT_TICK

	@JvmField
	val ZONE_DSTL_WISP = Patterns.ZONE_ENTITY_WISP
	@JvmField
	val WISP_TRIGGER_COMM = Patterns.WISP_TRIGGER_COMM

	@JvmField
	val LINK = Patterns.LINK
	@JvmField
	val POPULARITY = Patterns.LINK_NUM

	@JvmField
	val SEND_IOTA = Patterns.LINK_COMM_SEND
	@JvmField
	val RECITATION = Patterns.LINK_COMM_READ

	private val registry = IXplatAbstractions.INSTANCE.actionRegistry
	private fun patternOf(loc: ResourceLocation): PatternIota = PatternIota(registry.get(loc)!!.prototype)
	private fun patternOf(are: ActionRegistryEntry): PatternIota = PatternIota(are.prototype)
}