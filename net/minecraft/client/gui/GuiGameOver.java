/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.client.multiplayer.WorldClient;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ 
/*     */ public class GuiGameOver
/*     */   extends GuiScreen
/*     */   implements GuiYesNoCallback {
/*     */   private int enableButtonsTimer;
/*     */   private boolean field_146346_f = false;
/*     */   
/*     */   public void initGui() {
/*  16 */     this.buttonList.clear();
/*     */     
/*  18 */     if (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
/*     */       
/*  20 */       if (this.mc.isIntegratedServerRunning())
/*     */       {
/*  22 */         this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.deleteWorld", new Object[0])));
/*     */       }
/*     */       else
/*     */       {
/*  26 */         this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.leaveServer", new Object[0])));
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/*  31 */       this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 72, I18n.format("deathScreen.respawn", new Object[0])));
/*  32 */       this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.titleScreen", new Object[0])));
/*     */       
/*  34 */       if (this.mc.getSession() == null)
/*     */       {
/*  36 */         ((GuiButton)this.buttonList.get(1)).enabled = false;
/*     */       }
/*     */     } 
/*     */     
/*  40 */     for (GuiButton guibutton : this.buttonList)
/*     */     {
/*  42 */       guibutton.enabled = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {}
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/*     */     GuiYesNo guiyesno;
/*  52 */     switch (button.id) {
/*     */       
/*     */       case 0:
/*  55 */         this.mc.thePlayer.respawnPlayer();
/*  56 */         this.mc.displayGuiScreen((GuiScreen)null);
/*     */         break;
/*     */       
/*     */       case 1:
/*  60 */         if (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
/*     */           
/*  62 */           this.mc.displayGuiScreen(new GuiMainMenu());
/*     */           
/*     */           break;
/*     */         } 
/*  66 */         guiyesno = new GuiYesNo(this, I18n.format("deathScreen.quit.confirm", new Object[0]), "", I18n.format("deathScreen.titleScreen", new Object[0]), I18n.format("deathScreen.respawn", new Object[0]), 0);
/*  67 */         this.mc.displayGuiScreen(guiyesno);
/*  68 */         guiyesno.setButtonDelay(20);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void confirmClicked(boolean result, int id) {
/*  75 */     if (result) {
/*     */       
/*  77 */       this.mc.theWorld.sendQuittingDisconnectingPacket();
/*  78 */       this.mc.loadWorld((WorldClient)null);
/*  79 */       this.mc.displayGuiScreen(new GuiMainMenu());
/*     */     }
/*     */     else {
/*     */       
/*  83 */       this.mc.thePlayer.respawnPlayer();
/*  84 */       this.mc.displayGuiScreen((GuiScreen)null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/*  90 */     drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
/*  91 */     GlStateManager.pushMatrix();
/*  92 */     GlStateManager.scale(2.0F, 2.0F, 2.0F);
/*  93 */     boolean flag = this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled();
/*  94 */     String s = flag ? I18n.format("deathScreen.title.hardcore", new Object[0]) : I18n.format("deathScreen.title", new Object[0]);
/*  95 */     drawCenteredString(this.fontRendererObj, s, this.width / 2 / 2, 30, 16777215);
/*  96 */     GlStateManager.popMatrix();
/*     */     
/*  98 */     if (flag)
/*     */     {
/* 100 */       drawCenteredString(this.fontRendererObj, I18n.format("deathScreen.hardcoreInfo", new Object[0]), this.width / 2, 144, 16777215);
/*     */     }
/*     */     
/* 103 */     drawCenteredString(this.fontRendererObj, I18n.format("deathScreen.score", new Object[0]) + ": " + EnumChatFormatting.YELLOW + this.mc.thePlayer.getScore(), this.width / 2, 100, 16777215);
/* 104 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doesGuiPauseGame() {
/* 109 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateScreen() {
/* 114 */     super.updateScreen();
/* 115 */     this.enableButtonsTimer++;
/*     */     
/* 117 */     if (this.enableButtonsTimer == 20)
/*     */     {
/* 119 */       for (GuiButton guibutton : this.buttonList)
/*     */       {
/* 121 */         guibutton.enabled = true;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiGameOver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */