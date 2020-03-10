package it.xquickglare.timednpc.utils;

import it.xquickglare.timednpc.TimedNPCPlugin;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public abstract class Command implements CommandExecutor {

    protected final TimedNPCPlugin plugin = TimedNPCPlugin.getInstance();

    private final String permission;
    private final boolean onlyPlayer;

    /**
     * This method is executed when the Command has all the requisites to do it
     * @param sender The CommandSender
     * @param args The arguments of the Command
     */
    public abstract void execute(CommandSender sender, String[] args);

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player) && onlyPlayer)
            return true;

        if (!sender.hasPermission(permission)) {
            plugin.getMessages().getMessage("commands.noPermission").format().send(sender);
            return true;
        }

        execute(sender, args);
        return true;
    }
    
}
