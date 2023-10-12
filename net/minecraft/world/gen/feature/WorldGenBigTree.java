/*     */ package net.minecraft.world.gen.feature;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockLeaves;
/*     */ import net.minecraft.block.BlockLog;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class WorldGenBigTree extends WorldGenAbstractTree {
/*     */   private Random rand;
/*     */   private World world;
/*  20 */   private BlockPos basePos = BlockPos.ORIGIN;
/*     */   int heightLimit;
/*     */   int height;
/*  23 */   double heightAttenuation = 0.618D;
/*  24 */   double branchSlope = 0.381D;
/*  25 */   double scaleWidth = 1.0D;
/*  26 */   double leafDensity = 1.0D;
/*  27 */   int trunkSize = 1;
/*  28 */   int heightLimitLimit = 12;
/*  29 */   int leafDistanceLimit = 4;
/*     */   
/*     */   List<FoliageCoordinates> field_175948_j;
/*     */   
/*     */   public WorldGenBigTree(boolean p_i2008_1_) {
/*  34 */     super(p_i2008_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   void generateLeafNodeList() {
/*  39 */     this.height = (int)(this.heightLimit * this.heightAttenuation);
/*     */     
/*  41 */     if (this.height >= this.heightLimit)
/*     */     {
/*  43 */       this.height = this.heightLimit - 1;
/*     */     }
/*     */     
/*  46 */     int i = (int)(1.382D + Math.pow(this.leafDensity * this.heightLimit / 13.0D, 2.0D));
/*     */     
/*  48 */     if (i < 1)
/*     */     {
/*  50 */       i = 1;
/*     */     }
/*     */     
/*  53 */     int j = this.basePos.getY() + this.height;
/*  54 */     int k = this.heightLimit - this.leafDistanceLimit;
/*  55 */     this.field_175948_j = Lists.newArrayList();
/*  56 */     this.field_175948_j.add(new FoliageCoordinates(this.basePos.up(k), j));
/*     */     
/*  58 */     for (; k >= 0; k--) {
/*     */       
/*  60 */       float f = layerSize(k);
/*     */       
/*  62 */       if (f >= 0.0F)
/*     */       {
/*  64 */         for (int l = 0; l < i; l++) {
/*     */           
/*  66 */           double d0 = this.scaleWidth * f * (this.rand.nextFloat() + 0.328D);
/*  67 */           double d1 = (this.rand.nextFloat() * 2.0F) * Math.PI;
/*  68 */           double d2 = d0 * Math.sin(d1) + 0.5D;
/*  69 */           double d3 = d0 * Math.cos(d1) + 0.5D;
/*  70 */           BlockPos blockpos = this.basePos.add(d2, (k - 1), d3);
/*  71 */           BlockPos blockpos1 = blockpos.up(this.leafDistanceLimit);
/*     */           
/*  73 */           if (checkBlockLine(blockpos, blockpos1) == -1) {
/*     */             
/*  75 */             int i1 = this.basePos.getX() - blockpos.getX();
/*  76 */             int j1 = this.basePos.getZ() - blockpos.getZ();
/*  77 */             double d4 = blockpos.getY() - Math.sqrt((i1 * i1 + j1 * j1)) * this.branchSlope;
/*  78 */             int k1 = (d4 > j) ? j : (int)d4;
/*  79 */             BlockPos blockpos2 = new BlockPos(this.basePos.getX(), k1, this.basePos.getZ());
/*     */             
/*  81 */             if (checkBlockLine(blockpos2, blockpos) == -1)
/*     */             {
/*  83 */               this.field_175948_j.add(new FoliageCoordinates(blockpos, blockpos2.getY()));
/*     */             }
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void func_181631_a(BlockPos p_181631_1_, float p_181631_2_, IBlockState p_181631_3_) {
/*  93 */     int i = (int)(p_181631_2_ + 0.618D);
/*     */     
/*  95 */     for (int j = -i; j <= i; j++) {
/*     */       
/*  97 */       for (int k = -i; k <= i; k++) {
/*     */         
/*  99 */         if (Math.pow(Math.abs(j) + 0.5D, 2.0D) + Math.pow(Math.abs(k) + 0.5D, 2.0D) <= (p_181631_2_ * p_181631_2_)) {
/*     */           
/* 101 */           BlockPos blockpos = p_181631_1_.add(j, 0, k);
/* 102 */           Material material = this.world.getBlockState(blockpos).getBlock().getMaterial();
/*     */           
/* 104 */           if (material == Material.air || material == Material.leaves)
/*     */           {
/* 106 */             setBlockAndNotifyAdequately(this.world, blockpos, p_181631_3_);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   float layerSize(int p_76490_1_) {
/* 115 */     if (p_76490_1_ < this.heightLimit * 0.3F)
/*     */     {
/* 117 */       return -1.0F;
/*     */     }
/*     */ 
/*     */     
/* 121 */     float f = this.heightLimit / 2.0F;
/* 122 */     float f1 = f - p_76490_1_;
/* 123 */     float f2 = MathHelper.sqrt_float(f * f - f1 * f1);
/*     */     
/* 125 */     if (f1 == 0.0F) {
/*     */       
/* 127 */       f2 = f;
/*     */     }
/* 129 */     else if (Math.abs(f1) >= f) {
/*     */       
/* 131 */       return 0.0F;
/*     */     } 
/*     */     
/* 134 */     return f2 * 0.5F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   float leafSize(int p_76495_1_) {
/* 140 */     return (p_76495_1_ >= 0 && p_76495_1_ < this.leafDistanceLimit) ? ((p_76495_1_ != 0 && p_76495_1_ != this.leafDistanceLimit - 1) ? 3.0F : 2.0F) : -1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   void generateLeafNode(BlockPos pos) {
/* 145 */     for (int i = 0; i < this.leafDistanceLimit; i++)
/*     */     {
/* 147 */       func_181631_a(pos.up(i), leafSize(i), Blocks.leaves.getDefaultState().withProperty((IProperty)BlockLeaves.CHECK_DECAY, Boolean.valueOf(false)));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void func_175937_a(BlockPos p_175937_1_, BlockPos p_175937_2_, Block p_175937_3_) {
/* 153 */     BlockPos blockpos = p_175937_2_.add(-p_175937_1_.getX(), -p_175937_1_.getY(), -p_175937_1_.getZ());
/* 154 */     int i = getGreatestDistance(blockpos);
/* 155 */     float f = blockpos.getX() / i;
/* 156 */     float f1 = blockpos.getY() / i;
/* 157 */     float f2 = blockpos.getZ() / i;
/*     */     
/* 159 */     for (int j = 0; j <= i; j++) {
/*     */       
/* 161 */       BlockPos blockpos1 = p_175937_1_.add((0.5F + j * f), (0.5F + j * f1), (0.5F + j * f2));
/* 162 */       BlockLog.EnumAxis blocklog$enumaxis = func_175938_b(p_175937_1_, blockpos1);
/* 163 */       setBlockAndNotifyAdequately(this.world, blockpos1, p_175937_3_.getDefaultState().withProperty((IProperty)BlockLog.LOG_AXIS, (Comparable)blocklog$enumaxis));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int getGreatestDistance(BlockPos posIn) {
/* 169 */     int i = MathHelper.abs_int(posIn.getX());
/* 170 */     int j = MathHelper.abs_int(posIn.getY());
/* 171 */     int k = MathHelper.abs_int(posIn.getZ());
/* 172 */     return (k > i && k > j) ? k : ((j > i) ? j : i);
/*     */   }
/*     */ 
/*     */   
/*     */   private BlockLog.EnumAxis func_175938_b(BlockPos p_175938_1_, BlockPos p_175938_2_) {
/* 177 */     BlockLog.EnumAxis blocklog$enumaxis = BlockLog.EnumAxis.Y;
/* 178 */     int i = Math.abs(p_175938_2_.getX() - p_175938_1_.getX());
/* 179 */     int j = Math.abs(p_175938_2_.getZ() - p_175938_1_.getZ());
/* 180 */     int k = Math.max(i, j);
/*     */     
/* 182 */     if (k > 0)
/*     */     {
/* 184 */       if (i == k) {
/*     */         
/* 186 */         blocklog$enumaxis = BlockLog.EnumAxis.X;
/*     */       }
/* 188 */       else if (j == k) {
/*     */         
/* 190 */         blocklog$enumaxis = BlockLog.EnumAxis.Z;
/*     */       } 
/*     */     }
/*     */     
/* 194 */     return blocklog$enumaxis;
/*     */   }
/*     */ 
/*     */   
/*     */   void generateLeaves() {
/* 199 */     for (FoliageCoordinates worldgenbigtree$foliagecoordinates : this.field_175948_j)
/*     */     {
/* 201 */       generateLeafNode(worldgenbigtree$foliagecoordinates);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   boolean leafNodeNeedsBase(int p_76493_1_) {
/* 207 */     return (p_76493_1_ >= this.heightLimit * 0.2D);
/*     */   }
/*     */ 
/*     */   
/*     */   void generateTrunk() {
/* 212 */     BlockPos blockpos = this.basePos;
/* 213 */     BlockPos blockpos1 = this.basePos.up(this.height);
/* 214 */     Block block = Blocks.log;
/* 215 */     func_175937_a(blockpos, blockpos1, block);
/*     */     
/* 217 */     if (this.trunkSize == 2) {
/*     */       
/* 219 */       func_175937_a(blockpos.east(), blockpos1.east(), block);
/* 220 */       func_175937_a(blockpos.east().south(), blockpos1.east().south(), block);
/* 221 */       func_175937_a(blockpos.south(), blockpos1.south(), block);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void generateLeafNodeBases() {
/* 227 */     for (FoliageCoordinates worldgenbigtree$foliagecoordinates : this.field_175948_j) {
/*     */       
/* 229 */       int i = worldgenbigtree$foliagecoordinates.func_177999_q();
/* 230 */       BlockPos blockpos = new BlockPos(this.basePos.getX(), i, this.basePos.getZ());
/*     */       
/* 232 */       if (!blockpos.equals(worldgenbigtree$foliagecoordinates) && leafNodeNeedsBase(i - this.basePos.getY()))
/*     */       {
/* 234 */         func_175937_a(blockpos, worldgenbigtree$foliagecoordinates, Blocks.log);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   int checkBlockLine(BlockPos posOne, BlockPos posTwo) {
/* 241 */     BlockPos blockpos = posTwo.add(-posOne.getX(), -posOne.getY(), -posOne.getZ());
/* 242 */     int i = getGreatestDistance(blockpos);
/* 243 */     float f = blockpos.getX() / i;
/* 244 */     float f1 = blockpos.getY() / i;
/* 245 */     float f2 = blockpos.getZ() / i;
/*     */     
/* 247 */     if (i == 0)
/*     */     {
/* 249 */       return -1;
/*     */     }
/*     */ 
/*     */     
/* 253 */     for (int j = 0; j <= i; j++) {
/*     */       
/* 255 */       BlockPos blockpos1 = posOne.add((0.5F + j * f), (0.5F + j * f1), (0.5F + j * f2));
/*     */       
/* 257 */       if (!func_150523_a(this.world.getBlockState(blockpos1).getBlock()))
/*     */       {
/* 259 */         return j;
/*     */       }
/*     */     } 
/*     */     
/* 263 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_175904_e() {
/* 269 */     this.leafDistanceLimit = 5;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean generate(World worldIn, Random rand, BlockPos position) {
/* 274 */     this.world = worldIn;
/* 275 */     this.basePos = position;
/* 276 */     this.rand = new Random(rand.nextLong());
/*     */     
/* 278 */     if (this.heightLimit == 0)
/*     */     {
/* 280 */       this.heightLimit = 5 + this.rand.nextInt(this.heightLimitLimit);
/*     */     }
/*     */     
/* 283 */     if (!validTreeLocation())
/*     */     {
/* 285 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 289 */     generateLeafNodeList();
/* 290 */     generateLeaves();
/* 291 */     generateTrunk();
/* 292 */     generateLeafNodeBases();
/* 293 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean validTreeLocation() {
/* 299 */     Block block = this.world.getBlockState(this.basePos.down()).getBlock();
/*     */     
/* 301 */     if (block != Blocks.dirt && block != Blocks.grass && block != Blocks.farmland)
/*     */     {
/* 303 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 307 */     int i = checkBlockLine(this.basePos, this.basePos.up(this.heightLimit - 1));
/*     */     
/* 309 */     if (i == -1)
/*     */     {
/* 311 */       return true;
/*     */     }
/* 313 */     if (i < 6)
/*     */     {
/* 315 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 319 */     this.heightLimit = i;
/* 320 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   static class FoliageCoordinates
/*     */     extends BlockPos
/*     */   {
/*     */     private final int field_178000_b;
/*     */ 
/*     */     
/*     */     public FoliageCoordinates(BlockPos p_i45635_1_, int p_i45635_2_) {
/* 331 */       super(p_i45635_1_.getX(), p_i45635_1_.getY(), p_i45635_1_.getZ());
/* 332 */       this.field_178000_b = p_i45635_2_;
/*     */     }
/*     */ 
/*     */     
/*     */     public int func_177999_q() {
/* 337 */       return this.field_178000_b;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\feature\WorldGenBigTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */