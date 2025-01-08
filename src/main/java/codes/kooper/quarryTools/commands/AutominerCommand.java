package codes.kooper.quarryTools.commands;

import codes.kooper.quarryTools.guis.AutominerGUI;
import codes.kooper.quarryTools.guis.AutominerUpgradeGUI;
import codes.kooper.shaded.litecommands.annotations.command.Command;
import codes.kooper.shaded.litecommands.annotations.context.Context;
import codes.kooper.shaded.litecommands.annotations.execute.Execute;
import org.bukkit.entity.Player;

@Command(name = "autominer", aliases = {"automine"})
public class AutominerCommand {

    @Execute
    public void onAutoMiner(@Context Player player) {
        new AutominerGUI(player);
    }

    @Execute(name = "upgrade", aliases = {"upgrades"})
    public void onAutoMinerUpgrades(@Context Player player) {
        new AutominerUpgradeGUI(player);
    }
}
