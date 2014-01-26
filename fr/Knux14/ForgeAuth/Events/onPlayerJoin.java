package fr.Knux14.ForgeAuth.Events;

import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import fr.Knux14.ForgeAuth.Auth;
import fr.Knux14.ForgeAuth.Vars;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;

public class onPlayerJoin implements IPlayerTracker {
	
	public void onPlayerLogin(EntityPlayer player) {
		if (Vars.modEnabled) {
			Auth.print("No login, mod disabled");
			Auth.players.put(player, Boolean.valueOf(true));
		} else {
			Auth.print("Asking player to register/Login");
			Auth.players.put(player, Boolean.valueOf(false));
			boolean hasPass = Auth.hasPass(player);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
			DataOutputStream outputStream = new DataOutputStream(bos);
			if (hasPass)
				try {
					outputStream.writeUTF("Login");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			else {
				try {
					outputStream.writeUTF("Register");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "AuthChan1";
			packet.data = bos.toByteArray();
			packet.length = bos.size();
			PacketDispatcher.sendPacketToPlayer(packet, (Player) player);
		}
	}

	public void onPlayerLogout(EntityPlayer player) {
		Auth.players.remove(player);
	}

	public void onPlayerChangedDimension(EntityPlayer player) {
	}

	public void onPlayerRespawn(EntityPlayer player) {
	}
	
}