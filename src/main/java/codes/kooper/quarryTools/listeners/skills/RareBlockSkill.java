package codes.kooper.quarryTools.listeners.skills;

import codes.kooper.koopKore.database.models.User;
import codes.kooper.quarrySkills.QuarrySkills;
import codes.kooper.quarrySkills.managers.SkillManager;
import codes.kooper.quarrySkills.models.Skill;
import codes.kooper.quarryTools.events.QuarryMineEvent;
import io.papermc.paper.math.Position;
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

//    @EventHandler
//    public void onBreak(QuarryMineEvent event) {
//        User user = event.getUser();
//        Player player = event.getPlayer();
//        if (event.getBlockData().getMaterial() == Material.ANCIENT_DEBRIS) {
//            handleAncientDebrisRewards(player, user);
//            return;
//        }
//        if (!user.hasSkill("rare_block") || user.hasDisabledSkill("rare_block")) return;
//        Skill skill = QuarrySkills.getInstance().getSkillManager().getSkills().get("rare_block");
//        if (ThreadLocalRandom.current().nextDouble() > SkillManager.getChance(user, skill)) return;
//        Set<Position> blockifyPositions = getRarePositions(event.getView(), event.getPosition());
//        event.getView().setBlocks(blockifyPositions, Material.ANCIENT_DEBRIS.createBlockData());
//        event.getStage().refreshBlocksToAudience(blockifyPositions);
//        blockifyPositions.forEach(pos -> player.spawnParticle(Particle.SOUL, pos.toLocation(player.getWorld()).add(new Vector(0.5, 0.5, 0.5)), 5, 0.2, 0.2, 0.2, 0));
//        if (user.hasDisabledSkillNotification("rare_block")) return;
//        player.playSound(player.getLocation(), Sound.BLOCK_BEEHIVE_ENTER, 5, 1);
//    }
//
//    private void handleAncientDebrisRewards(Player player, User user) {
//        if (ThreadLocalRandom.current().nextDouble() > 0.3) return;
//        if (ThreadLocalRandom.current().nextDouble() <= 0.4) {
//            if (ThreadLocalRandom.current().nextDouble() <= 0.15 && user.getHealth() < 100) {
//                int randomHealth = ThreadLocalRandom.current().nextInt(1, 3);
//                user.addHealth(randomHealth);
//                player.sendMessage(textUtils.colorize("<#966c6c><bold>RARE BLOCK <reset><red>+" + randomHealth + "❤"));
//            } else {
//                int prestiges;
//                if (user.getRebirth() >= 1) {
//                    prestiges = (user.getPrestige() * 4) / 2;
//                } else if (user.getPrestige() >= 1) {
//                    prestiges = (user.getRebirth() * 200) / 2;
//                } else {
//                    prestiges = 1;
//                }
//                user.addPrestige(prestiges);
//                player.sendMessage(textUtils.colorize("<#966c6c><bold>RARE BLOCK <reset><white>+" + numberUtils.format(prestiges) + " Prestiges"));
//            }
//        } else {
//            int tokens;
//            if (user.getRebirth() >= 1) {
//                tokens = user.getPrestige();
//            } else if (user.getPrestige() >= 1) {
//                tokens = (user.getRebirth() * 200) / 2;
//            } else {
//                tokens = ThreadLocalRandom.current().nextInt(1, 11);
//            }
//            user.addSkillTokens(tokens);
//            player.sendMessage(textUtils.colorize("<#966c6c><bold>RARE BLOCK <reset><#a884f3>+" + numberUtils.format(tokens) + " S. Tokens"));
//        }
//    }
//
//    private Set<BlockifyPosition> getRarePositions(View view, BlockifyPosition center) {
//        Set<BlockifyPosition> blockifyPositions = new HashSet<>();
//        int radius = 2;
//        for (int x = center.getX() - radius; x <= center.getX() + radius; x++) {
//            for (int y = center.getY() - radius; y <= center.getY() + radius; y++) {
//                for (int z = center.getZ() - radius; z <= center.getZ() + radius; z++) {
//                    double distance = Math.sqrt(
//                            Math.pow(x - center.getX(), 2) +
//                                    Math.pow(y - center.getY(), 2) +
//                                    Math.pow(z - center.getZ(), 2)
//                    );
//                    if (distance <= radius) {
//                        BlockifyPosition blockifyPosition = new BlockifyPosition(x, y, z);
//                        if (view.hasBlock(blockifyPosition)) blockifyPositions.add(blockifyPosition);
//                    }
//                }
//            }
//        }
//        return blockifyPositions;
//    }

}
