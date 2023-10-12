/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ 
/*    */ public class S00PacketKeepAlive
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int id;
/*    */   
/*    */   public S00PacketKeepAlive() {}
/*    */   
/*    */   public S00PacketKeepAlive(int idIn) {
/* 18 */     this.id = idIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 23 */     handler.handleKeepAlive(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 28 */     this.id = buf.readVarIntFromBuffer();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 33 */     buf.writeVarIntToBuffer(this.id);
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_149134_c() {
/* 38 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S00PacketKeepAlive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */