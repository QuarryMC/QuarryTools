package codes.kooper.quarryTools.commands;

import codes.kooper.quarryTools.items.ArmorItems;
import codes.kooper.shaded.litecommands.annotations.argument.Arg;
import codes.kooper.shaded.litecommands.annotations.command.Command;
import codes.kooper.shaded.litecommands.annotations.context.Context;
import codes.kooper.shaded.litecommands.annotations.execute.Execute;
import codes.kooper.shaded.litecommands.annotations.optional.OptionalArg;
import codes.kooper.shaded.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;

import java.util.Optional;

@Command(name = "givearmorset")
@Permission("quarrytools.admin")
public class GiveArmorSetCommand {

    @Execute
    public void onCommand(@Context Player player, @Arg ArmorItems.ArmorSet set, @OptionalArg Optional<Player> target) {
        Player p = target.orElse(player);
        for (ArmorItems.Armor armor : set.armorPieces().values()) {
            p.getInventory().addItem(armor.itemStack());
        }
    }

}
