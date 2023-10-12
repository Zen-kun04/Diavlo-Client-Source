/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemSeedFood
/*    */   extends ItemFood
/*    */ {
/*    */   private Block crops;
/*    */   private Block soilId;
/*    */   
/*    */   public ItemSeedFood(int healAmount, float saturation, Block crops, Block soil) {
/* 16 */     super(healAmount, saturation, false);
/* 17 */     this.crops = crops;
/* 18 */     this.soilId = soil;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
/* 23 */     if (side != EnumFacing.UP)
/*    */     {
/* 25 */       return false;
/*    */     }
/* 27 */     if (!playerIn.canPlayerEdit(pos.offset(side), side, stack))
/*    */     {
/* 29 */       return false;
/*    */     }
/* 31 */     if (worldIn.getBlockState(pos).getBlock() == this.soilId && worldIn.isAirBlock(pos.up())) {
/*    */       
/* 33 */       worldIn.setBlockState(pos.up(), this.crops.getDefaultState());
/* 34 */       stack.stackSize--;
/* 35 */       return true;
/*    */     } 
/*    */ 
/*    */     
/* 39 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemSeedFood.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */