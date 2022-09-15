package ram.talia.hexal.api.spell.casting.triggers

import at.petrak.hexcasting.api.spell.SpellDatum
import at.petrak.hexcasting.api.spell.casting.CastingContext
import net.minecraft.nbt.Tag
import net.minecraft.server.level.ServerLevel
import ram.talia.hexal.common.entities.BaseCastingWisp

interface IWispTrigger {

	/**
	 * Called each time the wisp attempts to cast a spell. If it returns true, the cast is allowed, if it returns false, the cast fails. Also called each tick to determine
	 * how much the wisp should cost (returning false will reduce the cost of the wisp per tick).
	 */
	fun shouldTrigger(wisp: BaseCastingWisp): Boolean

	/**
	 * Called before [shouldTrigger]; If it returns true, this trigger is removed from the wisp (meaning the cast that caused the trigger will succeed).
	 */
	fun shouldRemoveTrigger(wisp: BaseCastingWisp): Boolean

	/**
	 * Return the registered WispTriggerType for this Trigger, used to save/load the trigger.
	 */
	fun getTriggerType(): WispTriggerRegistry.WispTriggerType<*>

	fun writeToNbt(): Tag
}