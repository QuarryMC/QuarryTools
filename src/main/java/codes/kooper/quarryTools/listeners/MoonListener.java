package codes.kooper.quarryTools.listeners;

import codes.kooper.quarryMoons.QuarryMoons;
import codes.kooper.quarryMoons.enums.MOONS;
import codes.kooper.quarryTools.events.QuarryMineEvent;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

public class MoonListener implements Listener {

    @EventHandler
    public void onBreak(QuarryMineEvent event) {
        QuarryMoons.getInstance().getMoonManager().progressMoon();

        if (event.getUser().hasOption("particles")) return;

        // Particle Effect
        Location location = event.getLocation().clone();
        MOONS moon = QuarryMoons.getInstance().getMoonManager().getCurrentMoon();

        if (moon == MOONS.CURSED) {
            event.getPlayer().spawnParticle(moon.getParticle(), location.add(new Vector(0.5, 0.5, 0.5)), 5, 0.2, 0.2, 0.2, ThreadLocalRandom.current().nextFloat());
            return;
        }

        event.getPlayer().spawnParticle(moon.getParticle(), location.add(new Vector(0.5, 0.5, 0.5)), 5, 0.2, 0.2, 0.2, 0);
    }

}
