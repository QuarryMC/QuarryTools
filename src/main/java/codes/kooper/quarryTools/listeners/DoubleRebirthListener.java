package codes.kooper.quarryTools.listeners;

import codes.kooper.koopKore.database.models.User;
import codes.kooper.quarryEconomy.events.LevelingEvent;
import codes.kooper.quarryTools.QuarryTools;
import codes.kooper.quarryTools.items.ArmorItems;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.concurrent.ThreadLocalRandom;

import static codes.kooper.koopKore.KoopKore.textUtils;

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
        if (ThreadLocalRandom.current().nextDouble() > 0.2) return;
        player.playSound(player.getLocation(), Sound.ENTITY_PARROT_IMITATE_PHANTOM, 5, 1.5f);
        player.sendMessage(textUtils.colorize(armorSet.color() + "<bold>PHANTOM ARMOR:<reset><blue> Your phantom armor set gave you an extra rebirth!"));
        user.setRebirth(user.getRebirth() + 1);
    }

}
