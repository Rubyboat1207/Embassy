package nebulous.embassy;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Executors implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("embassy")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;

                Location loc = player.getLocation();
                World world = loc.getWorld();

                if(args[0].equalsIgnoreCase("create")) {
                    if(args.length != 2) {
                        player.sendMessage(ChatColor.RED + "Usage: /embassy create <name>");
                        return false;
                    }
                    int return_code = EmbassySaveData.AddEmbassy(player, args[1], loc);
                    if(return_code == 1) {
                        player.sendMessage(ChatColor.GREEN + "Embassy " + args[1] +  " created");
                    }else if(return_code == 2) {
                        player.sendMessage(ChatColor.YELLOW + "Overrode existing embassy");
                    }else if(return_code == -1) {
                        player.sendMessage(ChatColor.RED + "Embassy already exists");
                    }else if(return_code == -3) {
                        player.sendMessage(ChatColor.RED + "Invalid embassy");
                    }else if(return_code == -4) {
                        player.sendMessage(ChatColor.RED + "You do not own this embassy");
                    }else if(return_code == -5) {
                        player.sendMessage(ChatColor.RED + "An embassy already exists here");
                    }

                }else if(args[0].equalsIgnoreCase("save")) {
                    EmbassySaveData.Save();
                    player.sendMessage(ChatColor.GREEN + "Saved");
                }else if(args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("forcetp")) {
                    if(args.length <= 1) {
                        player.sendMessage(ChatColor.RED + "Usage: /embassy tp <name>");
                        return false;
                    }
                    Embassy.teleportToEmbassy(player, args[1], args[0].equalsIgnoreCase("forcetp"));
                }else if(args[0].equalsIgnoreCase("list")) {
                    player.sendMessage(ChatColor.GREEN + "Embassies:");
                    for (String name : EmbassySaveData.INSTANCE.embassies.keySet()) {
                        player.sendMessage((Main.embassy.isValid(EmbassySaveData.INSTANCE.embassies.get(name).toLocation()) ? ChatColor.GREEN : Main.embassy.hasImportantBlocks(EmbassySaveData.INSTANCE.embassies.get(name).toLocation()) ? ChatColor.YELLOW : ChatColor.RED) + name);
                    }
                }else if(args[0].equalsIgnoreCase("remove")) {
                    if (args.length <= 1) {
                        player.sendMessage(ChatColor.RED + "Usage: /embassy remove <name>");
                        return false;
                    }
                    Location embassy = EmbassySaveData.INSTANCE.embassies.get(args[1]).toLocation();
                    if(EmbassySaveData.getOwner(embassy).equals(player.getUniqueId())) {
                        EmbassySaveData.RemoveEmbassy(args[1]);
                        player.sendMessage(ChatColor.GREEN + "Removed embassy " + args[1]);
                    }else if(player.hasPermission("embassy.admin")) {
                        EmbassySaveData.RemoveEmbassy(args[1]);
                        player.sendMessage(ChatColor.GREEN + "Removed embassy " + args[1]);
                    }else{
                        player.sendMessage(ChatColor.RED + "You do not own this embassy");
                    }
                }
            }

        }
        return false;
    }
}
