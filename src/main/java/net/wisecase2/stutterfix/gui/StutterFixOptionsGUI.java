package net.wisecase2.stutterfix.gui;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.Options;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;
import net.wisecase2.stutterfix.StutterFix;

public class StutterFixOptionsGUI extends Screen {
    private final Screen lastScreen;
    private final Options options;
    private OptionsList list;

    public StutterFixOptionsGUI(Screen parent, Options options) {
        super(Component.translatable("stutterfix.options.name"));
        this.lastScreen = parent;
        this.options = options;
    }

    @Override
    protected void init() {
        this.list = new OptionsList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);
        this.addOptions();
        this.addRenderableWidget(this.list);
        this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), (button) -> {
            this.minecraft.setScreen(this.lastScreen);
        }).bounds(this.width / 2 - 100, this.height - 27, 200, 20).build());
    }

    private void addOptions() {
        int max_threads_count = Runtime.getRuntime().availableProcessors();

        int default_vanilla = StutterFix.getDefaultVanillaMainWorkerExecutorCount();
        int default_stutterfix = StutterFix.getDefaultStutterFixMainWorkerExecutorCount();

        int defaultRenderThreadPriority = (max_threads_count > 4) ? 10 : 5;
        int defaultServerThreadPriority = (max_threads_count > 4) ? 8 : 5;

        this.list.addBig(
                new OptionInstance<Integer>(
                        "stutterfix.options.worker_threads",
                        OptionInstance.cachedConstantTooltip(Component.translatable("stutterfix.options.worker_threads.tooltip", default_vanilla, default_stutterfix)),
                        (OptionInstance.CaptionBasedToString<Integer>) (opt, value) -> Component.translatable("options.generic_value",
                                Component.translatable("stutterfix.options.worker_threads"),
                                Component.translatable("stutterfix.options.threads", value)),
                        new OptionInstance.IntRange(1, max_threads_count),
                        StutterFix.threadconfig.mainWorkerExecutorCount,
                        (Integer value) -> {
                            StutterFix.threadconfig.mainWorkerExecutorCount = value;
                            StutterFix.loadMainWorkerExecutor();
                        }
                )
        );

        this.list.addBig(
                new OptionInstance<Integer>(
                        "stutterfix.options.Worker_threads_priority_cut",
                        OptionInstance.cachedConstantTooltip(Component.translatable("stutterfix.options.Worker_threads_priority_cut.tooltip")),
                        (OptionInstance.CaptionBasedToString<Integer>) (opt, value) -> Component.translatable("options.generic_value",
                                Component.translatable("stutterfix.options.Worker_threads_priority_cut"),
                                Component.translatable("stutterfix.options.priority_cut", value)),
                        new OptionInstance.IntRange(0, max_threads_count),
                        StutterFix.threadconfig.mainWorkerExecutorPriorityCut,
                        (Integer value) -> {
                            StutterFix.threadconfig.mainWorkerExecutorPriorityCut = value;
                            StutterFix.loadMainWorkerExecutor();
                        }
                )
        );

        if (StutterFix.isInitializedRenderThread) {
            this.list.addBig(
                    new OptionInstance<Integer>(
                            "stutterfix.options.render_thread_priority",
                            OptionInstance.cachedConstantTooltip(Component.translatable("stutterfix.options.render_thread_priority.tooltip", defaultRenderThreadPriority)),
                            (OptionInstance.CaptionBasedToString<Integer>) (opt, value) -> Component.translatable("options.generic_value",
                                    Component.translatable("stutterfix.options.render_thread_priority"),
                                    Component.translatable("stutterfix.options.thread_priority", value)),
                            new OptionInstance.IntRange(1, 10),
                            StutterFix.threadconfig.renderThreadPriority,
                            (Integer value) -> {
                                StutterFix.threadconfig.renderThreadPriority = value;
                                StutterFix.configPriorityRenderThread();
                            }
                    )
            );
        }

        if (StutterFix.isInitializedServerThread) {
            this.list.addBig(
                    new OptionInstance<Integer>(
                            "stutterfix.options.server_thread_priority",
                            OptionInstance.cachedConstantTooltip(Component.translatable("stutterfix.options.server_thread_priority.tooltip", defaultServerThreadPriority)),
                            (OptionInstance.CaptionBasedToString<Integer>) (opt, value) -> Component.translatable("options.generic_value",
                                    Component.translatable("stutterfix.options.server_thread_priority"),
                                    Component.translatable("stutterfix.options.thread_priority", value)),
                            new OptionInstance.IntRange(1, 10),
                            StutterFix.threadconfig.serverThreadPriority,
                            (Integer value) -> {
                                StutterFix.threadconfig.serverThreadPriority = value;
                                StutterFix.configPriorityServerThread();
                            }
                    )
            );
        }

        if (StutterFix.removeYieldOption) {
            this.list.addBig(
                    new OptionInstance<Boolean>(
                            "stutterfix.options.remove_yield",
                            OptionInstance.cachedConstantTooltip(Component.translatable("stutterfix.options.remove_yield.tooltip")),
                            (OptionInstance.CaptionBasedToString<Boolean>) (opt, value) -> value
                                    ? Component.translatable("options.generic_value",
                                    Component.translatable("stutterfix.options.remove_yield"),
                                    Component.translatable("stutterfix.options.remove"))
                                    : Component.translatable("options.generic_value",
                                    Component.translatable("stutterfix.options.remove_yield"),
                                    Component.translatable("stutterfix.options.keep")),
                            OptionInstance.BOOLEAN_VALUES,
                            StutterFix.threadconfig.renderRemoveYield,
                            (Boolean value) -> StutterFix.threadconfig.renderRemoveYield = value
                    )
            );
        }
    }

    @Override
    public void onClose() {
        StutterFix.saveThread.execute(() -> StutterFix.threadconfig.saveConfig());
        this.minecraft.setScreen(this.lastScreen);
    }
}