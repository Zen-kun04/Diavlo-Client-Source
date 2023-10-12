/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.entity.item.EntityItem;
/*    */ import net.minecraft.entity.passive.EntityVillager;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.inventory.InventoryBasic;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class EntityAIVillagerInteract extends EntityAIWatchClosest2 {
/*    */   private int interactionDelay;
/*    */   private EntityVillager villager;
/*    */   
/*    */   public EntityAIVillagerInteract(EntityVillager villagerIn) {
/* 18 */     super((EntityLiving)villagerIn, (Class)EntityVillager.class, 3.0F, 0.02F);
/* 19 */     this.villager = villagerIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 24 */     super.startExecuting();
/*    */     
/* 26 */     if (this.villager.canAbondonItems() && this.closestEntity instanceof EntityVillager && ((EntityVillager)this.closestEntity).func_175557_cr()) {
/*    */       
/* 28 */       this.interactionDelay = 10;
/*    */     }
/*    */     else {
/*    */       
/* 32 */       this.interactionDelay = 0;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateTask() {
/* 38 */     super.updateTask();
/*    */     
/* 40 */     if (this.interactionDelay > 0) {
/*    */       
/* 42 */       this.interactionDelay--;
/*    */       
/* 44 */       if (this.interactionDelay == 0) {
/*    */         
/* 46 */         InventoryBasic inventorybasic = this.villager.getVillagerInventory();
/*    */         
/* 48 */         for (int i = 0; i < inventorybasic.getSizeInventory(); i++) {
/*    */           
/* 50 */           ItemStack itemstack = inventorybasic.getStackInSlot(i);
/* 51 */           ItemStack itemstack1 = null;
/*    */           
/* 53 */           if (itemstack != null) {
/*    */             
/* 55 */             Item item = itemstack.getItem();
/*    */             
/* 57 */             if ((item == Items.bread || item == Items.potato || item == Items.carrot) && itemstack.stackSize > 3) {
/*    */               
/* 59 */               int l = itemstack.stackSize / 2;
/* 60 */               itemstack.stackSize -= l;
/* 61 */               itemstack1 = new ItemStack(item, l, itemstack.getMetadata());
/*    */             }
/* 63 */             else if (item == Items.wheat && itemstack.stackSize > 5) {
/*    */               
/* 65 */               int j = itemstack.stackSize / 2 / 3 * 3;
/* 66 */               int k = j / 3;
/* 67 */               itemstack.stackSize -= j;
/* 68 */               itemstack1 = new ItemStack(Items.bread, k, 0);
/*    */             } 
/*    */             
/* 71 */             if (itemstack.stackSize <= 0)
/*    */             {
/* 73 */               inventorybasic.setInventorySlotContents(i, (ItemStack)null);
/*    */             }
/*    */           } 
/*    */           
/* 77 */           if (itemstack1 != null) {
/*    */             
/* 79 */             double d0 = this.villager.posY - 0.30000001192092896D + this.villager.getEyeHeight();
/* 80 */             EntityItem entityitem = new EntityItem(this.villager.worldObj, this.villager.posX, d0, this.villager.posZ, itemstack1);
/* 81 */             float f = 0.3F;
/* 82 */             float f1 = this.villager.rotationYawHead;
/* 83 */             float f2 = this.villager.rotationPitch;
/* 84 */             entityitem.motionX = (-MathHelper.sin(f1 / 180.0F * 3.1415927F) * MathHelper.cos(f2 / 180.0F * 3.1415927F) * f);
/* 85 */             entityitem.motionZ = (MathHelper.cos(f1 / 180.0F * 3.1415927F) * MathHelper.cos(f2 / 180.0F * 3.1415927F) * f);
/* 86 */             entityitem.motionY = (-MathHelper.sin(f2 / 180.0F * 3.1415927F) * f + 0.1F);
/* 87 */             entityitem.setDefaultPickupDelay();
/* 88 */             this.villager.worldObj.spawnEntityInWorld((Entity)entityitem);
/*    */             break;
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIVillagerInteract.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */