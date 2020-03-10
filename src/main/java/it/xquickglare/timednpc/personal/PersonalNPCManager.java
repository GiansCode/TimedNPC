package it.xquickglare.timednpc.personal;

import it.xquickglare.timednpc.TimedNPCPlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersonalNPCManager {
    
    private final TimedNPCPlugin plugin = TimedNPCPlugin.getInstance();
    @Getter private final List<PersonalNPC> npcs = new ArrayList<>();
    
    public void enable() {
        loadNPCs();

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Location location = player.getLocation();
                for (PersonalNPC npc : npcs) {
                    if (npc.getViewers().containsKey(player.getUniqueId())) {
                        if (location.getWorld() != npc.getLocation().getWorld() || location.distance(npc.getLocation()) > 100)
                            Bukkit.getScheduler().runTask(plugin, () -> npc.despawn(player));
                        continue;
                    }
                    
                    if (location.getWorld() == npc.getLocation().getWorld() && location.distance(npc.getLocation()) < 50)
                        Bukkit.getScheduler().runTask(plugin, () -> npc.spawn(player));
                }
            }
        }, 20, 20);
    }

    /**
     * This method despawns all the NPCs
     */
    public void despawnAll() {
        for (PersonalNPC npc : npcs) {
            for (UUID uuid : npc.getViewers().keySet()) {
                npc.despawn(Bukkit.getPlayer(uuid));
            }
        }
    }

    /**
     * This method gets a PersonalNPC by the entityId
     * @param entityId The entityId
     * @return The PersonalNPC, null if not found
     */
    public PersonalNPC getNPC(int entityId) {
        for (PersonalNPC npc : npcs) {
            if (npc.getEntityId() == entityId)
                return npc;
        }
        
        return null;
    }

    /**
     * This method checks if exist a PersonalNPC with the specified name
     * @param id The id of the PersonalNPC
     * @return True if exist
     */
    public boolean existNPC(String id) {
        for (PersonalNPC npc : npcs) {
            if (npc.getId().equalsIgnoreCase(id))
                return true;
        }
        
        return false;
    }

    /**
     * This method loads a PersonalNPC from config
     * @param id The id of the PersonalNPC
     */
    public void loadNPC(String id) {
        ConfigurationSection section = plugin.getPersonalConfig().getConfig().getConfigurationSection(id);

        String name = ChatColor.translateAlternateColorCodes('&', section.getString("name"));
        Location location = plugin.getPersonalConfig().getLocation(id + ".location");
        Location hologramLocation = plugin.getPersonalConfig().getLocation(id + ".hologramLocation");
        List<String> hologram = section.getStringList("hologramText");
        List<String> commands = section.getStringList("commands");

        PersonalNPC npc = new PersonalNPC(
                id,
                name,
                location,
                hologram,
                hologramLocation,
                commands
        );
        
        npcs.add(npc);
    }

    /**
     * This method loads all the PersonalNPC(s) in the config
     */
    private void loadNPCs() {
        for (String id : plugin.getPersonalConfig().getConfig().getKeys(false))
            loadNPC(id);
        
        plugin.getLogger().info("Loaded " + npcs.size() + " personal NPCs.");
    }
    
}
