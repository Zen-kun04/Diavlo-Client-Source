/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ 
/*    */ public class GuiMemoryErrorScreen
/*    */   extends GuiScreen
/*    */ {
/*    */   public void initGui() {
/* 10 */     this.buttonList.clear();
/* 11 */     this.buttonList.add(new GuiOptionButton(0, this.width / 2 - 155, this.height / 4 + 120 + 12, I18n.format("gui.toTitle", new Object[0])));
/* 12 */     this.buttonList.add(new GuiOptionButton(1, this.width / 2 - 155 + 160, this.height / 4 + 120 + 12, I18n.format("menu.quit", new Object[0])));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void actionPerformed(GuiButton button) throws IOException {
/* 17 */     if (button.id == 0) {
/*    */       
/* 19 */       this.mc.displayGuiScreen(new GuiMainMenu());
/*    */     }
/* 21 */     else if (button.id == 1) {
/*    */       
/* 23 */       this.mc.shutdown();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void keyTyped(char typedChar, int keyCode) throws IOException {}
/*    */ 
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 33 */     drawDefaultBackground();
/* 34 */     drawCenteredString(this.fontRendererObj, "Out of memory!", this.width / 2, this.height / 4 - 60 + 20, 16777215);
/* 35 */     drawString(this.fontRendererObj, "Minecraft has run out of memory.", this.width / 2 - 140, this.height / 4 - 60 + 60 + 0, 10526880);
/* 36 */     drawString(this.fontRendererObj, "This could be caused by a bug in the game or by the", this.width / 2 - 140, this.height / 4 - 60 + 60 + 18, 10526880);
/* 37 */     drawString(this.fontRendererObj, "Java Virtual Machine not being allocated enough", this.width / 2 - 140, this.height / 4 - 60 + 60 + 27, 10526880);
/* 38 */     drawString(this.fontRendererObj, "memory.", this.width / 2 - 140, this.height / 4 - 60 + 60 + 36, 10526880);
/* 39 */     drawString(this.fontRendererObj, "To prevent level corruption, the current game has quit.", this.width / 2 - 140, this.height / 4 - 60 + 60 + 54, 10526880);
/* 40 */     drawString(this.fontRendererObj, "We've tried to free up enough memory to let you go back to", this.width / 2 - 140, this.height / 4 - 60 + 60 + 63, 10526880);
/* 41 */     drawString(this.fontRendererObj, "the main menu and back to playing, but this may not have worked.", this.width / 2 - 140, this.height / 4 - 60 + 60 + 72, 10526880);
/* 42 */     drawString(this.fontRendererObj, "Please restart the game if you see this message again.", this.width / 2 - 140, this.height / 4 - 60 + 60 + 81, 10526880);
/* 43 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiMemoryErrorScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */