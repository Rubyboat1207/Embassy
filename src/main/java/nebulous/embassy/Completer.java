package nebulous.embassy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class Completer implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if(command.getName().equalsIgnoreCase("embassy")) {
            System.out.println("args.length: " + args.length);
            if(args.length == 0) {
                completions.add("create");
                completions.add("save");
                completions.add("tp");
                completions.add("forcetp");
                completions.add("list");
                completions.add("remove");
            }else if(args.length == 1) {
                if(args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("forcetp") || args[0].equalsIgnoreCase("remove")) {
                    completions.addAll(EmbassySaveData.INSTANCE.embassies.keySet());
                }
            }
        }
        return completions;
    }
}
