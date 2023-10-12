/*    */ package com.viaversion.viaversion.protocols.protocol1_9to1_8.providers;
/*    */ 
/*    */ import com.viaversion.viaversion.api.Via;
/*    */ import com.viaversion.viaversion.api.connection.UserConnection;
/*    */ import com.viaversion.viaversion.api.platform.providers.Provider;
/*    */ import com.viaversion.viaversion.api.protocol.packet.PacketType;
/*    */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*    */ import com.viaversion.viaversion.api.protocol.packet.State;
/*    */ import com.viaversion.viaversion.api.type.Type;
/*    */ import com.viaversion.viaversion.protocols.protocol1_8.ServerboundPackets1_8;
/*    */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
/*    */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.MovementTracker;
/*    */ import java.util.logging.Level;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MovementTransmitterProvider
/*    */   implements Provider
/*    */ {
/*    */   public void sendPlayer(UserConnection userConnection) {
/* 35 */     if (userConnection.getProtocolInfo().getClientState() != State.PLAY || userConnection.getEntityTracker(Protocol1_9To1_8.class).clientEntityId() == -1) {
/*    */       return;
/*    */     }
/*    */     
/* 39 */     MovementTracker movementTracker = (MovementTracker)userConnection.get(MovementTracker.class);
/* 40 */     movementTracker.incrementIdlePacket();
/*    */     
/*    */     try {
/* 43 */       PacketWrapper playerMovement = PacketWrapper.create((PacketType)ServerboundPackets1_8.PLAYER_MOVEMENT, userConnection);
/* 44 */       playerMovement.write((Type)Type.BOOLEAN, Boolean.valueOf(movementTracker.isGround()));
/* 45 */       playerMovement.scheduleSendToServer(Protocol1_9To1_8.class);
/* 46 */     } catch (Throwable e) {
/* 47 */       Via.getPlatform().getLogger().log(Level.WARNING, "Failed to send player movement packet", e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_9to1_8\providers\MovementTransmitterProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */