package net.wisecase2.stutterfix.mixin.gui;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.wisecase2.stutterfix.StutterFix;
import net.wisecase2.stutterfix.gui.StutterFixOptionsGUI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {

    protected OptionsScreenMixin(Component title) {
        super(title);
    }

    @Inject(
            method = "init()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;I)Lnet/minecraft/client/gui/layouts/LayoutElement;",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void inject_StutterFixOption(CallbackInfo ci, GridLayout gridLayout, GridLayout.RowHelper rowHelper) {
        if (!StutterFix.threadconfig.hideGui) {
            rowHelper.addChild(Button.builder(Component.translatable("stutterfix.name"), (button) ->
                    this.minecraft.setScreen(new StutterFixOptionsGUI(this, this.minecraft.options))
            ).build(), 2);
        }
    }
}