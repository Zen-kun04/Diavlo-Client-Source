/*    */ package net.minecraft.item.crafting;
/*    */ 
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.inventory.InventoryCrafting;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.storage.MapData;
/*    */ 
/*    */ public class RecipesMapExtending
/*    */   extends ShapedRecipes {
/*    */   public RecipesMapExtending() {
/* 14 */     super(3, 3, new ItemStack[] { new ItemStack(Items.paper), new ItemStack(Items.paper), new ItemStack(Items.paper), new ItemStack(Items.paper), new ItemStack((Item)Items.filled_map, 0, 32767), new ItemStack(Items.paper), new ItemStack(Items.paper), new ItemStack(Items.paper), new ItemStack(Items.paper) }new ItemStack((Item)Items.map, 0, 0));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(InventoryCrafting inv, World worldIn) {
/* 19 */     if (!super.matches(inv, worldIn))
/*    */     {
/* 21 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 25 */     ItemStack itemstack = null;
/*    */     
/* 27 */     for (int i = 0; i < inv.getSizeInventory() && itemstack == null; i++) {
/*    */       
/* 29 */       ItemStack itemstack1 = inv.getStackInSlot(i);
/*    */       
/* 31 */       if (itemstack1 != null && itemstack1.getItem() == Items.filled_map)
/*    */       {
/* 33 */         itemstack = itemstack1;
/*    */       }
/*    */     } 
/*    */     
/* 37 */     if (itemstack == null)
/*    */     {
/* 39 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 43 */     MapData mapdata = Items.filled_map.getMapData(itemstack, worldIn);
/* 44 */     return (mapdata == null) ? false : ((mapdata.scale < 4));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack getCraftingResult(InventoryCrafting inv) {
/* 51 */     ItemStack itemstack = null;
/*    */     
/* 53 */     for (int i = 0; i < inv.getSizeInventory() && itemstack == null; i++) {
/*    */       
/* 55 */       ItemStack itemstack1 = inv.getStackInSlot(i);
/*    */       
/* 57 */       if (itemstack1 != null && itemstack1.getItem() == Items.filled_map)
/*    */       {
/* 59 */         itemstack = itemstack1;
/*    */       }
/*    */     } 
/*    */     
/* 63 */     itemstack = itemstack.copy();
/* 64 */     itemstack.stackSize = 1;
/*    */     
/* 66 */     if (itemstack.getTagCompound() == null)
/*    */     {
/* 68 */       itemstack.setTagCompound(new NBTTagCompound());
/*    */     }
/*    */     
/* 71 */     itemstack.getTagCompound().setBoolean("map_is_scaling", true);
/* 72 */     return itemstack;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\crafting\RecipesMapExtending.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */