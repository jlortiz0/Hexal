package ram.talia.hexal.api.spell.casting;

import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface IMixinCastingEnvironment {

	int getConsumedMedia();

	void setConsumedMedia(int media);

	int getTimesTicked(BlockPos pos);

	void incTimesTicked(BlockPos pos);

	@Nullable UUID getBoundStorage();

	void setTemporaryBoundStorage(@Nullable UUID temporaryStorage);
}
