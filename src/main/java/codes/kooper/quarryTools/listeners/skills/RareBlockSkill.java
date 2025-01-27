package codes.kooper.quarryTools.listeners.skills;

import codes.kooper.ChromaBlockManager;
import codes.kooper.koopKore.database.models.User;
import codes.kooper.models.View;
import codes.kooper.quarrySkills.QuarrySkills;
import codes.kooper.quarrySkills.managers.SkillManager;
import codes.kooper.quarrySkills.models.Skill;
import codes.kooper.quarryTools.events.QuarryMineEvent;
import io.papermc.paper.math.Position;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static codes.kooper.koopKore.KoopKore.numberUtils;
import static codes.kooper.koopKore.KoopKore.textUtils;

public class RareBlockSkill implements Listener {

    @EventHandler
    public void onBreak(QuarryMineEvent event) {
        User user = event.getUser();
        Player player = event.getPlayer();
        if (event.getBlockData().getMaterial() == Material.PRISMARINE) {
            handleAncientDebrisRewards(player, user);
            return;
        }
        if (!user.hasSkill("rare_block") || user.hasDisabledSkill("rare_block")) return;
        Skill skill = QuarrySkills.getInstance().getSkillManager().getSkills().get("rare_block");
        if (ThreadLocalRandom.current().nextDouble() > SkillManager.getChance(user, skill)) return;
        Set<Position> blockifyPositions = getRarePositions(event.getPlayer(), event.getLocation());
        ChromaBlockManager blockManager = player.getChromaBlockManager(player.getWorld());
        blockManager.setBlocks(blockifyPositions, Material.PRISMARINE.createBlockData());
        blockManager.refreshBlocks(blockifyPositions);
        blockifyPositions.forEach(pos -> player.spawnParticle(Particle.SOUL, pos.toLocation(player.getWorld()).add(new Vector(0.5, 0.5, 0.5)), 5, 0.2, 0.2, 0.2, 0));
        if (user.hasDisabledSkillNotification("rare_block")) return;
        player.playSound(player.getLocation(), Sound.BLOCK_BEEHIVE_ENTER, 5, 1);
    }

    private void handleAncientDebrisRewards(Player player, User user) {
        if (ThreadLocalRandom.current().nextDouble() > 0.3) return;
        if (ThreadLocalRandom.current().nextDouble() <= 0.4) {
            if (ThreadLocalRandom.current().nextDouble() <= 0.15 && user.getHealth() < 100) {
                int randomHealth = ThreadLocalRandom.current().nextInt(1, 3);
                user.addHealth(randomHealth);
                player.sendMessage(textUtils.colorize("<#966c6c><bold>RARE BLOCK <reset><red>+" + randomHealth + "❤"));
            } else {
                int prestiges;
                if (user.getRebirth() >= 1) {
                    prestiges = (user.getRebirth() * 200) / 2;
                } else if (user.getPrestige() >= 1) {
                    prestiges = 100;
                } else {
                    prestiges = 1;
                }
                user.addPrestige(prestiges);
                player.sendMessage(textUtils.colorize("<#966c6c><bold>RARE BLOCK <reset><white>+" + numberUtils.format(prestiges) + " Prestiges"));
            }
        } else {
            int tokens;
            if (user.getRebirth() >= 1) {
                tokens = (user.getRebirth() * 200) / 2;
            } else if (user.getPrestige() >= 1) {
                tokens = user.getPrestige();
            } else {
                tokens = ThreadLocalRandom.current().nextInt(1, 11);
            }
            user.addSkillTokens(tokens);
            player.sendMessage(textUtils.colorize("<#966c6c><bold>RARE BLOCK <reset><#a884f3>+" + numberUtils.format(tokens) + " S. Tokens"));
        }
    }

    private Set<Position> getRarePositions(Player player, Location center) {
        Set<Position> blockifyPositions = new HashSet<>();
        View view = player.getChromaBlockManager(player.getWorld()).getView(player.getName(), "mine");
        if (view == null) return Set.of();
        int radius = 2;
        for (int x = center.getBlockX() - radius; x <= center.getX() + radius; x++) {
            for (int y = center.getBlockY() - radius; y <= center.getY() + radius; y++) {
                for (int z = center.getBlockZ() - radius; z <= center.getZ() + radius; z++) {
                    double distance = Math.sqrt(
                            Math.pow(x - center.getX(), 2) +
                                    Math.pow(y - center.getY(), 2) +
                                    Math.pow(z - center.getZ(), 2)
                    );
                    if (distance <= radius) {
                        Position blockifyPosition = Position.block(x, y, z);
                        if (view.getBound().contains(blockifyPosition)) {
                            blockifyPositions.add(blockifyPosition);
                        }
                    }
                }
            }
        }
        return blockifyPositions;
    }

}
