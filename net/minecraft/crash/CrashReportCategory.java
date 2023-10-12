/*     */ package net.minecraft.crash;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.util.BlockPos;
/*     */ 
/*     */ public class CrashReportCategory
/*     */ {
/*     */   private final CrashReport crashReport;
/*     */   private final String name;
/*  14 */   private final List<Entry> children = Lists.newArrayList();
/*  15 */   private StackTraceElement[] stackTrace = new StackTraceElement[0];
/*     */ 
/*     */   
/*     */   public CrashReportCategory(CrashReport report, String name) {
/*  19 */     this.crashReport = report;
/*  20 */     this.name = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getCoordinateInfo(double x, double y, double z) {
/*  25 */     return String.format("%.2f,%.2f,%.2f - %s", new Object[] { Double.valueOf(x), Double.valueOf(y), Double.valueOf(z), getCoordinateInfo(new BlockPos(x, y, z)) });
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getCoordinateInfo(BlockPos pos) {
/*  30 */     int i = pos.getX();
/*  31 */     int j = pos.getY();
/*  32 */     int k = pos.getZ();
/*  33 */     StringBuilder stringbuilder = new StringBuilder();
/*     */ 
/*     */     
/*     */     try {
/*  37 */       stringbuilder.append(String.format("World: (%d,%d,%d)", new Object[] { Integer.valueOf(i), Integer.valueOf(j), Integer.valueOf(k) }));
/*     */     }
/*  39 */     catch (Throwable var17) {
/*     */       
/*  41 */       stringbuilder.append("(Error finding world loc)");
/*     */     } 
/*     */     
/*  44 */     stringbuilder.append(", ");
/*     */ 
/*     */     
/*     */     try {
/*  48 */       int l = i >> 4;
/*  49 */       int i1 = k >> 4;
/*  50 */       int j1 = i & 0xF;
/*  51 */       int k1 = j >> 4;
/*  52 */       int l1 = k & 0xF;
/*  53 */       int i2 = l << 4;
/*  54 */       int j2 = i1 << 4;
/*  55 */       int k2 = (l + 1 << 4) - 1;
/*  56 */       int l2 = (i1 + 1 << 4) - 1;
/*  57 */       stringbuilder.append(String.format("Chunk: (at %d,%d,%d in %d,%d; contains blocks %d,0,%d to %d,255,%d)", new Object[] { Integer.valueOf(j1), Integer.valueOf(k1), Integer.valueOf(l1), Integer.valueOf(l), Integer.valueOf(i1), Integer.valueOf(i2), Integer.valueOf(j2), Integer.valueOf(k2), Integer.valueOf(l2) }));
/*     */     }
/*  59 */     catch (Throwable var16) {
/*     */       
/*  61 */       stringbuilder.append("(Error finding chunk loc)");
/*     */     } 
/*     */     
/*  64 */     stringbuilder.append(", ");
/*     */ 
/*     */     
/*     */     try {
/*  68 */       int j3 = i >> 9;
/*  69 */       int k3 = k >> 9;
/*  70 */       int l3 = j3 << 5;
/*  71 */       int i4 = k3 << 5;
/*  72 */       int j4 = (j3 + 1 << 5) - 1;
/*  73 */       int k4 = (k3 + 1 << 5) - 1;
/*  74 */       int l4 = j3 << 9;
/*  75 */       int i5 = k3 << 9;
/*  76 */       int j5 = (j3 + 1 << 9) - 1;
/*  77 */       int i3 = (k3 + 1 << 9) - 1;
/*  78 */       stringbuilder.append(String.format("Region: (%d,%d; contains chunks %d,%d to %d,%d, blocks %d,0,%d to %d,255,%d)", new Object[] { Integer.valueOf(j3), Integer.valueOf(k3), Integer.valueOf(l3), Integer.valueOf(i4), Integer.valueOf(j4), Integer.valueOf(k4), Integer.valueOf(l4), Integer.valueOf(i5), Integer.valueOf(j5), Integer.valueOf(i3) }));
/*     */     }
/*  80 */     catch (Throwable var15) {
/*     */       
/*  82 */       stringbuilder.append("(Error finding world loc)");
/*     */     } 
/*     */     
/*  85 */     return stringbuilder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCrashSectionCallable(String sectionName, Callable<String> callable) {
/*     */     try {
/*  92 */       addCrashSection(sectionName, callable.call());
/*     */     }
/*  94 */     catch (Throwable throwable) {
/*     */       
/*  96 */       addCrashSectionThrowable(sectionName, throwable);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCrashSection(String sectionName, Object value) {
/* 102 */     this.children.add(new Entry(sectionName, value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCrashSectionThrowable(String sectionName, Throwable throwable) {
/* 107 */     addCrashSection(sectionName, throwable);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPrunedStackTrace(int size) {
/* 112 */     StackTraceElement[] astacktraceelement = Thread.currentThread().getStackTrace();
/*     */     
/* 114 */     if (astacktraceelement.length <= 0)
/*     */     {
/* 116 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 120 */     this.stackTrace = new StackTraceElement[astacktraceelement.length - 3 - size];
/* 121 */     System.arraycopy(astacktraceelement, 3 + size, this.stackTrace, 0, this.stackTrace.length);
/* 122 */     return this.stackTrace.length;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean firstTwoElementsOfStackTraceMatch(StackTraceElement s1, StackTraceElement s2) {
/* 128 */     if (this.stackTrace.length != 0 && s1 != null) {
/*     */       
/* 130 */       StackTraceElement stacktraceelement = this.stackTrace[0];
/*     */       
/* 132 */       if (stacktraceelement.isNativeMethod() == s1.isNativeMethod() && stacktraceelement.getClassName().equals(s1.getClassName()) && stacktraceelement.getFileName().equals(s1.getFileName()) && stacktraceelement.getMethodName().equals(s1.getMethodName())) {
/*     */         
/* 134 */         if (((s2 != null) ? true : false) != ((this.stackTrace.length > 1) ? true : false))
/*     */         {
/* 136 */           return false;
/*     */         }
/* 138 */         if (s2 != null && !this.stackTrace[1].equals(s2))
/*     */         {
/* 140 */           return false;
/*     */         }
/*     */ 
/*     */         
/* 144 */         this.stackTrace[0] = s1;
/* 145 */         return true;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 150 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 155 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void trimStackTraceEntriesFromBottom(int amount) {
/* 161 */     StackTraceElement[] astacktraceelement = new StackTraceElement[this.stackTrace.length - amount];
/* 162 */     System.arraycopy(this.stackTrace, 0, astacktraceelement, 0, astacktraceelement.length);
/* 163 */     this.stackTrace = astacktraceelement;
/*     */   }
/*     */ 
/*     */   
/*     */   public void appendToStringBuilder(StringBuilder builder) {
/* 168 */     builder.append("-- ").append(this.name).append(" --\n");
/* 169 */     builder.append("Details:");
/*     */     
/* 171 */     for (Entry crashreportcategory$entry : this.children) {
/*     */       
/* 173 */       builder.append("\n\t");
/* 174 */       builder.append(crashreportcategory$entry.getKey());
/* 175 */       builder.append(": ");
/* 176 */       builder.append(crashreportcategory$entry.getValue());
/*     */     } 
/*     */     
/* 179 */     if (this.stackTrace != null && this.stackTrace.length > 0) {
/*     */       
/* 181 */       builder.append("\nStacktrace:");
/*     */       
/* 183 */       for (StackTraceElement stacktraceelement : this.stackTrace) {
/*     */         
/* 185 */         builder.append("\n\tat ");
/* 186 */         builder.append(stacktraceelement.toString());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public StackTraceElement[] getStackTrace() {
/* 193 */     return this.stackTrace;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addBlockInfo(CrashReportCategory category, final BlockPos pos, final Block blockIn, final int blockData) {
/* 198 */     final int i = Block.getIdFromBlock(blockIn);
/* 199 */     category.addCrashSectionCallable("Block type", new Callable<String>()
/*     */         {
/*     */           
/*     */           public String call() throws Exception
/*     */           {
/*     */             try {
/* 205 */               return String.format("ID #%d (%s // %s)", new Object[] { Integer.valueOf(this.val$i), this.val$blockIn.getUnlocalizedName(), this.val$blockIn.getClass().getCanonicalName() });
/*     */             }
/* 207 */             catch (Throwable var2) {
/*     */               
/* 209 */               return "ID #" + i;
/*     */             } 
/*     */           }
/*     */         });
/* 213 */     category.addCrashSectionCallable("Block data value", new Callable<String>()
/*     */         {
/*     */           public String call() throws Exception
/*     */           {
/* 217 */             if (blockData < 0)
/*     */             {
/* 219 */               return "Unknown? (Got " + blockData + ")";
/*     */             }
/*     */ 
/*     */             
/* 223 */             String s = String.format("%4s", new Object[] { Integer.toBinaryString(this.val$blockData) }).replace(" ", "0");
/* 224 */             return String.format("%1$d / 0x%1$X / 0b%2$s", new Object[] { Integer.valueOf(this.val$blockData), s });
/*     */           }
/*     */         });
/*     */     
/* 228 */     category.addCrashSectionCallable("Block location", new Callable<String>()
/*     */         {
/*     */           public String call() throws Exception
/*     */           {
/* 232 */             return CrashReportCategory.getCoordinateInfo(pos);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addBlockInfo(CrashReportCategory category, final BlockPos pos, final IBlockState state) {
/* 239 */     category.addCrashSectionCallable("Block", new Callable<String>()
/*     */         {
/*     */           public String call() throws Exception
/*     */           {
/* 243 */             return state.toString();
/*     */           }
/*     */         });
/* 246 */     category.addCrashSectionCallable("Block location", new Callable<String>()
/*     */         {
/*     */           public String call() throws Exception
/*     */           {
/* 250 */             return CrashReportCategory.getCoordinateInfo(pos);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   static class Entry
/*     */   {
/*     */     private final String key;
/*     */     private final String value;
/*     */     
/*     */     public Entry(String key, Object value) {
/* 262 */       this.key = key;
/*     */       
/* 264 */       if (value == null) {
/*     */         
/* 266 */         this.value = "~~NULL~~";
/*     */       }
/* 268 */       else if (value instanceof Throwable) {
/*     */         
/* 270 */         Throwable throwable = (Throwable)value;
/* 271 */         this.value = "~~ERROR~~ " + throwable.getClass().getSimpleName() + ": " + throwable.getMessage();
/*     */       }
/*     */       else {
/*     */         
/* 275 */         this.value = value.toString();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String getKey() {
/* 281 */       return this.key;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getValue() {
/* 286 */       return this.value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\crash\CrashReportCategory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */