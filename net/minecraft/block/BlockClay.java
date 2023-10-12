/*    */ package net.minecraft.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.Item;
/*    */ 
/*    */ public class BlockClay
/*    */   extends Block
/*    */ {
/*    */   public BlockClay() {
/* 14 */     super(Material.clay);
/* 15 */     setCreativeTab(CreativeTabs.tabBlock);
/*    */   }
/*    */ 
/*    */   
/*    */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/* 20 */     return Items.clay_ball;
/*    */   }
/*    */ 
/*    */   
/*    */   public int quantityDropped(Random random) {
/* 25 */     return 4;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockClay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */