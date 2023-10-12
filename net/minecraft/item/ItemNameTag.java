/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ 
/*    */ public class ItemNameTag
/*    */   extends Item
/*    */ {
/*    */   public ItemNameTag() {
/* 12 */     setCreativeTab(CreativeTabs.tabTools);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target) {
/* 17 */     if (!stack.hasDisplayName())
/*    */     {
/* 19 */       return false;
/*    */     }
/* 21 */     if (target instanceof EntityLiving) {
/*    */       
/* 23 */       EntityLiving entityliving = (EntityLiving)target;
/* 24 */       entityliving.setCustomNameTag(stack.getDisplayName());
/* 25 */       entityliving.enablePersistence();
/* 26 */       stack.stackSize--;
/* 27 */       return true;
/*    */     } 
/*    */ 
/*    */     
/* 31 */     return super.itemInteractionForEntity(stack, playerIn, target);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemNameTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */