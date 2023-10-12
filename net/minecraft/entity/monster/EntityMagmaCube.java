/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityMagmaCube
/*     */   extends EntitySlime {
/*     */   public EntityMagmaCube(World worldIn) {
/*  14 */     super(worldIn);
/*  15 */     this.isImmuneToFire = true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/*  20 */     super.applyEntityAttributes();
/*  21 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000000298023224D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getCanSpawnHere() {
/*  26 */     return (this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isNotColliding() {
/*  31 */     return (this.worldObj.checkNoEntityCollision(getEntityBoundingBox(), (Entity)this) && this.worldObj.getCollidingBoundingBoxes((Entity)this, getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(getEntityBoundingBox()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTotalArmorValue() {
/*  36 */     return getSlimeSize() * 3;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBrightnessForRender(float partialTicks) {
/*  41 */     return 15728880;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getBrightness(float partialTicks) {
/*  46 */     return 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected EnumParticleTypes getParticleType() {
/*  51 */     return EnumParticleTypes.FLAME;
/*     */   }
/*     */ 
/*     */   
/*     */   protected EntitySlime createInstance() {
/*  56 */     return new EntityMagmaCube(this.worldObj);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item getDropItem() {
/*  61 */     return Items.magma_cream;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
/*  66 */     Item item = getDropItem();
/*     */     
/*  68 */     if (item != null && getSlimeSize() > 1) {
/*     */       
/*  70 */       int i = this.rand.nextInt(4) - 2;
/*     */       
/*  72 */       if (lootingModifier > 0)
/*     */       {
/*  74 */         i += this.rand.nextInt(lootingModifier + 1);
/*     */       }
/*     */       
/*  77 */       for (int j = 0; j < i; j++)
/*     */       {
/*  79 */         dropItem(item, 1);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBurning() {
/*  86 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getJumpDelay() {
/*  91 */     return super.getJumpDelay() * 4;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void alterSquishAmount() {
/*  96 */     this.squishAmount *= 0.9F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void jump() {
/* 101 */     this.motionY = (0.42F + getSlimeSize() * 0.1F);
/* 102 */     this.isAirBorne = true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleJumpLava() {
/* 107 */     this.motionY = (0.22F + getSlimeSize() * 0.05F);
/* 108 */     this.isAirBorne = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void fall(float distance, float damageMultiplier) {}
/*     */ 
/*     */   
/*     */   protected boolean canDamagePlayer() {
/* 117 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAttackStrength() {
/* 122 */     return super.getAttackStrength() + 2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getJumpSound() {
/* 127 */     return (getSlimeSize() > 1) ? "mob.magmacube.big" : "mob.magmacube.small";
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean makesSoundOnLand() {
/* 132 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\monster\EntityMagmaCube.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */