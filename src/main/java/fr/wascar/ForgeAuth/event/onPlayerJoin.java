package fr.wascar.ForgeAuth.event;

import fr.wascar.ForgeAuth.ForgeAuth;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import fr.wascar.ForgeAuth.network.Packet250CustomPayload;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class onPlayerJoin {

	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
	{
		if (!ForgeAuth.modEnabled) {
			ForgeAuth.print("No login, mod disabled");
			ForgeAuth.players.put(event.player, true);
		}
        else {
            ForgeAuth.print("Asking player to register/Login");
            ForgeAuth.players.put(event.player, false);
            boolean hasPass = ForgeAuth.hasPass(event.player);
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
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ForgeAuth.network.sendTo(packet, (EntityPlayerMP) event.player);
        }
	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
		ForgeAuth.players.remove(event.player);
	}

}