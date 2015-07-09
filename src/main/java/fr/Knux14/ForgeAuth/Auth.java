package fr.Knux14.ForgeAuth;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import fr.Knux14.ForgeAuth.proxy.CommonProxy;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import fr.Knux14.ForgeAuth.command.Command;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.codec.binary.Hex;
import net.minecraft.entity.player.EntityPlayer;

@Mod(modid=Vars.modid, name=Vars.name, version=Vars.version)
public class Auth
{
  public static HashMap<EntityPlayer, Boolean> players = new HashMap<EntityPlayer, Boolean>();

  public static SimpleNetworkWrapper network;

  @Instance("ForgeAuth")
  public static Auth instance;

  @SidedProxy(clientSide="fr.Knux14.ForgeAuth.proxy.ClientProxy", serverSide="fr.Knux14.ForgeAuth.proxy.CommonProxy")
  public static CommonProxy proxy;
  public static Configuration config;
  
  @EventHandler
  public void preInit(FMLPreInitializationEvent e) { config = new Configuration(e.getSuggestedConfigurationFile());
      Vars.userfolder = new File(e.getSuggestedConfigurationFile().getParentFile(), "AuthPlayers");
      network = NetworkRegistry.INSTANCE.newSimpleChannel("AuthChan1");
      try
      {
      if ((e.getSide() == Side.SERVER) && !Vars.userfolder.exists())
          if(!Vars.userfolder.mkdir())
            throw new IOException("[" + Vars.modid + "] Cannot to create config folder");
      }
      catch(Exception ex)
      {
          ex.printStackTrace();
      }
  }

  @EventHandler
  public void load(FMLInitializationEvent e)
  {
    proxy.registerRenderers();
    Vars.debug = config.get(Configuration.CATEGORY_GENERAL, "Debug", false).getBoolean(false);
    Vars.useMYSQL = config.get(Configuration.CATEGORY_GENERAL, "Use mysql", false).getBoolean(false);
    Vars.mysql.put("host", config.get(Configuration.CATEGORY_GENERAL, "MYSQL-Host", "localhost").getString());
    Vars.mysql.put("user", config.get(Configuration.CATEGORY_GENERAL, "MYSQL-User", "username").getString());
    Vars.mysql.put("pass", config.get(Configuration.CATEGORY_GENERAL, "MYSQL-Password", "psswd").getString());
    Vars.mysql.put("port", config.get(Configuration.CATEGORY_GENERAL, "MYSQL-Port", "3306").getString());
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent e)
  {
	  if (config.hasChanged()) config.save();
  }

  @EventHandler
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
    String username = pl.getUniqueID().toString();
    File playerFile = new File(Vars.userfolder, username);
    if (playerFile.exists()) {
      if (readPlayer(username).isEmpty()) {
    	  Auth.print("Player doesn't exists");
        return false;
      }
      Auth.print("Player exists");
      return true;
    }
    Auth.print("Player doesn't exists");
    return false;
  }

  public static boolean checkPass(EntityPlayer pl, String passwd)
  {
    String username = pl.getUniqueID().toString();
    File playerFile = new File(Vars.userfolder, username);
    if (playerFile.exists()) {
      String savedPass = readPlayer(pl.getUniqueID().toString());
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
	  return saveFile(new File(Vars.userfolder, pl.getUniqueID().toString()), pass);
  }

  public static boolean saveFile(File f, String str) {
    try {
      if (!f.exists())
      {
          if (!(f.createNewFile()))
              throw new IOException("[" + Vars.modid + "] Cannot to save player password");
      }
      else {
          if (!(f.delete() && f.createNewFile()))
              throw new IOException("[" + Vars.modid + "] Cannot to save player password");
      }
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
	}
    return "";
  }
  
  public static void print(String s) {
	  if (Vars.debug) {
		  System.out.println(s);
	  }
  }
}