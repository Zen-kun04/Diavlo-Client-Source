/*     */ package net.minecraft.stats;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonParser;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.server.S37PacketStatistics;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.IJsonSerializable;
/*     */ import net.minecraft.util.TupleIntJsonSerializable;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class StatisticsFile extends StatFileWriter {
/*  28 */   private static final Logger logger = LogManager.getLogger();
/*     */   private final MinecraftServer mcServer;
/*     */   private final File statsFile;
/*  31 */   private final Set<StatBase> field_150888_e = Sets.newHashSet();
/*  32 */   private int field_150885_f = -300;
/*     */   
/*     */   private boolean field_150886_g = false;
/*     */   
/*     */   public StatisticsFile(MinecraftServer serverIn, File statsFileIn) {
/*  37 */     this.mcServer = serverIn;
/*  38 */     this.statsFile = statsFileIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void readStatFile() {
/*  43 */     if (this.statsFile.isFile()) {
/*     */       
/*     */       try {
/*     */         
/*  47 */         this.statsData.clear();
/*  48 */         this.statsData.putAll(parseJson(FileUtils.readFileToString(this.statsFile)));
/*     */       }
/*  50 */       catch (IOException ioexception) {
/*     */         
/*  52 */         logger.error("Couldn't read statistics file " + this.statsFile, ioexception);
/*     */       }
/*  54 */       catch (JsonParseException jsonparseexception) {
/*     */         
/*  56 */         logger.error("Couldn't parse statistics file " + this.statsFile, (Throwable)jsonparseexception);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveStatFile() {
/*     */     try {
/*  65 */       FileUtils.writeStringToFile(this.statsFile, dumpJson(this.statsData));
/*     */     }
/*  67 */     catch (IOException ioexception) {
/*     */       
/*  69 */       logger.error("Couldn't save stats", ioexception);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void unlockAchievement(EntityPlayer playerIn, StatBase statIn, int p_150873_3_) {
/*  75 */     int i = statIn.isAchievement() ? readStat(statIn) : 0;
/*  76 */     super.unlockAchievement(playerIn, statIn, p_150873_3_);
/*  77 */     this.field_150888_e.add(statIn);
/*     */     
/*  79 */     if (statIn.isAchievement() && i == 0 && p_150873_3_ > 0) {
/*     */       
/*  81 */       this.field_150886_g = true;
/*     */       
/*  83 */       if (this.mcServer.isAnnouncingPlayerAchievements())
/*     */       {
/*  85 */         this.mcServer.getConfigurationManager().sendChatMsg((IChatComponent)new ChatComponentTranslation("chat.type.achievement", new Object[] { playerIn.getDisplayName(), statIn.createChatComponent() }));
/*     */       }
/*     */     } 
/*     */     
/*  89 */     if (statIn.isAchievement() && i > 0 && p_150873_3_ == 0) {
/*     */       
/*  91 */       this.field_150886_g = true;
/*     */       
/*  93 */       if (this.mcServer.isAnnouncingPlayerAchievements())
/*     */       {
/*  95 */         this.mcServer.getConfigurationManager().sendChatMsg((IChatComponent)new ChatComponentTranslation("chat.type.achievement.taken", new Object[] { playerIn.getDisplayName(), statIn.createChatComponent() }));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<StatBase> func_150878_c() {
/* 102 */     Set<StatBase> set = Sets.newHashSet(this.field_150888_e);
/* 103 */     this.field_150888_e.clear();
/* 104 */     this.field_150886_g = false;
/* 105 */     return set;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<StatBase, TupleIntJsonSerializable> parseJson(String p_150881_1_) {
/* 110 */     JsonElement jsonelement = (new JsonParser()).parse(p_150881_1_);
/*     */     
/* 112 */     if (!jsonelement.isJsonObject())
/*     */     {
/* 114 */       return Maps.newHashMap();
/*     */     }
/*     */ 
/*     */     
/* 118 */     JsonObject jsonobject = jsonelement.getAsJsonObject();
/* 119 */     Map<StatBase, TupleIntJsonSerializable> map = Maps.newHashMap();
/*     */     
/* 121 */     for (Map.Entry<String, JsonElement> entry : (Iterable<Map.Entry<String, JsonElement>>)jsonobject.entrySet()) {
/*     */       
/* 123 */       StatBase statbase = StatList.getOneShotStat(entry.getKey());
/*     */       
/* 125 */       if (statbase != null) {
/*     */         
/* 127 */         TupleIntJsonSerializable tupleintjsonserializable = new TupleIntJsonSerializable();
/*     */         
/* 129 */         if (((JsonElement)entry.getValue()).isJsonPrimitive() && ((JsonElement)entry.getValue()).getAsJsonPrimitive().isNumber()) {
/*     */           
/* 131 */           tupleintjsonserializable.setIntegerValue(((JsonElement)entry.getValue()).getAsInt());
/*     */         }
/* 133 */         else if (((JsonElement)entry.getValue()).isJsonObject()) {
/*     */           
/* 135 */           JsonObject jsonobject1 = ((JsonElement)entry.getValue()).getAsJsonObject();
/*     */           
/* 137 */           if (jsonobject1.has("value") && jsonobject1.get("value").isJsonPrimitive() && jsonobject1.get("value").getAsJsonPrimitive().isNumber())
/*     */           {
/* 139 */             tupleintjsonserializable.setIntegerValue(jsonobject1.getAsJsonPrimitive("value").getAsInt());
/*     */           }
/*     */           
/* 142 */           if (jsonobject1.has("progress") && statbase.func_150954_l() != null) {
/*     */             
/*     */             try {
/*     */               
/* 146 */               Constructor<? extends IJsonSerializable> constructor = statbase.func_150954_l().getConstructor(new Class[0]);
/* 147 */               IJsonSerializable ijsonserializable = constructor.newInstance(new Object[0]);
/* 148 */               ijsonserializable.fromJson(jsonobject1.get("progress"));
/* 149 */               tupleintjsonserializable.setJsonSerializableValue(ijsonserializable);
/*     */             }
/* 151 */             catch (Throwable throwable) {
/*     */               
/* 153 */               logger.warn("Invalid statistic progress in " + this.statsFile, throwable);
/*     */             } 
/*     */           }
/*     */         } 
/*     */         
/* 158 */         map.put(statbase, tupleintjsonserializable);
/*     */         
/*     */         continue;
/*     */       } 
/* 162 */       logger.warn("Invalid statistic in " + this.statsFile + ": Don't know what " + (String)entry.getKey() + " is");
/*     */     } 
/*     */ 
/*     */     
/* 166 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String dumpJson(Map<StatBase, TupleIntJsonSerializable> p_150880_0_) {
/* 172 */     JsonObject jsonobject = new JsonObject();
/*     */     
/* 174 */     for (Map.Entry<StatBase, TupleIntJsonSerializable> entry : p_150880_0_.entrySet()) {
/*     */       
/* 176 */       if (((TupleIntJsonSerializable)entry.getValue()).getJsonSerializableValue() != null) {
/*     */         
/* 178 */         JsonObject jsonobject1 = new JsonObject();
/* 179 */         jsonobject1.addProperty("value", Integer.valueOf(((TupleIntJsonSerializable)entry.getValue()).getIntegerValue()));
/*     */ 
/*     */         
/*     */         try {
/* 183 */           jsonobject1.add("progress", ((TupleIntJsonSerializable)entry.getValue()).getJsonSerializableValue().getSerializableElement());
/*     */         }
/* 185 */         catch (Throwable throwable) {
/*     */           
/* 187 */           logger.warn("Couldn't save statistic " + ((StatBase)entry.getKey()).getStatName() + ": error serializing progress", throwable);
/*     */         } 
/*     */         
/* 190 */         jsonobject.add(((StatBase)entry.getKey()).statId, (JsonElement)jsonobject1);
/*     */         
/*     */         continue;
/*     */       } 
/* 194 */       jsonobject.addProperty(((StatBase)entry.getKey()).statId, Integer.valueOf(((TupleIntJsonSerializable)entry.getValue()).getIntegerValue()));
/*     */     } 
/*     */ 
/*     */     
/* 198 */     return jsonobject.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_150877_d() {
/* 203 */     for (StatBase statbase : this.statsData.keySet())
/*     */     {
/* 205 */       this.field_150888_e.add(statbase);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_150876_a(EntityPlayerMP p_150876_1_) {
/* 211 */     int i = this.mcServer.getTickCounter();
/* 212 */     Map<StatBase, Integer> map = Maps.newHashMap();
/*     */     
/* 214 */     if (this.field_150886_g || i - this.field_150885_f > 300) {
/*     */       
/* 216 */       this.field_150885_f = i;
/*     */       
/* 218 */       for (StatBase statbase : func_150878_c())
/*     */       {
/* 220 */         map.put(statbase, Integer.valueOf(readStat(statbase)));
/*     */       }
/*     */     } 
/*     */     
/* 224 */     p_150876_1_.playerNetServerHandler.sendPacket((Packet)new S37PacketStatistics(map));
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendAchievements(EntityPlayerMP player) {
/* 229 */     Map<StatBase, Integer> map = Maps.newHashMap();
/*     */     
/* 231 */     for (Achievement achievement : AchievementList.achievementList) {
/*     */       
/* 233 */       if (hasAchievementUnlocked(achievement)) {
/*     */         
/* 235 */         map.put(achievement, Integer.valueOf(readStat(achievement)));
/* 236 */         this.field_150888_e.remove(achievement);
/*     */       } 
/*     */     } 
/*     */     
/* 240 */     player.playerNetServerHandler.sendPacket((Packet)new S37PacketStatistics(map));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean func_150879_e() {
/* 245 */     return this.field_150886_g;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\stats\StatisticsFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */