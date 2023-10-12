/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemPickaxe;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.crafting.CraftingManager;
/*     */ import net.minecraft.stats.AchievementList;
/*     */ import net.minecraft.stats.StatBase;
/*     */ 
/*     */ 
/*     */ public class SlotCrafting
/*     */   extends Slot
/*     */ {
/*     */   private final InventoryCrafting craftMatrix;
/*     */   private final EntityPlayer thePlayer;
/*     */   private int amountCrafted;
/*     */   
/*     */   public SlotCrafting(EntityPlayer player, InventoryCrafting craftingInventory, IInventory p_i45790_3_, int slotIndex, int xPosition, int yPosition) {
/*  22 */     super(p_i45790_3_, slotIndex, xPosition, yPosition);
/*  23 */     this.thePlayer = player;
/*  24 */     this.craftMatrix = craftingInventory;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isItemValid(ItemStack stack) {
/*  29 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack decrStackSize(int amount) {
/*  34 */     if (getHasStack())
/*     */     {
/*  36 */       this.amountCrafted += Math.min(amount, (getStack()).stackSize);
/*     */     }
/*     */     
/*  39 */     return super.decrStackSize(amount);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onCrafting(ItemStack stack, int amount) {
/*  44 */     this.amountCrafted += amount;
/*  45 */     onCrafting(stack);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onCrafting(ItemStack stack) {
/*  50 */     if (this.amountCrafted > 0)
/*     */     {
/*  52 */       stack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.amountCrafted);
/*     */     }
/*     */     
/*  55 */     this.amountCrafted = 0;
/*     */     
/*  57 */     if (stack.getItem() == Item.getItemFromBlock(Blocks.crafting_table))
/*     */     {
/*  59 */       this.thePlayer.triggerAchievement((StatBase)AchievementList.buildWorkBench);
/*     */     }
/*     */     
/*  62 */     if (stack.getItem() instanceof ItemPickaxe)
/*     */     {
/*  64 */       this.thePlayer.triggerAchievement((StatBase)AchievementList.buildPickaxe);
/*     */     }
/*     */     
/*  67 */     if (stack.getItem() == Item.getItemFromBlock(Blocks.furnace))
/*     */     {
/*  69 */       this.thePlayer.triggerAchievement((StatBase)AchievementList.buildFurnace);
/*     */     }
/*     */     
/*  72 */     if (stack.getItem() instanceof net.minecraft.item.ItemHoe)
/*     */     {
/*  74 */       this.thePlayer.triggerAchievement((StatBase)AchievementList.buildHoe);
/*     */     }
/*     */     
/*  77 */     if (stack.getItem() == Items.bread)
/*     */     {
/*  79 */       this.thePlayer.triggerAchievement((StatBase)AchievementList.makeBread);
/*     */     }
/*     */     
/*  82 */     if (stack.getItem() == Items.cake)
/*     */     {
/*  84 */       this.thePlayer.triggerAchievement((StatBase)AchievementList.bakeCake);
/*     */     }
/*     */     
/*  87 */     if (stack.getItem() instanceof ItemPickaxe && ((ItemPickaxe)stack.getItem()).getToolMaterial() != Item.ToolMaterial.WOOD)
/*     */     {
/*  89 */       this.thePlayer.triggerAchievement((StatBase)AchievementList.buildBetterPickaxe);
/*     */     }
/*     */     
/*  92 */     if (stack.getItem() instanceof net.minecraft.item.ItemSword)
/*     */     {
/*  94 */       this.thePlayer.triggerAchievement((StatBase)AchievementList.buildSword);
/*     */     }
/*     */     
/*  97 */     if (stack.getItem() == Item.getItemFromBlock(Blocks.enchanting_table))
/*     */     {
/*  99 */       this.thePlayer.triggerAchievement((StatBase)AchievementList.enchantments);
/*     */     }
/*     */     
/* 102 */     if (stack.getItem() == Item.getItemFromBlock(Blocks.bookshelf))
/*     */     {
/* 104 */       this.thePlayer.triggerAchievement((StatBase)AchievementList.bookcase);
/*     */     }
/*     */     
/* 107 */     if (stack.getItem() == Items.golden_apple && stack.getMetadata() == 1)
/*     */     {
/* 109 */       this.thePlayer.triggerAchievement((StatBase)AchievementList.overpowered);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
/* 115 */     onCrafting(stack);
/* 116 */     ItemStack[] aitemstack = CraftingManager.getInstance().func_180303_b(this.craftMatrix, playerIn.worldObj);
/*     */     
/* 118 */     for (int i = 0; i < aitemstack.length; i++) {
/*     */       
/* 120 */       ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
/* 121 */       ItemStack itemstack1 = aitemstack[i];
/*     */       
/* 123 */       if (itemstack != null)
/*     */       {
/* 125 */         this.craftMatrix.decrStackSize(i, 1);
/*     */       }
/*     */       
/* 128 */       if (itemstack1 != null)
/*     */       {
/* 130 */         if (this.craftMatrix.getStackInSlot(i) == null) {
/*     */           
/* 132 */           this.craftMatrix.setInventorySlotContents(i, itemstack1);
/*     */         }
/* 134 */         else if (!this.thePlayer.inventory.addItemStackToInventory(itemstack1)) {
/*     */           
/* 136 */           this.thePlayer.dropPlayerItemWithRandomChoice(itemstack1, false);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\inventory\SlotCrafting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */