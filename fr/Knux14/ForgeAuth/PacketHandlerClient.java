package fr.Knux14.ForgeAuth;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import fr.Knux14.ForgeAuth.Gui.GuiLogin;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketHandlerClient
  implements IPacketHandler
{
  public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
  {
    DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
    try {
      String type = inputStream.readUTF();
      GuiLogin gl = new GuiLogin(type);
      Minecraft.getMinecraft().displayGuiScreen(gl);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}