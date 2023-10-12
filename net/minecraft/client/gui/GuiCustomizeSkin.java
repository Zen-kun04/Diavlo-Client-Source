/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import net.minecraft.entity.player.EnumPlayerModelParts;
/*    */ import net.optifine.gui.GuiButtonOF;
/*    */ import net.optifine.gui.GuiScreenCapeOF;
/*    */ 
/*    */ public class GuiCustomizeSkin
/*    */   extends GuiScreen
/*    */ {
/*    */   private final GuiScreen parentScreen;
/*    */   private String title;
/*    */   
/*    */   public GuiCustomizeSkin(GuiScreen parentScreenIn) {
/* 16 */     this.parentScreen = parentScreenIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void initGui() {
/* 21 */     int i = 0;
/* 22 */     this.title = I18n.format("options.skinCustomisation.title", new Object[0]);
/*    */     
/* 24 */     for (EnumPlayerModelParts enumplayermodelparts : EnumPlayerModelParts.values()) {
/*    */       
/* 26 */       this.buttonList.add(new ButtonPart(enumplayermodelparts.getPartId(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, enumplayermodelparts));
/* 27 */       i++;
/*    */     } 
/*    */     
/* 30 */     if (i % 2 == 1)
/*    */     {
/* 32 */       i++;
/*    */     }
/*    */     
/* 35 */     this.buttonList.add(new GuiButtonOF(210, this.width / 2 - 100, this.height / 6 + 24 * (i >> 1), I18n.format("of.options.skinCustomisation.ofCape", new Object[0])));
/* 36 */     i += 2;
/* 37 */     this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 24 * (i >> 1), I18n.format("gui.done", new Object[0])));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void actionPerformed(GuiButton button) throws IOException {
/* 42 */     if (button.enabled) {
/*    */       
/* 44 */       if (button.id == 210)
/*    */       {
/* 46 */         this.mc.displayGuiScreen((GuiScreen)new GuiScreenCapeOF(this));
/*    */       }
/*    */       
/* 49 */       if (button.id == 200) {
/*    */         
/* 51 */         this.mc.gameSettings.saveOptions();
/* 52 */         this.mc.displayGuiScreen(this.parentScreen);
/*    */       }
/* 54 */       else if (button instanceof ButtonPart) {
/*    */         
/* 56 */         EnumPlayerModelParts enumplayermodelparts = ((ButtonPart)button).playerModelParts;
/* 57 */         this.mc.gameSettings.switchModelPartEnabled(enumplayermodelparts);
/* 58 */         button.displayString = func_175358_a(enumplayermodelparts);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 65 */     drawDefaultBackground();
/* 66 */     drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 20, 16777215);
/* 67 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private String func_175358_a(EnumPlayerModelParts playerModelParts) {
/*    */     String s;
/* 74 */     if (this.mc.gameSettings.getModelParts().contains(playerModelParts)) {
/*    */       
/* 76 */       s = I18n.format("options.on", new Object[0]);
/*    */     }
/*    */     else {
/*    */       
/* 80 */       s = I18n.format("options.off", new Object[0]);
/*    */     } 
/*    */     
/* 83 */     return playerModelParts.func_179326_d().getFormattedText() + ": " + s;
/*    */   }
/*    */   
/*    */   class ButtonPart
/*    */     extends GuiButton
/*    */   {
/*    */     private final EnumPlayerModelParts playerModelParts;
/*    */     
/*    */     private ButtonPart(int p_i45514_2_, int p_i45514_3_, int p_i45514_4_, int p_i45514_5_, int p_i45514_6_, EnumPlayerModelParts playerModelParts) {
/* 92 */       super(p_i45514_2_, p_i45514_3_, p_i45514_4_, p_i45514_5_, p_i45514_6_, GuiCustomizeSkin.this.func_175358_a(playerModelParts));
/* 93 */       this.playerModelParts = playerModelParts;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiCustomizeSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */