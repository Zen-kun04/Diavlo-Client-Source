/*    */ package rip.diavlo.base.modules.player;
/*    */ 
/*    */ import com.google.common.eventbus.Subscribe;
/*    */ import rip.diavlo.base.api.module.Category;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ import rip.diavlo.base.events.player.UpdateEvent;
/*    */ 
/*    */ public class GodMode
/*    */   extends Module
/*    */ {
/*    */   int delay;
/*    */   
/*    */   public GodMode() {
/* 14 */     super("GodMode", 0, Category.PLAYER);
/*    */   }
/*    */ 
/*    */   
/*    */   @Subscribe
/*    */   public void onUpdate(UpdateEvent event) {
/* 20 */     if (this.delay <= 159)
/* 21 */       this.delay++; 
/* 22 */     if (this.delay > 159) {
/* 23 */       mc.thePlayer.sendChatMessage("/delhome home");
/* 24 */       mc.thePlayer.sendChatMessage("/sethome home");
/* 25 */       mc.thePlayer.sendChatMessage("/home home");
/* 26 */       this.delay = 0;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSuffix() {
/* 33 */     return String.valueOf(this.delay);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\modules\player\GodMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */