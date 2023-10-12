/*     */ package net.minecraft.crash;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.RuntimeMXBean;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import net.minecraft.world.gen.layer.IntCache;
/*     */ import net.optifine.CrashReporter;
/*     */ import net.optifine.reflect.Reflector;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ public class CrashReport
/*     */ {
/*  26 */   private static final Logger logger = LogManager.getLogger();
/*     */   private final String description;
/*     */   private final Throwable cause;
/*  29 */   private final CrashReportCategory theReportCategory = new CrashReportCategory(this, "System Details");
/*  30 */   private final List<CrashReportCategory> crashReportSections = Lists.newArrayList();
/*     */   private File crashReportFile;
/*     */   private boolean firstCategoryInCrashReport = true;
/*  33 */   private StackTraceElement[] stacktrace = new StackTraceElement[0];
/*     */   
/*     */   private boolean reported = false;
/*     */   
/*     */   public CrashReport(String descriptionIn, Throwable causeThrowable) {
/*  38 */     this.description = descriptionIn;
/*  39 */     this.cause = causeThrowable;
/*  40 */     populateEnvironment();
/*     */   }
/*     */ 
/*     */   
/*     */   private void populateEnvironment() {
/*  45 */     this.theReportCategory.addCrashSectionCallable("Minecraft Version", new Callable<String>()
/*     */         {
/*     */           public String call()
/*     */           {
/*  49 */             return "1.8.9";
/*     */           }
/*     */         });
/*  52 */     this.theReportCategory.addCrashSectionCallable("Operating System", new Callable<String>()
/*     */         {
/*     */           public String call()
/*     */           {
/*  56 */             return System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version");
/*     */           }
/*     */         });
/*  59 */     this.theReportCategory.addCrashSectionCallable("Java Version", new Callable<String>()
/*     */         {
/*     */           public String call()
/*     */           {
/*  63 */             return System.getProperty("java.version") + ", " + System.getProperty("java.vendor");
/*     */           }
/*     */         });
/*  66 */     this.theReportCategory.addCrashSectionCallable("Java VM Version", new Callable<String>()
/*     */         {
/*     */           public String call()
/*     */           {
/*  70 */             return System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor");
/*     */           }
/*     */         });
/*  73 */     this.theReportCategory.addCrashSectionCallable("Memory", new Callable<String>()
/*     */         {
/*     */           public String call()
/*     */           {
/*  77 */             Runtime runtime = Runtime.getRuntime();
/*  78 */             long i = runtime.maxMemory();
/*  79 */             long j = runtime.totalMemory();
/*  80 */             long k = runtime.freeMemory();
/*  81 */             long l = i / 1024L / 1024L;
/*  82 */             long i1 = j / 1024L / 1024L;
/*  83 */             long j1 = k / 1024L / 1024L;
/*  84 */             return k + " bytes (" + j1 + " MB) / " + j + " bytes (" + i1 + " MB) up to " + i + " bytes (" + l + " MB)";
/*     */           }
/*     */         });
/*  87 */     this.theReportCategory.addCrashSectionCallable("JVM Flags", new Callable<String>()
/*     */         {
/*     */           public String call()
/*     */           {
/*  91 */             RuntimeMXBean runtimemxbean = ManagementFactory.getRuntimeMXBean();
/*  92 */             List<String> list = runtimemxbean.getInputArguments();
/*  93 */             int i = 0;
/*  94 */             StringBuilder stringbuilder = new StringBuilder();
/*     */             
/*  96 */             for (String s : list) {
/*     */               
/*  98 */               if (s.startsWith("-X")) {
/*     */                 
/* 100 */                 if (i++ > 0)
/*     */                 {
/* 102 */                   stringbuilder.append(" ");
/*     */                 }
/*     */                 
/* 105 */                 stringbuilder.append(s);
/*     */               } 
/*     */             } 
/*     */             
/* 109 */             return String.format("%d total; %s", new Object[] { Integer.valueOf(i), stringbuilder.toString() });
/*     */           }
/*     */         });
/* 112 */     this.theReportCategory.addCrashSectionCallable("IntCache", new Callable<String>()
/*     */         {
/*     */           public String call() throws Exception
/*     */           {
/* 116 */             return IntCache.getCacheSizes();
/*     */           }
/*     */         });
/*     */     
/* 120 */     if (Reflector.FMLCommonHandler_enhanceCrashReport.exists()) {
/*     */       
/* 122 */       Object object = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
/* 123 */       Reflector.callString(object, Reflector.FMLCommonHandler_enhanceCrashReport, new Object[] { this, this.theReportCategory });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 129 */     return this.description;
/*     */   }
/*     */ 
/*     */   
/*     */   public Throwable getCrashCause() {
/* 134 */     return this.cause;
/*     */   }
/*     */ 
/*     */   
/*     */   public void getSectionsInStringBuilder(StringBuilder builder) {
/* 139 */     if ((this.stacktrace == null || this.stacktrace.length <= 0) && this.crashReportSections.size() > 0)
/*     */     {
/* 141 */       this.stacktrace = (StackTraceElement[])ArrayUtils.subarray((Object[])((CrashReportCategory)this.crashReportSections.get(0)).getStackTrace(), 0, 1);
/*     */     }
/*     */     
/* 144 */     if (this.stacktrace != null && this.stacktrace.length > 0) {
/*     */       
/* 146 */       builder.append("-- Head --\n");
/* 147 */       builder.append("Stacktrace:\n");
/*     */       
/* 149 */       for (StackTraceElement stacktraceelement : this.stacktrace) {
/*     */         
/* 151 */         builder.append("\t").append("at ").append(stacktraceelement.toString());
/* 152 */         builder.append("\n");
/*     */       } 
/*     */       
/* 155 */       builder.append("\n");
/*     */     } 
/*     */     
/* 158 */     for (CrashReportCategory crashreportcategory : this.crashReportSections) {
/*     */       
/* 160 */       crashreportcategory.appendToStringBuilder(builder);
/* 161 */       builder.append("\n\n");
/*     */     } 
/*     */     
/* 164 */     this.theReportCategory.appendToStringBuilder(builder);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCauseStackTraceOrString() {
/* 169 */     StringWriter stringwriter = null;
/* 170 */     PrintWriter printwriter = null;
/* 171 */     Throwable throwable = this.cause;
/*     */     
/* 173 */     if (throwable.getMessage() == null) {
/*     */       
/* 175 */       if (throwable instanceof NullPointerException) {
/*     */         
/* 177 */         throwable = new NullPointerException(this.description);
/*     */       }
/* 179 */       else if (throwable instanceof StackOverflowError) {
/*     */         
/* 181 */         throwable = new StackOverflowError(this.description);
/*     */       }
/* 183 */       else if (throwable instanceof OutOfMemoryError) {
/*     */         
/* 185 */         throwable = new OutOfMemoryError(this.description);
/*     */       } 
/*     */       
/* 188 */       throwable.setStackTrace(this.cause.getStackTrace());
/*     */     } 
/*     */     
/* 191 */     String s = throwable.toString();
/*     */ 
/*     */     
/*     */     try {
/* 195 */       stringwriter = new StringWriter();
/* 196 */       printwriter = new PrintWriter(stringwriter);
/* 197 */       throwable.printStackTrace(printwriter);
/* 198 */       s = stringwriter.toString();
/*     */     }
/*     */     finally {
/*     */       
/* 202 */       IOUtils.closeQuietly(stringwriter);
/* 203 */       IOUtils.closeQuietly(printwriter);
/*     */     } 
/*     */     
/* 206 */     return s;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCompleteReport() {
/* 211 */     if (!this.reported) {
/*     */       
/* 213 */       this.reported = true;
/* 214 */       CrashReporter.onCrashReport(this, this.theReportCategory);
/*     */     } 
/*     */     
/* 217 */     StringBuilder stringbuilder = new StringBuilder();
/* 218 */     stringbuilder.append("---- Minecraft Crash Report ----\n");
/* 219 */     Reflector.call(Reflector.BlamingTransformer_onCrash, new Object[] { stringbuilder });
/* 220 */     Reflector.call(Reflector.CoreModManager_onCrash, new Object[] { stringbuilder });
/* 221 */     stringbuilder.append("// ");
/* 222 */     stringbuilder.append(getWittyComment());
/* 223 */     stringbuilder.append("\n\n");
/* 224 */     stringbuilder.append("Time: ");
/* 225 */     stringbuilder.append((new SimpleDateFormat()).format(new Date()));
/* 226 */     stringbuilder.append("\n");
/* 227 */     stringbuilder.append("Description: ");
/* 228 */     stringbuilder.append(this.description);
/* 229 */     stringbuilder.append("\n\n");
/* 230 */     stringbuilder.append(getCauseStackTraceOrString());
/* 231 */     stringbuilder.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");
/*     */     
/* 233 */     for (int i = 0; i < 87; i++)
/*     */     {
/* 235 */       stringbuilder.append("-");
/*     */     }
/*     */     
/* 238 */     stringbuilder.append("\n\n");
/* 239 */     getSectionsInStringBuilder(stringbuilder);
/* 240 */     return stringbuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public File getFile() {
/* 245 */     return this.crashReportFile;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean saveToFile(File toFile) {
/* 250 */     if (this.crashReportFile != null)
/*     */     {
/* 252 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 256 */     if (toFile.getParentFile() != null)
/*     */     {
/* 258 */       toFile.getParentFile().mkdirs();
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 263 */       FileWriter filewriter = new FileWriter(toFile);
/* 264 */       filewriter.write(getCompleteReport());
/* 265 */       filewriter.close();
/* 266 */       this.crashReportFile = toFile;
/* 267 */       return true;
/*     */     }
/* 269 */     catch (Throwable throwable) {
/*     */       
/* 271 */       logger.error("Could not save crash report to " + toFile, throwable);
/* 272 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CrashReportCategory getCategory() {
/* 279 */     return this.theReportCategory;
/*     */   }
/*     */ 
/*     */   
/*     */   public CrashReportCategory makeCategory(String name) {
/* 284 */     return makeCategoryDepth(name, 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public CrashReportCategory makeCategoryDepth(String categoryName, int stacktraceLength) {
/* 289 */     CrashReportCategory crashreportcategory = new CrashReportCategory(this, categoryName);
/*     */     
/* 291 */     if (this.firstCategoryInCrashReport) {
/*     */       
/* 293 */       int i = crashreportcategory.getPrunedStackTrace(stacktraceLength);
/* 294 */       StackTraceElement[] astacktraceelement = this.cause.getStackTrace();
/* 295 */       StackTraceElement stacktraceelement = null;
/* 296 */       StackTraceElement stacktraceelement1 = null;
/* 297 */       int j = astacktraceelement.length - i;
/*     */       
/* 299 */       if (j < 0)
/*     */       {
/* 301 */         System.out.println("Negative index in crash report handler (" + astacktraceelement.length + "/" + i + ")");
/*     */       }
/*     */       
/* 304 */       if (astacktraceelement != null && 0 <= j && j < astacktraceelement.length) {
/*     */         
/* 306 */         stacktraceelement = astacktraceelement[j];
/*     */         
/* 308 */         if (astacktraceelement.length + 1 - i < astacktraceelement.length)
/*     */         {
/* 310 */           stacktraceelement1 = astacktraceelement[astacktraceelement.length + 1 - i];
/*     */         }
/*     */       } 
/*     */       
/* 314 */       this.firstCategoryInCrashReport = crashreportcategory.firstTwoElementsOfStackTraceMatch(stacktraceelement, stacktraceelement1);
/*     */       
/* 316 */       if (i > 0 && !this.crashReportSections.isEmpty()) {
/*     */         
/* 318 */         CrashReportCategory crashreportcategory1 = this.crashReportSections.get(this.crashReportSections.size() - 1);
/* 319 */         crashreportcategory1.trimStackTraceEntriesFromBottom(i);
/*     */       }
/* 321 */       else if (astacktraceelement != null && astacktraceelement.length >= i && 0 <= j && j < astacktraceelement.length) {
/*     */         
/* 323 */         this.stacktrace = new StackTraceElement[j];
/* 324 */         System.arraycopy(astacktraceelement, 0, this.stacktrace, 0, this.stacktrace.length);
/*     */       }
/*     */       else {
/*     */         
/* 328 */         this.firstCategoryInCrashReport = false;
/*     */       } 
/*     */     } 
/*     */     
/* 332 */     this.crashReportSections.add(crashreportcategory);
/* 333 */     return crashreportcategory;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getWittyComment() {
/* 338 */     String[] astring = { "Who set us up the TNT?", "Everything's going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I'm sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don't be sad. I'll do better next time, I promise!", "Don't be sad, have a hug! <3", "I just don't know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn't worry myself about that.", "I bet Cylons wouldn't have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I'm Minecraft, and I'm a crashaholic.", "Ooh. Shiny.", "This doesn't make any sense!", "Why is it breaking :(", "Don't do that.", "Ouch. That hurt :(", "You're mean.", "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!", "But it works on my machine." };
/*     */ 
/*     */     
/*     */     try {
/* 342 */       return astring[(int)(System.nanoTime() % astring.length)];
/*     */     }
/* 344 */     catch (Throwable var2) {
/*     */       
/* 346 */       return "Witty comment unavailable :(";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static CrashReport makeCrashReport(Throwable causeIn, String descriptionIn) {
/*     */     CrashReport crashreport;
/* 354 */     if (causeIn instanceof ReportedException) {
/*     */       
/* 356 */       crashreport = ((ReportedException)causeIn).getCrashReport();
/*     */     }
/*     */     else {
/*     */       
/* 360 */       crashreport = new CrashReport(descriptionIn, causeIn);
/*     */     } 
/*     */     
/* 363 */     return crashreport;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\crash\CrashReport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */