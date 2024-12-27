package codes.kooper.quarryTools.commands;


import codes.kooper.quarryTools.QuarryTools;
import codes.kooper.quarryTools.database.models.Pickaxe;
import codes.kooper.quarryTools.database.models.PickaxeStorage;
import codes.kooper.quarryTools.items.PickaxeItems;
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
        if (name.contains("pickaxe")) {
            Optional<PickaxeStorage> optionalPickaxeStorage = QuarryTools.getInstance().getPickStorageCache().get(player.getUniqueId());
            if (optionalPickaxeStorage.isEmpty()) return;
            PickaxeStorage pickaxeStorage = optionalPickaxeStorage.get();
            String newName = name.replace("_pickaxe", "");
            PickaxeItems.Pickaxe pickaxe = QuarryTools.getInstance().getPickaxeItems().getPickaxe(newName);
            pickaxeStorage.getPickaxes().add(new Pickaxe(pickaxe));
            player.sendMessage(textUtils.success("You have given " + giveTo.getName() + " " + quantity + "x of " + textUtils.capitalize(name) + "."));
            return;
        }
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
