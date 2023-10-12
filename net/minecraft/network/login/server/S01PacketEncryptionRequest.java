/*    */ package net.minecraft.network.login.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.security.PublicKey;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.login.INetHandlerLoginClient;
/*    */ import net.minecraft.util.CryptManager;
/*    */ 
/*    */ 
/*    */ public class S01PacketEncryptionRequest
/*    */   implements Packet<INetHandlerLoginClient>
/*    */ {
/*    */   private String hashedServerId;
/*    */   private PublicKey publicKey;
/*    */   private byte[] verifyToken;
/*    */   
/*    */   public S01PacketEncryptionRequest() {}
/*    */   
/*    */   public S01PacketEncryptionRequest(String serverId, PublicKey key, byte[] verifyToken) {
/* 22 */     this.hashedServerId = serverId;
/* 23 */     this.publicKey = key;
/* 24 */     this.verifyToken = verifyToken;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 29 */     this.hashedServerId = buf.readStringFromBuffer(20);
/* 30 */     this.publicKey = CryptManager.decodePublicKey(buf.readByteArray());
/* 31 */     this.verifyToken = buf.readByteArray();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 36 */     buf.writeString(this.hashedServerId);
/* 37 */     buf.writeByteArray(this.publicKey.getEncoded());
/* 38 */     buf.writeByteArray(this.verifyToken);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerLoginClient handler) {
/* 43 */     handler.handleEncryptionRequest(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getServerId() {
/* 48 */     return this.hashedServerId;
/*    */   }
/*    */ 
/*    */   
/*    */   public PublicKey getPublicKey() {
/* 53 */     return this.publicKey;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getVerifyToken() {
/* 58 */     return this.verifyToken;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\login\server\S01PacketEncryptionRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */