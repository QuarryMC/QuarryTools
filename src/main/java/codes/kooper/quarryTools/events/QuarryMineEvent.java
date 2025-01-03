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

import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class QuarryMineEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Quarry quarry;
    private final User user;
    private final Location location;
    private final Player player;
    private final BlockData blockData;
    private final PickaxeItems.Pickaxe pickaxe;
    private final AtomicInteger blocks = new AtomicInteger(1);

    public QuarryMineEvent(Quarry quarry, User user, Location location, Player player, BlockData blockData, PickaxeItems.Pickaxe pickaxe) {
        super(true);
        this.quarry = quarry;
        this.user = user;
        this.location = location;
        this.player = player;
        this.blockData = blockData;
        this.pickaxe = pickaxe;
    }

    public void addBlocks(int blocks) {
        this.blocks.getAndAdd(blocks);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
