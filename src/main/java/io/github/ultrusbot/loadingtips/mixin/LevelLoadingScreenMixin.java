package io.github.ultrusbot.loadingtips.mixin;

import io.github.ultrusbot.loadingtips.LoadingTips;
import net.minecraft.client.gui.WorldGenerationProgressTracker;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;


@Mixin(LevelLoadingScreen.class)
public abstract class LevelLoadingScreenMixin extends Screen {

    protected LevelLoadingScreenMixin(Text text) {
        super(text);
    }
    private String randomTip;
    float tipTimer = 0f;
    @Inject(method = "<init>", at = @At("TAIL"))
    void pickRandomTip(WorldGenerationProgressTracker worldGenerationProgressTracker, CallbackInfo ci) {
        randomTip = LoadingTips.getRandomTip();
    }

    @Inject(method = "render", at = @At("TAIL"))
    void drawLoadingTip(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        tipTimer+=delta;
        if (tipTimer >= 50F) {
            randomTip = LoadingTips.getRandomTip();
            tipTimer = 0;
        }
        List<OrderedText> wrappedText = textRenderer.wrapLines(new TranslatableText(randomTip), width/3);
        int textY = this.height-this.textRenderer.fontHeight;
        for (int i = wrappedText.size() - 1; i >= 0; i--) {
//        for (OrderedText orderedText : wrappedText) {
            OrderedText orderedText = wrappedText.get(i);
            textRenderer.draw(matrices, orderedText, 0, textY, 16777215);
            textY -= textRenderer.fontHeight * 1.25f;

        }
        drawTextWithShadow(matrices, this.textRenderer, new TranslatableText("text.loadingtips.tip"),0, textY, 3847130);
//        drawTextWithShadow(matrices, this.textRenderer, new TranslatableText(randomTip),0, this.height-this.textRenderer.fontHeight*3, 16777215);
    }
}
