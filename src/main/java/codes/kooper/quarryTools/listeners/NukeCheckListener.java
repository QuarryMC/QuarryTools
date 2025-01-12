package codes.kooper.quarryTools.listeners;

import codes.kooper.koopKore.utils.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static codes.kooper.koopKore.KoopKore.textUtils;

public class NukeCheckListener implements Listener {
    private final Map<UUID, Integer> nukeCache;
    private final Set<UUID> staff;
    private final Map<UUID, Long> lastAlertTime; // Store the last alert time for each player
    private static final int blocksThreshold = 30;
    private static final int distanceThreshold = 49; // 7 blocks
    private static final long ALERT_COOLDOWN = TimeUnit.SECONDS.toMillis(5); // 5 second cooldown for each alert

    public NukeCheckListener() {
        nukeCache = new HashMap<>();
        staff = new HashSet<>();
        lastAlertTime = new HashMap<>();

        Tasks.runSyncTimer(() -> {
            nukeCache.forEach((uuid, val) -> {
                if (val >= blocksThreshold) {
                    alertStaff(uuid, val);
                }
            });
            nukeCache.clear();
        }, 0L, 20L);
    }

    private void alertStaff(UUID uuid, int blocks) {
        Player nuker = Bukkit.getPlayer(uuid);
        if (nuker == null) return;

        // Get the current time
        long currentTime = System.currentTimeMillis();

        // Check if the alert cooldown has passed for this player
        if (lastAlertTime.containsKey(uuid) && currentTime - lastAlertTime.get(uuid) < ALERT_COOLDOWN) {
            return; // Don't send alert if within cooldown period
        }

        // Update the last alert time for the player
        lastAlertTime.put(uuid, currentTime);

        // Send the alert to all staff members
        for (UUID id : staff) {
            Player staffPlayer = Bukkit.getPlayer(id);
            if (staffPlayer == null) continue;
            staffPlayer.sendMessage(textUtils.colorize("<red>" + nuker.getName() + " may be nuking! They have " + blocks + " blocks in the last second."));
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (!event.isClientSided()) return;
        UUID uuid = event.getPlayer().getUniqueId();
        nukeCache.put(uuid, nukeCache.getOrDefault(uuid, 0) + 1);

        double distance = event.getBlock().getLocation().distanceSquared(event.getPlayer().getLocation());
        if (distance > distanceThreshold) {
            for (UUID id : staff) {
                Player staffPlayer = Bukkit.getPlayer(id);
                if (staffPlayer == null) continue;
                staffPlayer.sendMessage(textUtils.colorize("<red>" + event.getPlayer().getName() + " may be using reach! They broke a block " + Math.sqrt(distance) + " blocks away."));
            }
        }
    }

    @EventHandler
    public void onStaffJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPermission("nuker.notify")) return;
        staff.add(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onStaffQuit(PlayerQuitEvent event) {
        if (!event.getPlayer().hasPermission("nuker.notify")) return;
        staff.remove(event.getPlayer().getUniqueId());
    }
}
