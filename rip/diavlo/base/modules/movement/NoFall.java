/*    */ package rip.diavlo.base.modules.movement;
/*    */ 
/*    */ import com.google.common.eventbus.Subscribe;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.C03PacketPlayer;
/*    */ import rip.diavlo.base.api.module.Category;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ import rip.diavlo.base.events.player.UpdateEvent;
/*    */ 
/*    */ public class NoFall
/*    */   extends Module
/*    */ {
/*    */   public NoFall() {
/* 14 */     super("NoFall", 0, Category.MOVEMENT);
/*    */   }
/*    */ 
/*    */   
/*    */   @Subscribe
/*    */   public void onUpdate(UpdateEvent event) {
/* 20 */     this; if (mc.thePlayer.fallDistance > 2.0F) {
/* 21 */       this; mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer(true));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 27 */     super.onEnable();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 32 */     super.onDisable();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\modules\movement\NoFall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */