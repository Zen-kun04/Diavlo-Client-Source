/*    */ package rip.diavlo.base.events.network;
/*    */ 
/*    */ import net.minecraft.network.Packet;
/*    */ import rip.diavlo.base.api.event.Event;
/*    */ 
/*    */ public class SendPacketEvent
/*    */   extends Event {
/*    */   public SendPacketEvent(Packet<?> packet) {
/*  9 */     this.packet = packet;
/*    */   }
/*    */ 
/*    */   
/*    */   private final Packet<?> packet;
/*    */   
/*    */   public <T extends Packet<?>> T getPacket() {
/* 16 */     return (T)this.packet;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\events\network\SendPacketEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */