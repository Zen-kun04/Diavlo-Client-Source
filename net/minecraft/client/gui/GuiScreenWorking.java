/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import net.minecraft.util.IProgressUpdate;
/*    */ import net.optifine.CustomLoadingScreen;
/*    */ import net.optifine.CustomLoadingScreens;
/*    */ 
/*    */ public class GuiScreenWorking
/*    */   extends GuiScreen implements IProgressUpdate {
/*  9 */   private String field_146591_a = "";
/* 10 */   private String field_146589_f = "";
/*    */   private int progress;
/*    */   private boolean doneWorking;
/* 13 */   private CustomLoadingScreen customLoadingScreen = CustomLoadingScreens.getCustomLoadingScreen();
/*    */ 
/*    */   
/*    */   public void displaySavingString(String message) {
/* 17 */     resetProgressAndMessage(message);
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetProgressAndMessage(String message) {
/* 22 */     this.field_146591_a = message;
/* 23 */     displayLoadingString("Working...");
/*    */   }
/*    */ 
/*    */   
/*    */   public void displayLoadingString(String message) {
/* 28 */     this.field_146589_f = message;
/* 29 */     setLoadingProgress(0);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setLoadingProgress(int progress) {
/* 34 */     this.progress = progress;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDoneWorking() {
/* 39 */     this.doneWorking = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 44 */     if (this.doneWorking) {
/*    */       
/* 46 */       if (!this.mc.isConnectedToRealms())
/*    */       {
/* 48 */         this.mc.displayGuiScreen((GuiScreen)null);
/*    */       }
/*    */     }
/*    */     else {
/*    */       
/* 53 */       if (this.customLoadingScreen != null && this.mc.theWorld == null) {
/*    */         
/* 55 */         this.customLoadingScreen.drawBackground(this.width, this.height);
/*    */       }
/*    */       else {
/*    */         
/* 59 */         drawDefaultBackground();
/*    */       } 
/*    */       
/* 62 */       if (this.progress > 0) {
/*    */         
/* 64 */         drawCenteredString(this.fontRendererObj, this.field_146591_a, this.width / 2, 70, 16777215);
/* 65 */         drawCenteredString(this.fontRendererObj, this.field_146589_f + " " + this.progress + "%", this.width / 2, 90, 16777215);
/*    */       } 
/*    */       
/* 68 */       super.drawScreen(mouseX, mouseY, partialTicks);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiScreenWorking.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */