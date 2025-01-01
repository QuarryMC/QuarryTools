package codes.kooper.quarryTools.commands;

import codes.kooper.quarryTools.QuarryTools;
import codes.kooper.quarryTools.database.models.PickaxeStorage;
import codes.kooper.shaded.litecommands.annotations.argument.Arg;
import codes.kooper.shaded.litecommands.annotations.command.Command;
import codes.kooper.shaded.litecommands.annotations.execute.Execute;
import codes.kooper.shaded.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;

import java.util.Optional;

@Command(name = "fixpickaxe")
@Permission("quarrytools.admin")
public class FixPickaxeCommand {

    @Execute
    public void fix(@Arg Player target) {
        Optional<PickaxeStorage> optionalPickaxeStorage = QuarryTools.getInstance().getPickStorageCache().get(target.getUniqueId());
        if (optionalPickaxeStorage.isEmpty()) return;
        PickaxeStorage pickaxeStorage = optionalPickaxeStorage.get();
        target.getInventory().setItem(0, pickaxeStorage.getSelected().toItem(target).clone());
    }
}
