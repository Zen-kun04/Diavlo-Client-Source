/*    */ package rip.diavlo.base.utils;
/*    */ 
/*    */ public class Random
/*    */ {
/*  5 */   static final java.util.Random rand = new java.util.Random();
/*    */ 
/*    */   
/*    */   public static int rand(int max) {
/*  9 */     return rand.nextInt(max);
/*    */   }
/*    */ 
/*    */   
/*    */   public static int rand() {
/* 14 */     return rand.nextInt();
/*    */   }
/*    */ 
/*    */   
/*    */   public static byte randByte() {
/* 19 */     return (byte)rand();
/*    */   }
/*    */ 
/*    */   
/*    */   public static byte[] randBytes(int len) {
/* 24 */     byte[] a = new byte[len];
/* 25 */     for (int i = 0; i < len; i++) {
/* 26 */       a[i] = randByte();
/*    */     }
/* 28 */     return a;
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean randBool() {
/* 33 */     return rand.nextBoolean();
/*    */   }
/*    */ 
/*    */   
/*    */   public static long randLong() {
/* 38 */     return rand.nextLong();
/*    */   }
/*    */ 
/*    */   
/*    */   public static float randFloat() {
/* 43 */     return rand.nextFloat();
/*    */   }
/*    */ 
/*    */   
/*    */   public static double randDouble() {
/* 48 */     return rand.nextDouble();
/*    */   }
/*    */ 
/*    */   
/*    */   public static double randDouble(double min, double max) {
/* 53 */     return rand.nextDouble() * (max - min) + min;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\utils\Random.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */