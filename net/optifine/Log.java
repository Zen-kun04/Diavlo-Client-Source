/*    */ package net.optifine;
/*    */ 
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class Log
/*    */ {
/*  8 */   private static final Logger LOGGER = LogManager.getLogger();
/*  9 */   public static final boolean logDetail = System.getProperty("log.detail", "false").equals("true");
/*    */ 
/*    */   
/*    */   public static void detail(String s) {
/* 13 */     if (logDetail)
/*    */     {
/* 15 */       LOGGER.info("[OptiFine] " + s);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public static void dbg(String s) {
/* 21 */     LOGGER.info("[OptiFine] " + s);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void warn(String s) {
/* 26 */     LOGGER.warn("[OptiFine] " + s);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void warn(String s, Throwable t) {
/* 31 */     LOGGER.warn("[OptiFine] " + s, t);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void error(String s) {
/* 36 */     LOGGER.error("[OptiFine] " + s);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void error(String s, Throwable t) {
/* 41 */     LOGGER.error("[OptiFine] " + s, t);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void log(String s) {
/* 46 */     dbg(s);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\Log.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */