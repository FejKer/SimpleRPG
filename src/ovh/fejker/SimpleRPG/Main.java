package ovh.fejker.SimpleRPG;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class Main extends JavaPlugin {

    @Override
    public void onEnable(){
        PluginManager pm = this.getServer().getPluginManager();
        try {
            pm.registerEvents(new PlayerHandler(), this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseHandler databaseHandler;
        try {
            databaseHandler = new DatabaseHandler();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            databaseHandler.DatabaseCreate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void onDisable(){

    }

}
