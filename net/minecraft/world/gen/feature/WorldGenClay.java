/*    */ package net.minecraft.world.gen.feature;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class WorldGenClay
/*    */   extends WorldGenerator {
/* 12 */   private Block field_150546_a = Blocks.clay;
/*    */   
/*    */   private int numberOfBlocks;
/*    */   
/*    */   public WorldGenClay(int p_i2011_1_) {
/* 17 */     this.numberOfBlocks = p_i2011_1_;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean generate(World worldIn, Random rand, BlockPos position) {
/* 22 */     if (worldIn.getBlockState(position).getBlock().getMaterial() != Material.water)
/*    */     {
/* 24 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 28 */     int i = rand.nextInt(this.numberOfBlocks - 2) + 2;
/* 29 */     int j = 1;
/*    */     
/* 31 */     for (int k = position.getX() - i; k <= position.getX() + i; k++) {
/*    */       
/* 33 */       for (int l = position.getZ() - i; l <= position.getZ() + i; l++) {
/*    */         
/* 35 */         int i1 = k - position.getX();
/* 36 */         int j1 = l - position.getZ();
/*    */         
/* 38 */         if (i1 * i1 + j1 * j1 <= i * i)
/*    */         {
/* 40 */           for (int k1 = position.getY() - j; k1 <= position.getY() + j; k1++) {
/*    */             
/* 42 */             BlockPos blockpos = new BlockPos(k, k1, l);
/* 43 */             Block block = worldIn.getBlockState(blockpos).getBlock();
/*    */             
/* 45 */             if (block == Blocks.dirt || block == Blocks.clay)
/*    */             {
/* 47 */               worldIn.setBlockState(blockpos, this.field_150546_a.getDefaultState(), 2);
/*    */             }
/*    */           } 
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 54 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\feature\WorldGenClay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */