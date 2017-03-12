package pl.mcplay.mcplayautograss;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.Set;

public class McPlayAutoGrass extends JavaPlugin {

    // w jednej klasie, bo po co 2. ?

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        if (!sender.hasPermission("mcplayautograss.cmd")) {
            sender.sendMessage(ChatColor.RED + "Nie masz uprawnien do tej komendy");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Uzyj: " + ChatColor.GRAY + "/" + command.getName() + " <radius>");
            return true;
        }

        int radius;
        try {
            radius = Integer.parseInt(args[0]);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "To nie jest liczba!");
            return true;
        }

        if (radius > 200) {
            sender.sendMessage(ChatColor.RED + " Hej hej, nie rozpedzaj sie tak z tym. Chcesz crasha?");
            return true;
        }

        Player player = (Player) sender;

        Location loc;
        if (command.getName().equals("autograss")) {
            loc = player.getLocation();
        } else {
            Block block = player.getTargetBlock((Set<Material>) null, 30);

            if (block == null) {
                sender.sendMessage(ChatColor.RED + "Nie wskazujesz bloku");
                return true;
            }

            loc = block.getLocation();
        }

        int c = parseLocation(loc, radius);
        sender.sendMessage(ChatColor.GREEN + "Trawa wysetowana (" + c + ")");
        return true;
    }

    private int parseLocation(Location loc, int radius) {
        int radius3 = radius * radius * radius;

        int counter = 0;

        // jak ktos chce zmienic ta petle na cos szybszego to smialo
        // mi sie nie chce :V
        for (int dx = -radius ; dx <= radius; dx++) {
            for (int dy = -radius ; dy <= radius; dy++) {
                for (int dz = -radius ; dz <= radius; dz++) {
                    if (dx * dx + dy * dy + dz * dz <= radius3) {

                        int typeId = loc.getWorld().getBlockTypeIdAt(loc.getBlockX() + dx, loc.getBlockY() + dy, loc.getBlockZ() + dz);
                        Block block = loc.getWorld().getBlockAt(loc.getBlockX() + dx, loc.getBlockY() + dy + 1, loc.getBlockZ() + dz);

                        if (typeId == Material.GRASS.getId() && block.getType() == Material.AIR) {
                            if (setRandomMaterial(block)) {
                                counter++;
                            }
                        }
                    }
                }
            }
        }
        return counter;
    }

    private Random random = new Random();

    private boolean setRandomMaterial(Block block) {
        double d = random.nextDouble();

        if (d < 0.08) {
            block.setTypeIdAndData(Material.LONG_GRASS.getId(), (byte) 2, false);
        } else if (d < 0.3) {
            block.setTypeIdAndData(Material.LONG_GRASS.getId(), (byte) 1, false);
        } else if (d < 0.32) {
            block.setType(Material.YELLOW_FLOWER);
        } else if (d < 0.34) {
            block.setType(Material.RED_ROSE);
        } else if (d < 0.36) {
            block.setTypeIdAndData(Material.RED_ROSE.getId(), (byte) 3, false);
        } else if (d < 0.38) {
            block.setTypeIdAndData(Material.RED_ROSE.getId(), (byte) 6, false);
        } else if (d < 0.38) {
            block.setTypeIdAndData(Material.RED_ROSE.getId(), (byte) 4, false);
        } else {
            return false;
        }

        return true;
    }
}
