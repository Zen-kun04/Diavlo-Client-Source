/*    */ package rip.diavlo.base.modules.player;
/*    */ 
/*    */ import com.google.common.eventbus.Subscribe;
/*    */ import rip.diavlo.base.api.module.Category;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ import rip.diavlo.base.events.player.UpdateEvent;
/*    */ import rip.diavlo.base.utils.MinecraftUtil;
/*    */ 
/*    */ 
/*    */ public class FastBreak
/*    */   extends Module
/*    */ {
/*    */   public FastBreak() {
/* 14 */     super("FastBreak", 0, Category.PLAYER);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Subscribe
/*    */   public void onUpdate(UpdateEvent event) {
/* 21 */     if (MinecraftUtil.mc.playerController.curBlockDamageMP > 0.6F)
/* 22 */       MinecraftUtil.mc.playerController.curBlockDamageMP = 1.0F; 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\modules\player\FastBreak.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */