package it.xquickglare.timednpc.commands;

import it.xquickglare.timednpc.utils.Command;
import it.xquickglare.timednpc.utils.NumberUtil;
import it.xquickglare.timednpc.utils.TimeUtil;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetTimedNPCCommand extends Command {
    
    public SetTimedNPCCommand() {
        super("timednpc.settimed", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        // /settimed <id> <time> <NPCs>
        
        if (args.length < 4) {
            plugin.getMessages().getMessage("commands.setTimedNPC.usage").format().send(player);
            return;
        }
        
        String id = args[0];
        if (plugin.getTimedNPCManager().existNPC(id)) {
            plugin.getMessages().getMessage("commands.setTimedNPC.alreadyExist").format().send(player);
            return;
        }
        
        String time = args[1];
        long parsedTime = TimeUtil.toMillis(time);
        if (parsedTime == 0) {
            plugin.getMessages().getMessage("commands.setTimedNPC.noValidTime").format().send(player);
            return;
        }

        NPCRegistry registry = CitizensAPI.getNPCRegistry();
        List<Integer> npcs = new ArrayList<>();
        for (int i = 2; i < args.length; i++) {
            Integer npcID = NumberUtil.parseInteger(args[i]);
            if (npcID == null) {
                plugin.getMessages().getMessage("commands.setTimedNPC.noValidNPCId").format().send(player);
                return;
            }
            
            if (registry.getById(npcID) == null) {
                plugin.getMessages().getMessage("commands.setTimedNPC.npcNotFound").format().addPlaceholder("npcID", npcID).send(player);
                return;
            }
            
            npcs.add(npcID);
        }
        
        ConfigurationSection section = plugin.getNpcConfig().getConfig().createSection(id);
        section.set("NPCs", npcs);
        section.set("changeEvery", time);
        plugin.getNpcConfig().setLocation(id + ".location", player.getLocation());
        section.set("lastChange", System.currentTimeMillis());
        section.set("lastNPC", 0);
        
        plugin.getNpcConfig().save();
        plugin.getTimedNPCManager().loadNPC(id);
        
        plugin.getMessages().getMessage("commands.setTimedNPC.created").format().send(player);
    }
    
}
