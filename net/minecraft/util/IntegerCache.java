/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class IntegerCache
/*    */ {
/*  5 */   private static final Integer[] CACHE = new Integer[65535];
/*    */ 
/*    */   
/*    */   public static Integer getInteger(int value) {
/*  9 */     return (value >= 0 && value < CACHE.length) ? CACHE[value] : new Integer(value);
/*    */   }
/*    */ 
/*    */   
/*    */   static {
/* 14 */     int i = 0;
/*    */     
/* 16 */     for (int j = CACHE.length; i < j; i++)
/*    */     {
/* 18 */       CACHE[i] = Integer.valueOf(i);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\IntegerCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */