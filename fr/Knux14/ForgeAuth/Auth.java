package fr.Knux14.ForgeAuth;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.relauncher.Side;
import fr.Knux14.ForgeAuth.Proxy.CommonProxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import org.apache.commons.codec.binary.Hex;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.Configuration;

@Mod(modid=Vars.modid, name=Vars.name, version=Vars.version)
@NetworkMod(clientSideRequired=true, serverSideRequired=true, clientPacketHandlerSpec=@NetworkMod.SidedPacketHandler(channels={"AuthChan1"}, packetHandler=PacketHandlerClient.class), serverPacketHandlerSpec=@NetworkMod.SidedPacketHandler(channels={"AuthChan1"}, packetHandler=PacketHandlerServer.class))
public class Auth
{
  public static HashMap<EntityPlayer, Boolean> players = new HashMap();

  @Mod.Instance("ForgeAuth")
  public static Auth instance;

  @SidedProxy(clientSide="fr.Knux14.ForgeAuth.Proxy.ClientProxy", serverSide="fr.Knux14.ForgeAuth.Proxy.CommonProxy")
  public static CommonProxy proxy;
  public static Configuration config;
  
  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent e) { config = new Configuration(e.getSuggestedConfigurationFile());
    Vars.userfolder = new File(e.getSuggestedConfigurationFile().getParentFile(), "AuthPlayers");
    if ((e.getSide() == Side.SERVER) && 
      (!Vars.userfolder.exists())) Vars.userfolder.mkdir();
  }

  @Mod.EventHandler
  public void load(FMLInitializationEvent e)
  {
    proxy.registerRenderers();
    
  }

  @Mod.EventHandler
  public void postInit(FMLPostInitializationEvent e)
  {
  }

  @Mod.EventHandler
  public void serverStart(FMLServerStartingEvent event)
  {
    event.registerServerCommand(new Command());
  }

  public static String readPlayer(String player) {
    return readFile(new File(Vars.userfolder, player));
  }

  public static String readFile(File f) {
    try {
      InputStream ips = new FileInputStream(f);
      InputStreamReader ipsr = new InputStreamReader(ips);
      BufferedReader br = new BufferedReader(ipsr);
      String ligne = br.readLine();
      br.close();
      return ligne;
    }
    catch (Exception e) {
    	print(e.getLocalizedMessage());
        return null;
    }
  }

  public static boolean hasPass(EntityPlayer pl)
  {
    String username = pl.username;
    File playerFile = new File(Vars.userfolder, username);
    if (playerFile.exists()) {
      if (readPlayer(username).isEmpty()) {
    	  Auth.print("Player doesn't exists");
        return false;
      }
      Auth.print("Player exists");
      return true;
    }
    Auth.print("Plyer doesn't exists");
    return false;
  }

  public static boolean checkPass(EntityPlayer pl, String passwd)
  {
    String username = pl.username;
    File playerFile = new File(Vars.userfolder, username);
    if (playerFile.exists()) {
      String savedPass = readPlayer(pl.username);
      if (savedPass.equals(passwd)) {
    	  Auth.print("Password correct: ");
    	  Auth.print(passwd);
    	  Auth.print(savedPass);
    	  return true;
      }
      Auth.print("Password incorrect: ");
	  Auth.print(passwd);
	  Auth.print(savedPass);
    } else {
    	Auth.print("Player doesn't exists");
    }    
    return false;
  }

  public static boolean savePlayer(EntityPlayer pl, String pass)
  {
	  Auth.print("Saving player password");
	  return saveFile(new File(Vars.userfolder, pl.username), pass);
  }

  public static boolean saveFile(File f, String str) {
    try {
      if (f.exists()) f.delete();
      f.createNewFile();
      FileWriter fw = new FileWriter(f);
      fw.write(str);
      fw.close();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }return false;
  }
  
  public static String hash (String pass) {
	MessageDigest cr;
	try {
		cr = MessageDigest.getInstance("SHA-1");
	    cr.reset();
	    cr.update(pass.getBytes());
		return new String(Hex.encodeHex(cr.digest()));
	} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
		return null;
	}
  }
  
  public static void print(String s) {
	  if (Vars.debug) {
		  System.out.println(s);
	  }
  }
}