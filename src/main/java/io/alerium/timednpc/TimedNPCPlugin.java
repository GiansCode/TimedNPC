package io.alerium.timednpc;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import io.alerium.timednpc.commands.SetPersonalNPCCommand;
import io.alerium.timednpc.commands.SetTimedNPCCommand;
import io.alerium.timednpc.npcs.TimedNPCManager;
import io.alerium.timednpc.npcs.TimedPlaceholder;
import io.alerium.timednpc.personal.PersonalNPCManager;
import io.alerium.timednpc.personal.listener.PersonalNPCListener;
import io.alerium.timednpc.utils.configuration.YAMLConfiguration;
import io.alerium.timednpc.npcs.listener.TimedNPCListener;
import io.samdev.actionutil.ActionUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TimedNPCPlugin extends JavaPlugin{

    @Getter private static TimedNPCPlugin instance;
    
    @Getter private YAMLConfiguration messages;
    @Getter private YAMLConfiguration npcConfig;
    @Getter private YAMLConfiguration personalConfig;

    @Getter private TimedNPCManager timedNPCManager;
    @Getter private PersonalNPCManager personalNPCManager;
    
    @Getter private ActionUtil actionUtil;
    
    @Override
    public void onEnable() {
        instance = this;
        long time = System.currentTimeMillis();
        
        registerConfigs();
        registerInstances();
        registerListeners();
        registerCommands();
        registerHooks();
        
        getLogger().info("Plugin enabled in " + (System.currentTimeMillis()-time) + "ms");
    }

    @Override
    public void onDisable() {
        HologramsAPI.getHolograms(this).forEach(Hologram::delete);
        personalNPCManager.despawnAll();
        
        timedNPCManager.saveAll();
    }

    private void registerConfigs() {
        messages = new YAMLConfiguration(this, "messages");
        npcConfig = new YAMLConfiguration(this, "npcs");
        personalConfig = new YAMLConfiguration(this, "personalNpcs");
    } 
    
    private void registerInstances() {
        timedNPCManager = new TimedNPCManager();
        timedNPCManager.enable();
        
        personalNPCManager = new PersonalNPCManager();
        personalNPCManager.enable();
        
        actionUtil = ActionUtil.init(this);
    }
    
    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        
        pm.registerEvents(new PersonalNPCListener(), this);
        pm.registerEvents(new TimedNPCListener(), this);
    }
    
    private void registerCommands() {
        getCommand("setpersonalnpc").setExecutor(new SetPersonalNPCCommand());
        getCommand("settimednpc").setExecutor(new SetTimedNPCCommand());
    }
    
    private void registerHooks() {
        new TimedPlaceholder().register();
    }
    
}
