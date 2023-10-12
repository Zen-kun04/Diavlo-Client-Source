/*    */ package rip.diavlo.base.modules.player;
/*    */ 
/*    */ import rip.diavlo.base.api.module.Category;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ 
/*    */ public class FastPlace
/*    */   extends Module
/*    */ {
/*    */   public static boolean toggled = false;
/*    */   
/*    */   public FastPlace() {
/* 12 */     super("FastPlace", 0, Category.PLAYER);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 18 */     super.onEnable();
/* 19 */     toggled = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 24 */     super.onDisable();
/* 25 */     toggled = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\modules\player\FastPlace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */