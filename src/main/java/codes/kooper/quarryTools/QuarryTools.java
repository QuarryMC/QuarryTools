package codes.kooper.quarryTools;

import codes.kooper.koopKore.database.tasks.DataSyncTask;
import codes.kooper.quarryTools.commands.*;
import codes.kooper.quarryTools.database.cache.PickStorageCache;
import codes.kooper.quarryTools.database.listeners.PickaxeLoadListener;
import codes.kooper.quarryTools.database.models.PickaxeStorage;
import codes.kooper.quarryTools.database.services.PickaxeService;
//import codes.kooper.quarryTools.listeners.AutoMineListener;
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

import java.util.UUID;
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
    private PickaxeService pickaxeService;
    private PickStorageCache pickStorageCache;
    private DataSyncTask<UUID, PickaxeStorage> pickSyncTask;

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
//        getServer().getPluginManager().registerEvents(new AutoMineListener(), this);
        getServer().getPluginManager().registerEvents(new MineRewardListener(), this);

        // Skills
        getServer().getPluginManager().registerEvents(new AutoRebirthSkill(), this);
        getServer().getPluginManager().registerEvents(new HeartSkill(), this);
        getServer().getPluginManager().registerEvents(new JackhammerSkill(), this);
        getServer().getPluginManager().registerEvents(new RareBlockSkill(), this);
        getServer().getPluginManager().registerEvents(new BossFriendSkill(), this);
        getServer().getPluginManager().registerEvents(new LootboxFinderSkill(), this);
        getServer().getPluginManager().registerEvents(new BoltSkill(), this);

        // Pickaxe Storage
        pickaxeService = new PickaxeService();
        pickStorageCache = new PickStorageCache();
        getServer().getPluginManager().registerEvents(new PickaxeLoadListener(), this);
        pickSyncTask = new DataSyncTask<>(this, pickStorageCache.getAll(), (uuid, pickaxeStorage) -> pickaxeService.savePickStorage(pickaxeStorage), false);
        pickSyncTask.start(2400);

        // Managers
        mineResetManager = new MineResetManager();

        // Item Management
        itemManager = new ItemManager();
        pickaxeItems = new PickaxeItems();
        armorItems = new ArmorItems();

        // Commands
        this.liteCommands = LiteBukkitFactory.builder("quarrytools")
                .commands(
                        new ItemCommand(),
                        new GiveArmorSetCommand(),
                        new GiveQuarryBombCommand(),
                        new FixPickaxeCommand(),
                        new CodexCommand()
                )
                .argument(ArmorItems.ArmorSet.class, new ArmorSetArgument())
                .argumentSuggestion(String.class, SuggestionResult.of(itemManager.getItems().keySet()))
                .message(LiteBukkitMessages.INVALID_USAGE, textUtils.error("<red>Invalid usage of the /quarrytools command!"))
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
