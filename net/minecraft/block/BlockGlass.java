/*    */ package net.minecraft.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.util.EnumWorldBlockLayer;
/*    */ 
/*    */ public class BlockGlass
/*    */   extends BlockBreakable
/*    */ {
/*    */   public BlockGlass(Material materialIn, boolean ignoreSimilarity) {
/* 12 */     super(materialIn, ignoreSimilarity);
/* 13 */     setCreativeTab(CreativeTabs.tabBlock);
/*    */   }
/*    */ 
/*    */   
/*    */   public int quantityDropped(Random random) {
/* 18 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public EnumWorldBlockLayer getBlockLayer() {
/* 23 */     return EnumWorldBlockLayer.CUTOUT;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isFullCube() {
/* 28 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canSilkHarvest() {
/* 33 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockGlass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */