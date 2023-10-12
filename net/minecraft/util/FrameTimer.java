/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class FrameTimer
/*    */ {
/*  5 */   private final long[] frames = new long[240];
/*    */   
/*    */   private int lastIndex;
/*    */   private int counter;
/*    */   private int index;
/*    */   
/*    */   public void addFrame(long runningTime) {
/* 12 */     this.frames[this.index] = runningTime;
/* 13 */     this.index++;
/*    */     
/* 15 */     if (this.index == 240)
/*    */     {
/* 17 */       this.index = 0;
/*    */     }
/*    */     
/* 20 */     if (this.counter < 240) {
/*    */       
/* 22 */       this.lastIndex = 0;
/* 23 */       this.counter++;
/*    */     }
/*    */     else {
/*    */       
/* 27 */       this.lastIndex = parseIndex(this.index + 1);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int getLagometerValue(long time, int multiplier) {
/* 33 */     double d0 = time / 1.6666666E7D;
/* 34 */     return (int)(d0 * multiplier);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getLastIndex() {
/* 39 */     return this.lastIndex;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getIndex() {
/* 44 */     return this.index;
/*    */   }
/*    */ 
/*    */   
/*    */   public int parseIndex(int rawIndex) {
/* 49 */     return rawIndex % 240;
/*    */   }
/*    */ 
/*    */   
/*    */   public long[] getFrames() {
/* 54 */     return this.frames;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\FrameTimer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */