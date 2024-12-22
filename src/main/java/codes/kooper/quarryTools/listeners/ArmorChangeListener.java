package codes.kooper.quarryTools.listeners;

import codes.kooper.quarryTools.QuarryTools;
import codes.kooper.quarryTools.items.ArmorItems;
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class ArmorChangeListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        QuarryTools.getInstance().getArmorItems().removeFullSet(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onArmorChange(PlayerArmorChangeEvent event) {
        Player player = event.getPlayer();

        // Calculate health bonus based on new or old item
        int healthBonus = event.getNewItem().isEmpty()
                ? ArmorItems.getHealthBonus(event.getOldItem())
                : ArmorItems.getHealthBonus(event.getNewItem());

        // Get current absorption level
        int currentAbsorptionLevel = player.hasPotionEffect(PotionEffectType.ABSORPTION)
                ? Objects.requireNonNull(player.getPotionEffect(PotionEffectType.ABSORPTION)).getAmplifier()
                : 0;

        player.removePotionEffect(PotionEffectType.ABSORPTION);

        // Adjust absorption level based on hearts (healthBonus divided by 2 for each heart)
        int newAbsorptionLevel = event.getNewItem().isEmpty()
                ? currentAbsorptionLevel - (healthBonus / 2 / 2) // Convert health points to absorption tiers
                : (healthBonus / 2 / 2) + currentAbsorptionLevel;

        // Prevent negative absorption levels
        if (newAbsorptionLevel <= 0) {
            player.removePotionEffect(PotionEffectType.ABSORPTION);
            return;
        }

        // Apply absorption effect with calculated tiers
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, Integer.MAX_VALUE, newAbsorptionLevel - 1, false, false, false));

        if (!ArmorItems.hasFullArmorSet(player)) return;
        QuarryTools.getInstance().getArmorItems().addFullSet(player.getUniqueId(), QuarryTools.getInstance().getArmorItems().getArmorSet(player.getInventory().getHelmet()));
    }

}
