package nebulous.embassy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import jdk.jpackage.internal.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class EmbassySaveData {

    public static EmbassySaveData INSTANCE = new EmbassySaveData();
    HashMap<String, SerializedLocation> embassies = new HashMap<String, SerializedLocation>();
    HashMap<String, ArrayList<SerializedLocation>> embassyOwnership = new HashMap<String, ArrayList<SerializedLocation>>();

    public static int AddEmbassy(Player creator, String name, Location loc) {
        if(INSTANCE.embassies.containsValue(loc)) {
            return -1;
        }
        if(!Main.embassy.isValid(loc)) {
            return -3;
        }
        if(!INSTANCE.embassyOwnership.containsKey(creator.getUniqueId().toString())) {
            INSTANCE.embassyOwnership.put(creator.getUniqueId().toString(), new ArrayList<>());
        }
        for (SerializedLocation location : INSTANCE.embassies.values()) {
            if(location.equals(new SerializedLocation(loc))) {
                return -5;
            }
        }


        boolean override = INSTANCE.embassies.containsKey(name);
        int player_index = 0;
        if(override) {
            if(getOwner(loc) != creator) {
                return -4;
            }
        }

        INSTANCE.embassies.put(name, new SerializedLocation(loc));
        if(!INSTANCE.embassyOwnership.get(creator.getUniqueId().toString()).contains(new SerializedLocation(loc))) {
            INSTANCE.embassyOwnership.get(creator.getUniqueId().toString()).add(new SerializedLocation(loc));
        }
        return override ? 2 : 1;

    }

    public static Player getOwner(Location loc) {
        for(ArrayList<SerializedLocation> locs : INSTANCE.embassyOwnership.values()) {
            for(SerializedLocation sl : locs) {
                if(sl.equals(new SerializedLocation(loc))) {
                    return Bukkit.getPlayer(UUID.fromString(INSTANCE.embassyOwnership.keySet().stream().toList().get(0)));
                }
            }
        }
        return null;
    }

    public static void RemoveEmbassy(String name) {
        for (Iterator<Map.Entry<String, ArrayList<SerializedLocation>>> iter = INSTANCE.embassyOwnership.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry<String, ArrayList<SerializedLocation>> it = iter.next();
            if(it.getValue().contains(INSTANCE.embassies.get(name))) {
                it.getValue().remove(INSTANCE.embassies.get(name));
            }
        }
        INSTANCE.embassies.remove(name);
    }

    public static void Save() {
        String json = new GsonBuilder().setPrettyPrinting().create().toJson(INSTANCE);
        try {
            File file = new File(Main.instance.getDataFolder().getAbsolutePath(), "embassies.json");
            System.out.println(file.getAbsolutePath());
            if(!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                System.out.println("Created new embassies.json");
            }

            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void Load() {
        File file = new File(Main.instance.getDataFolder().getAbsolutePath(), "embassies.json");
        System.out.println("Loading embassies.json");
        if(!file.exists()) {
            System.out.println("File not found");
            Save();
            return;
        }
        try{
            String json = new String(Files.readAllBytes(file.toPath()));
            INSTANCE = new Gson().fromJson(json, EmbassySaveData.class);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class SerializedLocation {
        public double x;
        public double y;
        public double z;
        public String world;

        public SerializedLocation(Location loc) {
            x = Math.floor(loc.getX()) + 0.5;
            y = loc.getY();
            z = Math.floor(loc.getZ()) + 0.5;
            world = loc.getWorld().getName();
        }

        public Location toLocation() {
            return new Location(Bukkit.getWorld(world), x, y, z);
        }

        public boolean equals(SerializedLocation sl) {
            return sl.x == x && sl.y == y && sl.z == z && sl.world.equals(world);
        }
    }
}
