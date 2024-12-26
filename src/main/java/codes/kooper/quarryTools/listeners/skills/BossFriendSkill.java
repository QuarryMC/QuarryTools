package codes.kooper.quarryTools.listeners.skills;

import codes.kooper.koopKore.database.models.User;
import codes.kooper.quarryMines.QuarryMines;
import codes.kooper.quarryMines.managers.BossManager;
import codes.kooper.quarrySkills.QuarrySkills;
import codes.kooper.quarrySkills.managers.SkillManager;
import codes.kooper.quarrySkills.models.Skill;
import codes.kooper.quarryTools.events.QuarryMineEvent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

import static codes.kooper.koopKore.KoopKore.textUtils;

public class BossFriendSkill implements Listener {

    @EventHandler
    public void onMine(QuarryMineEvent event) {
        User user = event.getUser();
        Player player = event.getPlayer();
        if (!user.hasSkill("boss_friend") || user.hasDisabledSkill("boss_friend")) return;

        Skill skill = QuarrySkills.getInstance().getSkillManager().getSkills().get("boss_friend");
        if (ThreadLocalRandom.current().nextDouble() > SkillManager.getChance(user, skill)) return;

        int random = ThreadLocalRandom.current().nextInt(1, 101);
        ItemStack bossBlock;
        if (random <= 50) { // 50% chance
            bossBlock = BossManager.getSpawnBlock(QuarryMines.getInstance().getBossManager().getBosses().get("shorse"));
        } else if (random <= 70) { // 20% chance
            bossBlock = BossManager.getSpawnBlock(QuarryMines.getInstance().getBossManager().getBosses().get("zoglin"));
        } else if (random <= 85) { // 15%  chance
            bossBlock = BossManager.getSpawnBlock(QuarryMines.getInstance().getBossManager().getBosses().get("polarbear"));
        } else if (random <= 95) { // 10% chance
            bossBlock = BossManager.getSpawnBlock(QuarryMines.getInstance().getBossManager().getBosses().get("irongolem"));
        } else { // 5% chance
            bossBlock = BossManager.getSpawnBlock(QuarryMines.getInstance().getBossManager().getBosses().get("phantom"));
        }

        player.getInventory().addItem(bossBlock);
        if (user.hasDisabledSkillNotification("boss_friend")) return;
        player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_4, 5, 1.5f);
        player.sendMessage(textUtils.colorize(skill.color1() + "<bold>BOSS FRIEND<reset><dark_gray>: " + skill.color2() + "You received a ").append(bossBlock.displayName()) + skill.color2() + ".");
    }

}
