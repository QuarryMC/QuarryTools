package codes.kooper.quarryTools.listeners;

import codes.kooper.quarryMines.utils.QuarryBlockUtils;
import codes.kooper.quarryTools.events.QuarryMineEvent;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class QuarryBlockListener implements Listener {

    @EventHandler
    public void onQuarryBlock(QuarryMineEvent event) {
        if (!event.getBlockData().getMaterial().name().contains("GLAZED")) {
            event.getPlayer().playSound(event.getLocation(), Sound.BLOCK_ANCIENT_DEBRIS_BREAK, 1, 1.3f);
            return;
        }
        QuarryBlockUtils.handleLuckyBlock(event.getUser());
        event.getPlayer().playSound(event.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1, 1.3f);
    }

}
