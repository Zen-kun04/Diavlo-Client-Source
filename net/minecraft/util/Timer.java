/*    */ package net.minecraft.util;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ 
/*    */ public class Timer
/*    */ {
/*    */   float ticksPerSecond;
/*    */   private double lastHRTime;
/*    */   public int elapsedTicks;
/*    */   public float renderPartialTicks;
/* 11 */   public static float timerSpeed = 1.0F;
/*    */   public float elapsedPartialTicks;
/*    */   private long lastSyncSysClock;
/*    */   private long lastSyncHRClock;
/*    */   private long counter;
/* 16 */   private double timeSyncAdjustment = 1.0D;
/*    */ 
/*    */   
/*    */   public Timer(float tps) {
/* 20 */     this.ticksPerSecond = tps;
/* 21 */     this.lastSyncSysClock = Minecraft.getSystemTime();
/* 22 */     this.lastSyncHRClock = System.nanoTime() / 1000000L;
/*    */   }
/*    */   
/*    */   public static void setTimerSpeed(float value) {
/* 26 */     timerSpeed = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateTimer() {
/* 31 */     long i = Minecraft.getSystemTime();
/* 32 */     long j = i - this.lastSyncSysClock;
/* 33 */     long k = System.nanoTime() / 1000000L;
/* 34 */     double d0 = k / 1000.0D;
/*    */     
/* 36 */     if (j <= 1000L && j >= 0L) {
/*    */       
/* 38 */       this.counter += j;
/*    */       
/* 40 */       if (this.counter > 1000L) {
/*    */         
/* 42 */         long l = k - this.lastSyncHRClock;
/* 43 */         double d1 = this.counter / l;
/* 44 */         this.timeSyncAdjustment += (d1 - this.timeSyncAdjustment) * 0.20000000298023224D;
/* 45 */         this.lastSyncHRClock = k;
/* 46 */         this.counter = 0L;
/*    */       } 
/*    */       
/* 49 */       if (this.counter < 0L)
/*    */       {
/* 51 */         this.lastSyncHRClock = k;
/*    */       }
/*    */     }
/*    */     else {
/*    */       
/* 56 */       this.lastHRTime = d0;
/*    */     } 
/*    */     
/* 59 */     this.lastSyncSysClock = i;
/* 60 */     double d2 = (d0 - this.lastHRTime) * this.timeSyncAdjustment;
/* 61 */     this.lastHRTime = d0;
/* 62 */     d2 = MathHelper.clamp_double(d2, 0.0D, 1.0D);
/* 63 */     this; this.elapsedPartialTicks = (float)(this.elapsedPartialTicks + d2 * timerSpeed * this.ticksPerSecond);
/* 64 */     this.elapsedTicks = (int)this.elapsedPartialTicks;
/* 65 */     this.elapsedPartialTicks -= this.elapsedTicks;
/*    */     
/* 67 */     if (this.elapsedTicks > 10)
/*    */     {
/* 69 */       this.elapsedTicks = 10;
/*    */     }
/*    */     
/* 72 */     this.renderPartialTicks = this.elapsedPartialTicks;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\Timer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */