package me.NullException.GlobalChat;
import java.io.File;
import java.io.IOException;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

public class bungeelistener implements Listener {
	private String server;
	private Configuration cfg;
	private File CfgFile;

	@EventHandler
	public void onMessage(ChatEvent e) {
		if(e.getSender() instanceof ProxiedPlayer)
		{
			CfgFile = new File(main.instance.getDataFolder(),"config.yml");
			try {
				cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(CfgFile);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if(e.getMessage().startsWith("/")) return;

			e.setCancelled(true);
			ProxiedPlayer player = (ProxiedPlayer)e.getSender();
			String playerName = player.getDisplayName();
			main.instance.getLogger().info(playerName);
			String test = cfg.getString(player.getServer().getInfo().getName());
			test = test.replaceAll("&", "§");
			main.instance.getProxy().broadcast(new TextComponent(test + playerName + ": " + e.getMessage()));
		}

	}
	

}
