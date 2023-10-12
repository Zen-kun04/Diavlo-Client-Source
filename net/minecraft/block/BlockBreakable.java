/*    */ package net.minecraft.block;
/*    */ 
/*    */ import net.minecraft.block.material.MapColor;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ 
/*    */ public class BlockBreakable
/*    */   extends Block
/*    */ {
/*    */   private boolean ignoreSimilarity;
/*    */   
/*    */   protected BlockBreakable(Material materialIn, boolean ignoreSimilarityIn) {
/* 17 */     this(materialIn, ignoreSimilarityIn, materialIn.getMaterialMapColor());
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockBreakable(Material p_i46393_1_, boolean p_i46393_2_, MapColor p_i46393_3_) {
/* 22 */     super(p_i46393_1_, p_i46393_3_);
/* 23 */     this.ignoreSimilarity = p_i46393_2_;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isOpaqueCube() {
/* 28 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
/* 33 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/* 34 */     Block block = iblockstate.getBlock();
/*    */     
/* 36 */     if (this == Blocks.glass || this == Blocks.stained_glass) {
/*    */       
/* 38 */       if (worldIn.getBlockState(pos.offset(side.getOpposite())) != iblockstate)
/*    */       {
/* 40 */         return true;
/*    */       }
/*    */       
/* 43 */       if (block == this)
/*    */       {
/* 45 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 49 */     return (!this.ignoreSimilarity && block == this) ? false : super.shouldSideBeRendered(worldIn, pos, side);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockBreakable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */