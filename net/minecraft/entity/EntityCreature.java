/*     */ package net.minecraft.entity;
/*     */ 
/*     */ import java.util.UUID;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
/*     */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.entity.passive.EntityTameable;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.Vec3i;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class EntityCreature extends EntityLiving {
/*  14 */   public static final UUID FLEEING_SPEED_MODIFIER_UUID = UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A");
/*  15 */   public static final AttributeModifier FLEEING_SPEED_MODIFIER = (new AttributeModifier(FLEEING_SPEED_MODIFIER_UUID, "Fleeing speed bonus", 2.0D, 2)).setSaved(false);
/*  16 */   private BlockPos homePosition = BlockPos.ORIGIN;
/*  17 */   private float maximumHomeDistance = -1.0F;
/*  18 */   private EntityAIBase aiBase = (EntityAIBase)new EntityAIMoveTowardsRestriction(this, 1.0D);
/*     */   
/*     */   private boolean isMovementAITaskSet;
/*     */   
/*     */   public EntityCreature(World worldIn) {
/*  23 */     super(worldIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getBlockPathWeight(BlockPos pos) {
/*  28 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getCanSpawnHere() {
/*  33 */     return (super.getCanSpawnHere() && getBlockPathWeight(new BlockPos(this.posX, (getEntityBoundingBox()).minY, this.posZ)) >= 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasPath() {
/*  38 */     return !this.navigator.noPath();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWithinHomeDistanceCurrentPosition() {
/*  43 */     return isWithinHomeDistanceFromPosition(new BlockPos(this));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWithinHomeDistanceFromPosition(BlockPos pos) {
/*  48 */     return (this.maximumHomeDistance == -1.0F) ? true : ((this.homePosition.distanceSq((Vec3i)pos) < (this.maximumHomeDistance * this.maximumHomeDistance)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHomePosAndDistance(BlockPos pos, int distance) {
/*  53 */     this.homePosition = pos;
/*  54 */     this.maximumHomeDistance = distance;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos getHomePosition() {
/*  59 */     return this.homePosition;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getMaximumHomeDistance() {
/*  64 */     return this.maximumHomeDistance;
/*     */   }
/*     */ 
/*     */   
/*     */   public void detachHome() {
/*  69 */     this.maximumHomeDistance = -1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasHome() {
/*  74 */     return (this.maximumHomeDistance != -1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateLeashedState() {
/*  79 */     super.updateLeashedState();
/*     */     
/*  81 */     if (getLeashed() && getLeashedToEntity() != null && (getLeashedToEntity()).worldObj == this.worldObj) {
/*     */       
/*  83 */       Entity entity = getLeashedToEntity();
/*  84 */       setHomePosAndDistance(new BlockPos((int)entity.posX, (int)entity.posY, (int)entity.posZ), 5);
/*  85 */       float f = getDistanceToEntity(entity);
/*     */       
/*  87 */       if (this instanceof EntityTameable && ((EntityTameable)this).isSitting()) {
/*     */         
/*  89 */         if (f > 10.0F)
/*     */         {
/*  91 */           clearLeashed(true, true);
/*     */         }
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*  97 */       if (!this.isMovementAITaskSet) {
/*     */         
/*  99 */         this.tasks.addTask(2, this.aiBase);
/*     */         
/* 101 */         if (getNavigator() instanceof PathNavigateGround)
/*     */         {
/* 103 */           ((PathNavigateGround)getNavigator()).setAvoidsWater(false);
/*     */         }
/*     */         
/* 106 */         this.isMovementAITaskSet = true;
/*     */       } 
/*     */       
/* 109 */       func_142017_o(f);
/*     */       
/* 111 */       if (f > 4.0F)
/*     */       {
/* 113 */         getNavigator().tryMoveToEntityLiving(entity, 1.0D);
/*     */       }
/*     */       
/* 116 */       if (f > 6.0F) {
/*     */         
/* 118 */         double d0 = (entity.posX - this.posX) / f;
/* 119 */         double d1 = (entity.posY - this.posY) / f;
/* 120 */         double d2 = (entity.posZ - this.posZ) / f;
/* 121 */         this.motionX += d0 * Math.abs(d0) * 0.4D;
/* 122 */         this.motionY += d1 * Math.abs(d1) * 0.4D;
/* 123 */         this.motionZ += d2 * Math.abs(d2) * 0.4D;
/*     */       } 
/*     */       
/* 126 */       if (f > 10.0F)
/*     */       {
/* 128 */         clearLeashed(true, true);
/*     */       }
/*     */     }
/* 131 */     else if (!getLeashed() && this.isMovementAITaskSet) {
/*     */       
/* 133 */       this.isMovementAITaskSet = false;
/* 134 */       this.tasks.removeTask(this.aiBase);
/*     */       
/* 136 */       if (getNavigator() instanceof PathNavigateGround)
/*     */       {
/* 138 */         ((PathNavigateGround)getNavigator()).setAvoidsWater(true);
/*     */       }
/*     */       
/* 141 */       detachHome();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void func_142017_o(float p_142017_1_) {}
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\EntityCreature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */