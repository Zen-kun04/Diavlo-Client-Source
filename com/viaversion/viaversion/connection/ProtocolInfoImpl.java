/*     */ package com.viaversion.viaversion.connection;
/*     */ 
/*     */ import com.viaversion.viaversion.api.Via;
/*     */ import com.viaversion.viaversion.api.connection.ProtocolInfo;
/*     */ import com.viaversion.viaversion.api.connection.UserConnection;
/*     */ import com.viaversion.viaversion.api.protocol.ProtocolPipeline;
/*     */ import com.viaversion.viaversion.api.protocol.packet.State;
/*     */ import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
/*     */ import java.util.UUID;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProtocolInfoImpl
/*     */   implements ProtocolInfo
/*     */ {
/*     */   private final UserConnection connection;
/*  30 */   private State clientState = State.HANDSHAKE;
/*  31 */   private State serverState = State.HANDSHAKE;
/*  32 */   private int protocolVersion = -1;
/*  33 */   private int serverProtocolVersion = -1;
/*     */   private String username;
/*     */   private UUID uuid;
/*     */   private ProtocolPipeline pipeline;
/*     */   
/*     */   public ProtocolInfoImpl(UserConnection connection) {
/*  39 */     this.connection = connection;
/*     */   }
/*     */ 
/*     */   
/*     */   public State getClientState() {
/*  44 */     return this.clientState;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setClientState(State clientState) {
/*  49 */     if (Via.getManager().debugHandler().enabled()) {
/*  50 */       Via.getPlatform().getLogger().info("Client state changed from " + this.clientState + " to " + clientState + " for " + this.connection.getProtocolInfo().getUuid());
/*     */     }
/*  52 */     this.clientState = clientState;
/*     */   }
/*     */ 
/*     */   
/*     */   public State getServerState() {
/*  57 */     return this.serverState;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setServerState(State serverState) {
/*  62 */     if (Via.getManager().debugHandler().enabled()) {
/*  63 */       Via.getPlatform().getLogger().info("Server state changed from " + this.serverState + " to " + serverState + " for " + this.connection.getProtocolInfo().getUuid());
/*     */     }
/*  65 */     this.serverState = serverState;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getProtocolVersion() {
/*  70 */     return this.protocolVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProtocolVersion(int protocolVersion) {
/*  76 */     ProtocolVersion protocol = ProtocolVersion.getProtocol(protocolVersion);
/*  77 */     this.protocolVersion = protocol.getVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getServerProtocolVersion() {
/*  82 */     return this.serverProtocolVersion;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setServerProtocolVersion(int serverProtocolVersion) {
/*  87 */     ProtocolVersion protocol = ProtocolVersion.getProtocol(serverProtocolVersion);
/*  88 */     this.serverProtocolVersion = protocol.getVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUsername() {
/*  93 */     return this.username;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUsername(String username) {
/*  98 */     this.username = username;
/*     */   }
/*     */ 
/*     */   
/*     */   public UUID getUuid() {
/* 103 */     return this.uuid;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUuid(UUID uuid) {
/* 108 */     this.uuid = uuid;
/*     */   }
/*     */ 
/*     */   
/*     */   public ProtocolPipeline getPipeline() {
/* 113 */     return this.pipeline;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPipeline(ProtocolPipeline pipeline) {
/* 118 */     this.pipeline = pipeline;
/*     */   }
/*     */ 
/*     */   
/*     */   public UserConnection getUser() {
/* 123 */     return this.connection;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 128 */     return "ProtocolInfo{clientState=" + this.clientState + ", serverState=" + this.serverState + ", protocolVersion=" + this.protocolVersion + ", serverProtocolVersion=" + this.serverProtocolVersion + ", username='" + this.username + '\'' + ", uuid=" + this.uuid + '}';
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\connection\ProtocolInfoImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */