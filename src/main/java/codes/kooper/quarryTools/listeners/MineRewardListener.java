package codes.kooper.quarryTools.listeners;

import codes.kooper.koopKore.database.models.User;
import codes.kooper.koopKore.utils.Tasks;
import codes.kooper.quarryMoons.enums.MOONS;
import codes.kooper.quarryTools.events.QuarryMineEvent;
import eu.endercentral.crazy_advancements.JSONMessage;
import eu.endercentral.crazy_advancements.NameKey;
import eu.endercentral.crazy_advancements.advancement.Advancement;
import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay;
import eu.endercentral.crazy_advancements.advancement.AdvancementVisibility;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

import static codes.kooper.koopKore.KoopKore.numberUtils;

public class MineRewardListener implements Listener {
    final String[] boosters = {"money", "heart", "skill-token"};

    @EventHandler
    public void onQuarryMine(QuarryMineEvent event) {
        User user = event.getUser();
        if (user.getMined() % 5000 != 0) return;
        Player player = event.getPlayer();

        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 5, 1.5f);
        ItemStack icon = new ItemStack(Material.NETHER_STAR);
        JSONMessage title = new JSONMessage(new TextComponent(numberUtils.commaFormat(user.getMined()) + " Blocks Mined!"));
        JSONMessage description = new JSONMessage(new TextComponent("Reward: Random Booster"));
        AdvancementDisplay advancementDisplay = new AdvancementDisplay(icon, title, description, AdvancementDisplay.AdvancementFrame.TASK, AdvancementVisibility.ALWAYS);
        advancementDisplay.setX(1);
        advancementDisplay.setY(3.5f);
        Advancement rootAdvancement = new Advancement(new NameKey("quarrytools", "blocks"), advancementDisplay);
        String booster = boosters[ThreadLocalRandom.current().nextInt(boosters.length)];
        double multi = ThreadLocalRandom.current().nextDouble(0.25, 1.01);
        Tasks.runSync(() -> {
            rootAdvancement.displayToast(player);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "boostersadmin give " + player.getName() + " " + booster + "-booster personal " + (multi * 100) + " 5m 1");
        });
    }

}
