/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import net.minecraft.client.settings.GameSettings;
/*    */ 
/*    */ public class ScreenChatOptions
/*    */   extends GuiScreen {
/*  9 */   private static final GameSettings.Options[] field_146399_a = new GameSettings.Options[] { GameSettings.Options.CHAT_VISIBILITY, GameSettings.Options.CHAT_COLOR, GameSettings.Options.CHAT_LINKS, GameSettings.Options.CHAT_OPACITY, GameSettings.Options.CHAT_LINKS_PROMPT, GameSettings.Options.CHAT_SCALE, GameSettings.Options.CHAT_HEIGHT_FOCUSED, GameSettings.Options.CHAT_HEIGHT_UNFOCUSED, GameSettings.Options.CHAT_WIDTH, GameSettings.Options.REDUCED_DEBUG_INFO };
/*    */   
/*    */   private final GuiScreen parentScreen;
/*    */   private final GameSettings game_settings;
/*    */   private String field_146401_i;
/*    */   
/*    */   public ScreenChatOptions(GuiScreen parentScreenIn, GameSettings gameSettingsIn) {
/* 16 */     this.parentScreen = parentScreenIn;
/* 17 */     this.game_settings = gameSettingsIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void initGui() {
/* 22 */     int i = 0;
/* 23 */     this.field_146401_i = I18n.format("options.chat.title", new Object[0]);
/*    */     
/* 25 */     for (GameSettings.Options gamesettings$options : field_146399_a) {
/*    */       
/* 27 */       if (gamesettings$options.getEnumFloat()) {
/*    */         
/* 29 */         this.buttonList.add(new GuiOptionSlider(gamesettings$options.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), gamesettings$options));
/*    */       }
/*    */       else {
/*    */         
/* 33 */         this.buttonList.add(new GuiOptionButton(gamesettings$options.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), gamesettings$options, this.game_settings.getKeyBinding(gamesettings$options)));
/*    */       } 
/*    */       
/* 36 */       i++;
/*    */     } 
/*    */     
/* 39 */     this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 120, I18n.format("gui.done", new Object[0])));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void actionPerformed(GuiButton button) throws IOException {
/* 44 */     if (button.enabled) {
/*    */       
/* 46 */       if (button.id < 100 && button instanceof GuiOptionButton) {
/*    */         
/* 48 */         this.game_settings.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
/* 49 */         button.displayString = this.game_settings.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
/*    */       } 
/*    */       
/* 52 */       if (button.id == 200) {
/*    */         
/* 54 */         this.mc.gameSettings.saveOptions();
/* 55 */         this.mc.displayGuiScreen(this.parentScreen);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 62 */     drawDefaultBackground();
/* 63 */     drawCenteredString(this.fontRendererObj, this.field_146401_i, this.width / 2, 20, 16777215);
/* 64 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\ScreenChatOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */