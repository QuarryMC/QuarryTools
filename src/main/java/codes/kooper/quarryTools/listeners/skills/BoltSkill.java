package codes.kooper.quarryTools.listeners.skills;

import codes.kooper.koopKore.KoopKore;
import codes.kooper.koopKore.database.models.User;
import codes.kooper.quarryTools.items.PickaxeItems;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Optional;

public class BoltSkill implements Listener {

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        Optional<User> userOptional = KoopKore.getInstance().getUserAPI().getUser(player.getUniqueId());
        if (userOptional.isEmpty()) return;
        User user = userOptional.get();

        if (!user.hasSkill("bolt") || user.hasDisabledSkill("bolt")) return;

        ItemStack item = player.getInventory().getItem(event.getNewSlot());

        if (PickaxeItems.isPickaxe(item)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, Integer.MAX_VALUE, 4, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 4, false, false));
        } else {
            player.removePotionEffect(PotionEffectType.HASTE);
            player.removePotionEffect(PotionEffectType.SPEED);
        }
    }
}
