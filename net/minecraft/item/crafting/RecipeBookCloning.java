/*     */ package net.minecraft.item.crafting;
/*     */ 
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.InventoryCrafting;
/*     */ import net.minecraft.item.ItemEditableBook;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class RecipeBookCloning
/*     */   implements IRecipe
/*     */ {
/*     */   public boolean matches(InventoryCrafting inv, World worldIn) {
/*  14 */     int i = 0;
/*  15 */     ItemStack itemstack = null;
/*     */     
/*  17 */     for (int j = 0; j < inv.getSizeInventory(); j++) {
/*     */       
/*  19 */       ItemStack itemstack1 = inv.getStackInSlot(j);
/*     */       
/*  21 */       if (itemstack1 != null)
/*     */       {
/*  23 */         if (itemstack1.getItem() == Items.written_book) {
/*     */           
/*  25 */           if (itemstack != null)
/*     */           {
/*  27 */             return false;
/*     */           }
/*     */           
/*  30 */           itemstack = itemstack1;
/*     */         }
/*     */         else {
/*     */           
/*  34 */           if (itemstack1.getItem() != Items.writable_book)
/*     */           {
/*  36 */             return false;
/*     */           }
/*     */           
/*  39 */           i++;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/*  44 */     return (itemstack != null && i > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getCraftingResult(InventoryCrafting inv) {
/*  49 */     int i = 0;
/*  50 */     ItemStack itemstack = null;
/*     */     
/*  52 */     for (int j = 0; j < inv.getSizeInventory(); j++) {
/*     */       
/*  54 */       ItemStack itemstack1 = inv.getStackInSlot(j);
/*     */       
/*  56 */       if (itemstack1 != null)
/*     */       {
/*  58 */         if (itemstack1.getItem() == Items.written_book) {
/*     */           
/*  60 */           if (itemstack != null)
/*     */           {
/*  62 */             return null;
/*     */           }
/*     */           
/*  65 */           itemstack = itemstack1;
/*     */         }
/*     */         else {
/*     */           
/*  69 */           if (itemstack1.getItem() != Items.writable_book)
/*     */           {
/*  71 */             return null;
/*     */           }
/*     */           
/*  74 */           i++;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/*  79 */     if (itemstack != null && i >= 1 && ItemEditableBook.getGeneration(itemstack) < 2) {
/*     */       
/*  81 */       ItemStack itemstack2 = new ItemStack(Items.written_book, i);
/*  82 */       itemstack2.setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
/*  83 */       itemstack2.getTagCompound().setInteger("generation", ItemEditableBook.getGeneration(itemstack) + 1);
/*     */       
/*  85 */       if (itemstack.hasDisplayName())
/*     */       {
/*  87 */         itemstack2.setStackDisplayName(itemstack.getDisplayName());
/*     */       }
/*     */       
/*  90 */       return itemstack2;
/*     */     } 
/*     */ 
/*     */     
/*  94 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRecipeSize() {
/* 100 */     return 9;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getRecipeOutput() {
/* 105 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack[] getRemainingItems(InventoryCrafting inv) {
/* 110 */     ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
/*     */     
/* 112 */     for (int i = 0; i < aitemstack.length; i++) {
/*     */       
/* 114 */       ItemStack itemstack = inv.getStackInSlot(i);
/*     */       
/* 116 */       if (itemstack != null && itemstack.getItem() instanceof ItemEditableBook) {
/*     */         
/* 118 */         aitemstack[i] = itemstack;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 123 */     return aitemstack;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\crafting\RecipeBookCloning.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */