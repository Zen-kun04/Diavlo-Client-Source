/*    */ package rip.diavlo.base.managers;
/*    */ 
/*    */ public class TimerUtil
/*    */ {
/*    */   private long currentTime;
/*    */   
/*    */   public TimerUtil() {
/*  8 */     setCurrentTime(getCurrentTime());
/*    */   }
/*    */   
/*    */   public long getCurrentTime() {
/* 12 */     return System.nanoTime() / 1000000L;
/*    */   }
/*    */   
/*    */   public void reset() {
/* 16 */     setCurrentTime(getCurrentTime());
/*    */   }
/*    */   
/*    */   public long getStartTime() {
/* 20 */     return this.currentTime;
/*    */   }
/*    */   
/*    */   public void setCurrentTime(long currentTime) {
/* 24 */     this.currentTime = currentTime;
/*    */   }
/*    */   
/*    */   public long getMs() {
/* 28 */     return getDelay() / 1000000L - this.currentTime;
/*    */   }
/*    */   
/*    */   public long getDelay() {
/* 32 */     return System.nanoTime();
/*    */   }
/*    */   
/*    */   public boolean hasReached(long milliseconds) {
/* 36 */     return (getCurrentTime() - getStartTime() >= milliseconds);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\managers\TimerUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */