package net.wisecase2.stutterfix.mixin.client;

import net.minecraft.client.Minecraft;
import net.wisecase2.stutterfix.StutterFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {
    @Inject(method = "run()V", at = @At("HEAD"))
    private void loadThread(CallbackInfo ci) {
        if (!StutterFix.isClientInitialized) {
            StutterFix.loadRenderThread(Thread.currentThread());
            StutterFix.configPriorityRenderThread();
            StutterFix.isClientInitialized = true;
        }
    }
}