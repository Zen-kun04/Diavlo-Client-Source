/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ 
/*    */ 
/*    */ public class C00PacketKeepAlive
/*    */   implements Packet<INetHandlerPlayServer>
/*    */ {
/*    */   private int key;
/*    */   
/*    */   public C00PacketKeepAlive() {}
/*    */   
/*    */   public C00PacketKeepAlive(int key) {
/* 18 */     this.key = key;
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayServer handler) {
/* 23 */     handler.processKeepAlive(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 28 */     this.key = buf.readVarIntFromBuffer();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 33 */     buf.writeVarIntToBuffer(this.key);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getKey() {
/* 38 */     return this.key;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\client\C00PacketKeepAlive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */