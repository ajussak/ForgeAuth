package fr.Knux14.ForgeAuth.proxy;

import fr.Knux14.ForgeAuth.Auth;
import fr.Knux14.ForgeAuth.network.Packet250CustomPayload;
import fr.Knux14.ForgeAuth.network.client.CustomPayloadClientHandler;
import net.minecraftforge.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {

    public void registerRenderers()
   {
       Auth.network.registerMessage(CustomPayloadClientHandler.class, Packet250CustomPayload.class, 0, Side.CLIENT);
   }

}