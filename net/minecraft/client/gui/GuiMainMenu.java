/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import es.diavlo.api.screens.GuiLoginDiavlo;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.opengl.Display;
/*     */ import rip.diavlo.base.Client;
/*     */ import rip.diavlo.base.api.ui.GuiCrediti;
/*     */ import rip.diavlo.base.api.ui.alt.GuiAltManager;
/*     */ import rip.diavlo.base.utils.FileUtility;
/*     */ import rip.diavlo.base.utils.render.RenderUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuiMainMenu
/*     */   extends GuiScreen
/*     */   implements GuiYesNoCallback
/*     */ {
/*     */   public boolean doesGuiPauseGame() {
/*  22 */     return false;
/*     */   }
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {}
/*     */   
/*     */   public void initGui() {
/*  28 */     Display.setTitle(Client.clientName + " v" + Client.clientVersion);
/*     */     
/*  30 */     String x = FileUtility.readFirstLine("animazione.txt");
/*  31 */     if (x == null) {
/*  32 */       x = "false";
/*     */     }
/*  34 */     GuiScreen.animazione = Boolean.parseBoolean(x);
/*     */     
/*  36 */     int j = this.height / 4 + 24 + 10;
/*     */     
/*  38 */     addSingleplayerMultiplayerButtons(j, 24);
/*     */     
/*  40 */     this.buttonList.add(new GuiButton(1010, this.width / 2 - 100, j + 72, 98, 20, I18n.format("Fondo", new Object[0])));
/*  41 */     this.buttonList.add(new GuiButton(2020, this.width / 2 + 2, j + 72, 98, 20, I18n.format("Creditos", new Object[0])));
/*     */     
/*  43 */     this.buttonList.add(new GuiButton(0, this.width / 2 - 100, j + 72 + 24, 98, 20, I18n.format("Opciones", new Object[0])));
/*  44 */     this.buttonList.add(new GuiButton(4, this.width / 2 + 2, j + 72 + 24, 98, 20, I18n.format("Salir", new Object[0])));
/*     */ 
/*     */     
/*  47 */     this.buttonList.add(new GuiButton(2023, this.width / 2 - 100, j + 72 + 24 + 24, "Login Diavlo"));
/*     */   }
/*     */ 
/*     */   
/*     */   private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
/*  52 */     this.buttonList.add(new GuiButton(1, this.width / 2 - 100, p_73969_1_, I18n.format("Un Jugador", new Object[0])));
/*  53 */     this.buttonList.add(new GuiButton(2, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 1, "Multijugador"));
/*  54 */     this.buttonList.add(new GuiButton(3, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, "Alts"));
/*     */   }
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/*  58 */     switch (button.id) {
/*     */       case 0:
/*  60 */         this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
/*     */         break;
/*     */       case 1:
/*  63 */         this.mc.displayGuiScreen(new GuiSelectWorld(this));
/*     */         break;
/*     */       case 2:
/*  66 */         this.mc.displayGuiScreen(new GuiMultiplayer(this));
/*     */         break;
/*     */       case 3:
/*  69 */         this.mc.displayGuiScreen((GuiScreen)new GuiAltManager(this));
/*     */         break;
/*     */       case 4:
/*  72 */         this.mc.shutdown();
/*     */         break;
/*     */       case 5:
/*  75 */         this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
/*     */         break;
/*     */       case 1010:
/*  78 */         GuiScreen.animazione = !GuiScreen.animazione;
/*  79 */         FileUtility.overwriteFileContent("animazione.txt", String.valueOf(GuiScreen.animazione));
/*  80 */         this.mc.displayGuiScreen(new GuiMainMenu());
/*     */         break;
/*     */       case 2020:
/*  83 */         this.mc.displayGuiScreen((GuiScreen)new GuiCrediti(this));
/*     */         break;
/*     */       case 2023:
/*  86 */         this.mc.displayGuiScreen((GuiScreen)new GuiLoginDiavlo(this));
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/*  94 */     drawDefaultBackground();
/*     */     
/*  96 */     ScaledResolution sr = new ScaledResolution(this.mc);
/*  97 */     ResourceLocation logo = new ResourceLocation("client/logotipo.png");
/*     */     
/*  99 */     int var3 = this.height / 4 + 48 - 30;
/*     */     
/* 101 */     RenderUtil.drawImage(logo, (sr.getScaledWidth() / 2 - 32), (var3 - 60), 64.0F, 64.0F, -1);
/*     */     
/* 103 */     String s2 = "© DiavloClient 2023 - 2024";
/*     */     
/* 105 */     drawString(this.fontRendererObj, s2, this.width - this.fontRendererObj.getStringWidth(s2) - 2, this.height - 10, -1);
/*     */     
/* 107 */     drawString(this.fontRendererObj, "discord.gg/programadores", 2, this.height - 10, -1);
/*     */ 
/*     */     
/* 110 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 114 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiMainMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */