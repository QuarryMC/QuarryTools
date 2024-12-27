package codes.kooper.quarryTools.database.listeners;

import codes.kooper.koopKore.utils.Tasks;
import codes.kooper.quarryTools.QuarryTools;
import codes.kooper.quarryTools.database.models.Pickaxe;
import codes.kooper.quarryTools.database.models.PickaxeStorage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PickaxeLoadListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        if (QuarryTools.getInstance().getPickStorageCache().containsKey(uuid)) {
            return;
        }

        Tasks.runAsync(() -> {
            Pickaxe defaultPick = new Pickaxe(QuarryTools.getInstance().getPickaxeItems().getPickaxe("starter"));
            PickaxeStorage pickaxeStorage = QuarryTools.getInstance().getPickaxeService().findById(uuid).orElseGet(() -> new PickaxeStorage(uuid, defaultPick));
            QuarryTools.getInstance().getPickStorageCache().put(uuid, pickaxeStorage);
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
