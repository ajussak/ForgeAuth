package fr.wascar.ForgeAuth.client.gui;

import fr.wascar.ForgeAuth.ForgeAuth;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import fr.wascar.ForgeAuth.network.Packet250CustomPayload;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class GuiLogin extends GuiScreen {
	private final String label;
	private GuiTextField passField;
	private GuiPasswordField passConfirmField;
	private GuiCheckBox remember;
	private File saveFile;
	private final int posYConfirm = 85;
	private final int posYRemember;

	public GuiLogin(String registerOrLogin) {
		label = registerOrLogin;
		saveFile = new File(ForgeAuth.userfolder.getParent(), "ForgeAuthLastPass");
		posYRemember = (label.equals("Register")) ? 25 : 0;
	}

	public void updateScreen() {
		passField.updateCursorCounter();
		if (label.equals("Register"))
			passConfirmField.updateCursorCounter();
	}

	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		buttonList.clear();
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 12, label));
		((GuiButton) buttonList.get(0)).enabled = false;
		buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12,
				I18n.format("gui.cancel")));
		buttonList.add(this.remember = new GuiCheckBox(2, width / 2 + 84, posYConfirm + posYRemember));
		String savedPass = "";
		if (saveFile.exists())
			savedPass = ForgeAuth.readFile(saveFile);
		if (ForgeAuth.debug)
			passField = new GuiTextField(3, fontRendererObj, width / 2 - 100, 60, 200, 20);
		else
			passField = new GuiPasswordField(3, fontRendererObj, width / 2 - 100, 60, 200, 20);
		passField.setFocused(true);

		if (label.equals("Register"))
			passConfirmField = new GuiPasswordField(4, fontRendererObj, width / 2 - 100, posYConfirm, 200, 20);
		else
		{
			if(savedPass != null)
			{
				passField.setText(savedPass);
				((GuiButton) buttonList.get(0)).enabled = true;
			}
			else
				passField.setText("");
		}

		remember.setChecked((savedPass != null) && (!savedPass.isEmpty()));
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.id == 0) {
			if (remember.isChecked())
					ForgeAuth.saveFile(saveFile, passField.getText());
			else if(saveFile.exists())
				if(saveFile.delete())
                    FMLLog.warning("Cannot to remove Last Pass");

			ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
			DataOutputStream outputStream = new DataOutputStream(bos);
			try {
				outputStream.writeUTF(label);
				outputStream.writeUTF(ForgeAuth.hash(passField.getText()));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.data = bos.toByteArray();
			packet.length = bos.size();
			ForgeAuth.network.sendToServer(packet);
			mc.displayGuiScreen(null);
			mc.setIngameFocus();
		} else if (par1GuiButton.id == 1) {
			mc.theWorld.sendQuittingDisconnectingPacket();
		} else if (par1GuiButton.id == 2) {
			GuiCheckBox box = (GuiCheckBox) par1GuiButton;
			box.setChecked(!box.isChecked());
		}
	}

	protected void keyTyped(char par1, int par2) {
		passField.textboxKeyTyped(par1, par2);
		if (label.equals("Register")) {
			passConfirmField.textboxKeyTyped(par1, par2);
			((GuiButton) buttonList.get(0)).enabled = (passField.getText().trim().length() > 0 &&
					passConfirmField.getText().equals(passField.getText()));
		}
		else
			((GuiButton) buttonList.get(0)).enabled = (passField.getText().trim().length() > 0);

		if ((par2 == 28) || (par2 == 156)) {
			actionPerformed((GuiButton) buttonList.get(1));
		}
	}

	protected void mouseClicked(int par1, int par2, int par3) {
		try {
			super.mouseClicked(par1, par2, par3);
		} catch (IOException e) {
			e.printStackTrace();
		}
		passField.mouseClicked(par1, par2, par3);
		if (label.equals("Register"))
			passConfirmField.mouseClicked(par1, par2, par3);
	}

	public void drawScreen(int par1, int par2, float par3) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, label, width / 2, 20, 16777215);
		drawString(fontRendererObj,
				"This server is asking you to " + label.toLowerCase(),
				width / 2 - 100, 47, 10526880);
		drawString(fontRendererObj, "Remember ?", width / 2 - 100, posYConfirm + posYRemember + 7, 10526880);
		passField.drawTextBox();
		if (label.equals("Register"))
			passConfirmField.drawTextBox();
		super.drawScreen(par1, par2, par3);
	}
}