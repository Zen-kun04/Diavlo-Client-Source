/*    */ package net.optifine.gui;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.gui.GuiOptionButton;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import net.minecraft.client.settings.GameSettings;
/*    */ 
/*    */ public class GuiDetailSettingsOF
/*    */   extends GuiScreen {
/*    */   private GuiScreen prevScreen;
/*    */   protected String title;
/*    */   private GameSettings settings;
/* 14 */   private static GameSettings.Options[] enumOptions = new GameSettings.Options[] { GameSettings.Options.CLOUDS, GameSettings.Options.CLOUD_HEIGHT, GameSettings.Options.TREES, GameSettings.Options.RAIN, GameSettings.Options.SKY, GameSettings.Options.STARS, GameSettings.Options.SUN_MOON, GameSettings.Options.SHOW_CAPES, GameSettings.Options.FOG_FANCY, GameSettings.Options.FOG_START, GameSettings.Options.TRANSLUCENT_BLOCKS, GameSettings.Options.HELD_ITEM_TOOLTIPS, GameSettings.Options.DROPPED_ITEMS, GameSettings.Options.ENTITY_SHADOWS, GameSettings.Options.VIGNETTE, GameSettings.Options.ALTERNATE_BLOCKS, GameSettings.Options.SWAMP_COLORS, GameSettings.Options.SMOOTH_BIOMES };
/* 15 */   private TooltipManager tooltipManager = new TooltipManager(this, new TooltipProviderOptions());
/*    */ 
/*    */   
/*    */   public GuiDetailSettingsOF(GuiScreen guiscreen, GameSettings gamesettings) {
/* 19 */     this.prevScreen = guiscreen;
/* 20 */     this.settings = gamesettings;
/*    */   }
/*    */ 
/*    */   
/*    */   public void initGui() {
/* 25 */     this.title = I18n.format("of.options.detailsTitle", new Object[0]);
/* 26 */     this.buttonList.clear();
/*    */     
/* 28 */     for (int i = 0; i < enumOptions.length; i++) {
/*    */       
/* 30 */       GameSettings.Options gamesettings$options = enumOptions[i];
/* 31 */       int j = this.width / 2 - 155 + i % 2 * 160;
/* 32 */       int k = this.height / 6 + 21 * i / 2 - 12;
/*    */       
/* 34 */       if (!gamesettings$options.getEnumFloat()) {
/*    */         
/* 36 */         this.buttonList.add(new GuiOptionButtonOF(gamesettings$options.returnEnumOrdinal(), j, k, gamesettings$options, this.settings.getKeyBinding(gamesettings$options)));
/*    */       }
/*    */       else {
/*    */         
/* 40 */         this.buttonList.add(new GuiOptionSliderOF(gamesettings$options.returnEnumOrdinal(), j, k, gamesettings$options));
/*    */       } 
/*    */     } 
/*    */     
/* 44 */     this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168 + 11, I18n.format("gui.done", new Object[0])));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void actionPerformed(GuiButton guibutton) {
/* 49 */     if (guibutton.enabled) {
/*    */       
/* 51 */       if (guibutton.id < 200 && guibutton instanceof GuiOptionButton) {
/*    */         
/* 53 */         this.settings.setOptionValue(((GuiOptionButton)guibutton).returnEnumOptions(), 1);
/* 54 */         guibutton.displayString = this.settings.getKeyBinding(GameSettings.Options.getEnumOptions(guibutton.id));
/*    */       } 
/*    */       
/* 57 */       if (guibutton.id == 200) {
/*    */         
/* 59 */         this.mc.gameSettings.saveOptions();
/* 60 */         this.mc.displayGuiScreen(this.prevScreen);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawScreen(int x, int y, float f) {
/* 67 */     drawDefaultBackground();
/* 68 */     drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 15, 16777215);
/* 69 */     super.drawScreen(x, y, f);
/* 70 */     this.tooltipManager.drawTooltips(x, y, this.buttonList);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\gui\GuiDetailSettingsOF.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */