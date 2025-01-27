package codes.kooper.quarryTools.listeners;

import codes.kooper.koopKore.database.events.UserLoadEvent;
import codes.kooper.quarryMoons.QuarryMoons;
import codes.kooper.quarryMoons.enums.MOONS;
import codes.kooper.quarryTools.events.QuarryMineEvent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

import static codes.kooper.koopKore.KoopKore.textUtils;

public class HeartListener implements Listener {

    // JOIN HEALTH
    @EventHandler
    public void onFirstJoin(UserLoadEvent event) {
        Player player = Bukkit.getPlayer(event.getUser().getId());
        if (player == null) return;
        event.getUser().syncHealth();
    }

    // RED MOON
    @EventHandler
    public void onBreak(QuarryMineEvent event) {
        if (QuarryMoons.getInstance().getMoonManager().getCurrentMoon() != MOONS.RED) return;
        int random = ThreadLocalRandom.current().nextInt(1, 1001);
        if (random > 3) return;
        MOONS moon = QuarryMoons.getInstance().getMoonManager().getCurrentMoon();
        double hearts = ThreadLocalRandom.current().nextInt(1, 6);
        Player player = event.getPlayer();
        if (event.getUser().getHealth() + hearts > event.getUser().getHealthCap()) {
            hearts = event.getUser().getHealthCap() - event.getUser().getHealth();
            if (hearts <= 0) return;
        }
        player.sendMessage(
            textUtils.colorize(
                moon.getColorCode() + "<bold>" + textUtils.capitalize(moon.name()).toUpperCase() + " MOON<reset><white> The server's moon is currently " + moon.getColorCode() + "red<white>! Due to this, you have received <red>" + hearts + " HP<white>."
            )
        );
        player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 5, 1.5f);
        event.getUser().addHealth(hearts);
    }

    // REDSTONE BLOCK
    @EventHandler
    public void onBreakOfRedstone(QuarryMineEvent event) {
        if (event.getBlockData().getMaterial() != Material.REDSTONE_BLOCK) return;
        Player player = event.getPlayer();
        if (event.getUser().getHealth() + 1 > event.getUser().getHealthCap()) {
            return;
        }
        event.getUser().addHealth(1);
        Location location = event.getLocation().clone();
        player.spawnParticle(Particle.HEART, location.add(new Vector(0.5, 0.5, 0.5)),5, 0,0 ,0 ,0);
        player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 5, 1.5f);
    }

}
