/*     */ package net.optifine.gui;
/*     */ 
/*     */ import com.mojang.authlib.exceptions.InvalidCredentialsException;
/*     */ import java.math.BigInteger;
/*     */ import java.net.URI;
/*     */ import java.util.Random;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.src.Config;
/*     */ import net.optifine.Lang;
/*     */ 
/*     */ 
/*     */ public class GuiScreenCapeOF
/*     */   extends GuiScreenOF
/*     */ {
/*     */   private final GuiScreen parentScreen;
/*     */   private String title;
/*     */   private String message;
/*     */   private long messageHideTimeMs;
/*     */   private String linkUrl;
/*     */   private GuiButtonOF buttonCopyLink;
/*     */   private FontRenderer fontRenderer;
/*     */   
/*     */   public GuiScreenCapeOF(GuiScreen parentScreenIn) {
/*  27 */     this.fontRenderer = (Config.getMinecraft()).fontRendererObj;
/*  28 */     this.parentScreen = parentScreenIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  33 */     int i = 0;
/*  34 */     this.title = I18n.format("of.options.capeOF.title", new Object[0]);
/*  35 */     i += 2;
/*  36 */     this.buttonList.add(new GuiButtonOF(210, this.width / 2 - 155, this.height / 6 + 24 * (i >> 1), 150, 20, I18n.format("of.options.capeOF.openEditor", new Object[0])));
/*  37 */     this.buttonList.add(new GuiButtonOF(220, this.width / 2 - 155 + 160, this.height / 6 + 24 * (i >> 1), 150, 20, I18n.format("of.options.capeOF.reloadCape", new Object[0])));
/*  38 */     i += 6;
/*  39 */     this.buttonCopyLink = new GuiButtonOF(230, this.width / 2 - 100, this.height / 6 + 24 * (i >> 1), 200, 20, I18n.format("of.options.capeOF.copyEditorLink", new Object[0]));
/*  40 */     this.buttonCopyLink.visible = (this.linkUrl != null);
/*  41 */     this.buttonList.add(this.buttonCopyLink);
/*  42 */     i += 4;
/*  43 */     this.buttonList.add(new GuiButtonOF(200, this.width / 2 - 100, this.height / 6 + 24 * (i >> 1), I18n.format("gui.done", new Object[0])));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) {
/*  48 */     if (button.enabled) {
/*     */       
/*  50 */       if (button.id == 200)
/*     */       {
/*  52 */         this.mc.displayGuiScreen(this.parentScreen);
/*     */       }
/*     */       
/*  55 */       if (button.id == 210) {
/*     */         
/*     */         try {
/*     */           
/*  59 */           String s = this.mc.getSession().getProfile().getName();
/*  60 */           String s1 = this.mc.getSession().getProfile().getId().toString().replace("-", "");
/*  61 */           String s2 = this.mc.getSession().getToken();
/*  62 */           Random random = new Random();
/*  63 */           Random random1 = new Random(System.identityHashCode(new Object()));
/*  64 */           BigInteger biginteger = new BigInteger(128, random);
/*  65 */           BigInteger biginteger1 = new BigInteger(128, random1);
/*  66 */           BigInteger biginteger2 = biginteger.xor(biginteger1);
/*  67 */           String s3 = biginteger2.toString(16);
/*  68 */           this.mc.getSessionService().joinServer(this.mc.getSession().getProfile(), s2, s3);
/*  69 */           String s4 = "https://optifine.net/capeChange?u=" + s1 + "&n=" + s + "&s=" + s3;
/*  70 */           boolean flag = Config.openWebLink(new URI(s4));
/*     */           
/*  72 */           if (flag)
/*     */           {
/*  74 */             showMessage(Lang.get("of.message.capeOF.openEditor"), 10000L);
/*     */           }
/*     */           else
/*     */           {
/*  78 */             showMessage(Lang.get("of.message.capeOF.openEditorError"), 10000L);
/*  79 */             setLinkUrl(s4);
/*     */           }
/*     */         
/*  82 */         } catch (InvalidCredentialsException invalidcredentialsexception) {
/*     */           
/*  84 */           Config.showGuiMessage(I18n.format("of.message.capeOF.error1", new Object[0]), I18n.format("of.message.capeOF.error2", new Object[] { invalidcredentialsexception.getMessage() }));
/*  85 */           Config.warn("Mojang authentication failed");
/*  86 */           Config.warn(invalidcredentialsexception.getClass().getName() + ": " + invalidcredentialsexception.getMessage());
/*     */         }
/*  88 */         catch (Exception exception) {
/*     */           
/*  90 */           Config.warn("Error opening OptiFine cape link");
/*  91 */           Config.warn(exception.getClass().getName() + ": " + exception.getMessage());
/*     */         } 
/*     */       }
/*     */       
/*  95 */       if (button.id == 220) {
/*     */         
/*  97 */         showMessage(Lang.get("of.message.capeOF.reloadCape"), 15000L);
/*     */         
/*  99 */         if (this.mc.thePlayer != null) {
/*     */           
/* 101 */           long i = 15000L;
/* 102 */           long j = System.currentTimeMillis() + i;
/* 103 */           this.mc.thePlayer.setReloadCapeTimeMs(j);
/*     */         } 
/*     */       } 
/*     */       
/* 107 */       if (button.id == 230 && this.linkUrl != null)
/*     */       {
/* 109 */         setClipboardString(this.linkUrl);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void showMessage(String msg, long timeMs) {
/* 116 */     this.message = msg;
/* 117 */     this.messageHideTimeMs = System.currentTimeMillis() + timeMs;
/* 118 */     setLinkUrl((String)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 123 */     drawDefaultBackground();
/* 124 */     drawCenteredString(this.fontRenderer, this.title, this.width / 2, 20, 16777215);
/*     */     
/* 126 */     if (this.message != null) {
/*     */       
/* 128 */       drawCenteredString(this.fontRenderer, this.message, this.width / 2, this.height / 6 + 60, 16777215);
/*     */       
/* 130 */       if (System.currentTimeMillis() > this.messageHideTimeMs) {
/*     */         
/* 132 */         this.message = null;
/* 133 */         setLinkUrl((String)null);
/*     */       } 
/*     */     } 
/*     */     
/* 137 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLinkUrl(String linkUrl) {
/* 142 */     this.linkUrl = linkUrl;
/* 143 */     this.buttonCopyLink.visible = (linkUrl != null);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\gui\GuiScreenCapeOF.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */