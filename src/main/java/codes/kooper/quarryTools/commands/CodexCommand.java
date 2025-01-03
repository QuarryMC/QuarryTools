package codes.kooper.quarryTools.commands;

import codes.kooper.quarryTools.guis.CodexGUI;
import codes.kooper.shaded.litecommands.annotations.command.Command;
import codes.kooper.shaded.litecommands.annotations.execute.Execute;
import codes.kooper.shaded.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;

@Command(name = "codex")
@Permission("codex.open")
public class CodexCommand {

    @Execute
    public void openCodex(Player player) {
        new CodexGUI(player);
    }
}
