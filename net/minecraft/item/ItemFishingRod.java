/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.projectile.EntityFishHook;
/*    */ import net.minecraft.stats.StatList;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemFishingRod extends Item {
/*    */   public ItemFishingRod() {
/* 13 */     setMaxDamage(64);
/* 14 */     setMaxStackSize(1);
/* 15 */     setCreativeTab(CreativeTabs.tabTools);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isFull3D() {
/* 20 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldRotateAroundWhenRendering() {
/* 25 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
/* 30 */     if (playerIn.fishEntity != null) {
/*    */       
/* 32 */       int i = playerIn.fishEntity.handleHookRetraction();
/* 33 */       itemStackIn.damageItem(i, (EntityLivingBase)playerIn);
/* 34 */       playerIn.swingItem();
/*    */     }
/*    */     else {
/*    */       
/* 38 */       worldIn.playSoundAtEntity((Entity)playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
/*    */       
/* 40 */       if (!worldIn.isRemote)
/*    */       {
/* 42 */         worldIn.spawnEntityInWorld((Entity)new EntityFishHook(worldIn, playerIn));
/*    */       }
/*    */       
/* 45 */       playerIn.swingItem();
/* 46 */       playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
/*    */     } 
/*    */     
/* 49 */     return itemStackIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isItemTool(ItemStack stack) {
/* 54 */     return super.isItemTool(stack);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getItemEnchantability() {
/* 59 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemFishingRod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */