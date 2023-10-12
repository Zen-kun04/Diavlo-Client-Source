/*    */ package de.gerrygames.viarewind.fabric.util;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.LogRecord;
/*    */ import java.util.logging.Logger;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class LoggerWrapper extends Logger {
/*    */   private final Logger base;
/*    */   
/*    */   public LoggerWrapper(Logger logger) {
/* 13 */     super("logger", null);
/* 14 */     this.base = logger;
/*    */   }
/*    */   
/*    */   public void log(LogRecord record) {
/* 18 */     log(record.getLevel(), record.getMessage());
/*    */   }
/*    */   
/*    */   public void log(Level level, String msg) {
/* 22 */     if (level == Level.FINE) {
/* 23 */       this.base.debug(msg);
/* 24 */     } else if (level == Level.WARNING) {
/* 25 */       this.base.warn(msg);
/* 26 */     } else if (level == Level.SEVERE) {
/* 27 */       this.base.error(msg);
/* 28 */     } else if (level == Level.INFO) {
/* 29 */       this.base.info(msg);
/*    */     } else {
/* 31 */       this.base.trace(msg);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void log(Level level, String msg, Object param1) {
/* 37 */     if (level == Level.FINE) {
/* 38 */       this.base.debug(msg, param1);
/* 39 */     } else if (level == Level.WARNING) {
/* 40 */       this.base.warn(msg, param1);
/* 41 */     } else if (level == Level.SEVERE) {
/* 42 */       this.base.error(msg, param1);
/* 43 */     } else if (level == Level.INFO) {
/* 44 */       this.base.info(msg, param1);
/*    */     } else {
/* 46 */       this.base.trace(msg, param1);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void log(Level level, String msg, Object[] params) {
/* 52 */     log(level, MessageFormat.format(msg, params));
/*    */   }
/*    */   
/*    */   public void log(Level level, String msg, Throwable params) {
/* 56 */     if (level == Level.FINE) {
/* 57 */       this.base.debug(msg, params);
/* 58 */     } else if (level == Level.WARNING) {
/* 59 */       this.base.warn(msg, params);
/* 60 */     } else if (level == Level.SEVERE) {
/* 61 */       this.base.error(msg, params);
/* 62 */     } else if (level == Level.INFO) {
/* 63 */       this.base.info(msg, params);
/*    */     } else {
/* 65 */       this.base.trace(msg, params);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\de\gerrygames\viarewind\fabri\\util\LoggerWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */