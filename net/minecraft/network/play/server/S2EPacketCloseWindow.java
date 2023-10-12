/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ 
/*    */ public class S2EPacketCloseWindow
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int windowId;
/*    */   
/*    */   public S2EPacketCloseWindow() {}
/*    */   
/*    */   public S2EPacketCloseWindow(int windowIdIn) {
/* 18 */     this.windowId = windowIdIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 23 */     handler.handleCloseWindow(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 28 */     this.windowId = buf.readUnsignedByte();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 33 */     buf.writeByte(this.windowId);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S2EPacketCloseWindow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */