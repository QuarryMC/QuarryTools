package codes.kooper.quarryTools.events;

import codes.kooper.koopKore.database.models.User;
import codes.kooper.quarryMines.database.models.Quarry;
import codes.kooper.quarryTools.items.PickaxeItems;
import com.oresmash.blockify.models.Stage;
import com.oresmash.blockify.models.View;
import com.oresmash.blockify.types.BlockifyPosition;
import lombok.Getter;
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
    private final BlockifyPosition position;
    private final Stage stage;
    private final View view;
    private final Player player;
    private final BlockData blockData;
    private final PickaxeItems.Pickaxe pickaxe;
    private int blocks = 1;

    public QuarryMineEvent(Quarry quarry, User user, BlockifyPosition position, Stage stage, View view, Player player, BlockData blockData, PickaxeItems.Pickaxe pickaxe) {
        super(true);
        this.quarry = quarry;
        this.user = user;
        this.position = position;
        this.stage = stage;
        this.view = view;
        this.player = player;
        this.blockData = blockData;
        this.pickaxe = pickaxe;
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
