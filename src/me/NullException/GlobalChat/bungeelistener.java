package me.NullException.GlobalChat;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

public class bungeelistener implements Listener {
	private Configuration cfg;
	private File CfgFile;
	public Statement statement;
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
			String playerPrefix = "";
			ResultSet results;
			try {
				PreparedStatement statement = main.instance.getConnection().prepareStatement("SELECT p.value "
						+ "FROM permissions_inheritance pi "
						+ "INNER JOIN permissions p "
						+ "ON pi.child = ? AND pi.parent = p.name AND p.permission = 'prefix'");
				statement.setString(1, player.getUniqueId().toString());
				results = statement.executeQuery();
				if(results.next())
				{
					playerPrefix = results.getString(1);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return;
			}
			playerPrefix = playerPrefix.replaceAll("&", "§");
			String globalPrefix = cfg.getString("Servers." + player.getServer().getInfo().getName());
			globalPrefix = globalPrefix.replaceAll("&", "§");
			main.instance.getProxy().broadcast(new TextComponent(globalPrefix + playerPrefix + playerName + ChatColor.RESET + ": " + e.getMessage()));
		}

	}
	
	

}
