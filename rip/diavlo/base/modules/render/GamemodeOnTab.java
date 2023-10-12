/*    */ package rip.diavlo.base.modules.render;
/*    */ 
/*    */ import rip.diavlo.base.Client;
/*    */ import rip.diavlo.base.api.module.Category;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ 
/*    */ public class GamemodeOnTab
/*    */   extends Module
/*    */ {
/*    */   public GamemodeOnTab() {
/* 11 */     super("GamemodeOnTab", 0, Category.RENDER);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 16 */     super.onEnable();
/* 17 */     Client.getInstance().toggleCreativeOnTab();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 22 */     super.onDisable();
/* 23 */     Client.getInstance().toggleCreativeOnTab();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\modules\render\GamemodeOnTab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */