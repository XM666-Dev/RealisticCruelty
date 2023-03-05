package com.xm666.realisticcruelty.particle;

import java.util.function.Consumer;

public class Process {
    public static void f(float x, float init, float mid, float end, Consumer<Float> initConsumer, Runnable midRunnable, Consumer<Float> endConsumer) {
        if (x < init) {
            initConsumer.accept(x / init);
        } else if ((x -= mid) > 0) {
            endConsumer.accept(x / end);
        } else {
            midRunnable.run();
        }
    }
}
