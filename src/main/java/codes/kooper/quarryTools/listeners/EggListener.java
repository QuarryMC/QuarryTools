package codes.kooper.quarryTools.listeners;

import codes.kooper.koopKore.database.models.User;
import codes.kooper.quarryPets.QuarryPets;
import codes.kooper.quarryTools.events.QuarryMineEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.concurrent.ThreadLocalRandom;

public class EggListener implements Listener {

    @EventHandler
    public void onBreak(QuarryMineEvent event) {
        User user = event.getUser();

        int add = 1;
        if (user.hasSkill("candy") && !user.hasDisabledSkill("candy")) {
            int max = (int) (user.getSkillLevel("candy") / 200);
            add = ThreadLocalRandom .current().nextInt(1, max + 1);
        }

        QuarryPets.getInstance().getEggManager().progressEggs(event.getPlayer(), add);
    }

}
