package codes.kooper.quarryTools.listeners.skills;

import codes.kooper.koopKore.database.models.User;
import codes.kooper.models.View;
import codes.kooper.quarryMines.utils.BlockUtils;
import codes.kooper.quarryMines.utils.QuarryBlockUtils;
import codes.kooper.quarrySkills.QuarrySkills;
import codes.kooper.quarrySkills.managers.SkillManager;
import codes.kooper.quarrySkills.models.Skill;
import codes.kooper.quarryTools.events.QuarryMineEvent;
import io.papermc.paper.math.Position;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class JackhammerSkill implements Listener {

    @EventHandler
    public void onBreak(QuarryMineEvent event) {
        User user = event.getUser();
        Player player = event.getPlayer();
        if (!user.hasSkill("jackhammer") || user.hasDisabledSkill("jackhammer")) return;

        Skill skill = QuarrySkills.getInstance().getSkillManager().getSkills().get("jackhammer");
        if (ThreadLocalRandom.current().nextDouble() > SkillManager.getChance(user, skill)) return;

        View view = player.getChromaBlockManager(player.getWorld()).getView(player.getName(), "mine");
        if (view == null) return;

        Location loc1 = new Location(player.getWorld(), view.getBound().getMaxX(), event.getLocation().getBlockY(), view.getBound().getMaxZ());
        Location loc2 = new Location(player.getWorld(), view.getBound().getMinX(), event.getLocation().getBlockY(), view.getBound().getMinZ());

        Set<Position> positions = BlockUtils.getBlocksBetween(loc1, loc2);
        if (positions.isEmpty()) return;

        int count = positions.size();
        AtomicInteger glazedCount = new AtomicInteger();

        positions = positions.stream()
                .filter(position -> {
                    BlockData blockData = player.getChromaBlockManager(player.getWorld()).getBlockData(position);
                    if (blockData == null) return false;

                    Material material = blockData.getMaterial();
                    if (material.name().contains("GLAZED")) {
                        glazedCount.getAndIncrement();
                    }
                    if (event.getQuarry().getSpawnedBoss() != null && material == event.getQuarry().getSpawnedBoss().getBoss().bossBlock())
                        return false;
                    return material != Material.AIR &&
                            material != Material.BEDROCK &&
                            material != Material.NETHERRACK &&
                            material != Material.PRISMARINE &&
                            material != Material.REDSTONE_BLOCK;
                })
                .collect(Collectors.toSet());

        if (positions.isEmpty()) return;

        for (Position position : positions) {
            player.getChromaBlockManager(player.getWorld()).setBlock(position, Material.AIR.createBlockData());
            if (user.hasOption("particles")) continue;
            BlockData blockData = player.getChromaBlockManager(player.getWorld()).getBlockData(position);
            player.spawnParticle(Particle.BLOCK, position.toLocation(player.getWorld()), 1, 0.3, 0.3, 0.3, blockData);
        }

        for (int i = 0; i < (glazedCount.get() / 20); i++) {
            QuarryBlockUtils.handleLuckyBlock(event.getUser());
        }

        event.addBlocks(positions.size() / 10);
        event.addResetBlocks(count);

        player.getChromaBlockManager(player.getWorld()).refreshBlocks(positions);
        if (!user.hasDisabledSkillNotification("jackhammer")) {
            player.playSound(player.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1, 1);
        }
    }
}
