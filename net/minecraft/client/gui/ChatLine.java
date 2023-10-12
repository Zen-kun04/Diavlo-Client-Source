/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import net.minecraft.util.IChatComponent;
/*    */ 
/*    */ 
/*    */ public class ChatLine
/*    */ {
/*    */   private final int updateCounterCreated;
/*    */   private final IChatComponent lineString;
/*    */   private final int chatLineID;
/*    */   
/*    */   public ChatLine(int p_i45000_1_, IChatComponent p_i45000_2_, int p_i45000_3_) {
/* 13 */     this.lineString = p_i45000_2_;
/* 14 */     this.updateCounterCreated = p_i45000_1_;
/* 15 */     this.chatLineID = p_i45000_3_;
/*    */   }
/*    */ 
/*    */   
/*    */   public IChatComponent getChatComponent() {
/* 20 */     return this.lineString;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getUpdatedCounter() {
/* 25 */     return this.updateCounterCreated;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getChatLineID() {
/* 30 */     return this.chatLineID;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\ChatLine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */