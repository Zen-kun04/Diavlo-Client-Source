/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.src.Config;
/*     */ import net.optifine.Lang;
/*     */ import net.optifine.gui.GuiAnimationSettingsOF;
/*     */ import net.optifine.gui.GuiDetailSettingsOF;
/*     */ import net.optifine.gui.GuiOptionButtonOF;
/*     */ import net.optifine.gui.GuiOptionSliderOF;
/*     */ import net.optifine.gui.GuiOtherSettingsOF;
/*     */ import net.optifine.gui.GuiPerformanceSettingsOF;
/*     */ import net.optifine.gui.GuiQualitySettingsOF;
/*     */ import net.optifine.gui.GuiScreenOF;
/*     */ import net.optifine.gui.TooltipManager;
/*     */ import net.optifine.gui.TooltipProvider;
/*     */ import net.optifine.gui.TooltipProviderOptions;
/*     */ import net.optifine.shaders.gui.GuiShaders;
/*     */ 
/*     */ public class GuiVideoSettings extends GuiScreenOF {
/*     */   private GuiScreen parentGuiScreen;
/*  23 */   protected String screenTitle = "Video Settings";
/*     */   private GameSettings guiGameSettings;
/*  25 */   private static GameSettings.Options[] videoOptions = new GameSettings.Options[] { GameSettings.Options.GRAPHICS, GameSettings.Options.RENDER_DISTANCE, GameSettings.Options.AMBIENT_OCCLUSION, GameSettings.Options.FRAMERATE_LIMIT, GameSettings.Options.AO_LEVEL, GameSettings.Options.VIEW_BOBBING, GameSettings.Options.GUI_SCALE, GameSettings.Options.USE_VBO, GameSettings.Options.GAMMA, GameSettings.Options.BLOCK_ALTERNATIVES, GameSettings.Options.DYNAMIC_LIGHTS, GameSettings.Options.DYNAMIC_FOV };
/*  26 */   private TooltipManager tooltipManager = new TooltipManager((GuiScreen)this, (TooltipProvider)new TooltipProviderOptions());
/*     */ 
/*     */   
/*     */   public GuiVideoSettings(GuiScreen parentScreenIn, GameSettings gameSettingsIn) {
/*  30 */     this.parentGuiScreen = parentScreenIn;
/*  31 */     this.guiGameSettings = gameSettingsIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  36 */     this.screenTitle = I18n.format("options.videoTitle", new Object[0]);
/*  37 */     this.buttonList.clear();
/*     */     
/*  39 */     for (int i = 0; i < videoOptions.length; i++) {
/*     */       
/*  41 */       GameSettings.Options gamesettings$options = videoOptions[i];
/*     */       
/*  43 */       if (gamesettings$options != null) {
/*     */         
/*  45 */         int j = this.width / 2 - 155 + i % 2 * 160;
/*  46 */         int k = this.height / 6 + 21 * i / 2 - 12;
/*     */         
/*  48 */         if (gamesettings$options.getEnumFloat()) {
/*     */           
/*  50 */           this.buttonList.add(new GuiOptionSliderOF(gamesettings$options.returnEnumOrdinal(), j, k, gamesettings$options));
/*     */         }
/*     */         else {
/*     */           
/*  54 */           this.buttonList.add(new GuiOptionButtonOF(gamesettings$options.returnEnumOrdinal(), j, k, gamesettings$options, this.guiGameSettings.getKeyBinding(gamesettings$options)));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  59 */     int l = this.height / 6 + 21 * videoOptions.length / 2 - 12;
/*  60 */     int i1 = 0;
/*  61 */     i1 = this.width / 2 - 155 + 0;
/*  62 */     this.buttonList.add(new GuiOptionButton(231, i1, l, Lang.get("of.options.shaders")));
/*  63 */     i1 = this.width / 2 - 155 + 160;
/*  64 */     this.buttonList.add(new GuiOptionButton(202, i1, l, Lang.get("of.options.quality")));
/*  65 */     l += 21;
/*  66 */     i1 = this.width / 2 - 155 + 0;
/*  67 */     this.buttonList.add(new GuiOptionButton(201, i1, l, Lang.get("of.options.details")));
/*  68 */     i1 = this.width / 2 - 155 + 160;
/*  69 */     this.buttonList.add(new GuiOptionButton(212, i1, l, Lang.get("of.options.performance")));
/*  70 */     l += 21;
/*  71 */     i1 = this.width / 2 - 155 + 0;
/*  72 */     this.buttonList.add(new GuiOptionButton(211, i1, l, Lang.get("of.options.animations")));
/*  73 */     i1 = this.width / 2 - 155 + 160;
/*  74 */     this.buttonList.add(new GuiOptionButton(222, i1, l, Lang.get("of.options.other")));
/*  75 */     l += 21;
/*  76 */     this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168 + 11, I18n.format("gui.done", new Object[0])));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/*  81 */     actionPerformed(button, 1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformedRightClick(GuiButton p_actionPerformedRightClick_1_) {
/*  86 */     if (p_actionPerformedRightClick_1_.id == GameSettings.Options.GUI_SCALE.ordinal())
/*     */     {
/*  88 */       actionPerformed(p_actionPerformedRightClick_1_, -1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void actionPerformed(GuiButton p_actionPerformed_1_, int p_actionPerformed_2_) {
/*  94 */     if (p_actionPerformed_1_.enabled) {
/*     */       
/*  96 */       int i = this.guiGameSettings.guiScale;
/*     */       
/*  98 */       if (p_actionPerformed_1_.id < 200 && p_actionPerformed_1_ instanceof GuiOptionButton) {
/*     */         
/* 100 */         this.guiGameSettings.setOptionValue(((GuiOptionButton)p_actionPerformed_1_).returnEnumOptions(), p_actionPerformed_2_);
/* 101 */         p_actionPerformed_1_.displayString = this.guiGameSettings.getKeyBinding(GameSettings.Options.getEnumOptions(p_actionPerformed_1_.id));
/*     */       } 
/*     */       
/* 104 */       if (p_actionPerformed_1_.id == 200) {
/*     */         
/* 106 */         this.mc.gameSettings.saveOptions();
/* 107 */         this.mc.displayGuiScreen(this.parentGuiScreen);
/*     */       } 
/*     */       
/* 110 */       if (this.guiGameSettings.guiScale != i) {
/*     */         
/* 112 */         ScaledResolution scaledresolution = new ScaledResolution(this.mc);
/* 113 */         int j = scaledresolution.getScaledWidth();
/* 114 */         int k = scaledresolution.getScaledHeight();
/* 115 */         setWorldAndResolution(this.mc, j, k);
/*     */       } 
/*     */       
/* 118 */       if (p_actionPerformed_1_.id == 201) {
/*     */         
/* 120 */         this.mc.gameSettings.saveOptions();
/* 121 */         GuiDetailSettingsOF guidetailsettingsof = new GuiDetailSettingsOF((GuiScreen)this, this.guiGameSettings);
/* 122 */         this.mc.displayGuiScreen((GuiScreen)guidetailsettingsof);
/*     */       } 
/*     */       
/* 125 */       if (p_actionPerformed_1_.id == 202) {
/*     */         
/* 127 */         this.mc.gameSettings.saveOptions();
/* 128 */         GuiQualitySettingsOF guiqualitysettingsof = new GuiQualitySettingsOF((GuiScreen)this, this.guiGameSettings);
/* 129 */         this.mc.displayGuiScreen((GuiScreen)guiqualitysettingsof);
/*     */       } 
/*     */       
/* 132 */       if (p_actionPerformed_1_.id == 211) {
/*     */         
/* 134 */         this.mc.gameSettings.saveOptions();
/* 135 */         GuiAnimationSettingsOF guianimationsettingsof = new GuiAnimationSettingsOF((GuiScreen)this, this.guiGameSettings);
/* 136 */         this.mc.displayGuiScreen((GuiScreen)guianimationsettingsof);
/*     */       } 
/*     */       
/* 139 */       if (p_actionPerformed_1_.id == 212) {
/*     */         
/* 141 */         this.mc.gameSettings.saveOptions();
/* 142 */         GuiPerformanceSettingsOF guiperformancesettingsof = new GuiPerformanceSettingsOF((GuiScreen)this, this.guiGameSettings);
/* 143 */         this.mc.displayGuiScreen((GuiScreen)guiperformancesettingsof);
/*     */       } 
/*     */       
/* 146 */       if (p_actionPerformed_1_.id == 222) {
/*     */         
/* 148 */         this.mc.gameSettings.saveOptions();
/* 149 */         GuiOtherSettingsOF guiothersettingsof = new GuiOtherSettingsOF((GuiScreen)this, this.guiGameSettings);
/* 150 */         this.mc.displayGuiScreen((GuiScreen)guiothersettingsof);
/*     */       } 
/*     */       
/* 153 */       if (p_actionPerformed_1_.id == 231) {
/*     */         
/* 155 */         if (Config.isAntialiasing() || Config.isAntialiasingConfigured()) {
/*     */           
/* 157 */           Config.showGuiMessage(Lang.get("of.message.shaders.aa1"), Lang.get("of.message.shaders.aa2"));
/*     */           
/*     */           return;
/*     */         } 
/* 161 */         if (Config.isAnisotropicFiltering()) {
/*     */           
/* 163 */           Config.showGuiMessage(Lang.get("of.message.shaders.af1"), Lang.get("of.message.shaders.af2"));
/*     */           
/*     */           return;
/*     */         } 
/* 167 */         if (Config.isFastRender()) {
/*     */           
/* 169 */           Config.showGuiMessage(Lang.get("of.message.shaders.fr1"), Lang.get("of.message.shaders.fr2"));
/*     */           
/*     */           return;
/*     */         } 
/* 173 */         if ((Config.getGameSettings()).anaglyph) {
/*     */           
/* 175 */           Config.showGuiMessage(Lang.get("of.message.shaders.an1"), Lang.get("of.message.shaders.an2"));
/*     */           
/*     */           return;
/*     */         } 
/* 179 */         this.mc.gameSettings.saveOptions();
/* 180 */         GuiShaders guishaders = new GuiShaders((GuiScreen)this, this.guiGameSettings);
/* 181 */         this.mc.displayGuiScreen((GuiScreen)guishaders);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 188 */     drawDefaultBackground();
/* 189 */     drawCenteredString(this.fontRendererObj, this.screenTitle, this.width / 2, 15, 16777215);
/* 190 */     String s = Config.getVersion();
/* 191 */     String s1 = "HD_U";
/*     */     
/* 193 */     if (s1.equals("HD"))
/*     */     {
/* 195 */       s = "OptiFine HD M6_pre2";
/*     */     }
/*     */     
/* 198 */     if (s1.equals("HD_U"))
/*     */     {
/* 200 */       s = "OptiFine HD M6_pre2 Ultra";
/*     */     }
/*     */     
/* 203 */     if (s1.equals("L"))
/*     */     {
/* 205 */       s = "OptiFine M6_pre2 Light";
/*     */     }
/*     */     
/* 208 */     drawString(this.fontRendererObj, s, 2, this.height - 10, 8421504);
/* 209 */     String s2 = "Minecraft 1.8.9";
/* 210 */     int i = this.fontRendererObj.getStringWidth(s2);
/* 211 */     drawString(this.fontRendererObj, s2, this.width - i - 2, this.height - 10, 8421504);
/* 212 */     super.drawScreen(mouseX, mouseY, partialTicks);
/* 213 */     this.tooltipManager.drawTooltips(mouseX, mouseY, this.buttonList);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getButtonWidth(GuiButton p_getButtonWidth_0_) {
/* 218 */     return p_getButtonWidth_0_.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getButtonHeight(GuiButton p_getButtonHeight_0_) {
/* 223 */     return p_getButtonHeight_0_.height;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void drawGradientRect(GuiScreen p_drawGradientRect_0_, int p_drawGradientRect_1_, int p_drawGradientRect_2_, int p_drawGradientRect_3_, int p_drawGradientRect_4_, int p_drawGradientRect_5_, int p_drawGradientRect_6_) {
/* 228 */     p_drawGradientRect_0_.drawGradientRect(p_drawGradientRect_1_, p_drawGradientRect_2_, p_drawGradientRect_3_, p_drawGradientRect_4_, p_drawGradientRect_5_, p_drawGradientRect_6_);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getGuiChatText(GuiChat p_getGuiChatText_0_) {
/* 233 */     return p_getGuiChatText_0_.inputField.getText();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiVideoSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */