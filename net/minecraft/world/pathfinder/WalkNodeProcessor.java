/*     */ package net.minecraft.world.pathfinder;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.pathfinding.PathPoint;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WalkNodeProcessor
/*     */   extends NodeProcessor
/*     */ {
/*     */   private boolean canEnterDoors;
/*     */   private boolean canBreakDoors;
/*     */   private boolean avoidsWater;
/*     */   private boolean canSwim;
/*     */   private boolean shouldAvoidWater;
/*     */   
/*     */   public void initProcessor(IBlockAccess iblockaccessIn, Entity entityIn) {
/*  27 */     super.initProcessor(iblockaccessIn, entityIn);
/*  28 */     this.shouldAvoidWater = this.avoidsWater;
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcess() {
/*  33 */     super.postProcess();
/*  34 */     this.avoidsWater = this.shouldAvoidWater;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PathPoint getPathPointTo(Entity entityIn) {
/*     */     int i;
/*  41 */     if (this.canSwim && entityIn.isInWater()) {
/*     */       
/*  43 */       i = (int)(entityIn.getEntityBoundingBox()).minY;
/*  44 */       BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor_double(entityIn.posX), i, MathHelper.floor_double(entityIn.posZ));
/*     */       
/*  46 */       for (Block block = this.blockaccess.getBlockState((BlockPos)blockpos$mutableblockpos).getBlock(); block == Blocks.flowing_water || block == Blocks.water; block = this.blockaccess.getBlockState((BlockPos)blockpos$mutableblockpos).getBlock()) {
/*     */         
/*  48 */         i++;
/*  49 */         blockpos$mutableblockpos.set(MathHelper.floor_double(entityIn.posX), i, MathHelper.floor_double(entityIn.posZ));
/*     */       } 
/*     */       
/*  52 */       this.avoidsWater = false;
/*     */     }
/*     */     else {
/*     */       
/*  56 */       i = MathHelper.floor_double((entityIn.getEntityBoundingBox()).minY + 0.5D);
/*     */     } 
/*     */     
/*  59 */     return openPoint(MathHelper.floor_double((entityIn.getEntityBoundingBox()).minX), i, MathHelper.floor_double((entityIn.getEntityBoundingBox()).minZ));
/*     */   }
/*     */ 
/*     */   
/*     */   public PathPoint getPathPointToCoords(Entity entityIn, double x, double y, double target) {
/*  64 */     return openPoint(MathHelper.floor_double(x - (entityIn.width / 2.0F)), MathHelper.floor_double(y), MathHelper.floor_double(target - (entityIn.width / 2.0F)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int findPathOptions(PathPoint[] pathOptions, Entity entityIn, PathPoint currentPoint, PathPoint targetPoint, float maxDistance) {
/*  69 */     int i = 0;
/*  70 */     int j = 0;
/*     */     
/*  72 */     if (getVerticalOffset(entityIn, currentPoint.xCoord, currentPoint.yCoord + 1, currentPoint.zCoord) == 1)
/*     */     {
/*  74 */       j = 1;
/*     */     }
/*     */     
/*  77 */     PathPoint pathpoint = getSafePoint(entityIn, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord + 1, j);
/*  78 */     PathPoint pathpoint1 = getSafePoint(entityIn, currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord, j);
/*  79 */     PathPoint pathpoint2 = getSafePoint(entityIn, currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord, j);
/*  80 */     PathPoint pathpoint3 = getSafePoint(entityIn, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord - 1, j);
/*     */     
/*  82 */     if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance)
/*     */     {
/*  84 */       pathOptions[i++] = pathpoint;
/*     */     }
/*     */     
/*  87 */     if (pathpoint1 != null && !pathpoint1.visited && pathpoint1.distanceTo(targetPoint) < maxDistance)
/*     */     {
/*  89 */       pathOptions[i++] = pathpoint1;
/*     */     }
/*     */     
/*  92 */     if (pathpoint2 != null && !pathpoint2.visited && pathpoint2.distanceTo(targetPoint) < maxDistance)
/*     */     {
/*  94 */       pathOptions[i++] = pathpoint2;
/*     */     }
/*     */     
/*  97 */     if (pathpoint3 != null && !pathpoint3.visited && pathpoint3.distanceTo(targetPoint) < maxDistance)
/*     */     {
/*  99 */       pathOptions[i++] = pathpoint3;
/*     */     }
/*     */     
/* 102 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   private PathPoint getSafePoint(Entity entityIn, int x, int y, int z, int p_176171_5_) {
/* 107 */     PathPoint pathpoint = null;
/* 108 */     int i = getVerticalOffset(entityIn, x, y, z);
/*     */     
/* 110 */     if (i == 2)
/*     */     {
/* 112 */       return openPoint(x, y, z);
/*     */     }
/*     */ 
/*     */     
/* 116 */     if (i == 1)
/*     */     {
/* 118 */       pathpoint = openPoint(x, y, z);
/*     */     }
/*     */     
/* 121 */     if (pathpoint == null && p_176171_5_ > 0 && i != -3 && i != -4 && getVerticalOffset(entityIn, x, y + p_176171_5_, z) == 1) {
/*     */       
/* 123 */       pathpoint = openPoint(x, y + p_176171_5_, z);
/* 124 */       y += p_176171_5_;
/*     */     } 
/*     */     
/* 127 */     if (pathpoint != null) {
/*     */       
/* 129 */       int j = 0;
/*     */       
/*     */       int k;
/* 132 */       for (k = 0; y > 0; pathpoint = openPoint(x, y, z)) {
/*     */         
/* 134 */         k = getVerticalOffset(entityIn, x, y - 1, z);
/*     */         
/* 136 */         if (this.avoidsWater && k == -1)
/*     */         {
/* 138 */           return null;
/*     */         }
/*     */         
/* 141 */         if (k != 1) {
/*     */           break;
/*     */         }
/*     */ 
/*     */         
/* 146 */         if (j++ >= entityIn.getMaxFallHeight())
/*     */         {
/* 148 */           return null;
/*     */         }
/*     */         
/* 151 */         y--;
/*     */         
/* 153 */         if (y <= 0)
/*     */         {
/* 155 */           return null;
/*     */         }
/*     */       } 
/*     */       
/* 159 */       if (k == -2)
/*     */       {
/* 161 */         return null;
/*     */       }
/*     */     } 
/*     */     
/* 165 */     return pathpoint;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int getVerticalOffset(Entity entityIn, int x, int y, int z) {
/* 171 */     return func_176170_a(this.blockaccess, entityIn, x, y, z, this.entitySizeX, this.entitySizeY, this.entitySizeZ, this.avoidsWater, this.canBreakDoors, this.canEnterDoors);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int func_176170_a(IBlockAccess blockaccessIn, Entity entityIn, int x, int y, int z, int sizeX, int sizeY, int sizeZ, boolean avoidWater, boolean breakDoors, boolean enterDoors) {
/* 176 */     boolean flag = false;
/* 177 */     BlockPos blockpos = new BlockPos(entityIn);
/* 178 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */     
/* 180 */     for (int i = x; i < x + sizeX; i++) {
/*     */       
/* 182 */       for (int j = y; j < y + sizeY; j++) {
/*     */         
/* 184 */         for (int k = z; k < z + sizeZ; k++) {
/*     */           
/* 186 */           blockpos$mutableblockpos.set(i, j, k);
/* 187 */           Block block = blockaccessIn.getBlockState((BlockPos)blockpos$mutableblockpos).getBlock();
/*     */           
/* 189 */           if (block.getMaterial() != Material.air) {
/*     */             
/* 191 */             if (block != Blocks.trapdoor && block != Blocks.iron_trapdoor) {
/*     */               
/* 193 */               if (block != Blocks.flowing_water && block != Blocks.water)
/*     */               {
/* 195 */                 if (!enterDoors && block instanceof net.minecraft.block.BlockDoor && block.getMaterial() == Material.wood)
/*     */                 {
/* 197 */                   return 0;
/*     */                 }
/*     */               }
/*     */               else
/*     */               {
/* 202 */                 if (avoidWater)
/*     */                 {
/* 204 */                   return -1;
/*     */                 }
/*     */                 
/* 207 */                 flag = true;
/*     */               }
/*     */             
/*     */             } else {
/*     */               
/* 212 */               flag = true;
/*     */             } 
/*     */             
/* 215 */             if (entityIn.worldObj.getBlockState((BlockPos)blockpos$mutableblockpos).getBlock() instanceof net.minecraft.block.BlockRailBase) {
/*     */               
/* 217 */               if (!(entityIn.worldObj.getBlockState(blockpos).getBlock() instanceof net.minecraft.block.BlockRailBase) && !(entityIn.worldObj.getBlockState(blockpos.down()).getBlock() instanceof net.minecraft.block.BlockRailBase))
/*     */               {
/* 219 */                 return -3;
/*     */               }
/*     */             }
/* 222 */             else if (!block.isPassable(blockaccessIn, (BlockPos)blockpos$mutableblockpos) && (!breakDoors || !(block instanceof net.minecraft.block.BlockDoor) || block.getMaterial() != Material.wood)) {
/*     */               
/* 224 */               if (block instanceof net.minecraft.block.BlockFence || block instanceof net.minecraft.block.BlockFenceGate || block instanceof net.minecraft.block.BlockWall)
/*     */               {
/* 226 */                 return -3;
/*     */               }
/*     */               
/* 229 */               if (block == Blocks.trapdoor || block == Blocks.iron_trapdoor)
/*     */               {
/* 231 */                 return -4;
/*     */               }
/*     */               
/* 234 */               Material material = block.getMaterial();
/*     */               
/* 236 */               if (material != Material.lava)
/*     */               {
/* 238 */                 return 0;
/*     */               }
/*     */               
/* 241 */               if (!entityIn.isInLava())
/*     */               {
/* 243 */                 return -2;
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 251 */     return flag ? 2 : 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEnterDoors(boolean canEnterDoorsIn) {
/* 256 */     this.canEnterDoors = canEnterDoorsIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBreakDoors(boolean canBreakDoorsIn) {
/* 261 */     this.canBreakDoors = canBreakDoorsIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAvoidsWater(boolean avoidsWaterIn) {
/* 266 */     this.avoidsWater = avoidsWaterIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCanSwim(boolean canSwimIn) {
/* 271 */     this.canSwim = canSwimIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getEnterDoors() {
/* 276 */     return this.canEnterDoors;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getCanSwim() {
/* 281 */     return this.canSwim;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getAvoidsWater() {
/* 286 */     return this.avoidsWater;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\pathfinder\WalkNodeProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */