package codes.kooper.quarryTools.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import static codes.kooper.koopKore.KoopKore.textUtils;

public class AutoMineListener implements Listener {

    private final Vector corner1 = new Vector(-102, 62, -4);
    private final Vector corner2 = new Vector(-93, 72, 5);

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Vector playerPos = player.getLocation().toVector();
        if (isInArea(playerPos)) {
            player.sendMessage(textUtils.colorize("<green>You have entered the AutoMine area!"));
        }
    }

    private boolean isInArea(Vector position) {
        double minX = Math.min(corner1.getX(), corner2.getX());
        double maxX = Math.max(corner1.getX(), corner2.getX());
        double minY = Math.min(corner1.getY(), corner2.getY());
        double maxY = Math.max(corner1.getY(), corner2.getY());
        double minZ = Math.min(corner1.getZ(), corner2.getZ());
        double maxZ = Math.max(corner1.getZ(), corner2.getZ());

        return position.getX() > minX && position.getX() < maxX &&
                position.getY() > minY && position.getY() < maxY &&
                position.getZ() > minZ && position.getZ() < maxZ;
    }
}
