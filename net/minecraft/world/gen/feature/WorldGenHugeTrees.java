/*     */ package net.minecraft.world.gen.feature;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class WorldGenHugeTrees
/*     */   extends WorldGenAbstractTree
/*     */ {
/*     */   protected final int baseHeight;
/*     */   protected final IBlockState woodMetadata;
/*     */   protected final IBlockState leavesMetadata;
/*     */   protected int extraRandomHeight;
/*     */   
/*     */   public WorldGenHugeTrees(boolean p_i46447_1_, int p_i46447_2_, int p_i46447_3_, IBlockState p_i46447_4_, IBlockState p_i46447_5_) {
/*  20 */     super(p_i46447_1_);
/*  21 */     this.baseHeight = p_i46447_2_;
/*  22 */     this.extraRandomHeight = p_i46447_3_;
/*  23 */     this.woodMetadata = p_i46447_4_;
/*  24 */     this.leavesMetadata = p_i46447_5_;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int func_150533_a(Random p_150533_1_) {
/*  29 */     int i = p_150533_1_.nextInt(3) + this.baseHeight;
/*     */     
/*  31 */     if (this.extraRandomHeight > 1)
/*     */     {
/*  33 */       i += p_150533_1_.nextInt(this.extraRandomHeight);
/*     */     }
/*     */     
/*  36 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean func_175926_c(World worldIn, BlockPos p_175926_2_, int p_175926_3_) {
/*  41 */     boolean flag = true;
/*     */     
/*  43 */     if (p_175926_2_.getY() >= 1 && p_175926_2_.getY() + p_175926_3_ + 1 <= 256) {
/*     */       
/*  45 */       for (int i = 0; i <= 1 + p_175926_3_; i++) {
/*     */         
/*  47 */         int j = 2;
/*     */         
/*  49 */         if (i == 0) {
/*     */           
/*  51 */           j = 1;
/*     */         }
/*  53 */         else if (i >= 1 + p_175926_3_ - 2) {
/*     */           
/*  55 */           j = 2;
/*     */         } 
/*     */         
/*  58 */         for (int k = -j; k <= j && flag; k++) {
/*     */           
/*  60 */           for (int l = -j; l <= j && flag; l++) {
/*     */             
/*  62 */             if (p_175926_2_.getY() + i < 0 || p_175926_2_.getY() + i >= 256 || !func_150523_a(worldIn.getBlockState(p_175926_2_.add(k, i, l)).getBlock()))
/*     */             {
/*  64 */               flag = false;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/*  70 */       return flag;
/*     */     } 
/*     */ 
/*     */     
/*  74 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean func_175927_a(BlockPos p_175927_1_, World worldIn) {
/*  80 */     BlockPos blockpos = p_175927_1_.down();
/*  81 */     Block block = worldIn.getBlockState(blockpos).getBlock();
/*     */     
/*  83 */     if ((block == Blocks.grass || block == Blocks.dirt) && p_175927_1_.getY() >= 2) {
/*     */       
/*  85 */       func_175921_a(worldIn, blockpos);
/*  86 */       func_175921_a(worldIn, blockpos.east());
/*  87 */       func_175921_a(worldIn, blockpos.south());
/*  88 */       func_175921_a(worldIn, blockpos.south().east());
/*  89 */       return true;
/*     */     } 
/*     */ 
/*     */     
/*  93 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean func_175929_a(World worldIn, Random p_175929_2_, BlockPos p_175929_3_, int p_175929_4_) {
/*  99 */     return (func_175926_c(worldIn, p_175929_3_, p_175929_4_) && func_175927_a(p_175929_3_, worldIn));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_175925_a(World worldIn, BlockPos p_175925_2_, int p_175925_3_) {
/* 104 */     int i = p_175925_3_ * p_175925_3_;
/*     */     
/* 106 */     for (int j = -p_175925_3_; j <= p_175925_3_ + 1; j++) {
/*     */       
/* 108 */       for (int k = -p_175925_3_; k <= p_175925_3_ + 1; k++) {
/*     */         
/* 110 */         int l = j - 1;
/* 111 */         int i1 = k - 1;
/*     */         
/* 113 */         if (j * j + k * k <= i || l * l + i1 * i1 <= i || j * j + i1 * i1 <= i || l * l + k * k <= i) {
/*     */           
/* 115 */           BlockPos blockpos = p_175925_2_.add(j, 0, k);
/* 116 */           Material material = worldIn.getBlockState(blockpos).getBlock().getMaterial();
/*     */           
/* 118 */           if (material == Material.air || material == Material.leaves)
/*     */           {
/* 120 */             setBlockAndNotifyAdequately(worldIn, blockpos, this.leavesMetadata);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_175928_b(World worldIn, BlockPos p_175928_2_, int p_175928_3_) {
/* 129 */     int i = p_175928_3_ * p_175928_3_;
/*     */     
/* 131 */     for (int j = -p_175928_3_; j <= p_175928_3_; j++) {
/*     */       
/* 133 */       for (int k = -p_175928_3_; k <= p_175928_3_; k++) {
/*     */         
/* 135 */         if (j * j + k * k <= i) {
/*     */           
/* 137 */           BlockPos blockpos = p_175928_2_.add(j, 0, k);
/* 138 */           Material material = worldIn.getBlockState(blockpos).getBlock().getMaterial();
/*     */           
/* 140 */           if (material == Material.air || material == Material.leaves)
/*     */           {
/* 142 */             setBlockAndNotifyAdequately(worldIn, blockpos, this.leavesMetadata);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\feature\WorldGenHugeTrees.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */