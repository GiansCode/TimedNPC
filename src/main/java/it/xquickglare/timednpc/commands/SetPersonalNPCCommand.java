package it.xquickglare.timednpc.commands;

import it.xquickglare.timednpc.utils.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SetPersonalNPCCommand extends Command {
    
    public SetPersonalNPCCommand() {
        super("timednpc.setpersonal", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        
        if (args.length == 0) {
            plugin.getMessages().getMessage("commands.setPersonalNPC.usage").format().send(player);
            return;
        }
        
        String id = args[0];
        if (plugin.getPersonalNPCManager().existNPC(id)) {
            plugin.getMessages().getMessage("commands.setPersonalNPC.alreadyExist").format().send(player);
            return;
        }
        
        ConfigurationSection section = plugin.getPersonalConfig().getConfig().createSection(id);
        section.set("name", "&cNPC Name");
        plugin.getPersonalConfig().setLocation(id + ".location", player.getLocation());
        plugin.getPersonalConfig().setLocation(id + ".hologramLocation", player.getLocation().clone().add(0, 3, 0));
        section.set("hologramText", Arrays.asList("&6Test Hologram", "&6Hi %player_name%"));
        section.set("commands", Arrays.asList("[PLAYER] me I''ve clicked an NPC", "[CONSOLE] say %player% clicked an NPC"));
        
        plugin.getPersonalConfig().save();
        
        plugin.getPersonalNPCManager().loadNPC(id);
        plugin.getMessages().getMessage("commands.setPersonalNPC.created").format().send(player);
    }
    
}
