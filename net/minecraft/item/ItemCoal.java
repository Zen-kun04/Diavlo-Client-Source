/*    */ package net.minecraft.item;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ 
/*    */ 
/*    */ public class ItemCoal
/*    */   extends Item
/*    */ {
/*    */   public ItemCoal() {
/* 11 */     setHasSubtypes(true);
/* 12 */     setMaxDamage(0);
/* 13 */     setCreativeTab(CreativeTabs.tabMaterials);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUnlocalizedName(ItemStack stack) {
/* 18 */     return (stack.getMetadata() == 1) ? "item.charcoal" : "item.coal";
/*    */   }
/*    */ 
/*    */   
/*    */   public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
/* 23 */     subItems.add(new ItemStack(itemIn, 1, 0));
/* 24 */     subItems.add(new ItemStack(itemIn, 1, 1));
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemCoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */