package ovh.fejker.SimpleRPG;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.*;
import java.util.Random;

public class PlayerHandler implements Listener {

    DatabaseHandler databaseHandler = new DatabaseHandler();

    public PlayerHandler() throws SQLException {
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        player.sendMessage("" + ChatColor.BLUE + "Mapa dostępna do pobrania na " + ChatColor.YELLOW + "fejker.ovh"); //send MOTD to player

        player.sendMessage("Przeszukiwanie bazy danych.");
        databaseHandler.onConnect(uuid, player);
    }
    @EventHandler
    public void onPlayerKillEntity (EntityDeathEvent event) throws SQLException {
        Random random = new Random();
        int playerScore;
        int level;
        int common;
        int uncommon;
        int rand;
        Entity entity = event.getEntity();
        String type = entity.getType().toString();
        if (event.getEntity().getKiller() instanceof Player) {
            Player player = event.getEntity().getKiller();
            player.sendMessage(event.getEntityType().toString());
            String uuid = player.getUniqueId().toString();
            int mobKilled;

            if(type.contains("SPIDER") || type.contains("ZOMBIE") || type.contains("CREEPER") || type.contains("SKELETON") || type.contains("BLAZE") || type.contains("ENDERMAN") || type.contains("PIGMAN")) {
                mobKilled = 1; //define rarity of mob killed
                rand = random.nextInt(4) + 1; //roll random exp number

                databaseHandler.getPlayerInfo(uuid, mobKilled);
                common = databaseHandler.common;
                playerScore = databaseHandler.playerScore;
                level = databaseHandler.level;

                common++;
                playerScore += rand;

                level = levelCheck(player, level, playerScore);

                databaseHandler.dataUpdater("UPDATE players SET common = " + common + ", score = " + playerScore + ", level = " + level + " WHERE uuid = \"" + uuid + "\"");

                player.sendMessage("Otrzymujesz " + rand + " punkt za zabicie " + entity.getName());


            } else if (type.contains("RAVAGER") || type.contains("PILLAGER") || type.contains("VINDICATOR") ||  type.contains("GUARDIAN")) {
                mobKilled = 2; //define rarity of mob killed
                rand = random.nextInt(10) + 10; //roll random exp number

                databaseHandler.getPlayerInfo(uuid, mobKilled); //pass info to get data
                uncommon = databaseHandler.uncommon;
                playerScore = databaseHandler.playerScore;
                level = databaseHandler.level;

                uncommon++;
                playerScore += rand;

                level = levelCheck(player, level, playerScore);
                databaseHandler.dataUpdater("UPDATE players SET uncommon = " + uncommon + ", score = " + playerScore + ", level = " + level + " WHERE uuid = \"" + uuid + "\"");

                player.sendMessage("Otrzymujesz " + rand + " punktów za zabicie " + entity.getName());

            } else {
                mobKilled = 3;
                //TODO unusual mobs
            }

        }
    }

    int levelCheck(Player player, int level, int score) {
        if(score > Math.pow(level + 1, 2) * 50) {
            level++;
            player.sendMessage("Twój poziom wzrósł do " + level + " !");
        }
        return level;
    }

}
