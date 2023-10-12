/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ 
/*    */ 
/*    */ public class S02PacketChat
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private IChatComponent chatComponent;
/*    */   private byte type;
/*    */   
/*    */   public S02PacketChat() {}
/*    */   
/*    */   public S02PacketChat(IChatComponent component) {
/* 20 */     this(component, (byte)1);
/*    */   }
/*    */ 
/*    */   
/*    */   public S02PacketChat(IChatComponent message, byte typeIn) {
/* 25 */     this.chatComponent = message;
/* 26 */     this.type = typeIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 31 */     this.chatComponent = buf.readChatComponent();
/* 32 */     this.type = buf.readByte();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 37 */     buf.writeChatComponent(this.chatComponent);
/* 38 */     buf.writeByte(this.type);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 43 */     handler.handleChat(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public IChatComponent getChatComponent() {
/* 48 */     return this.chatComponent;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isChat() {
/* 53 */     return (this.type == 1 || this.type == 2);
/*    */   }
/*    */ 
/*    */   
/*    */   public byte getType() {
/* 58 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S02PacketChat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */