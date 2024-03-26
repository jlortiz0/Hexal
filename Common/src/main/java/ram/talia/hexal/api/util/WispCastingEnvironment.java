package ram.talia.hexal.api.util;

import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.eval.CastResult;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.MishapEnvironment;
import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import ram.talia.hexal.common.entities.BaseCastingWisp;

import javax.annotation.Nonnull;
import java.util.List;

@MethodsReturnNonnullByDefault
public class WispCastingEnvironment extends CastingEnvironment {
    @Nonnull private final BaseCastingWisp wisp;

    public WispCastingEnvironment(@Nonnull BaseCastingWisp wisp) {
        super((ServerLevel) wisp.getLevel());
        this.wisp = wisp;
    }

    @Override
    public long extractMedia(long l) {
        long mediaAvailable = wisp.getMedia();
        if (mediaAvailable < 0L) {
            return 0L;
        } else {
            long mediaToTake = Math.min(l, mediaAvailable);
            l -= mediaToTake;
            wisp.setMedia((int) (mediaAvailable - mediaToTake));
            if (l > 0 && this.wisp.getShouldComplainNotEnoughMedia()) {
                this.printMessage(Component.translatable("hexcasting.message.cant_overcast"));
            }
            return l;
        }
    }

    @Override
    public InteractionHand getCastingHand() {
        return InteractionHand.MAIN_HAND;
    }

    @Override
    public FrozenPigment getPigment() {
        return wisp.colouriser();
    }

    public BaseCastingWisp getWisp () {
        return wisp;
    }

    @Override
    public @Nullable ServerPlayer getCaster() {
        return (ServerPlayer) this.wisp.getCaster();
    }

    @Override
    public void postExecution(CastResult result) {
        SoundEvent sound = result.getSound().sound();
        if (sound != null) {
            this.world.playSound(null, new BlockPos(this.wisp.position()), sound, SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        for (OperatorSideEffect sideEffect : result.getSideEffects()) {
            if (sideEffect instanceof OperatorSideEffect.DoMishap doMishap) {
                this.printMessage(doMishap.getMishap().errorMessageWithName(this, doMishap.getErrorCtx()));
            }
        }
    }

    @Override
    protected List<ItemStack> getUsableStacks(StackDiscoveryMode mode) {
        return List.of();
    }

    @Override
    protected List<HeldItemInfo> getPrimaryStacks() {
        return List.of();
    }

    @Override
    public boolean isVecInRange(Vec3 vec) {
        return this.wisp.position().distanceTo(vec) <= wisp.maxSqrCastingDistance();
    }

    @Override
    public boolean hasEditPermissionsAt(BlockPos blockPos) {
        return true;
    }

    @Override
    public ItemStack getAlternateItem() {
        return ItemStack.EMPTY.copy();
    }

    @Override
    public @Nullable FrozenPigment setPigment(@Nullable FrozenPigment pigment) {
        if (pigment == null) return this.wisp.colouriser();
        this.wisp.setColouriser(pigment);
        return pigment;
    }

    @Override
    public void produceParticles(ParticleSpray particles, FrozenPigment colorizer) {
        particles.sprayParticles(this.world, colorizer);
    }

    @Override
    public void printMessage(Component component) {
        if ((this.wisp.getCaster() instanceof ServerPlayer sp)) {
            sp.sendSystemMessage(component);
        }
    }

    @Override
    public Vec3 mishapSprayPos() {
        return this.wisp.position();
    }

    @Override
    public MishapEnvironment getMishapEnvironment() {
        return new WispMishapEnvironment(this.world, this.wisp);
    }
}
