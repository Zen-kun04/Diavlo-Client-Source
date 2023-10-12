/*    */ package net.minecraft.world.gen.feature;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class WorldGenSand
/*    */   extends WorldGenerator
/*    */ {
/*    */   private Block block;
/*    */   private int radius;
/*    */   
/*    */   public WorldGenSand(Block p_i45462_1_, int p_i45462_2_) {
/* 17 */     this.block = p_i45462_1_;
/* 18 */     this.radius = p_i45462_2_;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean generate(World worldIn, Random rand, BlockPos position) {
/* 23 */     if (worldIn.getBlockState(position).getBlock().getMaterial() != Material.water)
/*    */     {
/* 25 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 29 */     int i = rand.nextInt(this.radius - 2) + 2;
/* 30 */     int j = 2;
/*    */     
/* 32 */     for (int k = position.getX() - i; k <= position.getX() + i; k++) {
/*    */       
/* 34 */       for (int l = position.getZ() - i; l <= position.getZ() + i; l++) {
/*    */         
/* 36 */         int i1 = k - position.getX();
/* 37 */         int j1 = l - position.getZ();
/*    */         
/* 39 */         if (i1 * i1 + j1 * j1 <= i * i)
/*    */         {
/* 41 */           for (int k1 = position.getY() - j; k1 <= position.getY() + j; k1++) {
/*    */             
/* 43 */             BlockPos blockpos = new BlockPos(k, k1, l);
/* 44 */             Block block = worldIn.getBlockState(blockpos).getBlock();
/*    */             
/* 46 */             if (block == Blocks.dirt || block == Blocks.grass)
/*    */             {
/* 48 */               worldIn.setBlockState(blockpos, this.block.getDefaultState(), 2);
/*    */             }
/*    */           } 
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 55 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\feature\WorldGenSand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */