/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ public class StringUtils
/*    */ {
/*  7 */   private static final Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
/*    */ 
/*    */   
/*    */   public static String ticksToElapsedTime(int ticks) {
/* 11 */     int i = ticks / 20;
/* 12 */     int j = i / 60;
/* 13 */     i %= 60;
/* 14 */     return (i < 10) ? (j + ":0" + i) : (j + ":" + i);
/*    */   }
/*    */ 
/*    */   
/*    */   public static String stripControlCodes(String text) {
/* 19 */     return patternControlCode.matcher(text).replaceAll("");
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean isNullOrEmpty(String string) {
/* 24 */     return org.apache.commons.lang3.StringUtils.isEmpty(string);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\StringUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */