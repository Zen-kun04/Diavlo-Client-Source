/*    */ package net.minecraft.network.play.handlehand;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ import net.minecraft.network.play.handlehand.hand.EnumHandSide;
/*    */ 
/*    */ public class CPacketAnimation implements Packet<INetHandlerPlayServer> {
/*    */   private EnumHandSide hand;
/*    */   
/*    */   public CPacketAnimation() {}
/*    */   
/*    */   public CPacketAnimation(EnumHandSide handIn) {
/* 16 */     this.hand = handIn;
/*    */   }
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 20 */     this.hand = (EnumHandSide)buf.readEnumValue(EnumHandSide.class);
/*    */   }
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 24 */     buf.writeEnumValue((Enum)this.hand);
/*    */   }
/*    */   
/*    */   public void processPacket(INetHandlerPlayServer handler) {
/* 28 */     handler.handleAnimation(this);
/*    */   }
/*    */   
/*    */   public EnumHandSide getHand() {
/* 32 */     return this.hand;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\handlehand\CPacketAnimation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */