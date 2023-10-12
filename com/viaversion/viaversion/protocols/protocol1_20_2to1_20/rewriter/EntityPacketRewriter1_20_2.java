/*     */ package com.viaversion.viaversion.protocols.protocol1_20_2to1_20.rewriter;
/*     */ 
/*     */ import com.viaversion.viaversion.api.minecraft.entities.Entity1_19_4Types;
/*     */ import com.viaversion.viaversion.api.minecraft.entities.EntityType;
/*     */ import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
/*     */ import com.viaversion.viaversion.api.protocol.Protocol;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import com.viaversion.viaversion.api.type.types.version.Types1_20;
/*     */ import com.viaversion.viaversion.api.type.types.version.Types1_20_2;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ClientboundPackets1_19_4;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.Protocol1_20_2To1_20;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.packet.ClientboundPackets1_20_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.storage.ConfigurationState;
/*     */ import com.viaversion.viaversion.rewriter.EntityRewriter;
/*     */ import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
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
/*     */ public final class EntityPacketRewriter1_20_2
/*     */   extends EntityRewriter<ClientboundPackets1_19_4, Protocol1_20_2To1_20>
/*     */ {
/*     */   public EntityPacketRewriter1_20_2(Protocol1_20_2To1_20 protocol) {
/*  37 */     super((Protocol)protocol);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerPackets() {
/*  42 */     registerTrackerWithData1_19((ClientboundPacketType)ClientboundPackets1_19_4.SPAWN_ENTITY, (EntityType)Entity1_19_4Types.FALLING_BLOCK);
/*  43 */     registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_19_4.ENTITY_METADATA, Types1_20.METADATA_LIST, Types1_20_2.METADATA_LIST);
/*  44 */     registerRemoveEntities((ClientboundPacketType)ClientboundPackets1_19_4.REMOVE_ENTITIES);
/*     */     
/*  46 */     ((Protocol1_20_2To1_20)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.SPAWN_PLAYER, (ClientboundPacketType)ClientboundPackets1_20_2.SPAWN_ENTITY, wrapper -> {
/*     */           wrapper.passthrough((Type)Type.VAR_INT);
/*     */           
/*     */           wrapper.passthrough(Type.UUID);
/*     */           
/*     */           wrapper.write((Type)Type.VAR_INT, Integer.valueOf(Entity1_19_4Types.PLAYER.getId()));
/*     */           
/*     */           wrapper.passthrough((Type)Type.DOUBLE);
/*     */           
/*     */           wrapper.passthrough((Type)Type.DOUBLE);
/*     */           wrapper.passthrough((Type)Type.DOUBLE);
/*     */           byte yaw = ((Byte)wrapper.read((Type)Type.BYTE)).byteValue();
/*     */           wrapper.passthrough((Type)Type.BYTE);
/*     */           wrapper.write((Type)Type.BYTE, Byte.valueOf(yaw));
/*     */           wrapper.write((Type)Type.BYTE, Byte.valueOf(yaw));
/*     */           wrapper.write((Type)Type.VAR_INT, Integer.valueOf(0));
/*     */           wrapper.write((Type)Type.SHORT, Short.valueOf((short)0));
/*     */           wrapper.write((Type)Type.SHORT, Short.valueOf((short)0));
/*     */           wrapper.write((Type)Type.SHORT, Short.valueOf((short)0));
/*     */         });
/*  66 */     ((Protocol1_20_2To1_20)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.JOIN_GAME, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/*  69 */             handler(wrapper -> {
/*     */                   wrapper.passthrough((Type)Type.INT);
/*     */                   
/*     */                   wrapper.passthrough((Type)Type.BOOLEAN);
/*     */                   
/*     */                   byte gamemode = ((Short)wrapper.read((Type)Type.UNSIGNED_BYTE)).byteValue();
/*     */                   
/*     */                   byte previousGamemode = ((Byte)wrapper.read((Type)Type.BYTE)).byteValue();
/*     */                   
/*     */                   wrapper.passthrough(Type.STRING_ARRAY);
/*     */                   
/*     */                   CompoundTag dimensionRegistry = (CompoundTag)wrapper.read(Type.NBT);
/*     */                   
/*     */                   String dimensionType = (String)wrapper.read(Type.STRING);
/*     */                   
/*     */                   String world = (String)wrapper.read(Type.STRING);
/*     */                   
/*     */                   long seed = ((Long)wrapper.read((Type)Type.LONG)).longValue();
/*     */                   
/*     */                   EntityPacketRewriter1_20_2.this.trackBiomeSize(wrapper.user(), dimensionRegistry);
/*     */                   
/*     */                   EntityPacketRewriter1_20_2.this.cacheDimensionData(wrapper.user(), dimensionRegistry);
/*     */                   
/*     */                   wrapper.passthrough((Type)Type.VAR_INT);
/*     */                   
/*     */                   wrapper.passthrough((Type)Type.VAR_INT);
/*     */                   
/*     */                   wrapper.passthrough((Type)Type.VAR_INT);
/*     */                   
/*     */                   wrapper.passthrough((Type)Type.BOOLEAN);
/*     */                   
/*     */                   wrapper.passthrough((Type)Type.BOOLEAN);
/*     */                   
/*     */                   wrapper.write((Type)Type.BOOLEAN, Boolean.valueOf(false));
/*     */                   wrapper.write(Type.STRING, dimensionType);
/*     */                   wrapper.write(Type.STRING, world);
/*     */                   wrapper.write((Type)Type.LONG, Long.valueOf(seed));
/*     */                   wrapper.write((Type)Type.BYTE, Byte.valueOf(gamemode));
/*     */                   wrapper.write((Type)Type.BYTE, Byte.valueOf(previousGamemode));
/*     */                   ConfigurationState configurationBridge = (ConfigurationState)wrapper.user().get(ConfigurationState.class);
/*     */                   if (!configurationBridge.setLastDimensionRegistry(dimensionRegistry)) {
/*     */                     PacketWrapper clientInformationPacket = configurationBridge.clientInformationPacket(wrapper.user());
/*     */                     if (clientInformationPacket != null) {
/*     */                       clientInformationPacket.sendToServer(Protocol1_20_2To1_20.class);
/*     */                     }
/*     */                     return;
/*     */                   } 
/*     */                   if (configurationBridge.bridgePhase() == ConfigurationState.BridgePhase.NONE) {
/*     */                     PacketWrapper configurationPacket = wrapper.create((PacketType)ClientboundPackets1_20_2.START_CONFIGURATION);
/*     */                     configurationPacket.send(Protocol1_20_2To1_20.class);
/*     */                     configurationBridge.setBridgePhase(ConfigurationState.BridgePhase.REENTERING_CONFIGURATION);
/*     */                     configurationBridge.setJoinGamePacket(wrapper);
/*     */                     wrapper.cancel();
/*     */                     return;
/*     */                   } 
/*     */                   Protocol1_20_2To1_20.sendConfigurationPackets(wrapper.user(), dimensionRegistry, null);
/*     */                   configurationBridge.setJoinGamePacket(wrapper);
/*     */                   wrapper.cancel();
/*     */                 });
/* 128 */             handler(EntityPacketRewriter1_20_2.this.worldDataTrackerHandlerByKey());
/*     */           }
/*     */         });
/*     */     
/* 132 */     ((Protocol1_20_2To1_20)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.RESPAWN, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 135 */             handler(wrapper -> {
/*     */                   wrapper.passthrough(Type.STRING);
/*     */                   
/*     */                   wrapper.passthrough(Type.STRING);
/*     */                   
/*     */                   wrapper.passthrough((Type)Type.LONG);
/*     */                   
/*     */                   wrapper.write((Type)Type.BYTE, Byte.valueOf(((Short)wrapper.read((Type)Type.UNSIGNED_BYTE)).byteValue()));
/*     */                   
/*     */                   wrapper.passthrough((Type)Type.BYTE);
/*     */                   wrapper.passthrough((Type)Type.BOOLEAN);
/*     */                   wrapper.passthrough((Type)Type.BOOLEAN);
/*     */                   byte dataToKeep = ((Byte)wrapper.read((Type)Type.BYTE)).byteValue();
/*     */                   wrapper.passthrough(Type.OPTIONAL_GLOBAL_POSITION);
/*     */                   wrapper.passthrough((Type)Type.VAR_INT);
/*     */                   wrapper.write((Type)Type.BYTE, Byte.valueOf(dataToKeep));
/*     */                 });
/* 152 */             handler(EntityPacketRewriter1_20_2.this.worldDataTrackerHandlerByKey());
/*     */           }
/*     */         });
/*     */     
/* 156 */     ((Protocol1_20_2To1_20)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.ENTITY_EFFECT, wrapper -> {
/*     */           wrapper.passthrough((Type)Type.VAR_INT);
/*     */           
/*     */           wrapper.write((Type)Type.VAR_INT, Integer.valueOf(((Integer)wrapper.read((Type)Type.VAR_INT)).intValue() - 1));
/*     */           wrapper.passthrough((Type)Type.BYTE);
/*     */           wrapper.passthrough((Type)Type.VAR_INT);
/*     */           wrapper.passthrough((Type)Type.BYTE);
/*     */           if (((Boolean)wrapper.passthrough((Type)Type.BOOLEAN)).booleanValue()) {
/*     */             wrapper.write(Type.NAMELESS_NBT, wrapper.read(Type.NBT));
/*     */           }
/*     */         });
/* 167 */     ((Protocol1_20_2To1_20)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.REMOVE_ENTITY_EFFECT, wrapper -> {
/*     */           wrapper.passthrough((Type)Type.VAR_INT);
/*     */           wrapper.write((Type)Type.VAR_INT, Integer.valueOf(((Integer)wrapper.read((Type)Type.VAR_INT)).intValue() - 1));
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerRewrites() {
/* 175 */     filter().handler((event, meta) -> meta.setMetaType(Types1_20_2.META_TYPES.byId(meta.metaType().typeId())));
/* 176 */     registerMetaTypeHandler(null, Types1_20_2.META_TYPES.blockStateType, Types1_20_2.META_TYPES.optionalBlockStateType, Types1_20_2.META_TYPES.particleType);
/*     */     
/* 178 */     filter().filterFamily((EntityType)Entity1_19_4Types.DISPLAY).addIndex(10);
/*     */     
/* 180 */     filter().filterFamily((EntityType)Entity1_19_4Types.MINECART_ABSTRACT).index(11).handler((event, meta) -> {
/*     */           int blockState = ((Integer)meta.value()).intValue();
/*     */           meta.setValue(Integer.valueOf(((Protocol1_20_2To1_20)this.protocol).getMappingData().getNewBlockStateId(blockState)));
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityType typeFromId(int type) {
/* 188 */     return Entity1_19_4Types.getTypeFromId(type);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_20_2to1_20\rewriter\EntityPacketRewriter1_20_2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */