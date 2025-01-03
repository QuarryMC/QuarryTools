package codes.kooper.quarryTools.guis;

import codes.kooper.shaded.gui.builder.item.ItemBuilder;
import codes.kooper.shaded.gui.guis.Gui;
import codes.kooper.shaded.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

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

        // List of 14 placeholder items
        List<ItemDetails> items = List.of(
                new ItemDetails(Material.PURPLE_WOOL, "Prestige", "/prestige", "Prestiging gives you skill tokens aswell as unlocks better mines when you get enough"),
                new ItemDetails(Material.GOLD_BLOCK, "Rebirth", "/rebirth", "Rebirthing gives you a Rebirth Lootbox, Better luckly blocks, aswell as access to a Rebirth mine and /rebirthshop"),
                new ItemDetails(Material.REDSTONE_BLOCK, "Auto Commands", "/autosell /autoprestige /automine", "(tbd)"),
                new ItemDetails(Material.SAND, "Placeholder 4", "This is item 4.", "Custom description."),
                new ItemDetails(Material.GRAVEL, "Placeholder 5", "This is item 5.", "Custom description."),
                new ItemDetails(Material.OAK_LOG, "Placeholder 6", "This is item 6.", "Custom description."),
                new ItemDetails(Material.SPRUCE_LOG, "Placeholder 7", "This is item 7.", "Custom description."),
                new ItemDetails(Material.BIRCH_LOG, "Placeholder 8", "This is item 8.", "Custom description."),
                new ItemDetails(Material.JUNGLE_LOG, "Placeholder 9", "This is item 9.", "Custom description."),
                new ItemDetails(Material.ACACIA_LOG, "Placeholder 10", "This is item 10.", "Custom description."),
                new ItemDetails(Material.DARK_OAK_LOG, "Placeholder 11", "This is item 11.", "Custom description."),
                new ItemDetails(Material.CLAY, "Placeholder 12", "This is item 12.", "Custom description."),
                new ItemDetails(Material.SNOW_BLOCK, "Placeholder 13", "This is item 13.", "Custom description."),
                new ItemDetails(Material.ICE, "Placeholder 14", "This is item 14.", "Custom description.")
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
                        .lore(
                                Component.text(item.lore1),
                                Component.text(item.lore2)
                        )
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
        String lore1;
        String lore2;

        public ItemDetails(Material material, String name, String lore1, String lore2) {
            this.material = material;
            this.name = name;
            this.lore1 = lore1;
            this.lore2 = lore2;
        }
    }
}
