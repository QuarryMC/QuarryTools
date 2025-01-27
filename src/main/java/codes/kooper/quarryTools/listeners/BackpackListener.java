package codes.kooper.quarryTools.listeners;

import codes.kooper.koopKore.utils.CacheEntry;
import codes.kooper.quarryEconomy.utils.BackpackUtils;
import codes.kooper.quarryPets.QuarryPets;
import codes.kooper.quarryPets.database.models.Pet;
import codes.kooper.quarryTools.events.QuarryMineEvent;
import codes.kooper.quarryTools.items.PickaxeItems;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static codes.kooper.koopKore.KoopKore.textUtils;

public class BackpackListener implements Listener {
    private final Map<UUID, CacheEntry<Double>> fortuneCache;
    private static final long EXPIRATION_TIME_MS = 60_000;

    public BackpackListener() {
        fortuneCache = new HashMap<>();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        fortuneCache.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(QuarryMineEvent event) {
        Player player = event.getPlayer();
        if (event.getUser().getBackpackItems() + event.getBlocks().get() >= event.getUser().getBackpackCapacity2()) {
            if (event.getUser().isAutoSell()) {
                BackpackUtils.sell(event.getUser());
                return;
            }
            event.getUser().setBackpackItems(event.getUser().getBackpackCapacity2());
            if (event.getUser().hasOption("backpack_full_notification")) return;
            player.sendTitlePart(TitlePart.TITLE, textUtils.colorize("<#ea4f36><bold>BACKPACK FULL"));
            player.sendTitlePart(TitlePart.SUBTITLE, textUtils.colorize("<#9babb2><italic>(( USE /SELL ))"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 5, 0.5f);
            return;
        }

        if (fortuneCache.get(player.getUniqueId()) == null || fortuneCache.get(player.getUniqueId()).isExpired(EXPIRATION_TIME_MS)) {
            double megaFortune = 1;
            if (event.getUser().hasSkill("mega_fortune") && !event.getUser().hasDisabledSkill("mega_fortune")) {
                long megaFortuneLevel = event.getUser().getSkillLevel("mega_fortune");
                megaFortune = (megaFortuneLevel / 3.0) + 1;
            }
            double petBoost = 1.0;
            for (Pet pet : QuarryPets.getInstance().getPetManager().getSelectedPets(player)) {
                petBoost += pet.getFortuneBoost();
            }
            double fortuneBoost = PickaxeItems.getFortuneBoost(player.getInventory().getItemInMainHand()) + 1;
            int fortune = PickaxeItems.getFortune(player.getInventory().getItemInMainHand());
            final double finalFortune = fortuneBoost * fortune * megaFortune * petBoost;
            fortuneCache.put(player.getUniqueId(), new CacheEntry<>(finalFortune, System.currentTimeMillis()));
        }

        event.getUser().addBlocksToBackpack((long) (event.getBlocks().get() * fortuneCache.get(player.getUniqueId()).getValue()));
    }
}
