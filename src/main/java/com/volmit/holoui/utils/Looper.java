package com.volmit.holoui.utils;

import lombok.NonNull;
import lombok.extern.java.Log;

import java.util.function.LongSupplier;
import java.util.logging.Level;

@Log
public class Looper extends Thread {
    private final LongSupplier supplier;

    public Looper(@NonNull LongSupplier supplier) {
        this.supplier = supplier;
    }

    public static Looper fixed(@NonNull Runnable runnable, long interval) {
        if (interval < 0) throw new IllegalArgumentException("interval cannot be negative");
        return new Looper(() -> {
            long time = System.currentTimeMillis();
            try {
                runnable.run();
            } catch (Throwable e) {
                log.log(Level.SEVERE, "Error in fixed looper", e);
            }
            return Math.max(System.currentTimeMillis() - time - interval, 0);
        });
    }

    protected Looper() {
        this.supplier = () -> Long.MAX_VALUE;
    }

    protected long loop() {
        return supplier.getAsLong();
    }

    @Override
    public final void run() {
        while (!isInterrupted()) {
            try {
                long sleep = loop();
                if (sleep < 0) return;
                try {
                    sleep(sleep);
                } catch (InterruptedException e) {
                    return;
                }
            } catch (Throwable e) {
                log.log(Level.SEVERE, "Error in Looper " + getName(), e);
                return;
            }
        }
    }
}
