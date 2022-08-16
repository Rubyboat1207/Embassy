package nebulous.embassy;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Embassy {
    public ArrayList<RequiredBlock> requiredBlocks = new ArrayList<RequiredBlock>();

    public Embassy(ArrayList<RequiredBlock> requiredBlocks) {
        this.requiredBlocks = requiredBlocks;
    }

    public static void teleportToEmbassy(Player player, String name, boolean force) {
        if(EmbassySaveData.INSTANCE.embassies.get(name) == null) {
            player.sendMessage(ChatColor.RED + "Embassy not found");
            return;
        }
        if(Main.embassy.isValid(EmbassySaveData.INSTANCE.embassies.get(name).toLocation())) {
            player.teleport(EmbassySaveData.INSTANCE.embassies.get(name).toLocation());
            player.sendMessage(ChatColor.GREEN + "Teleported to " + name + "'s embassy");
        }else if(!force) {
            player.sendMessage(ChatColor.RED + "Embassy was not properly constructed, use \"/embassy forcetp " + name + "\" to teleport anyway");
        }else{
            if(!Main.embassy.hasImportantBlocks(EmbassySaveData.INSTANCE.embassies.get(name).toLocation())) {
                player.sendMessage(ChatColor.YELLOW + "Embassy does not have any important blocks (eg. Diamond or Gold), canceling teleport");
                return;
            }
            player.teleport(EmbassySaveData.INSTANCE.embassies.get(name).toLocation());
            player.sendMessage(ChatColor.GREEN + "Teleported to " + name + "'s embassy dispite its improper construction");
        }
    }

    public static Embassy createDefaultEmbassy() {
        ArrayList<RequiredBlock> requiredBlocks = new ArrayList<RequiredBlock>();
        ArrayList<RequiredBlock> quarter = new ArrayList<RequiredBlock>();

        requiredBlocks.add(new RequiredBlock(true, new Vector(0, -1, 0), Material.DIAMOND_BLOCK));
        //Gold
        quarter.add(new RequiredBlock(true, new Vector(5, 3, 0), Material.GOLD_BLOCK));
        quarter.add(new RequiredBlock(true, new Vector(0, 3, 5), Material.GOLD_BLOCK));
        //Gold Holders
        quarter.add(new RequiredBlock(new Vector(5, 3, 1)));
        quarter.add(new RequiredBlock(new Vector(1, 3, 5)));
        //X-Pillar
        quarter.add(new RequiredBlock(new Vector(5, 2, 2)));
        quarter.add(new RequiredBlock(new Vector(5, 1, 2)));
        quarter.add(new RequiredBlock(new Vector(5, 0, 2)));
        quarter.add(new RequiredBlock(new Vector(5, -1, 2)));
        //Z-Pillar
        quarter.add(new RequiredBlock(new Vector(2, 2, 5)));
        quarter.add(new RequiredBlock(new Vector(2, 1, 5)));
        quarter.add(new RequiredBlock(new Vector(2, 0, 5)));
        quarter.add(new RequiredBlock(new Vector(2, -1, 5)));
        //Pillar Connectors
        quarter.add(new RequiredBlock(new Vector(3, -1, 4)));
        quarter.add(new RequiredBlock(new Vector(4, -1, 3)));
        //----STRUCTURE_VOID stuff
        //doorway
        quarter.add(new RequiredBlock(new Vector(5, 2, 0), Material.STRUCTURE_VOID));
        quarter.add(new RequiredBlock(new Vector(5, 1, 0), Material.STRUCTURE_VOID));
        quarter.add(new RequiredBlock(new Vector(5, 0, 0), Material.STRUCTURE_VOID));
        quarter.add(new RequiredBlock(new Vector(5, -1, 0), Material.STRUCTURE_VOID));
        quarter.add(new RequiredBlock(new Vector(5, 2, 1), Material.STRUCTURE_VOID));
        quarter.add(new RequiredBlock(new Vector(5, 1, 1), Material.STRUCTURE_VOID));
        quarter.add(new RequiredBlock(new Vector(5, 0, 1), Material.STRUCTURE_VOID));
        quarter.add(new RequiredBlock(new Vector(5, -1, 1), Material.STRUCTURE_VOID));
        //other doorway
        quarter.add(new RequiredBlock(new Vector(0, 2, 5), Material.STRUCTURE_VOID));
        quarter.add(new RequiredBlock(new Vector(0, 1, 5), Material.STRUCTURE_VOID));
        quarter.add(new RequiredBlock(new Vector(0, 0, 5), Material.STRUCTURE_VOID));
        quarter.add(new RequiredBlock(new Vector(0, -1, 5), Material.STRUCTURE_VOID));
        quarter.add(new RequiredBlock(new Vector(1, 2, 5), Material.STRUCTURE_VOID));
        quarter.add(new RequiredBlock(new Vector(1, 1, 5), Material.STRUCTURE_VOID));
        quarter.add(new RequiredBlock(new Vector(1, 0, 5), Material.STRUCTURE_VOID));
        quarter.add(new RequiredBlock(new Vector(1, -1, 5), Material.STRUCTURE_VOID));





        quarter.addAll(RequiredBlock.flipAll(quarter, RequiredBlock.FlipAxis.X));
        quarter.addAll(RequiredBlock.flipAll(quarter, RequiredBlock.FlipAxis.Y));

        RequiredBlock.removeDuplicates(quarter);
        requiredBlocks.addAll(quarter);
        //spawn
        requiredBlocks.add(new RequiredBlock(new Vector(0, 1, 0), Material.STRUCTURE_VOID));
        requiredBlocks.add(new RequiredBlock(new Vector(0, 0, 0), Material.STRUCTURE_VOID));
        return new Embassy(requiredBlocks);
    }

    public boolean isValid(Location origin) {
        for(RequiredBlock rb : requiredBlocks) {
            if(!rb.isValid(origin)) {
                return false;
            }
        }
        return true;
    }

    public boolean hasImportantBlocks(Location origin) {
        for(RequiredBlock rb : requiredBlocks) {
            if(!rb.important) {
                continue;
            }
            if(!rb.isValid(origin)) {
                return false;
            }
        }
        return true;
    }

    public void generateBaseEmbassy(World world, Location loc) {
        for(RequiredBlock rb : requiredBlocks) {
            System.out.println(rb.block);
            if(rb.block == null) {
                continue;
            }
            if(rb.anyBlock) {
                world.getBlockAt(loc.clone().add(rb.position)).setType(Material.DIRT);

            }else {
                world.getBlockAt(loc.clone().add(rb.position)).setType(rb.block);
            }
        }
    }
}
