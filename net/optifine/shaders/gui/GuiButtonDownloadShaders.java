/*    */ package net.optifine.shaders.gui;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class GuiButtonDownloadShaders
/*    */   extends GuiButton
/*    */ {
/*    */   public GuiButtonDownloadShaders(int buttonID, int xPos, int yPos) {
/* 12 */     super(buttonID, xPos, yPos, 22, 20, "");
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawButton(Minecraft mc, int mouseX, int mouseY) {
/* 17 */     if (this.visible) {
/*    */       
/* 19 */       super.drawButton(mc, mouseX, mouseY);
/* 20 */       ResourceLocation resourcelocation = new ResourceLocation("optifine/textures/icons.png");
/* 21 */       mc.getTextureManager().bindTexture(resourcelocation);
/* 22 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 23 */       drawTexturedModalRect(this.xPosition + 3, this.yPosition + 2, 0, 0, 16, 16);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\shaders\gui\GuiButtonDownloadShaders.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */