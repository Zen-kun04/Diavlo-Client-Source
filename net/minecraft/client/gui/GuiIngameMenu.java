/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.client.gui.achievement.GuiAchievements;
/*     */ import net.minecraft.client.gui.achievement.GuiStats;
/*     */ import net.minecraft.client.multiplayer.WorldClient;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import rip.diavlo.base.Client;
/*     */ import rip.diavlo.base.api.ui.griefing.GuiUUIDSpoof;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuiIngameMenu
/*     */   extends GuiScreen
/*     */ {
/*     */   private int field_146445_a;
/*     */   private int field_146444_f;
/*     */   
/*     */   public void initGui() {
/*  20 */     this.field_146445_a = 0;
/*  21 */     this.buttonList.clear();
/*  22 */     int i = -16;
/*  23 */     int j = 98;
/*  24 */     this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + i, I18n.format("menu.returnToMenu", new Object[0])));
/*     */     
/*  26 */     if (!this.mc.isIntegratedServerRunning())
/*     */     {
/*  28 */       ((GuiButton)this.buttonList.get(0)).displayString = I18n.format("menu.disconnect", new Object[0]);
/*     */     }
/*     */     
/*  31 */     this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + i, I18n.format("menu.returnToGame", new Object[0])));
/*  32 */     this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + i, 98, 20, I18n.format("menu.options", new Object[0])));
/*  33 */     this.buttonList.add(new GuiButton(104, this.width / 2 + 2, this.height / 4 + 96 + i, 98, 20, I18n.format("UUID Spoof", new Object[0])));
/*     */     
/*  35 */     if (!this.mc.isSingleplayer())
/*     */     {
/*     */       
/*  38 */       this.buttonList.add(new GuiButton(100, this.width / 2 - 100, this.height / 4 + 56, "Server List"));
/*     */     }
/*     */     
/*  41 */     this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 48 + i, 98, 20, I18n.format("gui.achievements", new Object[0])));
/*  42 */     this.buttonList.add(new GuiButton(6, this.width / 2 + 2, this.height / 4 + 48 + i, 98, 20, I18n.format("gui.stats", new Object[0])));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/*     */     boolean flag, flag1;
/*  48 */     switch (button.id) {
/*     */       
/*     */       case 0:
/*  51 */         this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
/*     */ 
/*     */       
/*     */       case 1:
/*  55 */         flag = this.mc.isIntegratedServerRunning();
/*  56 */         flag1 = this.mc.isConnectedToRealms();
/*  57 */         button.enabled = false;
/*  58 */         this.mc.theWorld.sendQuittingDisconnectingPacket();
/*  59 */         this.mc.loadWorld((WorldClient)null);
/*  60 */         Client.getInstance().setSearchingPasswords(false);
/*     */         
/*  62 */         if (flag) {
/*     */           
/*  64 */           this.mc.displayGuiScreen(new GuiMainMenu());
/*     */         
/*     */         }
/*     */         else {
/*     */           
/*  69 */           this.mc.displayGuiScreen(new GuiMultiplayer(this));
/*     */         } 
/*     */ 
/*     */       
/*     */       default:
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/*  78 */         this.mc.displayGuiScreen((GuiScreen)null);
/*  79 */         this.mc.setIngameFocus();
/*     */ 
/*     */       
/*     */       case 5:
/*  83 */         this.mc.displayGuiScreen((GuiScreen)new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
/*     */ 
/*     */       
/*     */       case 6:
/*  87 */         this.mc.displayGuiScreen((GuiScreen)new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
/*     */ 
/*     */       
/*     */       case 7:
/*  91 */         this.mc.displayGuiScreen(new GuiShareToLan(this));
/*     */       
/*     */       case 100:
/*  94 */         this.mc.displayGuiScreen(new GuiMultiplayer(this));
/*     */       case 104:
/*     */         break;
/*     */     } 
/*  98 */     this.mc.displayGuiScreen((GuiScreen)new GuiUUIDSpoof(this));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateScreen() {
/* 105 */     super.updateScreen();
/* 106 */     this.field_146444_f++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 111 */     drawDefaultBackground();
/* 112 */     drawCenteredString(this.fontRendererObj, I18n.format("menu.game", new Object[0]), this.width / 2, 40, 16777215);
/* 113 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiIngameMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */