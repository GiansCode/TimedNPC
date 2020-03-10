package it.xquickglare.timednpc.personal.listener;

import com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent;
import it.xquickglare.timednpc.TimedNPCPlugin;
import it.xquickglare.timednpc.personal.PersonalNPC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PersonalNPCListener implements Listener {
    
    private final TimedNPCPlugin plugin = TimedNPCPlugin.getInstance();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        for (PersonalNPC npc : plugin.getPersonalNPCManager().getNpcs())
            npc.despawn(event.getPlayer());
    }
    
    @EventHandler
    public void onInteractEntity(PlayerUseUnknownEntityEvent event) {
        if (!event.isAttack())
            return;
        
        PersonalNPC npc = plugin.getPersonalNPCManager().getNPC(event.getEntityId());
        if (npc == null)
            return;
        
        npc.executeCommands(event.getPlayer());
    }
    
}
