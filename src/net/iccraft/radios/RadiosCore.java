package net.iccraft.radios;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class RadiosCore extends JavaPlugin {

	public static RadiosCore instance;
	private ArrayList<RadioStation> stationList = new ArrayList<>();
	
	public void onEnable() {
		saveDefaultConfig();
		instance = this;
		Bukkit.getPluginManager().registerEvents(new RadioListener(), this);
		setup();
		getCommand("radio").setExecutor(new CMDRadio());
	}
	
	public void onDisable() {
		instance = null;
		stationList.clear();
	}
	
	private void setup() {
		if (!getConfig().isConfigurationSection("stations")) {
			return;
		}
		ConfigurationSection stations = getConfig().getConfigurationSection("stations");
		for (String values : stations.getKeys(false)) {
			ConfigurationSection currentStation = stations.getConfigurationSection(values);
			String directory = currentStation.getString("song-directory");
			String name = currentStation.getString("name");
			double frequency = currentStation.getDouble("frequency");
			
			File folder = new File(getDataFolder() + directory);
			List<File> foundSongs = new ArrayList<>();
			for (File songs : folder.listFiles()) {
				if (songs.isFile() && songs.getName().endsWith(".nbs")) {
					foundSongs.add(songs);
				}
			}
			
			RadioStation station = new RadioStation(name, frequency, foundSongs.toArray(new File[]{}));
			stationList.add(station);
		}
	}
	
	public ArrayList<RadioStation> getRadioStations() { return stationList; }
	
}
