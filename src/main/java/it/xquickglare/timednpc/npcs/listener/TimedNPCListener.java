package it.xquickglare.timednpc.npcs.listener;

import it.xquickglare.timednpc.TimedNPCPlugin;
import net.citizensnpcs.api.event.CitizensEnableEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TimedNPCListener implements Listener {
    
    private final TimedNPCPlugin plugin = TimedNPCPlugin.getInstance();
    
    @EventHandler
    public void onCitizensLoad(CitizensEnableEvent event) {
        plugin.getTimedNPCManager().loadNPCs();
    }
    
}
