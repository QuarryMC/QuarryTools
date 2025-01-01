package codes.kooper.quarryTools.guis;

import codes.kooper.quarryTools.QuarryTools;
import codes.kooper.quarryTools.database.models.PickaxeStorage;
import codes.kooper.quarryTools.enums.RARITIES;
import codes.kooper.quarryTools.items.PickaxeItems;
import codes.kooper.shaded.gui.builder.item.ItemBuilder;
import codes.kooper.shaded.gui.guis.Gui;
import codes.kooper.shaded.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static codes.kooper.koopKore.KoopKore.textUtils;

public class PickaxeSkinsGUI {

    public PickaxeSkinsGUI(Player player, RARITIES rarity) {
        Optional<PickaxeStorage> optionalPickaxeStorage = QuarryTools.getInstance().getPickStorageCache().get(player.getUniqueId());
        if (optionalPickaxeStorage.isEmpty()) return;
        PickaxeStorage pickaxeStorage = optionalPickaxeStorage.get();

        Gui gui = Gui.gui()
                .title(Component.text("Pickaxe Skins"))
                .disableAllInteractions()
                .rows(6)
                .create();

        gui.getFiller().fillBorder(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).name(Component.empty()).asGuiItem());
        int slot = 1;
        for (RARITIES rarities : RARITIES.values()) {
            GuiItem raritySelector = ItemBuilder.from(new codes.kooper.koopKore.item.ItemBuilder(Material.PLAYER_HEAD).setTexture(rarities.getTexture()).build())
                    .name(textUtils.colorize(rarities.getColor() + "<bold>" + textUtils.capitalize(rarities.name()).toUpperCase()))
                    .lore(textUtils.colorize(List.of(
                            "<gray>Click to view pickaxes",
                            "<gray>in this rarity."
                    )))
                    .asGuiItem();
            raritySelector.setAction((action) -> new PickaxeSkinsGUI(player, rarities));
            gui.setItem(slot, raritySelector);
            slot++;
        }

        for (PickaxeItems.Pickaxe pickaxe : QuarryTools.getInstance().getPickaxeItems().getPickaxes(rarity).stream().sorted(Comparator.comparingInt(PickaxeItems.Pickaxe::fortune)).collect(Collectors.toCollection(LinkedHashSet::new))) {
            GuiItem pickaxeItem;
            if (pickaxeStorage.hasPickaxe(pickaxe.name())) {
                ItemStack pickItem = pickaxeStorage.getPickaxe(pickaxe.name()).toItem(player).clone();
                List<Component> lore = pickItem.lore();
                if (lore == null) continue;
                lore.add(Component.empty());
                if (pickaxe.name().equals(pickaxeStorage.getSelected().getName())) {
                    lore.add(textUtils.success("Currently Equipped"));
                } else {
                    lore.add(textUtils.success("Click to Equip"));
                }
                pickaxeItem = ItemBuilder.from(pickItem).lore(lore).glow(pickaxe.name().equals(pickaxeStorage.getSelected().getName())).asGuiItem();
                pickaxeItem.setAction((action) -> {
                    if (pickaxe.name().equals(pickaxeStorage.getSelected().getName())) return;
                    pickaxeStorage.updateSelected(player.getInventory().getItemInMainHand());
                    pickaxeStorage.getPickaxes().put(pickaxeStorage.getSelected().getName(), pickaxeStorage.getSelected());
                    pickaxeStorage.setSelected(pickaxeStorage.getPickaxe(pickaxe.name()));
                    player.getInventory().setItemInMainHand(pickaxeStorage.getSelected().toItem(player).clone());
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 5, 1.5f);
                    new PickaxeSkinsGUI(player, rarity);
                });
            } else {
                List<Component> lore = pickaxe.itemStack().clone().lore();
                if (lore == null) continue;
                lore.add(Component.empty());
                lore.add(textUtils.error("Not Unlocked"));
                pickaxeItem = ItemBuilder.from(pickaxe.itemStack().clone()).lore(lore).asGuiItem();
            }
            gui.addItem(pickaxeItem);
        }

        gui.setItem(49, new GuiItem(player.getInventory().getItemInMainHand()));

        gui.open(player);
    }

}
