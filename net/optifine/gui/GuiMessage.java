/*    */ package net.optifine.gui;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.gui.GuiOptionButton;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import net.minecraft.src.Config;
/*    */ 
/*    */ public class GuiMessage
/*    */   extends GuiScreen
/*    */ {
/*    */   private GuiScreen parentScreen;
/*    */   private String messageLine1;
/*    */   private String messageLine2;
/* 18 */   private final List listLines2 = Lists.newArrayList();
/*    */   
/*    */   protected String confirmButtonText;
/*    */   private int ticksUntilEnable;
/*    */   
/*    */   public GuiMessage(GuiScreen parentScreen, String line1, String line2) {
/* 24 */     this.parentScreen = parentScreen;
/* 25 */     this.messageLine1 = line1;
/* 26 */     this.messageLine2 = line2;
/* 27 */     this.confirmButtonText = I18n.format("gui.done", new Object[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public void initGui() {
/* 32 */     this.buttonList.add(new GuiOptionButton(0, this.width / 2 - 74, this.height / 6 + 96, this.confirmButtonText));
/* 33 */     this.listLines2.clear();
/* 34 */     this.listLines2.addAll(this.fontRendererObj.listFormattedStringToWidth(this.messageLine2, this.width - 50));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void actionPerformed(GuiButton button) throws IOException {
/* 39 */     Config.getMinecraft().displayGuiScreen(this.parentScreen);
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 44 */     drawDefaultBackground();
/* 45 */     drawCenteredString(this.fontRendererObj, this.messageLine1, this.width / 2, 70, 16777215);
/* 46 */     int i = 90;
/*    */     
/* 48 */     for (Object s : this.listLines2) {
/*    */       
/* 50 */       drawCenteredString(this.fontRendererObj, (String)s, this.width / 2, i, 16777215);
/* 51 */       i += this.fontRendererObj.FONT_HEIGHT;
/*    */     } 
/*    */     
/* 54 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setButtonDelay(int ticksUntilEnable) {
/* 59 */     this.ticksUntilEnable = ticksUntilEnable;
/*    */     
/* 61 */     for (GuiButton guibutton : this.buttonList)
/*    */     {
/* 63 */       guibutton.enabled = false;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void updateScreen() {
/* 72 */     super.updateScreen();
/*    */     
/* 74 */     if (--this.ticksUntilEnable == 0)
/*    */     {
/* 76 */       for (GuiButton guibutton : this.buttonList)
/*    */       {
/* 78 */         guibutton.enabled = true;
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\gui\GuiMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */