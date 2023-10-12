/*    */ package net.minecraft.network.handshake.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.EnumConnectionState;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.handshake.INetHandlerHandshakeServer;
/*    */ 
/*    */ public class C00Handshake implements Packet<INetHandlerHandshakeServer> {
/*    */   private int protocolVersion;
/*    */   private String ip;
/*    */   private int port;
/*    */   private EnumConnectionState requestedState;
/*    */   
/* 15 */   public String getIp() { return this.ip; } public void setIp(String ip) { this.ip = ip; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public C00Handshake() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public C00Handshake(int version, String ip, int port, EnumConnectionState requestedState) {
/* 25 */     this.protocolVersion = version;
/* 26 */     this.ip = ip;
/* 27 */     this.port = port;
/* 28 */     this.requestedState = requestedState;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 33 */     this.protocolVersion = buf.readVarIntFromBuffer();
/* 34 */     this.ip = buf.readStringFromBuffer(255);
/* 35 */     this.port = buf.readUnsignedShort();
/* 36 */     this.requestedState = EnumConnectionState.getById(buf.readVarIntFromBuffer());
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 41 */     buf.writeVarIntToBuffer(this.protocolVersion);
/* 42 */     buf.writeString(this.ip);
/* 43 */     buf.writeShort(this.port);
/* 44 */     buf.writeVarIntToBuffer(this.requestedState.getId());
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerHandshakeServer handler) {
/* 49 */     handler.processHandshake(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public EnumConnectionState getRequestedState() {
/* 54 */     return this.requestedState;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getProtocolVersion() {
/* 59 */     return this.protocolVersion;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\handshake\client\C00Handshake.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */