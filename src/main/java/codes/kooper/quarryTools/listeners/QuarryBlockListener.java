package codes.kooper.quarryTools.listeners;

import codes.kooper.quarryMines.utils.QuarryBlockUtils;
import codes.kooper.quarryTools.events.QuarryMineEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class QuarryBlockListener implements Listener {

    @EventHandler
    public void onQuarryBlock(QuarryMineEvent event) {
        if (!event.getBlockData().getMaterial().name().contains("GLAZED")) return;
        QuarryBlockUtils.handleLuckyBlock(event.getUser());
    }

}
