package codes.kooper.quarryTools;

import codes.kooper.quarryTools.commands.GiveArmorSetCommand;
import codes.kooper.quarryTools.commands.GiveQuarryBombCommand;
import codes.kooper.quarryTools.commands.AutoMineCommand;
import codes.kooper.quarryTools.listeners.AutoMineListener;
import codes.kooper.quarryTools.commands.ItemCommand;
import codes.kooper.quarryTools.commands.arguments.ArmorSetArgument;
import codes.kooper.quarryTools.items.ArmorItems;
import codes.kooper.quarryTools.items.PickaxeItems;
import codes.kooper.quarryTools.listeners.*;
import codes.kooper.quarryTools.listeners.skills.*;
import codes.kooper.quarryTools.managers.ItemManager;
import codes.kooper.quarryTools.managers.MineResetManager;
import codes.kooper.shaded.litecommands.LiteCommands;
import codes.kooper.shaded.litecommands.bukkit.LiteBukkitFactory;
import codes.kooper.shaded.litecommands.bukkit.LiteBukkitMessages;
import codes.kooper.shaded.litecommands.suggestion.SuggestionResult;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static codes.kooper.koopKore.KoopKore.textUtils;

@Getter
public final class QuarryTools extends JavaPlugin {
    private PickaxeItems pickaxeItems;
    private ArmorItems armorItems;
    private ItemManager itemManager;
    private MineResetManager mineResetManager;
    private ExecutorService miningThreads;
    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Threads
        miningThreads = Executors.newFixedThreadPool(8);

        // Listeners
        getServer().getPluginManager().registerEvents(new MiningListener(), this);
        getServer().getPluginManager().registerEvents(new MoonListener(), this);
        getServer().getPluginManager().registerEvents(new PickaxeLevelingListener(), this);
        getServer().getPluginManager().registerEvents(new HeartListener(), this);
        getServer().getPluginManager().registerEvents(new MineResetListener(), this);
        getServer().getPluginManager().registerEvents(new BackpackListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLevelingListener(), this);
        getServer().getPluginManager().registerEvents(new QuarryBlockListener(), this);
        getServer().getPluginManager().registerEvents(new DoubleRebirthListener(), this);
        getServer().getPluginManager().registerEvents(new BossListener(), this);
        getServer().getPluginManager().registerEvents(new EggListener(), this);
        getServer().getPluginManager().registerEvents(new PetListener(), this);
        getServer().getPluginManager().registerEvents(new ArmorChangeListener(), this);
        getServer().getPluginManager().registerEvents(new QuarryBombListener(), this);
        getServer().getPluginManager().registerEvents(new PickaxeListener(), this);
        getServer().getPluginManager().registerEvents(new AutoMineListener(), this);

        // Skills
        getServer().getPluginManager().registerEvents(new AutoRebirthSkill(), this);
        getServer().getPluginManager().registerEvents(new HeartSkill(), this);
        getServer().getPluginManager().registerEvents(new JackhammerSkill(), this);
        getServer().getPluginManager().registerEvents(new RareBlockSkill(), this);
        getServer().getPluginManager().registerEvents(new BossFriendSkill(), this);

        // Managers
        mineResetManager = new MineResetManager();

        // Item Management
        itemManager = new ItemManager();
        pickaxeItems = new PickaxeItems();
        armorItems = new ArmorItems();

        // Commands
        AutoMineListener autoMineListener = new AutoMineListener();
        getServer().getPluginManager().registerEvents(autoMineListener, this);
        this.liteCommands = LiteBukkitFactory.builder("quarrytools")
                .commands(
                        new ItemCommand(),
                        new GiveArmorSetCommand(),
                        new GiveQuarryBombCommand(),
                        new AutoMineCommand(autoMineListener)
                )
                .argument(ArmorItems.ArmorSet.class, new ArmorSetArgument())
                .argumentSuggestion(String.class, SuggestionResult.of(itemManager.getItems().keySet()))
                .message(LiteBukkitMessages.INVALID_USAGE, textUtils.error("There's an invalid usage with the /quarrytools command!"))
                .build();
    }

    @Override
    public void onDisable() {
        // Shutdown LiteCommands
        if (this.liteCommands != null) {
            this.liteCommands.unregister();
        }

        // Shutdown Farming Threads
        miningThreads.shutdown();
        try {
            if (!miningThreads.awaitTermination(60, TimeUnit.SECONDS)) {
                miningThreads.shutdownNow();
            }
        } catch (InterruptedException e) {
            miningThreads.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public static QuarryTools getInstance() {
        return QuarryTools.getPlugin(QuarryTools.class);
    }
}
