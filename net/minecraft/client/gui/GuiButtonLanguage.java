/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ 
/*    */ public class GuiButtonLanguage
/*    */   extends GuiButton
/*    */ {
/*    */   public GuiButtonLanguage(int buttonID, int xPos, int yPos) {
/* 10 */     super(buttonID, xPos, yPos, 20, 20, "");
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawButton(Minecraft mc, int mouseX, int mouseY) {
/* 15 */     if (this.visible) {
/*    */       
/* 17 */       mc.getTextureManager().bindTexture(GuiButton.buttonTextures);
/* 18 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 19 */       boolean flag = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
/* 20 */       int i = 106;
/*    */       
/* 22 */       if (flag)
/*    */       {
/* 24 */         i += this.height;
/*    */       }
/*    */       
/* 27 */       drawTexturedModalRect(this.xPosition, this.yPosition, 0, i, this.width, this.height);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiButtonLanguage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */