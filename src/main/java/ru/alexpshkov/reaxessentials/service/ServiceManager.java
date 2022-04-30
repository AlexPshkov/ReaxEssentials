package ru.alexpshkov.reaxessentials.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.service.interfaces.IInitRequired;
import ru.alexpshkov.reaxessentials.service.services.EconomyService;

@Getter
@RequiredArgsConstructor
public class ServiceManager implements IInitRequired {
    private final ReaxEssentials reaxEssentials;

    private Economy economyService;
    private Chat chatService;

    @Override
    public void init() {
        RegisteredServiceProvider<Chat> chatRegisteredServiceProvider = reaxEssentials.getServer().getServicesManager().getRegistration(Chat.class);
        if (chatRegisteredServiceProvider == null) {
            reaxEssentials.getLogger().info("No chat provider found. Please install at least LuckPerms");
            Bukkit.shutdown();
            return;
        }
        economyService = new EconomyService(reaxEssentials.getDataBase());
        reaxEssentials.getServer().getServicesManager().register(Economy.class, economyService, reaxEssentials, ServicePriority.Highest);

        chatService = chatRegisteredServiceProvider.getProvider();
    }


}
