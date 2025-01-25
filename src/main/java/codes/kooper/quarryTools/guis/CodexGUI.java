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
                        "<#fb6b1d>Prestiging " + "<grey>gives you " + "<#a884f3>skill tokens",
                        "<grey>as well as unlocks better " + "<yellow>mines " + "<grey>when you get enough.",
                        "",
                        "<grey><bold>Command: /prestige"
                ))),
                new ItemDetails(Material.PINK_WOOL, textUtils.colorize("<#ed8099><bold>Rebirth"), textUtils.colorize(List.of(
                        "<#ed8099>Rebirthing " + "<grey>gives you a " + "<#ed8099>Rebirth" + " Lootbox",
                        "<grey>as well as access to better " + "<yellow>mines " + "<grey>and /rebirthshop.",
                        "",
                        "<grey><bold>Command: /rebirth"
                ))),
                new ItemDetails(Material.ENDER_DRAGON_SPAWN_EGG, textUtils.colorize("<dark_purple><bold>Bosses"), textUtils.colorize(List.of(
                        "<grey>Once you have 4 of a certain type of " + "<dark_purple>boss " + "block",
                        "<grey>you will be able to fight a " + "<dark_purple>boss.",
                        "",
                        "<grey><bold>Command: /bosses"
                ))),
                new ItemDetails(Material.DIAMOND, textUtils.colorize("<aqua><bold>Skills"), textUtils.colorize(List.of(
                        "<aqua>Skills " + "<grey>can be purchased to keep you progressing in the game.",
                        "<grey>You can see how much each " + "<aqua>Skill " + "<grey>costs by hovering over it.",
                        "",
                        "<grey><bold>Command: /skills"
                ))),
                new ItemDetails(Material.MOOSHROOM_SPAWN_EGG, textUtils.colorize("<#a00f10><bold>Eggs & Pets"), textUtils.colorize(List.of(
                        "<grey>Once you use an "+"<#a00f10>egg"+"<grey>, you can choose an "+"<#a00f10>egg "+"<grey>and",
                        "<grey>mine the required amount of blocks for your "+"<#a00f10>egg "+"<grey>for it to hatch.",

                        "<grey>once you claim your "+"<#a00f10>pet"+"<grey> use "+"<#a00f10>/pets"+"<grey> to equip your new "+"<#a00f10>pet"+"<grey>.",
                        "<#a00f10>pets"+"<grey> give you fortune and sell boosts that scale with "+"<#a00f10>pet"+"<grey> level.",
                        "",
                        "<#a00f10>Commands: /eggs & /pets"

                ))),
                new ItemDetails(Material.PINK_WOOL, textUtils.colorize("Preferences"), textUtils.colorize(List.of(
                        "<#FFC1CC>Command: /preferences",
                        "<#A94064>This command brings up your preferences.",
                        "<#A94064>many togglable setting are located here."
                ))),
                new ItemDetails(Material.IRON_INGOT, textUtils.colorize("Auto Sell"), textUtils.colorize(List.of(
                        "<white>Command: /autosell",
                        "<grey>People With COMET rank or above can use /autosell",
                        "<grey>solar rank or below, you cannot."
                ))),
                new ItemDetails(Material.EMERALD, textUtils.colorize("Auto Prestige"), textUtils.colorize(List.of(
                        "<dark_green>Command: /autoprestige",
                        "<green>There is no rank requirement to use /autoprestige."
                ))),
                new ItemDetails(Material.COBBLESTONE, textUtils.colorize("Auto Miner"), textUtils.colorize(List.of(
                        "<dark_gray>Command: /autominer",
                        "<gray>Players can purchase autominer time and speed",
                        "<gray>via heading to the upgrades tab in the gui",
                        "<gray>the autominer is also activated/deactivated via the gui."
                ))),
                new ItemDetails(Material.EXPERIENCE_BOTTLE, textUtils.colorize("Reclaim"), textUtils.colorize(List.of(
                        "<dark_green>Command: /reclaim",
                        "<yellow>This command allows you to claim rank based rewards",
                        "<yellow>so every season you can claim rewards for your rank via /reclaim."
                ))),
                new ItemDetails(Material.END_GATEWAY, textUtils.colorize("Moons"), textUtils.colorize(List.of(
                        "<#02006C>/moons",
                        "<dark_purple>This command brings brings up the moons GUI.",
                        "<dark_purple>the gui showcases the current moons aswell as their chances."

                ))),
                new ItemDetails(Material.FIRE_CHARGE, textUtils.colorize("Leaderboard"), textUtils.colorize(List.of(
                        "<#740a04>No Command",
                        "<#fb7604>Top 5 players with the most mined blocks get comets",
                        "<#fb7604>comets can be used in the quarry shop to buy many things."
                ))),

                new ItemDetails(Material.IRON_BARS, textUtils.colorize("Cells"), textUtils.colorize(List.of(
                        "<white>Command: /cells",
                        "<#292828>(NOT OUT YET)"
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
