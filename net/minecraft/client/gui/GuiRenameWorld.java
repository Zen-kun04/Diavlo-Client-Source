/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import net.minecraft.world.storage.ISaveFormat;
/*    */ import net.minecraft.world.storage.WorldInfo;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ 
/*    */ public class GuiRenameWorld
/*    */   extends GuiScreen
/*    */ {
/*    */   private GuiScreen parentScreen;
/*    */   private GuiTextField field_146583_f;
/*    */   private final String saveName;
/*    */   
/*    */   public GuiRenameWorld(GuiScreen parentScreenIn, String saveNameIn) {
/* 17 */     this.parentScreen = parentScreenIn;
/* 18 */     this.saveName = saveNameIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateScreen() {
/* 23 */     this.field_146583_f.updateCursorCounter();
/*    */   }
/*    */ 
/*    */   
/*    */   public void initGui() {
/* 28 */     Keyboard.enableRepeatEvents(true);
/* 29 */     this.buttonList.clear();
/* 30 */     this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, I18n.format("selectWorld.renameButton", new Object[0])));
/* 31 */     this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
/* 32 */     ISaveFormat isaveformat = this.mc.getSaveLoader();
/* 33 */     WorldInfo worldinfo = isaveformat.getWorldInfo(this.saveName);
/* 34 */     String s = worldinfo.getWorldName();
/* 35 */     this.field_146583_f = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
/* 36 */     this.field_146583_f.setFocused(true);
/* 37 */     this.field_146583_f.setText(s);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onGuiClosed() {
/* 42 */     Keyboard.enableRepeatEvents(false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void actionPerformed(GuiButton button) throws IOException {
/* 47 */     if (button.enabled)
/*    */     {
/* 49 */       if (button.id == 1) {
/*    */         
/* 51 */         this.mc.displayGuiScreen(this.parentScreen);
/*    */       }
/* 53 */       else if (button.id == 0) {
/*    */         
/* 55 */         ISaveFormat isaveformat = this.mc.getSaveLoader();
/* 56 */         isaveformat.renameWorld(this.saveName, this.field_146583_f.getText().trim());
/* 57 */         this.mc.displayGuiScreen(this.parentScreen);
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/* 64 */     this.field_146583_f.textboxKeyTyped(typedChar, keyCode);
/* 65 */     ((GuiButton)this.buttonList.get(0)).enabled = (this.field_146583_f.getText().trim().length() > 0);
/*    */     
/* 67 */     if (keyCode == 28 || keyCode == 156)
/*    */     {
/* 69 */       actionPerformed(this.buttonList.get(0));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 75 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/* 76 */     this.field_146583_f.mouseClicked(mouseX, mouseY, mouseButton);
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 81 */     drawDefaultBackground();
/* 82 */     drawCenteredString(this.fontRendererObj, I18n.format("selectWorld.renameTitle", new Object[0]), this.width / 2, 20, 16777215);
/* 83 */     drawString(this.fontRendererObj, I18n.format("selectWorld.enterName", new Object[0]), this.width / 2 - 100, 47, 10526880);
/* 84 */     this.field_146583_f.drawTextBox();
/* 85 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiRenameWorld.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */