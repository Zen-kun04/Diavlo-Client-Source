/*     */ package net.minecraft.profiler;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.RuntimeMXBean;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.util.HttpUtil;
/*     */ 
/*     */ 
/*     */ public class PlayerUsageSnooper
/*     */ {
/*  18 */   private final Map<String, Object> snooperStats = Maps.newHashMap();
/*  19 */   private final Map<String, Object> clientStats = Maps.newHashMap();
/*  20 */   private final String uniqueID = UUID.randomUUID().toString();
/*     */   private final URL serverUrl;
/*     */   private final IPlayerUsage playerStatsCollector;
/*  23 */   private final Timer threadTrigger = new Timer("Snooper Timer", true);
/*  24 */   private final Object syncLock = new Object();
/*     */   
/*     */   private final long minecraftStartTimeMilis;
/*     */   
/*     */   private boolean isRunning;
/*     */   private int selfCounter;
/*     */   
/*     */   public PlayerUsageSnooper(String side, IPlayerUsage playerStatCollector, long startTime) {
/*     */     try {
/*  33 */       this.serverUrl = new URL("http://snoop.minecraft.net/" + side + "?version=" + '\002');
/*     */     }
/*  35 */     catch (MalformedURLException var6) {
/*     */       
/*  37 */       throw new IllegalArgumentException();
/*     */     } 
/*     */     
/*  40 */     this.playerStatsCollector = playerStatCollector;
/*  41 */     this.minecraftStartTimeMilis = startTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startSnooper() {
/*  46 */     if (!this.isRunning) {
/*     */       
/*  48 */       this.isRunning = true;
/*  49 */       addOSData();
/*  50 */       this.threadTrigger.schedule(new TimerTask()
/*     */           {
/*     */             public void run()
/*     */             {
/*  54 */               if (PlayerUsageSnooper.this.playerStatsCollector.isSnooperEnabled()) {
/*     */                 Map<String, Object> map;
/*     */ 
/*     */                 
/*  58 */                 synchronized (PlayerUsageSnooper.this.syncLock) {
/*     */                   
/*  60 */                   map = Maps.newHashMap(PlayerUsageSnooper.this.clientStats);
/*     */                   
/*  62 */                   if (PlayerUsageSnooper.this.selfCounter == 0)
/*     */                   {
/*  64 */                     map.putAll(PlayerUsageSnooper.this.snooperStats);
/*     */                   }
/*     */                   
/*  67 */                   map.put("snooper_count", Integer.valueOf(PlayerUsageSnooper.this.selfCounter++));
/*  68 */                   map.put("snooper_token", PlayerUsageSnooper.this.uniqueID);
/*     */                 } 
/*     */                 
/*  71 */                 HttpUtil.postMap(PlayerUsageSnooper.this.serverUrl, map, true);
/*     */               } 
/*     */             }
/*     */           }0L, 900000L);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void addOSData() {
/*  80 */     addJvmArgsToSnooper();
/*  81 */     addClientStat("snooper_token", this.uniqueID);
/*  82 */     addStatToSnooper("snooper_token", this.uniqueID);
/*  83 */     addStatToSnooper("os_name", System.getProperty("os.name"));
/*  84 */     addStatToSnooper("os_version", System.getProperty("os.version"));
/*  85 */     addStatToSnooper("os_architecture", System.getProperty("os.arch"));
/*  86 */     addStatToSnooper("java_version", System.getProperty("java.version"));
/*  87 */     addClientStat("version", "1.8.9");
/*  88 */     this.playerStatsCollector.addServerTypeToSnooper(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private void addJvmArgsToSnooper() {
/*  93 */     RuntimeMXBean runtimemxbean = ManagementFactory.getRuntimeMXBean();
/*  94 */     List<String> list = runtimemxbean.getInputArguments();
/*  95 */     int i = 0;
/*     */     
/*  97 */     for (String s : list) {
/*     */       
/*  99 */       if (s.startsWith("-X"))
/*     */       {
/* 101 */         addClientStat("jvm_arg[" + i++ + "]", s);
/*     */       }
/*     */     } 
/*     */     
/* 105 */     addClientStat("jvm_args", Integer.valueOf(i));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addMemoryStatsToSnooper() {
/* 110 */     addStatToSnooper("memory_total", Long.valueOf(Runtime.getRuntime().totalMemory()));
/* 111 */     addStatToSnooper("memory_max", Long.valueOf(Runtime.getRuntime().maxMemory()));
/* 112 */     addStatToSnooper("memory_free", Long.valueOf(Runtime.getRuntime().freeMemory()));
/* 113 */     addStatToSnooper("cpu_cores", Integer.valueOf(Runtime.getRuntime().availableProcessors()));
/* 114 */     this.playerStatsCollector.addServerStatsToSnooper(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addClientStat(String statName, Object statValue) {
/* 119 */     synchronized (this.syncLock) {
/*     */       
/* 121 */       this.clientStats.put(statName, statValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addStatToSnooper(String statName, Object statValue) {
/* 127 */     synchronized (this.syncLock) {
/*     */       
/* 129 */       this.snooperStats.put(statName, statValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getCurrentStats() {
/* 135 */     Map<String, String> map = Maps.newLinkedHashMap();
/*     */     
/* 137 */     synchronized (this.syncLock) {
/*     */       
/* 139 */       addMemoryStatsToSnooper();
/*     */       
/* 141 */       for (Map.Entry<String, Object> entry : this.snooperStats.entrySet())
/*     */       {
/* 143 */         map.put(entry.getKey(), entry.getValue().toString());
/*     */       }
/*     */       
/* 146 */       for (Map.Entry<String, Object> entry1 : this.clientStats.entrySet())
/*     */       {
/* 148 */         map.put(entry1.getKey(), entry1.getValue().toString());
/*     */       }
/*     */       
/* 151 */       return map;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSnooperRunning() {
/* 157 */     return this.isRunning;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stopSnooper() {
/* 162 */     this.threadTrigger.cancel();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUniqueID() {
/* 167 */     return this.uniqueID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getMinecraftStartTimeMillis() {
/* 172 */     return this.minecraftStartTimeMilis;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\profiler\PlayerUsageSnooper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */