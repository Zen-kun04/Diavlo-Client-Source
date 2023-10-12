/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ public class S46PacketSetCompressionLevel
/*    */   implements Packet<INetHandlerPlayClient> {
/*    */   private int threshold;
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 14 */     this.threshold = buf.readVarIntFromBuffer();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 19 */     buf.writeVarIntToBuffer(this.threshold);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 24 */     handler.handleSetCompressionLevel(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getThreshold() {
/* 29 */     return this.threshold;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S46PacketSetCompressionLevel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */