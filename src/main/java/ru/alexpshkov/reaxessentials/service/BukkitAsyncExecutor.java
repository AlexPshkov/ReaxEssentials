package ru.alexpshkov.reaxessentials.service;

import com.sun.istack.internal.NotNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import ru.alexpshkov.reaxessentials.ReaxEssentials;

import java.util.concurrent.Executor;

@RequiredArgsConstructor
public class BukkitAsyncExecutor implements Executor {

    private final ReaxEssentials reaxEssentials;
    @Override
    public void execute(@NotNull Runnable command) {
        if(Thread.currentThread().getName().startsWith("Craft Scheduler Thread")) {
            command.run();
        } else Bukkit.getScheduler().runTaskAsynchronously(reaxEssentials, command);
    }
}
