/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.client.network.NetHandlerPlayClient;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.C0BPacketEntityAction;
/*    */ 
/*    */ public class GuiSleepMP extends GuiChat {
/*    */   public void initGui() {
/* 12 */     super.initGui();
/* 13 */     this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 40, I18n.format("multiplayer.stopSleeping", new Object[0])));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/* 18 */     if (keyCode == 1) {
/*    */       
/* 20 */       wakeFromSleep();
/*    */     }
/* 22 */     else if (keyCode != 28 && keyCode != 156) {
/*    */       
/* 24 */       super.keyTyped(typedChar, keyCode);
/*    */     }
/*    */     else {
/*    */       
/* 28 */       String s = this.inputField.getText().trim();
/*    */       
/* 30 */       if (!s.isEmpty())
/*    */       {
/* 32 */         this.mc.thePlayer.sendChatMessage(s);
/*    */       }
/*    */       
/* 35 */       this.inputField.setText("");
/* 36 */       this.mc.ingameGUI.getChatGUI().resetScroll();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void actionPerformed(GuiButton button) throws IOException {
/* 42 */     if (button.id == 1) {
/*    */       
/* 44 */       wakeFromSleep();
/*    */     }
/*    */     else {
/*    */       
/* 48 */       super.actionPerformed(button);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private void wakeFromSleep() {
/* 54 */     NetHandlerPlayClient nethandlerplayclient = this.mc.thePlayer.sendQueue;
/* 55 */     nethandlerplayclient.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)this.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SLEEPING));
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiSleepMP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */