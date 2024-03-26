package ram.talia.hexal.mixin;

import at.petrak.hexcasting.api.casting.SpellList;
import at.petrak.hexcasting.api.casting.eval.*;
import at.petrak.hexcasting.api.casting.eval.env.StaffCastEnv;
import at.petrak.hexcasting.api.casting.eval.vm.*;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.PatternIota;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import kotlin.Pair;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ram.talia.hexal.api.util.WispCastingEnvironment;
import ram.talia.hexal.common.casting.Patterns;
import ram.talia.hexal.xplat.IXplatAbstractions;

import java.util.List;

@SuppressWarnings("ConstantConditions")
@Mixin(value = CastingVM.class, remap = false)
public abstract class MixinCastingHarness {
	private final CastingVM harness = (CastingVM) (Object) this;
	
	/**
	 * Has two functions. Firstly, makes it so that when a player executes a pattern, if that pattern is marked as a
	 * macro in their Everbook it executes the macro instead. Secondly, if the caster is transmitting to a Linkable it
	 * will send all iotas that would have been executed to the Linkable instead.
	 */
	@Inject(method = "executeInner", at = @At("HEAD"), cancellable = true)
	private void executeIotaMacro (Iota iota, ServerLevel world, SpellContinuation continuation, CallbackInfoReturnable<CastResult> cir) {
		CastingEnvironment ctx = harness.getEnv();
		CastingImage img = harness.getImage();
		
		// only work if the caster is staff-casting and they haven't escaped this pattern
		// (meaning you can get a copy of the pattern to mark it as not a macro again)
		if (!(ctx instanceof StaffCastEnv))
			return;
		if (img.getEscapeNext())
			return;
		else if (iota.getType() != HexIotaTypes.PATTERN
						 || ((PatternIota) iota).getPattern().sigsEqual(SpecialPatterns.CONSIDERATION)) // hacky, make it so people can't lock themselves
			return;

		HexPattern pattern = ((PatternIota) iota).getPattern();
		List<Iota> toExecute = IXplatAbstractions.INSTANCE.getEverbookMacro(ctx.getCaster(), pattern);
		if (toExecute == null) return;
		ContinuationFrame frame = new FrameEvaluate(new SpellList.LList(toExecute), false);
		// HACK silent hermes, should by all rights break parens and stuff
		// TODO better colors if possible
		cir.setReturnValue(new CastResult(continuation.pushFrame(frame), img,  List.of(), ResolvedPatternType.EVALUATED, HexEvalSounds.NOTHING));
	}

	@Inject(method = "queueExecuteAndWrapIotas", at = @At("TAIL"), cancellable = true)
	private void dontClearTheReaper(CallbackInfoReturnable<ExecutionClientView> cir) {
		ExecutionClientView r = cir.getReturnValue();
		if (r.isStackClear() && IXplatAbstractions.INSTANCE.getPlayerTransmittingTo(harness.getEnv().getCaster()) != null) {
			cir.setReturnValue(new ExecutionClientView(false, r.getResolutionType(), r.getStackDescs(), r.getRavenmind()));
		}
	}

	@Inject(method = "handleParentheses", at= @At("HEAD"), cancellable = true)
	private void handleTransmission(Iota iota, CallbackInfoReturnable<Pair<CastingImage, ResolvedPatternType>> cir) {
		CastingEnvironment ctx = harness.getEnv();
		if (ctx instanceof WispCastingEnvironment) return;
		var transmittingTo = IXplatAbstractions.INSTANCE.getPlayerTransmittingTo(ctx.getCaster());
		boolean transmitting = transmittingTo != null;
		boolean isEscaped = harness.getImage().getEscapeNext();
		boolean unescapedPatten = !isEscaped && iota.getType() == HexIotaTypes.PATTERN;
		CastingImage image = harness.getImage();
		if (transmitting && unescapedPatten && ((PatternIota) iota).getPattern().sigsEqual(Patterns.LINK_COMM_CLOSE_TRANSMIT.getPattern())) {
			return;
		}

		boolean isUnescapedEscape = unescapedPatten &&
				((PatternIota) iota).getPattern().sigsEqual(SpecialPatterns.CONSIDERATION);
		if (transmitting && !isUnescapedEscape) {
			transmittingTo.receiveIota(IXplatAbstractions.INSTANCE.getLinkstore(ctx.getCaster()), iota);

			if (isEscaped) {
				image = image.copy(image.getStack(), image.getParenCount(), image.getParenthesized(), false, image.getOpsConsumed(), image.getUserData());
			}
			cir.setReturnValue(new Pair<>(image, ResolvedPatternType.ESCAPED));
		}
	}
}
