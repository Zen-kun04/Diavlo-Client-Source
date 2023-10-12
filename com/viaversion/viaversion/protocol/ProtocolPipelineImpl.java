/*     */ package com.viaversion.viaversion.protocol;
/*     */ 
/*     */ import com.viaversion.viaversion.api.Via;
/*     */ import com.viaversion.viaversion.api.connection.UserConnection;
/*     */ import com.viaversion.viaversion.api.debug.DebugHandler;
/*     */ import com.viaversion.viaversion.api.platform.ViaPlatform;
/*     */ import com.viaversion.viaversion.api.protocol.AbstractSimpleProtocol;
/*     */ import com.viaversion.viaversion.api.protocol.Protocol;
/*     */ import com.viaversion.viaversion.api.protocol.ProtocolPipeline;
/*     */ import com.viaversion.viaversion.api.protocol.packet.Direction;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.packet.State;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.logging.Level;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProtocolPipelineImpl
/*     */   extends AbstractSimpleProtocol
/*     */   implements ProtocolPipeline
/*     */ {
/*     */   private final UserConnection userConnection;
/*  45 */   private final List<Protocol> protocolList = new CopyOnWriteArrayList<>();
/*  46 */   private final Set<Class<? extends Protocol>> protocolSet = new HashSet<>();
/*  47 */   private List<Protocol> reversedProtocolList = new CopyOnWriteArrayList<>();
/*     */   private int baseProtocols;
/*     */   
/*     */   public ProtocolPipelineImpl(UserConnection userConnection) {
/*  51 */     this.userConnection = userConnection;
/*  52 */     userConnection.getProtocolInfo().setPipeline(this);
/*  53 */     registerPackets();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerPackets() {
/*  59 */     Protocol<?, ?, ?, ?> baseProtocol = Via.getManager().getProtocolManager().getBaseProtocol();
/*  60 */     this.protocolList.add(baseProtocol);
/*  61 */     this.reversedProtocolList.add(baseProtocol);
/*  62 */     this.protocolSet.add(baseProtocol.getClass());
/*  63 */     this.baseProtocols++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void init(UserConnection userConnection) {
/*  68 */     throw new UnsupportedOperationException("ProtocolPipeline can only be initialized once");
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void add(Protocol protocol) {
/*  73 */     if (protocol.isBaseProtocol()) {
/*     */       
/*  75 */       this.protocolList.add(this.baseProtocols, protocol);
/*  76 */       this.reversedProtocolList.add(this.baseProtocols, protocol);
/*  77 */       this.baseProtocols++;
/*     */     } else {
/*  79 */       this.protocolList.add(protocol);
/*  80 */       this.reversedProtocolList.add(0, protocol);
/*     */     } 
/*     */     
/*  83 */     this.protocolSet.add(protocol.getClass());
/*  84 */     protocol.init(this.userConnection);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void add(Collection<Protocol> protocols) {
/*  89 */     this.protocolList.addAll(protocols);
/*  90 */     for (Protocol protocol : protocols) {
/*  91 */       protocol.init(this.userConnection);
/*  92 */       this.protocolSet.add(protocol.getClass());
/*     */     } 
/*     */     
/*  95 */     refreshReversedList();
/*     */   }
/*     */   
/*     */   private synchronized void refreshReversedList() {
/*  99 */     List<Protocol> protocols = new ArrayList<>(this.protocolList.subList(0, this.baseProtocols));
/* 100 */     List<Protocol> additionalProtocols = new ArrayList<>(this.protocolList.subList(this.baseProtocols, this.protocolList.size()));
/* 101 */     Collections.reverse(additionalProtocols);
/* 102 */     protocols.addAll(additionalProtocols);
/* 103 */     this.reversedProtocolList = new CopyOnWriteArrayList<>(protocols);
/*     */   }
/*     */ 
/*     */   
/*     */   public void transform(Direction direction, State state, PacketWrapper packetWrapper) throws Exception {
/* 108 */     int originalID = packetWrapper.getId();
/*     */     
/* 110 */     DebugHandler debugHandler = Via.getManager().debugHandler();
/* 111 */     if (debugHandler.enabled() && !debugHandler.logPostPacketTransform() && debugHandler.shouldLog(packetWrapper, direction)) {
/* 112 */       logPacket(direction, state, packetWrapper, originalID);
/*     */     }
/*     */ 
/*     */     
/* 116 */     packetWrapper.apply(direction, state, 0, protocolListFor(direction));
/* 117 */     super.transform(direction, state, packetWrapper);
/*     */     
/* 119 */     if (debugHandler.enabled() && debugHandler.logPostPacketTransform() && debugHandler.shouldLog(packetWrapper, direction)) {
/* 120 */       logPacket(direction, state, packetWrapper, originalID);
/*     */     }
/*     */   }
/*     */   
/*     */   private List<Protocol> protocolListFor(Direction direction) {
/* 125 */     return Collections.unmodifiableList((direction == Direction.SERVERBOUND) ? this.protocolList : this.reversedProtocolList);
/*     */   }
/*     */ 
/*     */   
/*     */   private void logPacket(Direction direction, State state, PacketWrapper packetWrapper, int originalID) {
/* 130 */     int clientProtocol = this.userConnection.getProtocolInfo().getProtocolVersion();
/* 131 */     ViaPlatform<?> platform = Via.getPlatform();
/*     */     
/* 133 */     String actualUsername = packetWrapper.user().getProtocolInfo().getUsername();
/* 134 */     String username = (actualUsername != null) ? (actualUsername + " ") : "";
/*     */     
/* 136 */     platform.getLogger().log(Level.INFO, "{0}{1} {2}: {3} ({4}) -> {5} ({6}) [{7}] {8}", new Object[] { username, direction, state, 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 141 */           Integer.valueOf(originalID), 
/* 142 */           AbstractSimpleProtocol.toNiceHex(originalID), 
/* 143 */           Integer.valueOf(packetWrapper.getId()), 
/* 144 */           AbstractSimpleProtocol.toNiceHex(packetWrapper.getId()), 
/* 145 */           Integer.toString(clientProtocol), packetWrapper });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Class<? extends Protocol> protocolClass) {
/* 152 */     return this.protocolSet.contains(protocolClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public <P extends Protocol> P getProtocol(Class<P> pipeClass) {
/* 157 */     for (Protocol protocol : this.protocolList) {
/* 158 */       if (protocol.getClass() == pipeClass) {
/* 159 */         return (P)protocol;
/*     */       }
/*     */     } 
/* 162 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Protocol> pipes() {
/* 167 */     return Collections.unmodifiableList(this.protocolList);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Protocol> reversedPipes() {
/* 172 */     return Collections.unmodifiableList(this.reversedProtocolList);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasNonBaseProtocols() {
/* 177 */     for (Protocol protocol : this.protocolList) {
/* 178 */       if (!protocol.isBaseProtocol()) {
/* 179 */         return true;
/*     */       }
/*     */     } 
/* 182 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void cleanPipes() {
/* 187 */     registerPackets();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocol\ProtocolPipelineImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */