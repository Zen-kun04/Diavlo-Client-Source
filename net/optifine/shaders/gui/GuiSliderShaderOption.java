/*    */ package net.optifine.shaders.gui;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.optifine.shaders.config.ShaderOption;
/*    */ 
/*    */ public class GuiSliderShaderOption
/*    */   extends GuiButtonShaderOption {
/* 11 */   private float sliderValue = 1.0F;
/*    */   public boolean dragging;
/* 13 */   private ShaderOption shaderOption = null;
/*    */ 
/*    */   
/*    */   public GuiSliderShaderOption(int buttonId, int x, int y, int w, int h, ShaderOption shaderOption, String text) {
/* 17 */     super(buttonId, x, y, w, h, shaderOption, text);
/* 18 */     this.shaderOption = shaderOption;
/* 19 */     this.sliderValue = shaderOption.getIndexNormalized();
/* 20 */     this.displayString = GuiShaderOptions.getButtonText(shaderOption, this.width);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getHoverState(boolean mouseOver) {
/* 25 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
/* 30 */     if (this.visible) {
/*    */       
/* 32 */       if (this.dragging && !GuiScreen.isShiftKeyDown()) {
/*    */         
/* 34 */         this.sliderValue = (mouseX - this.xPosition + 4) / (this.width - 8);
/* 35 */         this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0F, 1.0F);
/* 36 */         this.shaderOption.setIndexNormalized(this.sliderValue);
/* 37 */         this.sliderValue = this.shaderOption.getIndexNormalized();
/* 38 */         this.displayString = GuiShaderOptions.getButtonText(this.shaderOption, this.width);
/*    */       } 
/*    */       
/* 41 */       mc.getTextureManager().bindTexture(buttonTextures);
/* 42 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 43 */       drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (this.width - 8)), this.yPosition, 0, 66, 4, 20);
/* 44 */       drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
/* 50 */     if (super.mousePressed(mc, mouseX, mouseY)) {
/*    */       
/* 52 */       this.sliderValue = (mouseX - this.xPosition + 4) / (this.width - 8);
/* 53 */       this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0F, 1.0F);
/* 54 */       this.shaderOption.setIndexNormalized(this.sliderValue);
/* 55 */       this.displayString = GuiShaderOptions.getButtonText(this.shaderOption, this.width);
/* 56 */       this.dragging = true;
/* 57 */       return true;
/*    */     } 
/*    */ 
/*    */     
/* 61 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void mouseReleased(int mouseX, int mouseY) {
/* 67 */     this.dragging = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void valueChanged() {
/* 72 */     this.sliderValue = this.shaderOption.getIndexNormalized();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSwitchable() {
/* 77 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\shaders\gui\GuiSliderShaderOption.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */