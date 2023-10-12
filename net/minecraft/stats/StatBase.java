/*     */ package net.minecraft.stats;
/*     */ 
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.NumberFormat;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.event.HoverEvent;
/*     */ import net.minecraft.scoreboard.IScoreObjectiveCriteria;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.IJsonSerializable;
/*     */ 
/*     */ public class StatBase
/*     */ {
/*     */   public final String statId;
/*     */   private final IChatComponent statName;
/*     */   public boolean isIndependent;
/*     */   private final IStatType type;
/*     */   private final IScoreObjectiveCriteria objectiveCriteria;
/*     */   private Class<? extends IJsonSerializable> field_150956_d;
/*  21 */   private static NumberFormat numberFormat = NumberFormat.getIntegerInstance(Locale.US);
/*  22 */   public static IStatType simpleStatType = new IStatType()
/*     */     {
/*     */       public String format(int number)
/*     */       {
/*  26 */         return StatBase.numberFormat.format(number);
/*     */       }
/*     */     };
/*  29 */   private static DecimalFormat decimalFormat = new DecimalFormat("########0.00");
/*  30 */   public static IStatType timeStatType = new IStatType()
/*     */     {
/*     */       public String format(int number)
/*     */       {
/*  34 */         double d0 = number / 20.0D;
/*  35 */         double d1 = d0 / 60.0D;
/*  36 */         double d2 = d1 / 60.0D;
/*  37 */         double d3 = d2 / 24.0D;
/*  38 */         double d4 = d3 / 365.0D;
/*  39 */         return (d4 > 0.5D) ? (StatBase.decimalFormat.format(d4) + " y") : ((d3 > 0.5D) ? (StatBase.decimalFormat.format(d3) + " d") : ((d2 > 0.5D) ? (StatBase.decimalFormat.format(d2) + " h") : ((d1 > 0.5D) ? (StatBase.decimalFormat.format(d1) + " m") : (d0 + " s"))));
/*     */       }
/*     */     };
/*  42 */   public static IStatType distanceStatType = new IStatType()
/*     */     {
/*     */       public String format(int number)
/*     */       {
/*  46 */         double d0 = number / 100.0D;
/*  47 */         double d1 = d0 / 1000.0D;
/*  48 */         return (d1 > 0.5D) ? (StatBase.decimalFormat.format(d1) + " km") : ((d0 > 0.5D) ? (StatBase.decimalFormat.format(d0) + " m") : (number + " cm"));
/*     */       }
/*     */     };
/*  51 */   public static IStatType field_111202_k = new IStatType()
/*     */     {
/*     */       public String format(int number)
/*     */       {
/*  55 */         return StatBase.decimalFormat.format(number * 0.1D);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   public StatBase(String statIdIn, IChatComponent statNameIn, IStatType typeIn) {
/*  61 */     this.statId = statIdIn;
/*  62 */     this.statName = statNameIn;
/*  63 */     this.type = typeIn;
/*  64 */     this.objectiveCriteria = (IScoreObjectiveCriteria)new ObjectiveStat(this);
/*  65 */     IScoreObjectiveCriteria.INSTANCES.put(this.objectiveCriteria.getName(), this.objectiveCriteria);
/*     */   }
/*     */ 
/*     */   
/*     */   public StatBase(String statIdIn, IChatComponent statNameIn) {
/*  70 */     this(statIdIn, statNameIn, simpleStatType);
/*     */   }
/*     */ 
/*     */   
/*     */   public StatBase initIndependentStat() {
/*  75 */     this.isIndependent = true;
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public StatBase registerStat() {
/*  81 */     if (StatList.oneShotStats.containsKey(this.statId))
/*     */     {
/*  83 */       throw new RuntimeException("Duplicate stat id: \"" + ((StatBase)StatList.oneShotStats.get(this.statId)).statName + "\" and \"" + this.statName + "\" at id " + this.statId);
/*     */     }
/*     */ 
/*     */     
/*  87 */     StatList.allStats.add(this);
/*  88 */     StatList.oneShotStats.put(this.statId, this);
/*  89 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAchievement() {
/*  95 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String format(int p_75968_1_) {
/* 100 */     return this.type.format(p_75968_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public IChatComponent getStatName() {
/* 105 */     IChatComponent ichatcomponent = this.statName.createCopy();
/* 106 */     ichatcomponent.getChatStyle().setColor(EnumChatFormatting.GRAY);
/* 107 */     ichatcomponent.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ACHIEVEMENT, (IChatComponent)new ChatComponentText(this.statId)));
/* 108 */     return ichatcomponent;
/*     */   }
/*     */ 
/*     */   
/*     */   public IChatComponent createChatComponent() {
/* 113 */     IChatComponent ichatcomponent = getStatName();
/* 114 */     IChatComponent ichatcomponent1 = (new ChatComponentText("[")).appendSibling(ichatcomponent).appendText("]");
/* 115 */     ichatcomponent1.setChatStyle(ichatcomponent.getChatStyle());
/* 116 */     return ichatcomponent1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object p_equals_1_) {
/* 121 */     if (this == p_equals_1_)
/*     */     {
/* 123 */       return true;
/*     */     }
/* 125 */     if (p_equals_1_ != null && getClass() == p_equals_1_.getClass()) {
/*     */       
/* 127 */       StatBase statbase = (StatBase)p_equals_1_;
/* 128 */       return this.statId.equals(statbase.statId);
/*     */     } 
/*     */ 
/*     */     
/* 132 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 138 */     return this.statId.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 143 */     return "Stat{id=" + this.statId + ", nameId=" + this.statName + ", awardLocallyOnly=" + this.isIndependent + ", formatter=" + this.type + ", objectiveCriteria=" + this.objectiveCriteria + '}';
/*     */   }
/*     */ 
/*     */   
/*     */   public IScoreObjectiveCriteria getCriteria() {
/* 148 */     return this.objectiveCriteria;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<? extends IJsonSerializable> func_150954_l() {
/* 153 */     return this.field_150956_d;
/*     */   }
/*     */ 
/*     */   
/*     */   public StatBase func_150953_b(Class<? extends IJsonSerializable> p_150953_1_) {
/* 158 */     this.field_150956_d = p_150953_1_;
/* 159 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\stats\StatBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */