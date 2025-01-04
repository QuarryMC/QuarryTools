package codes.kooper.quarryTools.managers;

import codes.kooper.koopKore.KoopKore;
import codes.kooper.koopKore.database.models.User;
import codes.kooper.quarryMines.database.models.Quarry;
import codes.kooper.quarryMines.managers.ChromaManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class MineResetManager {
    private final Map<UUID, AtomicInteger> mineResetCache;

    public MineResetManager() {
        mineResetCache = new ConcurrentHashMap<>();
    }

    public void mineBlocks(Quarry quarry, int blocksMined) {
        UUID quarryUuid = quarry.getUuid();

        // Ensure thread-safe access to the cache
        mineResetCache.computeIfAbsent(quarryUuid, k -> new AtomicInteger(0));
        if (mineResetCache.get(quarryUuid).get() == 0) {
            System.out.println("Mine reset cache is at 0: uuid(" + quarryUuid + ")");
            return;
        }

        synchronized (mineResetCache.get(quarryUuid)) {
            final AtomicInteger currentCount = mineResetCache.get(quarryUuid);
            final int updatedCount = currentCount.addAndGet(blocksMined);

            System.out.println("Updated mine reset cache: uuid(" + quarryUuid + ") - updatedCount(" + updatedCount + ")");

            if (updatedCount >= getMineBlockCount(quarry)) {
                currentCount.set(0); // Reset the count after reaching the limit
                System.out.println("Resetting mine reset cache: uuid(" + quarryUuid + ")");

                Player player = Bukkit.getPlayer(quarry.getOwner());
                if (player == null) {
                    System.out.println("Player not found for quarry: " + quarryUuid);
                    return;
                }

                Optional<User> userOptional = KoopKore.getInstance().getUserAPI().getUser(player.getUniqueId());
                if (userOptional.isEmpty()) {
                    System.out.println("User not found for player: " + player.getUniqueId());
                    return;
                }

                // Perform mine reset synchronously
                ChromaManager.resetMine(player, userOptional.get(), quarry);
                System.out.println("Finished mine reset: uuid(" + quarryUuid + ")");
                mineResetCache.remove(quarryUuid);
            }
        }
    }

    private int getMineBlockCount(Quarry quarry) {
        return (quarry.getSize() + 1) * 800;
    }
}