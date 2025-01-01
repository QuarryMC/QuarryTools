package codes.kooper.quarryTools.listeners.skills;

import codes.kooper.koopKore.database.models.User;
import codes.kooper.koopKore.utils.Tasks;
import codes.kooper.quarrySkills.QuarrySkills;
import codes.kooper.quarrySkills.managers.SkillManager;
import codes.kooper.quarrySkills.models.Skill;
import codes.kooper.quarryTools.events.QuarryMineEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.concurrent.ThreadLocalRandom;

import static codes.kooper.koopKore.KoopKore.textUtils;

public class LootboxFinderSkill implements Listener {

    @EventHandler
    public void onBreak(QuarryMineEvent event) {
        User user = event.getUser();
        Player player = event.getPlayer();
        if (!user.hasSkill("lootbox_finder") || user.hasDisabledSkill("lootbox_finder")) return;

        Skill skill = QuarrySkills.getInstance().getSkillManager().getSkills().get("lootbox_finder");
        if (ThreadLocalRandom.current().nextDouble() > SkillManager.getChance(user, skill)) return;

        double random = ThreadLocalRandom.current().nextDouble();
        String lootbox;
        if (random <= 0.485) {
            lootbox = "money-lootbox";
        } else if (random <= 0.97) {
            lootbox = "token-lootbox";
        } else {
            lootbox = "crazy-lootbox";
        }
        Tasks.runSync(() -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lootbox give " + player.getName() + " " + lootbox));

        if (user.hasDisabledSkillNotification("lootbox_finder")) return;
        player.playSound(player.getLocation(), Sound.ENTITY_LLAMA_CHEST, 5, 1.5f);
        player.sendMessage(textUtils.colorize(skill.color1() + "<bold>LOOT BOX FINDER<reset><dark_gray>: " + skill.color2() + "You received a <white>" + textUtils.capitalize(lootbox).replaceAll("-", " ") + skill.color2() + "."));
    }

}
