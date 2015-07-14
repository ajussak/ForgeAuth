package fr.wascar.ForgeAuth.network.client;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.wascar.ForgeAuth.client.gui.GuiLogin;
import fr.wascar.ForgeAuth.network.Packet250CustomPayload;
import net.minecraft.client.Minecraft;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class CustomPayloadClientHandler implements IMessageHandler<Packet250CustomPayload, IMessage> {

    @Override
    public IMessage onMessage(Packet250CustomPayload message, MessageContext ctx)
    {
        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(message.data));
        try {
            String type = inputStream.readUTF();
            Minecraft.getMinecraft().displayGuiScreen(new GuiLogin(type));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
