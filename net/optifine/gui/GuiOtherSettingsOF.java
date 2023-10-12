/*    */ package net.optifine.gui;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.gui.GuiOptionButton;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.gui.GuiYesNo;
/*    */ import net.minecraft.client.gui.GuiYesNoCallback;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import net.minecraft.client.settings.GameSettings;
/*    */ 
/*    */ public class GuiOtherSettingsOF extends GuiScreen implements GuiYesNoCallback {
/*    */   private GuiScreen prevScreen;
/* 12 */   private static GameSettings.Options[] enumOptions = new GameSettings.Options[] { GameSettings.Options.LAGOMETER, GameSettings.Options.PROFILER, GameSettings.Options.SHOW_FPS, GameSettings.Options.ADVANCED_TOOLTIPS, GameSettings.Options.WEATHER, GameSettings.Options.TIME, GameSettings.Options.USE_FULLSCREEN, GameSettings.Options.FULLSCREEN_MODE, GameSettings.Options.ANAGLYPH, GameSettings.Options.AUTOSAVE_TICKS, GameSettings.Options.SCREENSHOT_SIZE, GameSettings.Options.SHOW_GL_ERRORS }; protected String title; private GameSettings settings;
/* 13 */   private TooltipManager tooltipManager = new TooltipManager(this, new TooltipProviderOptions());
/*    */ 
/*    */   
/*    */   public GuiOtherSettingsOF(GuiScreen guiscreen, GameSettings gamesettings) {
/* 17 */     this.prevScreen = guiscreen;
/* 18 */     this.settings = gamesettings;
/*    */   }
/*    */ 
/*    */   
/*    */   public void initGui() {
/* 23 */     this.title = I18n.format("of.options.otherTitle", new Object[0]);
/* 24 */     this.buttonList.clear();
/*    */     
/* 26 */     for (int i = 0; i < enumOptions.length; i++) {
/*    */       
/* 28 */       GameSettings.Options gamesettings$options = enumOptions[i];
/* 29 */       int j = this.width / 2 - 155 + i % 2 * 160;
/* 30 */       int k = this.height / 6 + 21 * i / 2 - 12;
/*    */       
/* 32 */       if (!gamesettings$options.getEnumFloat()) {
/*    */         
/* 34 */         this.buttonList.add(new GuiOptionButtonOF(gamesettings$options.returnEnumOrdinal(), j, k, gamesettings$options, this.settings.getKeyBinding(gamesettings$options)));
/*    */       }
/*    */       else {
/*    */         
/* 38 */         this.buttonList.add(new GuiOptionSliderOF(gamesettings$options.returnEnumOrdinal(), j, k, gamesettings$options));
/*    */       } 
/*    */     } 
/*    */     
/* 42 */     this.buttonList.add(new GuiButton(210, this.width / 2 - 100, this.height / 6 + 168 + 11 - 44, I18n.format("of.options.other.reset", new Object[0])));
/* 43 */     this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168 + 11, I18n.format("gui.done", new Object[0])));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void actionPerformed(GuiButton guibutton) {
/* 48 */     if (guibutton.enabled) {
/*    */       
/* 50 */       if (guibutton.id < 200 && guibutton instanceof GuiOptionButton) {
/*    */         
/* 52 */         this.settings.setOptionValue(((GuiOptionButton)guibutton).returnEnumOptions(), 1);
/* 53 */         guibutton.displayString = this.settings.getKeyBinding(GameSettings.Options.getEnumOptions(guibutton.id));
/*    */       } 
/*    */       
/* 56 */       if (guibutton.id == 200) {
/*    */         
/* 58 */         this.mc.gameSettings.saveOptions();
/* 59 */         this.mc.displayGuiScreen(this.prevScreen);
/*    */       } 
/*    */       
/* 62 */       if (guibutton.id == 210) {
/*    */         
/* 64 */         this.mc.gameSettings.saveOptions();
/* 65 */         GuiYesNo guiyesno = new GuiYesNo(this, I18n.format("of.message.other.reset", new Object[0]), "", 9999);
/* 66 */         this.mc.displayGuiScreen((GuiScreen)guiyesno);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void confirmClicked(boolean flag, int i) {
/* 73 */     if (flag)
/*    */     {
/* 75 */       this.mc.gameSettings.resetSettings();
/*    */     }
/*    */     
/* 78 */     this.mc.displayGuiScreen(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawScreen(int x, int y, float f) {
/* 83 */     drawDefaultBackground();
/* 84 */     drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 15, 16777215);
/* 85 */     super.drawScreen(x, y, f);
/* 86 */     this.tooltipManager.drawTooltips(x, y, this.buttonList);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\gui\GuiOtherSettingsOF.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */