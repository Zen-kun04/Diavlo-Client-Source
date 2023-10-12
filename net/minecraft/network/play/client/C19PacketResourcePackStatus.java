/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ 
/*    */ public class C19PacketResourcePackStatus implements Packet<INetHandlerPlayServer> {
/*    */   private String hash;
/*    */   
/*    */   public void setStatus(Action status) {
/* 13 */     this.status = status;
/*    */   }
/*    */ 
/*    */   
/*    */   private Action status;
/*    */   
/*    */   public C19PacketResourcePackStatus() {}
/*    */   
/*    */   public C19PacketResourcePackStatus(String hashIn, Action statusIn) {
/* 22 */     if (hashIn.length() > 40)
/*    */     {
/* 24 */       hashIn = hashIn.substring(0, 40);
/*    */     }
/*    */     
/* 27 */     this.hash = hashIn;
/* 28 */     this.status = statusIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 33 */     this.hash = buf.readStringFromBuffer(40);
/* 34 */     this.status = (Action)buf.readEnumValue(Action.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 39 */     buf.writeString(this.hash);
/* 40 */     buf.writeEnumValue(this.status);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayServer handler) {
/* 45 */     handler.handleResourcePackStatus(this);
/*    */   }
/*    */   
/*    */   public enum Action
/*    */   {
/* 50 */     SUCCESSFULLY_LOADED,
/* 51 */     DECLINED,
/* 52 */     FAILED_DOWNLOAD,
/* 53 */     ACCEPTED;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\client\C19PacketResourcePackStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */