/*     */ package net.minecraft.util;
/*     */ 
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemFood;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ 
/*     */ public class FoodStats
/*     */ {
/*  11 */   private int foodLevel = 20;
/*  12 */   private float foodSaturationLevel = 5.0F;
/*     */   private float foodExhaustionLevel;
/*     */   private int foodTimer;
/*  15 */   private int prevFoodLevel = 20;
/*     */ 
/*     */   
/*     */   public void addStats(int foodLevelIn, float foodSaturationModifier) {
/*  19 */     this.foodLevel = Math.min(foodLevelIn + this.foodLevel, 20);
/*  20 */     this.foodSaturationLevel = Math.min(this.foodSaturationLevel + foodLevelIn * foodSaturationModifier * 2.0F, this.foodLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addStats(ItemFood foodItem, ItemStack p_151686_2_) {
/*  25 */     addStats(foodItem.getHealAmount(p_151686_2_), foodItem.getSaturationModifier(p_151686_2_));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate(EntityPlayer player) {
/*  30 */     EnumDifficulty enumdifficulty = player.worldObj.getDifficulty();
/*  31 */     this.prevFoodLevel = this.foodLevel;
/*     */     
/*  33 */     if (this.foodExhaustionLevel > 4.0F) {
/*     */       
/*  35 */       this.foodExhaustionLevel -= 4.0F;
/*     */       
/*  37 */       if (this.foodSaturationLevel > 0.0F) {
/*     */         
/*  39 */         this.foodSaturationLevel = Math.max(this.foodSaturationLevel - 1.0F, 0.0F);
/*     */       }
/*  41 */       else if (enumdifficulty != EnumDifficulty.PEACEFUL) {
/*     */         
/*  43 */         this.foodLevel = Math.max(this.foodLevel - 1, 0);
/*     */       } 
/*     */     } 
/*     */     
/*  47 */     if (player.worldObj.getGameRules().getBoolean("naturalRegeneration") && this.foodLevel >= 18 && player.shouldHeal()) {
/*     */       
/*  49 */       this.foodTimer++;
/*     */       
/*  51 */       if (this.foodTimer >= 80)
/*     */       {
/*  53 */         player.heal(1.0F);
/*  54 */         addExhaustion(3.0F);
/*  55 */         this.foodTimer = 0;
/*     */       }
/*     */     
/*  58 */     } else if (this.foodLevel <= 0) {
/*     */       
/*  60 */       this.foodTimer++;
/*     */       
/*  62 */       if (this.foodTimer >= 80)
/*     */       {
/*  64 */         if (player.getHealth() > 10.0F || enumdifficulty == EnumDifficulty.HARD || (player.getHealth() > 1.0F && enumdifficulty == EnumDifficulty.NORMAL))
/*     */         {
/*  66 */           player.attackEntityFrom(DamageSource.starve, 1.0F);
/*     */         }
/*     */         
/*  69 */         this.foodTimer = 0;
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/*  74 */       this.foodTimer = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void readNBT(NBTTagCompound p_75112_1_) {
/*  80 */     if (p_75112_1_.hasKey("foodLevel", 99)) {
/*     */       
/*  82 */       this.foodLevel = p_75112_1_.getInteger("foodLevel");
/*  83 */       this.foodTimer = p_75112_1_.getInteger("foodTickTimer");
/*  84 */       this.foodSaturationLevel = p_75112_1_.getFloat("foodSaturationLevel");
/*  85 */       this.foodExhaustionLevel = p_75112_1_.getFloat("foodExhaustionLevel");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeNBT(NBTTagCompound p_75117_1_) {
/*  91 */     p_75117_1_.setInteger("foodLevel", this.foodLevel);
/*  92 */     p_75117_1_.setInteger("foodTickTimer", this.foodTimer);
/*  93 */     p_75117_1_.setFloat("foodSaturationLevel", this.foodSaturationLevel);
/*  94 */     p_75117_1_.setFloat("foodExhaustionLevel", this.foodExhaustionLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFoodLevel() {
/*  99 */     return this.foodLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPrevFoodLevel() {
/* 104 */     return this.prevFoodLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean needFood() {
/* 109 */     return (this.foodLevel < 20);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addExhaustion(float p_75113_1_) {
/* 114 */     this.foodExhaustionLevel = Math.min(this.foodExhaustionLevel + p_75113_1_, 40.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getSaturationLevel() {
/* 119 */     return this.foodSaturationLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFoodLevel(int foodLevelIn) {
/* 124 */     this.foodLevel = foodLevelIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFoodSaturationLevel(float foodSaturationLevelIn) {
/* 129 */     this.foodSaturationLevel = foodSaturationLevelIn;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\FoodStats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */