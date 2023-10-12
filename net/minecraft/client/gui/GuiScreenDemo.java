/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import net.minecraft.client.settings.GameSettings;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class GuiScreenDemo
/*    */   extends GuiScreen {
/* 14 */   private static final Logger logger = LogManager.getLogger();
/* 15 */   private static final ResourceLocation field_146348_f = new ResourceLocation("textures/gui/demo_background.png");
/*    */ 
/*    */   
/*    */   public void initGui() {
/* 19 */     this.buttonList.clear();
/* 20 */     int i = -16;
/* 21 */     this.buttonList.add(new GuiButton(1, this.width / 2 - 116, this.height / 2 + 62 + i, 114, 20, I18n.format("demo.help.buy", new Object[0])));
/* 22 */     this.buttonList.add(new GuiButton(2, this.width / 2 + 2, this.height / 2 + 62 + i, 114, 20, I18n.format("demo.help.later", new Object[0])));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void actionPerformed(GuiButton button) throws IOException {
/* 27 */     switch (button.id) {
/*    */       
/*    */       case 1:
/* 30 */         button.enabled = false;
/*    */ 
/*    */         
/*    */         try {
/* 34 */           Class<?> oclass = Class.forName("java.awt.Desktop");
/* 35 */           Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
/* 36 */           oclass.getMethod("browse", new Class[] { URI.class }).invoke(object, new Object[] { new URI("http://www.minecraft.net/store?source=demo") });
/*    */         }
/* 38 */         catch (Throwable throwable) {
/*    */           
/* 40 */           logger.error("Couldn't open link", throwable);
/*    */         } 
/*    */         break;
/*    */ 
/*    */       
/*    */       case 2:
/* 46 */         this.mc.displayGuiScreen((GuiScreen)null);
/* 47 */         this.mc.setIngameFocus();
/*    */         break;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void updateScreen() {
/* 53 */     super.updateScreen();
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawDefaultBackground() {
/* 58 */     super.drawDefaultBackground();
/* 59 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 60 */     this.mc.getTextureManager().bindTexture(field_146348_f);
/* 61 */     int i = (this.width - 248) / 2;
/* 62 */     int j = (this.height - 166) / 2;
/* 63 */     drawTexturedModalRect(i, j, 0, 0, 248, 166);
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 68 */     drawDefaultBackground();
/* 69 */     int i = (this.width - 248) / 2 + 10;
/* 70 */     int j = (this.height - 166) / 2 + 8;
/* 71 */     this.fontRendererObj.drawString(I18n.format("demo.help.title", new Object[0]), i, j, 2039583);
/* 72 */     j += 12;
/* 73 */     GameSettings gamesettings = this.mc.gameSettings;
/* 74 */     this.fontRendererObj.drawString(I18n.format("demo.help.movementShort", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindForward.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindLeft.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindBack.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindRight.getKeyCode()) }), i, j, 5197647);
/* 75 */     this.fontRendererObj.drawString(I18n.format("demo.help.movementMouse", new Object[0]), i, (j + 12), 5197647);
/* 76 */     this.fontRendererObj.drawString(I18n.format("demo.help.jump", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindJump.getKeyCode()) }), i, (j + 24), 5197647);
/* 77 */     this.fontRendererObj.drawString(I18n.format("demo.help.inventory", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindInventory.getKeyCode()) }), i, (j + 36), 5197647);
/* 78 */     this.fontRendererObj.drawSplitString(I18n.format("demo.help.fullWrapped", new Object[0]), i, j + 68, 218, 2039583);
/* 79 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiScreenDemo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */