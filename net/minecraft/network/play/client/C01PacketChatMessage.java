/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ 
/*    */ 
/*    */ public class C01PacketChatMessage
/*    */   implements Packet<INetHandlerPlayServer>
/*    */ {
/*    */   private String message;
/*    */   
/*    */   public C01PacketChatMessage() {}
/*    */   
/*    */   public C01PacketChatMessage(String messageIn) {
/* 18 */     if (messageIn.length() > 100)
/*    */     {
/* 20 */       messageIn = messageIn.substring(0, 100);
/*    */     }
/*    */     
/* 23 */     this.message = messageIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 28 */     this.message = buf.readStringFromBuffer(100);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 33 */     buf.writeString(this.message);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayServer handler) {
/* 38 */     handler.processChatMessage(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 43 */     return this.message;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\client\C01PacketChatMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */