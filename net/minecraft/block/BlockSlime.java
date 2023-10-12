/*    */ package net.minecraft.block;
/*    */ 
/*    */ import net.minecraft.block.material.MapColor;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumWorldBlockLayer;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockSlime
/*    */   extends BlockBreakable
/*    */ {
/*    */   public BlockSlime() {
/* 15 */     super(Material.clay, false, MapColor.grassColor);
/* 16 */     setCreativeTab(CreativeTabs.tabDecorations);
/* 17 */     this.slipperiness = 0.8F;
/*    */   }
/*    */ 
/*    */   
/*    */   public EnumWorldBlockLayer getBlockLayer() {
/* 22 */     return EnumWorldBlockLayer.TRANSLUCENT;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
/* 27 */     if (entityIn.isSneaking()) {
/*    */       
/* 29 */       super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
/*    */     }
/*    */     else {
/*    */       
/* 33 */       entityIn.fall(fallDistance, 0.0F);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onLanded(World worldIn, Entity entityIn) {
/* 39 */     if (entityIn.isSneaking()) {
/*    */       
/* 41 */       super.onLanded(worldIn, entityIn);
/*    */     }
/* 43 */     else if (entityIn.motionY < 0.0D) {
/*    */       
/* 45 */       entityIn.motionY = -entityIn.motionY;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn) {
/* 51 */     if (Math.abs(entityIn.motionY) < 0.1D && !entityIn.isSneaking()) {
/*    */       
/* 53 */       double d0 = 0.4D + Math.abs(entityIn.motionY) * 0.2D;
/* 54 */       entityIn.motionX *= d0;
/* 55 */       entityIn.motionZ *= d0;
/*    */     } 
/*    */     
/* 58 */     super.onEntityCollidedWithBlock(worldIn, pos, entityIn);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockSlime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */