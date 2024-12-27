package codes.kooper.quarryTools.commands;

import codes.kooper.shaded.litecommands.annotations.argument.Arg;
import codes.kooper.shaded.litecommands.annotations.command.Command;
import codes.kooper.shaded.litecommands.annotations.context.Context;
import codes.kooper.shaded.litecommands.annotations.execute.Execute;
import codes.kooper.shaded.litecommands.annotations.permission.Permission;
import codes.kooper.quarryTools.listeners.AutoMineListener;
import org.bukkit.entity.Player;

@Command(name = "automine")
@Permission("quarrytools.admin")
public class AutoMineCommand {

    private final AutoMineListener autoMineListener;

    public AutoMineCommand(AutoMineListener autoMineListener) {
        this.autoMineListener = autoMineListener;
    }

    @Execute
    public void onCommand(
            @Context Player player,
            @Arg double minBlocks,
            @Arg double maxBlocks,
            @Arg double intervalSeconds
    ) {
        if (minBlocks < 0 || maxBlocks <= 0 || minBlocks >= maxBlocks) {
            player.sendMessage("§cInvalid block range. Ensure minBlocks is less than maxBlocks and both are positive.");
            return;
        }
        if (intervalSeconds < 1) {
            player.sendMessage("§cInvalid time interval. It must be at least 1 second.");
            return;
        }

        autoMineListener.setBlockRange(minBlocks, maxBlocks);
        autoMineListener.setInterval(intervalSeconds);

        player.sendMessage("§aAutoMine settings updated:");
        player.sendMessage("§aBlock range: " + minBlocks + " to " + maxBlocks);
        player.sendMessage("§aInterval: " + intervalSeconds + " seconds");
    }
}