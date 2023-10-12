/*    */ package net.minecraft.util;
/*    */ 
/*    */ import net.minecraft.crash.CrashReport;
/*    */ 
/*    */ public class ReportedException
/*    */   extends RuntimeException
/*    */ {
/*    */   private final CrashReport theReportedExceptionCrashReport;
/*    */   
/*    */   public ReportedException(CrashReport report) {
/* 11 */     this.theReportedExceptionCrashReport = report;
/*    */   }
/*    */ 
/*    */   
/*    */   public CrashReport getCrashReport() {
/* 16 */     return this.theReportedExceptionCrashReport;
/*    */   }
/*    */ 
/*    */   
/*    */   public Throwable getCause() {
/* 21 */     return this.theReportedExceptionCrashReport.getCrashCause();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 26 */     return this.theReportedExceptionCrashReport.getDescription();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\ReportedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */