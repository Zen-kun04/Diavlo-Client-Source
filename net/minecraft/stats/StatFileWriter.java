/*    */ package net.minecraft.stats;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.IJsonSerializable;
/*    */ import net.minecraft.util.TupleIntJsonSerializable;
/*    */ 
/*    */ public class StatFileWriter
/*    */ {
/* 11 */   protected final Map<StatBase, TupleIntJsonSerializable> statsData = Maps.newConcurrentMap();
/*    */ 
/*    */   
/*    */   public boolean hasAchievementUnlocked(Achievement achievementIn) {
/* 15 */     return (readStat(achievementIn) > 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUnlockAchievement(Achievement achievementIn) {
/* 20 */     return (achievementIn.parentAchievement == null || hasAchievementUnlocked(achievementIn.parentAchievement));
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_150874_c(Achievement p_150874_1_) {
/* 25 */     if (hasAchievementUnlocked(p_150874_1_))
/*    */     {
/* 27 */       return 0;
/*    */     }
/*    */ 
/*    */     
/* 31 */     int i = 0;
/*    */     
/* 33 */     for (Achievement achievement = p_150874_1_.parentAchievement; achievement != null && !hasAchievementUnlocked(achievement); i++)
/*    */     {
/* 35 */       achievement = achievement.parentAchievement;
/*    */     }
/*    */     
/* 38 */     return i;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void increaseStat(EntityPlayer player, StatBase stat, int amount) {
/* 44 */     if (!stat.isAchievement() || canUnlockAchievement((Achievement)stat))
/*    */     {
/* 46 */       unlockAchievement(player, stat, readStat(stat) + amount);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void unlockAchievement(EntityPlayer playerIn, StatBase statIn, int p_150873_3_) {
/* 52 */     TupleIntJsonSerializable tupleintjsonserializable = this.statsData.get(statIn);
/*    */     
/* 54 */     if (tupleintjsonserializable == null) {
/*    */       
/* 56 */       tupleintjsonserializable = new TupleIntJsonSerializable();
/* 57 */       this.statsData.put(statIn, tupleintjsonserializable);
/*    */     } 
/*    */     
/* 60 */     tupleintjsonserializable.setIntegerValue(p_150873_3_);
/*    */   }
/*    */ 
/*    */   
/*    */   public int readStat(StatBase stat) {
/* 65 */     TupleIntJsonSerializable tupleintjsonserializable = this.statsData.get(stat);
/* 66 */     return (tupleintjsonserializable == null) ? 0 : tupleintjsonserializable.getIntegerValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends IJsonSerializable> T func_150870_b(StatBase p_150870_1_) {
/* 71 */     TupleIntJsonSerializable tupleintjsonserializable = this.statsData.get(p_150870_1_);
/* 72 */     return (tupleintjsonserializable != null) ? (T)tupleintjsonserializable.getJsonSerializableValue() : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends IJsonSerializable> T func_150872_a(StatBase p_150872_1_, T p_150872_2_) {
/* 77 */     TupleIntJsonSerializable tupleintjsonserializable = this.statsData.get(p_150872_1_);
/*    */     
/* 79 */     if (tupleintjsonserializable == null) {
/*    */       
/* 81 */       tupleintjsonserializable = new TupleIntJsonSerializable();
/* 82 */       this.statsData.put(p_150872_1_, tupleintjsonserializable);
/*    */     } 
/*    */     
/* 85 */     tupleintjsonserializable.setJsonSerializableValue((IJsonSerializable)p_150872_2_);
/* 86 */     return p_150872_2_;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\stats\StatFileWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */