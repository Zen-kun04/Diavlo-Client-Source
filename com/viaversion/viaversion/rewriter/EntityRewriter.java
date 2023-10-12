/*     */ package com.viaversion.viaversion.rewriter;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.viaversion.viaversion.api.Via;
/*     */ import com.viaversion.viaversion.api.connection.UserConnection;
/*     */ import com.viaversion.viaversion.api.data.Int2IntMapMappings;
/*     */ import com.viaversion.viaversion.api.data.Mappings;
/*     */ import com.viaversion.viaversion.api.data.ParticleMappings;
/*     */ import com.viaversion.viaversion.api.data.entity.DimensionData;
/*     */ import com.viaversion.viaversion.api.data.entity.EntityTracker;
/*     */ import com.viaversion.viaversion.api.data.entity.TrackedEntity;
/*     */ import com.viaversion.viaversion.api.minecraft.entities.EntityType;
/*     */ import com.viaversion.viaversion.api.minecraft.item.Item;
/*     */ import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
/*     */ import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
/*     */ import com.viaversion.viaversion.api.protocol.Protocol;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
/*     */ import com.viaversion.viaversion.api.rewriter.EntityRewriter;
/*     */ import com.viaversion.viaversion.api.rewriter.RewriterBase;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import com.viaversion.viaversion.api.type.types.Particle;
/*     */ import com.viaversion.viaversion.data.entity.DimensionDataImpl;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
/*     */ import com.viaversion.viaversion.rewriter.meta.MetaFilter;
/*     */ import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
/*     */ import com.viaversion.viaversion.rewriter.meta.MetaHandlerEventImpl;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.stream.Collectors;
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
/*     */ public abstract class EntityRewriter<C extends ClientboundPacketType, T extends Protocol<C, ?, ?, ?>>
/*     */   extends RewriterBase<T>
/*     */   implements EntityRewriter<T>
/*     */ {
/*  59 */   private static final Metadata[] EMPTY_ARRAY = new Metadata[0];
/*  60 */   protected final List<MetaFilter> metadataFilters = new ArrayList<>();
/*     */   protected final boolean trackMappedType;
/*     */   protected Mappings typeMappings;
/*     */   
/*     */   protected EntityRewriter(T protocol) {
/*  65 */     this(protocol, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected EntityRewriter(T protocol, boolean trackMappedType) {
/*  75 */     super((Protocol)protocol);
/*  76 */     this.trackMappedType = trackMappedType;
/*  77 */     protocol.put(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MetaFilter.Builder filter() {
/*  88 */     return new MetaFilter.Builder(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerFilter(MetaFilter filter) {
/*  99 */     Preconditions.checkArgument(!this.metadataFilters.contains(filter));
/* 100 */     this.metadataFilters.add(filter);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleMetadata(int entityId, List<Metadata> metadataList, UserConnection connection) {
/* 105 */     TrackedEntity entity = tracker(connection).entity(entityId);
/* 106 */     EntityType type = (entity != null) ? entity.entityType() : null;
/* 107 */     for (Metadata metadata : (Metadata[])metadataList.<Metadata>toArray(EMPTY_ARRAY)) {
/*     */       
/* 109 */       if (!callOldMetaHandler(entityId, type, metadata, metadataList, connection)) {
/* 110 */         metadataList.remove(metadata);
/*     */       } else {
/*     */         MetaHandlerEventImpl metaHandlerEventImpl;
/*     */         
/* 114 */         MetaHandlerEvent event = null;
/* 115 */         for (MetaFilter filter : this.metadataFilters) {
/* 116 */           if (!filter.isFiltered(type, metadata)) {
/*     */             continue;
/*     */           }
/* 119 */           if (event == null)
/*     */           {
/* 121 */             metaHandlerEventImpl = new MetaHandlerEventImpl(connection, entity, entityId, metadata, metadataList);
/*     */           }
/*     */           
/*     */           try {
/* 125 */             filter.handler().handle((MetaHandlerEvent)metaHandlerEventImpl, metadata);
/* 126 */           } catch (Exception e) {
/* 127 */             logException(e, type, metadataList, metadata);
/* 128 */             metadataList.remove(metadata);
/*     */             
/*     */             break;
/*     */           } 
/* 132 */           if (metaHandlerEventImpl.cancelled()) {
/*     */             
/* 134 */             metadataList.remove(metadata);
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/* 139 */         List<Metadata> extraMeta = (metaHandlerEventImpl != null) ? metaHandlerEventImpl.extraMeta() : null;
/* 140 */         if (extraMeta != null)
/*     */         {
/* 142 */           metadataList.addAll(extraMeta);
/*     */         }
/*     */       } 
/*     */     } 
/* 146 */     if (entity != null) {
/* 147 */       entity.sentMetadata(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   private boolean callOldMetaHandler(int entityId, EntityType type, Metadata metadata, List<Metadata> metadataList, UserConnection connection) {
/*     */     try {
/* 154 */       handleMetadata(entityId, type, metadata, metadataList, connection);
/* 155 */       return true;
/* 156 */     } catch (Exception e) {
/* 157 */       logException(e, type, metadataList, metadata);
/* 158 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected void handleMetadata(int entityId, EntityType type, Metadata metadata, List<Metadata> metadatas, UserConnection connection) throws Exception {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int newEntityId(int id) {
/* 178 */     return (this.typeMappings != null) ? this.typeMappings.getNewIdOrDefault(id, id) : id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mapEntityType(EntityType type, EntityType mappedType) {
/* 189 */     Preconditions.checkArgument((type.getClass() != mappedType.getClass()), "EntityTypes should not be of the same class/enum");
/* 190 */     mapEntityType(type.getId(), mappedType.getId());
/*     */   }
/*     */   
/*     */   protected void mapEntityType(int id, int mappedId) {
/* 194 */     if (this.typeMappings == null) {
/* 195 */       this.typeMappings = (Mappings)Int2IntMapMappings.of();
/*     */     }
/* 197 */     this.typeMappings.setNewId(id, mappedId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <E extends Enum<E> & EntityType> void mapTypes(EntityType[] oldTypes, Class<E> newTypeClass) {
/* 208 */     if (this.typeMappings == null) {
/* 209 */       this.typeMappings = (Mappings)Int2IntMapMappings.of();
/*     */     }
/* 211 */     for (EntityType oldType : oldTypes) {
/*     */       try {
/* 213 */         E newType = Enum.valueOf(newTypeClass, oldType.name());
/* 214 */         this.typeMappings.setNewId(oldType.getId(), ((EntityType)newType).getId());
/* 215 */       } catch (IllegalArgumentException notFound) {
/* 216 */         if (!this.typeMappings.contains(oldType.getId())) {
/* 217 */           Via.getPlatform().getLogger().warning("Could not find new entity type for " + oldType + "! Old type: " + oldType
/* 218 */               .getClass().getEnclosingClass().getSimpleName() + ", new type: " + newTypeClass.getEnclosingClass().getSimpleName());
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mapTypes() {
/* 228 */     Preconditions.checkArgument((this.typeMappings == null), "Type mappings have already been set - manual type mappings should be set *after* this");
/* 229 */     Preconditions.checkNotNull(this.protocol.getMappingData().getEntityMappings(), "Protocol does not have entity mappings");
/* 230 */     this.typeMappings = (Mappings)this.protocol.getMappingData().getEntityMappings();
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
/*     */   public void registerMetaTypeHandler(MetaType itemType, MetaType blockStateType, MetaType optionalBlockStateType, MetaType particleType) {
/* 242 */     filter().handler((event, meta) -> {
/*     */           MetaType type = meta.metaType();
/*     */           if (type == itemType) {
/*     */             this.protocol.getItemRewriter().handleItemToClient((Item)meta.value());
/*     */           } else if (type == blockStateType) {
/*     */             int data = ((Integer)meta.value()).intValue();
/*     */             meta.setValue(Integer.valueOf(this.protocol.getMappingData().getNewBlockStateId(data)));
/*     */           } else if (type == optionalBlockStateType) {
/*     */             int data = ((Integer)meta.value()).intValue();
/*     */             if (data != 0) {
/*     */               meta.setValue(Integer.valueOf(this.protocol.getMappingData().getNewBlockStateId(data)));
/*     */             }
/*     */           } else if (type == particleType) {
/*     */             rewriteParticle((Particle)meta.value());
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   public void registerTracker(C packetType) {
/* 261 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 264 */             map((Type)Type.VAR_INT);
/* 265 */             map(Type.UUID);
/* 266 */             map((Type)Type.VAR_INT);
/* 267 */             handler(EntityRewriter.this.trackerHandler());
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void registerTrackerWithData(C packetType, final EntityType fallingBlockType) {
/* 273 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 276 */             map((Type)Type.VAR_INT);
/* 277 */             map(Type.UUID);
/* 278 */             map((Type)Type.VAR_INT);
/* 279 */             map((Type)Type.DOUBLE);
/* 280 */             map((Type)Type.DOUBLE);
/* 281 */             map((Type)Type.DOUBLE);
/* 282 */             map((Type)Type.BYTE);
/* 283 */             map((Type)Type.BYTE);
/* 284 */             map((Type)Type.INT);
/* 285 */             handler(EntityRewriter.this.trackerHandler());
/* 286 */             handler(wrapper -> {
/*     */                   int entityId = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
/*     */                   EntityType entityType = EntityRewriter.this.tracker(wrapper.user()).entityType(entityId);
/*     */                   if (entityType == fallingBlockType) {
/*     */                     wrapper.set((Type)Type.INT, 0, Integer.valueOf(EntityRewriter.this.protocol.getMappingData().getNewBlockStateId(((Integer)wrapper.get((Type)Type.INT, 0)).intValue())));
/*     */                   }
/*     */                 });
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void registerTrackerWithData1_19(C packetType, final EntityType fallingBlockType) {
/* 298 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 301 */             map((Type)Type.VAR_INT);
/* 302 */             map(Type.UUID);
/* 303 */             map((Type)Type.VAR_INT);
/* 304 */             map((Type)Type.DOUBLE);
/* 305 */             map((Type)Type.DOUBLE);
/* 306 */             map((Type)Type.DOUBLE);
/* 307 */             map((Type)Type.BYTE);
/* 308 */             map((Type)Type.BYTE);
/* 309 */             map((Type)Type.BYTE);
/* 310 */             map((Type)Type.VAR_INT);
/* 311 */             handler(EntityRewriter.this.trackerHandler());
/* 312 */             handler(wrapper -> {
/*     */                   if (EntityRewriter.this.protocol.getMappingData() == null) {
/*     */                     return;
/*     */                   }
/*     */                   int entityId = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
/*     */                   EntityType entityType = EntityRewriter.this.tracker(wrapper.user()).entityType(entityId);
/*     */                   if (entityType == fallingBlockType) {
/*     */                     wrapper.set((Type)Type.VAR_INT, 2, Integer.valueOf(EntityRewriter.this.protocol.getMappingData().getNewBlockStateId(((Integer)wrapper.get((Type)Type.VAR_INT, 2)).intValue())));
/*     */                   }
/*     */                 });
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
/*     */   public void registerTracker(C packetType, EntityType entityType, Type<Integer> intType) {
/* 335 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, wrapper -> {
/*     */           int entityId = ((Integer)wrapper.passthrough(intType)).intValue();
/*     */           tracker(wrapper.user()).addEntity(entityId, entityType);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerTracker(C packetType, EntityType entityType) {
/* 348 */     registerTracker(packetType, entityType, (Type<Integer>)Type.VAR_INT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerRemoveEntities(C packetType) {
/* 357 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, wrapper -> {
/*     */           int[] entityIds = (int[])wrapper.passthrough(Type.VAR_INT_ARRAY_PRIMITIVE);
/*     */           EntityTracker entityTracker = tracker(wrapper.user());
/*     */           for (int entity : entityIds) {
/*     */             entityTracker.removeEntity(entity);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerRemoveEntity(C packetType) {
/* 372 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, wrapper -> {
/*     */           int entityId = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */           tracker(wrapper.user()).removeEntity(entityId);
/*     */         });
/*     */   }
/*     */   
/*     */   public void registerMetadataRewriter(C packetType, final Type<List<Metadata>> oldMetaType, final Type<List<Metadata>> newMetaType) {
/* 379 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 382 */             map((Type)Type.VAR_INT);
/* 383 */             if (oldMetaType != null) {
/* 384 */               map(oldMetaType, newMetaType);
/*     */             } else {
/* 386 */               map(newMetaType);
/*     */             } 
/* 388 */             handler(wrapper -> {
/*     */                   int entityId = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
/*     */                   List<Metadata> metadata = (List<Metadata>)wrapper.get(newMetaType, 0);
/*     */                   EntityRewriter.this.handleMetadata(entityId, metadata, wrapper.user());
/*     */                 });
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void registerMetadataRewriter(C packetType, Type<List<Metadata>> metaType) {
/* 398 */     registerMetadataRewriter(packetType, (Type<List<Metadata>>)null, metaType);
/*     */   }
/*     */   
/*     */   public PacketHandler trackerHandler() {
/* 402 */     return trackerAndRewriterHandler((Type<List<Metadata>>)null);
/*     */   }
/*     */   
/*     */   public PacketHandler playerTrackerHandler() {
/* 406 */     return wrapper -> {
/*     */         EntityTracker tracker = tracker(wrapper.user());
/*     */         int entityId = ((Integer)wrapper.get((Type)Type.INT, 0)).intValue();
/*     */         tracker.setClientEntityId(entityId);
/*     */         tracker.addEntity(entityId, tracker.playerType());
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PacketHandler worldDataTrackerHandler(int nbtIndex) {
/* 422 */     return wrapper -> {
/*     */         EntityTracker tracker = tracker(wrapper.user());
/*     */         CompoundTag registryData = (CompoundTag)wrapper.get(Type.NBT, nbtIndex);
/*     */         Tag height = registryData.get("height");
/*     */         if (height instanceof IntTag) {
/*     */           int blockHeight = ((IntTag)height).asInt();
/*     */           tracker.setCurrentWorldSectionHeight(blockHeight >> 4);
/*     */         } else {
/*     */           Via.getPlatform().getLogger().warning("Height missing in dimension data: " + registryData);
/*     */         } 
/*     */         Tag minY = registryData.get("min_y");
/*     */         if (minY instanceof IntTag) {
/*     */           tracker.setCurrentMinY(((IntTag)minY).asInt());
/*     */         } else {
/*     */           Via.getPlatform().getLogger().warning("Min Y missing in dimension data: " + registryData);
/*     */         } 
/*     */         String world = (String)wrapper.get(Type.STRING, 0);
/*     */         if (tracker.currentWorld() != null && !tracker.currentWorld().equals(world)) {
/*     */           tracker.clearEntities();
/*     */           tracker.trackClientEntity();
/*     */         } 
/*     */         tracker.setCurrentWorld(world);
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PacketHandler worldDataTrackerHandlerByKey() {
/* 451 */     return wrapper -> {
/*     */         EntityTracker tracker = tracker(wrapper.user());
/*     */         String dimensionKey = (String)wrapper.get(Type.STRING, 0);
/*     */         DimensionData dimensionData = tracker.dimensionData(dimensionKey);
/*     */         if (dimensionData == null) {
/*     */           Via.getPlatform().getLogger().severe("Dimension data missing for dimension: " + dimensionKey + ", falling back to overworld");
/*     */           dimensionData = tracker.dimensionData("minecraft:overworld");
/*     */           Preconditions.checkNotNull(dimensionData, "Overworld data missing");
/*     */         } 
/*     */         tracker.setCurrentWorldSectionHeight(dimensionData.height() >> 4);
/*     */         tracker.setCurrentMinY(dimensionData.minY());
/*     */         String world = (String)wrapper.get(Type.STRING, 1);
/*     */         if (tracker.currentWorld() != null && !tracker.currentWorld().equals(world)) {
/*     */           tracker.clearEntities();
/*     */           tracker.trackClientEntity();
/*     */         } 
/*     */         tracker.setCurrentWorld(world);
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PacketHandler biomeSizeTracker() {
/* 474 */     return wrapper -> trackBiomeSize(wrapper.user(), (CompoundTag)wrapper.get(Type.NBT, 0));
/*     */   }
/*     */   
/*     */   public void trackBiomeSize(UserConnection connection, CompoundTag registry) {
/* 478 */     CompoundTag biomeRegistry = (CompoundTag)registry.get("minecraft:worldgen/biome");
/* 479 */     ListTag biomes = (ListTag)biomeRegistry.get("value");
/* 480 */     tracker(connection).setBiomesSent(biomes.size());
/*     */   }
/*     */   
/*     */   public PacketHandler dimensionDataHandler() {
/* 484 */     return wrapper -> cacheDimensionData(wrapper.user(), (CompoundTag)wrapper.get(Type.NBT, 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cacheDimensionData(UserConnection connection, CompoundTag registry) {
/* 491 */     ListTag dimensions = (ListTag)((CompoundTag)registry.get("minecraft:dimension_type")).get("value");
/* 492 */     Map<String, DimensionData> dimensionDataMap = new HashMap<>(dimensions.size());
/* 493 */     for (Tag dimension : dimensions) {
/* 494 */       CompoundTag dimensionCompound = (CompoundTag)dimension;
/* 495 */       CompoundTag element = (CompoundTag)dimensionCompound.get("element");
/* 496 */       String name = (String)dimensionCompound.get("name").getValue();
/* 497 */       dimensionDataMap.put(name, new DimensionDataImpl(element));
/*     */     } 
/* 499 */     tracker(connection).setDimensions(dimensionDataMap);
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
/*     */   public PacketHandler trackerAndRewriterHandler(Type<List<Metadata>> metaType) {
/* 512 */     return wrapper -> {
/*     */         int entityId = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
/*     */         int type = ((Integer)wrapper.get((Type)Type.VAR_INT, 1)).intValue();
/*     */         int newType = newEntityId(type);
/*     */         if (newType != type) {
/*     */           wrapper.set((Type)Type.VAR_INT, 1, Integer.valueOf(newType));
/*     */         }
/*     */         EntityType entType = typeFromId(this.trackMappedType ? newType : type);
/*     */         tracker(wrapper.user()).addEntity(entityId, entType);
/*     */         if (metaType != null) {
/*     */           handleMetadata(entityId, (List<Metadata>)wrapper.get(metaType, 0), wrapper.user());
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PacketHandler trackerAndRewriterHandler(Type<List<Metadata>> metaType, EntityType entityType) {
/* 532 */     return wrapper -> {
/*     */         int entityId = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
/*     */         tracker(wrapper.user()).addEntity(entityId, entityType);
/*     */         if (metaType != null) {
/*     */           handleMetadata(entityId, (List<Metadata>)wrapper.get(metaType, 0), wrapper.user());
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PacketHandler objectTrackerHandler() {
/* 549 */     return wrapper -> {
/*     */         int entityId = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
/*     */         byte type = ((Byte)wrapper.get((Type)Type.BYTE, 0)).byteValue();
/*     */         EntityType entType = objectTypeFromId(type);
/*     */         tracker(wrapper.user()).addEntity(entityId, entType);
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected Metadata metaByIndex(int index, List<Metadata> metadataList) {
/* 563 */     for (Metadata metadata : metadataList) {
/* 564 */       if (metadata.id() == index) {
/* 565 */         return metadata;
/*     */       }
/*     */     } 
/* 568 */     return null;
/*     */   }
/*     */   
/*     */   protected void rewriteParticle(Particle particle) {
/* 572 */     ParticleMappings mappings = this.protocol.getMappingData().getParticleMappings();
/* 573 */     int id = particle.getId();
/* 574 */     if (mappings.isBlockParticle(id)) {
/* 575 */       Particle.ParticleData data = particle.getArguments().get(0);
/* 576 */       data.setValue(Integer.valueOf(this.protocol.getMappingData().getNewBlockStateId(((Integer)data.get()).intValue())));
/* 577 */     } else if (mappings.isItemParticle(id) && this.protocol.getItemRewriter() != null) {
/* 578 */       Particle.ParticleData data = particle.getArguments().get(0);
/* 579 */       Item item = (Item)data.get();
/* 580 */       this.protocol.getItemRewriter().handleItemToClient(item);
/*     */     } 
/*     */     
/* 583 */     particle.setId(this.protocol.getMappingData().getNewParticleId(id));
/*     */   }
/*     */   
/*     */   private void logException(Exception e, EntityType type, List<Metadata> metadataList, Metadata metadata) {
/* 587 */     if (!Via.getConfig().isSuppressMetadataErrors() || Via.getManager().isDebug()) {
/* 588 */       Logger logger = Via.getPlatform().getLogger();
/* 589 */       logger.severe("An error occurred in metadata handler " + getClass().getSimpleName() + " for " + ((type != null) ? type
/* 590 */           .name() : "untracked") + " entity type: " + metadata);
/* 591 */       logger.severe(metadataList.stream().sorted(Comparator.comparingInt(Metadata::id))
/* 592 */           .map(Metadata::toString).collect(Collectors.joining("\n", "Full metadata: ", "")));
/* 593 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\rewriter\EntityRewriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */