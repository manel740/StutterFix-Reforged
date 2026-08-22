package net.wisecase2.stutterfix.mixin.util;

import net.minecraft.Util;
import net.wisecase2.stutterfix.StutterFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.concurrent.ExecutorService;

@Mixin(Util.class)
public abstract class utilMixin {

    /**
     * @author Wisecase2
     * @reason Replace main worker executor with a custom ForkJoinPool to reduce stuttering.
     */
    @Overwrite
    public static ExecutorService backgroundExecutor() {
        if (!StutterFix.isInitializedMainWorkerExecutor) {
            StutterFix.loadMainWorkerExecutor();
        }
        return StutterFix.mainWorkerExecutor;
    }
}