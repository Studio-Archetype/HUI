package studio.archetype.holoui.utils;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Consumer;

public class SchedulerUtils {

    public static BukkitTask scheduleSyncTimer(Plugin p, long period, long repetitions, Consumer<Long> onIteration, Runnable onFinish) {
        return new TimerRunnable(onIteration, onFinish, repetitions).runTaskTimer(p, 0L, period);
    }

    public static BukkitTask scheduleSyncTask(Plugin p, long period, Runnable onIteration, boolean delayStart) {
        return Bukkit.getScheduler().runTaskTimer(p, onIteration, period, delayStart ? period : 0);
    }

    @RequiredArgsConstructor
    private static class TimerRunnable extends BukkitRunnable {

        private final Consumer<Long> onIteration;
        private final Runnable onFinish;
        private final long iterations;

        private long currentIterations;

        @Override
        public void run() {
            if(currentIterations >= iterations) {
                onFinish.run();
                cancel();
                return;
            }
            onIteration.accept(currentIterations);
            currentIterations++;
        }
    }
}
