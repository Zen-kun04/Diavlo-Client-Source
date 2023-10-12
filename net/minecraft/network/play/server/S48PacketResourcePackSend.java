/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ 
/*    */ public class S48PacketResourcePackSend
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private String url;
/*    */   private String hash;
/*    */   
/*    */   public S48PacketResourcePackSend() {}
/*    */   
/*    */   public S48PacketResourcePackSend(String url, String hash) {
/* 19 */     this.url = url;
/* 20 */     this.hash = hash;
/*    */     
/* 22 */     if (hash.length() > 40)
/*    */     {
/* 24 */       throw new IllegalArgumentException("Hash is too long (max 40, was " + hash.length() + ")");
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 30 */     this.url = buf.readStringFromBuffer(32767);
/* 31 */     this.hash = buf.readStringFromBuffer(40);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 36 */     buf.writeString(this.url);
/* 37 */     buf.writeString(this.hash);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 42 */     handler.handleResourcePack(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getURL() {
/* 47 */     return this.url;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getHash() {
/* 52 */     return this.hash;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S48PacketResourcePackSend.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */