/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Random;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.util.ChatAllowedCharacters;
/*     */ import net.minecraft.world.WorldSettings;
/*     */ import net.minecraft.world.WorldType;
/*     */ import net.minecraft.world.storage.ISaveFormat;
/*     */ import net.minecraft.world.storage.WorldInfo;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ public class GuiCreateWorld
/*     */   extends GuiScreen {
/*     */   private GuiScreen parentScreen;
/*     */   private GuiTextField worldNameField;
/*     */   private GuiTextField worldSeedField;
/*     */   private String saveDirName;
/*  20 */   private String gameMode = "survival";
/*     */   private String savedGameMode;
/*     */   private boolean generateStructuresEnabled = true;
/*     */   private boolean allowCheats;
/*     */   private boolean allowCheatsWasSetByUser;
/*     */   private boolean bonusChestEnabled;
/*     */   private boolean hardCoreMode;
/*     */   private boolean alreadyGenerated;
/*     */   private boolean inMoreWorldOptionsDisplay;
/*     */   private GuiButton btnGameMode;
/*     */   private GuiButton btnMoreOptions;
/*     */   private GuiButton btnMapFeatures;
/*     */   private GuiButton btnBonusItems;
/*     */   private GuiButton btnMapType;
/*     */   private GuiButton btnAllowCommands;
/*     */   private GuiButton btnCustomizeType;
/*     */   private String gameModeDesc1;
/*     */   private String gameModeDesc2;
/*     */   private String worldSeed;
/*     */   private String worldName;
/*     */   private int selectedIndex;
/*  41 */   public String chunkProviderSettingsJson = "";
/*  42 */   private static final String[] disallowedFilenames = new String[] { "CON", "COM", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9" };
/*     */ 
/*     */   
/*     */   public GuiCreateWorld(GuiScreen p_i46320_1_) {
/*  46 */     this.parentScreen = p_i46320_1_;
/*  47 */     this.worldSeed = "";
/*  48 */     this.worldName = I18n.format("selectWorld.newWorld", new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateScreen() {
/*  53 */     this.worldNameField.updateCursorCounter();
/*  54 */     this.worldSeedField.updateCursorCounter();
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  59 */     Keyboard.enableRepeatEvents(true);
/*  60 */     this.buttonList.clear();
/*  61 */     this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, I18n.format("selectWorld.create", new Object[0])));
/*  62 */     this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, I18n.format("gui.cancel", new Object[0])));
/*  63 */     this.buttonList.add(this.btnGameMode = new GuiButton(2, this.width / 2 - 75, 115, 150, 20, I18n.format("selectWorld.gameMode", new Object[0])));
/*  64 */     this.buttonList.add(this.btnMoreOptions = new GuiButton(3, this.width / 2 - 75, 187, 150, 20, I18n.format("selectWorld.moreWorldOptions", new Object[0])));
/*  65 */     this.buttonList.add(this.btnMapFeatures = new GuiButton(4, this.width / 2 - 155, 100, 150, 20, I18n.format("selectWorld.mapFeatures", new Object[0])));
/*  66 */     this.btnMapFeatures.visible = false;
/*  67 */     this.buttonList.add(this.btnBonusItems = new GuiButton(7, this.width / 2 + 5, 151, 150, 20, I18n.format("selectWorld.bonusItems", new Object[0])));
/*  68 */     this.btnBonusItems.visible = false;
/*  69 */     this.buttonList.add(this.btnMapType = new GuiButton(5, this.width / 2 + 5, 100, 150, 20, I18n.format("selectWorld.mapType", new Object[0])));
/*  70 */     this.btnMapType.visible = false;
/*  71 */     this.buttonList.add(this.btnAllowCommands = new GuiButton(6, this.width / 2 - 155, 151, 150, 20, I18n.format("selectWorld.allowCommands", new Object[0])));
/*  72 */     this.btnAllowCommands.visible = false;
/*  73 */     this.buttonList.add(this.btnCustomizeType = new GuiButton(8, this.width / 2 + 5, 120, 150, 20, I18n.format("selectWorld.customizeType", new Object[0])));
/*  74 */     this.btnCustomizeType.visible = false;
/*  75 */     this.worldNameField = new GuiTextField(9, this.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
/*  76 */     this.worldNameField.setFocused(true);
/*  77 */     this.worldNameField.setText(this.worldName);
/*  78 */     this.worldSeedField = new GuiTextField(10, this.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
/*  79 */     this.worldSeedField.setText(this.worldSeed);
/*  80 */     showMoreWorldOptions(this.inMoreWorldOptionsDisplay);
/*  81 */     calcSaveDirName();
/*  82 */     updateDisplayState();
/*     */   }
/*     */ 
/*     */   
/*     */   private void calcSaveDirName() {
/*  87 */     this.saveDirName = this.worldNameField.getText().trim();
/*     */     
/*  89 */     for (char c0 : ChatAllowedCharacters.allowedCharactersArray)
/*     */     {
/*  91 */       this.saveDirName = this.saveDirName.replace(c0, '_');
/*     */     }
/*     */     
/*  94 */     if (StringUtils.isEmpty(this.saveDirName))
/*     */     {
/*  96 */       this.saveDirName = "World";
/*     */     }
/*     */     
/*  99 */     this.saveDirName = getUncollidingSaveDirName(this.mc.getSaveLoader(), this.saveDirName);
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateDisplayState() {
/* 104 */     this.btnGameMode.displayString = I18n.format("selectWorld.gameMode", new Object[0]) + ": " + I18n.format("selectWorld.gameMode." + this.gameMode, new Object[0]);
/* 105 */     this.gameModeDesc1 = I18n.format("selectWorld.gameMode." + this.gameMode + ".line1", new Object[0]);
/* 106 */     this.gameModeDesc2 = I18n.format("selectWorld.gameMode." + this.gameMode + ".line2", new Object[0]);
/* 107 */     this.btnMapFeatures.displayString = I18n.format("selectWorld.mapFeatures", new Object[0]) + " ";
/*     */     
/* 109 */     if (this.generateStructuresEnabled) {
/*     */       
/* 111 */       this.btnMapFeatures.displayString += I18n.format("options.on", new Object[0]);
/*     */     }
/*     */     else {
/*     */       
/* 115 */       this.btnMapFeatures.displayString += I18n.format("options.off", new Object[0]);
/*     */     } 
/*     */     
/* 118 */     this.btnBonusItems.displayString = I18n.format("selectWorld.bonusItems", new Object[0]) + " ";
/*     */     
/* 120 */     if (this.bonusChestEnabled && !this.hardCoreMode) {
/*     */       
/* 122 */       this.btnBonusItems.displayString += I18n.format("options.on", new Object[0]);
/*     */     }
/*     */     else {
/*     */       
/* 126 */       this.btnBonusItems.displayString += I18n.format("options.off", new Object[0]);
/*     */     } 
/*     */     
/* 129 */     this.btnMapType.displayString = I18n.format("selectWorld.mapType", new Object[0]) + " " + I18n.format(WorldType.worldTypes[this.selectedIndex].getTranslateName(), new Object[0]);
/* 130 */     this.btnAllowCommands.displayString = I18n.format("selectWorld.allowCommands", new Object[0]) + " ";
/*     */     
/* 132 */     if (this.allowCheats && !this.hardCoreMode) {
/*     */       
/* 134 */       this.btnAllowCommands.displayString += I18n.format("options.on", new Object[0]);
/*     */     }
/*     */     else {
/*     */       
/* 138 */       this.btnAllowCommands.displayString += I18n.format("options.off", new Object[0]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getUncollidingSaveDirName(ISaveFormat saveLoader, String name) {
/* 144 */     name = name.replaceAll("[\\./\"]", "_");
/*     */     
/* 146 */     for (String s : disallowedFilenames) {
/*     */       
/* 148 */       if (name.equalsIgnoreCase(s))
/*     */       {
/* 150 */         name = "_" + name + "_";
/*     */       }
/*     */     } 
/*     */     
/* 154 */     while (saveLoader.getWorldInfo(name) != null)
/*     */     {
/* 156 */       name = name + "-";
/*     */     }
/*     */     
/* 159 */     return name;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onGuiClosed() {
/* 164 */     Keyboard.enableRepeatEvents(false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/* 169 */     if (button.enabled)
/*     */     {
/* 171 */       if (button.id == 1) {
/*     */         
/* 173 */         this.mc.displayGuiScreen(this.parentScreen);
/*     */       }
/* 175 */       else if (button.id == 0) {
/*     */         
/* 177 */         this.mc.displayGuiScreen((GuiScreen)null);
/*     */         
/* 179 */         if (this.alreadyGenerated) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/* 184 */         this.alreadyGenerated = true;
/* 185 */         long i = (new Random()).nextLong();
/* 186 */         String s = this.worldSeedField.getText();
/*     */         
/* 188 */         if (!StringUtils.isEmpty(s)) {
/*     */           
/*     */           try {
/*     */             
/* 192 */             long j = Long.parseLong(s);
/*     */             
/* 194 */             if (j != 0L)
/*     */             {
/* 196 */               i = j;
/*     */             }
/*     */           }
/* 199 */           catch (NumberFormatException var7) {
/*     */             
/* 201 */             i = s.hashCode();
/*     */           } 
/*     */         }
/*     */         
/* 205 */         WorldSettings.GameType worldsettings$gametype = WorldSettings.GameType.getByName(this.gameMode);
/* 206 */         WorldSettings worldsettings = new WorldSettings(i, worldsettings$gametype, this.generateStructuresEnabled, this.hardCoreMode, WorldType.worldTypes[this.selectedIndex]);
/* 207 */         worldsettings.setWorldName(this.chunkProviderSettingsJson);
/*     */         
/* 209 */         if (this.bonusChestEnabled && !this.hardCoreMode)
/*     */         {
/* 211 */           worldsettings.enableBonusChest();
/*     */         }
/*     */         
/* 214 */         if (this.allowCheats && !this.hardCoreMode)
/*     */         {
/* 216 */           worldsettings.enableCommands();
/*     */         }
/*     */         
/* 219 */         this.mc.launchIntegratedServer(this.saveDirName, this.worldNameField.getText().trim(), worldsettings);
/*     */       }
/* 221 */       else if (button.id == 3) {
/*     */         
/* 223 */         toggleMoreWorldOptions();
/*     */       }
/* 225 */       else if (button.id == 2) {
/*     */         
/* 227 */         if (this.gameMode.equals("survival")) {
/*     */           
/* 229 */           if (!this.allowCheatsWasSetByUser)
/*     */           {
/* 231 */             this.allowCheats = false;
/*     */           }
/*     */           
/* 234 */           this.hardCoreMode = false;
/* 235 */           this.gameMode = "hardcore";
/* 236 */           this.hardCoreMode = true;
/* 237 */           this.btnAllowCommands.enabled = false;
/* 238 */           this.btnBonusItems.enabled = false;
/* 239 */           updateDisplayState();
/*     */         }
/* 241 */         else if (this.gameMode.equals("hardcore")) {
/*     */           
/* 243 */           if (!this.allowCheatsWasSetByUser)
/*     */           {
/* 245 */             this.allowCheats = true;
/*     */           }
/*     */           
/* 248 */           this.hardCoreMode = false;
/* 249 */           this.gameMode = "creative";
/* 250 */           updateDisplayState();
/* 251 */           this.hardCoreMode = false;
/* 252 */           this.btnAllowCommands.enabled = true;
/* 253 */           this.btnBonusItems.enabled = true;
/*     */         }
/*     */         else {
/*     */           
/* 257 */           if (!this.allowCheatsWasSetByUser)
/*     */           {
/* 259 */             this.allowCheats = false;
/*     */           }
/*     */           
/* 262 */           this.gameMode = "survival";
/* 263 */           updateDisplayState();
/* 264 */           this.btnAllowCommands.enabled = true;
/* 265 */           this.btnBonusItems.enabled = true;
/* 266 */           this.hardCoreMode = false;
/*     */         } 
/*     */         
/* 269 */         updateDisplayState();
/*     */       }
/* 271 */       else if (button.id == 4) {
/*     */         
/* 273 */         this.generateStructuresEnabled = !this.generateStructuresEnabled;
/* 274 */         updateDisplayState();
/*     */       }
/* 276 */       else if (button.id == 7) {
/*     */         
/* 278 */         this.bonusChestEnabled = !this.bonusChestEnabled;
/* 279 */         updateDisplayState();
/*     */       }
/* 281 */       else if (button.id == 5) {
/*     */         
/* 283 */         this.selectedIndex++;
/*     */         
/* 285 */         if (this.selectedIndex >= WorldType.worldTypes.length)
/*     */         {
/* 287 */           this.selectedIndex = 0;
/*     */         }
/*     */         
/* 290 */         while (!canSelectCurWorldType()) {
/*     */           
/* 292 */           this.selectedIndex++;
/*     */           
/* 294 */           if (this.selectedIndex >= WorldType.worldTypes.length)
/*     */           {
/* 296 */             this.selectedIndex = 0;
/*     */           }
/*     */         } 
/*     */         
/* 300 */         this.chunkProviderSettingsJson = "";
/* 301 */         updateDisplayState();
/* 302 */         showMoreWorldOptions(this.inMoreWorldOptionsDisplay);
/*     */       }
/* 304 */       else if (button.id == 6) {
/*     */         
/* 306 */         this.allowCheatsWasSetByUser = true;
/* 307 */         this.allowCheats = !this.allowCheats;
/* 308 */         updateDisplayState();
/*     */       }
/* 310 */       else if (button.id == 8) {
/*     */         
/* 312 */         if (WorldType.worldTypes[this.selectedIndex] == WorldType.FLAT) {
/*     */           
/* 314 */           this.mc.displayGuiScreen(new GuiCreateFlatWorld(this, this.chunkProviderSettingsJson));
/*     */         }
/*     */         else {
/*     */           
/* 318 */           this.mc.displayGuiScreen(new GuiCustomizeWorldScreen(this, this.chunkProviderSettingsJson));
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean canSelectCurWorldType() {
/* 326 */     WorldType worldtype = WorldType.worldTypes[this.selectedIndex];
/* 327 */     return (worldtype != null && worldtype.getCanBeCreated()) ? ((worldtype == WorldType.DEBUG_WORLD) ? isShiftKeyDown() : true) : false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void toggleMoreWorldOptions() {
/* 332 */     showMoreWorldOptions(!this.inMoreWorldOptionsDisplay);
/*     */   }
/*     */ 
/*     */   
/*     */   private void showMoreWorldOptions(boolean toggle) {
/* 337 */     this.inMoreWorldOptionsDisplay = toggle;
/*     */     
/* 339 */     if (WorldType.worldTypes[this.selectedIndex] == WorldType.DEBUG_WORLD) {
/*     */       
/* 341 */       this.btnGameMode.visible = !this.inMoreWorldOptionsDisplay;
/* 342 */       this.btnGameMode.enabled = false;
/*     */       
/* 344 */       if (this.savedGameMode == null)
/*     */       {
/* 346 */         this.savedGameMode = this.gameMode;
/*     */       }
/*     */       
/* 349 */       this.gameMode = "spectator";
/* 350 */       this.btnMapFeatures.visible = false;
/* 351 */       this.btnBonusItems.visible = false;
/* 352 */       this.btnMapType.visible = this.inMoreWorldOptionsDisplay;
/* 353 */       this.btnAllowCommands.visible = false;
/* 354 */       this.btnCustomizeType.visible = false;
/*     */     }
/*     */     else {
/*     */       
/* 358 */       this.btnGameMode.visible = !this.inMoreWorldOptionsDisplay;
/* 359 */       this.btnGameMode.enabled = true;
/*     */       
/* 361 */       if (this.savedGameMode != null) {
/*     */         
/* 363 */         this.gameMode = this.savedGameMode;
/* 364 */         this.savedGameMode = null;
/*     */       } 
/*     */       
/* 367 */       this.btnMapFeatures.visible = (this.inMoreWorldOptionsDisplay && WorldType.worldTypes[this.selectedIndex] != WorldType.CUSTOMIZED);
/* 368 */       this.btnBonusItems.visible = this.inMoreWorldOptionsDisplay;
/* 369 */       this.btnMapType.visible = this.inMoreWorldOptionsDisplay;
/* 370 */       this.btnAllowCommands.visible = this.inMoreWorldOptionsDisplay;
/* 371 */       this.btnCustomizeType.visible = (this.inMoreWorldOptionsDisplay && (WorldType.worldTypes[this.selectedIndex] == WorldType.FLAT || WorldType.worldTypes[this.selectedIndex] == WorldType.CUSTOMIZED));
/*     */     } 
/*     */     
/* 374 */     updateDisplayState();
/*     */     
/* 376 */     if (this.inMoreWorldOptionsDisplay) {
/*     */       
/* 378 */       this.btnMoreOptions.displayString = I18n.format("gui.done", new Object[0]);
/*     */     }
/*     */     else {
/*     */       
/* 382 */       this.btnMoreOptions.displayString = I18n.format("selectWorld.moreWorldOptions", new Object[0]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/* 388 */     if (this.worldNameField.isFocused() && !this.inMoreWorldOptionsDisplay) {
/*     */       
/* 390 */       this.worldNameField.textboxKeyTyped(typedChar, keyCode);
/* 391 */       this.worldName = this.worldNameField.getText();
/*     */     }
/* 393 */     else if (this.worldSeedField.isFocused() && this.inMoreWorldOptionsDisplay) {
/*     */       
/* 395 */       this.worldSeedField.textboxKeyTyped(typedChar, keyCode);
/* 396 */       this.worldSeed = this.worldSeedField.getText();
/*     */     } 
/*     */     
/* 399 */     if (keyCode == 28 || keyCode == 156)
/*     */     {
/* 401 */       actionPerformed(this.buttonList.get(0));
/*     */     }
/*     */     
/* 404 */     ((GuiButton)this.buttonList.get(0)).enabled = (this.worldNameField.getText().length() > 0);
/* 405 */     calcSaveDirName();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 410 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */     
/* 412 */     if (this.inMoreWorldOptionsDisplay) {
/*     */       
/* 414 */       this.worldSeedField.mouseClicked(mouseX, mouseY, mouseButton);
/*     */     }
/*     */     else {
/*     */       
/* 418 */       this.worldNameField.mouseClicked(mouseX, mouseY, mouseButton);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 424 */     drawDefaultBackground();
/* 425 */     drawCenteredString(this.fontRendererObj, I18n.format("selectWorld.create", new Object[0]), this.width / 2, 20, -1);
/*     */     
/* 427 */     if (this.inMoreWorldOptionsDisplay) {
/*     */       
/* 429 */       drawString(this.fontRendererObj, I18n.format("selectWorld.enterSeed", new Object[0]), this.width / 2 - 100, 47, -6250336);
/* 430 */       drawString(this.fontRendererObj, I18n.format("selectWorld.seedInfo", new Object[0]), this.width / 2 - 100, 85, -6250336);
/*     */       
/* 432 */       if (this.btnMapFeatures.visible)
/*     */       {
/* 434 */         drawString(this.fontRendererObj, I18n.format("selectWorld.mapFeatures.info", new Object[0]), this.width / 2 - 150, 122, -6250336);
/*     */       }
/*     */       
/* 437 */       if (this.btnAllowCommands.visible)
/*     */       {
/* 439 */         drawString(this.fontRendererObj, I18n.format("selectWorld.allowCommands.info", new Object[0]), this.width / 2 - 150, 172, -6250336);
/*     */       }
/*     */       
/* 442 */       this.worldSeedField.drawTextBox();
/*     */       
/* 444 */       if (WorldType.worldTypes[this.selectedIndex].showWorldInfoNotice())
/*     */       {
/* 446 */         this.fontRendererObj.drawSplitString(I18n.format(WorldType.worldTypes[this.selectedIndex].getTranslatedInfo(), new Object[0]), this.btnMapType.xPosition + 2, this.btnMapType.yPosition + 22, this.btnMapType.getButtonWidth(), 10526880);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 451 */       drawString(this.fontRendererObj, I18n.format("selectWorld.enterName", new Object[0]), this.width / 2 - 100, 47, -6250336);
/* 452 */       drawString(this.fontRendererObj, I18n.format("selectWorld.resultFolder", new Object[0]) + " " + this.saveDirName, this.width / 2 - 100, 85, -6250336);
/* 453 */       this.worldNameField.drawTextBox();
/* 454 */       drawString(this.fontRendererObj, this.gameModeDesc1, this.width / 2 - 100, 137, -6250336);
/* 455 */       drawString(this.fontRendererObj, this.gameModeDesc2, this.width / 2 - 100, 149, -6250336);
/*     */     } 
/*     */     
/* 458 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ 
/*     */   
/*     */   public void recreateFromExistingWorld(WorldInfo original) {
/* 463 */     this.worldName = I18n.format("selectWorld.newWorld.copyOf", new Object[] { original.getWorldName() });
/* 464 */     this.worldSeed = original.getSeed() + "";
/* 465 */     this.selectedIndex = original.getTerrainType().getWorldTypeID();
/* 466 */     this.chunkProviderSettingsJson = original.getGeneratorOptions();
/* 467 */     this.generateStructuresEnabled = original.isMapFeaturesEnabled();
/* 468 */     this.allowCheats = original.areCommandsAllowed();
/*     */     
/* 470 */     if (original.isHardcoreModeEnabled()) {
/*     */       
/* 472 */       this.gameMode = "hardcore";
/*     */     }
/* 474 */     else if (original.getGameType().isSurvivalOrAdventure()) {
/*     */       
/* 476 */       this.gameMode = "survival";
/*     */     }
/* 478 */     else if (original.getGameType().isCreative()) {
/*     */       
/* 480 */       this.gameMode = "creative";
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiCreateWorld.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */