/*    */ package net.optifine.gui;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.gui.GuiOptionButton;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import net.minecraft.client.settings.GameSettings;
/*    */ import net.optifine.Lang;
/*    */ 
/*    */ public class GuiAnimationSettingsOF
/*    */   extends GuiScreen {
/*    */   private GuiScreen prevScreen;
/*    */   protected String title;
/*    */   private GameSettings settings;
/* 16 */   private static GameSettings.Options[] enumOptions = new GameSettings.Options[] { GameSettings.Options.ANIMATED_WATER, GameSettings.Options.ANIMATED_LAVA, GameSettings.Options.ANIMATED_FIRE, GameSettings.Options.ANIMATED_PORTAL, GameSettings.Options.ANIMATED_REDSTONE, GameSettings.Options.ANIMATED_EXPLOSION, GameSettings.Options.ANIMATED_FLAME, GameSettings.Options.ANIMATED_SMOKE, GameSettings.Options.VOID_PARTICLES, GameSettings.Options.WATER_PARTICLES, GameSettings.Options.RAIN_SPLASH, GameSettings.Options.PORTAL_PARTICLES, GameSettings.Options.POTION_PARTICLES, GameSettings.Options.DRIPPING_WATER_LAVA, GameSettings.Options.ANIMATED_TERRAIN, GameSettings.Options.ANIMATED_TEXTURES, GameSettings.Options.FIREWORK_PARTICLES, GameSettings.Options.PARTICLES };
/*    */ 
/*    */   
/*    */   public GuiAnimationSettingsOF(GuiScreen guiscreen, GameSettings gamesettings) {
/* 20 */     this.prevScreen = guiscreen;
/* 21 */     this.settings = gamesettings;
/*    */   }
/*    */ 
/*    */   
/*    */   public void initGui() {
/* 26 */     this.title = I18n.format("of.options.animationsTitle", new Object[0]);
/* 27 */     this.buttonList.clear();
/*    */     
/* 29 */     for (int i = 0; i < enumOptions.length; i++) {
/*    */       
/* 31 */       GameSettings.Options gamesettings$options = enumOptions[i];
/* 32 */       int j = this.width / 2 - 155 + i % 2 * 160;
/* 33 */       int k = this.height / 6 + 21 * i / 2 - 12;
/*    */       
/* 35 */       if (!gamesettings$options.getEnumFloat()) {
/*    */         
/* 37 */         this.buttonList.add(new GuiOptionButtonOF(gamesettings$options.returnEnumOrdinal(), j, k, gamesettings$options, this.settings.getKeyBinding(gamesettings$options)));
/*    */       }
/*    */       else {
/*    */         
/* 41 */         this.buttonList.add(new GuiOptionSliderOF(gamesettings$options.returnEnumOrdinal(), j, k, gamesettings$options));
/*    */       } 
/*    */     } 
/*    */     
/* 45 */     this.buttonList.add(new GuiButton(210, this.width / 2 - 155, this.height / 6 + 168 + 11, 70, 20, Lang.get("of.options.animation.allOn")));
/* 46 */     this.buttonList.add(new GuiButton(211, this.width / 2 - 155 + 80, this.height / 6 + 168 + 11, 70, 20, Lang.get("of.options.animation.allOff")));
/* 47 */     this.buttonList.add(new GuiOptionButton(200, this.width / 2 + 5, this.height / 6 + 168 + 11, I18n.format("gui.done", new Object[0])));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void actionPerformed(GuiButton guibutton) {
/* 52 */     if (guibutton.enabled) {
/*    */       
/* 54 */       if (guibutton.id < 200 && guibutton instanceof GuiOptionButton) {
/*    */         
/* 56 */         this.settings.setOptionValue(((GuiOptionButton)guibutton).returnEnumOptions(), 1);
/* 57 */         guibutton.displayString = this.settings.getKeyBinding(GameSettings.Options.getEnumOptions(guibutton.id));
/*    */       } 
/*    */       
/* 60 */       if (guibutton.id == 200) {
/*    */         
/* 62 */         this.mc.gameSettings.saveOptions();
/* 63 */         this.mc.displayGuiScreen(this.prevScreen);
/*    */       } 
/*    */       
/* 66 */       if (guibutton.id == 210)
/*    */       {
/* 68 */         this.mc.gameSettings.setAllAnimations(true);
/*    */       }
/*    */       
/* 71 */       if (guibutton.id == 211)
/*    */       {
/* 73 */         this.mc.gameSettings.setAllAnimations(false);
/*    */       }
/*    */       
/* 76 */       ScaledResolution scaledresolution = new ScaledResolution(this.mc);
/* 77 */       setWorldAndResolution(this.mc, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawScreen(int x, int y, float f) {
/* 83 */     drawDefaultBackground();
/* 84 */     drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 15, 16777215);
/* 85 */     super.drawScreen(x, y, f);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\gui\GuiAnimationSettingsOF.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */