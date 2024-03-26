package ram.talia.hexal.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ram.talia.hexal.api.spell.casting.IMixinCastingEnvironment;
import ram.talia.hexal.common.entities.BaseCastingWisp;

import static java.lang.Math.max;

@Mixin(OperatorSideEffect.ConsumeMedia.class)
public abstract class MixinOperatorSideEffectConsumeMana {
	
	/**
	 * Makes it so that the cant_overcast message (i.e. message played when caster doesn't have enough media) doesn't play for cyclic wisps (and others that override
	 * {@link BaseCastingWisp#getShouldComplainNotEnoughMedia()}
	 *
	 * @return
	 */
	@Redirect(method = "performEffect",
					at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/api/casting/eval/CastingEnvironment;extractMedia(J)J"),
					remap = false
	)
	private long performEffectWisp (CastingEnvironment env, long value) {
		IMixinCastingEnvironment iCtx = (IMixinCastingEnvironment) env;

		var consumedMedia = iCtx.getConsumedMedia();
		if (consumedMedia == 0)
			return env.extractMedia(value);

		var newValue = max(value - consumedMedia, 0);
		consumedMedia -= (int) (value - newValue);
		iCtx.setConsumedMedia(consumedMedia);

		return env.extractMedia(value);
	}
}
