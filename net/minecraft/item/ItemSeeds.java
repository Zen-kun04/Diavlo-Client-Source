/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemSeeds
/*    */   extends Item
/*    */ {
/*    */   private Block crops;
/*    */   private Block soilBlockID;
/*    */   
/*    */   public ItemSeeds(Block crops, Block soil) {
/* 17 */     this.crops = crops;
/* 18 */     this.soilBlockID = soil;
/* 19 */     setCreativeTab(CreativeTabs.tabMaterials);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
/* 24 */     if (side != EnumFacing.UP)
/*    */     {
/* 26 */       return false;
/*    */     }
/* 28 */     if (!playerIn.canPlayerEdit(pos.offset(side), side, stack))
/*    */     {
/* 30 */       return false;
/*    */     }
/* 32 */     if (worldIn.getBlockState(pos).getBlock() == this.soilBlockID && worldIn.isAirBlock(pos.up())) {
/*    */       
/* 34 */       worldIn.setBlockState(pos.up(), this.crops.getDefaultState());
/* 35 */       stack.stackSize--;
/* 36 */       return true;
/*    */     } 
/*    */ 
/*    */     
/* 40 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemSeeds.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */