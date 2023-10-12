/*     */ package net.minecraft.item.crafting;
/*     */ 
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.InventoryCrafting;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class RecipesMapCloning
/*     */   implements IRecipe {
/*     */   public boolean matches(InventoryCrafting inv, World worldIn) {
/*  12 */     int i = 0;
/*  13 */     ItemStack itemstack = null;
/*     */     
/*  15 */     for (int j = 0; j < inv.getSizeInventory(); j++) {
/*     */       
/*  17 */       ItemStack itemstack1 = inv.getStackInSlot(j);
/*     */       
/*  19 */       if (itemstack1 != null)
/*     */       {
/*  21 */         if (itemstack1.getItem() == Items.filled_map) {
/*     */           
/*  23 */           if (itemstack != null)
/*     */           {
/*  25 */             return false;
/*     */           }
/*     */           
/*  28 */           itemstack = itemstack1;
/*     */         }
/*     */         else {
/*     */           
/*  32 */           if (itemstack1.getItem() != Items.map)
/*     */           {
/*  34 */             return false;
/*     */           }
/*     */           
/*  37 */           i++;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/*  42 */     return (itemstack != null && i > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getCraftingResult(InventoryCrafting inv) {
/*  47 */     int i = 0;
/*  48 */     ItemStack itemstack = null;
/*     */     
/*  50 */     for (int j = 0; j < inv.getSizeInventory(); j++) {
/*     */       
/*  52 */       ItemStack itemstack1 = inv.getStackInSlot(j);
/*     */       
/*  54 */       if (itemstack1 != null)
/*     */       {
/*  56 */         if (itemstack1.getItem() == Items.filled_map) {
/*     */           
/*  58 */           if (itemstack != null)
/*     */           {
/*  60 */             return null;
/*     */           }
/*     */           
/*  63 */           itemstack = itemstack1;
/*     */         }
/*     */         else {
/*     */           
/*  67 */           if (itemstack1.getItem() != Items.map)
/*     */           {
/*  69 */             return null;
/*     */           }
/*     */           
/*  72 */           i++;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/*  77 */     if (itemstack != null && i >= 1) {
/*     */       
/*  79 */       ItemStack itemstack2 = new ItemStack((Item)Items.filled_map, i + 1, itemstack.getMetadata());
/*     */       
/*  81 */       if (itemstack.hasDisplayName())
/*     */       {
/*  83 */         itemstack2.setStackDisplayName(itemstack.getDisplayName());
/*     */       }
/*     */       
/*  86 */       return itemstack2;
/*     */     } 
/*     */ 
/*     */     
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRecipeSize() {
/*  96 */     return 9;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getRecipeOutput() {
/* 101 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack[] getRemainingItems(InventoryCrafting inv) {
/* 106 */     ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
/*     */     
/* 108 */     for (int i = 0; i < aitemstack.length; i++) {
/*     */       
/* 110 */       ItemStack itemstack = inv.getStackInSlot(i);
/*     */       
/* 112 */       if (itemstack != null && itemstack.getItem().hasContainerItem())
/*     */       {
/* 114 */         aitemstack[i] = new ItemStack(itemstack.getItem().getContainerItem());
/*     */       }
/*     */     } 
/*     */     
/* 118 */     return aitemstack;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\crafting\RecipesMapCloning.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */