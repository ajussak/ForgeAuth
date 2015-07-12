package fr.Knux14.ForgeAuth.event;

import fr.Knux14.ForgeAuth.Auth;
import fr.Knux14.ForgeAuth.Vars;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import fr.Knux14.ForgeAuth.network.Packet250CustomPayload;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class onPlayerJoin {

	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
	{
		if (Vars.modEnabled) {
			Auth.print("No login, mod disabled");
			Auth.players.put(event.player, true);
		}
        else {
            Auth.print("Asking player to register/Login");
            Auth.players.put(event.player, false);
            boolean hasPass = Auth.hasPass(event.player);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
            DataOutputStream outputStream = new DataOutputStream(bos);
            if (hasPass) {
                try {
                    outputStream.writeUTF("Login");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                try {
                    outputStream.writeUTF("Register");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            Packet250CustomPayload packet = new Packet250CustomPayload();
            packet.data = bos.toByteArray();
            packet.length = bos.size();
            Auth.network.sendTo(packet, (EntityPlayerMP) event.player);
        }
	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
		Auth.players.remove(event.player);
	}

}