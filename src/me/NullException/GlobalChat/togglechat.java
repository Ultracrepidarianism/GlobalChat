package me.NullException.GlobalChat;

import java.io.File;
import java.io.IOException;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class togglechat extends Command {
	private Configuration cfg;
	private File CfgFile;
	public togglechat() {
		super("togglechat");
	}
	
	@Override
	public void execute(CommandSender commandSender, String[] args)
	{		 
		CfgFile = new File(main.instance.getDataFolder(), "config.yml");
		try {
			cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(CfgFile);
		} catch (IOException e) {
			System.out.println("The file doesn't exist !");
			e.printStackTrace();
			return;
		}
		cfg.set("ToggleChat", !cfg.getBoolean("ToggleChat"));
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, CfgFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
