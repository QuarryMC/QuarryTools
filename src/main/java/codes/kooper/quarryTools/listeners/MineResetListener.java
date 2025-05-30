package codes.kooper.quarryTools.listeners;

import codes.kooper.koopKore.KoopKore;
import codes.kooper.koopKore.database.models.User;
import codes.kooper.koopKore.utils.Tasks;
import codes.kooper.quarryEconomy.events.LevelingEvent;
import codes.kooper.quarryMines.QuarryMines;
import codes.kooper.quarryMines.database.events.QuarryUnloadEvent;
import codes.kooper.quarryMines.database.models.Quarry;
import codes.kooper.quarryMines.managers.ChromaManager;
import codes.kooper.quarryMoons.QuarryMoons;
import codes.kooper.quarryMoons.enums.MOONS;
import codes.kooper.quarryMoons.events.MoonChangeEvent;
import codes.kooper.quarryTools.QuarryTools;
import codes.kooper.quarryTools.events.QuarryMineEvent;
import eu.endercentral.crazy_advancements.JSONMessage;
import eu.endercentral.crazy_advancements.NameKey;
import eu.endercentral.crazy_advancements.advancement.Advancement;
import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay;
import eu.endercentral.crazy_advancements.advancement.AdvancementVisibility;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Optional;

import static codes.kooper.koopKore.KoopKore.textUtils;

public class MineResetListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(QuarryMineEvent event) {
        QuarryTools.getInstance().getMineResetManager().mineBlocks(event.getQuarry(), event.getResetBlocks().get());
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
        Player player = Bukkit.getPlayer(event.getUser().getId());
        if (player == null) return;
        Optional<User> user = KoopKore.getInstance().getUserAPI().getUser(player.getUniqueId());
        if (user.isEmpty()) return;
        if (QuarryMoons.getInstance().getMineManager().getMineIndex(event.getUser().getRebirth(), event.getUser().getPrestige()) == quarry.get().getMine()) return;
        int size = QuarryMoons.getInstance().getMineManager().getMineIndex(event.getUser().getRebirth(), event.getUser().getPrestige()) - quarry.get().getMine();
        if (size <= 0 || size >= 12) return;
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 5, 1.5f);
        MOONS moon = QuarryMoons.getInstance().getMoonManager().getCurrentMoon();
        ItemStack icon = moon == MOONS.CURSED ? new ItemStack(Material.WITHER_ROSE) : new ItemStack(moon.getConcrete().getFirst());
        JSONMessage title = new JSONMessage(new TextComponent("New Mine (/mines)"));
        JSONMessage description = new JSONMessage(new TextComponent("View /mines"));
        AdvancementDisplay advancementDisplay = new AdvancementDisplay(icon, title, description, AdvancementDisplay.AdvancementFrame.GOAL, AdvancementVisibility.ALWAYS);
        advancementDisplay.setX(1);
        quarry.get().setMine(quarry.get().getMine() + size);
        advancementDisplay.setY(10f);
        Advancement rootAdvancement = new Advancement(new NameKey("quarrytools", "newmine"), advancementDisplay);
        ChromaManager.increaseSize(player, user.get(), quarry.get(), size);
        QuarryTools.getInstance().getMineResetManager().getMineResetCache().remove(event.getUser().getQuarry());
        Tasks.runSync(() -> {
            rootAdvancement.displayToast(player);
            player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_TELEPORT, 5, 1.3f);
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 26, 1, false, false, false));
            player.teleport(new Location(player.getWorld(), -68, 65, 0));
        });
    }
}
