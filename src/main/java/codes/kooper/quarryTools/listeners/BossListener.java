package codes.kooper.quarryTools.listeners;

import codes.kooper.koopKore.database.models.User;
import codes.kooper.quarryMines.QuarryMines;
import codes.kooper.quarryMines.database.models.Quarry;
import codes.kooper.quarrySkills.QuarrySkills;
import codes.kooper.quarrySkills.managers.SkillManager;
import codes.kooper.quarrySkills.models.Skill;
import codes.kooper.quarryTools.events.QuarryMineEvent;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static codes.kooper.koopKore.KoopKore.numberUtils;
import static codes.kooper.koopKore.KoopKore.textUtils;

public class BossListener implements Listener {
    private final Map<UUID, Integer> healthGainSummary;
    private final Map<UUID, Integer> healthLossSummary;

    public BossListener() {
        healthGainSummary = new HashMap<>();
        healthLossSummary = new HashMap<>();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        healthGainSummary.remove(player.getUniqueId());
        healthLossSummary.remove(player.getUniqueId());
    }

    @EventHandler
    public void onSummary(QuarryMineEvent event) {
        Player player = event.getPlayer();
        if (event.getUser().getMined() % 1000 != 0) return;
        Quarry quarry = event.getQuarry();
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 3, 1.5f);
        String prefix = quarry.getSpawnedBoss().getBoss().color2() + "<bold>" + quarry.getSpawnedBoss().getBoss().name().toUpperCase() + " BOSS: ";
        player.sendMessage(textUtils.colorize(prefix +"<newline><reset><dark_gray> ┗ <red>Haha! You healed me by ❤" + numberUtils.commaFormat(healthGainSummary.getOrDefault(player.getUniqueId(), 0)) + ".<newline><reset><dark_gray> ┗ <green>Ouch! You damaged me by <red>❤" + numberUtils.commaFormat(healthLossSummary.getOrDefault(player.getUniqueId(), 0)) + "<green>.<newline><reset><dark_gray> ┗ " + quarry.getSpawnedBoss().getBoss().color2() + "I now have a health of <red>❤" + numberUtils.commaFormat(quarry.getSpawnedBoss().getHealth()) + quarry.getSpawnedBoss().getBoss().color2() + "."));
        healthGainSummary.put(player.getUniqueId(), 0);
        healthLossSummary.put(player.getUniqueId(), 0);
    }

    @EventHandler
    public void onMineOfGood(QuarryMineEvent event) {
        Quarry quarry = event.getQuarry();
        Player player = event.getPlayer();
        User user = event.getUser();
        if (quarry.getSpawnedBoss() == null || event.getBlockData().getMaterial() != quarry.getSpawnedBoss().getBoss().bossBlock()) return;
        int health = ThreadLocalRandom.current().nextInt(10, 31);
        Skill skill = QuarrySkills.getInstance().getSkillManager().getSkills().get("sworder");
        if (user.hasSkill("sworder") && !user.hasDisabledSkill("sworder") && ThreadLocalRandom.current().nextDouble() <= SkillManager.getChance(user, skill)) {
            health *= 2;
            if (user.hasDisabledSkillNotification("sworder")) return;
            player.sendMessage(textUtils.colorize(skill.color1() + "<bold>SWORDER<reset><dark_gray>:" + skill.color2() + "You hit the boss for 2x damage!"));
            player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_DAMAGE, 5, 1.5f);
        } else {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 5, 1.5f);
        }
        quarry.getSpawnedBoss().removeHealth(health);
        QuarryMines.getInstance().getBossManager().damageBossEffect(quarry);
        if (quarry.getSpawnedBoss().isDead()) {
            QuarryMines.getInstance().getBossManager().killBoss(quarry, player);
        }
        player.spawnParticle(Particle.SMALL_GUST, event.getPosition().toLocation(player.getWorld()).add(new Vector(0.5, 0.5, 0.5)), 5, 0.2, 0.2, 0.2, 0);
        healthLossSummary.put(player.getUniqueId(), healthLossSummary.getOrDefault(player.getUniqueId(), 0) + health);
    }

    @EventHandler
    public void onMineOfBad(QuarryMineEvent event) {
        Quarry quarry = event.getQuarry();
        Player player = event.getPlayer();
        if (quarry.getSpawnedBoss() == null || event.getBlockData().getMaterial() != Material.NETHERRACK) return;
        int health = ThreadLocalRandom.current().nextInt(10, 31);
        quarry.getSpawnedBoss().addHealth(health);
        player.spawnParticle(Particle.ANGRY_VILLAGER, event.getPosition().toLocation(player.getWorld()).add(new Vector(0.5, 0.5, 0.5)), 5, 0.2, 0.2, 0.2, 0);
        player.playSound(player.getLocation(), Sound.ENTITY_BEE_HURT, 5, 0.7f);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 5, 0.7f);
        healthGainSummary.put(player.getUniqueId(), healthGainSummary.getOrDefault(player.getUniqueId(), 0) + health);
    }

}
