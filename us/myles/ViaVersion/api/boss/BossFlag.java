/*    */ package us.myles.ViaVersion.api.boss;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public enum BossFlag
/*    */ {
/* 28 */   DARKEN_SKY(1),
/* 29 */   PLAY_BOSS_MUSIC(2);
/*    */   
/*    */   private final int id;
/*    */   
/*    */   BossFlag(int id) {
/* 34 */     this.id = id;
/*    */   }
/*    */   
/*    */   public int getId() {
/* 38 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar\\us\myles\ViaVersion\api\boss\BossFlag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */