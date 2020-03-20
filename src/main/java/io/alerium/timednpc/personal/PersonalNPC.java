package io.alerium.timednpc.personal;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import io.alerium.timednpc.TimedNPCPlugin;
import io.alerium.timednpc.utils.NPCUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor @Getter
public class PersonalNPC {
    
    private final String id;
    
    private final String name;
    private final Location location;
    
    private final List<String> hologram;
    private final Location hologramLocation;
    
    private final List<String> commands;
    
    private final Map<UUID, Hologram> viewers = new HashMap<>();
    
    @Setter private int entityId = -1;
    
    public void spawn(Player player) {
        if (viewers.containsKey(player.getUniqueId()))
            return;
        
        NPCUtil.spawn(this, player);

        Hologram hologram = HologramsAPI.createHologram(TimedNPCPlugin.getInstance(), hologramLocation);
        hologram.getVisibilityManager().setVisibleByDefault(false);
        hologram.getVisibilityManager().showTo(player);

        for (String s : this.hologram)
            hologram.appendTextLine(PlaceholderAPI.setPlaceholders(player, s));
        
        viewers.put(player.getUniqueId(), hologram);
    }
    
    public void despawn(Player player) {
        if (!viewers.containsKey(player.getUniqueId()))
            return;
        
        NPCUtil.despawn(this, player);
        viewers.remove(player.getUniqueId()).delete();
    }
    
    public void executeCommands(Player player) {
        TimedNPCPlugin.getInstance().getActionUtil().executeActions(player, commands);
    }
    
}
