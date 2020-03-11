package io.alerium.timednpc.npcs;

import io.alerium.timednpc.TimedNPCPlugin;
import io.alerium.timednpc.utils.TimeUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class TimedPlaceholder extends PlaceholderExpansion {
    
    private final TimedNPCPlugin plugin = TimedNPCPlugin.getInstance();
    
    @Override
    public String getIdentifier() {
        return "timednpc";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (!params.startsWith("time_"))
            return null;
        
        String id = params.split("_")[1];
        TimedNPC npc = plugin.getTimedNPCManager().getNPC(id);
        if (npc == null)
            return null;
        
        return TimeUtil.toFormatTime(npc.getNextChange());
    }
}
