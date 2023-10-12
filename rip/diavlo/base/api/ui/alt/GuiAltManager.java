/*     */ package rip.diavlo.base.api.ui.alt;
/*     */ 
/*     */ import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
/*     */ import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiTextField;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.Session;
/*     */ 
/*     */ public final class GuiAltManager extends GuiScreen {
/*     */   private GuiTextField password;
/*     */   private final GuiScreen previousScreen;
/*     */   private GuiTextField username;
/*     */   private AltLoginThread thread;
/*     */   private String crackedStatus;
/*     */   
/*  21 */   public String getCrackedStatus() { return this.crackedStatus; } public void setCrackedStatus(String crackedStatus) { this.crackedStatus = crackedStatus; }
/*     */   
/*     */   public GuiAltManager(GuiScreen previousScreen) {
/*  24 */     this.previousScreen = previousScreen;
/*     */   }
/*     */   protected void actionPerformed(GuiButton button) {
/*     */     try {
/*     */       MicrosoftAuthenticator authenticator;
/*     */       MicrosoftAuthResult result;
/*  30 */       switch (button.id) {
/*     */         case 1:
/*  32 */           this.mc.displayGuiScreen(this.previousScreen);
/*     */           break;
/*     */         
/*     */         case 0:
/*  36 */           this.thread = new AltLoginThread(this.username.getText(), this.password.getText());
/*  37 */           this.thread.start();
/*     */           break;
/*     */ 
/*     */         
/*     */         case 2:
/*  42 */           authenticator = new MicrosoftAuthenticator();
/*  43 */           result = authenticator.loginWithCredentials(this.username.getText(), this.password.getText());
/*  44 */           this.mc.session = new Session(result.getProfile().getName(), result.getProfile().getId(), result.getAccessToken(), "legacy");
/*     */           
/*  46 */           this.crackedStatus = EnumChatFormatting.GREEN + "Logged in. (" + this.mc.session.getUsername() + ")";
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */     
/*  52 */     } catch (Throwable throwable) {}
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int x, int y2, float z) {
/*  57 */     FontRenderer font = this.mc.fontRendererObj;
/*  58 */     ScaledResolution sr = new ScaledResolution(this.mc);
/*     */     
/*  60 */     drawDefaultBackground();
/*  61 */     this.username.drawTextBox();
/*  62 */     this.password.drawTextBox();
/*  63 */     drawCenteredString(font, "Account Login", (int)(this.width / 2.0F), 20, -1);
/*  64 */     drawCenteredString(font, (this.thread == null) ? ((this.crackedStatus == null) ? (EnumChatFormatting.GRAY + "Idle") : (EnumChatFormatting.GREEN + this.crackedStatus)) : this.thread.getStatus(), (int)(this.width / 2.0F), 29, -1);
/*  65 */     if (this.username.getText().isEmpty()) {
/*  66 */       font.drawStringWithShadow("Username", this.width / 2.0F - 96.0F, 66.0F, -7829368);
/*     */     }
/*  68 */     if (this.password.getText().isEmpty()) {
/*  69 */       font.drawStringWithShadow("Password", this.width / 2.0F - 96.0F, 106.0F, -7829368);
/*     */     }
/*  71 */     super.drawScreen(x, y2, z);
/*     */   }
/*     */ 
/*     */   
/*     */   private Session createMicrosoftSession() {
/*     */     try {
/*  77 */       MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
/*  78 */       MicrosoftAuthResult result = authenticator.loginWithCredentials(this.username.getText(), this.password.getText());
/*  79 */       this.mc.session = new Session(result.getProfile().getName(), result.getProfile().getId(), result.getAccessToken(), "legacy");
/*  80 */     } catch (Exception e) {
/*  81 */       e.printStackTrace();
/*  82 */       return null;
/*     */     } 
/*  84 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  89 */     int var3 = this.height / 4 + 24;
/*  90 */     this.buttonList.add(new GuiButton(0, this.width / 2 - 100, var3 + 72 + 12, "Login"));
/*  91 */     this.buttonList.add(new GuiButton(1, this.width / 2 - 100, var3 + 72 + 12 + 24, I18n.format("gui.cancel", new Object[0])));
/*  92 */     this.buttonList.add(new GuiButton(2, this.width / 2 - 100, var3 + 72 + 12 + 48, "Microsoft login"));
/*  93 */     this.username = new GuiTextField(var3, this.mc.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
/*  94 */     this.password = new GuiTextField(var3, this.mc.fontRendererObj, this.width / 2 - 100, 100, 200, 20);
/*  95 */     this.username.setFocused(true);
/*  96 */     Keyboard.enableRepeatEvents(true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void keyTyped(char character, int key) {
/*     */     try {
/* 102 */       super.keyTyped(character, key);
/* 103 */     } catch (IOException e) {
/* 104 */       e.printStackTrace();
/*     */     } 
/* 106 */     if (character == '\t') {
/* 107 */       if (!this.username.isFocused() && !this.password.isFocused()) {
/* 108 */         this.username.setFocused(true);
/*     */       } else {
/* 110 */         this.username.setFocused(this.password.isFocused());
/* 111 */         this.password.setFocused(!this.username.isFocused());
/*     */       } 
/*     */     }
/* 114 */     if (character == '\r') {
/* 115 */       actionPerformed(this.buttonList.get(0));
/*     */     }
/* 117 */     this.username.textboxKeyTyped(character, key);
/* 118 */     this.password.textboxKeyTyped(character, key);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int x, int y2, int button) {
/*     */     try {
/* 124 */       super.mouseClicked(x, y2, button);
/* 125 */     } catch (IOException e) {
/* 126 */       e.printStackTrace();
/*     */     } 
/* 128 */     this.username.mouseClicked(x, y2, button);
/* 129 */     this.password.mouseClicked(x, y2, button);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onGuiClosed() {
/* 134 */     Keyboard.enableRepeatEvents(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateScreen() {
/* 139 */     this.username.updateCursorCounter();
/* 140 */     this.password.updateCursorCounter();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\ap\\ui\alt\GuiAltManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */