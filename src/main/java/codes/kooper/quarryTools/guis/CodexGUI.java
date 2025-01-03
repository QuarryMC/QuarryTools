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
                        "<#292828>/bosses",
                        "<dark_purple>(tbd)"
                ))),
                new ItemDetails(Material.DIAMOND, "Skills", textUtils.colorize(List.of(
                        "<aqua>/skills",
                        "<dark_blue>(tbd)"
                ))),
                new ItemDetails(Material.IRON_BARS, "Cells", textUtils.colorize(List.of(
                        "<white>/cells",
                        "<#292828>(tbd.)"
                ))),
                new ItemDetails(Material.CHEST, "Crates", textUtils.colorize(List.of(
                        "<#684b0a>No Command Yet",
                        "<#af882f>(tbd)"
                ))),
                new ItemDetails(Material.GRAY_STAINED_GLASS_PANE, "???", textUtils.colorize(List.of(
                        "<gray>???",
                        "<green>???"
                ))),
                new ItemDetails(Material.GRAY_STAINED_GLASS_PANE, "???", textUtils.colorize(List.of(
                        "<gray>???",
                        "<green>???"
                ))),
                new ItemDetails(Material.GRAY_STAINED_GLASS_PANE, "???", textUtils.colorize(List.of(
                        "<gray>???",
                        "<green>???"
                ))),
                new ItemDetails(Material.GRAY_STAINED_GLASS_PANE, "???", textUtils.colorize(List.of(
                        "<gray>???",
                        "<green>???"
                ))),
                new ItemDetails(Material.GRAY_STAINED_GLASS_PANE, "???", textUtils.colorize(List.of(
                        "<gray>???",
                        "<green>???"
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
