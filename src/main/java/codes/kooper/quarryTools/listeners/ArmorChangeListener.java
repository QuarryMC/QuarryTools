package codes.kooper.quarryTools.listeners;

import codes.kooper.quarryTools.QuarryTools;
import codes.kooper.quarryTools.items.ArmorItems;
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ArmorChangeListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        QuarryTools.getInstance().getArmorItems().removeFullSet(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onArmorChange(PlayerArmorChangeEvent event) {
        Player player = event.getPlayer();
        if (!ArmorItems.hasFullArmorSet(player)) return;
        QuarryTools.getInstance().getArmorItems().addFullSet(player.getUniqueId(), QuarryTools.getInstance().getArmorItems().getArmorSet(player.getInventory().getHelmet()));
    }

}
