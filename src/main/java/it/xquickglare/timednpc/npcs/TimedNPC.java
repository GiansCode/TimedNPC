package it.xquickglare.timednpc.npcs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Location;

@AllArgsConstructor @Getter
public class TimedNPC {
    
    private final String id;
    
    private final int[] npcs;
    private final long changeEvery;
    
    private final Location location;
    
    private long lastChange;
    private int lastNpc;

    /**
     * This method inits the TimedNPC
     */
    public void init() {
        NPCRegistry registry = CitizensAPI.getNPCRegistry();
        for (int npc : npcs)
            registry.getById(npc).despawn();
        
        registry.getById(npcs[lastNpc]).spawn(location);
    }

    /**
     * This method change the NPC to the next
     */
    public void changeNPC() {
        int oldNpc = npcs[lastNpc];
        
        lastNpc++;
        if (lastNpc >= npcs.length)
            lastNpc = 0;
        
        int newNpc = npcs[lastNpc];

        NPCRegistry registry = CitizensAPI.getNPCRegistry();
        registry.getById(oldNpc).despawn();
        registry.getById(newNpc).spawn(location);
        
        lastChange = System.currentTimeMillis();
    }

    /**
     * This method returns the time in millis that the TimedNPC needs to change
     * @return The milliseconds
     */
    public long getNextChange() {
        return changeEvery - (System.currentTimeMillis() - lastChange);
    }

    /**
     * This method checks if the TimedNPC has to change
     * @return True if it has to change
     */
    public boolean isToChange() {
        return getNextChange() <= 0;
    }
    
}
