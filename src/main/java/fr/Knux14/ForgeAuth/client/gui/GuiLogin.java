package fr.Knux14.ForgeAuth.client.gui;

import fr.Knux14.ForgeAuth.Auth;
import fr.Knux14.ForgeAuth.Vars;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import fr.Knux14.ForgeAuth.network.Packet250CustomPayload;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class GuiLogin extends GuiScreen {
	private GuiTextField textField;
	private GuiTextField passField;
	private GuiCheckBox remember;
	private final String label;
	private File saveFile;

	public GuiLogin(String registerOrLogin) {
		label = registerOrLogin;
		saveFile = new File(Vars.userfolder.getParent(), "ForgeAuthLastPass");
	}

	public void updateScreen() {
		if (label.equals("Register"))
			textField.updateCursorCounter();
		else
			passField.updateCursorCounter();
	}

	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		buttonList.clear();
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 12,
				label));
		buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12,
				I18n.format("gui.cancel")));
		buttonList.add(this.remember = new GuiCheckBox(2, width / 2 + 84, 85));
		String savedPass = "";
		if (saveFile.exists()) {
			savedPass = Auth.readFile(saveFile);
		}
		if (label.equals("Register")) {
			textField = new GuiTextField(fontRendererObj, width / 2 - 100, 60,
					200, 20);
			textField.setFocused(true);
			textField.setText(savedPass != null ? savedPass : "");
			remember.setChecked((savedPass != null) && (!savedPass.isEmpty()));
		} else {
			if (Vars.debug)
				passField = new GuiTextField(fontRendererObj, width / 2 - 100, 60,
						200, 20);
			else
				passField = new GuiPasswordField(fontRendererObj, width / 2 - 100,
						60, 200, 20);
			passField.setFocused(true);
			passField.setText(savedPass != null ? savedPass : "");
			remember.setChecked((savedPass != null) && (!savedPass.isEmpty()));
		}
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.id == 0) {
			if (remember.isChecked()) {
				if (label.equals("Register"))
					Auth.saveFile(saveFile, textField.getText());
				else
					Auth.saveFile(saveFile, passField.getText());
			} else
				saveFile.delete();

			ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
			DataOutputStream outputStream = new DataOutputStream(bos);
			try {
				outputStream.writeUTF(label);
				if (label.equals("Register"))
					outputStream.writeUTF(Auth.hash(textField.getText()));
				else
					outputStream.writeUTF(Auth.hash(passField.getText()));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.data = bos.toByteArray();
			packet.length = bos.size();
			Auth.network.sendToServer(packet);
			mc.displayGuiScreen((GuiScreen) null);
			mc.setIngameFocus();
		} else if (par1GuiButton.id == 1) {
			mc.theWorld.sendQuittingDisconnectingPacket();
		} else if (par1GuiButton.id == 2) {
			GuiCheckBox box = (GuiCheckBox) par1GuiButton;
			box.setChecked(!box.isChecked());
		}
	}

	protected void keyTyped(char par1, int par2) {
		if (label.equals("Register")) {
			textField.textboxKeyTyped(par1, par2);
			((GuiButton) buttonList.get(0)).enabled = (textField.getText()
					.trim().length() > 0);
		} else {
			passField.textboxKeyTyped(par1, par2);
			((GuiButton) buttonList.get(0)).enabled = (passField.getText()
					.trim().length() > 0);
		}
		if ((par2 == 28) || (par2 == 156)) {
			actionPerformed((GuiButton) buttonList.get(1));
		}
	}

	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		if (label.equals("Register"))
			textField.mouseClicked(par1, par2, par3);
		else
			passField.mouseClicked(par1, par2, par3);
	}

	public void drawScreen(int par1, int par2, float par3) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, label, width / 2, 20, 16777215);
		drawString(fontRendererObj,
				"This server is asking you to " + label.toLowerCase(),
				width / 2 - 100, 47, 10526880);
		drawString(fontRendererObj, "Remember ?", width / 2 - 100, 90, 10526880);
		if (label.equals("Register")) {
			textField.drawTextBox();
			((GuiButton) buttonList.get(0)).enabled = (textField.getText()
					.trim().length() > 0);
		} else {
			passField.drawTextBox();
			((GuiButton) buttonList.get(0)).enabled = (passField.getText()
					.trim().length() > 0);
		}
		super.drawScreen(par1, par2, par3);
	}
}