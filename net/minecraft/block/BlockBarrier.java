/*    */ package net.minecraft.block;
/*    */ 
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockBarrier
/*    */   extends Block
/*    */ {
/*    */   protected BlockBarrier() {
/* 12 */     super(Material.barrier);
/* 13 */     setBlockUnbreakable();
/* 14 */     setResistance(6000001.0F);
/* 15 */     disableStats();
/* 16 */     this.translucent = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRenderType() {
/* 21 */     return -1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isOpaqueCube() {
/* 26 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getAmbientOcclusionLightValue() {
/* 31 */     return 1.0F;
/*    */   }
/*    */   
/*    */   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {}
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockBarrier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */