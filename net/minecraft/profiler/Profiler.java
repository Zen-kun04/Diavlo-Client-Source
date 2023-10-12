/*     */ package net.minecraft.profiler;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.src.Config;
/*     */ import net.optifine.Lagometer;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class Profiler
/*     */ {
/*  16 */   private static final Logger logger = LogManager.getLogger();
/*  17 */   private final List<String> sectionList = Lists.newArrayList();
/*  18 */   private final List<Long> timestampList = Lists.newArrayList();
/*     */   public boolean profilingEnabled;
/*  20 */   private String profilingSection = "";
/*  21 */   private final Map<String, Long> profilingMap = Maps.newHashMap();
/*     */   public boolean profilerGlobalEnabled = true;
/*     */   private boolean profilerLocalEnabled;
/*     */   private static final String SCHEDULED_EXECUTABLES = "scheduledExecutables";
/*     */   private static final String TICK = "tick";
/*     */   private static final String PRE_RENDER_ERRORS = "preRenderErrors";
/*     */   private static final String RENDER = "render";
/*     */   private static final String DISPLAY = "display";
/*  29 */   private static final int HASH_SCHEDULED_EXECUTABLES = "scheduledExecutables".hashCode();
/*  30 */   private static final int HASH_TICK = "tick".hashCode();
/*  31 */   private static final int HASH_PRE_RENDER_ERRORS = "preRenderErrors".hashCode();
/*  32 */   private static final int HASH_RENDER = "render".hashCode();
/*  33 */   private static final int HASH_DISPLAY = "display".hashCode();
/*     */ 
/*     */   
/*     */   public Profiler() {
/*  37 */     this.profilerLocalEnabled = this.profilerGlobalEnabled;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearProfiling() {
/*  42 */     this.profilingMap.clear();
/*  43 */     this.profilingSection = "";
/*  44 */     this.sectionList.clear();
/*  45 */     this.profilerLocalEnabled = this.profilerGlobalEnabled;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startSection(String name) {
/*  50 */     if (Lagometer.isActive()) {
/*     */       
/*  52 */       int i = name.hashCode();
/*     */       
/*  54 */       if (i == HASH_SCHEDULED_EXECUTABLES && name.equals("scheduledExecutables")) {
/*     */         
/*  56 */         Lagometer.timerScheduledExecutables.start();
/*     */       }
/*  58 */       else if (i == HASH_TICK && name.equals("tick") && Config.isMinecraftThread()) {
/*     */         
/*  60 */         Lagometer.timerScheduledExecutables.end();
/*  61 */         Lagometer.timerTick.start();
/*     */       }
/*  63 */       else if (i == HASH_PRE_RENDER_ERRORS && name.equals("preRenderErrors")) {
/*     */         
/*  65 */         Lagometer.timerTick.end();
/*     */       } 
/*     */     } 
/*     */     
/*  69 */     if (Config.isFastRender()) {
/*     */       
/*  71 */       int j = name.hashCode();
/*     */       
/*  73 */       if (j == HASH_RENDER && name.equals("render")) {
/*     */         
/*  75 */         GlStateManager.clearEnabled = false;
/*     */       }
/*  77 */       else if (j == HASH_DISPLAY && name.equals("display")) {
/*     */         
/*  79 */         GlStateManager.clearEnabled = true;
/*     */       } 
/*     */     } 
/*     */     
/*  83 */     if (this.profilerLocalEnabled)
/*     */     {
/*  85 */       if (this.profilingEnabled) {
/*     */         
/*  87 */         if (this.profilingSection.length() > 0)
/*     */         {
/*  89 */           this.profilingSection += ".";
/*     */         }
/*     */         
/*  92 */         this.profilingSection += name;
/*  93 */         this.sectionList.add(this.profilingSection);
/*  94 */         this.timestampList.add(Long.valueOf(System.nanoTime()));
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void endSection() {
/* 101 */     if (this.profilerLocalEnabled)
/*     */     {
/* 103 */       if (this.profilingEnabled) {
/*     */         
/* 105 */         long i = System.nanoTime();
/* 106 */         long j = ((Long)this.timestampList.remove(this.timestampList.size() - 1)).longValue();
/* 107 */         this.sectionList.remove(this.sectionList.size() - 1);
/* 108 */         long k = i - j;
/*     */         
/* 110 */         if (this.profilingMap.containsKey(this.profilingSection)) {
/*     */           
/* 112 */           this.profilingMap.put(this.profilingSection, Long.valueOf(((Long)this.profilingMap.get(this.profilingSection)).longValue() + k));
/*     */         }
/*     */         else {
/*     */           
/* 116 */           this.profilingMap.put(this.profilingSection, Long.valueOf(k));
/*     */         } 
/*     */         
/* 119 */         if (k > 100000000L)
/*     */         {
/* 121 */           logger.warn("Something's taking too long! '" + this.profilingSection + "' took aprox " + (k / 1000000.0D) + " ms");
/*     */         }
/*     */         
/* 124 */         this.profilingSection = !this.sectionList.isEmpty() ? this.sectionList.get(this.sectionList.size() - 1) : "";
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Result> getProfilingData(String profilerName) {
/* 131 */     if (!this.profilingEnabled)
/*     */     {
/* 133 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 137 */     long i = this.profilingMap.containsKey("root") ? ((Long)this.profilingMap.get("root")).longValue() : 0L;
/* 138 */     long j = this.profilingMap.containsKey(profilerName) ? ((Long)this.profilingMap.get(profilerName)).longValue() : -1L;
/* 139 */     List<Result> list = Lists.newArrayList();
/*     */     
/* 141 */     if (profilerName.length() > 0)
/*     */     {
/* 143 */       profilerName = profilerName + ".";
/*     */     }
/*     */     
/* 146 */     long k = 0L;
/*     */     
/* 148 */     for (String s : this.profilingMap.keySet()) {
/*     */       
/* 150 */       if (s.length() > profilerName.length() && s.startsWith(profilerName) && s.indexOf(".", profilerName.length() + 1) < 0)
/*     */       {
/* 152 */         k += ((Long)this.profilingMap.get(s)).longValue();
/*     */       }
/*     */     } 
/*     */     
/* 156 */     float f = (float)k;
/*     */     
/* 158 */     if (k < j)
/*     */     {
/* 160 */       k = j;
/*     */     }
/*     */     
/* 163 */     if (i < k)
/*     */     {
/* 165 */       i = k;
/*     */     }
/*     */     
/* 168 */     for (String s1 : this.profilingMap.keySet()) {
/*     */       
/* 170 */       if (s1.length() > profilerName.length() && s1.startsWith(profilerName) && s1.indexOf(".", profilerName.length() + 1) < 0) {
/*     */         
/* 172 */         long l = ((Long)this.profilingMap.get(s1)).longValue();
/* 173 */         double d0 = l * 100.0D / k;
/* 174 */         double d1 = l * 100.0D / i;
/* 175 */         String s2 = s1.substring(profilerName.length());
/* 176 */         list.add(new Result(s2, d0, d1));
/*     */       } 
/*     */     } 
/*     */     
/* 180 */     for (String s3 : this.profilingMap.keySet())
/*     */     {
/* 182 */       this.profilingMap.put(s3, Long.valueOf(((Long)this.profilingMap.get(s3)).longValue() * 950L / 1000L));
/*     */     }
/*     */     
/* 185 */     if ((float)k > f)
/*     */     {
/* 187 */       list.add(new Result("unspecified", ((float)k - f) * 100.0D / k, ((float)k - f) * 100.0D / i));
/*     */     }
/*     */     
/* 190 */     Collections.sort(list);
/* 191 */     list.add(0, new Result(profilerName, 100.0D, k * 100.0D / i));
/* 192 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void endStartSection(String name) {
/* 198 */     if (this.profilerLocalEnabled) {
/*     */       
/* 200 */       endSection();
/* 201 */       startSection(name);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNameOfLastSection() {
/* 207 */     return (this.sectionList.size() == 0) ? "[UNKNOWN]" : this.sectionList.get(this.sectionList.size() - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startSection(Class<?> p_startSection_1_) {
/* 212 */     if (this.profilingEnabled)
/*     */     {
/* 214 */       startSection(p_startSection_1_.getSimpleName());
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Result
/*     */     implements Comparable<Result>
/*     */   {
/*     */     public double field_76332_a;
/*     */     public double field_76330_b;
/*     */     public String field_76331_c;
/*     */     
/*     */     public Result(String profilerName, double usePercentage, double totalUsePercentage) {
/* 226 */       this.field_76331_c = profilerName;
/* 227 */       this.field_76332_a = usePercentage;
/* 228 */       this.field_76330_b = totalUsePercentage;
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(Result p_compareTo_1_) {
/* 233 */       return (p_compareTo_1_.field_76332_a < this.field_76332_a) ? -1 : ((p_compareTo_1_.field_76332_a > this.field_76332_a) ? 1 : p_compareTo_1_.field_76331_c.compareTo(this.field_76331_c));
/*     */     }
/*     */ 
/*     */     
/*     */     public int getColor() {
/* 238 */       return (this.field_76331_c.hashCode() & 0xAAAAAA) + 4473924;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\profiler\Profiler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */