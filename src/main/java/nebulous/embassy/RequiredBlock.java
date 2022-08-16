package nebulous.embassy;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;


public class RequiredBlock {

    public RequiredBlock(Vector relative_pos, Material block) {
        anyBlock = false;
        position = relative_pos;
        this.block = block;
    }
    public RequiredBlock(boolean important, Vector relative_pos, Material block) {
        anyBlock = false;
        position = relative_pos;
        this.block = block;
        this.important = important;
    }
    public RequiredBlock(Vector relative_pos, Material block, boolean invert) {
        anyBlock = false;
        position = relative_pos;
        this.block = block;
        this.invert = invert;
    }

    RequiredBlock(Material block, boolean anyBlock, boolean invert, Vector position) {
        this.anyBlock = anyBlock;
        this.block = block;
        this.invert = invert;
        this.position = position;
    }

    public RequiredBlock(Vector relative_pos) {
        anyBlock = true;
        position = relative_pos;
    }

    Material block;
    boolean anyBlock;
    boolean invert;
    boolean important = false;
    public Vector position;

    public boolean isValid(Location origin) {
        if(anyBlock) {
            return origin.clone().add(position).getBlock().getType() != Material.AIR;
        }
        if(invert) {
            return origin.clone().add(position).getBlock().getType() != block;
        }
        if(block == Material.STRUCTURE_VOID) {
            return origin.clone().add(position).getBlock().getType() == Material.AIR;
        }
        return origin.clone().add(position).getBlock().getType() == block;
    }

    public static void removeDuplicates(List<RequiredBlock> list) {
        for(int i = 0; i < list.size(); i++) {
            for(int j = i + 1; j < list.size(); j++) {
                if(list.get(i).position.equals(list.get(j).position)) {
                    list.remove(j);
                }
            }
        }
    }

    public static RequiredBlock flip(RequiredBlock rb, FlipAxis axis) {
        return new RequiredBlock(rb.block, rb.anyBlock, rb.invert, rb.position.clone().multiply(new Vector(axis.x, 1, axis.y)));
    }

    public static List<RequiredBlock> flipAll(List<RequiredBlock> blocks, FlipAxis axis) {
        List<RequiredBlock> newBlocks = new ArrayList<RequiredBlock>();
        for(RequiredBlock rb : blocks) {
            newBlocks.add(RequiredBlock.flip(rb, axis));
        }
        return newBlocks;
    }

    public static class FlipAxis {
        public int x;
        public int y;

        public static final FlipAxis X = new FlipAxis(true, false);
        public static final FlipAxis Y = new FlipAxis(false, true);
        public static final FlipAxis XY = new FlipAxis(true, true);
        public static final FlipAxis NONE = new FlipAxis(false, false);

        public FlipAxis(boolean x, boolean y) {
            this.x = x ? -1 : 1;
            this.y = y ? -1 : 1;
        }
    }
}
