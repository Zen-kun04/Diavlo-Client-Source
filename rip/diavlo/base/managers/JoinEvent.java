/*    */ package rip.diavlo.base.managers;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import rip.diavlo.base.api.event.Event;
/*    */ 
/*    */ public class JoinEvent
/*    */   extends Event {
/*    */   private final EntityPlayer player;
/*    */   
/*    */   public JoinEvent(EntityPlayer player) {
/* 11 */     this.player = player;
/*    */   }
/*    */   
/*    */   public EntityPlayer getPlayer() {
/* 15 */     return this.player;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\managers\JoinEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */