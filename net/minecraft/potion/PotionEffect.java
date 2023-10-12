/*     */ package net.minecraft.potion;
/*     */ 
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class PotionEffect
/*     */ {
/*  10 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private int potionID;
/*     */   private int duration;
/*     */   private int amplifier;
/*     */   private boolean isSplashPotion;
/*     */   private boolean isAmbient;
/*     */   private boolean isPotionDurationMax;
/*     */   private boolean showParticles;
/*     */   
/*     */   public PotionEffect(int id, int effectDuration) {
/*  21 */     this(id, effectDuration, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public PotionEffect(int id, int effectDuration, int effectAmplifier) {
/*  26 */     this(id, effectDuration, effectAmplifier, false, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public PotionEffect(int id, int effectDuration, int effectAmplifier, boolean ambient, boolean showParticles) {
/*  31 */     this.potionID = id;
/*  32 */     this.duration = effectDuration;
/*  33 */     this.amplifier = effectAmplifier;
/*  34 */     this.isAmbient = ambient;
/*  35 */     this.showParticles = showParticles;
/*     */   }
/*     */ 
/*     */   
/*     */   public PotionEffect(PotionEffect other) {
/*  40 */     this.potionID = other.potionID;
/*  41 */     this.duration = other.duration;
/*  42 */     this.amplifier = other.amplifier;
/*  43 */     this.isAmbient = other.isAmbient;
/*  44 */     this.showParticles = other.showParticles;
/*     */   }
/*     */ 
/*     */   
/*     */   public void combine(PotionEffect other) {
/*  49 */     if (this.potionID != other.potionID)
/*     */     {
/*  51 */       LOGGER.warn("This method should only be called for matching effects!");
/*     */     }
/*     */     
/*  54 */     if (other.amplifier > this.amplifier) {
/*     */       
/*  56 */       this.amplifier = other.amplifier;
/*  57 */       this.duration = other.duration;
/*     */     }
/*  59 */     else if (other.amplifier == this.amplifier && this.duration < other.duration) {
/*     */       
/*  61 */       this.duration = other.duration;
/*     */     }
/*  63 */     else if (!other.isAmbient && this.isAmbient) {
/*     */       
/*  65 */       this.isAmbient = other.isAmbient;
/*     */     } 
/*     */     
/*  68 */     this.showParticles = other.showParticles;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPotionID() {
/*  73 */     return this.potionID;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDuration() {
/*  78 */     return this.duration;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAmplifier() {
/*  83 */     return this.amplifier;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSplashPotion(boolean splashPotion) {
/*  88 */     this.isSplashPotion = splashPotion;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getIsAmbient() {
/*  93 */     return this.isAmbient;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getIsShowParticles() {
/*  98 */     return this.showParticles;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onUpdate(EntityLivingBase entityIn) {
/* 103 */     if (this.duration > 0) {
/*     */       
/* 105 */       if (Potion.potionTypes[this.potionID].isReady(this.duration, this.amplifier))
/*     */       {
/* 107 */         performEffect(entityIn);
/*     */       }
/*     */       
/* 110 */       deincrementDuration();
/*     */     } 
/*     */     
/* 113 */     return (this.duration > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   private int deincrementDuration() {
/* 118 */     return --this.duration;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performEffect(EntityLivingBase entityIn) {
/* 123 */     if (this.duration > 0)
/*     */     {
/* 125 */       Potion.potionTypes[this.potionID].performEffect(entityIn, this.amplifier);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String getEffectName() {
/* 131 */     return Potion.potionTypes[this.potionID].getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 136 */     return this.potionID;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 141 */     String s = "";
/*     */     
/* 143 */     if (getAmplifier() > 0) {
/*     */       
/* 145 */       s = getEffectName() + " x " + (getAmplifier() + 1) + ", Duration: " + getDuration();
/*     */     }
/*     */     else {
/*     */       
/* 149 */       s = getEffectName() + ", Duration: " + getDuration();
/*     */     } 
/*     */     
/* 152 */     if (this.isSplashPotion)
/*     */     {
/* 154 */       s = s + ", Splash: true";
/*     */     }
/*     */     
/* 157 */     if (!this.showParticles)
/*     */     {
/* 159 */       s = s + ", Particles: false";
/*     */     }
/*     */     
/* 162 */     return Potion.potionTypes[this.potionID].isUsable() ? ("(" + s + ")") : s;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object p_equals_1_) {
/* 167 */     if (!(p_equals_1_ instanceof PotionEffect))
/*     */     {
/* 169 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 173 */     PotionEffect potioneffect = (PotionEffect)p_equals_1_;
/* 174 */     return (this.potionID == potioneffect.potionID && this.amplifier == potioneffect.amplifier && this.duration == potioneffect.duration && this.isSplashPotion == potioneffect.isSplashPotion && this.isAmbient == potioneffect.isAmbient);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTTagCompound writeCustomPotionEffectToNBT(NBTTagCompound nbt) {
/* 180 */     nbt.setByte("Id", (byte)getPotionID());
/* 181 */     nbt.setByte("Amplifier", (byte)getAmplifier());
/* 182 */     nbt.setInteger("Duration", getDuration());
/* 183 */     nbt.setBoolean("Ambient", getIsAmbient());
/* 184 */     nbt.setBoolean("ShowParticles", getIsShowParticles());
/* 185 */     return nbt;
/*     */   }
/*     */ 
/*     */   
/*     */   public static PotionEffect readCustomPotionEffectFromNBT(NBTTagCompound nbt) {
/* 190 */     int i = nbt.getByte("Id");
/*     */     
/* 192 */     if (i >= 0 && i < Potion.potionTypes.length && Potion.potionTypes[i] != null) {
/*     */       
/* 194 */       int j = nbt.getByte("Amplifier");
/* 195 */       int k = nbt.getInteger("Duration");
/* 196 */       boolean flag = nbt.getBoolean("Ambient");
/* 197 */       boolean flag1 = true;
/*     */       
/* 199 */       if (nbt.hasKey("ShowParticles", 1))
/*     */       {
/* 201 */         flag1 = nbt.getBoolean("ShowParticles");
/*     */       }
/*     */       
/* 204 */       return new PotionEffect(i, k, j, flag, flag1);
/*     */     } 
/*     */ 
/*     */     
/* 208 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPotionDurationMax(boolean maxDuration) {
/* 214 */     this.isPotionDurationMax = maxDuration;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getIsPotionDurationMax() {
/* 219 */     return this.isPotionDurationMax;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\potion\PotionEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */