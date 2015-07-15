package fr.wascar.ForgeAuth;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.wascar.ForgeAuth.command.Command;
import fr.wascar.ForgeAuth.proxy.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

@Mod(modid = ForgeAuth.MODID, name = ForgeAuth.NAME, version = ForgeAuth.VERSION)
public class ForgeAuth {

    public static final String MODID = "ForgeAuth";
    public static final String VERSION = "0.6.2";
    public static final String NAME = "ForgeAuth";
    public static File userfolder;
    public static boolean debug;
    public static boolean modEnabled;
    public static boolean useMYSQL;
    public static HashMap<String, String> mysql = new HashMap<String, String>();

    public static HashMap<EntityPlayer, Boolean> players = new HashMap<EntityPlayer, Boolean>();

    public static SimpleNetworkWrapper network;

    @Instance("ForgeAuth")
    public static ForgeAuth instance;

    @SidedProxy(clientSide = "fr.wascar.ForgeAuth.proxy.ClientProxy", serverSide = "fr.wascar.ForgeAuth.proxy.CommonProxy")
    public static CommonProxy proxy;
    public static Configuration config;

    public static String readPlayer(String player) {
        return readFile(new File(userfolder, player));
    }

    public static String readFile(File f) {
        try {
            InputStream ips = new FileInputStream(f);
            InputStreamReader stream = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(stream);
            String line = br.readLine();
            br.close();
            return line;
        } catch (Exception e) {
            print(e.getLocalizedMessage());
            return null;
        }
    }

    public static boolean hasPass(EntityPlayer pl) {
        String username = pl.getDisplayName();
        File playerFile = new File(userfolder, username);
        if (playerFile.exists()) {
            if (readPlayer(username).isEmpty()) {
                print("Player doesn't exists");
                return false;
            }
            print("Player exists");
            return true;
        }
        print("Player doesn't exists");
        return false;
    }

    public static boolean checkPass(EntityPlayer pl, String passwd) {
        String username = pl.getDisplayName();
        File playerFile = new File(userfolder, username);
        if (playerFile.exists()) {
            String savedPass = readPlayer(pl.getDisplayName());
            if (savedPass.equals(passwd)) {
                print("Password correct: ");
                print(passwd);
                print(savedPass);
                return true;
            }
            print("Password incorrect: ");
            print(passwd);
            print(savedPass);
        } else {
            ForgeAuth.print("Player doesn't exists");
        }
        return false;
    }

    public static boolean savePlayer(EntityPlayer pl, String pass) {
        ForgeAuth.print("Saving player password");
        return saveFile(new File(userfolder, pl.getDisplayName()), pass);
    }

    public static boolean saveFile(File f, String str) {
        try {
            if (!f.exists()) {
                if (!(f.createNewFile()))
                    throw new IOException("[" + ForgeAuth.MODID + "] Cannot to save player password");
            } else {
                if (!(f.delete() && f.createNewFile()))
                    throw new IOException("[" + ForgeAuth.MODID + "] Cannot to save player password");
            }
            FileWriter fw = new FileWriter(f);
            fw.write(str);
            fw.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String hash(String pass) {
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
        if (debug) {
            FMLLog.log(Level.DEBUG, s);
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        config = new Configuration(e.getSuggestedConfigurationFile());
        userfolder = new File(e.getSuggestedConfigurationFile().getParentFile(), "AuthPlayers");
        network = NetworkRegistry.INSTANCE.newSimpleChannel("AuthChan1");
        try {
            if ((e.getSide() == Side.SERVER) && !userfolder.exists())
                if (!userfolder.mkdir())
                    throw new IOException("[" + ForgeAuth.MODID + "] Cannot to create config folder");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void load(FMLInitializationEvent e) {
        proxy.registerRenderers();
        debug = config.get(Configuration.CATEGORY_GENERAL, "Debug", false).getBoolean();
        useMYSQL = config.get(Configuration.CATEGORY_GENERAL, "Use mysql", false).getBoolean();
        mysql.put("host", config.get(Configuration.CATEGORY_GENERAL, "MYSQL-Host", "localhost").getString());
        mysql.put("user", config.get(Configuration.CATEGORY_GENERAL, "MYSQL-User", "username").getString());
        mysql.put("pass", config.get(Configuration.CATEGORY_GENERAL, "MYSQL-Password", "psswd").getString());
        mysql.put("port", config.get(Configuration.CATEGORY_GENERAL, "MYSQL-Port", "3306").getString());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        if (config.hasChanged()) config.save();
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new Command());
    }

    @SideOnly(Side.SERVER)
    @EventHandler
    public void loadComplete(FMLLoadCompleteEvent event)
    {
        modEnabled = !MinecraftServer.getServer().isServerInOnlineMode();
    }
}