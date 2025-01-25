package codes.kooper.quarryTools.guis;

import codes.kooper.shaded.gui.builder.item.ItemBuilder;
import codes.kooper.shaded.gui.guis.Gui;
import codes.kooper.shaded.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

import static codes.kooper.koopKore.KoopKore.textUtils;

public class CodexGUI {

    public CodexGUI(Player player) {
        Gui gui = Gui.gui()
                .title(Component.text("Codex"))
                .disableAllInteractions()
                .rows(4)
                .create();

        gui.getFiller().fill(ItemBuilder.from(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
                .name(Component.empty())
                .asGuiItem());

        int startRow = 2;
        int endRow = 3;
        int startCol = 2;
        int endCol = 8;

        List<ItemDetails> items = List.of(
                new ItemDetails(Material.ORANGE_WOOL, textUtils.colorize("<#fb6b1d><bold>Prestige"), textUtils.colorize(List.of(
                        "<#fb6b1d>Prestiging "+"<gray>gives you "+"<#a884f3>skill tokens",
                        "<gray>as well as unlocks better "+"<yellow>mines "+"<gray>when you get enough.",
                        "",
                        "<gray><bold>Command: /prestige"
                ))),
                new ItemDetails(Material.PINK_WOOL, textUtils.colorize("<#ed8099><bold>Rebirth"), textUtils.colorize(List.of(
                        "<#ed8099>Rebirthing "+"<gray>gives you a "+"<#ed8099>Rebirth"+" Lootbox",
                        "<gray>as well as access to better "+"<yellow>mines "+"<gray>and /rebirthshop.",
                        "",
                        "<gray><bold>Command: /rebirth"
                ))),
                new ItemDetails(Material.ENDER_DRAGON_SPAWN_EGG, textUtils.colorize("<dark_purple><bold>Bosses"), textUtils.colorize(List.of(
                        "<gray>Once you have 4 of a certain type of "+"<dark_purple>boss "+"block",
                        "<gray>you will be able to fight a "+"<dark_purple>boss at your "+"<yellow>mine.",
                        "",
                        "<gray><bold>Command: /bosses"
                ))),
                new ItemDetails(Material.DIAMOND, textUtils.colorize("<aqua><bold>Skills"), textUtils.colorize(List.of(
                        "<aqua>Skills "+"<gray>can be purchased to keep you progressing in the game.",
                        "<gray>You can see how much each "+"<aqua>Skill "+"<green>costs"+"<gray> by hovering over it.",
                        "",
                        "<gray><bold>Command: /skills"
                ))),
                new ItemDetails(Material.CREEPER_SPAWN_EGG, textUtils.colorize("<#609e60><bold>Eggs & Pets"), textUtils.colorize(List.of(
                        "<gray>Once you use an "+"<#609e60>egg"+"<gray>, you can choose an "+"<#609e60>egg "+"<gray>and",
                        "<gray>mine the required amount of blocks for your "+"<#609e60>egg "+"<gray>for it to hatch.",

                        "<gray>once you claim your "+"<#609e60>pet"+"<gray> use "+"<#609e60>/pets"+"<gray> to equip your new "+"<#609e60>pet"+"<gray>.",
                        "<#609e60>pets"+"<gray> will help you at your "+"<yellow>mine"+"<gray> via fortune etc.",
                        "",
                        "<#609e60>Commands: /eggs & /pets"

                ))),
                new ItemDetails(Material.GOLD_BLOCK, textUtils.colorize("<#ffbf00><bold>Auto Miner"), textUtils.colorize(List.of(
                        "<gray>Players can purchase "+"<#ffbf00>autominer"+"<gray> time and speed",
                        "<gray>via the upgrades tab in the autominergui",
                        "<gray>and can then AFK "+"<#ffbf00>automine"+"<gray> for "+"<#fb6b1d>prestiges.",
                        "",
                        "<dark_gray>Command: /autominer"
                ))),
                new ItemDetails(Material.WHITE_WOOL, textUtils.colorize("<white><bold>Moons"), textUtils.colorize(List.of(
                        "<gray>On quarry once 1M blocks are "+"<yellow>mined"+"<gray> globally the current",
                        "<white>moon"+"<gray> will be randomized causing the "+"<yellow>mine"+"<gray> to switch and the",
                        "rewards from "+"<yellow>mining"+"<gray> quarry blocks to change.",
                        "",
                        "<gray><bold>Command: /moons"

                ))),
                new ItemDetails(Material.PAPER, textUtils.colorize("<white><bold>Useful Commands"), textUtils.colorize(List.of(
                        "<gray><bold>/mines | Shows all unlockable mines",
                        "<gray><bold>/autosell | automatically sells your backpack when its full (rank locked cmd)",
                        "<gray><bold>/autoprestige | automatically prestiges (need required currencies & level)",
                        "<gray><bold>/pay | is used to pay other players currencies.",
                        "<gray><bold>/preferences | many QOL settings are located here."
                ))),
                new ItemDetails(Material.REDSTONE_BLOCK, textUtils.colorize("<red><bold>Hearts"), textUtils.colorize(List.of(
                        "<gray>On Quarry, "+"<red>Hearts "+"<grey>are a currency that can be used to purchase",
                        "<gray>a select variety of "+"<aqua>skills "+"<grey>and other items.",
                        "<red>Hearts"+"<gray> can be obtained via "+"<aqua>skills "+"<grey>and redstone blocks",
                        "",
                        "<gray><bold>NO COMMAND"
                ))),
                new ItemDetails(Material.FIRE_CHARGE, textUtils.colorize("<#fb7604><bold>Leaderboard"), textUtils.colorize(List.of(
                        "<grey>Top 5 players with the most "+"<yellow>mined"+"<grey> blocks get comets",
                        "<grey>comets can be used in the quarry shop to buy many things.",
                        "",
                        "<gray><bold>NO COMMAND"
                )))
        );

        int index = 0;
        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                if (index >= items.size()) {
                    break;
                }
                ItemDetails item = items.get(index);
                GuiItem guiItem = ItemBuilder.from(item.material)
                        .name(item.name)
                        .lore(item.lore)
                        .asGuiItem();
                int slot = (row - 1) * 9 + (col - 1);
                gui.setItem(slot, guiItem);
                index++;
            }
        }

        gui.open(player);
    }

    private static class ItemDetails {
        Material material;
        Component name;
        List<Component> lore;

        public ItemDetails(Material material, Component name, List<Component> lore) {
            this.material = material;
            this.name = name;
            this.lore = lore;
        }
    }
}
