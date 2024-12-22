package codes.kooper.quarryTools.managers;

import codes.kooper.koopKore.utils.Tasks;
import codes.kooper.quarryMines.database.models.Quarry;
import codes.kooper.quarryMines.managers.BlockifyManager;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class MineResetManager {
    private final Map<UUID, Integer> mineResetCache;

    public MineResetManager() {
        mineResetCache = new HashMap<>();
    }

    public void mineBlocks(Quarry quarry, int block) {
        mineResetCache.put(quarry.getUuid(), mineResetCache.getOrDefault(quarry.getUuid(), 0) + block);
        if (mineResetCache.get(quarry.getUuid()) < getMineBlockCount(quarry)) return;
        mineResetCache.put(quarry.getUuid(), 0);
        Tasks.runAsync(() -> BlockifyManager.resetMine(quarry));
    }

    private int getMineBlockCount(Quarry quarry) {
        return (quarry.getSize() + 1) * 1600;
    }
}
