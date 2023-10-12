/*    */ package net.minecraft.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.EnumSkyBlock;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockSnowBlock
/*    */   extends Block
/*    */ {
/*    */   protected BlockSnowBlock() {
/* 17 */     super(Material.craftedSnow);
/* 18 */     setTickRandomly(true);
/* 19 */     setCreativeTab(CreativeTabs.tabBlock);
/*    */   }
/*    */ 
/*    */   
/*    */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/* 24 */     return Items.snowball;
/*    */   }
/*    */ 
/*    */   
/*    */   public int quantityDropped(Random random) {
/* 29 */     return 4;
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/* 34 */     if (worldIn.getLightFor(EnumSkyBlock.BLOCK, pos) > 11) {
/*    */       
/* 36 */       dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
/* 37 */       worldIn.setBlockToAir(pos);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockSnowBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */