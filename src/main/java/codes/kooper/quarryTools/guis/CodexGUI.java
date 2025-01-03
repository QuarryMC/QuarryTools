package codes.kooper.quarryTools.guis;

import codes.kooper.shaded.gui.builder.item.ItemBuilder;
import codes.kooper.shaded.gui.guis.Gui;
import codes.kooper.shaded.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CodexGUI {

    public CodexGUI(Player player) {
        Gui gui = Gui.gui()
                .title(Component.text("Codex"))
                .disableAllInteractions()
                .rows(7)
                .create();

        gui.getFiller().fill(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE)
                .name(Component.empty())
                .asGuiItem());

        int startRow = 3;
        int endRow = 4;
        int startCol = 3;
        int endCol = 8;

        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                int slot = (row - 1) * 9 + (col - 1);
                GuiItem sunflowerItem = ItemBuilder.from(Material.SUNFLOWER)
                        .name(Component.text("Beautiful Sunflower"))
                        .lore(
                                Component.text("This is a lovely sunflower."),
                                Component.text("Perfect for your codex!")
                        )
                        .asGuiItem();
                gui.setItem(slot, sunflowerItem);
            }
        }

        gui.open(player);
    }
}
