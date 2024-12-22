package codes.kooper.quarryTools.listeners.skills;

import codes.kooper.koopKore.database.models.User;
import codes.kooper.quarrySkills.QuarrySkills;
import codes.kooper.quarrySkills.managers.SkillManager;
import codes.kooper.quarrySkills.models.Skill;
import codes.kooper.quarryTools.events.QuarryMineEvent;
import com.oresmash.blockify.types.BlockifyPosition;
import com.oresmash.blockify.utils.BlockUtils;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class JackhammerSkill implements Listener {

    @EventHandler
    public void onBreak(QuarryMineEvent event) {
        User user = event.getUser();
        Player player = event.getPlayer();
        if (!user.hasSkill("jackhammer") || user.hasDisabledSkill("jackhammer")) return;

        Skill skill = QuarrySkills.getInstance().getSkillManager().getSkills().get("jackhammer");
        if (ThreadLocalRandom.current().nextDouble() > SkillManager.getChance(user, skill)) return;

        BlockifyPosition loc1 = new BlockifyPosition(event.getPosition().getX(), event.getPosition().getY(), event.getPosition().getZ());
        BlockifyPosition loc2 = new BlockifyPosition(event.getPosition().getX(), event.getPosition().getY(), event.getPosition().getZ());
        loc1.setX(loc1.getX() - 3);
        loc1.setZ(loc1.getZ() - 3);
        loc2.setX(loc2.getX() + 3);
        loc2.setZ(loc2.getZ() + 3);

        Set<BlockifyPosition> positions = BlockUtils.getBlocksBetween(loc1, loc2);

        for (BlockifyPosition position : positions) {
            BlockData blockData = event.getView().getBlock(position);
            if (blockData == null) continue;
            Material material = blockData.getMaterial();
            if (material == Material.AIR || material.name().contains("GLAZED") || material == Material.ANCIENT_DEBRIS || material == Material.REDSTONE_BLOCK) continue;
            player.spawnParticle(Particle.BLOCK, position.toLocation(player.getWorld()), 10, 0.3, 0.3, 0.3, blockData);
            event.getView().setBlock(position, Material.AIR.createBlockData());
            event.addBlocks(1);
        }

        event.getStage().refreshBlocksToAudience(positions);
        if (user.hasDisabledSkillNotification("jackhammer")) return;
        player.playSound(player.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1, 1);
    }

}
