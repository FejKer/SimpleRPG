package ovh.fejker.SimpleRPG;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.*;
import java.util.Random;

public class PlayerHandler implements Listener {

    String database_choice; //TODO get choice from config file

    Connection connection = DriverManager.getConnection("jdbc:mysql://addresshere","","");

    public PlayerHandler() throws SQLException {

    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) throws SQLException {
        Boolean exists = false;
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        player.sendMessage("" + ChatColor.BLUE + "Mapa dostępna do pobrania na " + ChatColor.YELLOW + "fejker.ovh"); //send MOTD to player

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from rpg"); //check if player exists in database

        player.sendMessage("Przeszukiwanie bazy danych.");
        while(resultSet.next()) {
            if(uuid.equals(resultSet.getString(2))) {
                exists = true;
                player.sendMessage("Znaleziono użytkownika."); //TODO logging in
            }
        }
        if(!exists) {
            statement.executeUpdate("INSERT INTO rpg (uuid) VALUES (\"" + uuid + "\")");
            player.sendMessage("Zarejestrowano."); //TODO registering
        }
        statement.close();
        resultSet.close();

    }
    @EventHandler
    public void onPlayerKillEntity (EntityDeathEvent event) throws SQLException {
        Random random = new Random();
        int playerScore = 0;
        int level = 0;
        int common = 0;
        int uncommon = 0;
        int rand;
        Entity entity = event.getEntity();
        String type = entity.getType().toString();
        if (event.getEntity().getKiller() instanceof Player) {
            Player player = event.getEntity().getKiller();
            player.sendMessage(event.getEntityType().toString());
            String uuid = player.getUniqueId().toString();


            if(type.contains("SPIDER") || type.contains("ZOMBIE") || type.contains("CREEPER") || type.contains("SKELETON") || type.contains("BLAZE") || type.contains("ENDERMAN") || type.contains("PIGMAN")) {

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select * from rpg");

                rand = random.nextInt(4) + 1;

                while(resultSet.next()) {
                    if(uuid.equals(resultSet.getString(2))) {
                        common = resultSet.getInt(3);
                        playerScore = resultSet.getInt(6);
                        level = resultSet.getInt(7);
                        break;
                    }
                }

                common++;
                playerScore += rand;

                levelCheck(player, level, playerScore);

                statement.executeUpdate("UPDATE rpg SET common_killed = " + common + ", score = " + playerScore + " WHERE uuid = \"" + uuid + "\"");

                player.sendMessage("Otrzymujesz " + rand + " punkt za zabicie " + entity.getName());

                statement.close();
                resultSet.close();

            } else if (type.contains("RAVAGER") || type.contains("PILLAGER") || type.contains("VINDICATOR") ||  type.contains("GUARDIAN")) {

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select * from rpg");

                rand = random.nextInt(10) + 10;

                while(resultSet.next()) {
                    if(uuid.equals(resultSet.getString(2))) {
                        uncommon = resultSet.getInt(4);
                        playerScore = resultSet.getInt(6);
                        level = resultSet.getInt(7);
                        break;
                    }
                }

                uncommon++;
                playerScore += rand;

                levelCheck(player, level, playerScore);
                statement.executeUpdate("UPDATE rpg SET uncommon_killed = " + uncommon + ", score = " + playerScore + " WHERE uuid = \"" + uuid + "\"");

                player.sendMessage("Otrzymujesz " + rand + " punktów za zabicie " + entity.getName());

                statement.close();
                resultSet.close();

            } else {
                //TODO unusual mobs
            }

        }
    }

    void levelCheck(Player player, int level, int score) {
        if(score > Math.pow(level + 1, 2) * 50) {
            level++;
            player.sendMessage("Twój poziom wzrósł do " + level + " !");
            //TODO update player level in database
        }
    }

}
