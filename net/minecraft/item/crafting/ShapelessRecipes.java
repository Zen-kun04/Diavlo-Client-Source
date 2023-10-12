/*    */ package net.minecraft.item.crafting;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.inventory.InventoryCrafting;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ 
/*    */ public class ShapelessRecipes
/*    */   implements IRecipe
/*    */ {
/*    */   private final ItemStack recipeOutput;
/*    */   private final List<ItemStack> recipeItems;
/*    */   
/*    */   public ShapelessRecipes(ItemStack output, List<ItemStack> inputList) {
/* 17 */     this.recipeOutput = output;
/* 18 */     this.recipeItems = inputList;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack getRecipeOutput() {
/* 23 */     return this.recipeOutput;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack[] getRemainingItems(InventoryCrafting inv) {
/* 28 */     ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
/*    */     
/* 30 */     for (int i = 0; i < aitemstack.length; i++) {
/*    */       
/* 32 */       ItemStack itemstack = inv.getStackInSlot(i);
/*    */       
/* 34 */       if (itemstack != null && itemstack.getItem().hasContainerItem())
/*    */       {
/* 36 */         aitemstack[i] = new ItemStack(itemstack.getItem().getContainerItem());
/*    */       }
/*    */     } 
/*    */     
/* 40 */     return aitemstack;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(InventoryCrafting inv, World worldIn) {
/* 45 */     List<ItemStack> list = Lists.newArrayList(this.recipeItems);
/*    */     
/* 47 */     for (int i = 0; i < inv.getHeight(); i++) {
/*    */       
/* 49 */       for (int j = 0; j < inv.getWidth(); j++) {
/*    */         
/* 51 */         ItemStack itemstack = inv.getStackInRowAndColumn(j, i);
/*    */         
/* 53 */         if (itemstack != null) {
/*    */           
/* 55 */           boolean flag = false;
/*    */           
/* 57 */           for (ItemStack itemstack1 : list) {
/*    */             
/* 59 */             if (itemstack.getItem() == itemstack1.getItem() && (itemstack1.getMetadata() == 32767 || itemstack.getMetadata() == itemstack1.getMetadata())) {
/*    */               
/* 61 */               flag = true;
/* 62 */               list.remove(itemstack1);
/*    */               
/*    */               break;
/*    */             } 
/*    */           } 
/* 67 */           if (!flag)
/*    */           {
/* 69 */             return false;
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 75 */     return list.isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack getCraftingResult(InventoryCrafting inv) {
/* 80 */     return this.recipeOutput.copy();
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRecipeSize() {
/* 85 */     return this.recipeItems.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\crafting\ShapelessRecipes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */