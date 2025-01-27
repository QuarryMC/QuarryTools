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
import static codes.kooper.koopKore.KoopKore.textUtils;

public class MineRewardListener implements Listener {
    final String[] boosters = {"money", "heart", "skill-token"};

    @EventHandler
    public void onQuarryMine(QuarryMineEvent event) {
        User user = event.getUser();
        Player player = event.getPlayer();

        if (user.getMined() == 100) {
            player.playSound(player.getLocation(), Sound.ENTITY_LLAMA_CHEST, 5, 1.25f);
            Tasks.runSync(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "crazycrates give p pickaxe 1 " + player.getName()));
            player.sendMessage(textUtils.success("Nice job mining 100 blocks! You've received a pickaxe key. Use this at /crates. You can unlock better pickaxes from this crate. Right-click your pickaxe to apply these new pickaxes. These pickaxes give better fortune boosts."));
        } else if (user.getMined() == 1000) {
            player.sendMessage(textUtils.success("Nice job mining 1,000 blocks! You've received a stone egg. Right-click it to start hatching."));
            player.playSound(player.getLocation(), Sound.ENTITY_TURTLE_EGG_HATCH, 5, 1.25f);
            Tasks.runSync(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eggs give " + player.getName() + " stone 1"));
        } else if (user.getMined() == 15000) {
            player.sendMessage(textUtils.success("Nice job mining 15,000 blocks! You've received an iron egg. Right-click it to start hatching."));
            player.playSound(player.getLocation(), Sound.ENTITY_TURTLE_EGG_HATCH, 5, 1.25f);
            Tasks.runSync(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eggs give " + player.getName() + " iron 1"));
        } else if (user.getMined() % 1000 == 0) {
            String[] words = {"Marvelous", "Spectacular", "Wow", "Amazing", "Good Work"};
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 5, 1.25f);
            player.sendMessage(textUtils.success(words[ThreadLocalRandom.current().nextInt(words.length)] + "! You've mined 1,000 blocks. You've received an extreme key."));
            Tasks.runSync(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "crazycrates give p extreme 1 " + player.getName()));
        }

        if (user.getMined() % 5000 != 0) return;

        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 5, 1.5f);
        ItemStack icon = new ItemStack(Material.NETHER_STAR);
        JSONMessage title = new JSONMessage(new TextComponent(numberUtils.commaFormat(user.getMined()) + " Blocks Mined!"));
        JSONMessage description = new JSONMessage(new TextComponent("Reward: Random Booster"));
        AdvancementDisplay advancementDisplay = new AdvancementDisplay(icon, title, description, AdvancementDisplay.AdvancementFrame.TASK, AdvancementVisibility.ALWAYS);
        advancementDisplay.setX(1);
        advancementDisplay.setY(10f);
        Advancement rootAdvancement = new Advancement(new NameKey("quarrytools", "blocks"), advancementDisplay);
        String booster = boosters[ThreadLocalRandom.current().nextInt(boosters.length)];
        double multi = ThreadLocalRandom.current().nextDouble(0.25, 1.01);

        Tasks.runSync(() -> {
            rootAdvancement.displayToast(player);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "boostersadmin give " + player.getName() + " " + booster + "-booster personal " + (multi * 100) + " 5m 1");
        });
    }

}
