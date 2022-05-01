package ru.alexpshkov.reaxessentials.service;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
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
