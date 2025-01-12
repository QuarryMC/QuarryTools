package codes.kooper.quarryTools.listeners;

import codes.kooper.koopKore.KoopKore;
import codes.kooper.koopKore.database.models.User;
import codes.kooper.quarryMines.QuarryMines;
import codes.kooper.quarryMines.database.models.Quarry;
import codes.kooper.quarryTools.QuarryTools;
import codes.kooper.quarryTools.events.QuarryMineEvent;
import codes.kooper.quarryTools.items.PickaxeItems;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.util.Vector;

import java.util.Optional;

import static codes.kooper.koopKore.KoopKore.textUtils;

public class MiningListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.isClientSided()) return;
        Player player = event.getPlayer();

        if (QuarryMines.getInstance().getApi().getWatching(player).isPresent()) {
            player.sendMessage(textUtils.error("You can't mine while spectating!"));
            showError(player, event.getBlock().getLocation());
            event.setCancelled(true);
            return;
        }

        if (!PickaxeItems.isPickaxe(player.getInventory().getItemInMainHand())) {
            player.sendMessage(textUtils.error("You must use a pickaxe to mine!"));
            showError(player, event.getBlock().getLocation());
            event.setCancelled(true);
            return;
        }

        PickaxeItems.Pickaxe pickaxe = QuarryTools.getInstance().getPickaxeItems().getPickaxe(player.getInventory().getItemInMainHand());

        Optional<User> userOptional = KoopKore.getInstance().getUserAPI().getUser(player.getUniqueId());
        if (userOptional.isEmpty()) {
            event.setCancelled(true);
            return;
        }
        User user = userOptional.get();

        if (!user.hasQuarry()) {
            event.setCancelled(true);
            return;
        }

        Optional<Quarry> quarryOptional = QuarryMines.getInstance().getApi().getQuarry(user.getQuarry());
        if (quarryOptional.isEmpty()) {
            event.setCancelled(true);
            return;
        }
        Quarry quarry = quarryOptional.get();

        user.addMined(1);
        user.addDailyMined(1);
        QuarryTools.getInstance().getMineResetManager().mineBlocks(quarry, 1);

        QuarryTools.getInstance().getMiningThreads().submit(() -> {
            try {
                new QuarryMineEvent(
                        quarry,
                        user,
                        event.getBlock().getLocation(),
                        player,
                        event.getBlockData(),
                        pickaxe
                ).callEvent();
            } catch (Exception e) {
                QuarryTools.getInstance().getLogger().severe("Error while " + player.getName() + " was mining: " + e.getMessage());
            }
        });
    }

    private void showError(Player player, Location location) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 5, 0.5f);
        player.spawnParticle(Particle.ANGRY_VILLAGER, location.add(new Vector(0.5, 0.5, 0.5)),  5, 0,0 ,0 ,0);
    }
}