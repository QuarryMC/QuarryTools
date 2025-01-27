package codes.kooper.quarryTools.utils;

import codes.kooper.koopKore.KoopKore;
import codes.kooper.koopKore.database.models.User;
import codes.kooper.quarryMines.utils.QuarryBlockUtils;
import codes.kooper.quarryMoons.QuarryMoons;
import codes.kooper.quarryMoons.enums.MOONS;
import codes.kooper.quarryTools.QuarryTools;
import codes.kooper.shaded.entitylib.meta.display.BlockDisplayMeta;
import codes.kooper.shaded.entitylib.wrapper.WrapperEntity;
import codes.kooper.shaded.packetevents.api.util.SpigotConversionUtil;
import codes.kooper.shaded.packetevents.protocol.entity.type.EntityTypes;
import codes.kooper.shaded.packetevents.protocol.world.states.type.StateTypes;
import codes.kooper.shaded.packetevents.util.Vector3f;

import io.papermc.paper.math.Position;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class MineUtils {

    public static void luckyBlockNuker(Player player, Location center, int radius) {
        Optional<User> optionalUser = KoopKore.getInstance().getUserAPI().getUser(player.getUniqueId());
        if (optionalUser.isEmpty()) return;
        User user = optionalUser.get();
        Set<Position> positions = getAllPositionsInRadius(center, radius).stream()
                .filter(pos -> player.getChromaBlockManager(player.getWorld()).getBlockData(pos) != null && player.getChromaBlockManager(player.getWorld()).getBlockData(pos).getMaterial().name().contains("GLAZED"))
                .collect(Collectors.toSet());
        if (positions.isEmpty()) return;
        player.playSound(player.getLocation(), Sound.ENTITY_TNT_PRIMED, 5, 1.5f);

        // Create and position the TNT block
        WrapperEntity tntBlock = new WrapperEntity(EntityTypes.BLOCK_DISPLAY);
        BlockDisplayMeta blockDisplayMeta = (BlockDisplayMeta) tntBlock.getEntityMeta();
        blockDisplayMeta.setBlockId(StateTypes.TNT.createBlockState().getGlobalId());

        blockDisplayMeta.setScale(new Vector3f(5, 5, 5));
        tntBlock.addViewerSilently(player.getUniqueId());

        // Properly calculate spawn location to center the TNT block
        Location spawnLoc = center.toLocation(player.getWorld());
        spawnLoc.setY(61 + 15);
        spawnLoc.subtract((5 / 2.0) - 0.5, 0, (5 / 2.0) - 0.5);
        tntBlock.spawn(SpigotConversionUtil.fromBukkitLocation(spawnLoc));



        // Run asynchronously to send client-side packets
        new BukkitRunnable() {
            int ticks = 0;
            final Map<Position, WrapperEntity> entityMap = new HashMap<>(positions.size());
            final int startSize = positions.size();

            @Override
            public void run() {
                if (ticks >= 100) {
                    tntBlock.despawn();
                    entityMap.values().forEach(WrapperEntity::despawn);
                    cancel();
                    return;
                }
                if (ticks == 0) {
                    // Initialize block displays
                    for (Position pos : positions) {
                        WrapperEntity blockDisplay = new WrapperEntity(EntityTypes.BLOCK_DISPLAY);
                        BlockDisplayMeta blockDisplayMeta1 = (BlockDisplayMeta) blockDisplay.getEntityMeta();
                        BlockData blockData = player.getChromaBlockManager(player.getWorld()).getBlockData(pos);
                        if (blockData == null) continue;
                        blockDisplayMeta1.setBlockId(SpigotConversionUtil.fromBukkitBlockData(blockData).getGlobalId());
                        blockDisplay.addViewerSilently(player.getUniqueId());
                        blockDisplay.spawn(SpigotConversionUtil.fromBukkitLocation(pos.toLocation(player.getWorld())));
                        entityMap.put(pos, blockDisplay);
                    }
                    player.getChromaBlockManager(player.getWorld()).setBlocks(positions, Material.AIR.createBlockData());
                    player.getChromaBlockManager(player.getWorld()).refreshBlocks(positions);
                } else {
                    // Process positions
                    final Iterator<Position> iterator = positions.iterator();
                    while (iterator.hasNext()) {

                        // Prevent concurrent exceptions
                        Position pos;
                        try {
                            pos = iterator.next();
                        } catch (Exception e) {
                            return;
                        }

                        WrapperEntity wrapper = entityMap.get(pos);
                        Location location = SpigotConversionUtil.toBukkitLocation(player.getWorld(), wrapper.getLocation());
                        Location midPoint = SpigotConversionUtil.toBukkitLocation(player.getWorld(), tntBlock.getLocation());
                        midPoint.add((5 / 2.0) - 0.5, 0, (5 / 2.0) - 0.5);

                        // Check distance and remove if within range
                        if (location.distanceSquared(midPoint) <= 2.5) {
                            iterator.remove();
                            wrapper.despawn();
                            if (positions.isEmpty()) {
                                player.playSound(player.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 3, 1.5f);
                                for (int i = 0; i < startSize; i++) {
                                    QuarryBlockUtils.handleLuckyBlock(user);
                                }
                                tntBlock.despawn();
                                cancel();
                                return;
                            }
                        }

                        // Calculate speed based on distance
                        double distance = location.distance(midPoint);
                        double speed = Math.max(0.5, Math.min(1.5, 150.0 / distance)); // Speed increases as distance decreases
                        Vector direction = midPoint.toVector().subtract(location.toVector()).normalize().multiply(speed);
                        location.add(direction);
                        wrapper.teleport(SpigotConversionUtil.fromBukkitLocation(location));

                        // Add particle effects
                        MOONS moon = QuarryMoons.getInstance().getMoonManager().getCurrentMoon();
                        Particle particleType = moon == MOONS.CURSED ? Particle.CRIT : moon.getParticle();
                        player.spawnParticle(particleType, location.clone().add(new Vector(0.5, 0.5, 0.5)), 2, 0.5, 0.5, 0.5, 0);
                    }
                }
                ticks++;
            }
        }.runTaskTimerAsynchronously(QuarryTools.getInstance(), 0L, 1L);
    }

    public static Set<Position> getAllPositionsInRadius(Position center, int radius) {
        Set<Position> positions = new HashSet<>();
        for (int x = center.blockX() - radius; x <= center.blockX() + radius; x++) {
            for (int y = center.blockY() - radius; y <= center.blockY() + radius; y++) {
                for (int z = center.blockZ() - radius; z <= center.blockZ() + radius; z++) {
                    double distanceSquared = Math.pow(x - center.blockX(), 2) + Math.pow(y - center.blockY(), 2) + Math.pow(z - center.blockZ(), 2);
                    if (distanceSquared <= Math.pow(radius, 2)) {
                        positions.add(Position.block(x, y, z));
                    }
                }
            }
        }
        return positions;
    }
}
