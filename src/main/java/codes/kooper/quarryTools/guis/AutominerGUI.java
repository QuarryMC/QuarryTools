package codes.kooper.quarryTools.guis;

import codes.kooper.koopKore.KoopKore;
import codes.kooper.koopKore.database.models.User;
import codes.kooper.quarryTools.QuarryTools;
import codes.kooper.shaded.gui.builder.item.ItemBuilder;
import codes.kooper.shaded.gui.guis.Gui;
import codes.kooper.shaded.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.List;
import java.util.Optional;

import static codes.kooper.koopKore.KoopKore.textUtils;

public class AutominerGUI {

    public AutominerGUI(Player player) {
        Optional<User> userOptional = KoopKore.getInstance().getUserAPI().getUser(player.getUniqueId());
        if (userOptional.isEmpty()) return;
        User user = userOptional.get();

        Gui gui = Gui.gui().title(Component.text("Autominer")).disableAllInteractions().create();
        gui.getFiller().fill(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).name(Component.empty()).asGuiItem());

        if (QuarryTools.getInstance().getAutoMineListener().getPlayersInArea().contains(player)) {
            GuiItem guiItem = ItemBuilder.from(Material.RED_DYE)
                    .name(textUtils.colorize("<red><bold>DEACTIVATE AUTOMINER"))
                    .lore(textUtils.colorize(List.of(
                        "<gray>Stop Automining and",
                        "<gray>return to spawn."
                    )))
                    .asGuiItem();
            guiItem.setAction((action) -> player.performCommand("spawn"));
            gui.setItem(2, guiItem);
        } else {
            GuiItem guiItem = ItemBuilder.from(Material.LIME_DYE)
                    .name(textUtils.colorize("<green><bold>ACTIVATE AUTOMINER"))
                    .lore(textUtils.colorize(List.of()))
                    .asGuiItem();
            guiItem.setAction((action) -> player.teleport(new Location(player.getWorld(), -97, 62.5, 0)));
            gui.setItem(2, guiItem);
        }

        long seconds = user.getAutoMinerTime();
        String formatted = String.format("%dd%dh%dm",
            seconds / (24 * 3600),         // Days
            (seconds / 3600) % 24,         // Hours
            (seconds / 60) % 60          // Minutes
        );
        GuiItem timeItem = ItemBuilder.from(Material.CLOCK)
                .name(textUtils.colorize("<gold><bold>YOUR AUTOMINER"))
                .lore(textUtils.colorize(List.of(
                        "<yellow>Time Remaining: <white>" + formatted,
                        "<yellow>Speed: <white>" + user.getAutoMinerSpeed() + " <gray>(Prestiges Per Minute)"
                )))
                .asGuiItem();
        gui.setItem(4, timeItem);

        GuiItem upgradeItem = ItemBuilder.from(Material.ENDER_EYE)
                .name(textUtils.colorize("<yellow><bold>UPGRADE AUTOMINER"))
                .lore(textUtils.colorize(List.of(
                    "<gray>Click here to purchase",
                    "<gray>autominer upgrades."
                )))
                .asGuiItem();
        upgradeItem.setAction((action) -> new AutominerUpgradeGUI(player));
        gui.setItem(6, upgradeItem);

        gui.open(player);
    }

}
