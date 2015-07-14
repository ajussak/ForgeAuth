package fr.wascar.ForgeAuth.command;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import cpw.mods.fml.common.FMLLog;
import fr.wascar.ForgeAuth.ForgeAuth;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IChatComponent;


public class Command extends CommandBase {

	public String getCommandName() {
		return "authreset";
	}

	public String getCommandUsage(ICommandSender icommandsender) {
		return "/authreset <player>";
	}
	
    @Override
	public List getCommandAliases() {
		return Arrays.asList("ar", "authr");
	}
	
    @Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		if (astring.length < 1) {
			icommandsender.addChatMessage(IChatComponent.Serializer.func_150699_a(
                    "$4[ForgeAuth] Use: /authreset <user>"));
		} else {
            MinecraftServer server = MinecraftServer.getServer();
            File f = new File(ForgeAuth.userfolder, server.func_152358_ax().func_152655_a(astring[0]).getId().toString());
			if (f.exists())
            {
                if(!f.delete())
                {
                    icommandsender.addChatMessage(IChatComponent.Serializer.func_150699_a("§4[ForgeAuth]" +
                            "Error : Cannot to remove this player."));
                    FMLLog.warning("Cannot to remove " + astring[0]);
                }
                else
				    icommandsender.addChatMessage(IChatComponent.Serializer.func_150699_a("§4[ForgeAuth] " + astring[0]
                        + "'s password reset."));
			}
            else
            {
				icommandsender.addChatMessage(IChatComponent.Serializer.func_150699_a(
                        "§4[ForgeAuth] " + "This player doesn't exists."));
			}
		}
	}

    @Override
    public int getRequiredPermissionLevel()
    {
            return 4;
    }

}