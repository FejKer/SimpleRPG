package ovh.fejker.SimpleRPG;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class Main extends JavaPlugin {

    @Override
    public void onEnable(){
        System.out.printf("FejKerUtils załadowano.");
        PluginManager pm = this.getServer().getPluginManager();
        try {
            pm.registerEvents(new PlayerHandler(), this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDisable(){

    }

}
