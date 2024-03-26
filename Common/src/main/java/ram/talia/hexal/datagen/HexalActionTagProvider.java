package ram.talia.hexal.datagen;

import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.mod.HexTags;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;

import static ram.talia.hexal.api.HexalAPI.modLoc;

public class HexalActionTagProvider extends TagsProvider<ActionRegistryEntry> {

    public HexalActionTagProvider(DataGenerator gen) {
        super(gen, IXplatAbstractions.INSTANCE.getActionRegistry());
    }
    @Override
    protected void addTags() {
        for (var normalGreat : new String[]{
                "everbook/delete", "everbook/read", "everbook/write", "everbook/toggle_macro",
                "wisp/consume"
        }) {
            var loc = modLoc(normalGreat);
            var key = ResourceKey.create(IXplatAbstractions.INSTANCE.getActionRegistry().key(), loc);
            tag(HexTags.Actions.REQUIRES_ENLIGHTENMENT).add(key);
            tag(HexTags.Actions.CAN_START_ENLIGHTEN).add(key);
        }

        for (var perWorldGreat : new String[]{
                "wisp/consume", "wisp/seon/set", "tick", "gate/make"
        }) {
            var loc = modLoc(perWorldGreat);
            var key = ResourceKey.create(IXplatAbstractions.INSTANCE.getActionRegistry().key(), loc);
            tag(HexTags.Actions.PER_WORLD_PATTERN).add(key);
        }
    }
}
