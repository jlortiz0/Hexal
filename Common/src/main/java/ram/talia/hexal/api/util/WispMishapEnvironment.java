package ram.talia.hexal.api.util;

import at.petrak.hexcasting.api.casting.eval.MishapEnvironment;
import at.petrak.hexcasting.api.misc.HexDamageSources;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.Vec3;
import ram.talia.hexal.common.entities.BaseCastingWisp;

import javax.annotation.Nonnull;

public class WispMishapEnvironment extends MishapEnvironment {
    private final BaseCastingWisp wisp;
    protected WispMishapEnvironment(ServerLevel world, @Nonnull BaseCastingWisp wisp) {
        super(world, null);
        this.wisp = wisp;
    }

    @Override
    public void yeetHeldItemsTowards(Vec3 vec3) {
        wisp.setDeltaMovement(this.wisp.position().subtract(vec3).normalize());
    }

    @Override
    public void dropHeldItems() {
        wisp.rotate(Rotation.CLOCKWISE_90);
    }

    @Override
    public void drown() {
        if (this.wisp.getAirSupply() == 0) {
            this.wisp.hurt(DamageSource.DROWN, 1);
        }
        this.wisp.setAirSupply(0);
    }

    @Override
    public void damage(float v) {
        this.wisp.hurt(HexDamageSources.OVERCAST, v);
    }

    @Override
    public void removeXp(int i) {
        wisp.setRemainingFireTicks(i);
    }

    @Override
    public void blind(int i) {
        wisp.setTicksFrozen(i);
    }
}
