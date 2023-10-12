/*     */ package com.viaversion.viabackwards.protocol.protocol1_20to1_20_2;
/*     */ 
/*     */ import com.viaversion.viabackwards.api.BackwardsProtocol;
/*     */ import com.viaversion.viabackwards.api.data.BackwardsMappings;
/*     */ import com.viaversion.viabackwards.api.rewriters.SoundRewriter;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_20to1_20_2.rewriter.BlockItemPacketRewriter1_20_2;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_20to1_20_2.rewriter.EntityPacketRewriter1_20_2;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_20to1_20_2.storage.ConfigurationPacketStorage;
/*     */ import com.viaversion.viaversion.api.connection.StorableObject;
/*     */ import com.viaversion.viaversion.api.connection.UserConnection;
/*     */ import com.viaversion.viaversion.api.data.MappingData;
/*     */ import com.viaversion.viaversion.api.data.entity.EntityTracker;
/*     */ import com.viaversion.viaversion.api.minecraft.entities.Entity1_19_4Types;
/*     */ import com.viaversion.viaversion.api.minecraft.entities.EntityType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.Direction;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.packet.State;
/*     */ import com.viaversion.viaversion.api.rewriter.EntityRewriter;
/*     */ import com.viaversion.viaversion.api.rewriter.ItemRewriter;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import com.viaversion.viaversion.data.entity.EntityTrackerBase;
/*     */ import com.viaversion.viaversion.exception.CancelException;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
/*     */ import com.viaversion.viaversion.protocols.base.ClientboundLoginPackets;
/*     */ import com.viaversion.viaversion.protocols.base.ServerboundLoginPackets;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ClientboundPackets1_19_4;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ServerboundPackets1_19_4;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.Protocol1_20_2To1_20;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.packet.ClientboundConfigurationPackets1_20_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.packet.ClientboundPackets1_20_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.packet.ServerboundConfigurationPackets1_20_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.packet.ServerboundPackets1_20_2;
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
/*     */ public final class Protocol1_20To1_20_2
/*     */   extends BackwardsProtocol<ClientboundPackets1_20_2, ClientboundPackets1_19_4, ServerboundPackets1_20_2, ServerboundPackets1_19_4>
/*     */ {
/*  50 */   public static final BackwardsMappings MAPPINGS = new BackwardsMappings("1.20.2", "1.20", Protocol1_20_2To1_20.class);
/*  51 */   private final EntityPacketRewriter1_20_2 entityPacketRewriter = new EntityPacketRewriter1_20_2(this);
/*  52 */   private final BlockItemPacketRewriter1_20_2 itemPacketRewriter = new BlockItemPacketRewriter1_20_2(this);
/*     */   
/*     */   public Protocol1_20To1_20_2() {
/*  55 */     super(ClientboundPackets1_20_2.class, ClientboundPackets1_19_4.class, ServerboundPackets1_20_2.class, ServerboundPackets1_19_4.class);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerPackets() {
/*  60 */     super.registerPackets();
/*     */     
/*  62 */     SoundRewriter<ClientboundPackets1_20_2> soundRewriter = new SoundRewriter(this);
/*  63 */     soundRewriter.register1_19_3Sound((ClientboundPacketType)ClientboundPackets1_20_2.SOUND);
/*  64 */     soundRewriter.registerSound((ClientboundPacketType)ClientboundPackets1_20_2.ENTITY_SOUND);
/*  65 */     soundRewriter.registerStopSound((ClientboundPacketType)ClientboundPackets1_20_2.STOP_SOUND);
/*     */     
/*  67 */     registerClientbound((ClientboundPacketType)ClientboundPackets1_20_2.DISPLAY_SCOREBOARD, wrapper -> {
/*     */           int slot = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
/*     */           
/*     */           wrapper.write((Type)Type.BYTE, Byte.valueOf((byte)slot));
/*     */         });
/*  72 */     registerClientbound(State.LOGIN, ClientboundLoginPackets.GAME_PROFILE.getId(), ClientboundLoginPackets.GAME_PROFILE.getId(), wrapper -> {
/*     */           wrapper.user().put((StorableObject)new ConfigurationPacketStorage());
/*     */ 
/*     */ 
/*     */           
/*     */           wrapper.user().getProtocolInfo().setClientState(State.LOGIN);
/*     */ 
/*     */           
/*     */           wrapper.create((PacketType)ServerboundLoginPackets.LOGIN_ACKNOWLEDGED).sendToServer(Protocol1_20To1_20_2.class);
/*     */         });
/*     */ 
/*     */     
/*  84 */     registerClientbound(State.CONFIGURATION, ClientboundConfigurationPackets1_20_2.FINISH_CONFIGURATION.getId(), ClientboundConfigurationPackets1_20_2.FINISH_CONFIGURATION.getId(), wrapper -> {
/*     */           wrapper.cancel();
/*     */           
/*     */           wrapper.user().getProtocolInfo().setServerState(State.PLAY);
/*     */           
/*     */           ((ConfigurationPacketStorage)wrapper.user().get(ConfigurationPacketStorage.class)).setFinished(true);
/*     */           wrapper.create((PacketType)ServerboundConfigurationPackets1_20_2.FINISH_CONFIGURATION).sendToServer(Protocol1_20To1_20_2.class);
/*     */           wrapper.user().getProtocolInfo().setClientState(State.PLAY);
/*     */         });
/*  93 */     registerServerbound(State.LOGIN, ServerboundLoginPackets.HELLO.getId(), ServerboundLoginPackets.HELLO.getId(), wrapper -> {
/*     */           wrapper.passthrough(Type.STRING);
/*     */           
/*     */           UUID uuid = (UUID)wrapper.read(Type.OPTIONAL_UUID);
/*     */           
/*     */           wrapper.write(Type.UUID, (uuid != null) ? uuid : new UUID(0L, 0L));
/*     */         });
/*     */     
/* 101 */     registerClientbound((ClientboundPacketType)ClientboundPackets1_20_2.START_CONFIGURATION, null, wrapper -> {
/*     */           wrapper.cancel();
/*     */           
/*     */           wrapper.user().getProtocolInfo().setServerState(State.CONFIGURATION);
/*     */           
/*     */           PacketWrapper configAcknowledgedPacket = wrapper.create((PacketType)ServerboundPackets1_20_2.CONFIGURATION_ACKNOWLEDGED);
/*     */           configAcknowledgedPacket.sendToServer(Protocol1_20To1_20_2.class);
/*     */           wrapper.user().getProtocolInfo().setClientState(State.CONFIGURATION);
/*     */           wrapper.user().put((StorableObject)new ConfigurationPacketStorage());
/*     */         });
/* 111 */     cancelClientbound((ClientboundPacketType)ClientboundPackets1_20_2.PONG_RESPONSE);
/*     */ 
/*     */     
/* 114 */     registerClientbound(State.CONFIGURATION, ClientboundConfigurationPackets1_20_2.DISCONNECT.getId(), ClientboundPackets1_19_4.DISCONNECT.getId());
/* 115 */     registerClientbound(State.CONFIGURATION, ClientboundConfigurationPackets1_20_2.KEEP_ALIVE.getId(), ClientboundPackets1_19_4.KEEP_ALIVE.getId());
/* 116 */     registerClientbound(State.CONFIGURATION, ClientboundConfigurationPackets1_20_2.RESOURCE_PACK.getId(), ClientboundPackets1_19_4.RESOURCE_PACK.getId());
/* 117 */     registerClientbound(State.CONFIGURATION, ClientboundConfigurationPackets1_20_2.REGISTRY_DATA.getId(), -1, wrapper -> {
/*     */           wrapper.cancel();
/*     */           
/*     */           CompoundTag registry = (CompoundTag)wrapper.read(Type.NAMELESS_NBT);
/*     */           this.entityPacketRewriter.trackBiomeSize(wrapper.user(), registry);
/*     */           this.entityPacketRewriter.cacheDimensionData(wrapper.user(), registry);
/*     */           ((ConfigurationPacketStorage)wrapper.user().get(ConfigurationPacketStorage.class)).setRegistry(registry);
/*     */         });
/* 125 */     registerClientbound(State.CONFIGURATION, ClientboundConfigurationPackets1_20_2.UPDATE_ENABLED_FEATURES.getId(), -1, wrapper -> {
/*     */           wrapper.cancel();
/*     */           
/*     */           String[] enabledFeatures = (String[])wrapper.read(Type.STRING_ARRAY);
/*     */           ((ConfigurationPacketStorage)wrapper.user().get(ConfigurationPacketStorage.class)).setEnabledFeatures(enabledFeatures);
/*     */         });
/* 131 */     registerClientbound(State.CONFIGURATION, ClientboundConfigurationPackets1_20_2.UPDATE_TAGS.getId(), -1, wrapper -> {
/*     */           wrapper.cancel();
/*     */           ((ConfigurationPacketStorage)wrapper.user().get(ConfigurationPacketStorage.class)).addRawPacket(wrapper, (PacketType)ClientboundPackets1_19_4.TAGS);
/*     */         });
/* 135 */     registerClientbound(State.CONFIGURATION, ClientboundConfigurationPackets1_20_2.CUSTOM_PAYLOAD.getId(), -1, wrapper -> {
/*     */           wrapper.cancel();
/*     */           ((ConfigurationPacketStorage)wrapper.user().get(ConfigurationPacketStorage.class)).addRawPacket(wrapper, (PacketType)ClientboundPackets1_19_4.PLUGIN_MESSAGE);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void transform(Direction direction, State state, PacketWrapper wrapper) throws Exception {
/* 143 */     ConfigurationPacketStorage configurationPacketStorage = (ConfigurationPacketStorage)wrapper.user().get(ConfigurationPacketStorage.class);
/* 144 */     if (configurationPacketStorage == null || configurationPacketStorage.isFinished()) {
/* 145 */       super.transform(direction, state, wrapper);
/*     */       return;
/*     */     } 
/* 148 */     if (direction == Direction.CLIENTBOUND) {
/* 149 */       super.transform(direction, State.CONFIGURATION, wrapper);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 154 */     int id = wrapper.getId();
/* 155 */     if (id == ServerboundPackets1_19_4.CLIENT_SETTINGS.getId()) {
/* 156 */       wrapper.setPacketType((PacketType)ServerboundConfigurationPackets1_20_2.CLIENT_INFORMATION);
/* 157 */     } else if (id == ServerboundPackets1_19_4.PLUGIN_MESSAGE.getId()) {
/* 158 */       wrapper.setPacketType((PacketType)ServerboundConfigurationPackets1_20_2.CUSTOM_PAYLOAD);
/* 159 */     } else if (id == ServerboundPackets1_19_4.KEEP_ALIVE.getId()) {
/* 160 */       wrapper.setPacketType((PacketType)ServerboundConfigurationPackets1_20_2.KEEP_ALIVE);
/* 161 */     } else if (id == ServerboundPackets1_19_4.PONG.getId()) {
/* 162 */       wrapper.setPacketType((PacketType)ServerboundConfigurationPackets1_20_2.PONG);
/* 163 */     } else if (id == ServerboundPackets1_19_4.RESOURCE_PACK_STATUS.getId()) {
/* 164 */       wrapper.setPacketType((PacketType)ServerboundConfigurationPackets1_20_2.RESOURCE_PACK);
/*     */     } else {
/*     */       
/* 167 */       throw CancelException.generate();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerConfigurationChangeHandlers() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void init(UserConnection connection) {
/* 178 */     addEntityTracker(connection, (EntityTracker)new EntityTrackerBase(connection, (EntityType)Entity1_19_4Types.PLAYER));
/*     */   }
/*     */ 
/*     */   
/*     */   public BackwardsMappings getMappingData() {
/* 183 */     return MAPPINGS;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityRewriter<Protocol1_20To1_20_2> getEntityRewriter() {
/* 188 */     return (EntityRewriter<Protocol1_20To1_20_2>)this.entityPacketRewriter;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemRewriter<Protocol1_20To1_20_2> getItemRewriter() {
/* 193 */     return (ItemRewriter<Protocol1_20To1_20_2>)this.itemPacketRewriter;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viabackwards\protocol\protocol1_20to1_20_2\Protocol1_20To1_20_2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */