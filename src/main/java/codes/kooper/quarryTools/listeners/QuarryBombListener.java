package codes.kooper.quarryTools.listeners;

import codes.kooper.quarryTools.items.QuarryBombItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static codes.kooper.koopKore.KoopKore.textUtils;

public class QuarryBombListener implements Listener {

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        if (!QuarryBombItem.isBomb(event.getItem())) return;
        if (event.getItem() == null) return;
        final ItemStack item = event.getItem().clone();
        if (!QuarryBombItem.quarryBombUse(event.getPlayer(), item)) {
            event.getPlayer().sendMessage(textUtils.error("Please look at a valid block in your mine to use this!"));
            return;
        }
        event.getItem().setAmount(event.getItem().getAmount() - 1);
    }

}
