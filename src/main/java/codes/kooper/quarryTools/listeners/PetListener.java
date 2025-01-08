package codes.kooper.quarryTools.listeners;

import codes.kooper.koopKore.utils.CacheEntry;
import codes.kooper.quarryPets.QuarryPets;
import codes.kooper.quarryPets.database.models.Pet;
import codes.kooper.quarryTools.events.QuarryMineEvent;
//import codes.kooper.quarryTools.utils.MineUtils;
import codes.kooper.quarryTools.utils.MineUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class PetListener implements Listener {
    private final Map<UUID, CacheEntry<Integer>> petNukerCache;
    private final Map<UUID, Integer> petLevelCache;
    private static final long EXPIRATION_TIME_MS = 60_000;

    public PetListener() {
        petNukerCache = new HashMap<>();
        petLevelCache = new HashMap<>();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        petNukerCache.remove(event.getPlayer().getUniqueId());
        petLevelCache.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onMine(QuarryMineEvent event) {
        int add = ThreadLocalRandom.current().nextInt(1, 6);
        QuarryPets.getInstance().getPetManager().addXPToPets(event.getPlayer(), add, event.getUser());
    }

    @EventHandler
    public void onLuckyBlock(QuarryMineEvent event) {
        Player player = event.getPlayer();
        if (petNukerCache.get(player.getUniqueId()) == null || petNukerCache.get(player.getUniqueId()).isExpired(EXPIRATION_TIME_MS)) {
            int best = 0;
            int level = 0;
            for (Pet pet : QuarryPets.getInstance().getPetManager().getSelectedPets(player)) {
                if (pet.getLuckyBlockNuker() > best) {
                    best = pet.getLuckyBlockNuker();
                    level = pet.getLevel();
                }
            }
            petLevelCache.put(player.getUniqueId(), level);
            petNukerCache.put(player.getUniqueId(), new CacheEntry<>(best, System.currentTimeMillis()));
        }
        if (petNukerCache.get(player.getUniqueId()).getValue() == 0) return;
        int nukerRadius = petNukerCache.get(player.getUniqueId()).getValue();
        int level = petLevelCache.get(player.getUniqueId());
        double chance = level / 200.0;
        if (ThreadLocalRandom.current().nextDouble(0, 101) > chance) return;
        MineUtils.luckyBlockNuker(player, event.getLocation(), nukerRadius);
    }
}
