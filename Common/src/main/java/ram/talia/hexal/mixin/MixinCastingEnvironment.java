package ram.talia.hexal.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import ram.talia.hexal.api.mediafieditems.MediafiedItemManager;
import ram.talia.hexal.api.spell.casting.IMixinCastingEnvironment;
import ram.talia.hexal.common.entities.BaseCastingWisp;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.lang.Math.max;

/**
 * Modifies {@link at.petrak.hexcasting.api.casting.eval.CastingEnvironment} to make it properly allow wisps to affect things within their range.
 */
@Mixin(CastingEnvironment.class)
public abstract class MixinCastingEnvironment implements IMixinCastingEnvironment {
	private final CastingEnvironment self = (CastingEnvironment) (Object) this;

	private BaseCastingWisp wisp;

	private int consumedMedia;
	private final Map<BlockPos, Integer> numTimesTicked = new HashMap<>(); // stores how many times each blockpos has been ticked by OpTick (tick acceleration). Used to compute cost.

	private @Nullable UUID temporaryStorage; // UUID of the temporary Mediafied Item Storage that is being used by this ctx, if it exists.

	@Override
	public @Nullable UUID getBoundStorage() {
		if (temporaryStorage != null)
			return temporaryStorage;
		return MediafiedItemManager.getBoundStorage(self.getCaster());
	}

	@Override
	public void setTemporaryBoundStorage(@Nullable UUID temporaryStorage) {
		this.temporaryStorage = temporaryStorage;
	}

	@Override
	public int getConsumedMedia() {
		return consumedMedia;
	}

	@Override
	public void setConsumedMedia(int media) {
		consumedMedia = max(media, 0);
	}

	@Override
	public int getTimesTicked(BlockPos pos) {
		return numTimesTicked.getOrDefault(pos, 0);
	}

	@Override
	public void incTimesTicked(BlockPos pos) {
		numTimesTicked.merge(pos, 1, Integer::sum);
	}

	/**
	 * Modifies {@link at.petrak.hexcasting.api.casting.eval.CastingEnvironment} to make it properly allow wisps to affect things within their range. The INVOKE location and
	 * use of cancellable mean the wisp can affect things in range of the player's greater sentinel, but can't affect things in range of the player.
	 */
	@Inject(method = "isVecInRange", at = @At(value = "RETURN", ordinal = 0), cancellable = true,
			locals = LocalCapture.CAPTURE_FAILEXCEPTION, remap = false)
	private void isVecInRangeWisp (Vec3 vec, CallbackInfoReturnable<Boolean> cir) {
		if (this.wisp != null) {
			cir.setReturnValue(vec.distanceToSqr(this.wisp.position()) <= this.wisp.maxSqrCastingDistance());
		}
	}
}
