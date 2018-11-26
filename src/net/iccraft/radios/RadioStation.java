package net.iccraft.radios;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.NBSDecoder;
import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.SongPlayer;

public class RadioStation {

	private NormalSongPlayer currentSong;
	private HashMap<String, Song> songs = new HashMap<>();
	private HashMap<UUID, Boolean> listeners = new HashMap<>();	
	private double frequency;
	private String name;
	
	public RadioStation(String name, double frequency, File[] songNames) {
		this.name = name;
		this.frequency = frequency;
		
		for (File song : songNames) {
			songs.put(song.getName().replace(".nbs", ""), NBSDecoder.parse(song));
		}
		
		Entry<String, Song> newSong = getRandomSong();
		setSong(newSong.getKey(), newSong.getValue());
	}
	
	public void addListener(Player player) {
		listeners.put(player.getUniqueId(), true);
		currentSong.addPlayer(player);
		currentSong.setPlaying(true);
	}
	

	public void removeListener(Player player) {
		listeners.remove(player.getUniqueId());
		currentSong.removePlayer(player);
	}
	
	public void setSong(String name, Song song) {
		if (currentSong != null) currentSong.destroy();
		
		currentSong = new NormalSongPlayer(name, song, this);
		currentSong.setAutoDestroy(false);
		
		for (UUID listener : listeners.keySet()) {
			Player player = Bukkit.getPlayer(listener);
			currentSong.addPlayer(player);
		}

		currentSong.setPlaying(true);
	}
	
	public SongPlayer getCurrentSong() { return currentSong; }
	public Collection<Song> getSongs() { return songs.values(); }
	public HashMap<UUID, Boolean> getListeners() { return listeners; }
	public double getFrequency() { return frequency; }
	public String getName() { return name; }

	public Entry<String, Song> getRandomSong() {
		int size = songs.entrySet().size();
		int i = 0;
		Entry<String, Song> song = null;
		int item = (int) (Math.random()*size);
		for (Entry<String, Song> entry : songs.entrySet()) {
		    if (i == item) {
		    	song = entry;
		    	break;
		    }
		    i++;
		}
		return song;
	}
}
