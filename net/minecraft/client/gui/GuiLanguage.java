/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.client.resources.Language;
/*     */ import net.minecraft.client.resources.LanguageManager;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ 
/*     */ public class GuiLanguage
/*     */   extends GuiScreen
/*     */ {
/*     */   protected GuiScreen parentScreen;
/*     */   private List list;
/*     */   private final GameSettings game_settings_3;
/*     */   private final LanguageManager languageManager;
/*     */   private GuiOptionButton forceUnicodeFontBtn;
/*     */   private GuiOptionButton confirmSettingsBtn;
/*     */   
/*     */   public GuiLanguage(GuiScreen screen, GameSettings gameSettingsObj, LanguageManager manager) {
/*  24 */     this.parentScreen = screen;
/*  25 */     this.game_settings_3 = gameSettingsObj;
/*  26 */     this.languageManager = manager;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  31 */     this.buttonList.add(this.forceUnicodeFontBtn = new GuiOptionButton(100, this.width / 2 - 155, this.height - 38, GameSettings.Options.FORCE_UNICODE_FONT, this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT)));
/*  32 */     this.buttonList.add(this.confirmSettingsBtn = new GuiOptionButton(6, this.width / 2 - 155 + 160, this.height - 38, I18n.format("gui.done", new Object[0])));
/*  33 */     this.list = new List(this.mc);
/*  34 */     this.list.registerScrollButtons(7, 8);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleMouseInput() throws IOException {
/*  39 */     super.handleMouseInput();
/*  40 */     this.list.handleMouseInput();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/*  45 */     if (button.enabled) {
/*     */       
/*  47 */       switch (button.id) {
/*     */         case 5:
/*     */           return;
/*     */ 
/*     */         
/*     */         case 6:
/*  53 */           this.mc.displayGuiScreen(this.parentScreen);
/*     */ 
/*     */         
/*     */         case 100:
/*  57 */           if (button instanceof GuiOptionButton) {
/*     */             
/*  59 */             this.game_settings_3.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
/*  60 */             button.displayString = this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
/*  61 */             ScaledResolution scaledresolution = new ScaledResolution(this.mc);
/*  62 */             int i = scaledresolution.getScaledWidth();
/*  63 */             int j = scaledresolution.getScaledHeight();
/*  64 */             setWorldAndResolution(this.mc, i, j);
/*     */           } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*  70 */       this.list.actionPerformed(button);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/*  77 */     this.list.drawScreen(mouseX, mouseY, partialTicks);
/*  78 */     drawCenteredString(this.fontRendererObj, I18n.format("options.language", new Object[0]), this.width / 2, 16, 16777215);
/*  79 */     drawCenteredString(this.fontRendererObj, "(" + I18n.format("options.languageWarning", new Object[0]) + ")", this.width / 2, this.height - 56, 8421504);
/*  80 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */   class List
/*     */     extends GuiSlot {
/*  85 */     private final java.util.List<String> langCodeList = Lists.newArrayList();
/*  86 */     private final Map<String, Language> languageMap = Maps.newHashMap();
/*     */ 
/*     */     
/*     */     public List(Minecraft mcIn) {
/*  90 */       super(mcIn, GuiLanguage.this.width, GuiLanguage.this.height, 32, GuiLanguage.this.height - 65 + 4, 18);
/*     */       
/*  92 */       for (Language language : GuiLanguage.this.languageManager.getLanguages()) {
/*     */         
/*  94 */         this.languageMap.put(language.getLanguageCode(), language);
/*  95 */         this.langCodeList.add(language.getLanguageCode());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getSize() {
/* 101 */       return this.langCodeList.size();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
/* 106 */       Language language = this.languageMap.get(this.langCodeList.get(slotIndex));
/* 107 */       GuiLanguage.this.languageManager.setCurrentLanguage(language);
/* 108 */       GuiLanguage.this.game_settings_3.language = language.getLanguageCode();
/* 109 */       this.mc.refreshResources();
/* 110 */       GuiLanguage.this.fontRendererObj.setUnicodeFlag((GuiLanguage.this.languageManager.isCurrentLocaleUnicode() || GuiLanguage.this.game_settings_3.forceUnicodeFont));
/* 111 */       GuiLanguage.this.fontRendererObj.setBidiFlag(GuiLanguage.this.languageManager.isCurrentLanguageBidirectional());
/* 112 */       GuiLanguage.this.confirmSettingsBtn.displayString = I18n.format("gui.done", new Object[0]);
/* 113 */       GuiLanguage.this.forceUnicodeFontBtn.displayString = GuiLanguage.this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
/* 114 */       GuiLanguage.this.game_settings_3.saveOptions();
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isSelected(int slotIndex) {
/* 119 */       return ((String)this.langCodeList.get(slotIndex)).equals(GuiLanguage.this.languageManager.getCurrentLanguage().getLanguageCode());
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getContentHeight() {
/* 124 */       return getSize() * 18;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void drawBackground() {
/* 129 */       GuiLanguage.this.drawDefaultBackground();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn) {
/* 134 */       GuiLanguage.this.fontRendererObj.setBidiFlag(true);
/* 135 */       GuiLanguage.this.drawCenteredString(GuiLanguage.this.fontRendererObj, ((Language)this.languageMap.get(this.langCodeList.get(entryID))).toString(), this.width / 2, p_180791_3_ + 1, 16777215);
/* 136 */       GuiLanguage.this.fontRendererObj.setBidiFlag(GuiLanguage.this.languageManager.getCurrentLanguage().isBidirectional());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiLanguage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */