package fr.Knux14.ForgeAuth;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.ChatMessageComponent;

public class PacketHandlerServer
  implements IPacketHandler
{
  public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
  {
    DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
    try {
      String type = inputStream.readUTF();
      String pass = inputStream.readUTF();
      EntityPlayer ply = (EntityPlayer)player;
      if (Auth.hasPass(ply)) {
        if (Auth.checkPass(ply, pass)) {
          Auth.players.remove(ply);
          Auth.players.put(ply, Boolean.valueOf(true));
          ply.sendChatToPlayer(ChatMessageComponent.createFromText("You are logged in !"));
        } else {
          EntityPlayerMP pl = (EntityPlayerMP)player;
          pl.playerNetServerHandler.kickPlayerFromServer("Bad password");
        }
      } else {
        Auth.savePlayer(ply, pass);
        Auth.players.remove(ply);
        Auth.players.put(ply, Boolean.valueOf(true));
        ply.sendChatToPlayer(ChatMessageComponent.createFromText("You are registred !"));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}