package me.NullException.GlobalChat;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class main extends Plugin{
	public static main instance;
	public void onEnable()
	{
		instance = this;
		getProxy().getPluginManager().registerCommand(this, new togglechat());
        getProxy().getPluginManager().registerListener(this, new bungeelistener());
		if (!main.instance.getDataFolder().exists())
            main.instance.getDataFolder().mkdir();
		 
		File CfgFile = new File(main.instance.getDataFolder(), "config.yml");
		
		if (!CfgFile.exists()) {
			try {
				CfgFile.createNewFile();
				Configuration cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(CfgFile);
				cfg.set("ToggleChat", "true");
				for(ServerInfo server : getProxy().getServers().values())
				{
					cfg.set(server.getName(), server.getName());
				}
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, CfgFile);
			}
			catch (IOException e) {
				System.out.println("wat");
			}
		}
	}
	
}
