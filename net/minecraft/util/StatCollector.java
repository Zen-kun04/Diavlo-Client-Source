/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class StatCollector
/*    */ {
/*  5 */   private static final StringTranslate localizedName = StringTranslate.getInstance();
/*  6 */   private static final StringTranslate fallbackTranslator = new StringTranslate();
/*    */ 
/*    */   
/*    */   public static String translateToLocal(String key) {
/* 10 */     return localizedName.translateKey(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public static String translateToLocalFormatted(String key, Object... format) {
/* 15 */     return localizedName.translateKeyFormat(key, format);
/*    */   }
/*    */ 
/*    */   
/*    */   public static String translateToFallback(String key) {
/* 20 */     return fallbackTranslator.translateKey(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean canTranslate(String key) {
/* 25 */     return localizedName.isKeyTranslated(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public static long getLastTranslationUpdateTimeInMilliseconds() {
/* 30 */     return localizedName.getLastUpdateTimeInMilliseconds();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\StatCollector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */