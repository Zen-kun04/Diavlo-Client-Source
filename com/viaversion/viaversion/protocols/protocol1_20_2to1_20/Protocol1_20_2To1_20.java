/*     */ package com.viaversion.viaversion.protocols.protocol1_20_2to1_20;
/*     */ 
/*     */ import com.viaversion.viaversion.api.Via;
/*     */ import com.viaversion.viaversion.api.connection.ProtocolInfo;
/*     */ import com.viaversion.viaversion.api.connection.StorableObject;
/*     */ import com.viaversion.viaversion.api.connection.UserConnection;
/*     */ import com.viaversion.viaversion.api.data.MappingData;
/*     */ import com.viaversion.viaversion.api.data.MappingDataBase;
/*     */ import com.viaversion.viaversion.api.data.entity.EntityTracker;
/*     */ import com.viaversion.viaversion.api.minecraft.entities.Entity1_19_4Types;
/*     */ import com.viaversion.viaversion.api.minecraft.entities.EntityType;
/*     */ import com.viaversion.viaversion.api.protocol.AbstractProtocol;
/*     */ import com.viaversion.viaversion.api.protocol.Protocol;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.Direction;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.State;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
/*     */ import com.viaversion.viaversion.api.rewriter.EntityRewriter;
/*     */ import com.viaversion.viaversion.api.rewriter.ItemRewriter;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import com.viaversion.viaversion.data.entity.EntityTrackerBase;
/*     */ import com.viaversion.viaversion.exception.CancelException;
/*     */ import com.viaversion.viaversion.libs.gson.JsonElement;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
/*     */ import com.viaversion.viaversion.protocols.base.ClientboundLoginPackets;
/*     */ import com.viaversion.viaversion.protocols.base.ServerboundLoginPackets;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ClientboundPackets1_19_4;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ServerboundPackets1_19_4;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.packet.ClientboundConfigurationPackets1_20_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.packet.ClientboundPackets1_20_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.packet.ServerboundConfigurationPackets1_20_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.packet.ServerboundPackets1_20_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.rewriter.BlockItemPacketRewriter1_20_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.rewriter.EntityPacketRewriter1_20_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.storage.ConfigurationState;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.storage.LastResourcePack;
/*     */ import com.viaversion.viaversion.rewriter.SoundRewriter;
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
/*     */ public final class Protocol1_20_2To1_20
/*     */   extends AbstractProtocol<ClientboundPackets1_19_4, ClientboundPackets1_20_2, ServerboundPackets1_19_4, ServerboundPackets1_20_2>
/*     */ {
/*  57 */   public static final MappingData MAPPINGS = (MappingData)new MappingDataBase("1.20", "1.20.2");
/*  58 */   private final EntityPacketRewriter1_20_2 entityPacketRewriter = new EntityPacketRewriter1_20_2(this);
/*  59 */   private final BlockItemPacketRewriter1_20_2 itemPacketRewriter = new BlockItemPacketRewriter1_20_2(this);
/*     */   
/*     */   public Protocol1_20_2To1_20() {
/*  62 */     super(ClientboundPackets1_19_4.class, ClientboundPackets1_20_2.class, ServerboundPackets1_19_4.class, ServerboundPackets1_20_2.class);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerPackets() {
/*  68 */     super.registerPackets();
/*     */     
/*  70 */     SoundRewriter<ClientboundPackets1_19_4> soundRewriter = new SoundRewriter((Protocol)this);
/*  71 */     soundRewriter.register1_19_3Sound((ClientboundPacketType)ClientboundPackets1_19_4.SOUND);
/*  72 */     soundRewriter.registerEntitySound((ClientboundPacketType)ClientboundPackets1_19_4.ENTITY_SOUND);
/*     */     
/*  74 */     registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.PLUGIN_MESSAGE, wrapper -> {
/*     */           String channel = (String)wrapper.passthrough(Type.STRING);
/*     */           if (channel.equals("minecraft:brand")) {
/*     */             wrapper.passthrough(Type.STRING);
/*     */             wrapper.read(Type.REMAINING_BYTES);
/*     */           } 
/*     */         });
/*  81 */     registerServerbound((ServerboundPacketType)ServerboundPackets1_20_2.PLUGIN_MESSAGE, wrapper -> {
/*     */           String channel = (String)wrapper.passthrough(Type.STRING);
/*     */           
/*     */           if (channel.equals("minecraft:brand")) {
/*     */             wrapper.passthrough(Type.STRING);
/*     */             wrapper.read(Type.REMAINING_BYTES);
/*     */           } 
/*     */         });
/*  89 */     registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.RESOURCE_PACK, wrapper -> {
/*     */           String url = (String)wrapper.passthrough(Type.STRING);
/*     */           
/*     */           String hash = (String)wrapper.passthrough(Type.STRING);
/*     */           boolean required = ((Boolean)wrapper.passthrough((Type)Type.BOOLEAN)).booleanValue();
/*     */           JsonElement prompt = (JsonElement)wrapper.passthrough(Type.OPTIONAL_COMPONENT);
/*     */           wrapper.user().put((StorableObject)new LastResourcePack(url, hash, required, prompt));
/*     */         });
/*  97 */     registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.DISPLAY_SCOREBOARD, wrapper -> {
/*     */           byte slot = ((Byte)wrapper.read((Type)Type.BYTE)).byteValue();
/*     */           
/*     */           wrapper.write((Type)Type.VAR_INT, Integer.valueOf(slot));
/*     */         });
/* 102 */     registerServerbound(State.LOGIN, ServerboundLoginPackets.HELLO.getId(), ServerboundLoginPackets.HELLO.getId(), wrapper -> {
/*     */           wrapper.passthrough(Type.STRING);
/*     */ 
/*     */ 
/*     */           
/*     */           UUID uuid = (UUID)wrapper.read(Type.UUID);
/*     */ 
/*     */ 
/*     */           
/*     */           wrapper.write(Type.OPTIONAL_UUID, uuid);
/*     */         });
/*     */ 
/*     */     
/* 115 */     registerClientbound(State.LOGIN, ClientboundLoginPackets.GAME_PROFILE.getId(), ClientboundLoginPackets.GAME_PROFILE.getId(), wrapper -> {
/*     */           ((ConfigurationState)wrapper.user().get(ConfigurationState.class)).setBridgePhase(ConfigurationState.BridgePhase.PROFILE_SENT);
/*     */           
/*     */           wrapper.user().getProtocolInfo().setServerState(State.PLAY);
/*     */         });
/* 120 */     registerServerbound(State.LOGIN, ServerboundLoginPackets.LOGIN_ACKNOWLEDGED.getId(), -1, wrapper -> {
/*     */           wrapper.cancel();
/*     */           
/*     */           wrapper.user().getProtocolInfo().setServerState(State.PLAY);
/*     */           
/*     */           ConfigurationState configurationState = (ConfigurationState)wrapper.user().get(ConfigurationState.class);
/*     */           
/*     */           configurationState.setBridgePhase(ConfigurationState.BridgePhase.CONFIGURATION);
/*     */           
/*     */           configurationState.sendQueuedPackets(wrapper.user());
/*     */         });
/* 131 */     registerServerbound(State.CONFIGURATION, ServerboundConfigurationPackets1_20_2.FINISH_CONFIGURATION.getId(), -1, wrapper -> {
/*     */           wrapper.cancel();
/*     */           
/*     */           wrapper.user().getProtocolInfo().setClientState(State.PLAY);
/*     */           
/*     */           ConfigurationState configurationState = (ConfigurationState)wrapper.user().get(ConfigurationState.class);
/*     */           configurationState.setBridgePhase(ConfigurationState.BridgePhase.NONE);
/*     */           configurationState.sendQueuedPackets(wrapper.user());
/*     */           configurationState.clear();
/*     */         });
/* 141 */     registerServerbound(State.CONFIGURATION, ServerboundConfigurationPackets1_20_2.CLIENT_INFORMATION.getId(), -1, wrapper -> {
/*     */           ConfigurationState.ClientInformation clientInformation = new ConfigurationState.ClientInformation((String)wrapper.read(Type.STRING), ((Byte)wrapper.read((Type)Type.BYTE)).byteValue(), ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue(), ((Boolean)wrapper.read((Type)Type.BOOLEAN)).booleanValue(), ((Short)wrapper.read((Type)Type.UNSIGNED_BYTE)).shortValue(), ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue(), ((Boolean)wrapper.read((Type)Type.BOOLEAN)).booleanValue(), ((Boolean)wrapper.read((Type)Type.BOOLEAN)).booleanValue());
/*     */ 
/*     */ 
/*     */           
/*     */           ConfigurationState configurationState = (ConfigurationState)wrapper.user().get(ConfigurationState.class);
/*     */ 
/*     */ 
/*     */           
/*     */           configurationState.setClientInformation(clientInformation);
/*     */ 
/*     */ 
/*     */           
/*     */           wrapper.cancel();
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 159 */     registerServerbound(State.CONFIGURATION, ServerboundConfigurationPackets1_20_2.CUSTOM_PAYLOAD.getId(), -1, queueServerboundPacket(ServerboundPackets1_20_2.PLUGIN_MESSAGE));
/* 160 */     registerServerbound(State.CONFIGURATION, ServerboundConfigurationPackets1_20_2.KEEP_ALIVE.getId(), -1, queueServerboundPacket(ServerboundPackets1_20_2.KEEP_ALIVE));
/* 161 */     registerServerbound(State.CONFIGURATION, ServerboundConfigurationPackets1_20_2.PONG.getId(), -1, queueServerboundPacket(ServerboundPackets1_20_2.PONG));
/*     */ 
/*     */     
/* 164 */     registerServerbound(State.CONFIGURATION, ServerboundConfigurationPackets1_20_2.RESOURCE_PACK.getId(), -1, PacketWrapper::cancel);
/*     */     
/* 166 */     cancelClientbound((ClientboundPacketType)ClientboundPackets1_19_4.UPDATE_ENABLED_FEATURES);
/* 167 */     registerServerbound((ServerboundPacketType)ServerboundPackets1_20_2.CONFIGURATION_ACKNOWLEDGED, null, wrapper -> {
/*     */           wrapper.cancel();
/*     */           
/*     */           ConfigurationState configurationState = (ConfigurationState)wrapper.user().get(ConfigurationState.class);
/*     */           
/*     */           if (configurationState.bridgePhase() != ConfigurationState.BridgePhase.REENTERING_CONFIGURATION) {
/*     */             return;
/*     */           }
/*     */           
/*     */           wrapper.user().getProtocolInfo().setClientState(State.CONFIGURATION);
/*     */           
/*     */           configurationState.setBridgePhase(ConfigurationState.BridgePhase.CONFIGURATION);
/*     */           LastResourcePack lastResourcePack = (LastResourcePack)wrapper.user().get(LastResourcePack.class);
/*     */           sendConfigurationPackets(wrapper.user(), configurationState.lastDimensionRegistry(), lastResourcePack);
/*     */         });
/* 182 */     cancelServerbound((ServerboundPacketType)ServerboundPackets1_20_2.CHUNK_BATCH_RECEIVED);
/*     */     
/* 184 */     registerServerbound((ServerboundPacketType)ServerboundPackets1_20_2.PING_REQUEST, null, wrapper -> {
/*     */           wrapper.cancel();
/*     */           long time = ((Long)wrapper.read((Type)Type.LONG)).longValue();
/*     */           PacketWrapper responsePacket = wrapper.create((PacketType)ClientboundPackets1_20_2.PONG_RESPONSE);
/*     */           responsePacket.write((Type)Type.LONG, Long.valueOf(time));
/*     */           responsePacket.sendFuture(Protocol1_20_2To1_20.class);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private PacketHandler queueServerboundPacket(ServerboundPackets1_20_2 packetType) {
/* 195 */     return wrapper -> {
/*     */         wrapper.setPacketType((PacketType)packetType);
/*     */         ((ConfigurationState)wrapper.user().get(ConfigurationState.class)).addPacketToQueue(wrapper, false);
/*     */         wrapper.cancel();
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public void transform(Direction direction, State state, PacketWrapper packetWrapper) throws Exception {
/* 204 */     if (direction == Direction.SERVERBOUND) {
/*     */       
/* 206 */       super.transform(direction, state, packetWrapper);
/*     */       
/*     */       return;
/*     */     } 
/* 210 */     ConfigurationState configurationBridge = (ConfigurationState)packetWrapper.user().get(ConfigurationState.class);
/* 211 */     if (configurationBridge == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 216 */     ConfigurationState.BridgePhase phase = configurationBridge.bridgePhase();
/* 217 */     if (phase == ConfigurationState.BridgePhase.NONE) {
/* 218 */       super.transform(direction, state, packetWrapper);
/*     */       
/*     */       return;
/*     */     } 
/* 222 */     if (phase == ConfigurationState.BridgePhase.PROFILE_SENT || phase == ConfigurationState.BridgePhase.REENTERING_CONFIGURATION) {
/*     */       
/* 224 */       configurationBridge.addPacketToQueue(packetWrapper, true);
/* 225 */       throw CancelException.generate();
/*     */     } 
/*     */     
/* 228 */     if (packetWrapper.getPacketType() == null || packetWrapper.getPacketType().state() != State.CONFIGURATION) {
/*     */       
/* 230 */       int unmappedId = packetWrapper.getId();
/* 231 */       if (unmappedId == ClientboundPackets1_19_4.JOIN_GAME.getId()) {
/* 232 */         super.transform(direction, State.PLAY, packetWrapper);
/*     */         
/*     */         return;
/*     */       } 
/* 236 */       if (configurationBridge.queuedOrSentJoinGame()) {
/* 237 */         if (!packetWrapper.user().isClientSide() && !Via.getPlatform().isProxy() && unmappedId == ClientboundPackets1_19_4.SYSTEM_CHAT.getId()) {
/*     */ 
/*     */           
/* 240 */           super.transform(direction, State.PLAY, packetWrapper);
/*     */           
/*     */           return;
/*     */         } 
/* 244 */         configurationBridge.addPacketToQueue(packetWrapper, true);
/* 245 */         throw CancelException.generate();
/*     */       } 
/*     */       
/* 248 */       if (unmappedId == ClientboundPackets1_19_4.PLUGIN_MESSAGE.getId()) {
/* 249 */         packetWrapper.setPacketType((PacketType)ClientboundConfigurationPackets1_20_2.CUSTOM_PAYLOAD);
/* 250 */       } else if (unmappedId == ClientboundPackets1_19_4.DISCONNECT.getId()) {
/* 251 */         packetWrapper.setPacketType((PacketType)ClientboundConfigurationPackets1_20_2.DISCONNECT);
/* 252 */       } else if (unmappedId == ClientboundPackets1_19_4.KEEP_ALIVE.getId()) {
/* 253 */         packetWrapper.setPacketType((PacketType)ClientboundConfigurationPackets1_20_2.KEEP_ALIVE);
/* 254 */       } else if (unmappedId == ClientboundPackets1_19_4.PING.getId()) {
/* 255 */         packetWrapper.setPacketType((PacketType)ClientboundConfigurationPackets1_20_2.PING);
/* 256 */       } else if (unmappedId == ClientboundPackets1_19_4.UPDATE_ENABLED_FEATURES.getId()) {
/* 257 */         packetWrapper.setPacketType((PacketType)ClientboundConfigurationPackets1_20_2.UPDATE_ENABLED_FEATURES);
/* 258 */       } else if (unmappedId == ClientboundPackets1_19_4.TAGS.getId()) {
/* 259 */         packetWrapper.setPacketType((PacketType)ClientboundConfigurationPackets1_20_2.UPDATE_TAGS);
/*     */       }
/*     */       else {
/*     */         
/* 263 */         configurationBridge.addPacketToQueue(packetWrapper, true);
/* 264 */         throw CancelException.generate();
/*     */       } 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 271 */     super.transform(direction, State.CONFIGURATION, packetWrapper);
/*     */   }
/*     */   
/*     */   public static void sendConfigurationPackets(UserConnection connection, CompoundTag dimensionRegistry, LastResourcePack lastResourcePack) throws Exception {
/* 275 */     ProtocolInfo protocolInfo = connection.getProtocolInfo();
/* 276 */     protocolInfo.setServerState(State.CONFIGURATION);
/*     */     
/* 278 */     PacketWrapper registryDataPacket = PacketWrapper.create((PacketType)ClientboundConfigurationPackets1_20_2.REGISTRY_DATA, connection);
/* 279 */     registryDataPacket.write(Type.NAMELESS_NBT, dimensionRegistry);
/* 280 */     registryDataPacket.send(Protocol1_20_2To1_20.class);
/*     */ 
/*     */ 
/*     */     
/* 284 */     PacketWrapper enableFeaturesPacket = PacketWrapper.create((PacketType)ClientboundConfigurationPackets1_20_2.UPDATE_ENABLED_FEATURES, connection);
/* 285 */     enableFeaturesPacket.write((Type)Type.VAR_INT, Integer.valueOf(1));
/* 286 */     enableFeaturesPacket.write(Type.STRING, "minecraft:vanilla");
/* 287 */     enableFeaturesPacket.send(Protocol1_20_2To1_20.class);
/*     */     
/* 289 */     if (lastResourcePack != null) {
/*     */       
/* 291 */       PacketWrapper resourcePackPacket = PacketWrapper.create((PacketType)ClientboundConfigurationPackets1_20_2.RESOURCE_PACK, connection);
/* 292 */       resourcePackPacket.write(Type.STRING, lastResourcePack.url());
/* 293 */       resourcePackPacket.write(Type.STRING, lastResourcePack.hash());
/* 294 */       resourcePackPacket.write((Type)Type.BOOLEAN, Boolean.valueOf(lastResourcePack.required()));
/* 295 */       resourcePackPacket.write(Type.OPTIONAL_COMPONENT, lastResourcePack.prompt());
/* 296 */       resourcePackPacket.send(Protocol1_20_2To1_20.class);
/*     */     } 
/*     */     
/* 299 */     PacketWrapper finishConfigurationPacket = PacketWrapper.create((PacketType)ClientboundConfigurationPackets1_20_2.FINISH_CONFIGURATION, connection);
/* 300 */     finishConfigurationPacket.send(Protocol1_20_2To1_20.class);
/*     */     
/* 302 */     protocolInfo.setServerState(State.PLAY);
/*     */   }
/*     */ 
/*     */   
/*     */   public MappingData getMappingData() {
/* 307 */     return MAPPINGS;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerConfigurationChangeHandlers() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void init(UserConnection user) {
/* 317 */     user.put((StorableObject)new ConfigurationState());
/* 318 */     addEntityTracker(user, (EntityTracker)new EntityTrackerBase(user, (EntityType)Entity1_19_4Types.PLAYER));
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityRewriter<Protocol1_20_2To1_20> getEntityRewriter() {
/* 323 */     return (EntityRewriter<Protocol1_20_2To1_20>)this.entityPacketRewriter;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemRewriter<Protocol1_20_2To1_20> getItemRewriter() {
/* 328 */     return (ItemRewriter<Protocol1_20_2To1_20>)this.itemPacketRewriter;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_20_2to1_20\Protocol1_20_2To1_20.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */