/*    */ package rip.diavlo.base.utils;
/*    */ 
/*    */ public final class MSTimer
/*    */ {
/*  5 */   private long time = -1L;
/*    */   
/*    */   public boolean hasTimePassed(long MS) {
/*  8 */     return (System.currentTimeMillis() >= this.time + MS);
/*    */   }
/*    */   
/*    */   public long hasTimeLeft(long MS) {
/* 12 */     return MS + this.time - System.currentTimeMillis();
/*    */   }
/*    */   
/*    */   public void reset() {
/* 16 */     this.time = System.currentTimeMillis();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\utils\MSTimer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */