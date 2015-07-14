package fr.wascar.ForgeAuth.network.server;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.wascar.ForgeAuth.ForgeAuth;
import fr.wascar.ForgeAuth.network.Packet250CustomPayload;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class CustomPayloadServerHandler implements IMessageHandler<Packet250CustomPayload, IMessage>
{
    @Override
    public IMessage onMessage(Packet250CustomPayload message, MessageContext ctx)
    {
        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(message.data));
        try {
            inputStream.readUTF();
            String pass = inputStream.readUTF();
            EntityPlayer ply = ctx.getServerHandler().playerEntity;
            if (ForgeAuth.hasPass(ply)) {
                if (ForgeAuth.checkPass(ply, pass)) {
                    ForgeAuth.players.remove(ply);
                    ForgeAuth.players.put(ply, true);
                    ply.addChatMessage(new ChatComponentText("You are logged in !"));
                } else {
                    EntityPlayerMP pl = ctx.getServerHandler().playerEntity;
                    pl.playerNetServerHandler.kickPlayerFromServer("Bad password");
                }
            } else {
                ForgeAuth.savePlayer(ply, pass);
                ForgeAuth.players.remove(ply);
                ForgeAuth.players.put(ply, true);
                ply.addChatMessage(new ChatComponentText("You are registred !"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
