/*     */ package net.minecraft.pathfinding;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.pathfinder.NodeProcessor;
/*     */ import net.minecraft.world.pathfinder.WalkNodeProcessor;
/*     */ 
/*     */ public class PathNavigateGround
/*     */   extends PathNavigate
/*     */ {
/*     */   protected WalkNodeProcessor nodeProcessor;
/*     */   private boolean shouldAvoidSun;
/*     */   
/*     */   public PathNavigateGround(EntityLiving entitylivingIn, World worldIn) {
/*  22 */     super(entitylivingIn, worldIn);
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathFinder getPathFinder() {
/*  27 */     this.nodeProcessor = new WalkNodeProcessor();
/*  28 */     this.nodeProcessor.setEnterDoors(true);
/*  29 */     return new PathFinder((NodeProcessor)this.nodeProcessor);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canNavigate() {
/*  34 */     return (this.theEntity.onGround || (getCanSwim() && isInLiquid()) || (this.theEntity.isRiding() && this.theEntity instanceof net.minecraft.entity.monster.EntityZombie && this.theEntity.ridingEntity instanceof net.minecraft.entity.passive.EntityChicken));
/*     */   }
/*     */ 
/*     */   
/*     */   protected Vec3 getEntityPosition() {
/*  39 */     return new Vec3(this.theEntity.posX, getPathablePosY(), this.theEntity.posZ);
/*     */   }
/*     */ 
/*     */   
/*     */   private int getPathablePosY() {
/*  44 */     if (this.theEntity.isInWater() && getCanSwim()) {
/*     */       
/*  46 */       int i = (int)(this.theEntity.getEntityBoundingBox()).minY;
/*  47 */       Block block = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.theEntity.posX), i, MathHelper.floor_double(this.theEntity.posZ))).getBlock();
/*  48 */       int j = 0;
/*     */       
/*  50 */       while (block == Blocks.flowing_water || block == Blocks.water) {
/*     */         
/*  52 */         i++;
/*  53 */         block = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.theEntity.posX), i, MathHelper.floor_double(this.theEntity.posZ))).getBlock();
/*  54 */         j++;
/*     */         
/*  56 */         if (j > 16)
/*     */         {
/*  58 */           return (int)(this.theEntity.getEntityBoundingBox()).minY;
/*     */         }
/*     */       } 
/*     */       
/*  62 */       return i;
/*     */     } 
/*     */ 
/*     */     
/*  66 */     return (int)((this.theEntity.getEntityBoundingBox()).minY + 0.5D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void removeSunnyPath() {
/*  72 */     super.removeSunnyPath();
/*     */     
/*  74 */     if (this.shouldAvoidSun) {
/*     */       
/*  76 */       if (this.worldObj.canSeeSky(new BlockPos(MathHelper.floor_double(this.theEntity.posX), (int)((this.theEntity.getEntityBoundingBox()).minY + 0.5D), MathHelper.floor_double(this.theEntity.posZ)))) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/*  81 */       for (int i = 0; i < this.currentPath.getCurrentPathLength(); i++) {
/*     */         
/*  83 */         PathPoint pathpoint = this.currentPath.getPathPointFromIndex(i);
/*     */         
/*  85 */         if (this.worldObj.canSeeSky(new BlockPos(pathpoint.xCoord, pathpoint.yCoord, pathpoint.zCoord))) {
/*     */           
/*  87 */           this.currentPath.setCurrentPathLength(i - 1);
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isDirectPathBetweenPoints(Vec3 posVec31, Vec3 posVec32, int sizeX, int sizeY, int sizeZ) {
/*  96 */     int i = MathHelper.floor_double(posVec31.xCoord);
/*  97 */     int j = MathHelper.floor_double(posVec31.zCoord);
/*  98 */     double d0 = posVec32.xCoord - posVec31.xCoord;
/*  99 */     double d1 = posVec32.zCoord - posVec31.zCoord;
/* 100 */     double d2 = d0 * d0 + d1 * d1;
/*     */     
/* 102 */     if (d2 < 1.0E-8D)
/*     */     {
/* 104 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 108 */     double d3 = 1.0D / Math.sqrt(d2);
/* 109 */     d0 *= d3;
/* 110 */     d1 *= d3;
/* 111 */     sizeX += 2;
/* 112 */     sizeZ += 2;
/*     */     
/* 114 */     if (!isSafeToStandAt(i, (int)posVec31.yCoord, j, sizeX, sizeY, sizeZ, posVec31, d0, d1))
/*     */     {
/* 116 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 120 */     sizeX -= 2;
/* 121 */     sizeZ -= 2;
/* 122 */     double d4 = 1.0D / Math.abs(d0);
/* 123 */     double d5 = 1.0D / Math.abs(d1);
/* 124 */     double d6 = (i * 1) - posVec31.xCoord;
/* 125 */     double d7 = (j * 1) - posVec31.zCoord;
/*     */     
/* 127 */     if (d0 >= 0.0D)
/*     */     {
/* 129 */       d6++;
/*     */     }
/*     */     
/* 132 */     if (d1 >= 0.0D)
/*     */     {
/* 134 */       d7++;
/*     */     }
/*     */     
/* 137 */     d6 /= d0;
/* 138 */     d7 /= d1;
/* 139 */     int k = (d0 < 0.0D) ? -1 : 1;
/* 140 */     int l = (d1 < 0.0D) ? -1 : 1;
/* 141 */     int i1 = MathHelper.floor_double(posVec32.xCoord);
/* 142 */     int j1 = MathHelper.floor_double(posVec32.zCoord);
/* 143 */     int k1 = i1 - i;
/* 144 */     int l1 = j1 - j;
/*     */     
/* 146 */     while (k1 * k > 0 || l1 * l > 0) {
/*     */       
/* 148 */       if (d6 < d7) {
/*     */         
/* 150 */         d6 += d4;
/* 151 */         i += k;
/* 152 */         k1 = i1 - i;
/*     */       }
/*     */       else {
/*     */         
/* 156 */         d7 += d5;
/* 157 */         j += l;
/* 158 */         l1 = j1 - j;
/*     */       } 
/*     */       
/* 161 */       if (!isSafeToStandAt(i, (int)posVec31.yCoord, j, sizeX, sizeY, sizeZ, posVec31, d0, d1))
/*     */       {
/* 163 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 167 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isSafeToStandAt(int x, int y, int z, int sizeX, int sizeY, int sizeZ, Vec3 vec31, double p_179683_8_, double p_179683_10_) {
/* 174 */     int i = x - sizeX / 2;
/* 175 */     int j = z - sizeZ / 2;
/*     */     
/* 177 */     if (!isPositionClear(i, y, j, sizeX, sizeY, sizeZ, vec31, p_179683_8_, p_179683_10_))
/*     */     {
/* 179 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 183 */     for (int k = i; k < i + sizeX; k++) {
/*     */       
/* 185 */       for (int l = j; l < j + sizeZ; l++) {
/*     */         
/* 187 */         double d0 = k + 0.5D - vec31.xCoord;
/* 188 */         double d1 = l + 0.5D - vec31.zCoord;
/*     */         
/* 190 */         if (d0 * p_179683_8_ + d1 * p_179683_10_ >= 0.0D) {
/*     */           
/* 192 */           Block block = this.worldObj.getBlockState(new BlockPos(k, y - 1, l)).getBlock();
/* 193 */           Material material = block.getMaterial();
/*     */           
/* 195 */           if (material == Material.air)
/*     */           {
/* 197 */             return false;
/*     */           }
/*     */           
/* 200 */           if (material == Material.water && !this.theEntity.isInWater())
/*     */           {
/* 202 */             return false;
/*     */           }
/*     */           
/* 205 */           if (material == Material.lava)
/*     */           {
/* 207 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 213 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isPositionClear(int p_179692_1_, int p_179692_2_, int p_179692_3_, int p_179692_4_, int p_179692_5_, int p_179692_6_, Vec3 p_179692_7_, double p_179692_8_, double p_179692_10_) {
/* 219 */     for (BlockPos blockpos : BlockPos.getAllInBox(new BlockPos(p_179692_1_, p_179692_2_, p_179692_3_), new BlockPos(p_179692_1_ + p_179692_4_ - 1, p_179692_2_ + p_179692_5_ - 1, p_179692_3_ + p_179692_6_ - 1))) {
/*     */       
/* 221 */       double d0 = blockpos.getX() + 0.5D - p_179692_7_.xCoord;
/* 222 */       double d1 = blockpos.getZ() + 0.5D - p_179692_7_.zCoord;
/*     */       
/* 224 */       if (d0 * p_179692_8_ + d1 * p_179692_10_ >= 0.0D) {
/*     */         
/* 226 */         Block block = this.worldObj.getBlockState(blockpos).getBlock();
/*     */         
/* 228 */         if (!block.isPassable((IBlockAccess)this.worldObj, blockpos))
/*     */         {
/* 230 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 235 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAvoidsWater(boolean avoidsWater) {
/* 240 */     this.nodeProcessor.setAvoidsWater(avoidsWater);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getAvoidsWater() {
/* 245 */     return this.nodeProcessor.getAvoidsWater();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBreakDoors(boolean canBreakDoors) {
/* 250 */     this.nodeProcessor.setBreakDoors(canBreakDoors);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEnterDoors(boolean par1) {
/* 255 */     this.nodeProcessor.setEnterDoors(par1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getEnterDoors() {
/* 260 */     return this.nodeProcessor.getEnterDoors();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCanSwim(boolean canSwim) {
/* 265 */     this.nodeProcessor.setCanSwim(canSwim);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getCanSwim() {
/* 270 */     return this.nodeProcessor.getCanSwim();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAvoidSun(boolean par1) {
/* 275 */     this.shouldAvoidSun = par1;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\pathfinding\PathNavigateGround.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */