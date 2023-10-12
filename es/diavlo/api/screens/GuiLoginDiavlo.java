/*     */ package es.diavlo.api.screens;
/*     */ 
/*     */ import es.diavlo.api.data.UserData;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiTextField;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuiLoginDiavlo
/*     */   extends GuiScreen
/*     */ {
/*     */   private final GuiScreen previousScreen;
/*     */   private GuiTextField username;
/*     */   private GuiTextField password;
/*     */   private UserData dv;
/*  24 */   private String status = ""; public String getStatus() { return this.status; } public void setStatus(String status) {
/*  25 */     this.status = status;
/*     */   }
/*     */   
/*     */   public GuiLoginDiavlo(GuiScreen previousScreen) {
/*  29 */     this.previousScreen = previousScreen;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) {
/*     */     try {
/*  35 */       switch (button.id) {
/*     */         case 1337:
/*  37 */           this.mc.displayGuiScreen(this.previousScreen);
/*     */           break;
/*     */         case 1338:
/*  40 */           if (this.dv != null) {
/*  41 */             setStatus("§6Ya estás logeado (§6§l" + this.dv.getNickname() + "§6)");
/*     */             return;
/*     */           } 
/*     */           try {
/*  45 */             this.dv = UserData.getInstance(this.username.getText(), this.password.getText());
/*  46 */             setStatus("§aHas iniciado sesión: (§2§l" + this.dv.getNickname() + "§a)");
/*  47 */           } catch (InvalidDiavloLoginCredentialsException e) {
/*  48 */             setStatus("§4§lCredenciales Inválidas.");
/*     */           } 
/*     */           break;
/*     */       } 
/*  52 */     } catch (Throwable throwable) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawScreen(int x, int y2, float z) {
/*  58 */     FontRenderer font = this.mc.fontRendererObj;
/*  59 */     drawDefaultBackground();
/*  60 */     this.username.drawTextBox();
/*  61 */     this.password.drawTextBox();
/*  62 */     drawCenteredString(font, EnumChatFormatting.WHITE + "Diavlo " + EnumChatFormatting.BOLD + EnumChatFormatting.YELLOW + "VIP" + EnumChatFormatting.WHITE + " Login", (int)(this.width / 2.0F), 35, -1);
/*  63 */     drawCenteredString(font, getStatus(), (int)(this.width / 2.0F), 55, -1);
/*  64 */     if (this.username.getText().isEmpty()) {
/*  65 */       font.drawStringWithShadow("Usuario", this.width / 2.0F - 96.0F, 86.0F, -7829368);
/*     */     }
/*  67 */     if (this.password.getText().isEmpty()) {
/*  68 */       font.drawStringWithShadow("Contraseña", this.width / 2.0F - 96.0F, 126.0F, -7829368);
/*     */     }
/*  70 */     super.drawScreen(x, y2, z);
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  75 */     int var3 = this.height / 4 + 24;
/*  76 */     this.buttonList.add(new GuiButton(1338, this.width / 2 - 100, var3 + 72 + 12, "Login"));
/*  77 */     this.buttonList.add(new GuiButton(1337, this.width / 2 - 100, var3 + 72 + 12 + 24, I18n.format("Cancelar", new Object[0])));
/*  78 */     this.username = new GuiTextField(var3, this.mc.fontRendererObj, this.width / 2 - 100, 80, 200, 20);
/*  79 */     this.password = new GuiTextField(var3, this.mc.fontRendererObj, this.width / 2 - 100, 120, 200, 20);
/*  80 */     this.username.setFocused(true);
/*  81 */     Keyboard.enableRepeatEvents(true);
/*  82 */     this.dv = UserData.getInstance();
/*  83 */     if (this.dv != null) {
/*  84 */       setStatus(EnumChatFormatting.YELLOW + "Ya estás logeado (" + EnumChatFormatting.BOLD + EnumChatFormatting.YELLOW + this.dv.getNickname() + EnumChatFormatting.YELLOW + ")");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void keyTyped(char character, int key) {
/*  90 */     if (character == '\t') {
/*  91 */       if (!this.username.isFocused() && !this.password.isFocused()) {
/*  92 */         this.username.setFocused(true);
/*     */       } else {
/*  94 */         this.username.setFocused(this.password.isFocused());
/*  95 */         this.password.setFocused(!this.username.isFocused());
/*     */       } 
/*     */     }
/*  98 */     if (character == '\r') {
/*  99 */       actionPerformed(this.buttonList.get(0));
/*     */     }
/* 101 */     this.username.textboxKeyTyped(character, key);
/* 102 */     this.password.textboxKeyTyped(character, key);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int x, int y2, int button) throws IOException {
/* 107 */     this.username.mouseClicked(x, y2, button);
/* 108 */     this.password.mouseClicked(x, y2, button);
/* 109 */     super.mouseClicked(x, y2, button);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onGuiClosed() {
/* 114 */     Keyboard.enableRepeatEvents(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateScreen() {
/* 119 */     this.username.updateCursorCounter();
/* 120 */     this.password.updateCursorCounter();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\es\diavlo\api\screens\GuiLoginDiavlo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */