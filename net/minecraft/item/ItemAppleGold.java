/*    */ package net.minecraft.item;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.potion.Potion;
/*    */ import net.minecraft.potion.PotionEffect;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ 
/*    */ public class ItemAppleGold
/*    */   extends ItemFood
/*    */ {
/*    */   public ItemAppleGold(int amount, float saturation, boolean isWolfFood) {
/* 15 */     super(amount, saturation, isWolfFood);
/* 16 */     setHasSubtypes(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasEffect(ItemStack stack) {
/* 21 */     return (stack.getMetadata() > 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public EnumRarity getRarity(ItemStack stack) {
/* 26 */     return (stack.getMetadata() == 0) ? EnumRarity.RARE : EnumRarity.EPIC;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
/* 31 */     if (!worldIn.isRemote)
/*    */     {
/* 33 */       player.addPotionEffect(new PotionEffect(Potion.absorption.id, 2400, 0));
/*    */     }
/*    */     
/* 36 */     if (stack.getMetadata() > 0) {
/*    */       
/* 38 */       if (!worldIn.isRemote)
/*    */       {
/* 40 */         player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 600, 4));
/* 41 */         player.addPotionEffect(new PotionEffect(Potion.resistance.id, 6000, 0));
/* 42 */         player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 6000, 0));
/*    */       }
/*    */     
/*    */     } else {
/*    */       
/* 47 */       super.onFoodEaten(stack, worldIn, player);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
/* 53 */     subItems.add(new ItemStack(itemIn, 1, 0));
/* 54 */     subItems.add(new ItemStack(itemIn, 1, 1));
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemAppleGold.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */