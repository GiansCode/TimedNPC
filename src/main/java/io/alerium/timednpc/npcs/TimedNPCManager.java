package io.alerium.timednpc.npcs;

import io.alerium.timednpc.TimedNPCPlugin;
import io.alerium.timednpc.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class TimedNPCManager {
    
    private final TimedNPCPlugin plugin = TimedNPCPlugin.getInstance();
    private final List<TimedNPC> npcs = new ArrayList<>();
    
    public void enable() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (TimedNPC npc : npcs) {
                if (npc.isToChange())
                    npc.changeNPC();
            }
        }, 20*30, 20*30); 
        
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::saveAll, 20*60*5, 20*60*5);
    }

    /**
     * This method saves all the TimedNPC(s) in the config
     */
    public void saveAll() {
        for (TimedNPC npc : npcs) {
            ConfigurationSection section = plugin.getNpcConfig().getConfig().getConfigurationSection(npc.getId());
            section.set("lastChange", npc.getLastChange());
            section.set("lastNPC", npc.getLastNpc());
        }
        
        plugin.getNpcConfig().save();
    }

    /**
     * This method gets a TimedNPC by his id
     * @param id The ID of the TimedNPC
     * @return The TimedNPC, null if not found
     */
    public TimedNPC getNPC(String id) {
        for (TimedNPC npc : npcs) {
            if (npc.getId().equalsIgnoreCase(id))
                return npc;
        }
        
        return null;
    }

    /**
     * This method checks if exist a TimedNPC with the specified ID
     * @param id The ID of the TimedNPC
     * @return True if it exist
     */
    public boolean existNPC(String id) {
        return getNPC(id) != null;
    }

    /**
     * This method loads a TimedNPC from the config file
     * @param id The ID of the TimedNPC
     */
    public void loadNPC(String id) {
        ConfigurationSection section = plugin.getNpcConfig().getConfig().getConfigurationSection(id);

        int[] npcs = section.getIntegerList("NPCs").stream().mapToInt(i -> i).toArray();
        long changeEvery = TimeUtil.toMillis(section.getString("changeEvery"));
        Location location = plugin.getNpcConfig().getLocation(id + ".location");
        long lastChange = section.getLong("lastChange");
        int lastNpc = section.getInt("lastNPC");

        TimedNPC npc = new TimedNPC(
                id,
                npcs,
                changeEvery,
                location,
                lastChange,
                lastNpc
        );
        npc.init();
        
        this.npcs.add(npc);
    }

    /**
     * This method loads all the TimedNPC(s) from the config
     */
    public void loadNPCs() {
        for (String id : plugin.getNpcConfig().getConfig().getKeys(false))
            loadNPC(id);

        plugin.getLogger().info("Loaded " + npcs.size() + " timed NPCs.");
    }
    
}
