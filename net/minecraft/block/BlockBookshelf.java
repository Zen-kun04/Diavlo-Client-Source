/*    */ package net.minecraft.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.Item;
/*    */ 
/*    */ public class BlockBookshelf
/*    */   extends Block
/*    */ {
/*    */   public BlockBookshelf() {
/* 14 */     super(Material.wood);
/* 15 */     setCreativeTab(CreativeTabs.tabBlock);
/*    */   }
/*    */ 
/*    */   
/*    */   public int quantityDropped(Random random) {
/* 20 */     return 3;
/*    */   }
/*    */ 
/*    */   
/*    */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/* 25 */     return Items.book;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockBookshelf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */