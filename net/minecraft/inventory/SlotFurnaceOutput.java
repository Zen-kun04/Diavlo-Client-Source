/*    */ package net.minecraft.inventory;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.item.EntityXPOrb;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.item.crafting.FurnaceRecipes;
/*    */ import net.minecraft.stats.AchievementList;
/*    */ import net.minecraft.stats.StatBase;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class SlotFurnaceOutput extends Slot {
/*    */   private EntityPlayer thePlayer;
/*    */   private int field_75228_b;
/*    */   
/*    */   public SlotFurnaceOutput(EntityPlayer player, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
/* 18 */     super(inventoryIn, slotIndex, xPosition, yPosition);
/* 19 */     this.thePlayer = player;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isItemValid(ItemStack stack) {
/* 24 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack decrStackSize(int amount) {
/* 29 */     if (getHasStack())
/*    */     {
/* 31 */       this.field_75228_b += Math.min(amount, (getStack()).stackSize);
/*    */     }
/*    */     
/* 34 */     return super.decrStackSize(amount);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
/* 39 */     onCrafting(stack);
/* 40 */     super.onPickupFromSlot(playerIn, stack);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onCrafting(ItemStack stack, int amount) {
/* 45 */     this.field_75228_b += amount;
/* 46 */     onCrafting(stack);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onCrafting(ItemStack stack) {
/* 51 */     stack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.field_75228_b);
/*    */     
/* 53 */     if (!this.thePlayer.worldObj.isRemote) {
/*    */       
/* 55 */       int i = this.field_75228_b;
/* 56 */       float f = FurnaceRecipes.instance().getSmeltingExperience(stack);
/*    */       
/* 58 */       if (f == 0.0F) {
/*    */         
/* 60 */         i = 0;
/*    */       }
/* 62 */       else if (f < 1.0F) {
/*    */         
/* 64 */         int j = MathHelper.floor_float(i * f);
/*    */         
/* 66 */         if (j < MathHelper.ceiling_float_int(i * f) && Math.random() < (i * f - j))
/*    */         {
/* 68 */           j++;
/*    */         }
/*    */         
/* 71 */         i = j;
/*    */       } 
/*    */       
/* 74 */       while (i > 0) {
/*    */         
/* 76 */         int k = EntityXPOrb.getXPSplit(i);
/* 77 */         i -= k;
/* 78 */         this.thePlayer.worldObj.spawnEntityInWorld((Entity)new EntityXPOrb(this.thePlayer.worldObj, this.thePlayer.posX, this.thePlayer.posY + 0.5D, this.thePlayer.posZ + 0.5D, k));
/*    */       } 
/*    */     } 
/*    */     
/* 82 */     this.field_75228_b = 0;
/*    */     
/* 84 */     if (stack.getItem() == Items.iron_ingot)
/*    */     {
/* 86 */       this.thePlayer.triggerAchievement((StatBase)AchievementList.acquireIron);
/*    */     }
/*    */     
/* 89 */     if (stack.getItem() == Items.cooked_fish)
/*    */     {
/* 91 */       this.thePlayer.triggerAchievement((StatBase)AchievementList.cookFish);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\inventory\SlotFurnaceOutput.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */