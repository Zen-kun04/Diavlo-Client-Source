/*     */ package com.viaversion.viaversion.protocols.base;
/*     */ 
/*     */ import com.viaversion.viaversion.api.Via;
/*     */ import com.viaversion.viaversion.api.connection.ProtocolInfo;
/*     */ import com.viaversion.viaversion.api.platform.providers.Provider;
/*     */ import com.viaversion.viaversion.api.platform.providers.ViaProviders;
/*     */ import com.viaversion.viaversion.api.protocol.AbstractProtocol;
/*     */ import com.viaversion.viaversion.api.protocol.Protocol;
/*     */ import com.viaversion.viaversion.api.protocol.ProtocolPathEntry;
/*     */ import com.viaversion.viaversion.api.protocol.ProtocolPipeline;
/*     */ import com.viaversion.viaversion.api.protocol.packet.Direction;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.packet.State;
/*     */ import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
/*     */ import com.viaversion.viaversion.api.protocol.version.VersionProvider;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class BaseProtocol
/*     */   extends AbstractProtocol
/*     */ {
/*     */   protected void registerPackets() {
/*  41 */     registerServerbound(ServerboundHandshakePackets.CLIENT_INTENTION, wrapper -> {
/*     */           int protocolVersion = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */           wrapper.passthrough(Type.STRING);
/*     */           wrapper.passthrough((Type)Type.UNSIGNED_SHORT);
/*     */           int state = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */           ProtocolInfo info = wrapper.user().getProtocolInfo();
/*     */           info.setProtocolVersion(protocolVersion);
/*     */           VersionProvider versionProvider = (VersionProvider)Via.getManager().getProviders().get(VersionProvider.class);
/*     */           if (versionProvider == null) {
/*     */             wrapper.user().setActive(false);
/*     */             return;
/*     */           } 
/*     */           int serverProtocol = versionProvider.getClosestServerProtocol(wrapper.user());
/*     */           info.setServerProtocolVersion(serverProtocol);
/*     */           List<ProtocolPathEntry> protocolPath = null;
/*     */           if (info.getProtocolVersion() >= serverProtocol || Via.getPlatform().isOldClientsAllowed()) {
/*     */             protocolPath = Via.getManager().getProtocolManager().getProtocolPath(info.getProtocolVersion(), serverProtocol);
/*     */           }
/*     */           ProtocolPipeline pipeline = wrapper.user().getProtocolInfo().getPipeline();
/*     */           if (protocolPath != null) {
/*     */             List<Protocol> protocols = new ArrayList<>(protocolPath.size());
/*     */             for (ProtocolPathEntry entry : protocolPath) {
/*     */               protocols.add(entry.protocol());
/*     */               Via.getManager().getProtocolManager().completeMappingDataLoading(entry.protocol().getClass());
/*     */             } 
/*     */             pipeline.add(protocols);
/*     */             ProtocolVersion protocol = ProtocolVersion.getProtocol(serverProtocol);
/*     */             wrapper.set((Type)Type.VAR_INT, 0, Integer.valueOf(protocol.getOriginalVersion()));
/*     */           } 
/*     */           pipeline.add(Via.getManager().getProtocolManager().getBaseProtocol(serverProtocol));
/*     */           if (state == 1) {
/*     */             info.setState(State.STATUS);
/*     */           } else if (state == 2) {
/*     */             info.setState(State.LOGIN);
/*     */           } 
/*     */         });
/*     */   }
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
/*     */   public boolean isBaseProtocol() {
/*  98 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void register(ViaProviders providers) {
/* 103 */     providers.register(VersionProvider.class, (Provider)new BaseVersionProvider());
/*     */   }
/*     */ 
/*     */   
/*     */   public void transform(Direction direction, State state, PacketWrapper packetWrapper) throws Exception {
/* 108 */     super.transform(direction, state, packetWrapper);
/* 109 */     if (direction == Direction.SERVERBOUND && state == State.HANDSHAKE)
/*     */     {
/* 111 */       if (packetWrapper.getId() != 0)
/* 112 */         packetWrapper.user().setActive(false); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\base\BaseProtocol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */