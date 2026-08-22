package net.wisecase2.stutterfix.mixin.client;

import net.minecraft.client.Minecraft;
import net.wisecase2.stutterfix.StutterFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public class RemoveYieldMixin {
    @Redirect(method = "runTick(Z)V", at = @At(value = "INVOKE", target = "Ljava/lang/Thread;yield()V", remap = false))
    private void removeYield() {
        if (!StutterFix.threadconfig.renderRemoveYield) {
            Thread.yield();
        }
    }
}