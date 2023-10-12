/*     */ package net.optifine;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.src.Config;
/*     */ import net.optifine.http.FileUploadThread;
/*     */ import net.optifine.http.IFileUploadListener;
/*     */ import net.optifine.shaders.Shaders;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CrashReporter
/*     */ {
/*     */   public static void onCrashReport(CrashReport crashReport, CrashReportCategory category) {
/*     */     try {
/*  20 */       Throwable throwable = crashReport.getCrashCause();
/*     */       
/*  22 */       if (throwable == null) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/*  27 */       if (throwable.getClass().getName().contains(".fml.client.SplashProgress")) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/*  32 */       if (throwable.getClass() == Throwable.class) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/*  37 */       extendCrashReport(category);
/*  38 */       GameSettings gamesettings = Config.getGameSettings();
/*     */       
/*  40 */       if (gamesettings == null) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/*  45 */       if (!gamesettings.snooperEnabled) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/*  50 */       String s = "http://optifine.net/crashReport";
/*  51 */       String s1 = makeReport(crashReport);
/*  52 */       byte[] abyte = s1.getBytes("ASCII");
/*  53 */       IFileUploadListener ifileuploadlistener = new IFileUploadListener()
/*     */         {
/*     */           public void fileUploadFinished(String url, byte[] content, Throwable exception) {}
/*     */         };
/*     */ 
/*     */       
/*  59 */       Map<Object, Object> map = new HashMap<>();
/*  60 */       map.put("OF-Version", Config.getVersion());
/*  61 */       map.put("OF-Summary", makeSummary(crashReport));
/*  62 */       FileUploadThread fileuploadthread = new FileUploadThread(s, map, abyte, ifileuploadlistener);
/*  63 */       fileuploadthread.setPriority(10);
/*  64 */       fileuploadthread.start();
/*  65 */       Thread.sleep(1000L);
/*     */     }
/*  67 */     catch (Exception exception) {
/*     */       
/*  69 */       Config.dbg(exception.getClass().getName() + ": " + exception.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static String makeReport(CrashReport crashReport) {
/*  75 */     StringBuffer stringbuffer = new StringBuffer();
/*  76 */     stringbuffer.append("OptiFineVersion: " + Config.getVersion() + "\n");
/*  77 */     stringbuffer.append("Summary: " + makeSummary(crashReport) + "\n");
/*  78 */     stringbuffer.append("\n");
/*  79 */     stringbuffer.append(crashReport.getCompleteReport());
/*  80 */     stringbuffer.append("\n");
/*  81 */     return stringbuffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static String makeSummary(CrashReport crashReport) {
/*  86 */     Throwable throwable = crashReport.getCrashCause();
/*     */     
/*  88 */     if (throwable == null)
/*     */     {
/*  90 */       return "Unknown";
/*     */     }
/*     */ 
/*     */     
/*  94 */     StackTraceElement[] astacktraceelement = throwable.getStackTrace();
/*  95 */     String s = "unknown";
/*     */     
/*  97 */     if (astacktraceelement.length > 0)
/*     */     {
/*  99 */       s = astacktraceelement[0].toString().trim();
/*     */     }
/*     */     
/* 102 */     String s1 = throwable.getClass().getName() + ": " + throwable.getMessage() + " (" + crashReport.getDescription() + ") [" + s + "]";
/* 103 */     return s1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void extendCrashReport(CrashReportCategory cat) {
/* 109 */     cat.addCrashSection("OptiFine Version", Config.getVersion());
/* 110 */     cat.addCrashSection("OptiFine Build", Config.getBuild());
/*     */     
/* 112 */     if (Config.getGameSettings() != null) {
/*     */       
/* 114 */       cat.addCrashSection("Render Distance Chunks", "" + Config.getChunkViewDistance());
/* 115 */       cat.addCrashSection("Mipmaps", "" + Config.getMipmapLevels());
/* 116 */       cat.addCrashSection("Anisotropic Filtering", "" + Config.getAnisotropicFilterLevel());
/* 117 */       cat.addCrashSection("Antialiasing", "" + Config.getAntialiasingLevel());
/* 118 */       cat.addCrashSection("Multitexture", "" + Config.isMultiTexture());
/*     */     } 
/*     */     
/* 121 */     cat.addCrashSection("Shaders", "" + Shaders.getShaderPackName());
/* 122 */     cat.addCrashSection("OpenGlVersion", "" + Config.openGlVersion);
/* 123 */     cat.addCrashSection("OpenGlRenderer", "" + Config.openGlRenderer);
/* 124 */     cat.addCrashSection("OpenGlVendor", "" + Config.openGlVendor);
/* 125 */     cat.addCrashSection("CpuCount", "" + Config.getAvailableProcessors());
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\CrashReporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */