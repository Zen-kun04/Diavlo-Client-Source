/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.projectile.EntityEgg;
/*    */ import net.minecraft.stats.StatList;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemEgg extends Item {
/*    */   public ItemEgg() {
/* 13 */     this.maxStackSize = 16;
/* 14 */     setCreativeTab(CreativeTabs.tabMaterials);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
/* 19 */     if (!playerIn.capabilities.isCreativeMode)
/*    */     {
/* 21 */       itemStackIn.stackSize--;
/*    */     }
/*    */     
/* 24 */     worldIn.playSoundAtEntity((Entity)playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
/*    */     
/* 26 */     if (!worldIn.isRemote)
/*    */     {
/* 28 */       worldIn.spawnEntityInWorld((Entity)new EntityEgg(worldIn, (EntityLivingBase)playerIn));
/*    */     }
/*    */     
/* 31 */     playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
/* 32 */     return itemStackIn;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemEgg.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */