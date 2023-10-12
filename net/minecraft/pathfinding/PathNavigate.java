/*     */ package net.minecraft.pathfinding;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.ChunkCache;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class PathNavigate {
/*     */   protected EntityLiving theEntity;
/*     */   protected World worldObj;
/*     */   protected PathEntity currentPath;
/*     */   protected double speed;
/*     */   private final IAttributeInstance pathSearchRange;
/*     */   private int totalTicks;
/*     */   private int ticksAtLastPos;
/*  24 */   private Vec3 lastPosCheck = new Vec3(0.0D, 0.0D, 0.0D);
/*  25 */   private float heightRequirement = 1.0F;
/*     */   
/*     */   private final PathFinder pathFinder;
/*     */   
/*     */   public PathNavigate(EntityLiving entitylivingIn, World worldIn) {
/*  30 */     this.theEntity = entitylivingIn;
/*  31 */     this.worldObj = worldIn;
/*  32 */     this.pathSearchRange = entitylivingIn.getEntityAttribute(SharedMonsterAttributes.followRange);
/*  33 */     this.pathFinder = getPathFinder();
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract PathFinder getPathFinder();
/*     */   
/*     */   public void setSpeed(double speedIn) {
/*  40 */     this.speed = speedIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPathSearchRange() {
/*  45 */     return (float)this.pathSearchRange.getAttributeValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public final PathEntity getPathToXYZ(double x, double y, double z) {
/*  50 */     return getPathToPos(new BlockPos(MathHelper.floor_double(x), (int)y, MathHelper.floor_double(z)));
/*     */   }
/*     */ 
/*     */   
/*     */   public PathEntity getPathToPos(BlockPos pos) {
/*  55 */     if (!canNavigate())
/*     */     {
/*  57 */       return null;
/*     */     }
/*     */ 
/*     */     
/*  61 */     float f = getPathSearchRange();
/*  62 */     this.worldObj.theProfiler.startSection("pathfind");
/*  63 */     BlockPos blockpos = new BlockPos((Entity)this.theEntity);
/*  64 */     int i = (int)(f + 8.0F);
/*  65 */     ChunkCache chunkcache = new ChunkCache(this.worldObj, blockpos.add(-i, -i, -i), blockpos.add(i, i, i), 0);
/*  66 */     PathEntity pathentity = this.pathFinder.createEntityPathTo((IBlockAccess)chunkcache, (Entity)this.theEntity, pos, f);
/*  67 */     this.worldObj.theProfiler.endSection();
/*  68 */     return pathentity;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean tryMoveToXYZ(double x, double y, double z, double speedIn) {
/*  74 */     PathEntity pathentity = getPathToXYZ(MathHelper.floor_double(x), (int)y, MathHelper.floor_double(z));
/*  75 */     return setPath(pathentity, speedIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHeightRequirement(float jumpHeight) {
/*  80 */     this.heightRequirement = jumpHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public PathEntity getPathToEntityLiving(Entity entityIn) {
/*  85 */     if (!canNavigate())
/*     */     {
/*  87 */       return null;
/*     */     }
/*     */ 
/*     */     
/*  91 */     float f = getPathSearchRange();
/*  92 */     this.worldObj.theProfiler.startSection("pathfind");
/*  93 */     BlockPos blockpos = (new BlockPos((Entity)this.theEntity)).up();
/*  94 */     int i = (int)(f + 16.0F);
/*  95 */     ChunkCache chunkcache = new ChunkCache(this.worldObj, blockpos.add(-i, -i, -i), blockpos.add(i, i, i), 0);
/*  96 */     PathEntity pathentity = this.pathFinder.createEntityPathTo((IBlockAccess)chunkcache, (Entity)this.theEntity, entityIn, f);
/*  97 */     this.worldObj.theProfiler.endSection();
/*  98 */     return pathentity;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean tryMoveToEntityLiving(Entity entityIn, double speedIn) {
/* 104 */     PathEntity pathentity = getPathToEntityLiving(entityIn);
/* 105 */     return (pathentity != null) ? setPath(pathentity, speedIn) : false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setPath(PathEntity pathentityIn, double speedIn) {
/* 110 */     if (pathentityIn == null) {
/*     */       
/* 112 */       this.currentPath = null;
/* 113 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 117 */     if (!pathentityIn.isSamePath(this.currentPath))
/*     */     {
/* 119 */       this.currentPath = pathentityIn;
/*     */     }
/*     */     
/* 122 */     removeSunnyPath();
/*     */     
/* 124 */     if (this.currentPath.getCurrentPathLength() == 0)
/*     */     {
/* 126 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 130 */     this.speed = speedIn;
/* 131 */     Vec3 vec3 = getEntityPosition();
/* 132 */     this.ticksAtLastPos = this.totalTicks;
/* 133 */     this.lastPosCheck = vec3;
/* 134 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathEntity getPath() {
/* 141 */     return this.currentPath;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdateNavigation() {
/* 146 */     this.totalTicks++;
/*     */     
/* 148 */     if (!noPath()) {
/*     */       
/* 150 */       if (canNavigate()) {
/*     */         
/* 152 */         pathFollow();
/*     */       }
/* 154 */       else if (this.currentPath != null && this.currentPath.getCurrentPathIndex() < this.currentPath.getCurrentPathLength()) {
/*     */         
/* 156 */         Vec3 vec3 = getEntityPosition();
/* 157 */         Vec3 vec31 = this.currentPath.getVectorFromIndex((Entity)this.theEntity, this.currentPath.getCurrentPathIndex());
/*     */         
/* 159 */         if (vec3.yCoord > vec31.yCoord && !this.theEntity.onGround && MathHelper.floor_double(vec3.xCoord) == MathHelper.floor_double(vec31.xCoord) && MathHelper.floor_double(vec3.zCoord) == MathHelper.floor_double(vec31.zCoord))
/*     */         {
/* 161 */           this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
/*     */         }
/*     */       } 
/*     */       
/* 165 */       if (!noPath()) {
/*     */         
/* 167 */         Vec3 vec32 = this.currentPath.getPosition((Entity)this.theEntity);
/*     */         
/* 169 */         if (vec32 != null) {
/*     */           
/* 171 */           AxisAlignedBB axisalignedbb1 = (new AxisAlignedBB(vec32.xCoord, vec32.yCoord, vec32.zCoord, vec32.xCoord, vec32.yCoord, vec32.zCoord)).expand(0.5D, 0.5D, 0.5D);
/* 172 */           List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes((Entity)this.theEntity, axisalignedbb1.addCoord(0.0D, -1.0D, 0.0D));
/* 173 */           double d0 = -1.0D;
/* 174 */           axisalignedbb1 = axisalignedbb1.offset(0.0D, 1.0D, 0.0D);
/*     */           
/* 176 */           for (AxisAlignedBB axisalignedbb : list)
/*     */           {
/* 178 */             d0 = axisalignedbb.calculateYOffset(axisalignedbb1, d0);
/*     */           }
/*     */           
/* 181 */           this.theEntity.getMoveHelper().setMoveTo(vec32.xCoord, vec32.yCoord + d0, vec32.zCoord, this.speed);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void pathFollow() {
/* 189 */     Vec3 vec3 = getEntityPosition();
/* 190 */     int i = this.currentPath.getCurrentPathLength();
/*     */     
/* 192 */     for (int j = this.currentPath.getCurrentPathIndex(); j < this.currentPath.getCurrentPathLength(); j++) {
/*     */       
/* 194 */       if ((this.currentPath.getPathPointFromIndex(j)).yCoord != (int)vec3.yCoord) {
/*     */         
/* 196 */         i = j;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 201 */     float f = this.theEntity.width * this.theEntity.width * this.heightRequirement;
/*     */     
/* 203 */     for (int k = this.currentPath.getCurrentPathIndex(); k < i; k++) {
/*     */       
/* 205 */       Vec3 vec31 = this.currentPath.getVectorFromIndex((Entity)this.theEntity, k);
/*     */       
/* 207 */       if (vec3.squareDistanceTo(vec31) < f)
/*     */       {
/* 209 */         this.currentPath.setCurrentPathIndex(k + 1);
/*     */       }
/*     */     } 
/*     */     
/* 213 */     int j1 = MathHelper.ceiling_float_int(this.theEntity.width);
/* 214 */     int k1 = (int)this.theEntity.height + 1;
/* 215 */     int l = j1;
/*     */     
/* 217 */     for (int i1 = i - 1; i1 >= this.currentPath.getCurrentPathIndex(); i1--) {
/*     */       
/* 219 */       if (isDirectPathBetweenPoints(vec3, this.currentPath.getVectorFromIndex((Entity)this.theEntity, i1), j1, k1, l)) {
/*     */         
/* 221 */         this.currentPath.setCurrentPathIndex(i1);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 226 */     checkForStuck(vec3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void checkForStuck(Vec3 positionVec3) {
/* 231 */     if (this.totalTicks - this.ticksAtLastPos > 100) {
/*     */       
/* 233 */       if (positionVec3.squareDistanceTo(this.lastPosCheck) < 2.25D)
/*     */       {
/* 235 */         clearPathEntity();
/*     */       }
/*     */       
/* 238 */       this.ticksAtLastPos = this.totalTicks;
/* 239 */       this.lastPosCheck = positionVec3;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean noPath() {
/* 245 */     return (this.currentPath == null || this.currentPath.isFinished());
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearPathEntity() {
/* 250 */     this.currentPath = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract Vec3 getEntityPosition();
/*     */   
/*     */   protected abstract boolean canNavigate();
/*     */   
/*     */   protected boolean isInLiquid() {
/* 259 */     return (this.theEntity.isInWater() || this.theEntity.isInLava());
/*     */   }
/*     */   
/*     */   protected void removeSunnyPath() {}
/*     */   
/*     */   protected abstract boolean isDirectPathBetweenPoints(Vec3 paramVec31, Vec3 paramVec32, int paramInt1, int paramInt2, int paramInt3);
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\pathfinding\PathNavigate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */