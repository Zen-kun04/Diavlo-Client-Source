/*    */ package net.minecraft.block;
/*    */ 
/*    */ import java.util.IdentityHashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.util.AxisAlignedBB;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockAir
/*    */   extends Block {
/* 13 */   private static Map mapOriginalOpacity = new IdentityHashMap<>();
/*    */ 
/*    */   
/*    */   protected BlockAir() {
/* 17 */     super(Material.air);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRenderType() {
/* 22 */     return -1;
/*    */   }
/*    */ 
/*    */   
/*    */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
/* 27 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isOpaqueCube() {
/* 32 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
/* 37 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {}
/*    */ 
/*    */   
/*    */   public boolean isReplaceable(World worldIn, BlockPos pos) {
/* 46 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public static void setLightOpacity(Block p_setLightOpacity_0_, int p_setLightOpacity_1_) {
/* 51 */     if (!mapOriginalOpacity.containsKey(p_setLightOpacity_0_))
/*    */     {
/* 53 */       mapOriginalOpacity.put(p_setLightOpacity_0_, Integer.valueOf(p_setLightOpacity_0_.lightOpacity));
/*    */     }
/*    */     
/* 56 */     p_setLightOpacity_0_.lightOpacity = p_setLightOpacity_1_;
/*    */   }
/*    */ 
/*    */   
/*    */   public static void restoreLightOpacity(Block p_restoreLightOpacity_0_) {
/* 61 */     if (mapOriginalOpacity.containsKey(p_restoreLightOpacity_0_)) {
/*    */       
/* 63 */       int i = ((Integer)mapOriginalOpacity.get(p_restoreLightOpacity_0_)).intValue();
/* 64 */       setLightOpacity(p_restoreLightOpacity_0_, i);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockAir.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */