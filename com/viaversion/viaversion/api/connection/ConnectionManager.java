/*    */ package com.viaversion.viaversion.api.connection;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Set;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ConnectionManager
/*    */ {
/*    */   boolean isClientConnected(UUID paramUUID);
/*    */   
/*    */   default boolean isFrontEnd(UserConnection connection) {
/* 50 */     return !connection.isClientSide();
/*    */   }
/*    */   
/*    */   UserConnection getConnectedClient(UUID paramUUID);
/*    */   
/*    */   UUID getConnectedClientId(UserConnection paramUserConnection);
/*    */   
/*    */   Set<UserConnection> getConnections();
/*    */   
/*    */   Map<UUID, UserConnection> getConnectedClients();
/*    */   
/*    */   void onLoginSuccess(UserConnection paramUserConnection);
/*    */   
/*    */   void onDisconnect(UserConnection paramUserConnection);
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\api\connection\ConnectionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */