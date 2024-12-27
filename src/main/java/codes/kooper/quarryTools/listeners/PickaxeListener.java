package codes.kooper.quarryTools.listeners;

import codes.kooper.koopKore.database.events.UserLoadEvent;
import codes.kooper.quarryTools.QuarryTools;
import codes.kooper.quarryTools.guis.SkinGUI;
import codes.kooper.quarryTools.items.PickaxeItems;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class PickaxeListener implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (!PickaxeItems.isPickaxe(event.getItem()) || (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)) return;
        PickaxeItems.Pickaxe pickaxe = QuarryTools.getInstance().getPickaxeItems().getPickaxe(event.getItem());
        new SkinGUI(event.getPlayer(), pickaxe.rarity());
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        event.setCancelled(PickaxeItems.isPickaxe(event.getCurrentItem()));
    }

    @EventHandler
    public void inventoryMove(InventoryMoveItemEvent event) {
        event.setCancelled(PickaxeItems.isPickaxe(event.getItem()));
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        event.setCancelled(PickaxeItems.isPickaxe(event.getItemDrop().getItemStack()));
    }

    @EventHandler
    public void onOffhand(PlayerSwapHandItemsEvent event) {
        event.setCancelled(PickaxeItems.isPickaxe(event.getMainHandItem()));
    }

    @EventHandler
    public void onJoin(UserLoadEvent event) {
        if (event.getUser().getSelectedPickaxe() != null) return;
        event.getUser().setSelectedPickaxe("starter");
        Player player = Bukkit.getPlayer(event.getUser().getUuid());
        if (player == null) return;
        ItemStack itemStack = QuarryTools.getInstance().getPickaxeItems().getPickaxe("starter").itemStack().clone();
        event.getUser().addPickaxe("starter", itemStack);
        player.getInventory().setItem(0, itemStack);
    }

}
