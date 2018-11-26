package net.iccraft.radios;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.SongEndEvent;

public class RadioListener implements Listener {
	
	@EventHandler
	public void onSongEnd(SongEndEvent e) {
		for (RadioStation station : RadiosCore.instance.getRadioStations()) {
			if (station.getCurrentSong().equals(e.getSongPlayer())) {
				
				Bukkit.getScheduler().runTaskLater(RadiosCore.instance, new Runnable() {
					public void run() {
						Entry<String, Song> newSong = station.getRandomSong();
						station.setSong(newSong.getKey(), newSong.getValue());
					}
				}, 20*3);
				break;
			}
		}
	}
	
	
}
