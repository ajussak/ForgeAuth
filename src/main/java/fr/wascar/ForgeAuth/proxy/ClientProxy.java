package fr.wascar.ForgeAuth.proxy;

import fr.wascar.ForgeAuth.ForgeAuth;
import fr.wascar.ForgeAuth.network.Packet250CustomPayload;
import fr.wascar.ForgeAuth.network.client.CustomPayloadClientHandler;
import net.minecraftforge.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {

    public void registerRenderers()
   {
       ForgeAuth.network.registerMessage(CustomPayloadClientHandler.class, Packet250CustomPayload.class, 0, Side.CLIENT);
   }

}