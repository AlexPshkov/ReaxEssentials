package ru.alexpshkov.reaxessentials.commands;

import lombok.RequiredArgsConstructor;
import org.atteo.classindex.ClassIndex;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.service.interfaces.IInitRequired;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

@RequiredArgsConstructor
public class CommandManager implements IInitRequired {
    private final ReaxEssentials reaxEssentials;

    @Override
    public void init() throws IllegalAccessException, NoSuchFieldException {
        Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        bukkitCommandMap.setAccessible(true);
        CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

        ClassIndex.getSubclasses(AbstractCommand.class, reaxEssentials.getClass().getClassLoader()).forEach(aClass -> {
            try {
                AbstractCommand abstractCommand = aClass.getDeclaredConstructor(ReaxEssentials.class).newInstance(reaxEssentials);
                commandMap.register(abstractCommand.getCommandName(), abstractCommand);;
            } catch (IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }
}
