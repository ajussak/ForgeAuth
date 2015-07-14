package fr.wascar.ForgeAuth.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ChatAllowedCharacters;

import org.lwjgl.opengl.GL11;

public class GuiPasswordField extends GuiTextField
{
  private final FontRenderer fontRenderer;
  private final int xPos;
  private final int yPos;
  private final int width;
  private final int height;
  private String text = "";
  private int maxStringLength = 32;
  private int cursorCounter;
  private boolean enableBackgroundDrawing = true;

  private boolean canLoseFocus = true;

  public boolean isFocused = false;

  private boolean isEnabled = true;
  private int field_73816_n = 0;
  private int cursorPosition = 0;

  private int selectionEnd = 0;

  public GuiPasswordField(int id, FontRenderer par1FontRenderer, int par2, int par3, int par4, int par5)
  {
	super(id, par1FontRenderer, par2, par3, par4, par5);
    fontRenderer = par1FontRenderer;
    xPos = par2;
    yPos = par3;
    width = par4;
    height = par5;
  }

  public void updateCursorCounter()
  {
    cursorCounter += 1;
  }

  public void setText(String par1Str)
  {
    if (par1Str.length() > maxStringLength)
    {
      text = par1Str.substring(0, maxStringLength);
    }
    else
    {
      text = par1Str;
    }

    setCursorPositionEnd();
  }

  public String getText()
  {
    return text;
  }

  public String getSelectedtext()
  {
    int var1 = cursorPosition < selectionEnd ? cursorPosition : selectionEnd;
    int var2 = cursorPosition < selectionEnd ? selectionEnd : cursorPosition;
    return text.substring(var1, var2);
  }

  public void writeText(String par1Str)
  {
    String var2 = "";
    String var3 = ChatAllowedCharacters.filterAllowedCharacters(par1Str);
    int var4 = cursorPosition < selectionEnd ? cursorPosition : selectionEnd;
    int var5 = cursorPosition < selectionEnd ? selectionEnd : cursorPosition;
    int var6 = maxStringLength - text.length() - (var4 - selectionEnd);

    if (text.length() > 0)
    {
      var2 = var2 + text.substring(0, var4);
    }
    
    int var8;
    
    if (var6 < var3.length())
    {
      var2 = var2 + var3.substring(0, var6);
      var8 = var6;
    }
    else
    {
      var2 = var2 + var3;
      var8 = var3.length();
    }

    if ((text.length() > 0) && (var5 < text.length()))
    {
      var2 = var2 + text.substring(var5);
    }

    text = var2;
    func_73784_d(var4 - selectionEnd + var8);
  }

  public void func_73779_a(int par1)
  {
    if (text.length() != 0)
    {
      if (selectionEnd != cursorPosition)
      {
        writeText("");
      }
      else
      {
        deleteFromCursor(getNthWordFromCursor(par1) - cursorPosition);
      }
    }
  }

  public void deleteFromCursor(int par1)
  {
    if (text.length() != 0)
    {
      if (selectionEnd != cursorPosition)
      {
        writeText("");
      }
      else
      {
        boolean var2 = par1 < 0;
        int var3 = var2 ? cursorPosition + par1 : cursorPosition;
        int var4 = var2 ? cursorPosition : cursorPosition + par1;
        String var5 = "";

        if (var3 >= 0)
        {
          var5 = text.substring(0, var3);
        }

        if (var4 < text.length())
        {
          var5 = var5 + text.substring(var4);
        }

        text = var5;

        if (var2)
        {
          func_73784_d(par1);
        }
      }
    }
  }

  public int getNthWordFromCursor(int par1)
  {
    return getNthWordFromPos(par1, getCursorPosition());
  }

  public int getNthWordFromPos(int par1, int par2)
  {
    return func_73798_a(par1, getCursorPosition(), true);
  }

  public int func_73798_a(int par1, int par2, boolean par3)
  {
    int var4 = par2;
    boolean var5 = par1 < 0;
    int var6 = Math.abs(par1);

    for (int var7 = 0; var7 < var6; var7++)
    {
      if (var5)
      {
        while ((par3) && (var4 > 0) && (text.charAt(var4 - 1) == ' '))
        {
          var4--;
        }

        while ((var4 > 0) && (text.charAt(var4 - 1) != ' '))
        {
          var4--;
        }

      }

      int var8 = text.length();
      var4 = text.indexOf(' ', var4);

      if (var4 == -1)
      {
        var4 = var8;
      }
      else
      {
        while ((par3) && (var4 < var8) && (text.charAt(var4) == ' '))
        {
          var4++;
        }
      }

    }

    return var4;
  }

  public void func_73784_d(int par1)
  {
    setCursorPosition(selectionEnd + par1);
  }

  public void setCursorPosition(int par1)
  {
    cursorPosition = par1;
    int var2 = text.length();

    if (cursorPosition < 0)
    {
      cursorPosition = 0;
    }

    if (cursorPosition > var2)
    {
      cursorPosition = var2;
    }

    func_73800_i(cursorPosition);
  }

  public void setCursorPositionZero()
  {
    setCursorPosition(0);
  }

  public void setCursorPositionEnd()
  {
    setCursorPosition(text.length());
  }

  public boolean textboxKeyTyped(char par1, int par2)
  {
    if ((isEnabled) && (isFocused))
    {
      switch (par1)
      {
      case '\001':
        setCursorPositionEnd();
        func_73800_i(0);
        return true;
      case '\003':
        GuiScreen.setClipboardString(getSelectedtext());
        return true;
      case '\026':
        writeText(GuiScreen.getClipboardString());
        return true;
      case '\030':
        GuiScreen.setClipboardString(getSelectedtext());
        writeText("");
        return true;
      }

      switch (par2)
      {
      case 14:
        if (GuiScreen.isCtrlKeyDown())
        {
          func_73779_a(-1);
        }
        else
        {
          deleteFromCursor(-1);
        }

        return true;
      case 199:
        if (GuiScreen.isShiftKeyDown())
        {
          func_73800_i(0);
        }
        else
        {
          setCursorPositionZero();
        }

        return true;
      case 203:
        if (GuiScreen.isShiftKeyDown())
        {
          if (GuiScreen.isCtrlKeyDown())
          {
            func_73800_i(getNthWordFromPos(-1, getSelectionEnd()));
          }
          else
          {
            func_73800_i(getSelectionEnd() - 1);
          }
        }
        else if (GuiScreen.isCtrlKeyDown())
        {
          setCursorPosition(getNthWordFromCursor(-1));
        }
        else
        {
          func_73784_d(-1);
        }

        return true;
      case 205:
        if (GuiScreen.isShiftKeyDown())
        {
          if (GuiScreen.isCtrlKeyDown())
          {
            func_73800_i(getNthWordFromPos(1, getSelectionEnd()));
          }
          else
          {
            func_73800_i(getSelectionEnd() + 1);
          }
        }
        else if (GuiScreen.isCtrlKeyDown())
        {
          setCursorPosition(getNthWordFromCursor(1));
        }
        else
        {
          func_73784_d(1);
        }

        return true;
      case 207:
        if (GuiScreen.isShiftKeyDown())
        {
          func_73800_i(text.length());
        }
        else
        {
          setCursorPositionEnd();
        }

        return true;
      case 211:
        if (GuiScreen.isCtrlKeyDown())
        {
          func_73779_a(1);
        }
        else
        {
          deleteFromCursor(1);
        }

        return true;
      }

      if (ChatAllowedCharacters.isAllowedCharacter(par1))
      {
        writeText(Character.toString(par1));
        return true;
      }

      return false;
    }

    return false;
  }

  public void mouseClicked(int par1, int par2, int par3)
  {
    boolean var4 = (par1 >= xPos) && (par1 < xPos + width) && (par2 >= yPos) && (par2 < yPos + height);

    if (canLoseFocus)
    {
      setFocused((isEnabled) && (var4));
    }

    if ((isFocused) && (par3 == 0))
    {
      int var5 = par1 - xPos;

      if (enableBackgroundDrawing)
      {
        var5 -= 4;
      }

      String var6 = fontRenderer.trimStringToWidth(text.substring(field_73816_n), getWidth());
      setCursorPosition(fontRenderer.trimStringToWidth(var6, var5).length() + field_73816_n);
    }
  }

  public void drawTextBox()
  {
    if (func_73778_q())
    {
      if (getEnableBackgroundDrawing())
      {
        drawRect(xPos - 1, yPos - 1, xPos + width + 1, yPos + height + 1, -6250336);
        drawRect(xPos, yPos, xPos + width, yPos + height, -16777216);
      }

      int var1 = isEnabled ? 14737632 : 7368816;
      int var2 = cursorPosition - field_73816_n;
      int var3 = selectionEnd - field_73816_n;
      String var4 = fontRenderer.trimStringToWidth(text.substring(field_73816_n), getWidth());
      boolean var5 = (var2 >= 0) && (var2 <= var4.length());
      boolean var6 = (isFocused) && (cursorCounter / 6 % 2 == 0) && (var5);
      int var7 = enableBackgroundDrawing ? xPos + 4 : xPos;
      int var8 = enableBackgroundDrawing ? yPos + (height - 8) / 2 : yPos;
      int var9 = var7;

      if (var3 > var4.length())
      {
        var3 = var4.length();
      }

      if (var4.length() > 0)
      {
        var9 = fontRenderer.drawString(text.replaceAll("(?s).", "*"), var7, var8, var1);
      }

      boolean var13 = (cursorPosition < text.length()) || (text.length() >= getMaxStringLength());
      int var11 = var9;

      if (!var5)
      {
        var11 = var2 > 0 ? var7 + width : var7;
      }
      else if (var13)
      {
        var11 = var9 - 1;
        var9--;
      }

      if ((var4.length() > 0) && (var5) && (var2 < var4.length()))
      {
        fontRenderer.drawString(var4.substring(var2), var9, var8, var1);
      }

      if (var6)
      {
        if (var13)
        {
          Gui.drawRect(var11, var8 - 1, var11 + 1, var8 + 1 + fontRenderer.FONT_HEIGHT, -3092272);
        }
        else
        {
          fontRenderer.drawString("_", var11, var8, var1);
        }
      }

      if (var3 != var2)
      {
        int var12 = var7 + fontRenderer.getStringWidth(var4.substring(0, var3));
        drawCursorVertical(var11, var8 - 1, var12 - 1, var8 + 1 + fontRenderer.FONT_HEIGHT);
      }
    }
  }

  private void drawCursorVertical(int par1, int par2, int par3, int par4)
  {
    if (par1 < par3)
    {
      int var5 = par1;
      par1 = par3;
      par3 = var5;
    }

    if (par2 < par4)
    {
      int var5 = par2;
      par2 = par4;
      par4 = var5;
    }

    Tessellator var6 = Tessellator.getInstance();
    WorldRenderer wr = var6.getWorldRenderer();
    GL11.glColor4f(0.0F, 0.0F, 255.0F, 255.0F);
    GL11.glDisable(3553);
    GL11.glEnable(3058);
    GL11.glLogicOp(5387);
    wr.startDrawingQuads();
    wr.addVertex(par1, par4, 0.0D);
    wr.addVertex(par3, par4, 0.0D);
    wr.addVertex(par3, par2, 0.0D);
    wr.addVertex(par1, par2, 0.0D);
    var6.draw();
    GL11.glDisable(3058);
    GL11.glEnable(3553);
  }

  public void setMaxStringLength(int par1)
  {
    maxStringLength = par1;

    if (text.length() > par1)
    {
      text = text.substring(0, par1);
    }
  }

  public int getMaxStringLength()
  {
    return maxStringLength;
  }

  public int getCursorPosition()
  {
    return cursorPosition;
  }

  public boolean getEnableBackgroundDrawing()
  {
    return enableBackgroundDrawing;
  }

  public void setEnableBackgroundDrawing(boolean par1)
  {
    enableBackgroundDrawing = par1;
  }

  public void setFocused(boolean par1)
  {
    if ((par1) && (!isFocused))
    {
      cursorCounter = 0;
    }

    isFocused = par1;
  }

  public boolean isFocused()
  {
    return isFocused;
  }

  public int getSelectionEnd()
  {
    return selectionEnd;
  }

  public int getWidth()
  {
    return getEnableBackgroundDrawing() ? width - 8 : width;
  }

  public void func_73800_i(int par1)
  {
    int var2 = text.length();

    if (par1 > var2)
    {
      par1 = var2;
    }

    if (par1 < 0)
    {
      par1 = 0;
    }

    selectionEnd = par1;

    if (fontRenderer != null)
    {
      if (field_73816_n > var2)
      {
        field_73816_n = var2;
      }

      int var3 = getWidth();
      String var4 = fontRenderer.trimStringToWidth(text.substring(field_73816_n), var3);
      int var5 = var4.length() + field_73816_n;

      if (par1 == field_73816_n)
      {
        field_73816_n -= fontRenderer.trimStringToWidth(text, var3, true).length();
      }

      if (par1 > var5)
      {
        field_73816_n += par1 - var5;
      }
      else if (par1 <= field_73816_n)
      {
        field_73816_n -= field_73816_n - par1;
      }

      if (field_73816_n < 0)
      {
        field_73816_n = 0;
      }

      if (field_73816_n > var2)
      {
        field_73816_n = var2;
      }
    }
  }

  public void setCanLoseFocus(boolean par1)
  {
    canLoseFocus = par1;
  }

  public boolean func_73778_q()
  {
    return true;
  }

}