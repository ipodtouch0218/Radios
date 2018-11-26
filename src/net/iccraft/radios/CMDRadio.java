package net.iccraft.radios;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.NoteBlockPlayerMain;

public class CMDRadio implements CommandExecutor {

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Only players can use this command, silly! You don't even have ears!");
			return true;
		}
		if (args.length < 1) {
			return false;
		}
		
		String subCmd = args[0].toLowerCase();
		switch (subCmd) {
		case "scan": {
			sender.sendMessage("§2Current Radio Stations:");
			StringBuilder list = new StringBuilder();
			
			for (RadioStation stations : RadiosCore.instance.getRadioStations()) {
				list.append(", ").append(stations.getName() + " (" + stations.getFrequency() + "MHz)");
			}
			if (list.length() < 4) return true;
			sender.sendMessage(list.toString().substring(2));
			return true;
		}
		case "tune": {
			if (args.length < 2) {
				sender.sendMessage("§cInvalid Usage: /radio tune <frequency/player>");
				return true;
			}
			RadioStation station = null;
			try {
				for (RadioStation stations : RadiosCore.instance.getRadioStations()) {
					if (stations.getListeners().keySet().contains(((Player) sender).getUniqueId())) {
						stations.removeListener((Player) sender);
					}
					if (Double.parseDouble(args[1]) == stations.getFrequency()) {
						station = stations;
					}
				}
				if (station == null) {
					sender.sendMessage("§c" + args[1] + " is not a valid Radio Station!");
					return true;
				}
			} catch (NumberFormatException e) {
				UUID target = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
				for (RadioStation stations : RadiosCore.instance.getRadioStations()) {
					if (stations.getListeners().keySet().contains(((Player) sender).getUniqueId())) {
						stations.removeListener((Player) sender);
					}
					if (stations.getListeners().keySet().contains(target)) {
						station = stations;
					}
				}
				if (station == null) {
					sender.sendMessage("§c" + args[1] + " is not listening to a Radio Station!");
					return true;
				}
			}
			station.addListener((Player) sender);
			sender.sendMessage("§aSuccessfully Joined Radio Station: " + station.getName());
			return true;
		}
		case "off": {
			for (RadioStation stations : RadiosCore.instance.getRadioStations()) {
				if (stations.getListeners().keySet().contains(((Player) sender).getUniqueId())) {
					stations.removeListener((Player) sender);
				}
			}
			sender.sendMessage("§aSuccessfully Disconnected from all Radio Stations.");
			return true;
		}
		case "volume": {
			if (args.length < 2) {
				sender.sendMessage("§cInvalid Usage: /radio volume <0-100>");
				return true;
			}
			try {
				byte vol = Byte.parseByte(args[1]);
				if (vol < 0 || vol > 100) {
					sender.sendMessage("§cInvalid Usage: /radio volume <0-100>");
					return true;
				}
				NoteBlockPlayerMain.setPlayerVolume((Player) sender, Byte.parseByte(args[1]));
				sender.sendMessage("§aSuccessfully set volume to " + Byte.parseByte(args[1]));
				return true;
			} catch (NumberFormatException e) {
				sender.sendMessage("§cInvalid Usage: /radio volume <0-100>");
			}
			return true;
		}
		case "disp": {
			for (RadioStation stations : RadiosCore.instance.getRadioStations()) {
				if (stations.getListeners().keySet().contains(((Player) sender).getUniqueId())) {
					stations.getListeners().put(((Player) sender).getUniqueId(), !stations.getListeners().get(((Player) sender).getUniqueId()));
					
					String newVal = stations.getListeners().get(((Player) sender).getUniqueId()) ? "Enabled" : "Disabled";
					sender.sendMessage("§aThe display is now " + newVal);
					return true;
				}
			}
			sender.sendMessage("§cYou are not listening to a Radio Station!");
			return true;
		}
		}
		return false;
	}
}
