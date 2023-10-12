/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLeashKnot;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.AxisAlignedBB;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemLead
/*    */   extends Item
/*    */ {
/*    */   public ItemLead() {
/* 18 */     setCreativeTab(CreativeTabs.tabTools);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
/* 23 */     Block block = worldIn.getBlockState(pos).getBlock();
/*    */     
/* 25 */     if (block instanceof net.minecraft.block.BlockFence) {
/*    */       
/* 27 */       if (worldIn.isRemote)
/*    */       {
/* 29 */         return true;
/*    */       }
/*    */ 
/*    */       
/* 33 */       attachToFence(playerIn, worldIn, pos);
/* 34 */       return true;
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 39 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean attachToFence(EntityPlayer player, World worldIn, BlockPos fence) {
/* 45 */     EntityLeashKnot entityleashknot = EntityLeashKnot.getKnotForPosition(worldIn, fence);
/* 46 */     boolean flag = false;
/* 47 */     double d0 = 7.0D;
/* 48 */     int i = fence.getX();
/* 49 */     int j = fence.getY();
/* 50 */     int k = fence.getZ();
/*    */     
/* 52 */     for (EntityLiving entityliving : worldIn.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(i - d0, j - d0, k - d0, i + d0, j + d0, k + d0))) {
/*    */       
/* 54 */       if (entityliving.getLeashed() && entityliving.getLeashedToEntity() == player) {
/*    */         
/* 56 */         if (entityleashknot == null)
/*    */         {
/* 58 */           entityleashknot = EntityLeashKnot.createKnot(worldIn, fence);
/*    */         }
/*    */         
/* 61 */         entityliving.setLeashedToEntity((Entity)entityleashknot, true);
/* 62 */         flag = true;
/*    */       } 
/*    */     } 
/*    */     
/* 66 */     return flag;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemLead.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */