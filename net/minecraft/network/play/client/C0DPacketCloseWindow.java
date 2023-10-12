/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ 
/*    */ 
/*    */ public class C0DPacketCloseWindow
/*    */   implements Packet<INetHandlerPlayServer>
/*    */ {
/*    */   private int windowId;
/*    */   
/*    */   public C0DPacketCloseWindow() {}
/*    */   
/*    */   public C0DPacketCloseWindow(int windowId) {
/* 18 */     this.windowId = windowId;
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayServer handler) {
/* 23 */     handler.processCloseWindow(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 28 */     this.windowId = buf.readByte();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 33 */     buf.writeByte(this.windowId);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\client\C0DPacketCloseWindow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */