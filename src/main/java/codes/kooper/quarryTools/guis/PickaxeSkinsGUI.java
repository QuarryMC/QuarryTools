package codes.kooper.quarryTools.guis;

import codes.kooper.quarryTools.QuarryTools;
import codes.kooper.quarryTools.database.models.Pickaxe;
import codes.kooper.quarryTools.database.models.PickaxeStorage;
import codes.kooper.quarryTools.enums.RARITIES;
import codes.kooper.quarryTools.items.PickaxeItems;
import codes.kooper.shaded.gui.builder.item.ItemBuilder;
import codes.kooper.shaded.gui.guis.Gui;
import codes.kooper.shaded.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

import static codes.kooper.koopKore.KoopKore.textUtils;

public class PickaxeSkinsGUI {

    public PickaxeSkinsGUI(Player player, RARITIES rarity) {
        Optional<PickaxeStorage> optionalPickaxeStorage = QuarryTools.getInstance().getPickStorageCache().get(player.getUniqueId());
        if (optionalPickaxeStorage.isEmpty()) return;
        PickaxeStorage pickaxeStorage = optionalPickaxeStorage.get();

        pickaxeStorage.updateSelected(player.getInventory().getItem(0));

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
                pickaxeItem = ItemBuilder.from(pickItem).lore(lore).glow(pickaxe.name().equals(pickaxeStorage.getSelected().getName())).flags(ItemFlag.values()).asGuiItem();
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
                pickaxeItem = ItemBuilder.from(pickaxe.itemStack().clone()).lore(lore).flags(ItemFlag.values()).asGuiItem();
            }

            gui.addItem(pickaxeItem);
        }

        GuiItem equipBestItem = ItemBuilder.from(Material.WOODEN_PICKAXE).name(textUtils.colorize("<green><bold>Equip Best")).lore(textUtils.colorize("<white>Click to equip the best pickaxe")).asGuiItem();
        equipBestItem.setAction((event) -> pickaxeStorage.getPickaxes().values().stream().max(Comparator.comparingInt(Pickaxe::getFortune)).ifPresent(bestPickaxe -> {
            if (bestPickaxe.getName().equals(pickaxeStorage.getSelected().getName())) return;
            pickaxeStorage.updateSelected(player.getInventory().getItemInMainHand());
            pickaxeStorage.getPickaxes().put(pickaxeStorage.getSelected().getName(), pickaxeStorage.getSelected());
            pickaxeStorage.setSelected(pickaxeStorage.getPickaxe(bestPickaxe.getName()));
            player.getInventory().setItemInMainHand(pickaxeStorage.getSelected().toItem(player).clone());
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 5, 1.5f);
            new PickaxeSkinsGUI(player, rarity);
        }));
        gui.setItem(48, equipBestItem);

        GuiItem skillMenu = ItemBuilder.from(Material.EMERALD_BLOCK).name(textUtils.colorize("<aqua><bold>Skills")).lore(textUtils.colorize(List.of(
                "<gray>Click to open the skills menu.",
                "<gray>Skills boost your player and are permanent."
        ))).asGuiItem();
        skillMenu.setAction((event) -> player.performCommand("skills"));
        gui.setItem(49, skillMenu);

        gui.setItem(50, new GuiItem(player.getInventory().getItemInMainHand()));

        gui.open(player);
    }

}
