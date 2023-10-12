/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.passive.EntityTameable;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.pathfinding.PathNavigate;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityAIFollowOwner extends EntityAIBase {
/*     */   private EntityTameable thePet;
/*     */   private EntityLivingBase theOwner;
/*     */   World theWorld;
/*     */   private double followSpeed;
/*     */   private PathNavigate petPathfinder;
/*     */   private int field_75343_h;
/*     */   float maxDist;
/*     */   float minDist;
/*     */   private boolean field_75344_i;
/*     */   
/*     */   public EntityAIFollowOwner(EntityTameable thePetIn, double followSpeedIn, float minDistIn, float maxDistIn) {
/*  29 */     this.thePet = thePetIn;
/*  30 */     this.theWorld = thePetIn.worldObj;
/*  31 */     this.followSpeed = followSpeedIn;
/*  32 */     this.petPathfinder = thePetIn.getNavigator();
/*  33 */     this.minDist = minDistIn;
/*  34 */     this.maxDist = maxDistIn;
/*  35 */     setMutexBits(3);
/*     */     
/*  37 */     if (!(thePetIn.getNavigator() instanceof PathNavigateGround))
/*     */     {
/*  39 */       throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  45 */     EntityLivingBase entitylivingbase = this.thePet.getOwner();
/*     */     
/*  47 */     if (entitylivingbase == null)
/*     */     {
/*  49 */       return false;
/*     */     }
/*  51 */     if (entitylivingbase instanceof EntityPlayer && ((EntityPlayer)entitylivingbase).isSpectator())
/*     */     {
/*  53 */       return false;
/*     */     }
/*  55 */     if (this.thePet.isSitting())
/*     */     {
/*  57 */       return false;
/*     */     }
/*  59 */     if (this.thePet.getDistanceSqToEntity((Entity)entitylivingbase) < (this.minDist * this.minDist))
/*     */     {
/*  61 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  65 */     this.theOwner = entitylivingbase;
/*  66 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean continueExecuting() {
/*  72 */     return (!this.petPathfinder.noPath() && this.thePet.getDistanceSqToEntity((Entity)this.theOwner) > (this.maxDist * this.maxDist) && !this.thePet.isSitting());
/*     */   }
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  77 */     this.field_75343_h = 0;
/*  78 */     this.field_75344_i = ((PathNavigateGround)this.thePet.getNavigator()).getAvoidsWater();
/*  79 */     ((PathNavigateGround)this.thePet.getNavigator()).setAvoidsWater(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/*  84 */     this.theOwner = null;
/*  85 */     this.petPathfinder.clearPathEntity();
/*  86 */     ((PathNavigateGround)this.thePet.getNavigator()).setAvoidsWater(true);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean func_181065_a(BlockPos p_181065_1_) {
/*  91 */     IBlockState iblockstate = this.theWorld.getBlockState(p_181065_1_);
/*  92 */     Block block = iblockstate.getBlock();
/*  93 */     return (block == Blocks.air) ? true : (!block.isFullCube());
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTask() {
/*  98 */     this.thePet.getLookHelper().setLookPositionWithEntity((Entity)this.theOwner, 10.0F, this.thePet.getVerticalFaceSpeed());
/*     */     
/* 100 */     if (!this.thePet.isSitting())
/*     */     {
/* 102 */       if (--this.field_75343_h <= 0) {
/*     */         
/* 104 */         this.field_75343_h = 10;
/*     */         
/* 106 */         if (!this.petPathfinder.tryMoveToEntityLiving((Entity)this.theOwner, this.followSpeed))
/*     */         {
/* 108 */           if (!this.thePet.getLeashed())
/*     */           {
/* 110 */             if (this.thePet.getDistanceSqToEntity((Entity)this.theOwner) >= 144.0D) {
/*     */               
/* 112 */               int i = MathHelper.floor_double(this.theOwner.posX) - 2;
/* 113 */               int j = MathHelper.floor_double(this.theOwner.posZ) - 2;
/* 114 */               int k = MathHelper.floor_double((this.theOwner.getEntityBoundingBox()).minY);
/*     */               
/* 116 */               for (int l = 0; l <= 4; l++) {
/*     */                 
/* 118 */                 for (int i1 = 0; i1 <= 4; i1++) {
/*     */                   
/* 120 */                   if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && World.doesBlockHaveSolidTopSurface((IBlockAccess)this.theWorld, new BlockPos(i + l, k - 1, j + i1)) && func_181065_a(new BlockPos(i + l, k, j + i1)) && func_181065_a(new BlockPos(i + l, k + 1, j + i1))) {
/*     */                     
/* 122 */                     this.thePet.setLocationAndAngles(((i + l) + 0.5F), k, ((j + i1) + 0.5F), this.thePet.rotationYaw, this.thePet.rotationPitch);
/* 123 */                     this.petPathfinder.clearPathEntity();
/*     */                     return;
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           }
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIFollowOwner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */