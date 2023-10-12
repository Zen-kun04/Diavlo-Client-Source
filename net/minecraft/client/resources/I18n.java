/*    */ package net.minecraft.client.resources;
/*    */ 
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public class I18n
/*    */ {
/*    */   private static Locale i18nLocale;
/*    */   
/*    */   static void setLocale(Locale i18nLocaleIn) {
/* 11 */     i18nLocale = i18nLocaleIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public static String format(String translateKey, Object... parameters) {
/* 16 */     return i18nLocale.formatMessage(translateKey, parameters);
/*    */   }
/*    */ 
/*    */   
/*    */   public static Map getLocaleProperties() {
/* 21 */     return i18nLocale.properties;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\resources\I18n.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */