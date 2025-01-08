package codes.kooper.quarryTools.managers;

import codes.kooper.koopKore.KoopKore;
import codes.kooper.koopKore.database.models.User;
import codes.kooper.koopKore.utils.Tasks;
import codes.kooper.quarryMines.database.models.Quarry;
import codes.kooper.quarryMines.managers.ChromaManager;
import io.papermc.paper.math.Position;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.Objects;
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

        synchronized (mineResetCache.get(quarryUuid)) {
            final AtomicInteger currentCount = mineResetCache.get(quarryUuid);
            if (currentCount == null) return;
            final int updatedCount = currentCount.addAndGet(blocksMined);

            if (updatedCount >= getMineBlockCount(quarry)) {
                currentCount.set(0);

                Player player = Bukkit.getPlayer(quarry.getOwner());
                if (player == null) return;

                Tasks.runSync(() -> {
                    if (Objects.requireNonNull(Objects.requireNonNull(player.getChromaBlockManager().getView(player.getName(), "mine")).getBound()).contains(Position.block(player.getLocation()))) {
                        Location location = player.getLocation();
                        location.setY(62.5);
                        player.teleport(location);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 26, 1, false, false, false));
                    }
                });

                Optional<User> userOptional = KoopKore.getInstance().getUserAPI().getUser(player.getUniqueId());
                if (userOptional.isEmpty()) return;

                ChromaManager.resetMine(player, userOptional.get(), quarry);
                mineResetCache.remove(quarryUuid);
            }
        }
    }

    private int getMineBlockCount(Quarry quarry) {
        int size = 5 + 2 * quarry.getSize(); // Total length and width
        int height = 74; // Fixed height
        int totalBlocks = size * size * height; // Volume of the mine
        return (int) (totalBlocks * 0.8); // Calculate the reset threshold
    }

}