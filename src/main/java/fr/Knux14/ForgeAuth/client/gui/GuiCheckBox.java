package fr.Knux14.ForgeAuth.client.gui;

import fr.Knux14.ForgeAuth.Vars;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiCheckBox extends GuiButton
{
  private boolean isChecked;
  protected static final ResourceLocation a = new ResourceLocation(Vars.modid.toLowerCase(), "textures/gui/check.png");

  public GuiCheckBox(int id, int xPosition, int yPosition, boolean initialState)
  {
    super(id, xPosition, yPosition, 18, 18, "");
    isChecked = initialState;
  }

  public GuiCheckBox(int id, int xPosition, int yPosition) {
    this(id, xPosition, yPosition, false);
  }

  public void setChecked(boolean checked) {
    isChecked = checked;
  }

  public boolean isChecked() { return isChecked; }

  public void drawButton(Minecraft par1Minecraft, int xMousePosition, int yMousePosition)
  {
    int spriteX = 0;
    int spriteY = isChecked ? 18 : 0;
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    Minecraft.getMinecraft().getTextureManager().bindTexture(a);
    drawTexturedModalRect(xPosition, yPosition, spriteX, spriteY, 17, 17);
  }
}