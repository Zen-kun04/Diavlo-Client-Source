/*     */ package com.viaversion.viaversion.protocols.protocol1_20to1_19_4.packets;
/*     */ 
/*     */ import com.viaversion.viaversion.api.minecraft.Quaternion;
/*     */ import com.viaversion.viaversion.api.minecraft.entities.Entity1_19_4Types;
/*     */ import com.viaversion.viaversion.api.minecraft.entities.EntityType;
/*     */ import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
/*     */ import com.viaversion.viaversion.api.protocol.Protocol;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import com.viaversion.viaversion.api.type.types.version.Types1_19_4;
/*     */ import com.viaversion.viaversion.api.type.types.version.Types1_20;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.FloatTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ClientboundPackets1_19_4;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20to1_19_4.Protocol1_20To1_19_4;
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
/*     */ 
/*     */ public final class EntityPackets
/*     */   extends EntityRewriter<ClientboundPackets1_19_4, Protocol1_20To1_19_4>
/*     */ {
/*  40 */   private static final Quaternion Y_FLIPPED_ROTATION = new Quaternion(0.0F, 1.0F, 0.0F, 0.0F);
/*     */   
/*     */   public EntityPackets(Protocol1_20To1_19_4 protocol) {
/*  43 */     super((Protocol)protocol);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerPackets() {
/*  48 */     registerTrackerWithData1_19((ClientboundPacketType)ClientboundPackets1_19_4.SPAWN_ENTITY, (EntityType)Entity1_19_4Types.FALLING_BLOCK);
/*  49 */     registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_19_4.ENTITY_METADATA, Types1_19_4.METADATA_LIST, Types1_20.METADATA_LIST);
/*  50 */     registerRemoveEntities((ClientboundPacketType)ClientboundPackets1_19_4.REMOVE_ENTITIES);
/*     */     
/*  52 */     ((Protocol1_20To1_19_4)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.JOIN_GAME, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/*  55 */             map((Type)Type.INT);
/*  56 */             map((Type)Type.BOOLEAN);
/*  57 */             map((Type)Type.UNSIGNED_BYTE);
/*  58 */             map((Type)Type.BYTE);
/*  59 */             map(Type.STRING_ARRAY);
/*  60 */             map(Type.NBT);
/*  61 */             map(Type.STRING);
/*  62 */             map(Type.STRING);
/*  63 */             map((Type)Type.LONG);
/*  64 */             map((Type)Type.VAR_INT);
/*  65 */             map((Type)Type.VAR_INT);
/*  66 */             map((Type)Type.VAR_INT);
/*  67 */             map((Type)Type.BOOLEAN);
/*  68 */             map((Type)Type.BOOLEAN);
/*  69 */             map((Type)Type.BOOLEAN);
/*  70 */             map((Type)Type.BOOLEAN);
/*  71 */             map(Type.OPTIONAL_GLOBAL_POSITION);
/*  72 */             create((Type)Type.VAR_INT, Integer.valueOf(0));
/*     */             
/*  74 */             handler(EntityPackets.this.dimensionDataHandler());
/*  75 */             handler(EntityPackets.this.biomeSizeTracker());
/*  76 */             handler(EntityPackets.this.worldDataTrackerHandlerByKey());
/*  77 */             handler(wrapper -> {
/*     */                   CompoundTag registry = (CompoundTag)wrapper.get(Type.NBT, 0);
/*     */                   
/*     */                   CompoundTag damageTypeRegistry = (CompoundTag)registry.get("minecraft:damage_type");
/*     */                   
/*     */                   ListTag damageTypes = (ListTag)damageTypeRegistry.get("value");
/*     */                   
/*     */                   int highestId = -1;
/*     */                   
/*     */                   for (Tag damageType : damageTypes) {
/*     */                     IntTag id = (IntTag)((CompoundTag)damageType).get("id");
/*     */                     highestId = Math.max(highestId, id.asInt());
/*     */                   } 
/*     */                   CompoundTag outsideBorderReason = new CompoundTag();
/*     */                   CompoundTag outsideBorderElement = new CompoundTag();
/*     */                   outsideBorderElement.put("scaling", (Tag)new StringTag("always"));
/*     */                   outsideBorderElement.put("exhaustion", (Tag)new FloatTag(0.0F));
/*     */                   outsideBorderElement.put("message_id", (Tag)new StringTag("badRespawnPoint"));
/*     */                   outsideBorderReason.put("id", (Tag)new IntTag(highestId + 1));
/*     */                   outsideBorderReason.put("name", (Tag)new StringTag("minecraft:outside_border"));
/*     */                   outsideBorderReason.put("element", (Tag)outsideBorderElement);
/*     */                   damageTypes.add((Tag)outsideBorderReason);
/*     */                   CompoundTag genericKillReason = new CompoundTag();
/*     */                   CompoundTag genericKillElement = new CompoundTag();
/*     */                   genericKillElement.put("scaling", (Tag)new StringTag("always"));
/*     */                   genericKillElement.put("exhaustion", (Tag)new FloatTag(0.0F));
/*     */                   genericKillElement.put("message_id", (Tag)new StringTag("badRespawnPoint"));
/*     */                   genericKillReason.put("id", (Tag)new IntTag(highestId + 2));
/*     */                   genericKillReason.put("name", (Tag)new StringTag("minecraft:generic_kill"));
/*     */                   genericKillReason.put("element", (Tag)genericKillElement);
/*     */                   damageTypes.add((Tag)genericKillReason);
/*     */                 });
/*     */           }
/*     */         });
/* 111 */     ((Protocol1_20To1_19_4)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.RESPAWN, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 114 */             map(Type.STRING);
/* 115 */             map(Type.STRING);
/* 116 */             map((Type)Type.LONG);
/* 117 */             map((Type)Type.UNSIGNED_BYTE);
/* 118 */             map((Type)Type.BYTE);
/* 119 */             map((Type)Type.BOOLEAN);
/* 120 */             map((Type)Type.BOOLEAN);
/* 121 */             map((Type)Type.BYTE);
/* 122 */             map(Type.OPTIONAL_GLOBAL_POSITION);
/* 123 */             create((Type)Type.VAR_INT, Integer.valueOf(0));
/* 124 */             handler(EntityPackets.this.worldDataTrackerHandlerByKey());
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerRewrites() {
/* 131 */     filter().handler((event, meta) -> meta.setMetaType(Types1_20.META_TYPES.byId(meta.metaType().typeId())));
/* 132 */     registerMetaTypeHandler(Types1_20.META_TYPES.itemType, Types1_20.META_TYPES.blockStateType, Types1_20.META_TYPES.optionalBlockStateType, Types1_20.META_TYPES.particleType);
/*     */ 
/*     */     
/* 135 */     filter().filterFamily((EntityType)Entity1_19_4Types.ITEM_DISPLAY).handler((event, meta) -> {
/*     */           if (event.trackedEntity().hasSentMetadata() || event.hasExtraMeta()) {
/*     */             return;
/*     */           }
/*     */           
/*     */           if (event.metaAtIndex(12) == null) {
/*     */             event.createExtraMeta(new Metadata(12, Types1_20.META_TYPES.quaternionType, Y_FLIPPED_ROTATION));
/*     */           }
/*     */         });
/* 144 */     filter().filterFamily((EntityType)Entity1_19_4Types.ITEM_DISPLAY).index(12).handler((event, meta) -> {
/*     */           Quaternion quaternion = (Quaternion)meta.value();
/*     */           
/*     */           meta.setValue(rotateY180(quaternion));
/*     */         });
/* 149 */     filter().filterFamily((EntityType)Entity1_19_4Types.MINECART_ABSTRACT).index(11).handler((event, meta) -> {
/*     */           int blockState = ((Integer)meta.value()).intValue();
/*     */           meta.setValue(Integer.valueOf(((Protocol1_20To1_19_4)this.protocol).getMappingData().getNewBlockStateId(blockState)));
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityType typeFromId(int type) {
/* 157 */     return Entity1_19_4Types.getTypeFromId(type);
/*     */   }
/*     */   
/*     */   private Quaternion rotateY180(Quaternion quaternion) {
/* 161 */     return new Quaternion(-quaternion.z(), quaternion.w(), quaternion.x(), -quaternion.y());
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_20to1_19_4\packets\EntityPackets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */