package org.moon.moonfix.mixin;

import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.moon.moonfix.config.ConfigManager.Config;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SignEditScreen.class)
public class SignEditScreenMixin extends Screen {

    protected SignEditScreenMixin(Text title) {
        super(title);
    }

    @Shadow private SelectionManager selectionManager;
    @Shadow private int currentRow;
    @Shadow @Final private String[] text;
    @Shadow @Final private SignBlockEntity sign;

    //remove text limit from the sign edit screen >:3
    @Inject(at = @At("RETURN"), method = "init")
    public void init(CallbackInfo ci) {
        if ((boolean) Config.SIGN_TEXT.value) {
            this.selectionManager = new SelectionManager(() -> this.text[this.currentRow], text -> {
                this.text[this.currentRow] = text;
                this.sign.setTextOnRow(this.currentRow, new LiteralText(text));
            }, SelectionManager.makeClipboardGetter(this.client), SelectionManager.makeClipboardSetter(this.client), text -> true);
        }
    }
}
