package org.moon.moonfix.mixin;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.moon.moonfix.config.ConfigManager.Config;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {

    @Mutable @Shadow @Final private static Set<Item> BLOCK_MARKER_ITEMS;

    //add structure void and tripwire to have a block marker when holding its items on creative
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void init(CallbackInfo ci) {
        HashSet<Item> set = new HashSet<>(BLOCK_MARKER_ITEMS);

        if ((boolean) Config.STRUCTURE_VOID.value)
            set.add(Items.STRUCTURE_VOID);

        if ((boolean) Config.TRIPWIRE.value)
            set.add(Items.STRING);

        BLOCK_MARKER_ITEMS = set;
    }
}
