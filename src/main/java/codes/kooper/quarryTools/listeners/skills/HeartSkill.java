package codes.kooper.quarryTools.listeners.skills;

import codes.kooper.koopKore.database.models.User;
import codes.kooper.quarrySkills.QuarrySkills;
import codes.kooper.quarrySkills.managers.SkillManager;
import codes.kooper.quarrySkills.models.Skill;
import codes.kooper.quarryTools.events.QuarryMineEvent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.concurrent.ThreadLocalRandom;

import static codes.kooper.koopKore.KoopKore.textUtils;

public class HeartSkill implements Listener {

    @EventHandler
    public void onBreak(QuarryMineEvent event) {
        User user = event.getUser();
        Player player = event.getPlayer();
        if (!user.hasSkill("health_finder") || user.hasDisabledSkill("health_finder")) return;
        Skill skill = QuarrySkills.getInstance().getSkillManager().getSkills().get("health_finder");
        if (ThreadLocalRandom.current().nextDouble() > SkillManager.getChance(user, skill)) return;
        int hearts = ThreadLocalRandom.current().nextInt(1, 11);
        if (user.getHealth() + hearts > event.getUser().getHealthCap()) {
            hearts = (int) (event.getUser().getHealthCap() - user.getHealth());
            if (hearts <= 0) return;
        }
        user.addHealth(hearts);
        if (user.hasDisabledSkillNotification("health_finder")) return;
        player.sendMessage(textUtils.colorize(
                skill.color2() + "Your " + skill.color1() + "<bold>HEALTH FINDER SKILL<reset>" + skill.color2() + " has granted you <red><bold>+" + hearts + "❤<reset>" + skill.color1() + "."
        ));
        player.playSound(player.getLocation(), Sound.ENTITY_WANDERING_TRADER_DRINK_POTION, 5,1);
    }

}
