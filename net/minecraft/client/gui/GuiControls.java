/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ 
/*     */ public class GuiControls
/*     */   extends GuiScreen {
/*  11 */   private static final GameSettings.Options[] optionsArr = new GameSettings.Options[] { GameSettings.Options.INVERT_MOUSE, GameSettings.Options.SENSITIVITY, GameSettings.Options.TOUCHSCREEN };
/*     */   private GuiScreen parentScreen;
/*  13 */   protected String screenTitle = "Controls";
/*     */   private GameSettings options;
/*  15 */   public KeyBinding buttonId = null;
/*     */   
/*     */   public long time;
/*     */   private GuiKeyBindingList keyBindingList;
/*     */   private GuiButton buttonReset;
/*     */   
/*     */   public GuiControls(GuiScreen screen, GameSettings settings) {
/*  22 */     this.parentScreen = screen;
/*  23 */     this.options = settings;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  28 */     this.keyBindingList = new GuiKeyBindingList(this, this.mc);
/*  29 */     this.buttonList.add(new GuiButton(200, this.width / 2 - 155, this.height - 29, 150, 20, I18n.format("gui.done", new Object[0])));
/*  30 */     this.buttonList.add(this.buttonReset = new GuiButton(201, this.width / 2 - 155 + 160, this.height - 29, 150, 20, I18n.format("controls.resetAll", new Object[0])));
/*  31 */     this.screenTitle = I18n.format("controls.title", new Object[0]);
/*  32 */     int i = 0;
/*     */     
/*  34 */     for (GameSettings.Options gamesettings$options : optionsArr) {
/*     */       
/*  36 */       if (gamesettings$options.getEnumFloat()) {
/*     */         
/*  38 */         this.buttonList.add(new GuiOptionSlider(gamesettings$options.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, 18 + 24 * (i >> 1), gamesettings$options));
/*     */       }
/*     */       else {
/*     */         
/*  42 */         this.buttonList.add(new GuiOptionButton(gamesettings$options.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, 18 + 24 * (i >> 1), gamesettings$options, this.options.getKeyBinding(gamesettings$options)));
/*     */       } 
/*     */       
/*  45 */       i++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleMouseInput() throws IOException {
/*  51 */     super.handleMouseInput();
/*  52 */     this.keyBindingList.handleMouseInput();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/*  57 */     if (button.id == 200) {
/*     */       
/*  59 */       this.mc.displayGuiScreen(this.parentScreen);
/*     */     }
/*  61 */     else if (button.id == 201) {
/*     */       
/*  63 */       for (KeyBinding keybinding : this.mc.gameSettings.keyBindings)
/*     */       {
/*  65 */         keybinding.setKeyCode(keybinding.getKeyCodeDefault());
/*     */       }
/*     */       
/*  68 */       KeyBinding.resetKeyBindingArrayAndHash();
/*     */     }
/*  70 */     else if (button.id < 100 && button instanceof GuiOptionButton) {
/*     */       
/*  72 */       this.options.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
/*  73 */       button.displayString = this.options.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/*  79 */     if (this.buttonId != null) {
/*     */       
/*  81 */       this.options.setOptionKeyBinding(this.buttonId, -100 + mouseButton);
/*  82 */       this.buttonId = null;
/*  83 */       KeyBinding.resetKeyBindingArrayAndHash();
/*     */     }
/*  85 */     else if (mouseButton != 0 || !this.keyBindingList.mouseClicked(mouseX, mouseY, mouseButton)) {
/*     */       
/*  87 */       super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseReleased(int mouseX, int mouseY, int state) {
/*  93 */     if (state != 0 || !this.keyBindingList.mouseReleased(mouseX, mouseY, state))
/*     */     {
/*  95 */       super.mouseReleased(mouseX, mouseY, state);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/* 101 */     if (this.buttonId != null) {
/*     */       
/* 103 */       if (keyCode == 1) {
/*     */         
/* 105 */         this.options.setOptionKeyBinding(this.buttonId, 0);
/*     */       }
/* 107 */       else if (keyCode != 0) {
/*     */         
/* 109 */         this.options.setOptionKeyBinding(this.buttonId, keyCode);
/*     */       }
/* 111 */       else if (typedChar > '\000') {
/*     */         
/* 113 */         this.options.setOptionKeyBinding(this.buttonId, typedChar + 256);
/*     */       } 
/*     */       
/* 116 */       this.buttonId = null;
/* 117 */       this.time = Minecraft.getSystemTime();
/* 118 */       KeyBinding.resetKeyBindingArrayAndHash();
/*     */     }
/*     */     else {
/*     */       
/* 122 */       super.keyTyped(typedChar, keyCode);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 128 */     drawDefaultBackground();
/* 129 */     this.keyBindingList.drawScreen(mouseX, mouseY, partialTicks);
/* 130 */     drawCenteredString(this.fontRendererObj, this.screenTitle, this.width / 2, 8, 16777215);
/* 131 */     boolean flag = true;
/*     */     
/* 133 */     for (KeyBinding keybinding : this.options.keyBindings) {
/*     */       
/* 135 */       if (keybinding.getKeyCode() != keybinding.getKeyCodeDefault()) {
/*     */         
/* 137 */         flag = false;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 142 */     this.buttonReset.enabled = !flag;
/* 143 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiControls.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */