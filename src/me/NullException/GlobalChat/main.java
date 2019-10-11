package me.NullException.GlobalChat;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class main extends Plugin {
	public static main instance;
	private Connection connection;
	public String host, database, username, password;
	public int port;
	public void onDisable() {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onEnable() {
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
				cfg.set("Database.host", "localhost");
				cfg.set("Database.port", 3306);
				cfg.set("Database.database", "permissions");
				cfg.set("Database.username", "GlobalChat");
				cfg.set("Database.password", "password");
				cfg.set("ToggleChat", "true");
				for (ServerInfo server : getProxy().getServers().values()) {
					cfg.set("Servers." + server.getName(), server.getName());
				}
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, CfgFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		else {
			try {
				Configuration cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(CfgFile);
				if (!cfg.contains("Database")) {
					cfg.set("Database.host", "localhost");
					cfg.set("Database.port", 3306);
					cfg.set("Database.database", "permissions");
					cfg.set("Database.username", "GlobalChat");
					cfg.set("Database.password", "password");
					ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, CfgFile);
				}
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.getProxy().getScheduler().schedule(this, new Runnable() {

			@Override
			public void run() {
				if(getConnection() != null) {
					try {
						getConnection().close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				mysqlSetup();
			}
			
		}
		, 0, 1, TimeUnit.HOURS);
	}

	public void mysqlSetup() {
		File CfgFile = new File(main.instance.getDataFolder(), "config.yml");
		Configuration cfg;
		try {
			cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(CfgFile);
			host = cfg.getString("Database.host");
			port = cfg.getInt("Database.port");
			database = cfg.getString("Database.database");
			username = cfg.getString("Database.username");
			password = cfg.getString("Database.password");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {

			synchronized (this) {
				if (getConnection() != null && !getConnection().isClosed())
					return;
				setConnection(
						DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database,
								this.username, this.password));
				System.out.println("SQL Connected!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("SQL might not be set up correctly please be sure to modify the config");
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
}
