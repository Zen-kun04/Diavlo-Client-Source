/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.passive.EntityPig;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ 
/*    */ public class ItemSaddle
/*    */   extends Item {
/*    */   public ItemSaddle() {
/* 12 */     this.maxStackSize = 1;
/* 13 */     setCreativeTab(CreativeTabs.tabTransport);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target) {
/* 18 */     if (target instanceof EntityPig) {
/*    */       
/* 20 */       EntityPig entitypig = (EntityPig)target;
/*    */       
/* 22 */       if (!entitypig.getSaddled() && !entitypig.isChild()) {
/*    */         
/* 24 */         entitypig.setSaddled(true);
/* 25 */         entitypig.worldObj.playSoundAtEntity((Entity)entitypig, "mob.horse.leather", 0.5F, 1.0F);
/* 26 */         stack.stackSize--;
/*    */       } 
/*    */       
/* 29 */       return true;
/*    */     } 
/*    */ 
/*    */     
/* 33 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
/* 39 */     itemInteractionForEntity(stack, (EntityPlayer)null, target);
/* 40 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemSaddle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */