/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.world.WorldSettings;
/*     */ 
/*     */ public class GuiShareToLan
/*     */   extends GuiScreen {
/*     */   private final GuiScreen field_146598_a;
/*     */   private GuiButton field_146596_f;
/*     */   private GuiButton field_146597_g;
/*  15 */   private String field_146599_h = "survival";
/*     */   
/*     */   private boolean field_146600_i;
/*     */   
/*     */   public GuiShareToLan(GuiScreen p_i1055_1_) {
/*  20 */     this.field_146598_a = p_i1055_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  25 */     this.buttonList.clear();
/*  26 */     this.buttonList.add(new GuiButton(101, this.width / 2 - 155, this.height - 28, 150, 20, I18n.format("lanServer.start", new Object[0])));
/*  27 */     this.buttonList.add(new GuiButton(102, this.width / 2 + 5, this.height - 28, 150, 20, I18n.format("gui.cancel", new Object[0])));
/*  28 */     this.buttonList.add(this.field_146597_g = new GuiButton(104, this.width / 2 - 155, 100, 150, 20, I18n.format("selectWorld.gameMode", new Object[0])));
/*  29 */     this.buttonList.add(this.field_146596_f = new GuiButton(103, this.width / 2 + 5, 100, 150, 20, I18n.format("selectWorld.allowCommands", new Object[0])));
/*  30 */     func_146595_g();
/*     */   }
/*     */ 
/*     */   
/*     */   private void func_146595_g() {
/*  35 */     this.field_146597_g.displayString = I18n.format("selectWorld.gameMode", new Object[0]) + " " + I18n.format("selectWorld.gameMode." + this.field_146599_h, new Object[0]);
/*  36 */     this.field_146596_f.displayString = I18n.format("selectWorld.allowCommands", new Object[0]) + " ";
/*     */     
/*  38 */     if (this.field_146600_i) {
/*     */       
/*  40 */       this.field_146596_f.displayString += I18n.format("options.on", new Object[0]);
/*     */     }
/*     */     else {
/*     */       
/*  44 */       this.field_146596_f.displayString += I18n.format("options.off", new Object[0]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/*  50 */     if (button.id == 102) {
/*     */       
/*  52 */       this.mc.displayGuiScreen(this.field_146598_a);
/*     */     }
/*  54 */     else if (button.id == 104) {
/*     */       
/*  56 */       if (this.field_146599_h.equals("spectator")) {
/*     */         
/*  58 */         this.field_146599_h = "creative";
/*     */       }
/*  60 */       else if (this.field_146599_h.equals("creative")) {
/*     */         
/*  62 */         this.field_146599_h = "adventure";
/*     */       }
/*  64 */       else if (this.field_146599_h.equals("adventure")) {
/*     */         
/*  66 */         this.field_146599_h = "survival";
/*     */       }
/*     */       else {
/*     */         
/*  70 */         this.field_146599_h = "spectator";
/*     */       } 
/*     */       
/*  73 */       func_146595_g();
/*     */     }
/*  75 */     else if (button.id == 103) {
/*     */       
/*  77 */       this.field_146600_i = !this.field_146600_i;
/*  78 */       func_146595_g();
/*     */     }
/*  80 */     else if (button.id == 101) {
/*     */       ChatComponentText chatComponentText;
/*  82 */       this.mc.displayGuiScreen((GuiScreen)null);
/*  83 */       String s = this.mc.getIntegratedServer().shareToLAN(WorldSettings.GameType.getByName(this.field_146599_h), this.field_146600_i);
/*     */ 
/*     */       
/*  86 */       if (s != null) {
/*     */         
/*  88 */         ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("commands.publish.started", new Object[] { s });
/*     */       }
/*     */       else {
/*     */         
/*  92 */         chatComponentText = new ChatComponentText("commands.publish.failed");
/*     */       } 
/*     */       
/*  95 */       this.mc.ingameGUI.getChatGUI().printChatMessage((IChatComponent)chatComponentText);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 101 */     drawDefaultBackground();
/* 102 */     drawCenteredString(this.fontRendererObj, I18n.format("lanServer.title", new Object[0]), this.width / 2, 50, 16777215);
/* 103 */     drawCenteredString(this.fontRendererObj, I18n.format("lanServer.otherPlayers", new Object[0]), this.width / 2, 82, 16777215);
/* 104 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiShareToLan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */