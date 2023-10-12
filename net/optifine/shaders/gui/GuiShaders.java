/*     */ package net.optifine.shaders.gui;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.src.Config;
/*     */ import net.optifine.Lang;
/*     */ import net.optifine.gui.GuiScreenOF;
/*     */ import net.optifine.gui.TooltipManager;
/*     */ import net.optifine.gui.TooltipProvider;
/*     */ import net.optifine.gui.TooltipProviderEnumShaderOptions;
/*     */ import net.optifine.shaders.Shaders;
/*     */ import net.optifine.shaders.ShadersTex;
/*     */ import net.optifine.shaders.config.EnumShaderOption;
/*     */ import org.lwjgl.Sys;
/*     */ 
/*     */ public class GuiShaders
/*     */   extends GuiScreenOF {
/*     */   protected GuiScreen parentGui;
/*  25 */   protected String screenTitle = "Shaders";
/*  26 */   private TooltipManager tooltipManager = new TooltipManager((GuiScreen)this, (TooltipProvider)new TooltipProviderEnumShaderOptions());
/*  27 */   private int updateTimer = -1;
/*     */   private GuiSlotShaders shaderList;
/*     */   private boolean saved = false;
/*  30 */   private static float[] QUALITY_MULTIPLIERS = new float[] { 0.5F, 0.6F, 0.6666667F, 0.75F, 0.8333333F, 0.9F, 1.0F, 1.1666666F, 1.3333334F, 1.5F, 1.6666666F, 1.8F, 2.0F };
/*  31 */   private static String[] QUALITY_MULTIPLIER_NAMES = new String[] { "0.5x", "0.6x", "0.66x", "0.75x", "0.83x", "0.9x", "1x", "1.16x", "1.33x", "1.5x", "1.66x", "1.8x", "2x" };
/*  32 */   private static float QUALITY_MULTIPLIER_DEFAULT = 1.0F;
/*  33 */   private static float[] HAND_DEPTH_VALUES = new float[] { 0.0625F, 0.125F, 0.25F };
/*  34 */   private static String[] HAND_DEPTH_NAMES = new String[] { "0.5x", "1x", "2x" };
/*  35 */   private static float HAND_DEPTH_DEFAULT = 0.125F;
/*     */   
/*     */   public static final int EnumOS_UNKNOWN = 0;
/*     */   public static final int EnumOS_WINDOWS = 1;
/*     */   public static final int EnumOS_OSX = 2;
/*     */   public static final int EnumOS_SOLARIS = 3;
/*     */   public static final int EnumOS_LINUX = 4;
/*     */   
/*     */   public GuiShaders(GuiScreen par1GuiScreen, GameSettings par2GameSettings) {
/*  44 */     this.parentGui = par1GuiScreen;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  49 */     this.screenTitle = I18n.format("of.options.shadersTitle", new Object[0]);
/*     */     
/*  51 */     if (Shaders.shadersConfig == null)
/*     */     {
/*  53 */       Shaders.loadConfig();
/*     */     }
/*     */     
/*  56 */     int i = 120;
/*  57 */     int j = 20;
/*  58 */     int k = this.width - i - 10;
/*  59 */     int l = 30;
/*  60 */     int i1 = 20;
/*  61 */     int j1 = this.width - i - 20;
/*  62 */     this.shaderList = new GuiSlotShaders(this, j1, this.height, l, this.height - 50, 16);
/*  63 */     this.shaderList.registerScrollButtons(7, 8);
/*  64 */     this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.ANTIALIASING, k, 0 * i1 + l, i, j));
/*  65 */     this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.NORMAL_MAP, k, 1 * i1 + l, i, j));
/*  66 */     this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.SPECULAR_MAP, k, 2 * i1 + l, i, j));
/*  67 */     this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.RENDER_RES_MUL, k, 3 * i1 + l, i, j));
/*  68 */     this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.SHADOW_RES_MUL, k, 4 * i1 + l, i, j));
/*  69 */     this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.HAND_DEPTH_MUL, k, 5 * i1 + l, i, j));
/*  70 */     this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.OLD_HAND_LIGHT, k, 6 * i1 + l, i, j));
/*  71 */     this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.OLD_LIGHTING, k, 7 * i1 + l, i, j));
/*  72 */     int k1 = Math.min(150, j1 / 2 - 10);
/*  73 */     int l1 = j1 / 4 - k1 / 2;
/*  74 */     int i2 = this.height - 25;
/*  75 */     this.buttonList.add(new GuiButton(201, l1, i2, k1 - 22 + 1, j, Lang.get("of.options.shaders.shadersFolder")));
/*  76 */     this.buttonList.add(new GuiButtonDownloadShaders(210, l1 + k1 - 22 - 1, i2));
/*  77 */     this.buttonList.add(new GuiButton(202, j1 / 4 * 3 - k1 / 2, this.height - 25, k1, j, I18n.format("gui.done", new Object[0])));
/*  78 */     this.buttonList.add(new GuiButton(203, k, this.height - 25, i, j, Lang.get("of.options.shaders.shaderOptions")));
/*  79 */     updateButtons();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateButtons() {
/*  84 */     boolean flag = Config.isShaders();
/*     */     
/*  86 */     for (GuiButton guibutton : this.buttonList) {
/*     */       
/*  88 */       if (guibutton.id != 201 && guibutton.id != 202 && guibutton.id != 210 && guibutton.id != EnumShaderOption.ANTIALIASING.ordinal())
/*     */       {
/*  90 */         guibutton.enabled = flag;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleMouseInput() throws IOException {
/*  97 */     super.handleMouseInput();
/*  98 */     this.shaderList.handleMouseInput();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) {
/* 103 */     actionPerformed(button, false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformedRightClick(GuiButton button) {
/* 108 */     actionPerformed(button, true);
/*     */   }
/*     */ 
/*     */   
/*     */   private void actionPerformed(GuiButton button, boolean rightClick) {
/* 113 */     if (button.enabled)
/*     */     {
/* 115 */       if (!(button instanceof GuiButtonEnumShaderOption)) {
/*     */         
/* 117 */         if (!rightClick) {
/*     */           String s; boolean flag; GuiShaderOptions guishaderoptions;
/* 119 */           switch (button.id) {
/*     */             
/*     */             case 201:
/* 122 */               switch (getOSType()) {
/*     */                 
/*     */                 case 1:
/* 125 */                   s = String.format("cmd.exe /C start \"Open file\" \"%s\"", new Object[] { Shaders.shaderPacksDir.getAbsolutePath() });
/*     */ 
/*     */                   
/*     */                   try {
/* 129 */                     Runtime.getRuntime().exec(s);
/*     */                     
/*     */                     return;
/* 132 */                   } catch (IOException ioexception) {
/*     */                     
/* 134 */                     ioexception.printStackTrace();
/*     */                     break;
/*     */                   } 
/*     */ 
/*     */                 
/*     */                 case 2:
/*     */                   try {
/* 141 */                     Runtime.getRuntime().exec(new String[] { "/usr/bin/open", Shaders.shaderPacksDir.getAbsolutePath() });
/*     */                     
/*     */                     return;
/* 144 */                   } catch (IOException ioexception1) {
/*     */                     
/* 146 */                     ioexception1.printStackTrace();
/*     */                     break;
/*     */                   } 
/*     */               } 
/* 150 */               flag = false;
/*     */ 
/*     */               
/*     */               try {
/* 154 */                 Class<?> oclass1 = Class.forName("java.awt.Desktop");
/* 155 */                 Object object1 = oclass1.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
/* 156 */                 oclass1.getMethod("browse", new Class[] { URI.class }).invoke(object1, new Object[] { (new File(this.mc.mcDataDir, "shaderpacks")).toURI() });
/*     */               }
/* 158 */               catch (Throwable throwable1) {
/*     */                 
/* 160 */                 throwable1.printStackTrace();
/* 161 */                 flag = true;
/*     */               } 
/*     */               
/* 164 */               if (flag) {
/*     */                 
/* 166 */                 Config.dbg("Opening via system class!");
/* 167 */                 Sys.openURL("file://" + Shaders.shaderPacksDir.getAbsolutePath());
/*     */               } 
/*     */               return;
/*     */ 
/*     */             
/*     */             case 202:
/* 173 */               Shaders.storeConfig();
/* 174 */               this.saved = true;
/* 175 */               this.mc.displayGuiScreen(this.parentGui);
/*     */               return;
/*     */             
/*     */             case 203:
/* 179 */               guishaderoptions = new GuiShaderOptions((GuiScreen)this, Config.getGameSettings());
/* 180 */               Config.getMinecraft().displayGuiScreen((GuiScreen)guishaderoptions);
/*     */               return;
/*     */ 
/*     */             
/*     */             case 210:
/*     */               try {
/* 186 */                 Class<?> oclass = Class.forName("java.awt.Desktop");
/* 187 */                 Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
/* 188 */                 oclass.getMethod("browse", new Class[] { URI.class }).invoke(object, new Object[] { new URI("http://optifine.net/shaderPacks") });
/*     */               }
/* 190 */               catch (Throwable throwable) {
/*     */                 
/* 192 */                 throwable.printStackTrace();
/*     */               } 
/*     */               break;
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 202 */           this.shaderList.actionPerformed(button);
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 208 */         GuiButtonEnumShaderOption guibuttonenumshaderoption = (GuiButtonEnumShaderOption)button;
/*     */         
/* 210 */         switch (guibuttonenumshaderoption.getEnumShaderOption()) {
/*     */           
/*     */           case ANTIALIASING:
/* 213 */             Shaders.nextAntialiasingLevel(!rightClick);
/*     */             
/* 215 */             if (hasShiftDown())
/*     */             {
/* 217 */               Shaders.configAntialiasingLevel = 0;
/*     */             }
/*     */             
/* 220 */             Shaders.uninit();
/*     */             break;
/*     */           
/*     */           case NORMAL_MAP:
/* 224 */             Shaders.configNormalMap = !Shaders.configNormalMap;
/*     */             
/* 226 */             if (hasShiftDown())
/*     */             {
/* 228 */               Shaders.configNormalMap = true;
/*     */             }
/*     */             
/* 231 */             Shaders.uninit();
/* 232 */             this.mc.scheduleResourcesRefresh();
/*     */             break;
/*     */           
/*     */           case SPECULAR_MAP:
/* 236 */             Shaders.configSpecularMap = !Shaders.configSpecularMap;
/*     */             
/* 238 */             if (hasShiftDown())
/*     */             {
/* 240 */               Shaders.configSpecularMap = true;
/*     */             }
/*     */             
/* 243 */             Shaders.uninit();
/* 244 */             this.mc.scheduleResourcesRefresh();
/*     */             break;
/*     */           
/*     */           case RENDER_RES_MUL:
/* 248 */             Shaders.configRenderResMul = getNextValue(Shaders.configRenderResMul, QUALITY_MULTIPLIERS, QUALITY_MULTIPLIER_DEFAULT, !rightClick, hasShiftDown());
/* 249 */             Shaders.uninit();
/* 250 */             Shaders.scheduleResize();
/*     */             break;
/*     */           
/*     */           case SHADOW_RES_MUL:
/* 254 */             Shaders.configShadowResMul = getNextValue(Shaders.configShadowResMul, QUALITY_MULTIPLIERS, QUALITY_MULTIPLIER_DEFAULT, !rightClick, hasShiftDown());
/* 255 */             Shaders.uninit();
/* 256 */             Shaders.scheduleResizeShadow();
/*     */             break;
/*     */           
/*     */           case HAND_DEPTH_MUL:
/* 260 */             Shaders.configHandDepthMul = getNextValue(Shaders.configHandDepthMul, HAND_DEPTH_VALUES, HAND_DEPTH_DEFAULT, !rightClick, hasShiftDown());
/* 261 */             Shaders.uninit();
/*     */             break;
/*     */           
/*     */           case OLD_HAND_LIGHT:
/* 265 */             Shaders.configOldHandLight.nextValue(!rightClick);
/*     */             
/* 267 */             if (hasShiftDown())
/*     */             {
/* 269 */               Shaders.configOldHandLight.resetValue();
/*     */             }
/*     */             
/* 272 */             Shaders.uninit();
/*     */             break;
/*     */           
/*     */           case OLD_LIGHTING:
/* 276 */             Shaders.configOldLighting.nextValue(!rightClick);
/*     */             
/* 278 */             if (hasShiftDown())
/*     */             {
/* 280 */               Shaders.configOldLighting.resetValue();
/*     */             }
/*     */             
/* 283 */             Shaders.updateBlockLightLevel();
/* 284 */             Shaders.uninit();
/* 285 */             this.mc.scheduleResourcesRefresh();
/*     */             break;
/*     */           
/*     */           case TWEAK_BLOCK_DAMAGE:
/* 289 */             Shaders.configTweakBlockDamage = !Shaders.configTweakBlockDamage;
/*     */             break;
/*     */           
/*     */           case CLOUD_SHADOW:
/* 293 */             Shaders.configCloudShadow = !Shaders.configCloudShadow;
/*     */             break;
/*     */           
/*     */           case TEX_MIN_FIL_B:
/* 297 */             Shaders.configTexMinFilB = (Shaders.configTexMinFilB + 1) % 3;
/* 298 */             Shaders.configTexMinFilN = Shaders.configTexMinFilS = Shaders.configTexMinFilB;
/* 299 */             button.displayString = "Tex Min: " + Shaders.texMinFilDesc[Shaders.configTexMinFilB];
/* 300 */             ShadersTex.updateTextureMinMagFilter();
/*     */             break;
/*     */           
/*     */           case TEX_MAG_FIL_N:
/* 304 */             Shaders.configTexMagFilN = (Shaders.configTexMagFilN + 1) % 2;
/* 305 */             button.displayString = "Tex_n Mag: " + Shaders.texMagFilDesc[Shaders.configTexMagFilN];
/* 306 */             ShadersTex.updateTextureMinMagFilter();
/*     */             break;
/*     */           
/*     */           case TEX_MAG_FIL_S:
/* 310 */             Shaders.configTexMagFilS = (Shaders.configTexMagFilS + 1) % 2;
/* 311 */             button.displayString = "Tex_s Mag: " + Shaders.texMagFilDesc[Shaders.configTexMagFilS];
/* 312 */             ShadersTex.updateTextureMinMagFilter();
/*     */             break;
/*     */           
/*     */           case SHADOW_CLIP_FRUSTRUM:
/* 316 */             Shaders.configShadowClipFrustrum = !Shaders.configShadowClipFrustrum;
/* 317 */             button.displayString = "ShadowClipFrustrum: " + toStringOnOff(Shaders.configShadowClipFrustrum);
/* 318 */             ShadersTex.updateTextureMinMagFilter();
/*     */             break;
/*     */         } 
/* 321 */         guibuttonenumshaderoption.updateButtonText();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onGuiClosed() {
/* 328 */     super.onGuiClosed();
/*     */     
/* 330 */     if (!this.saved)
/*     */     {
/* 332 */       Shaders.storeConfig();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 338 */     drawDefaultBackground();
/* 339 */     this.shaderList.drawScreen(mouseX, mouseY, partialTicks);
/*     */     
/* 341 */     if (this.updateTimer <= 0) {
/*     */       
/* 343 */       this.shaderList.updateList();
/* 344 */       this.updateTimer += 20;
/*     */     } 
/*     */     
/* 347 */     drawCenteredString(this.fontRendererObj, this.screenTitle + " ", this.width / 2, 15, 16777215);
/* 348 */     String s = "OpenGL: " + Shaders.glVersionString + ", " + Shaders.glVendorString + ", " + Shaders.glRendererString;
/* 349 */     int i = this.fontRendererObj.getStringWidth(s);
/*     */     
/* 351 */     if (i < this.width - 5) {
/*     */       
/* 353 */       drawCenteredString(this.fontRendererObj, s, this.width / 2, this.height - 40, 8421504);
/*     */     }
/*     */     else {
/*     */       
/* 357 */       drawString(this.fontRendererObj, s, 5, this.height - 40, 8421504);
/*     */     } 
/*     */     
/* 360 */     super.drawScreen(mouseX, mouseY, partialTicks);
/* 361 */     this.tooltipManager.drawTooltips(mouseX, mouseY, this.buttonList);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateScreen() {
/* 366 */     super.updateScreen();
/* 367 */     this.updateTimer--;
/*     */   }
/*     */ 
/*     */   
/*     */   public Minecraft getMc() {
/* 372 */     return this.mc;
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawCenteredString(String text, int x, int y, int color) {
/* 377 */     drawCenteredString(this.fontRendererObj, text, x, y, color);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String toStringOnOff(boolean value) {
/* 382 */     String s = Lang.getOn();
/* 383 */     String s1 = Lang.getOff();
/* 384 */     return value ? s : s1;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String toStringAa(int value) {
/* 389 */     return (value == 2) ? "FXAA 2x" : ((value == 4) ? "FXAA 4x" : Lang.getOff());
/*     */   }
/*     */ 
/*     */   
/*     */   public static String toStringValue(float val, float[] values, String[] names) {
/* 394 */     int i = getValueIndex(val, values);
/* 395 */     return names[i];
/*     */   }
/*     */ 
/*     */   
/*     */   private float getNextValue(float val, float[] values, float valDef, boolean forward, boolean reset) {
/* 400 */     if (reset)
/*     */     {
/* 402 */       return valDef;
/*     */     }
/*     */ 
/*     */     
/* 406 */     int i = getValueIndex(val, values);
/*     */     
/* 408 */     if (forward) {
/*     */       
/* 410 */       i++;
/*     */       
/* 412 */       if (i >= values.length)
/*     */       {
/* 414 */         i = 0;
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 419 */       i--;
/*     */       
/* 421 */       if (i < 0)
/*     */       {
/* 423 */         i = values.length - 1;
/*     */       }
/*     */     } 
/*     */     
/* 427 */     return values[i];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getValueIndex(float val, float[] values) {
/* 433 */     for (int i = 0; i < values.length; i++) {
/*     */       
/* 435 */       float f = values[i];
/*     */       
/* 437 */       if (f >= val)
/*     */       {
/* 439 */         return i;
/*     */       }
/*     */     } 
/*     */     
/* 443 */     return values.length - 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String toStringQuality(float val) {
/* 448 */     return toStringValue(val, QUALITY_MULTIPLIERS, QUALITY_MULTIPLIER_NAMES);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String toStringHandDepth(float val) {
/* 453 */     return toStringValue(val, HAND_DEPTH_VALUES, HAND_DEPTH_NAMES);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getOSType() {
/* 458 */     String s = System.getProperty("os.name").toLowerCase();
/* 459 */     return s.contains("win") ? 1 : (s.contains("mac") ? 2 : (s.contains("solaris") ? 3 : (s.contains("sunos") ? 3 : (s.contains("linux") ? 4 : (s.contains("unix") ? 4 : 0)))));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasShiftDown() {
/* 464 */     return isShiftKeyDown();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\shaders\gui\GuiShaders.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */