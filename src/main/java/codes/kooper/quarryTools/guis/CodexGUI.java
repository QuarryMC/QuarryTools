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

        gui.getFiller().fill(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE)
                .name(Component.empty())
                .asGuiItem());

        int startRow = 2;
        int endRow = 3;
        int startCol = 2;
        int endCol = 8;

        List<ItemDetails> items = List.of(
                new ItemDetails(Material.PURPLE_WOOL, "Prestige", textUtils.colorize(List.of(
                        "<dark_purple>/prestige",
                        "<light_purple>Prestiging gives you skill tokens,",
                        "<light_purple>As well as unlocks better mines when you get enough"
                ))),
                new ItemDetails(Material.GOLD_BLOCK, "Rebirth", textUtils.colorize(List.of(
                        "<yellow>/rebirth",
                        "<#dcc100>Rebirthing gives you a Rebirth Lootbox,",
                        "<#dcc100>Better lucky blocks,",
                        "<#dcc100>As well as access to a Rebirth mine and /rebirthshop"
                ))),
                new ItemDetails(Material.IRON_INGOT, "Auto Sell", textUtils.colorize(List.of(
                        "<white>/autosell",
                        "<grey>(tbd)"
                ))),
                new ItemDetails(Material.EMERALD, "Auto Prestige", textUtils.colorize(List.of(
                        "<dark_green>/automine",
                        "<green>(tbd)"
                ))),
                new ItemDetails(Material.COBBLESTONE, "Auto Miner", textUtils.colorize(List.of(
                        "<dark_gray>/automine",
                        "<gray>(tbd)"
                ))),
                new ItemDetails(Material.ENDER_DRAGON_SPAWN_EGG, "Bosses", textUtils.colorize(List.of(
                        "<black>/bosses",
                        "<dark_purple>(tbd)"
                ))),
                new ItemDetails(Material.DIAMOND, "Skills", textUtils.colorize(List.of(
                        "<aqua>/skills",
                        "<dark_blue>(tbd)"
                ))),
                new ItemDetails(Material.BIRCH_LOG, "Placeholder 8", textUtils.colorize(List.of(
                        "<gray>This is item 8.",
                        "<green>Custom description."
                ))),
                new ItemDetails(Material.JUNGLE_LOG, "Placeholder 9", textUtils.colorize(List.of(
                        "<gray>This is item 9.",
                        "<green>Custom description."
                ))),
                new ItemDetails(Material.ACACIA_LOG, "Placeholder 10", textUtils.colorize(List.of(
                        "<gray>This is item 10.",
                        "<green>Custom description."
                ))),
                new ItemDetails(Material.DARK_OAK_LOG, "Placeholder 11", textUtils.colorize(List.of(
                        "<gray>This is item 11.",
                        "<green>Custom description."
                ))),
                new ItemDetails(Material.CLAY, "Placeholder 12", textUtils.colorize(List.of(
                        "<gray>This is item 12.",
                        "<green>Custom description."
                ))),
                new ItemDetails(Material.SNOW_BLOCK, "Placeholder 13", textUtils.colorize(List.of(
                        "<gray>This is item 13.",
                        "<green>Custom description."
                ))),
                new ItemDetails(Material.ICE, "Placeholder 14", textUtils.colorize(List.of(
                        "<gray>This is item 14.",
                        "<green>Custom description."
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
                        .name(Component.text(item.name))
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
        String name;
        List<Component> lore;

        public ItemDetails(Material material, String name, List<Component> lore) {
            this.material = material;
            this.name = name;
            this.lore = lore;
        }
    }
}
