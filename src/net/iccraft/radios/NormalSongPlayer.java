package net.iccraft.radios;

import org.bukkit.entity.Player;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.xxmicloxx.NoteBlockAPI.Instrument;
import com.xxmicloxx.NoteBlockAPI.Layer;
import com.xxmicloxx.NoteBlockAPI.Note;
import com.xxmicloxx.NoteBlockAPI.NoteBlockPlayerMain;
import com.xxmicloxx.NoteBlockAPI.NotePitch;
import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.SongPlayer;

public class NormalSongPlayer extends SongPlayer {

	private RadioStation station;
	private String name;
	
	public NormalSongPlayer(String name, Song song, RadioStation station) {
		super(song);
		this.station = station;
		this.name = name;
	}

	@Override
	public void playTick(Player p, int tick) {
	    byte playerVolume = NoteBlockPlayerMain.getPlayerVolume(p);

	    for (Layer l : song.getLayerHashMap().values()) {
	        Note note = l.getNote(tick);
	        if (note == null) {
	            continue;
	        }
	        
	        if (station.getListeners().get(p.getUniqueId())) {
		        int currentTimeSec = (int) (tick/song.getSpeed());
		        int currentTimeMins = currentTimeSec/60;
		        int endTimeSec = (int) (song.getLength()/song.getSpeed());
		        int endTimeMins = endTimeSec/60;
		        
		        String currentSecStr = currentTimeSec%60 <= 9 ? "0" + currentTimeSec%60 : "" + currentTimeSec%60;
		        String endSecStr = endTimeSec%60 <= 9 ? "0" + endTimeSec%60 : "" + endTimeSec%60;
	        
	        	ActionBarAPI.sendActionBar(p, station.getName() + " (" + station.getFrequency() + "MHz) » " + name + " » " 
	        		+ currentTimeMins + ":" + currentSecStr + " - " + endTimeMins + ":" + endSecStr);
	        }
	        
	        p.playNote(p.getLocation(), Instrument.getBukkitInstrument(note.getInstrument()),
	                new org.bukkit.Note(note.getKey() - 33));
	        
	        if (Instrument.isCustomInstrument(note.getInstrument())){
	        	if (song.getCustomInstruments()[note.getInstrument() - Instrument.getCustomInstrumentFirstIndex()].getSound() != null){
	        		p.playSound(p.getLocation(),
	                        song.getCustomInstruments()[note.getInstrument() - Instrument.getCustomInstrumentFirstIndex()].getSound(),
	                        ((l.getVolume() * (int) volume * (int) playerVolume) / 1000000f),
	                        NotePitch.getPitch(note.getKey() - 33));
	        	}else {
	        		p.playSound(p.getLocation(),
	                        song.getCustomInstruments()[note.getInstrument() - Instrument.getCustomInstrumentFirstIndex()].getSoundfile(),
	                        ((l.getVolume() * (int) volume * (int) playerVolume) / 1000000f),
	                        NotePitch.getPitch(note.getKey() - 33));
	        	}
	        	
	        }else {
	        	p.playSound(p.getLocation(),
	                Instrument.getInstrument(note.getInstrument()),
	                ((l.getVolume() * (int) volume * (int) playerVolume) / 1000000f),
	                NotePitch.getPitch(note.getKey() - 33));
	        }
	        
	    }
	}

}
