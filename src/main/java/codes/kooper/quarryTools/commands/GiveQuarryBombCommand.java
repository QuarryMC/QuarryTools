package codes.kooper.quarryTools.commands;

import codes.kooper.quarryTools.items.QuarryBombItem;
import codes.kooper.shaded.litecommands.annotations.argument.Arg;
import codes.kooper.shaded.litecommands.annotations.command.Command;
import codes.kooper.shaded.litecommands.annotations.context.Context;
import codes.kooper.shaded.litecommands.annotations.execute.Execute;
import codes.kooper.shaded.litecommands.annotations.optional.OptionalArg;
import codes.kooper.shaded.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;

import java.util.Optional;

@Command(name = "givequarrybomb")
@Permission("quarrytools.admin")
public class GiveQuarryBombCommand {

    @Execute
    public void onCommand(@Context Player player, @Arg int tier, @OptionalArg Optional<Player> target, @OptionalArg Optional<Integer> amount) {
        Player p = target.orElse(player);
        int add = amount.orElse(1);
        p.getInventory().addItem(QuarryBombItem.getQuarryBombItem(tier, add));
    }
}
