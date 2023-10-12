/*     */ package net.minecraft.world;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.BlockPortal;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.block.state.pattern.BlockPattern;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.LongHashMap;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3i;
/*     */ 
/*     */ public class Teleporter {
/*     */   private final WorldServer worldServerInstance;
/*  21 */   private final LongHashMap<PortalPosition> destinationCoordinateCache = new LongHashMap(); private final Random random;
/*  22 */   private final List<Long> destinationCoordinateKeys = Lists.newArrayList();
/*     */ 
/*     */   
/*     */   public Teleporter(WorldServer worldIn) {
/*  26 */     this.worldServerInstance = worldIn;
/*  27 */     this.random = new Random(worldIn.getSeed());
/*     */   }
/*     */ 
/*     */   
/*     */   public void placeInPortal(Entity entityIn, float rotationYaw) {
/*  32 */     if (this.worldServerInstance.provider.getDimensionId() != 1) {
/*     */       
/*  34 */       if (!placeInExistingPortal(entityIn, rotationYaw))
/*     */       {
/*  36 */         makePortal(entityIn);
/*  37 */         placeInExistingPortal(entityIn, rotationYaw);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/*  42 */       int i = MathHelper.floor_double(entityIn.posX);
/*  43 */       int j = MathHelper.floor_double(entityIn.posY) - 1;
/*  44 */       int k = MathHelper.floor_double(entityIn.posZ);
/*  45 */       int l = 1;
/*  46 */       int i1 = 0;
/*     */       
/*  48 */       for (int j1 = -2; j1 <= 2; j1++) {
/*     */         
/*  50 */         for (int k1 = -2; k1 <= 2; k1++) {
/*     */           
/*  52 */           for (int l1 = -1; l1 < 3; l1++) {
/*     */             
/*  54 */             int i2 = i + k1 * l + j1 * i1;
/*  55 */             int j2 = j + l1;
/*  56 */             int k2 = k + k1 * i1 - j1 * l;
/*  57 */             boolean flag = (l1 < 0);
/*  58 */             this.worldServerInstance.setBlockState(new BlockPos(i2, j2, k2), flag ? Blocks.obsidian.getDefaultState() : Blocks.air.getDefaultState());
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/*  63 */       entityIn.setLocationAndAngles(i, j, k, entityIn.rotationYaw, 0.0F);
/*  64 */       entityIn.motionX = entityIn.motionY = entityIn.motionZ = 0.0D;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean placeInExistingPortal(Entity entityIn, float rotationYaw) {
/*  70 */     int i = 128;
/*  71 */     double d0 = -1.0D;
/*  72 */     int j = MathHelper.floor_double(entityIn.posX);
/*  73 */     int k = MathHelper.floor_double(entityIn.posZ);
/*  74 */     boolean flag = true;
/*  75 */     BlockPos blockpos = BlockPos.ORIGIN;
/*  76 */     long l = ChunkCoordIntPair.chunkXZ2Int(j, k);
/*     */     
/*  78 */     if (this.destinationCoordinateCache.containsItem(l)) {
/*     */       
/*  80 */       PortalPosition teleporter$portalposition = (PortalPosition)this.destinationCoordinateCache.getValueByKey(l);
/*  81 */       d0 = 0.0D;
/*  82 */       blockpos = teleporter$portalposition;
/*  83 */       teleporter$portalposition.lastUpdateTime = this.worldServerInstance.getTotalWorldTime();
/*  84 */       flag = false;
/*     */     }
/*     */     else {
/*     */       
/*  88 */       BlockPos blockpos3 = new BlockPos(entityIn);
/*     */       
/*  90 */       for (int i1 = -128; i1 <= 128; i1++) {
/*     */ 
/*     */ 
/*     */         
/*  94 */         for (int j1 = -128; j1 <= 128; j1++) {
/*     */           
/*  96 */           for (BlockPos blockpos1 = blockpos3.add(i1, this.worldServerInstance.getActualHeight() - 1 - blockpos3.getY(), j1); blockpos1.getY() >= 0; blockpos1 = blockpos2) {
/*     */             
/*  98 */             BlockPos blockpos2 = blockpos1.down();
/*     */             
/* 100 */             if (this.worldServerInstance.getBlockState(blockpos1).getBlock() == Blocks.portal) {
/*     */               
/* 102 */               while (this.worldServerInstance.getBlockState(blockpos2 = blockpos1.down()).getBlock() == Blocks.portal)
/*     */               {
/* 104 */                 blockpos1 = blockpos2;
/*     */               }
/*     */               
/* 107 */               double d1 = blockpos1.distanceSq((Vec3i)blockpos3);
/*     */               
/* 109 */               if (d0 < 0.0D || d1 < d0) {
/*     */                 
/* 111 */                 d0 = d1;
/* 112 */                 blockpos = blockpos1;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 120 */     if (d0 >= 0.0D) {
/*     */       
/* 122 */       if (flag) {
/*     */         
/* 124 */         this.destinationCoordinateCache.add(l, new PortalPosition(blockpos, this.worldServerInstance.getTotalWorldTime()));
/* 125 */         this.destinationCoordinateKeys.add(Long.valueOf(l));
/*     */       } 
/*     */       
/* 128 */       double d5 = blockpos.getX() + 0.5D;
/* 129 */       double d6 = blockpos.getY() + 0.5D;
/* 130 */       double d7 = blockpos.getZ() + 0.5D;
/* 131 */       BlockPattern.PatternHelper blockpattern$patternhelper = Blocks.portal.func_181089_f(this.worldServerInstance, blockpos);
/* 132 */       boolean flag1 = (blockpattern$patternhelper.getFinger().rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE);
/* 133 */       double d2 = (blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X) ? blockpattern$patternhelper.getPos().getZ() : blockpattern$patternhelper.getPos().getX();
/* 134 */       d6 = (blockpattern$patternhelper.getPos().getY() + 1) - (entityIn.func_181014_aG()).yCoord * blockpattern$patternhelper.func_181119_e();
/*     */       
/* 136 */       if (flag1)
/*     */       {
/* 138 */         d2++;
/*     */       }
/*     */       
/* 141 */       if (blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X) {
/*     */         
/* 143 */         d7 = d2 + (1.0D - (entityIn.func_181014_aG()).xCoord) * blockpattern$patternhelper.func_181118_d() * blockpattern$patternhelper.getFinger().rotateY().getAxisDirection().getOffset();
/*     */       }
/*     */       else {
/*     */         
/* 147 */         d5 = d2 + (1.0D - (entityIn.func_181014_aG()).xCoord) * blockpattern$patternhelper.func_181118_d() * blockpattern$patternhelper.getFinger().rotateY().getAxisDirection().getOffset();
/*     */       } 
/*     */       
/* 150 */       float f = 0.0F;
/* 151 */       float f1 = 0.0F;
/* 152 */       float f2 = 0.0F;
/* 153 */       float f3 = 0.0F;
/*     */       
/* 155 */       if (blockpattern$patternhelper.getFinger().getOpposite() == entityIn.getTeleportDirection()) {
/*     */         
/* 157 */         f = 1.0F;
/* 158 */         f1 = 1.0F;
/*     */       }
/* 160 */       else if (blockpattern$patternhelper.getFinger().getOpposite() == entityIn.getTeleportDirection().getOpposite()) {
/*     */         
/* 162 */         f = -1.0F;
/* 163 */         f1 = -1.0F;
/*     */       }
/* 165 */       else if (blockpattern$patternhelper.getFinger().getOpposite() == entityIn.getTeleportDirection().rotateY()) {
/*     */         
/* 167 */         f2 = 1.0F;
/* 168 */         f3 = -1.0F;
/*     */       }
/*     */       else {
/*     */         
/* 172 */         f2 = -1.0F;
/* 173 */         f3 = 1.0F;
/*     */       } 
/*     */       
/* 176 */       double d3 = entityIn.motionX;
/* 177 */       double d4 = entityIn.motionZ;
/* 178 */       entityIn.motionX = d3 * f + d4 * f3;
/* 179 */       entityIn.motionZ = d3 * f2 + d4 * f1;
/* 180 */       entityIn.rotationYaw = rotationYaw - (entityIn.getTeleportDirection().getOpposite().getHorizontalIndex() * 90) + (blockpattern$patternhelper.getFinger().getHorizontalIndex() * 90);
/* 181 */       entityIn.setLocationAndAngles(d5, d6, d7, entityIn.rotationYaw, entityIn.rotationPitch);
/* 182 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 186 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean makePortal(Entity entityIn) {
/* 192 */     int i = 16;
/* 193 */     double d0 = -1.0D;
/* 194 */     int j = MathHelper.floor_double(entityIn.posX);
/* 195 */     int k = MathHelper.floor_double(entityIn.posY);
/* 196 */     int l = MathHelper.floor_double(entityIn.posZ);
/* 197 */     int i1 = j;
/* 198 */     int j1 = k;
/* 199 */     int k1 = l;
/* 200 */     int l1 = 0;
/* 201 */     int i2 = this.random.nextInt(4);
/* 202 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */     
/* 204 */     for (int j2 = j - i; j2 <= j + i; j2++) {
/*     */       
/* 206 */       double d1 = j2 + 0.5D - entityIn.posX;
/*     */       
/* 208 */       for (int l2 = l - i; l2 <= l + i; l2++) {
/*     */         
/* 210 */         double d2 = l2 + 0.5D - entityIn.posZ;
/*     */         
/*     */         int j3;
/* 213 */         label172: for (j3 = this.worldServerInstance.getActualHeight() - 1; j3 >= 0; j3--) {
/*     */           
/* 215 */           if (this.worldServerInstance.isAirBlock((BlockPos)blockpos$mutableblockpos.set(j2, j3, l2))) {
/*     */             
/* 217 */             while (j3 > 0 && this.worldServerInstance.isAirBlock((BlockPos)blockpos$mutableblockpos.set(j2, j3 - 1, l2)))
/*     */             {
/* 219 */               j3--;
/*     */             }
/*     */             
/* 222 */             for (int k3 = i2; k3 < i2 + 4; k3++) {
/*     */               
/* 224 */               int l3 = k3 % 2;
/* 225 */               int i4 = 1 - l3;
/*     */               
/* 227 */               if (k3 % 4 >= 2) {
/*     */                 
/* 229 */                 l3 = -l3;
/* 230 */                 i4 = -i4;
/*     */               } 
/*     */               
/* 233 */               for (int j4 = 0; j4 < 3; j4++) {
/*     */                 
/* 235 */                 for (int k4 = 0; k4 < 4; k4++) {
/*     */                   
/* 237 */                   for (int l4 = -1; l4 < 4; ) {
/*     */                     
/* 239 */                     int i5 = j2 + (k4 - 1) * l3 + j4 * i4;
/* 240 */                     int j5 = j3 + l4;
/* 241 */                     int k5 = l2 + (k4 - 1) * i4 - j4 * l3;
/* 242 */                     blockpos$mutableblockpos.set(i5, j5, k5);
/*     */                     
/* 244 */                     if (l4 >= 0 || this.worldServerInstance.getBlockState((BlockPos)blockpos$mutableblockpos).getBlock().getMaterial().isSolid()) { if (l4 >= 0 && !this.worldServerInstance.isAirBlock((BlockPos)blockpos$mutableblockpos))
/*     */                         continue label172; 
/*     */                       l4++; }
/*     */                     
/*     */                     continue label172;
/*     */                   } 
/*     */                 } 
/*     */               } 
/* 252 */               double d5 = j3 + 0.5D - entityIn.posY;
/* 253 */               double d7 = d1 * d1 + d5 * d5 + d2 * d2;
/*     */               
/* 255 */               if (d0 < 0.0D || d7 < d0) {
/*     */                 
/* 257 */                 d0 = d7;
/* 258 */                 i1 = j2;
/* 259 */                 j1 = j3;
/* 260 */                 k1 = l2;
/* 261 */                 l1 = k3 % 4;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 269 */     if (d0 < 0.0D)
/*     */     {
/* 271 */       for (int l5 = j - i; l5 <= j + i; l5++) {
/*     */         
/* 273 */         double d3 = l5 + 0.5D - entityIn.posX;
/*     */         
/* 275 */         for (int j6 = l - i; j6 <= l + i; j6++) {
/*     */           
/* 277 */           double d4 = j6 + 0.5D - entityIn.posZ;
/*     */           
/*     */           int i7;
/* 280 */           label169: for (i7 = this.worldServerInstance.getActualHeight() - 1; i7 >= 0; i7--) {
/*     */             
/* 282 */             if (this.worldServerInstance.isAirBlock((BlockPos)blockpos$mutableblockpos.set(l5, i7, j6))) {
/*     */               
/* 284 */               while (i7 > 0 && this.worldServerInstance.isAirBlock((BlockPos)blockpos$mutableblockpos.set(l5, i7 - 1, j6)))
/*     */               {
/* 286 */                 i7--;
/*     */               }
/*     */               
/* 289 */               for (int k7 = i2; k7 < i2 + 2; k7++) {
/*     */                 
/* 291 */                 int j8 = k7 % 2;
/* 292 */                 int j9 = 1 - j8;
/*     */                 
/* 294 */                 for (int j10 = 0; j10 < 4; j10++) {
/*     */                   
/* 296 */                   for (int j11 = -1; j11 < 4; ) {
/*     */                     
/* 298 */                     int j12 = l5 + (j10 - 1) * j8;
/* 299 */                     int i13 = i7 + j11;
/* 300 */                     int j13 = j6 + (j10 - 1) * j9;
/* 301 */                     blockpos$mutableblockpos.set(j12, i13, j13);
/*     */                     
/* 303 */                     if (j11 >= 0 || this.worldServerInstance.getBlockState((BlockPos)blockpos$mutableblockpos).getBlock().getMaterial().isSolid()) { if (j11 >= 0 && !this.worldServerInstance.isAirBlock((BlockPos)blockpos$mutableblockpos))
/*     */                         continue label169; 
/*     */                       j11++; }
/*     */                     
/*     */                     continue label169;
/*     */                   } 
/*     */                 } 
/* 310 */                 double d6 = i7 + 0.5D - entityIn.posY;
/* 311 */                 double d8 = d3 * d3 + d6 * d6 + d4 * d4;
/*     */                 
/* 313 */                 if (d0 < 0.0D || d8 < d0) {
/*     */                   
/* 315 */                   d0 = d8;
/* 316 */                   i1 = l5;
/* 317 */                   j1 = i7;
/* 318 */                   k1 = j6;
/* 319 */                   l1 = k7 % 2;
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 328 */     int i6 = i1;
/* 329 */     int k2 = j1;
/* 330 */     int k6 = k1;
/* 331 */     int l6 = l1 % 2;
/* 332 */     int i3 = 1 - l6;
/*     */     
/* 334 */     if (l1 % 4 >= 2) {
/*     */       
/* 336 */       l6 = -l6;
/* 337 */       i3 = -i3;
/*     */     } 
/*     */     
/* 340 */     if (d0 < 0.0D) {
/*     */       
/* 342 */       j1 = MathHelper.clamp_int(j1, 70, this.worldServerInstance.getActualHeight() - 10);
/* 343 */       k2 = j1;
/*     */       
/* 345 */       for (int j7 = -1; j7 <= 1; j7++) {
/*     */         
/* 347 */         for (int l7 = 1; l7 < 3; l7++) {
/*     */           
/* 349 */           for (int k8 = -1; k8 < 3; k8++) {
/*     */             
/* 351 */             int k9 = i6 + (l7 - 1) * l6 + j7 * i3;
/* 352 */             int k10 = k2 + k8;
/* 353 */             int k11 = k6 + (l7 - 1) * i3 - j7 * l6;
/* 354 */             boolean flag = (k8 < 0);
/* 355 */             this.worldServerInstance.setBlockState(new BlockPos(k9, k10, k11), flag ? Blocks.obsidian.getDefaultState() : Blocks.air.getDefaultState());
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 361 */     IBlockState iblockstate = Blocks.portal.getDefaultState().withProperty((IProperty)BlockPortal.AXIS, (l6 != 0) ? (Comparable)EnumFacing.Axis.X : (Comparable)EnumFacing.Axis.Z);
/*     */     
/* 363 */     for (int i8 = 0; i8 < 4; i8++) {
/*     */       
/* 365 */       for (int l8 = 0; l8 < 4; l8++) {
/*     */         
/* 367 */         for (int l9 = -1; l9 < 4; l9++) {
/*     */           
/* 369 */           int l10 = i6 + (l8 - 1) * l6;
/* 370 */           int l11 = k2 + l9;
/* 371 */           int k12 = k6 + (l8 - 1) * i3;
/* 372 */           boolean flag1 = (l8 == 0 || l8 == 3 || l9 == -1 || l9 == 3);
/* 373 */           this.worldServerInstance.setBlockState(new BlockPos(l10, l11, k12), flag1 ? Blocks.obsidian.getDefaultState() : iblockstate, 2);
/*     */         } 
/*     */       } 
/*     */       
/* 377 */       for (int i9 = 0; i9 < 4; i9++) {
/*     */         
/* 379 */         for (int i10 = -1; i10 < 4; i10++) {
/*     */           
/* 381 */           int i11 = i6 + (i9 - 1) * l6;
/* 382 */           int i12 = k2 + i10;
/* 383 */           int l12 = k6 + (i9 - 1) * i3;
/* 384 */           BlockPos blockpos = new BlockPos(i11, i12, l12);
/* 385 */           this.worldServerInstance.notifyNeighborsOfStateChange(blockpos, this.worldServerInstance.getBlockState(blockpos).getBlock());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 390 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeStalePortalLocations(long worldTime) {
/* 395 */     if (worldTime % 100L == 0L) {
/*     */       
/* 397 */       Iterator<Long> iterator = this.destinationCoordinateKeys.iterator();
/* 398 */       long i = worldTime - 300L;
/*     */       
/* 400 */       while (iterator.hasNext()) {
/*     */         
/* 402 */         Long olong = iterator.next();
/* 403 */         PortalPosition teleporter$portalposition = (PortalPosition)this.destinationCoordinateCache.getValueByKey(olong.longValue());
/*     */         
/* 405 */         if (teleporter$portalposition == null || teleporter$portalposition.lastUpdateTime < i) {
/*     */           
/* 407 */           iterator.remove();
/* 408 */           this.destinationCoordinateCache.remove(olong.longValue());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public class PortalPosition
/*     */     extends BlockPos
/*     */   {
/*     */     public long lastUpdateTime;
/*     */     
/*     */     public PortalPosition(BlockPos pos, long lastUpdate) {
/* 420 */       super(pos.getX(), pos.getY(), pos.getZ());
/* 421 */       this.lastUpdateTime = lastUpdate;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\Teleporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */