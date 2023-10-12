/*    */ package net.minecraft.item.crafting;
/*    */ 
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ public class RecipesWeapons
/*    */ {
/* 10 */   private String[][] recipePatterns = new String[][] { { "X", "X", "#" } };
/* 11 */   private Object[][] recipeItems = new Object[][] { { Blocks.planks, Blocks.cobblestone, Items.iron_ingot, Items.diamond, Items.gold_ingot }, { Items.wooden_sword, Items.stone_sword, Items.iron_sword, Items.diamond_sword, Items.golden_sword } };
/*    */ 
/*    */   
/*    */   public void addRecipes(CraftingManager p_77583_1_) {
/* 15 */     for (int i = 0; i < (this.recipeItems[0]).length; i++) {
/*    */       
/* 17 */       Object object = this.recipeItems[0][i];
/*    */       
/* 19 */       for (int j = 0; j < this.recipeItems.length - 1; j++) {
/*    */         
/* 21 */         Item item = (Item)this.recipeItems[j + 1][i];
/* 22 */         p_77583_1_.addRecipe(new ItemStack(item), new Object[] { this.recipePatterns[j], Character.valueOf('#'), Items.stick, Character.valueOf('X'), object });
/*    */       } 
/*    */     } 
/*    */     
/* 26 */     p_77583_1_.addRecipe(new ItemStack((Item)Items.bow, 1), new Object[] { " #X", "# X", " #X", Character.valueOf('X'), Items.string, Character.valueOf('#'), Items.stick });
/* 27 */     p_77583_1_.addRecipe(new ItemStack(Items.arrow, 4), new Object[] { "X", "#", "Y", Character.valueOf('Y'), Items.feather, Character.valueOf('X'), Items.flint, Character.valueOf('#'), Items.stick });
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\crafting\RecipesWeapons.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */