package codes.kooper.quarryTools.database.listeners;

import codes.kooper.koopKore.utils.Tasks;
import codes.kooper.quarryTools.QuarryTools;
import codes.kooper.quarryTools.database.models.Pickaxe;
import codes.kooper.quarryTools.database.models.PickaxeStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PickaxeLoadListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = event.getPlayer().getUniqueId();

        if (QuarryTools.getInstance().getPickStorageCache().containsKey(uuid)) {
            return;
        }

        Tasks.runAsync(() -> {
            Pickaxe defaultPick = new Pickaxe(QuarryTools.getInstance().getPickaxeItems().getPickaxe("starter"));
            PickaxeStorage pickaxeStorage = QuarryTools.getInstance().getPickaxeService().findById(uuid).orElseGet(() -> new PickaxeStorage(uuid, defaultPick));
            QuarryTools.getInstance().getPickStorageCache().put(uuid, pickaxeStorage);

            Tasks.runSync(() -> {
                ItemStack item = player.getInventory().getItem(0);
                if (item == null || item.isEmpty()) {
                    player.getInventory().setItem(0, pickaxeStorage.getSelected().toItem(player).clone());
                }
            });
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        QuarryTools.getInstance().getPickStorageCache().get(uuid).ifPresent(storage -> {
            Tasks.runAsync(() -> QuarryTools.getInstance().getPickaxeService().savePickStorage(storage));
            QuarryTools.getInstance().getPickStorageCache().invalidate(uuid);
        });
    }

}
