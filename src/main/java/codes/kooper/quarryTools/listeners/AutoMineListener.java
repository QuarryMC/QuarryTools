package codes.kooper.quarryTools.listeners;

import codes.kooper.koopKore.KoopKore;
import codes.kooper.koopKore.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static codes.kooper.koopKore.KoopKore.textUtils;

public class AutoMineListener implements Listener {

    private final JavaPlugin plugin;
    private final Vector corner1 = new Vector(-102, 62, -4);
    private final Vector corner2 = new Vector(-93, 72, 5);

    private final Set<Player> playersInArea = new HashSet<>();
    private final Random random = new Random();

    public AutoMineListener(JavaPlugin plugin) {
        this.plugin = plugin;
        startRewardTask();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Vector playerPos = player.getLocation().toVector();

        if (isInArea(playerPos)) {
            if (!playersInArea.contains(player)) {
                playersInArea.add(player);
                player.sendMessage(textUtils.colorize("<green>You have entered the AutoMine area!"));
            }
        } else {
            if (playersInArea.contains(player)) {
                playersInArea.remove(player);
                player.sendMessage(textUtils.colorize("<red>You have left the AutoMine area!"));
            }
        }
    }

    private boolean isInArea(Vector position) {
        double minX = Math.min(corner1.getX(), corner2.getX());
        double maxX = Math.max(corner1.getX(), corner2.getX());
        double minY = Math.min(corner1.getY(), corner2.getY());
        double maxY = Math.max(corner1.getY(), corner2.getY());
        double minZ = Math.min(corner1.getZ(), corner2.getZ());
        double maxZ = Math.max(corner1.getZ(), corner2.getZ());

        return position.getX() >= minX && position.getX() <= maxX &&
                position.getY() >= minY && position.getY() <= maxY &&
                position.getZ() >= minZ && position.getZ() <= maxZ;
    }

    private void startRewardTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : playersInArea) {
                Optional<User> userOptional = KoopKore.getInstance().getUserAPI().getUser(player.getUniqueId());
                if (userOptional.isEmpty()) continue;

                User user = userOptional.get();
                int amount = random.nextInt(60) + 1;
                user.addBlocksToBackpack(amount);
                player.sendMessage(textUtils.colorize("<yellow>You received " + amount + " blocks in your backpack!"));
            }
        }, 0L, 1200L);
    }
}
