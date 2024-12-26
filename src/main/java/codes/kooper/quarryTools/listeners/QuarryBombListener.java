package codes.kooper.quarryTools.listeners;

import codes.kooper.quarryTools.items.QuarryBombItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class QuarryBombListener implements Listener {

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        if (!QuarryBombItem.isBomb(event.getItem())) return;
        if (event.getItem() == null) return;
        final ItemStack item = event.getItem().clone();
        event.getItem().setAmount(event.getItem().getAmount() - 1);
        QuarryBombItem.quarryBombUse(event.getPlayer(), item);
    }

}
