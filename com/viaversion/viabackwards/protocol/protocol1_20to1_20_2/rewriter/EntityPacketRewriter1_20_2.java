/*     */ package com.viaversion.viabackwards.protocol.protocol1_20to1_20_2.rewriter;
/*     */ 
/*     */ import com.viaversion.viabackwards.api.BackwardsProtocol;
/*     */ import com.viaversion.viabackwards.api.rewriters.EntityRewriter;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_20to1_20_2.Protocol1_20To1_20_2;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_20to1_20_2.storage.ConfigurationPacketStorage;
/*     */ import com.viaversion.viaversion.api.minecraft.GlobalPosition;
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
/*     */ import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ClientboundPackets1_19_4;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.packet.ClientboundPackets1_20_2;
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
/*     */ public final class EntityPacketRewriter1_20_2
/*     */   extends EntityRewriter<ClientboundPackets1_20_2, Protocol1_20To1_20_2>
/*     */ {
/*     */   public EntityPacketRewriter1_20_2(Protocol1_20To1_20_2 protocol) {
/*  37 */     super((BackwardsProtocol)protocol);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerPackets() {
/*  42 */     registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_20_2.ENTITY_METADATA, Types1_20_2.METADATA_LIST, Types1_20.METADATA_LIST);
/*  43 */     registerRemoveEntities((ClientboundPacketType)ClientboundPackets1_20_2.REMOVE_ENTITIES);
/*     */     
/*  45 */     ((Protocol1_20To1_20_2)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_20_2.SPAWN_ENTITY, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           protected void register() {
/*  48 */             handler(wrapper -> {
/*     */                   int entityId = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */                   
/*     */                   wrapper.passthrough(Type.UUID);
/*     */                   
/*     */                   int entityType = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
/*     */                   
/*     */                   if (entityType != Entity1_19_4Types.PLAYER.getId()) {
/*     */                     wrapper.write((Type)Type.VAR_INT, Integer.valueOf(entityType));
/*     */                     
/*     */                     if (entityType == Entity1_19_4Types.FALLING_BLOCK.getId()) {
/*     */                       wrapper.passthrough((Type)Type.DOUBLE);
/*     */                       
/*     */                       wrapper.passthrough((Type)Type.DOUBLE);
/*     */                       
/*     */                       wrapper.passthrough((Type)Type.DOUBLE);
/*     */                       
/*     */                       wrapper.passthrough((Type)Type.BYTE);
/*     */                       
/*     */                       wrapper.passthrough((Type)Type.BYTE);
/*     */                       
/*     */                       wrapper.passthrough((Type)Type.BYTE);
/*     */                       
/*     */                       int blockState = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
/*     */                       
/*     */                       wrapper.write((Type)Type.VAR_INT, Integer.valueOf(((Protocol1_20To1_20_2)EntityPacketRewriter1_20_2.this.protocol).getMappingData().getNewBlockStateId(blockState)));
/*     */                     } 
/*     */                     return;
/*     */                   } 
/*     */                   wrapper.setPacketType((PacketType)ClientboundPackets1_19_4.SPAWN_PLAYER);
/*     */                   wrapper.passthrough((Type)Type.DOUBLE);
/*     */                   wrapper.passthrough((Type)Type.DOUBLE);
/*     */                   wrapper.passthrough((Type)Type.DOUBLE);
/*     */                   byte pitch = ((Byte)wrapper.read((Type)Type.BYTE)).byteValue();
/*     */                   wrapper.passthrough((Type)Type.BYTE);
/*     */                   wrapper.write((Type)Type.BYTE, Byte.valueOf(pitch));
/*     */                   wrapper.read((Type)Type.BYTE);
/*     */                   wrapper.read((Type)Type.VAR_INT);
/*     */                   short velocityX = ((Short)wrapper.read((Type)Type.SHORT)).shortValue();
/*     */                   short velocityY = ((Short)wrapper.read((Type)Type.SHORT)).shortValue();
/*     */                   short velocityZ = ((Short)wrapper.read((Type)Type.SHORT)).shortValue();
/*     */                   if (velocityX == 0 && velocityY == 0 && velocityZ == 0) {
/*     */                     return;
/*     */                   }
/*     */                   wrapper.send(Protocol1_20To1_20_2.class);
/*     */                   wrapper.cancel();
/*     */                   PacketWrapper velocityPacket = wrapper.create((PacketType)ClientboundPackets1_19_4.ENTITY_VELOCITY);
/*     */                   velocityPacket.write((Type)Type.VAR_INT, Integer.valueOf(entityId));
/*     */                   velocityPacket.write((Type)Type.SHORT, Short.valueOf(velocityX));
/*     */                   velocityPacket.write((Type)Type.SHORT, Short.valueOf(velocityY));
/*     */                   velocityPacket.write((Type)Type.SHORT, Short.valueOf(velocityZ));
/*     */                   velocityPacket.send(Protocol1_20To1_20_2.class);
/*     */                 });
/*     */           }
/*     */         });
/* 103 */     ((Protocol1_20To1_20_2)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_20_2.JOIN_GAME, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 106 */             handler(wrapper -> {
/*     */                   ConfigurationPacketStorage configurationPacketStorage = (ConfigurationPacketStorage)wrapper.user().remove(ConfigurationPacketStorage.class);
/*     */                   
/*     */                   wrapper.passthrough((Type)Type.INT);
/*     */                   
/*     */                   wrapper.passthrough((Type)Type.BOOLEAN);
/*     */                   
/*     */                   String[] worlds = (String[])wrapper.read(Type.STRING_ARRAY);
/*     */                   
/*     */                   int maxPlayers = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
/*     */                   
/*     */                   int viewDistance = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
/*     */                   
/*     */                   int simulationDistance = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
/*     */                   
/*     */                   boolean reducedDebugInfo = ((Boolean)wrapper.read((Type)Type.BOOLEAN)).booleanValue();
/*     */                   
/*     */                   boolean showRespawnScreen = ((Boolean)wrapper.read((Type)Type.BOOLEAN)).booleanValue();
/*     */                   
/*     */                   wrapper.read((Type)Type.BOOLEAN);
/*     */                   
/*     */                   String dimensionType = (String)wrapper.read(Type.STRING);
/*     */                   String world = (String)wrapper.read(Type.STRING);
/*     */                   long seed = ((Long)wrapper.read((Type)Type.LONG)).longValue();
/*     */                   wrapper.write((Type)Type.UNSIGNED_BYTE, Short.valueOf(((Byte)wrapper.read((Type)Type.BYTE)).shortValue()));
/*     */                   wrapper.passthrough((Type)Type.BYTE);
/*     */                   wrapper.write(Type.STRING_ARRAY, worlds);
/*     */                   wrapper.write(Type.NBT, configurationPacketStorage.registry());
/*     */                   wrapper.write(Type.STRING, dimensionType);
/*     */                   wrapper.write(Type.STRING, world);
/*     */                   wrapper.write((Type)Type.LONG, Long.valueOf(seed));
/*     */                   wrapper.write((Type)Type.VAR_INT, Integer.valueOf(maxPlayers));
/*     */                   wrapper.write((Type)Type.VAR_INT, Integer.valueOf(viewDistance));
/*     */                   wrapper.write((Type)Type.VAR_INT, Integer.valueOf(simulationDistance));
/*     */                   wrapper.write((Type)Type.BOOLEAN, Boolean.valueOf(reducedDebugInfo));
/*     */                   wrapper.write((Type)Type.BOOLEAN, Boolean.valueOf(showRespawnScreen));
/*     */                   EntityPacketRewriter1_20_2.this.worldDataTrackerHandlerByKey().handle(wrapper);
/*     */                   wrapper.send(Protocol1_20To1_20_2.class);
/*     */                   wrapper.cancel();
/*     */                   PacketWrapper featuresPacket = wrapper.create((PacketType)ClientboundPackets1_19_4.UPDATE_ENABLED_FEATURES);
/*     */                   featuresPacket.write(Type.STRING_ARRAY, configurationPacketStorage.enabledFeatures());
/*     */                   featuresPacket.send(Protocol1_20To1_20_2.class);
/*     */                   configurationPacketStorage.sendQueuedPackets(wrapper.user());
/*     */                 });
/*     */           }
/*     */         });
/* 152 */     ((Protocol1_20To1_20_2)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_20_2.RESPAWN, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 155 */             handler(wrapper -> {
/*     */                   wrapper.passthrough(Type.STRING);
/*     */                   
/*     */                   wrapper.passthrough(Type.STRING);
/*     */                   
/*     */                   wrapper.passthrough((Type)Type.LONG);
/*     */                   
/*     */                   wrapper.write((Type)Type.UNSIGNED_BYTE, Short.valueOf(((Byte)wrapper.read((Type)Type.BYTE)).shortValue()));
/*     */                   wrapper.passthrough((Type)Type.BYTE);
/*     */                   wrapper.passthrough((Type)Type.BOOLEAN);
/*     */                   wrapper.passthrough((Type)Type.BOOLEAN);
/*     */                   GlobalPosition lastDeathPosition = (GlobalPosition)wrapper.read(Type.OPTIONAL_GLOBAL_POSITION);
/*     */                   int portalCooldown = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
/*     */                   wrapper.passthrough((Type)Type.BYTE);
/*     */                   wrapper.write(Type.OPTIONAL_GLOBAL_POSITION, lastDeathPosition);
/*     */                   wrapper.write((Type)Type.VAR_INT, Integer.valueOf(portalCooldown));
/*     */                 });
/* 172 */             handler(EntityPacketRewriter1_20_2.this.worldDataTrackerHandlerByKey());
/*     */           }
/*     */         });
/*     */     
/* 176 */     ((Protocol1_20To1_20_2)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_20_2.ENTITY_EFFECT, wrapper -> {
/*     */           wrapper.passthrough((Type)Type.VAR_INT);
/*     */           
/*     */           wrapper.write((Type)Type.VAR_INT, Integer.valueOf(((Integer)wrapper.read((Type)Type.VAR_INT)).intValue() + 1));
/*     */           wrapper.passthrough((Type)Type.BYTE);
/*     */           wrapper.passthrough((Type)Type.VAR_INT);
/*     */           wrapper.passthrough((Type)Type.BYTE);
/*     */           if (((Boolean)wrapper.passthrough((Type)Type.BOOLEAN)).booleanValue()) {
/*     */             wrapper.write(Type.NBT, wrapper.read(Type.NAMELESS_NBT));
/*     */           }
/*     */         });
/* 187 */     ((Protocol1_20To1_20_2)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_20_2.REMOVE_ENTITY_EFFECT, wrapper -> {
/*     */           wrapper.passthrough((Type)Type.VAR_INT);
/*     */           wrapper.write((Type)Type.VAR_INT, Integer.valueOf(((Integer)wrapper.read((Type)Type.VAR_INT)).intValue() + 1));
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerRewrites() {
/* 195 */     filter().handler((event, meta) -> meta.setMetaType(Types1_20.META_TYPES.byId(meta.metaType().typeId())));
/* 196 */     registerMetaTypeHandler(null, Types1_20.META_TYPES.blockStateType, Types1_20.META_TYPES.optionalBlockStateType, Types1_20.META_TYPES.particleType, null, null);
/*     */     
/* 198 */     filter().filterFamily((EntityType)Entity1_19_4Types.DISPLAY).removeIndex(10);
/*     */     
/* 200 */     filter().filterFamily((EntityType)Entity1_19_4Types.MINECART_ABSTRACT).index(11).handler((event, meta) -> {
/*     */           int blockState = ((Integer)meta.value()).intValue();
/*     */           meta.setValue(Integer.valueOf(((Protocol1_20To1_20_2)this.protocol).getMappingData().getNewBlockStateId(blockState)));
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityType typeFromId(int type) {
/* 208 */     return Entity1_19_4Types.getTypeFromId(type);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viabackwards\protocol\protocol1_20to1_20_2\rewriter\EntityPacketRewriter1_20_2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */