/*     */ package com.viaversion.viaversion.protocols.protocol1_19to1_18_2.packets;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.viaversion.viaversion.api.Via;
/*     */ import com.viaversion.viaversion.api.data.ParticleMappings;
/*     */ import com.viaversion.viaversion.api.data.entity.DimensionData;
/*     */ import com.viaversion.viaversion.api.minecraft.Position;
/*     */ import com.viaversion.viaversion.api.minecraft.entities.Entity1_19Types;
/*     */ import com.viaversion.viaversion.api.minecraft.entities.EntityType;
/*     */ import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
/*     */ import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
/*     */ import com.viaversion.viaversion.api.minecraft.nbt.BinaryTagIO;
/*     */ import com.viaversion.viaversion.api.protocol.Protocol;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import com.viaversion.viaversion.api.type.types.Particle;
/*     */ import com.viaversion.viaversion.api.type.types.version.Types1_18;
/*     */ import com.viaversion.viaversion.api.type.types.version.Types1_19;
/*     */ import com.viaversion.viaversion.data.entity.DimensionDataImpl;
/*     */ import com.viaversion.viaversion.libs.gson.JsonElement;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
/*     */ import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.ClientboundPackets1_18;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.ClientboundPackets1_19;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.Protocol1_19To1_18_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.storage.DimensionRegistryStorage;
/*     */ import com.viaversion.viaversion.rewriter.EntityRewriter;
/*     */ import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
/*     */ import com.viaversion.viaversion.util.Key;
/*     */ import com.viaversion.viaversion.util.Pair;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public final class EntityPackets
/*     */   extends EntityRewriter<ClientboundPackets1_18, Protocol1_19To1_18_2>
/*     */ {
/*     */   private static final String CHAT_REGISTRY_SNBT = "{\n  \"minecraft:chat_type\": {\n    \"type\": \"minecraft:chat_type\",\n    \"value\": [\n      {\n        \"name\": \"minecraft:system\",\n        \"id\": 1,\n        \"element\": {\n          \"chat\": {},\n          \"narration\": {\n            \"priority\": \"system\"\n          }\n        }\n      },\n      {\n        \"name\": \"minecraft:game_info\",\n        \"id\": 2,\n        \"element\": {\n          \"overlay\": {}\n        }\n      }\n    ]\n  }\n}";
/*     */   public static final CompoundTag CHAT_REGISTRY;
/*     */   
/*     */   static {
/*     */     try {
/*  86 */       CHAT_REGISTRY = (CompoundTag)BinaryTagIO.readString("{\n  \"minecraft:chat_type\": {\n    \"type\": \"minecraft:chat_type\",\n    \"value\": [\n      {\n        \"name\": \"minecraft:system\",\n        \"id\": 1,\n        \"element\": {\n          \"chat\": {},\n          \"narration\": {\n            \"priority\": \"system\"\n          }\n        }\n      },\n      {\n        \"name\": \"minecraft:game_info\",\n        \"id\": 2,\n        \"element\": {\n          \"overlay\": {}\n        }\n      }\n    ]\n  }\n}").get("minecraft:chat_type");
/*  87 */     } catch (IOException e) {
/*  88 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public EntityPackets(Protocol1_19To1_18_2 protocol) {
/*  93 */     super((Protocol)protocol);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerPackets() {
/*  98 */     registerTracker((ClientboundPacketType)ClientboundPackets1_18.SPAWN_PLAYER, (EntityType)Entity1_19Types.PLAYER);
/*  99 */     registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_18.ENTITY_METADATA, Types1_18.METADATA_LIST, Types1_19.METADATA_LIST);
/* 100 */     registerRemoveEntities((ClientboundPacketType)ClientboundPackets1_18.REMOVE_ENTITIES);
/*     */     
/* 102 */     ((Protocol1_19To1_18_2)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_18.SPAWN_ENTITY, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 105 */             map((Type)Type.VAR_INT);
/* 106 */             map(Type.UUID);
/* 107 */             map((Type)Type.VAR_INT);
/* 108 */             map((Type)Type.DOUBLE);
/* 109 */             map((Type)Type.DOUBLE);
/* 110 */             map((Type)Type.DOUBLE);
/* 111 */             map((Type)Type.BYTE);
/* 112 */             map((Type)Type.BYTE);
/* 113 */             handler(wrapper -> {
/*     */                   byte yaw = ((Byte)wrapper.get((Type)Type.BYTE, 1)).byteValue();
/*     */                   wrapper.write((Type)Type.BYTE, Byte.valueOf(yaw));
/*     */                 });
/* 117 */             map((Type)Type.INT, (Type)Type.VAR_INT);
/* 118 */             handler(EntityPackets.this.trackerHandler());
/* 119 */             handler(wrapper -> {
/*     */                   int entityId = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
/*     */                   
/*     */                   EntityType entityType = EntityPackets.this.tracker(wrapper.user()).entityType(entityId);
/*     */                   if (entityType == Entity1_19Types.FALLING_BLOCK) {
/*     */                     wrapper.set((Type)Type.VAR_INT, 2, Integer.valueOf(((Protocol1_19To1_18_2)EntityPackets.this.protocol).getMappingData().getNewBlockStateId(((Integer)wrapper.get((Type)Type.VAR_INT, 2)).intValue())));
/*     */                   }
/*     */                 });
/*     */           }
/*     */         });
/* 129 */     ((Protocol1_19To1_18_2)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_18.SPAWN_PAINTING, (ClientboundPacketType)ClientboundPackets1_19.SPAWN_ENTITY, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 132 */             map((Type)Type.VAR_INT);
/* 133 */             map(Type.UUID);
/* 134 */             handler(wrapper -> {
/*     */                   wrapper.write((Type)Type.VAR_INT, Integer.valueOf(Entity1_19Types.PAINTING.getId()));
/*     */                   
/*     */                   int motive = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
/*     */                   
/*     */                   Position blockPosition = (Position)wrapper.read(Type.POSITION1_14);
/*     */                   
/*     */                   byte direction = ((Byte)wrapper.read((Type)Type.BYTE)).byteValue();
/*     */                   
/*     */                   wrapper.write((Type)Type.DOUBLE, Double.valueOf(blockPosition.x() + 0.5D));
/*     */                   
/*     */                   wrapper.write((Type)Type.DOUBLE, Double.valueOf(blockPosition.y() + 0.5D));
/*     */                   wrapper.write((Type)Type.DOUBLE, Double.valueOf(blockPosition.z() + 0.5D));
/*     */                   wrapper.write((Type)Type.BYTE, Byte.valueOf((byte)0));
/*     */                   wrapper.write((Type)Type.BYTE, Byte.valueOf((byte)0));
/*     */                   wrapper.write((Type)Type.BYTE, Byte.valueOf((byte)0));
/*     */                   wrapper.write((Type)Type.VAR_INT, Integer.valueOf(EntityPackets.to3dId(direction)));
/*     */                   wrapper.write((Type)Type.SHORT, Short.valueOf((short)0));
/*     */                   wrapper.write((Type)Type.SHORT, Short.valueOf((short)0));
/*     */                   wrapper.write((Type)Type.SHORT, Short.valueOf((short)0));
/*     */                   wrapper.send(Protocol1_19To1_18_2.class);
/*     */                   wrapper.cancel();
/*     */                   PacketWrapper metaPacket = wrapper.create((PacketType)ClientboundPackets1_19.ENTITY_METADATA);
/*     */                   metaPacket.write((Type)Type.VAR_INT, wrapper.get((Type)Type.VAR_INT, 0));
/*     */                   List<Metadata> metadata = new ArrayList<>();
/*     */                   metadata.add(new Metadata(8, Types1_19.META_TYPES.paintingVariantType, Integer.valueOf(((Protocol1_19To1_18_2)EntityPackets.this.protocol).getMappingData().getPaintingMappings().getNewIdOrDefault(motive, 0))));
/*     */                   metaPacket.write(Types1_19.METADATA_LIST, metadata);
/*     */                   metaPacket.send(Protocol1_19To1_18_2.class);
/*     */                 });
/*     */           }
/*     */         });
/* 165 */     ((Protocol1_19To1_18_2)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_18.SPAWN_MOB, (ClientboundPacketType)ClientboundPackets1_19.SPAWN_ENTITY, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 168 */             map((Type)Type.VAR_INT);
/* 169 */             map(Type.UUID);
/* 170 */             map((Type)Type.VAR_INT);
/* 171 */             map((Type)Type.DOUBLE);
/* 172 */             map((Type)Type.DOUBLE);
/* 173 */             map((Type)Type.DOUBLE);
/* 174 */             handler(wrapper -> {
/*     */                   byte yaw = ((Byte)wrapper.read((Type)Type.BYTE)).byteValue();
/*     */                   
/*     */                   byte pitch = ((Byte)wrapper.read((Type)Type.BYTE)).byteValue();
/*     */                   wrapper.write((Type)Type.BYTE, Byte.valueOf(pitch));
/*     */                   wrapper.write((Type)Type.BYTE, Byte.valueOf(yaw));
/*     */                 });
/* 181 */             map((Type)Type.BYTE);
/* 182 */             create((Type)Type.VAR_INT, Integer.valueOf(0));
/* 183 */             map((Type)Type.SHORT);
/* 184 */             map((Type)Type.SHORT);
/* 185 */             map((Type)Type.SHORT);
/* 186 */             handler(EntityPackets.this.trackerHandler());
/*     */           }
/*     */         });
/*     */     
/* 190 */     ((Protocol1_19To1_18_2)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_18.ENTITY_EFFECT, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 193 */             map((Type)Type.VAR_INT);
/* 194 */             map((Type)Type.VAR_INT);
/* 195 */             map((Type)Type.BYTE);
/* 196 */             map((Type)Type.VAR_INT);
/* 197 */             map((Type)Type.BYTE);
/* 198 */             create((Type)Type.BOOLEAN, Boolean.valueOf(false));
/*     */           }
/*     */         });
/*     */     
/* 202 */     ((Protocol1_19To1_18_2)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_18.JOIN_GAME, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 205 */             map((Type)Type.INT);
/* 206 */             map((Type)Type.BOOLEAN);
/* 207 */             map((Type)Type.UNSIGNED_BYTE);
/* 208 */             map((Type)Type.BYTE);
/* 209 */             map(Type.STRING_ARRAY);
/* 210 */             map(Type.NBT);
/* 211 */             handler(wrapper -> {
/*     */                   CompoundTag tag = (CompoundTag)wrapper.get(Type.NBT, 0);
/*     */                   
/*     */                   tag.put("minecraft:chat_type", (Tag)EntityPackets.CHAT_REGISTRY.clone());
/*     */                   
/*     */                   ListTag dimensions = (ListTag)((CompoundTag)tag.get("minecraft:dimension_type")).get("value");
/*     */                   
/*     */                   Map<String, DimensionData> dimensionDataMap = new HashMap<>(dimensions.size());
/*     */                   
/*     */                   Map<CompoundTag, String> dimensionsMap = new HashMap<>(dimensions.size());
/*     */                   
/*     */                   for (Tag dimension : dimensions) {
/*     */                     CompoundTag dimensionCompound = (CompoundTag)dimension;
/*     */                     CompoundTag element = (CompoundTag)dimensionCompound.get("element");
/*     */                     String name = (String)dimensionCompound.get("name").getValue();
/*     */                     EntityPackets.addMonsterSpawnData(element);
/*     */                     dimensionDataMap.put(name, new DimensionDataImpl(element));
/*     */                     dimensionsMap.put(element.clone(), name);
/*     */                   } 
/*     */                   EntityPackets.this.tracker(wrapper.user()).setDimensions(dimensionDataMap);
/*     */                   DimensionRegistryStorage registryStorage = (DimensionRegistryStorage)wrapper.user().get(DimensionRegistryStorage.class);
/*     */                   registryStorage.setDimensions(dimensionsMap);
/*     */                   EntityPackets.writeDimensionKey(wrapper, registryStorage);
/*     */                 });
/* 235 */             map(Type.STRING);
/* 236 */             map((Type)Type.LONG);
/* 237 */             map((Type)Type.VAR_INT);
/* 238 */             map((Type)Type.VAR_INT);
/* 239 */             map((Type)Type.VAR_INT);
/* 240 */             map((Type)Type.BOOLEAN);
/* 241 */             map((Type)Type.BOOLEAN);
/* 242 */             map((Type)Type.BOOLEAN);
/* 243 */             map((Type)Type.BOOLEAN);
/* 244 */             create(Type.OPTIONAL_GLOBAL_POSITION, null);
/* 245 */             handler(EntityPackets.this.playerTrackerHandler());
/* 246 */             handler(EntityPackets.this.worldDataTrackerHandlerByKey());
/* 247 */             handler(EntityPackets.this.biomeSizeTracker());
/* 248 */             handler(wrapper -> {
/*     */                   PacketWrapper displayPreviewPacket = wrapper.create((PacketType)ClientboundPackets1_19.SET_DISPLAY_CHAT_PREVIEW);
/*     */                   
/*     */                   displayPreviewPacket.write((Type)Type.BOOLEAN, Boolean.valueOf(false));
/*     */                   displayPreviewPacket.scheduleSend(Protocol1_19To1_18_2.class);
/*     */                 });
/*     */           }
/*     */         });
/* 256 */     ((Protocol1_19To1_18_2)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_18.RESPAWN, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 259 */             handler(wrapper -> EntityPackets.writeDimensionKey(wrapper, (DimensionRegistryStorage)wrapper.user().get(DimensionRegistryStorage.class)));
/* 260 */             map(Type.STRING);
/* 261 */             map((Type)Type.LONG);
/* 262 */             map((Type)Type.UNSIGNED_BYTE);
/* 263 */             map((Type)Type.BYTE);
/* 264 */             map((Type)Type.BOOLEAN);
/* 265 */             map((Type)Type.BOOLEAN);
/* 266 */             map((Type)Type.BOOLEAN);
/* 267 */             create(Type.OPTIONAL_GLOBAL_POSITION, null);
/* 268 */             handler(EntityPackets.this.worldDataTrackerHandlerByKey());
/*     */           }
/*     */         });
/*     */     
/* 272 */     ((Protocol1_19To1_18_2)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_18.PLAYER_INFO, wrapper -> {
/*     */           int action = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */           int entries = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */           for (int i = 0; i < entries; i++) {
/*     */             wrapper.passthrough(Type.UUID);
/*     */             if (action == 0) {
/*     */               wrapper.passthrough(Type.STRING);
/*     */               int properties = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */               for (int j = 0; j < properties; j++) {
/*     */                 wrapper.passthrough(Type.STRING);
/*     */                 wrapper.passthrough(Type.STRING);
/*     */                 wrapper.passthrough(Type.OPTIONAL_STRING);
/*     */               } 
/*     */               wrapper.passthrough((Type)Type.VAR_INT);
/*     */               wrapper.passthrough((Type)Type.VAR_INT);
/*     */               JsonElement displayName = (JsonElement)wrapper.read(Type.OPTIONAL_COMPONENT);
/*     */               if (!Protocol1_19To1_18_2.isTextComponentNull(displayName)) {
/*     */                 wrapper.write(Type.OPTIONAL_COMPONENT, displayName);
/*     */               } else {
/*     */                 wrapper.write(Type.OPTIONAL_COMPONENT, null);
/*     */               } 
/*     */               wrapper.write(Type.OPTIONAL_PROFILE_KEY, null);
/*     */             } else if (action == 1 || action == 2) {
/*     */               wrapper.passthrough((Type)Type.VAR_INT);
/*     */             } else if (action == 3) {
/*     */               JsonElement displayName = (JsonElement)wrapper.read(Type.OPTIONAL_COMPONENT);
/*     */               if (!Protocol1_19To1_18_2.isTextComponentNull(displayName)) {
/*     */                 wrapper.write(Type.OPTIONAL_COMPONENT, displayName);
/*     */               } else {
/*     */                 wrapper.write(Type.OPTIONAL_COMPONENT, null);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void writeDimensionKey(PacketWrapper wrapper, DimensionRegistryStorage registryStorage) throws Exception {
/* 314 */     CompoundTag currentDimension = (CompoundTag)wrapper.read(Type.NBT);
/* 315 */     addMonsterSpawnData(currentDimension);
/* 316 */     String dimensionKey = registryStorage.dimensionKey(currentDimension);
/* 317 */     if (dimensionKey == null) {
/* 318 */       if (!Via.getConfig().isSuppressConversionWarnings()) {
/* 319 */         Via.getPlatform().getLogger().warning("The server tried to send dimension data from a dimension the client wasn't told about on join. Plugins and mods have to make sure they are not creating new dimension types while players are online, and proxies need to make sure they don't scramble dimension data. Received dimension: " + currentDimension + ". Known dimensions: " + registryStorage
/*     */             
/* 321 */             .dimensions());
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 330 */       dimensionKey = (String)((Map.Entry)((Pair)registryStorage.dimensions().entrySet().stream().map(it -> new Pair(it, Maps.difference(currentDimension.getValue(), ((CompoundTag)it.getKey()).getValue()).entriesInCommon())).filter(it -> (((Map)it.value()).containsKey("min_y") && ((Map)it.value()).containsKey("height"))).max(Comparator.comparingInt(it -> ((Map)it.value()).size())).orElseThrow(() -> new IllegalArgumentException("Dimension not found in registry data from join packet: " + currentDimension))).key()).getValue();
/*     */     } 
/*     */     
/* 333 */     wrapper.write(Type.STRING, dimensionKey);
/*     */   }
/*     */   
/*     */   private static int to3dId(int id) {
/* 337 */     switch (id) {
/*     */       case -1:
/* 339 */         return 1;
/*     */       case 2:
/* 341 */         return 2;
/*     */       case 0:
/* 343 */         return 3;
/*     */       case 1:
/* 345 */         return 4;
/*     */       case 3:
/* 347 */         return 5;
/*     */     } 
/* 349 */     throw new IllegalArgumentException("Unknown 2d id: " + id);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addMonsterSpawnData(CompoundTag dimension) {
/* 354 */     dimension.put("monster_spawn_block_light_limit", (Tag)new IntTag(0));
/* 355 */     dimension.put("monster_spawn_light_level", (Tag)new IntTag(11));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerRewrites() {
/* 360 */     filter().handler((event, meta) -> {
/*     */           meta.setMetaType(Types1_19.META_TYPES.byId(meta.metaType().typeId()));
/*     */           
/*     */           MetaType type = meta.metaType();
/*     */           
/*     */           if (type == Types1_19.META_TYPES.particleType) {
/*     */             Particle particle = (Particle)meta.getValue();
/*     */             
/*     */             ParticleMappings particleMappings = ((Protocol1_19To1_18_2)this.protocol).getMappingData().getParticleMappings();
/*     */             
/*     */             if (particle.getId() == particleMappings.id("vibration")) {
/*     */               particle.getArguments().remove(0);
/*     */               
/*     */               String resourceLocation = Key.stripMinecraftNamespace((String)((Particle.ParticleData)particle.getArguments().get(0)).get());
/*     */               
/*     */               if (resourceLocation.equals("entity")) {
/*     */                 particle.getArguments().add(2, new Particle.ParticleData((Type)Type.FLOAT, Float.valueOf(0.0F)));
/*     */               }
/*     */             } 
/*     */             rewriteParticle(particle);
/*     */           } 
/*     */         });
/* 382 */     registerMetaTypeHandler(Types1_19.META_TYPES.itemType, Types1_19.META_TYPES.blockStateType, null, null);
/*     */     
/* 384 */     filter().filterFamily((EntityType)Entity1_19Types.MINECART_ABSTRACT).index(11).handler((event, meta) -> {
/*     */           int data = ((Integer)meta.getValue()).intValue();
/*     */           
/*     */           meta.setValue(Integer.valueOf(((Protocol1_19To1_18_2)this.protocol).getMappingData().getNewBlockStateId(data)));
/*     */         });
/*     */     
/* 390 */     filter().type((EntityType)Entity1_19Types.CAT).index(19).handler((event, meta) -> meta.setMetaType(Types1_19.META_TYPES.catVariantType));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMappingDataLoaded() {
/* 395 */     mapTypes();
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityType typeFromId(int type) {
/* 400 */     return Entity1_19Types.getTypeFromId(type);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_19to1_18_2\packets\EntityPackets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */