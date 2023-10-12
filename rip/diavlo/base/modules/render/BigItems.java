/*    */ package rip.diavlo.base.modules.render;
/*    */ 
/*    */ import rip.diavlo.base.api.module.Category;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ 
/*    */ public class BigItems
/*    */   extends Module
/*    */ {
/*    */   public static boolean scale = true;
/*    */   
/*    */   public BigItems() {
/* 12 */     super("BigItems", 0, Category.RENDER);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 17 */     super.onEnable();
/* 18 */     scale = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 23 */     super.onDisable();
/* 24 */     scale = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSuffix() {
/* 29 */     return "";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\modules\render\BigItems.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */