/*     */ package net.minecraft.item.crafting;
/*     */ 
/*     */ import net.minecraft.inventory.InventoryCrafting;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ShapedRecipes
/*     */   implements IRecipe
/*     */ {
/*     */   private final int recipeWidth;
/*     */   private final int recipeHeight;
/*     */   private final ItemStack[] recipeItems;
/*     */   private final ItemStack recipeOutput;
/*     */   private boolean copyIngredientNBT;
/*     */   
/*     */   public ShapedRecipes(int width, int height, ItemStack[] p_i1917_3_, ItemStack output) {
/*  18 */     this.recipeWidth = width;
/*  19 */     this.recipeHeight = height;
/*  20 */     this.recipeItems = p_i1917_3_;
/*  21 */     this.recipeOutput = output;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getRecipeOutput() {
/*  26 */     return this.recipeOutput;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack[] getRemainingItems(InventoryCrafting inv) {
/*  31 */     ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
/*     */     
/*  33 */     for (int i = 0; i < aitemstack.length; i++) {
/*     */       
/*  35 */       ItemStack itemstack = inv.getStackInSlot(i);
/*     */       
/*  37 */       if (itemstack != null && itemstack.getItem().hasContainerItem())
/*     */       {
/*  39 */         aitemstack[i] = new ItemStack(itemstack.getItem().getContainerItem());
/*     */       }
/*     */     } 
/*     */     
/*  43 */     return aitemstack;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(InventoryCrafting inv, World worldIn) {
/*  48 */     for (int i = 0; i <= 3 - this.recipeWidth; i++) {
/*     */       
/*  50 */       for (int j = 0; j <= 3 - this.recipeHeight; j++) {
/*     */         
/*  52 */         if (checkMatch(inv, i, j, true))
/*     */         {
/*  54 */           return true;
/*     */         }
/*     */         
/*  57 */         if (checkMatch(inv, i, j, false))
/*     */         {
/*  59 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  64 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean checkMatch(InventoryCrafting p_77573_1_, int p_77573_2_, int p_77573_3_, boolean p_77573_4_) {
/*  69 */     for (int i = 0; i < 3; i++) {
/*     */       
/*  71 */       for (int j = 0; j < 3; j++) {
/*     */         
/*  73 */         int k = i - p_77573_2_;
/*  74 */         int l = j - p_77573_3_;
/*  75 */         ItemStack itemstack = null;
/*     */         
/*  77 */         if (k >= 0 && l >= 0 && k < this.recipeWidth && l < this.recipeHeight)
/*     */         {
/*  79 */           if (p_77573_4_) {
/*     */             
/*  81 */             itemstack = this.recipeItems[this.recipeWidth - k - 1 + l * this.recipeWidth];
/*     */           }
/*     */           else {
/*     */             
/*  85 */             itemstack = this.recipeItems[k + l * this.recipeWidth];
/*     */           } 
/*     */         }
/*     */         
/*  89 */         ItemStack itemstack1 = p_77573_1_.getStackInRowAndColumn(i, j);
/*     */         
/*  91 */         if (itemstack1 != null || itemstack != null) {
/*     */           
/*  93 */           if ((itemstack1 == null && itemstack != null) || (itemstack1 != null && itemstack == null))
/*     */           {
/*  95 */             return false;
/*     */           }
/*     */           
/*  98 */           if (itemstack.getItem() != itemstack1.getItem())
/*     */           {
/* 100 */             return false;
/*     */           }
/*     */           
/* 103 */           if (itemstack.getMetadata() != 32767 && itemstack.getMetadata() != itemstack1.getMetadata())
/*     */           {
/* 105 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 111 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getCraftingResult(InventoryCrafting inv) {
/* 116 */     ItemStack itemstack = getRecipeOutput().copy();
/*     */     
/* 118 */     if (this.copyIngredientNBT)
/*     */     {
/* 120 */       for (int i = 0; i < inv.getSizeInventory(); i++) {
/*     */         
/* 122 */         ItemStack itemstack1 = inv.getStackInSlot(i);
/*     */         
/* 124 */         if (itemstack1 != null && itemstack1.hasTagCompound())
/*     */         {
/* 126 */           itemstack.setTagCompound((NBTTagCompound)itemstack1.getTagCompound().copy());
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 131 */     return itemstack;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRecipeSize() {
/* 136 */     return this.recipeWidth * this.recipeHeight;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\crafting\ShapedRecipes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */