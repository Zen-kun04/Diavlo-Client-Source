/*    */ package net.minecraft.world.gen.feature;
/*    */ 
/*    */ import com.google.common.base.Predicate;
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.block.state.pattern.BlockHelper;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class WorldGenMinable
/*    */   extends WorldGenerator
/*    */ {
/*    */   private final IBlockState oreBlock;
/*    */   private final int numberOfBlocks;
/*    */   private final Predicate<IBlockState> predicate;
/*    */   
/*    */   public WorldGenMinable(IBlockState state, int blockCount) {
/* 20 */     this(state, blockCount, (Predicate<IBlockState>)BlockHelper.forBlock(Blocks.stone));
/*    */   }
/*    */ 
/*    */   
/*    */   public WorldGenMinable(IBlockState state, int blockCount, Predicate<IBlockState> p_i45631_3_) {
/* 25 */     this.oreBlock = state;
/* 26 */     this.numberOfBlocks = blockCount;
/* 27 */     this.predicate = p_i45631_3_;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean generate(World worldIn, Random rand, BlockPos position) {
/* 32 */     float f = rand.nextFloat() * 3.1415927F;
/* 33 */     double d0 = ((position.getX() + 8) + MathHelper.sin(f) * this.numberOfBlocks / 8.0F);
/* 34 */     double d1 = ((position.getX() + 8) - MathHelper.sin(f) * this.numberOfBlocks / 8.0F);
/* 35 */     double d2 = ((position.getZ() + 8) + MathHelper.cos(f) * this.numberOfBlocks / 8.0F);
/* 36 */     double d3 = ((position.getZ() + 8) - MathHelper.cos(f) * this.numberOfBlocks / 8.0F);
/* 37 */     double d4 = (position.getY() + rand.nextInt(3) - 2);
/* 38 */     double d5 = (position.getY() + rand.nextInt(3) - 2);
/*    */     
/* 40 */     for (int i = 0; i < this.numberOfBlocks; i++) {
/*    */       
/* 42 */       float f1 = i / this.numberOfBlocks;
/* 43 */       double d6 = d0 + (d1 - d0) * f1;
/* 44 */       double d7 = d4 + (d5 - d4) * f1;
/* 45 */       double d8 = d2 + (d3 - d2) * f1;
/* 46 */       double d9 = rand.nextDouble() * this.numberOfBlocks / 16.0D;
/* 47 */       double d10 = (MathHelper.sin(3.1415927F * f1) + 1.0F) * d9 + 1.0D;
/* 48 */       double d11 = (MathHelper.sin(3.1415927F * f1) + 1.0F) * d9 + 1.0D;
/* 49 */       int j = MathHelper.floor_double(d6 - d10 / 2.0D);
/* 50 */       int k = MathHelper.floor_double(d7 - d11 / 2.0D);
/* 51 */       int l = MathHelper.floor_double(d8 - d10 / 2.0D);
/* 52 */       int i1 = MathHelper.floor_double(d6 + d10 / 2.0D);
/* 53 */       int j1 = MathHelper.floor_double(d7 + d11 / 2.0D);
/* 54 */       int k1 = MathHelper.floor_double(d8 + d10 / 2.0D);
/*    */       
/* 56 */       for (int l1 = j; l1 <= i1; l1++) {
/*    */         
/* 58 */         double d12 = (l1 + 0.5D - d6) / d10 / 2.0D;
/*    */         
/* 60 */         if (d12 * d12 < 1.0D)
/*    */         {
/* 62 */           for (int i2 = k; i2 <= j1; i2++) {
/*    */             
/* 64 */             double d13 = (i2 + 0.5D - d7) / d11 / 2.0D;
/*    */             
/* 66 */             if (d12 * d12 + d13 * d13 < 1.0D)
/*    */             {
/* 68 */               for (int j2 = l; j2 <= k1; j2++) {
/*    */                 
/* 70 */                 double d14 = (j2 + 0.5D - d8) / d10 / 2.0D;
/*    */                 
/* 72 */                 if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D) {
/*    */                   
/* 74 */                   BlockPos blockpos = new BlockPos(l1, i2, j2);
/*    */                   
/* 76 */                   if (this.predicate.apply(worldIn.getBlockState(blockpos)))
/*    */                   {
/* 78 */                     worldIn.setBlockState(blockpos, this.oreBlock, 2);
/*    */                   }
/*    */                 } 
/*    */               } 
/*    */             }
/*    */           } 
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 88 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\feature\WorldGenMinable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */