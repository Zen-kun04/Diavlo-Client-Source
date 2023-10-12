/*    */ package net.minecraft.block;
/*    */ 
/*    */ import net.minecraft.block.material.MapColor;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ 
/*    */ public class BlockNetherrack
/*    */   extends Block
/*    */ {
/*    */   public BlockNetherrack() {
/* 12 */     super(Material.rock);
/* 13 */     setCreativeTab(CreativeTabs.tabBlock);
/*    */   }
/*    */ 
/*    */   
/*    */   public MapColor getMapColor(IBlockState state) {
/* 18 */     return MapColor.netherrackColor;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockNetherrack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */