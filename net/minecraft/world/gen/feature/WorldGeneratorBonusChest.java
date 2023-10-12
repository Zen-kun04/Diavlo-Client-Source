/*    */ package net.minecraft.world.gen.feature;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.inventory.IInventory;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.WeightedRandomChestContent;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class WorldGeneratorBonusChest
/*    */   extends WorldGenerator {
/*    */   private final List<WeightedRandomChestContent> chestItems;
/*    */   private final int itemsToGenerateInBonusChest;
/*    */   
/*    */   public WorldGeneratorBonusChest(List<WeightedRandomChestContent> p_i45634_1_, int p_i45634_2_) {
/* 21 */     this.chestItems = p_i45634_1_;
/* 22 */     this.itemsToGenerateInBonusChest = p_i45634_2_;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean generate(World worldIn, Random rand, BlockPos position) {
/*    */     Block block;
/* 29 */     while (((block = worldIn.getBlockState(position).getBlock()).getMaterial() == Material.air || block.getMaterial() == Material.leaves) && position.getY() > 1)
/*    */     {
/* 31 */       position = position.down();
/*    */     }
/*    */     
/* 34 */     if (position.getY() < 1)
/*    */     {
/* 36 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 40 */     position = position.up();
/*    */     
/* 42 */     for (int i = 0; i < 4; i++) {
/*    */       
/* 44 */       BlockPos blockpos = position.add(rand.nextInt(4) - rand.nextInt(4), rand.nextInt(3) - rand.nextInt(3), rand.nextInt(4) - rand.nextInt(4));
/*    */       
/* 46 */       if (worldIn.isAirBlock(blockpos) && World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, blockpos.down())) {
/*    */         
/* 48 */         worldIn.setBlockState(blockpos, Blocks.chest.getDefaultState(), 2);
/* 49 */         TileEntity tileentity = worldIn.getTileEntity(blockpos);
/*    */         
/* 51 */         if (tileentity instanceof net.minecraft.tileentity.TileEntityChest)
/*    */         {
/* 53 */           WeightedRandomChestContent.generateChestContents(rand, this.chestItems, (IInventory)tileentity, this.itemsToGenerateInBonusChest);
/*    */         }
/*    */         
/* 56 */         BlockPos blockpos1 = blockpos.east();
/* 57 */         BlockPos blockpos2 = blockpos.west();
/* 58 */         BlockPos blockpos3 = blockpos.north();
/* 59 */         BlockPos blockpos4 = blockpos.south();
/*    */         
/* 61 */         if (worldIn.isAirBlock(blockpos2) && World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, blockpos2.down()))
/*    */         {
/* 63 */           worldIn.setBlockState(blockpos2, Blocks.torch.getDefaultState(), 2);
/*    */         }
/*    */         
/* 66 */         if (worldIn.isAirBlock(blockpos1) && World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, blockpos1.down()))
/*    */         {
/* 68 */           worldIn.setBlockState(blockpos1, Blocks.torch.getDefaultState(), 2);
/*    */         }
/*    */         
/* 71 */         if (worldIn.isAirBlock(blockpos3) && World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, blockpos3.down()))
/*    */         {
/* 73 */           worldIn.setBlockState(blockpos3, Blocks.torch.getDefaultState(), 2);
/*    */         }
/*    */         
/* 76 */         if (worldIn.isAirBlock(blockpos4) && World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, blockpos4.down()))
/*    */         {
/* 78 */           worldIn.setBlockState(blockpos4, Blocks.torch.getDefaultState(), 2);
/*    */         }
/*    */         
/* 81 */         return true;
/*    */       } 
/*    */     } 
/*    */     
/* 85 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\feature\WorldGeneratorBonusChest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */