package codes.kooper.quarryTools.guis;

import codes.kooper.koopKore.KoopKore;
import codes.kooper.koopKore.database.models.User;
import codes.kooper.quarryEconomy.QuarryEconomy;
import codes.kooper.shaded.gui.builder.item.ItemBuilder;
import codes.kooper.shaded.gui.components.GuiType;
import codes.kooper.shaded.gui.guis.Gui;
import codes.kooper.shaded.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

import static codes.kooper.koopKore.KoopKore.numberUtils;
import static codes.kooper.koopKore.KoopKore.textUtils;

public class AutominerUpgradeGUI {

    public AutominerUpgradeGUI(Player player) {
        Optional<User> userOptional = KoopKore.getInstance().getUserAPI().getUser(player.getUniqueId());
        if (userOptional.isEmpty()) return;
        User user = userOptional.get();

        Gui gui = Gui.gui().type(GuiType.HOPPER).disableAllInteractions().title(Component.text("AutoMiner - Upgrades")).create();
        gui.getFiller().fill(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).name(Component.empty()).asGuiItem());

        double multi = QuarryEconomy.getInstance().getGlobalMultiplierManager().getGlobalMultiplier();

        long cost = (long) (4000000 * (multi / 3)) * 30;
        GuiItem clockItem = ItemBuilder.from(Material.CLOCK)
                .name(textUtils.colorize(textUtils.pc + "<bold>+30 Minutes"))
                .lore(textUtils.colorize(List.of(
                    "",
                    "<gray><italic>The cost of running the autominer increases",
                    "<gray><italic>to keep up with the current state of the",
                    "<gray><italic>economy by using the global multiplier.",
                    "",
                    textUtils.sc + "<bold>Cost: <reset><green>$" + numberUtils.format(cost),
                    "",
                    "<white><bold>Click to purchase."
                )))
                .asGuiItem();
        clockItem.setAction((action) -> {
            if (user.hasSufficientBalance(cost)) {
                user.subtractBalance(cost);
                user.addAutoMinerTime(1800);
                player.sendMessage(textUtils.success("+30 minutes added to autominer."));
                new AutominerUpgradeGUI(player);
            } else {
                player.sendMessage(textUtils.error("You don't have enough money."));
            }
        });
        gui.setItem(0, clockItem);

        long speedCost = (long) (75000000 * (multi / 2)) * (long) Math.pow(1.1, user.getAutoMinerSpeed());
        GuiItem speedItem = ItemBuilder.from(Material.RABBIT_FOOT)
                .name(textUtils.colorize("<gold><bold>Speed Upgrade"))
                .lore(textUtils.colorize(List.of(
                    "",
                    "<gray><italic>Increase how many prestiges",
                    "<gray><italic>you earn per minute.",
                    "",
                    textUtils.sc + "<bold>Cost: <reset><green>$" + numberUtils.format(speedCost),
                    "",
                    "<white><bold>Click to purchase."
                )))
                .asGuiItem();
        speedItem.setAction((action) -> {
            if (user.getAutoMinerSpeed() >= 50) {
                player.sendMessage(textUtils.error("You're speed is maxed out (Level 50)."));
                return;
            }
            if (user.hasSufficientBalance(speedCost)) {
                user.subtractBalance(speedCost);
                user.addAutoMinerSpeed(1);
                player.sendMessage(textUtils.success("+1 Speed."));
                new AutominerUpgradeGUI(player);
            } else {
                player.sendMessage(textUtils.error("You don't have enough money."));
            }
        });
        gui.setItem(2, speedItem);

        GuiItem backItem = ItemBuilder.from(Material.RED_STAINED_GLASS_PANE)
                    .name(textUtils.colorize("<red><bold>Back"))
                    .asGuiItem();
        backItem.setAction((action) -> new AutominerGUI(player));
        gui.setItem(4, backItem);

        gui.open(player);
    }

}
