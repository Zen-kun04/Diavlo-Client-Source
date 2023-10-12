/*    */ package net.optifine.gui;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.gui.GuiVideoSettings;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GuiScreenOF
/*    */   extends GuiScreen
/*    */ {
/*    */   protected void actionPerformedRightClick(GuiButton button) throws IOException {}
/*    */   
/*    */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 18 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*    */     
/* 20 */     if (mouseButton == 1) {
/*    */       
/* 22 */       GuiButton guibutton = getSelectedButton(mouseX, mouseY, this.buttonList);
/*    */       
/* 24 */       if (guibutton != null && guibutton.enabled) {
/*    */         
/* 26 */         guibutton.playPressSound(this.mc.getSoundHandler());
/* 27 */         actionPerformedRightClick(guibutton);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static GuiButton getSelectedButton(int x, int y, List<GuiButton> listButtons) {
/* 34 */     for (int i = 0; i < listButtons.size(); i++) {
/*    */       
/* 36 */       GuiButton guibutton = listButtons.get(i);
/*    */       
/* 38 */       if (guibutton.visible) {
/*    */         
/* 40 */         int j = GuiVideoSettings.getButtonWidth(guibutton);
/* 41 */         int k = GuiVideoSettings.getButtonHeight(guibutton);
/*    */         
/* 43 */         if (x >= guibutton.xPosition && y >= guibutton.yPosition && x < guibutton.xPosition + j && y < guibutton.yPosition + k)
/*    */         {
/* 45 */           return guibutton;
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 50 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\gui\GuiScreenOF.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */