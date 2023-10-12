/*     */ package net.optifine.shaders.gui;
/*     */ 
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.src.Config;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.optifine.Lang;
/*     */ import net.optifine.gui.GuiScreenOF;
/*     */ import net.optifine.gui.TooltipManager;
/*     */ import net.optifine.gui.TooltipProvider;
/*     */ import net.optifine.gui.TooltipProviderShaderOptions;
/*     */ import net.optifine.shaders.Shaders;
/*     */ import net.optifine.shaders.config.ShaderOption;
/*     */ import net.optifine.shaders.config.ShaderOptionProfile;
/*     */ import net.optifine.shaders.config.ShaderOptionScreen;
/*     */ 
/*     */ public class GuiShaderOptions
/*     */   extends GuiScreenOF {
/*     */   private GuiScreen prevScreen;
/*     */   protected String title;
/*     */   private GameSettings settings;
/*     */   private TooltipManager tooltipManager;
/*     */   private String screenName;
/*     */   private String screenText;
/*     */   private boolean changed;
/*     */   public static final String OPTION_PROFILE = "<profile>";
/*     */   public static final String OPTION_EMPTY = "<empty>";
/*     */   public static final String OPTION_REST = "*";
/*     */   
/*     */   public GuiShaderOptions(GuiScreen guiscreen, GameSettings gamesettings) {
/*  34 */     this.tooltipManager = new TooltipManager((GuiScreen)this, (TooltipProvider)new TooltipProviderShaderOptions());
/*  35 */     this.screenName = null;
/*  36 */     this.screenText = null;
/*  37 */     this.changed = false;
/*  38 */     this.title = "Shader Options";
/*  39 */     this.prevScreen = guiscreen;
/*  40 */     this.settings = gamesettings;
/*     */   }
/*     */ 
/*     */   
/*     */   public GuiShaderOptions(GuiScreen guiscreen, GameSettings gamesettings, String screenName) {
/*  45 */     this(guiscreen, gamesettings);
/*  46 */     this.screenName = screenName;
/*     */     
/*  48 */     if (screenName != null)
/*     */     {
/*  50 */       this.screenText = Shaders.translate("screen." + screenName, screenName);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  56 */     this.title = I18n.format("of.options.shaderOptionsTitle", new Object[0]);
/*  57 */     int i = 100;
/*  58 */     int j = 0;
/*  59 */     int k = 30;
/*  60 */     int l = 20;
/*  61 */     int i1 = 120;
/*  62 */     int j1 = 20;
/*  63 */     int k1 = Shaders.getShaderPackColumns(this.screenName, 2);
/*  64 */     ShaderOption[] ashaderoption = Shaders.getShaderPackOptions(this.screenName);
/*     */     
/*  66 */     if (ashaderoption != null) {
/*     */       
/*  68 */       int l1 = MathHelper.ceiling_double_int(ashaderoption.length / 9.0D);
/*     */       
/*  70 */       if (k1 < l1)
/*     */       {
/*  72 */         k1 = l1;
/*     */       }
/*     */       
/*  75 */       for (int i2 = 0; i2 < ashaderoption.length; i2++) {
/*     */         
/*  77 */         ShaderOption shaderoption = ashaderoption[i2];
/*     */         
/*  79 */         if (shaderoption != null && shaderoption.isVisible()) {
/*     */           GuiButtonShaderOption guibuttonshaderoption;
/*  81 */           int j2 = i2 % k1;
/*  82 */           int k2 = i2 / k1;
/*  83 */           int l2 = Math.min(this.width / k1, 200);
/*  84 */           j = (this.width - l2 * k1) / 2;
/*  85 */           int i3 = j2 * l2 + 5 + j;
/*  86 */           int j3 = k + k2 * l;
/*  87 */           int k3 = l2 - 10;
/*  88 */           String s = getButtonText(shaderoption, k3);
/*     */ 
/*     */           
/*  91 */           if (Shaders.isShaderPackOptionSlider(shaderoption.getName())) {
/*     */             
/*  93 */             guibuttonshaderoption = new GuiSliderShaderOption(i + i2, i3, j3, k3, j1, shaderoption, s);
/*     */           }
/*     */           else {
/*     */             
/*  97 */             guibuttonshaderoption = new GuiButtonShaderOption(i + i2, i3, j3, k3, j1, shaderoption, s);
/*     */           } 
/*     */           
/* 100 */           guibuttonshaderoption.enabled = shaderoption.isEnabled();
/* 101 */           this.buttonList.add(guibuttonshaderoption);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 106 */     this.buttonList.add(new GuiButton(201, this.width / 2 - i1 - 20, this.height / 6 + 168 + 11, i1, j1, I18n.format("controls.reset", new Object[0])));
/* 107 */     this.buttonList.add(new GuiButton(200, this.width / 2 + 20, this.height / 6 + 168 + 11, i1, j1, I18n.format("gui.done", new Object[0])));
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getButtonText(ShaderOption so, int btnWidth) {
/* 112 */     String s = so.getNameText();
/*     */     
/* 114 */     if (so instanceof ShaderOptionScreen) {
/*     */       
/* 116 */       ShaderOptionScreen shaderoptionscreen = (ShaderOptionScreen)so;
/* 117 */       return s + "...";
/*     */     } 
/*     */ 
/*     */     
/* 121 */     FontRenderer fontrenderer = (Config.getMinecraft()).fontRendererObj;
/*     */     
/* 123 */     for (int i = fontrenderer.getStringWidth(": " + Lang.getOff()) + 5; fontrenderer.getStringWidth(s) + i >= btnWidth && s.length() > 0; s = s.substring(0, s.length() - 1));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 128 */     String s1 = so.isChanged() ? so.getValueColor(so.getValue()) : "";
/* 129 */     String s2 = so.getValueText(so.getValue());
/* 130 */     return s + ": " + s1 + s2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton guibutton) {
/* 136 */     if (guibutton.enabled) {
/*     */       
/* 138 */       if (guibutton.id < 200 && guibutton instanceof GuiButtonShaderOption) {
/*     */         
/* 140 */         GuiButtonShaderOption guibuttonshaderoption = (GuiButtonShaderOption)guibutton;
/* 141 */         ShaderOption shaderoption = guibuttonshaderoption.getShaderOption();
/*     */         
/* 143 */         if (shaderoption instanceof ShaderOptionScreen) {
/*     */           
/* 145 */           String s = shaderoption.getName();
/* 146 */           GuiShaderOptions guishaderoptions = new GuiShaderOptions((GuiScreen)this, this.settings, s);
/* 147 */           this.mc.displayGuiScreen((GuiScreen)guishaderoptions);
/*     */           
/*     */           return;
/*     */         } 
/* 151 */         if (isShiftKeyDown()) {
/*     */           
/* 153 */           shaderoption.resetValue();
/*     */         }
/* 155 */         else if (guibuttonshaderoption.isSwitchable()) {
/*     */           
/* 157 */           shaderoption.nextValue();
/*     */         } 
/*     */         
/* 160 */         updateAllButtons();
/* 161 */         this.changed = true;
/*     */       } 
/*     */       
/* 164 */       if (guibutton.id == 201) {
/*     */         
/* 166 */         ShaderOption[] ashaderoption = Shaders.getChangedOptions(Shaders.getShaderPackOptions());
/*     */         
/* 168 */         for (int i = 0; i < ashaderoption.length; i++) {
/*     */           
/* 170 */           ShaderOption shaderoption1 = ashaderoption[i];
/* 171 */           shaderoption1.resetValue();
/* 172 */           this.changed = true;
/*     */         } 
/*     */         
/* 175 */         updateAllButtons();
/*     */       } 
/*     */       
/* 178 */       if (guibutton.id == 200) {
/*     */         
/* 180 */         if (this.changed) {
/*     */           
/* 182 */           Shaders.saveShaderPackOptions();
/* 183 */           this.changed = false;
/* 184 */           Shaders.uninit();
/*     */         } 
/*     */         
/* 187 */         this.mc.displayGuiScreen(this.prevScreen);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformedRightClick(GuiButton btn) {
/* 194 */     if (btn instanceof GuiButtonShaderOption) {
/*     */       
/* 196 */       GuiButtonShaderOption guibuttonshaderoption = (GuiButtonShaderOption)btn;
/* 197 */       ShaderOption shaderoption = guibuttonshaderoption.getShaderOption();
/*     */       
/* 199 */       if (isShiftKeyDown()) {
/*     */         
/* 201 */         shaderoption.resetValue();
/*     */       }
/* 203 */       else if (guibuttonshaderoption.isSwitchable()) {
/*     */         
/* 205 */         shaderoption.prevValue();
/*     */       } 
/*     */       
/* 208 */       updateAllButtons();
/* 209 */       this.changed = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onGuiClosed() {
/* 215 */     super.onGuiClosed();
/*     */     
/* 217 */     if (this.changed) {
/*     */       
/* 219 */       Shaders.saveShaderPackOptions();
/* 220 */       this.changed = false;
/* 221 */       Shaders.uninit();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateAllButtons() {
/* 227 */     for (GuiButton guibutton : this.buttonList) {
/*     */       
/* 229 */       if (guibutton instanceof GuiButtonShaderOption) {
/*     */         
/* 231 */         GuiButtonShaderOption guibuttonshaderoption = (GuiButtonShaderOption)guibutton;
/* 232 */         ShaderOption shaderoption = guibuttonshaderoption.getShaderOption();
/*     */         
/* 234 */         if (shaderoption instanceof ShaderOptionProfile) {
/*     */           
/* 236 */           ShaderOptionProfile shaderoptionprofile = (ShaderOptionProfile)shaderoption;
/* 237 */           shaderoptionprofile.updateProfile();
/*     */         } 
/*     */         
/* 240 */         guibuttonshaderoption.displayString = getButtonText(shaderoption, guibuttonshaderoption.getButtonWidth());
/* 241 */         guibuttonshaderoption.valueChanged();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int x, int y, float f) {
/* 248 */     drawDefaultBackground();
/*     */     
/* 250 */     if (this.screenText != null) {
/*     */       
/* 252 */       drawCenteredString(this.fontRendererObj, this.screenText, this.width / 2, 15, 16777215);
/*     */     }
/*     */     else {
/*     */       
/* 256 */       drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 15, 16777215);
/*     */     } 
/*     */     
/* 259 */     super.drawScreen(x, y, f);
/* 260 */     this.tooltipManager.drawTooltips(x, y, this.buttonList);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\shaders\gui\GuiShaderOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */