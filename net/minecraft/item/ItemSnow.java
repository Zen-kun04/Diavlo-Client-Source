/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.BlockSnow;
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.AxisAlignedBB;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemSnow
/*    */   extends ItemBlock {
/*    */   public ItemSnow(Block block) {
/* 16 */     super(block);
/* 17 */     setMaxDamage(0);
/* 18 */     setHasSubtypes(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
/* 23 */     if (stack.stackSize == 0)
/*    */     {
/* 25 */       return false;
/*    */     }
/* 27 */     if (!playerIn.canPlayerEdit(pos, side, stack))
/*    */     {
/* 29 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 33 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/* 34 */     Block block = iblockstate.getBlock();
/* 35 */     BlockPos blockpos = pos;
/*    */     
/* 37 */     if ((side != EnumFacing.UP || block != this.block) && !block.isReplaceable(worldIn, pos)) {
/*    */       
/* 39 */       blockpos = pos.offset(side);
/* 40 */       iblockstate = worldIn.getBlockState(blockpos);
/* 41 */       block = iblockstate.getBlock();
/*    */     } 
/*    */     
/* 44 */     if (block == this.block) {
/*    */       
/* 46 */       int i = ((Integer)iblockstate.getValue((IProperty)BlockSnow.LAYERS)).intValue();
/*    */       
/* 48 */       if (i <= 7) {
/*    */         
/* 50 */         IBlockState iblockstate1 = iblockstate.withProperty((IProperty)BlockSnow.LAYERS, Integer.valueOf(i + 1));
/* 51 */         AxisAlignedBB axisalignedbb = this.block.getCollisionBoundingBox(worldIn, blockpos, iblockstate1);
/*    */         
/* 53 */         if (axisalignedbb != null && worldIn.checkNoEntityCollision(axisalignedbb) && worldIn.setBlockState(blockpos, iblockstate1, 2)) {
/*    */           
/* 55 */           worldIn.playSoundEffect((blockpos.getX() + 0.5F), (blockpos.getY() + 0.5F), (blockpos.getZ() + 0.5F), this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0F) / 2.0F, this.block.stepSound.getFrequency() * 0.8F);
/* 56 */           stack.stackSize--;
/* 57 */           return true;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 62 */     return super.onItemUse(stack, playerIn, worldIn, blockpos, side, hitX, hitY, hitZ);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getMetadata(int damage) {
/* 68 */     return damage;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemSnow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */