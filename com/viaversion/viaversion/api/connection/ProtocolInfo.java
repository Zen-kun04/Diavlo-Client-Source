/*    */ package com.viaversion.viaversion.api.connection;
/*    */ 
/*    */ import com.viaversion.viaversion.api.protocol.ProtocolPipeline;
/*    */ import com.viaversion.viaversion.api.protocol.packet.Direction;
/*    */ import com.viaversion.viaversion.api.protocol.packet.State;
/*    */ import java.util.UUID;
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
/*    */ public interface ProtocolInfo
/*    */ {
/*    */   @Deprecated
/*    */   default State getState() {
/* 41 */     return getServerState();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   State getClientState();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   State getServerState();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default State getState(Direction direction) {
/* 66 */     return (direction == Direction.CLIENTBOUND) ? getServerState() : getClientState();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default void setState(State state) {
/* 77 */     setClientState(state);
/* 78 */     setServerState(state);
/*    */   }
/*    */   
/*    */   void setClientState(State paramState);
/*    */   
/*    */   void setServerState(State paramState);
/*    */   
/*    */   int getProtocolVersion();
/*    */   
/*    */   void setProtocolVersion(int paramInt);
/*    */   
/*    */   int getServerProtocolVersion();
/*    */   
/*    */   void setServerProtocolVersion(int paramInt);
/*    */   
/*    */   String getUsername();
/*    */   
/*    */   void setUsername(String paramString);
/*    */   
/*    */   UUID getUuid();
/*    */   
/*    */   void setUuid(UUID paramUUID);
/*    */   
/*    */   ProtocolPipeline getPipeline();
/*    */   
/*    */   void setPipeline(ProtocolPipeline paramProtocolPipeline);
/*    */   
/*    */   @Deprecated
/*    */   UserConnection getUser();
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\api\connection\ProtocolInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */