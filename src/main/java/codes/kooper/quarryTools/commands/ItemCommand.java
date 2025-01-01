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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

import static codes.kooper.koopKore.KoopKore.textUtils;

@Command(name = "quarrytools", aliases = {"tools", "items", "giveitem", "givetool", "armor", "givearmor"})
public class ItemCommand {

    @Execute
    @Permission("quarrytools.admin")
    public void executeItem(@Context CommandSender sender, @Arg String name, @Arg Player target, @Arg Optional<Integer> amount) {
        Integer quantity = amount.orElse(1);
        if (name.contains("pickaxe")) {
            Optional<PickaxeStorage> optionalPickaxeStorage = QuarryTools.getInstance().getPickStorageCache().get(target.getUniqueId());
            if (optionalPickaxeStorage.isEmpty()) return;
            PickaxeStorage pickaxeStorage = optionalPickaxeStorage.get();
            String newName = name.replace("_pickaxe", "");
            PickaxeItems.Pickaxe pickaxe = QuarryTools.getInstance().getPickaxeItems().getPickaxe(newName);
            pickaxeStorage.getPickaxes().put(pickaxe.name(), new Pickaxe(pickaxe));
            sender.sendMessage(textUtils.success("You have given " + target.getName() + " " + quantity + "x of " + textUtils.capitalize(name) + "."));
            return;
        }
        ItemStack item = QuarryTools.getInstance().getItemManager().getItem(name);
        if (item == null) {
            sender.sendMessage(textUtils.error("The specified tool does not exist."));
            return;
        }
        item.setAmount(quantity);
        target.getInventory().addItem(item);
        sender.sendMessage(textUtils.success("You have given " + target.getName() + " " + quantity + "x of " + textUtils.capitalize(name) + "."));
    }

}
