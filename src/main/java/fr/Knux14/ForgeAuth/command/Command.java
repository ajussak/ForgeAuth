package fr.Knux14.ForgeAuth.command;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import fr.Knux14.ForgeAuth.Vars;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
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
	    	File f = new File(Vars.userfolder, astring[0]);
			if (f.exists()) {
				f.delete();
				icommandsender.addChatMessage(IChatComponent.Serializer.func_150699_a("§4[ForgeAuth] " + astring[0]
                                + "'s password reset."));
			} else {
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