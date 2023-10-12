/*    */ package net.minecraft.network.login.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.login.INetHandlerLoginClient;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ 
/*    */ 
/*    */ public class S00PacketDisconnect
/*    */   implements Packet<INetHandlerLoginClient>
/*    */ {
/*    */   private IChatComponent reason;
/*    */   
/*    */   public S00PacketDisconnect() {}
/*    */   
/*    */   public S00PacketDisconnect(IChatComponent reasonIn) {
/* 19 */     this.reason = reasonIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 24 */     this.reason = buf.readChatComponent();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 29 */     buf.writeChatComponent(this.reason);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerLoginClient handler) {
/* 34 */     handler.handleDisconnect(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public IChatComponent func_149603_c() {
/* 39 */     return this.reason;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\login\server\S00PacketDisconnect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */