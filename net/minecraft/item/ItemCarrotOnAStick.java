/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.passive.EntityPig;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.stats.StatList;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemCarrotOnAStick
/*    */   extends Item {
/*    */   public ItemCarrotOnAStick() {
/* 14 */     setCreativeTab(CreativeTabs.tabTransport);
/* 15 */     setMaxStackSize(1);
/* 16 */     setMaxDamage(25);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isFull3D() {
/* 21 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldRotateAroundWhenRendering() {
/* 26 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
/* 31 */     if (playerIn.isRiding() && playerIn.ridingEntity instanceof EntityPig) {
/*    */       
/* 33 */       EntityPig entitypig = (EntityPig)playerIn.ridingEntity;
/*    */       
/* 35 */       if (entitypig.getAIControlledByPlayer().isControlledByPlayer() && itemStackIn.getMaxDamage() - itemStackIn.getMetadata() >= 7) {
/*    */         
/* 37 */         entitypig.getAIControlledByPlayer().boostSpeed();
/* 38 */         itemStackIn.damageItem(7, (EntityLivingBase)playerIn);
/*    */         
/* 40 */         if (itemStackIn.stackSize == 0) {
/*    */           
/* 42 */           ItemStack itemstack = new ItemStack(Items.fishing_rod);
/* 43 */           itemstack.setTagCompound(itemStackIn.getTagCompound());
/* 44 */           return itemstack;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 49 */     playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
/* 50 */     return itemStackIn;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemCarrotOnAStick.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */