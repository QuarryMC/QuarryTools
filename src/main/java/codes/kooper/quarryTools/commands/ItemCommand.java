package codes.kooper.quarryTools.commands;


import codes.kooper.quarryTools.QuarryTools;
import codes.kooper.shaded.litecommands.annotations.argument.Arg;
import codes.kooper.shaded.litecommands.annotations.command.Command;
import codes.kooper.shaded.litecommands.annotations.context.Context;
import codes.kooper.shaded.litecommands.annotations.execute.Execute;
import codes.kooper.shaded.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

import static codes.kooper.koopKore.KoopKore.textUtils;

@Command(name = "quarrytools", aliases = {"tools", "items", "giveitem", "givetool", "armor", "givearmor"})
public class ItemCommand {

    @Execute
    @Permission("quarrytools.admin")
    public void executeItem(@Context Player player, @Arg String name, @Arg Optional<Player> target, @Arg Optional<Integer> amount) {
        Player giveTo = target.orElse(player);
        Integer quantity = amount.orElse(1);
        ItemStack item = QuarryTools.getInstance().getItemManager().getItem(name);
        if (item == null) {
            player.sendMessage(textUtils.error("The specified tool does not exist."));
            return;
        }
        item.setAmount(quantity);
        giveTo.getInventory().addItem(item);
        player.sendMessage(textUtils.success("You have given " + giveTo.getName() + " " + quantity + "x of " + textUtils.capitalize(name) + "."));
    }

}
