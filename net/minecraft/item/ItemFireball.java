/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemFireball
/*    */   extends Item
/*    */ {
/*    */   public ItemFireball() {
/* 15 */     setCreativeTab(CreativeTabs.tabMisc);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
/* 20 */     if (worldIn.isRemote)
/*    */     {
/* 22 */       return true;
/*    */     }
/*    */ 
/*    */     
/* 26 */     pos = pos.offset(side);
/*    */     
/* 28 */     if (!playerIn.canPlayerEdit(pos, side, stack))
/*    */     {
/* 30 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 34 */     if (worldIn.getBlockState(pos).getBlock().getMaterial() == Material.air) {
/*    */       
/* 36 */       worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "item.fireCharge.use", 1.0F, (itemRand.nextFloat() - itemRand.nextFloat()) * 0.2F + 1.0F);
/* 37 */       worldIn.setBlockState(pos, Blocks.fire.getDefaultState());
/*    */     } 
/*    */     
/* 40 */     if (!playerIn.capabilities.isCreativeMode)
/*    */     {
/* 42 */       stack.stackSize--;
/*    */     }
/*    */     
/* 45 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemFireball.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */