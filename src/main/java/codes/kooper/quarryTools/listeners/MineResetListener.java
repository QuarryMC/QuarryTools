package codes.kooper.quarryTools.listeners;

import codes.kooper.koopKore.utils.Tasks;
import codes.kooper.quarryEconomy.events.LevelingEvent;
import codes.kooper.quarryMines.QuarryMines;
import codes.kooper.quarryMines.database.events.QuarryUnloadEvent;
import codes.kooper.quarryMines.database.models.Quarry;
import codes.kooper.quarryMines.managers.BlockifyManager;
import codes.kooper.quarryMoons.QuarryMoons;
import codes.kooper.quarryMoons.events.MoonChangeEvent;
import codes.kooper.quarryMoons.models.QuarryMine;
import codes.kooper.quarryTools.QuarryTools;
import codes.kooper.quarryTools.events.QuarryMineEvent;
import com.oresmash.blockify.Blockify;
import com.oresmash.blockify.models.Pattern;
import com.oresmash.blockify.models.Stage;
import com.oresmash.blockify.models.View;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Optional;

import static codes.kooper.koopKore.KoopKore.textUtils;

public class MineResetListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(QuarryMineEvent event) {
        QuarryTools.getInstance().getMineResetManager().mineBlocks(event.getQuarry(), event.getBlocks());
    }

    @EventHandler
    public void onQuarryUnload(QuarryUnloadEvent event) {
        QuarryTools.getInstance().getMineResetManager().getMineResetCache().remove(event.getQuarry().getUuid());
    }

    @EventHandler
    public void onMoonChange(MoonChangeEvent event) {
        QuarryTools.getInstance().getMineResetManager().getMineResetCache().clear();
    }

    @EventHandler
    public void onLevel(LevelingEvent event) {
        Optional<Quarry> quarry = QuarryMines.getInstance().getApi().getQuarry(event.getUser().getQuarry());
        if (quarry.isEmpty()) return;
        QuarryMine quarryMine = QuarryMoons.getInstance().getMineManager().getBestMine(event.getUser());
        Pattern pattern = QuarryMoons.getInstance().getMineManager().getBlockifyPattern(quarry.get(), quarryMine);
        Stage stage = Blockify.getInstance().getStageManager().getStage(event.getUser().getQuarry().toString());
        View view = stage.getView("mine");
        if (view.getPattern().equals(pattern)) return;
        QuarryTools.getInstance().getMineResetManager().getMineResetCache().remove(event.getUser().getQuarry());
        Player player = Bukkit.getPlayer(event.getUser().getId());
        if (player == null) return;
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 5, 1.5f);
        player.sendMessage(textUtils.success("Congratulations! You've unlocked a new mine! Your mine has been refreshed with new blocks and increased in size. Check out the block chances with /mines."));
        BlockifyManager.increaseSize(quarry.get());
        BlockifyManager.resetMine(quarry.get());
        Tasks.runSync(() -> {
            player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_TELEPORT, 5, 1.3f);
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 26, 1, false, false, false));
            player.teleport(new Location(player.getWorld(), -68, 65, 0));
        });
    }
}
