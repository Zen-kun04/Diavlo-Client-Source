/*    */ package net.minecraft.server.network;
/*    */ 
/*    */ import net.minecraft.network.EnumConnectionState;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.NetworkManager;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.handshake.INetHandlerHandshakeServer;
/*    */ import net.minecraft.network.handshake.client.C00Handshake;
/*    */ import net.minecraft.network.login.server.S00PacketDisconnect;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.ChatComponentText;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ 
/*    */ public class NetHandlerHandshakeTCP implements INetHandlerHandshakeServer {
/*    */   private final MinecraftServer server;
/*    */   private final NetworkManager networkManager;
/*    */   
/*    */   public NetHandlerHandshakeTCP(MinecraftServer serverIn, NetworkManager netManager) {
/* 19 */     this.server = serverIn;
/* 20 */     this.networkManager = netManager;
/*    */   }
/*    */ 
/*    */   
/*    */   public void processHandshake(C00Handshake packetIn) {
/* 25 */     switch (packetIn.getRequestedState()) {
/*    */       
/*    */       case LOGIN:
/* 28 */         this.networkManager.setConnectionState(EnumConnectionState.LOGIN);
/*    */         
/* 30 */         if (packetIn.getProtocolVersion() > 47) {
/*    */           
/* 32 */           ChatComponentText chatcomponenttext = new ChatComponentText("Outdated server! I'm still on 1.8.9");
/* 33 */           this.networkManager.sendPacket((Packet)new S00PacketDisconnect((IChatComponent)chatcomponenttext));
/* 34 */           this.networkManager.closeChannel((IChatComponent)chatcomponenttext);
/*    */         }
/* 36 */         else if (packetIn.getProtocolVersion() < 47) {
/*    */           
/* 38 */           ChatComponentText chatcomponenttext1 = new ChatComponentText("Outdated client! Please use 1.8.9");
/* 39 */           this.networkManager.sendPacket((Packet)new S00PacketDisconnect((IChatComponent)chatcomponenttext1));
/* 40 */           this.networkManager.closeChannel((IChatComponent)chatcomponenttext1);
/*    */         }
/*    */         else {
/*    */           
/* 44 */           this.networkManager.setNetHandler((INetHandler)new NetHandlerLoginServer(this.server, this.networkManager));
/*    */         } 
/*    */         return;
/*    */ 
/*    */       
/*    */       case STATUS:
/* 50 */         this.networkManager.setConnectionState(EnumConnectionState.STATUS);
/* 51 */         this.networkManager.setNetHandler((INetHandler)new NetHandlerStatusServer(this.server, this.networkManager));
/*    */         return;
/*    */     } 
/*    */     
/* 55 */     throw new UnsupportedOperationException("Invalid intention " + packetIn.getRequestedState());
/*    */   }
/*    */   
/*    */   public void onDisconnect(IChatComponent reason) {}
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\server\network\NetHandlerHandshakeTCP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */