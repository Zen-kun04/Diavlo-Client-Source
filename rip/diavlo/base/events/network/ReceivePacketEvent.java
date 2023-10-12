/*    */ package rip.diavlo.base.events.network;
/*    */ 
/*    */ import net.minecraft.network.Packet;
/*    */ import rip.diavlo.base.api.event.Event;
/*    */ 
/*    */ public class ReceivePacketEvent
/*    */   extends Event {
/*    */   public ReceivePacketEvent(Packet packet) {
/*  9 */     this.packet = packet;
/*    */   } private final Packet packet;
/*    */   public Packet getPacket() {
/* 12 */     return this.packet;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\events\network\ReceivePacketEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */