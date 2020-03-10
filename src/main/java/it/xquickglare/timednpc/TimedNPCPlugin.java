package it.xquickglare.timednpc;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import it.xquickglare.timednpc.commands.SetPersonalNPCCommand;
import it.xquickglare.timednpc.commands.SetTimedNPCCommand;
import it.xquickglare.timednpc.npcs.listener.TimedNPCListener;
import it.xquickglare.timednpc.npcs.TimedNPCManager;
import it.xquickglare.timednpc.npcs.TimedPlaceholder;
import it.xquickglare.timednpc.personal.PersonalNPCManager;
import it.xquickglare.timednpc.personal.listener.PersonalNPCListener;
import it.xquickglare.timednpc.utils.configuration.YAMLConfiguration;
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
