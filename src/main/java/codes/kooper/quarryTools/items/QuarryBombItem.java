package codes.kooper.quarryTools.items;

import codes.kooper.koopKore.KoopKore;
import codes.kooper.koopKore.database.models.User;
import codes.kooper.koopKore.item.ItemBuilder;
import codes.kooper.koopKore.utils.RomanNumber;
import codes.kooper.quarryTools.QuarryTools;
import codes.kooper.quarryTools.utils.MineUtils;
import codes.kooper.shaded.entitylib.meta.display.ItemDisplayMeta;
import codes.kooper.shaded.entitylib.wrapper.WrapperEntity;
import codes.kooper.shaded.nbtapi.NBT;
import codes.kooper.shaded.packetevents.api.util.SpigotConversionUtil;
import codes.kooper.shaded.packetevents.protocol.entity.type.EntityTypes;
import com.oresmash.blockify.Blockify;
import com.oresmash.blockify.models.Stage;
import com.oresmash.blockify.models.View;
import com.oresmash.blockify.types.BlockifyPosition;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Optional;

public class QuarryBombItem {

    public static ItemStack getQuarryBombItem(int tier, int amount) {
        ItemStack itemStack = new ItemBuilder(Material.PLAYER_HEAD)
            .setAmount(amount)
            .setName("<#FF2400><bold>Quarry Bomb <white><reset>[Tier " + RomanNumber.toRoman(tier) + "]")
            .setLore(List.of(
                "<gray>Throw this in your mine [Right-Click] to",
                "<gray>destroy all blocks in a " + tier + " radius."
            ))
            .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWU0MzUyNjgwZDBiYjI5YjkxMzhhZjc4MzMwMWEzOTFiMzQwOTBjYjQ5NDFkNTJjMDg3Y2E3M2M4MDM2Y2I1MSJ9fX0=")
            .hideFlags(true)
            .build();
        NBT.modify(itemStack, (nbt) -> {
            nbt.setInteger("bomb", tier);
        });
        return itemStack;
    }

    public static boolean isBomb(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) return false;
        return NBT.get(itemStack, (nbt) -> {
            return nbt.hasTag("bomb");
        });
    }

    public static int getBombTier(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) return 0;
        return NBT.get(itemStack, (nbt) -> {
            return nbt.getInteger("bomb");
        });
    }

    public static void quarryBombUse(Player player, ItemStack item) {
        Optional<User> targetUser = KoopKore.getInstance().getUserAPI().getUser(player.getUniqueId());
        if (targetUser.isEmpty()) return;

        Stage stage = Blockify.getInstance().getStageManager().getStage(targetUser.get().getQuarry().toString());
        if (stage == null) return;

        View view = stage.getView("mine");
        if (view == null) return;

        WrapperEntity wrapper = new WrapperEntity(EntityTypes.ITEM_DISPLAY);
        ItemDisplayMeta itemDisplayMeta = (ItemDisplayMeta) wrapper.getEntityMeta();
        itemDisplayMeta.setItem(SpigotConversionUtil.fromBukkitItemStack(item));
        wrapper.addViewerSilently(player.getUniqueId());
        Vector direction = player.getEyeLocation().getDirection().normalize().multiply(10);
        Location spawnLoc = player.getLocation();
        Location toLocation = spawnLoc.clone().add(direction);
        Location mineLocation = view.getHighestBlock(toLocation.getBlockX(), toLocation.getBlockZ()).toLocation(player.getWorld());
        wrapper.spawn(SpigotConversionUtil.fromBukkitLocation(spawnLoc));

        int tier = getBombTier(item);

        new BukkitRunnable() {
            @Override
            public void run() {
                Vector direction = mineLocation.toVector().subtract(spawnLoc.toVector()).normalize().multiply(1.1);
                spawnLoc.add(direction);
                float rotation = (spawnLoc.getYaw() + 35) % 360;
                spawnLoc.setYaw(rotation);
                spawnLoc.setPitch(0);
                wrapper.teleport(SpigotConversionUtil.fromBukkitLocation(spawnLoc));

                if (spawnLoc.distanceSquared(mineLocation) <= 1.0) {
                    MineUtils.luckyBlockNuker(player, BlockifyPosition.fromLocation(mineLocation.toLocation(player.getWorld())), view, stage, tier);
                    this.cancel();
                    wrapper.despawn();
                }
            }
        }.runTaskTimerAsynchronously(QuarryTools.getInstance(), 0L, 1L);
    }
}
