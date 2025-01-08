package codes.kooper.quarryTools.listeners;

import codes.kooper.koopKore.KoopKore;
import codes.kooper.koopKore.database.models.User;
import codes.kooper.quarryTools.QuarryTools;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static codes.kooper.koopKore.KoopKore.textUtils;

@Getter
public class AutoMineListener implements Listener {
    private final Vector corner1 = new Vector(-102, 62, -4);
    private final Vector corner2 = new Vector(-93, 72, 5);

    private final Set<Player> playersInArea = new HashSet<>();
    private final Random random = new Random();

    public AutoMineListener() {
        startRewardTask();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Vector playerPos = player.getLocation().toVector();

        if (isInArea(playerPos)) {
            if (!playersInArea.contains(player)) {
                Optional<User> userOptional = KoopKore.getInstance().getUserAPI().getUser(player.getUniqueId());
                if (userOptional.isEmpty()) return;
                User user = userOptional.get();

                if (user.getAutoMinerTime() <= 0) {
                    player.sendMessage(textUtils.error("You do not have any autominer time. Buy some with /autominer."));
                    player.performCommand("spawn");
                    return;
                }

                playersInArea.add(player);
                player.sendMessage(textUtils.success("You have entered the AutoMine area!"));
            }
        } else {
            if (playersInArea.contains(player)) {
                playersInArea.remove(player);
                player.sendMessage(textUtils.error("You have left the AutoMine area!"));
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
        Bukkit.getScheduler().runTaskTimer(QuarryTools.getInstance(), () -> {
            for (Player player : playersInArea) {
                Optional<User> userOptional = KoopKore.getInstance().getUserAPI().getUser(player.getUniqueId());
                if (userOptional.isEmpty()) continue;
                User user = userOptional.get();
                if (user.getAutoMinerTime() < 60) {
                    player.performCommand("spawn");
                    user.setAutoMinerTime(0);
                    player.sendMessage(textUtils.error("Your autominer has expired. Purchase more time in the /autominer menu."));
                    continue;
                }
                user.reduceAutoMinerTime(60);
                user.addPrestige(user.getAutoMinerSpeed());
                player.sendMessage(textUtils.colorize("<yellow>You received " + user.getAutoMinerSpeed() + " prestige(s) from autominer!"));
            }
        }, 0L, 60 * 20);
    }
}