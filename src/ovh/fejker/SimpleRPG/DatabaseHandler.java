package ovh.fejker.SimpleRPG;

import org.bukkit.entity.Player;

import java.sql.*;

public class DatabaseHandler {
    int common,playerScore,level,uncommon,unusual;
    public DatabaseHandler() throws SQLException {

    }

    String database_choice = "sql"; //TODO get choice from config file
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/rpg","mc","password");
    Statement statement = connection.createStatement();

    void DatabaseCreate() throws SQLException {
        if(database_choice.equals("sql")){
            statement.execute("CREATE DATABASE IF NOT EXISTS rpg;"); //TODO DEFAULT VALUES
            statement.execute("CREATE TABLE IF NOT EXISTS `rpg`.`players` ( `id` INT(255) NOT NULL AUTO_INCREMENT , `uuid` VARCHAR(255) NOT NULL , `password` VARCHAR(255) NULL DEFAULT NULL , `score` INT NOT NULL DEFAULT '0' , `level` INT NOT NULL DEFAULT '0' , `money` INT NOT NULL DEFAULT '0' , `items_owned` VARCHAR(2500) NULL DEFAULT NULL , `common` INT NOT NULL DEFAULT '0' , `uncommon` INT NOT NULL DEFAULT '0' , `unusual` INT NOT NULL DEFAULT '0' , `isRegistered` INT NOT NULL DEFAULT '0' , PRIMARY KEY (`id`)) ENGINE = InnoDB; ");
        } else if(database_choice.equals("local")){
            //TODO LOCAL DATABASE
        }
    }


    void onConnect(String uuid, Player player) throws SQLException {
        Boolean exists = false;
        ResultSet resultSet = statement.executeQuery("SELECT * FROM players");
        while(resultSet.next()) {
            if(uuid.equals(resultSet.getString(2))) {
                exists = true;
                player.sendMessage("Znaleziono użytkownika."); //TODO logging in
            }
        }
        if(!exists) {
            player.sendMessage("Zarejestruj sie uzywajac komendy /register haslo");
            statement.executeUpdate("INSERT INTO players (uuid) VALUES (\"" + uuid + "\")");
            player.sendMessage("Zarejestrowano."); //TODO registering
        }
    }

    public void getPlayerInfo(String uuid, int mobKilled) throws SQLException {
        ResultSet resultSet = statement.executeQuery("select * from players");
        switch(mobKilled){
            case 1:
                while(resultSet.next()) {
                    if(uuid.equals(resultSet.getString(2))) {
                        common = resultSet.getInt(8);
                        playerScore = resultSet.getInt(4);
                        level = resultSet.getInt(5);
                        break;
                    }
                }
            case 2:
                while(resultSet.next()) {
                    if(uuid.equals(resultSet.getString(2))) {
                        uncommon = resultSet.getInt(9);
                        playerScore = resultSet.getInt(4);
                        level = resultSet.getInt(5);
                        break;
                    }
                }
            case 3:
        }
    }
    void dataUpdater(String cmd) throws SQLException {
        statement.executeUpdate(cmd);
    }
}
