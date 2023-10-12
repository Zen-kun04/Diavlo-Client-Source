/*     */ package net.minecraft.item.crafting;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.inventory.InventoryCrafting;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ public class RecipeRepairItem
/*     */   implements IRecipe
/*     */ {
/*     */   public boolean matches(InventoryCrafting inv, World worldIn) {
/*  15 */     List<ItemStack> list = Lists.newArrayList();
/*     */     
/*  17 */     for (int i = 0; i < inv.getSizeInventory(); i++) {
/*     */       
/*  19 */       ItemStack itemstack = inv.getStackInSlot(i);
/*     */       
/*  21 */       if (itemstack != null) {
/*     */         
/*  23 */         list.add(itemstack);
/*     */         
/*  25 */         if (list.size() > 1) {
/*     */           
/*  27 */           ItemStack itemstack1 = list.get(0);
/*     */           
/*  29 */           if (itemstack.getItem() != itemstack1.getItem() || itemstack1.stackSize != 1 || itemstack.stackSize != 1 || !itemstack1.getItem().isDamageable())
/*     */           {
/*  31 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  37 */     return (list.size() == 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getCraftingResult(InventoryCrafting inv) {
/*  42 */     List<ItemStack> list = Lists.newArrayList();
/*     */     
/*  44 */     for (int i = 0; i < inv.getSizeInventory(); i++) {
/*     */       
/*  46 */       ItemStack itemstack = inv.getStackInSlot(i);
/*     */       
/*  48 */       if (itemstack != null) {
/*     */         
/*  50 */         list.add(itemstack);
/*     */         
/*  52 */         if (list.size() > 1) {
/*     */           
/*  54 */           ItemStack itemstack1 = list.get(0);
/*     */           
/*  56 */           if (itemstack.getItem() != itemstack1.getItem() || itemstack1.stackSize != 1 || itemstack.stackSize != 1 || !itemstack1.getItem().isDamageable())
/*     */           {
/*  58 */             return null;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  64 */     if (list.size() == 2) {
/*     */       
/*  66 */       ItemStack itemstack2 = list.get(0);
/*  67 */       ItemStack itemstack3 = list.get(1);
/*     */       
/*  69 */       if (itemstack2.getItem() == itemstack3.getItem() && itemstack2.stackSize == 1 && itemstack3.stackSize == 1 && itemstack2.getItem().isDamageable()) {
/*     */         
/*  71 */         Item item = itemstack2.getItem();
/*  72 */         int j = item.getMaxDamage() - itemstack2.getItemDamage();
/*  73 */         int k = item.getMaxDamage() - itemstack3.getItemDamage();
/*  74 */         int l = j + k + item.getMaxDamage() * 5 / 100;
/*  75 */         int i1 = item.getMaxDamage() - l;
/*     */         
/*  77 */         if (i1 < 0)
/*     */         {
/*  79 */           i1 = 0;
/*     */         }
/*     */         
/*  82 */         return new ItemStack(itemstack2.getItem(), 1, i1);
/*     */       } 
/*     */     } 
/*     */     
/*  86 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRecipeSize() {
/*  91 */     return 4;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getRecipeOutput() {
/*  96 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack[] getRemainingItems(InventoryCrafting inv) {
/* 101 */     ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
/*     */     
/* 103 */     for (int i = 0; i < aitemstack.length; i++) {
/*     */       
/* 105 */       ItemStack itemstack = inv.getStackInSlot(i);
/*     */       
/* 107 */       if (itemstack != null && itemstack.getItem().hasContainerItem())
/*     */       {
/* 109 */         aitemstack[i] = new ItemStack(itemstack.getItem().getContainerItem());
/*     */       }
/*     */     } 
/*     */     
/* 113 */     return aitemstack;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\crafting\RecipeRepairItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */