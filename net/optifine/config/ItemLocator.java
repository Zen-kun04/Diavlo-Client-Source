/*    */ package net.optifine.config;
/*    */ 
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class ItemLocator
/*    */   implements IObjectLocator
/*    */ {
/*    */   public Object getObject(ResourceLocation loc) {
/* 10 */     Item item = Item.getByNameOrId(loc.toString());
/* 11 */     return item;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\config\ItemLocator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */