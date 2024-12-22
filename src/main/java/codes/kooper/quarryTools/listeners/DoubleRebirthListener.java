package codes.kooper.quarryTools.listeners;

import codes.kooper.koopKore.database.models.User;
import codes.kooper.quarryEconomy.events.LevelingEvent;
import codes.kooper.quarryTools.QuarryTools;
import codes.kooper.quarryTools.items.ArmorItems;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.concurrent.ThreadLocalRandom;

public class DoubleRebirthListener implements Listener {

    @EventHandler
    public void onRebirth(LevelingEvent event) {
        if (!event.isRebirth()) return;
        Player player = Bukkit.getPlayer(event.getUser().getId());
        User user = event.getUser();
        if (player == null) return;
        if (!QuarryTools.getInstance().getArmorItems().inFullSetCache(player.getUniqueId())) return;
        ArmorItems.ArmorSet armorSet = QuarryTools.getInstance().getArmorItems().getFullSet(player.getUniqueId());
        if (!armorSet.setName().equalsIgnoreCase("phantom")) return;
        System.out.println("Phantom double rebirth");
        if (ThreadLocalRandom.current().nextDouble() > 0.2) return;
        user.setRebirth(user.getRebirth() + 1);
    }

}
