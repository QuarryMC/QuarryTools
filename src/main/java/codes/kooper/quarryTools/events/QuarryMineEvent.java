package codes.kooper.quarryTools.events;

import codes.kooper.koopKore.database.models.User;
import codes.kooper.quarryMines.database.models.Quarry;
import codes.kooper.quarryTools.items.PickaxeItems;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class QuarryMineEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Quarry quarry;
    private final User user;
    private final Location location;
    private final Player player;
    private final BlockData blockData;
    private final PickaxeItems.Pickaxe pickaxe;
    private int blocks = 1;
    private int resetBlocks = 1;

    public QuarryMineEvent(Quarry quarry, User user, Location location, Player player, BlockData blockData, PickaxeItems.Pickaxe pickaxe) {
        super(true);
        this.quarry = quarry;
        this.user = user;
        this.location = location;
        this.player = player;
        this.blockData = blockData;
        this.pickaxe = pickaxe;
    }

    public void addResetBlocks(int resetBlocks) {
        this.resetBlocks += resetBlocks;
    }

    public void addBlocks(int blocks) {
        this.blocks += blocks;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
