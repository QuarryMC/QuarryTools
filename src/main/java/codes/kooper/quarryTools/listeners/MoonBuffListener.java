package codes.kooper.quarryTools.listeners;

import codes.kooper.koopKore.database.models.User;
import codes.kooper.koopKore.utils.Tasks;
import codes.kooper.quarryEconomy.QuarryEconomy;
import codes.kooper.quarryMines.QuarryMines;
import codes.kooper.quarryMines.managers.BossManager;
import codes.kooper.quarryMoons.QuarryMoons;
import codes.kooper.quarryTools.events.QuarryMineEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

import static codes.kooper.koopKore.KoopKore.numberUtils;
import static codes.kooper.koopKore.KoopKore.textUtils;

public class MoonBuffListener implements Listener {
    private static final DecimalFormat df = new DecimalFormat("#.##");

    @EventHandler
    public void onBreak(QuarryMineEvent event) {
        Player player = event.getPlayer();
        User user = event.getUser();
        if (ThreadLocalRandom.current().nextInt(1, 1001) > 3) return;
        switch (QuarryMoons.getInstance().getMoonManager().getCurrentMoon()) {
            case YELLOW:
                int random = ThreadLocalRandom.current().nextInt(1, 5);
                double addBalance = QuarryEconomy.getInstance().getGlobalMultiplierManager().getGlobalMultiplier() * 175487451 * random;
                user.addBalance(addBalance);
                player.sendMessage(textUtils.colorize("<yellow><bold>YELLOW MOON<reset><white> The server's moon is currently <yellow>yellow<white>! Due to this, you have received <green>$" + numberUtils.format(addBalance) + "."));
                break;
            case PURPLE:
                if (ThreadLocalRandom.current().nextDouble(1, 101) <= 20) {
                    int stokens = ThreadLocalRandom.current().nextInt(1000, 2001);
                    user.addSkillTokens(stokens);
                    player.sendMessage(textUtils.colorize("<light_purple><bold>PURPLE MOON<reset><white> The server's moon is currently <light_purple>purple<white>! Due to this, you have received <#a884f3>⛂" + df.format(stokens) + "."));
                } else {
                    int stokens = ThreadLocalRandom.current().nextInt(1, 1001);
                    user.addSkillTokens(stokens);
                }
                break;
            case GRAY:
                BossManager.Boss boss;
                if (ThreadLocalRandom.current().nextDouble() <= 0.5) {
                    boss = QuarryMines.getInstance().getBossManager().getBosses().get("shorse");
                } else {
                    if (ThreadLocalRandom.current().nextDouble() <= 0.4) {
                        if (ThreadLocalRandom.current().nextDouble() <= 0.25) {
                            boss = QuarryMines.getInstance().getBossManager().getBosses().get("phantom");
                        } else {
                            if (ThreadLocalRandom.current().nextDouble() <= 0.25) {
                                boss = QuarryMines.getInstance().getBossManager().getBosses().get("irongolem");
                            } else {
                                boss = QuarryMines.getInstance().getBossManager().getBosses().get("polarbear");
                            }
                        }
                    } else {
                        boss = QuarryMines.getInstance().getBossManager().getBosses().get("zoglin");
                    }
                }
                Tasks.runSync(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "boss givespawnblock " + boss.key() + " " + player.getName()));
                player.sendMessage(textUtils.colorize("<gray><bold>GRAY MOON<reset><white> The server's moon is currently <gray>gray<white>! Due to this, you have received a <white>" + boss.color1() + "<bold>" + textUtils.capitalize(boss.name()).toUpperCase() + " BOSS<reset><white> spawn block."));
                break;
        }
    }
}
