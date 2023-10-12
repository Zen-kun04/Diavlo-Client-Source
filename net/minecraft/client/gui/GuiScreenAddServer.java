/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.io.IOException;
/*     */ import java.net.IDN;
/*     */ import net.minecraft.client.multiplayer.ServerData;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ public class GuiScreenAddServer extends GuiScreen {
/*     */   private final GuiScreen parentScreen;
/*     */   private final ServerData serverData;
/*     */   private GuiTextField serverIPField;
/*     */   private GuiTextField serverNameField;
/*     */   private GuiButton serverResourcePacks;
/*     */   
/*  17 */   private Predicate<String> field_181032_r = new Predicate<String>()
/*     */     {
/*     */       public boolean apply(String p_apply_1_)
/*     */       {
/*  21 */         if (p_apply_1_.length() == 0)
/*     */         {
/*  23 */           return true;
/*     */         }
/*     */ 
/*     */         
/*  27 */         String[] astring = p_apply_1_.split(":");
/*     */         
/*  29 */         if (astring.length == 0)
/*     */         {
/*  31 */           return true;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/*  37 */           String s = IDN.toASCII(astring[0]);
/*  38 */           return true;
/*     */         }
/*  40 */         catch (IllegalArgumentException var4) {
/*     */           
/*  42 */           return false;
/*     */         } 
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GuiScreenAddServer(GuiScreen p_i1033_1_, ServerData p_i1033_2_) {
/*  51 */     this.parentScreen = p_i1033_1_;
/*  52 */     this.serverData = p_i1033_2_;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateScreen() {
/*  57 */     this.serverNameField.updateCursorCounter();
/*  58 */     this.serverIPField.updateCursorCounter();
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  63 */     Keyboard.enableRepeatEvents(true);
/*  64 */     this.buttonList.clear();
/*  65 */     this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 18, I18n.format("addServer.add", new Object[0])));
/*  66 */     this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 18, I18n.format("gui.cancel", new Object[0])));
/*  67 */     this.buttonList.add(this.serverResourcePacks = new GuiButton(2, this.width / 2 - 100, this.height / 4 + 72, I18n.format("addServer.resourcePack", new Object[0]) + ": " + this.serverData.getResourceMode().getMotd().getFormattedText()));
/*  68 */     this.serverNameField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, 66, 200, 20);
/*  69 */     this.serverNameField.setFocused(true);
/*  70 */     this.serverNameField.setText(this.serverData.serverName);
/*  71 */     this.serverIPField = new GuiTextField(1, this.fontRendererObj, this.width / 2 - 100, 106, 200, 20);
/*  72 */     this.serverIPField.setMaxStringLength(128);
/*  73 */     this.serverIPField.setText(this.serverData.serverIP);
/*  74 */     this.serverIPField.setValidator(this.field_181032_r);
/*  75 */     ((GuiButton)this.buttonList.get(0)).enabled = (this.serverIPField.getText().length() > 0 && (this.serverIPField.getText().split(":")).length > 0 && this.serverNameField.getText().length() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onGuiClosed() {
/*  80 */     Keyboard.enableRepeatEvents(false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/*  85 */     if (button.enabled)
/*     */     {
/*  87 */       if (button.id == 2) {
/*     */         
/*  89 */         this.serverData.setResourceMode(ServerData.ServerResourceMode.values()[(this.serverData.getResourceMode().ordinal() + 1) % (ServerData.ServerResourceMode.values()).length]);
/*  90 */         this.serverResourcePacks.displayString = I18n.format("addServer.resourcePack", new Object[0]) + ": " + this.serverData.getResourceMode().getMotd().getFormattedText();
/*     */       }
/*  92 */       else if (button.id == 1) {
/*     */         
/*  94 */         this.parentScreen.confirmClicked(false, 0);
/*     */       }
/*  96 */       else if (button.id == 0) {
/*     */         
/*  98 */         this.serverData.serverName = this.serverNameField.getText();
/*  99 */         this.serverData.serverIP = this.serverIPField.getText();
/* 100 */         this.parentScreen.confirmClicked(true, 0);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/* 107 */     this.serverNameField.textboxKeyTyped(typedChar, keyCode);
/* 108 */     this.serverIPField.textboxKeyTyped(typedChar, keyCode);
/*     */     
/* 110 */     if (keyCode == 15) {
/*     */       
/* 112 */       this.serverNameField.setFocused(!this.serverNameField.isFocused());
/* 113 */       this.serverIPField.setFocused(!this.serverIPField.isFocused());
/*     */     } 
/*     */     
/* 116 */     if (keyCode == 28 || keyCode == 156)
/*     */     {
/* 118 */       actionPerformed(this.buttonList.get(0));
/*     */     }
/*     */     
/* 121 */     ((GuiButton)this.buttonList.get(0)).enabled = (this.serverIPField.getText().length() > 0 && (this.serverIPField.getText().split(":")).length > 0 && this.serverNameField.getText().length() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 126 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/* 127 */     this.serverIPField.mouseClicked(mouseX, mouseY, mouseButton);
/* 128 */     this.serverNameField.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 133 */     drawDefaultBackground();
/* 134 */     drawCenteredString(this.fontRendererObj, I18n.format("addServer.title", new Object[0]), this.width / 2, 17, 16777215);
/* 135 */     drawString(this.fontRendererObj, I18n.format("addServer.enterName", new Object[0]), this.width / 2 - 100, 53, 10526880);
/* 136 */     drawString(this.fontRendererObj, I18n.format("addServer.enterIp", new Object[0]), this.width / 2 - 100, 94, 10526880);
/* 137 */     this.serverNameField.drawTextBox();
/* 138 */     this.serverIPField.drawTextBox();
/* 139 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiScreenAddServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */