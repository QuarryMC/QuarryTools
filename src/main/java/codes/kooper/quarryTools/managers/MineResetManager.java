package codes.kooper.quarryTools.managers;

import codes.kooper.koopKore.KoopKore;
import codes.kooper.koopKore.database.models.User;
import codes.kooper.koopKore.utils.Tasks;
import codes.kooper.quarryMines.database.models.Quarry;
import codes.kooper.quarryMines.managers.ChromaManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class MineResetManager {
    private final Map<UUID, Integer> mineResetCache;

    public MineResetManager() {
        mineResetCache = new ConcurrentHashMap<>();
    }

    public void mineBlocks(Quarry quarry, int block) {
        mineResetCache.put(quarry.getUuid(), mineResetCache.getOrDefault(quarry.getUuid(), 0) + block);
        if (mineResetCache.get(quarry.getUuid()) < getMineBlockCount(quarry)) return;
        mineResetCache.put(quarry.getUuid(), 0);
        Player player = Bukkit.getPlayer(quarry.getOwner());
        if (player == null) return;
        Optional<User> user = KoopKore.getInstance().getUserAPI().getUser(player.getUniqueId());
        if (user.isEmpty()) return;
        Tasks.runAsync(() -> ChromaManager.resetMine(player, user.get(), quarry));
    }

    private int getMineBlockCount(Quarry quarry) {
        return (quarry.getSize() + 1) * 1600;
    }
}
