/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ 
/*    */ public class GuiErrorScreen
/*    */   extends GuiScreen
/*    */ {
/*    */   private String field_146313_a;
/*    */   private String field_146312_f;
/*    */   
/*    */   public GuiErrorScreen(String p_i46319_1_, String p_i46319_2_) {
/* 13 */     this.field_146313_a = p_i46319_1_;
/* 14 */     this.field_146312_f = p_i46319_2_;
/*    */   }
/*    */ 
/*    */   
/*    */   public void initGui() {
/* 19 */     super.initGui();
/* 20 */     this.buttonList.add(new GuiButton(0, this.width / 2 - 100, 140, I18n.format("gui.cancel", new Object[0])));
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 25 */     drawGradientRect(0, 0, this.width, this.height, -12574688, -11530224);
/* 26 */     drawCenteredString(this.fontRendererObj, this.field_146313_a, this.width / 2, 90, 16777215);
/* 27 */     drawCenteredString(this.fontRendererObj, this.field_146312_f, this.width / 2, 110, 16777215);
/* 28 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void keyTyped(char typedChar, int keyCode) throws IOException {}
/*    */ 
/*    */   
/*    */   protected void actionPerformed(GuiButton button) throws IOException {
/* 37 */     this.mc.displayGuiScreen((GuiScreen)null);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiErrorScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */