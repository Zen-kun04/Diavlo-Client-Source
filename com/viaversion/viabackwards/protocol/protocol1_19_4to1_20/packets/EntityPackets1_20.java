/*     */ package com.viaversion.viabackwards.protocol.protocol1_19_4to1_20.packets;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.viaversion.viabackwards.api.BackwardsProtocol;
/*     */ import com.viaversion.viabackwards.api.rewriters.EntityRewriter;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_19_4to1_20.Protocol1_19_4To1_20;
/*     */ import com.viaversion.viaversion.api.minecraft.Quaternion;
/*     */ import com.viaversion.viaversion.api.minecraft.entities.Entity1_19_4Types;
/*     */ import com.viaversion.viaversion.api.minecraft.entities.EntityType;
/*     */ import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import com.viaversion.viaversion.api.type.types.version.Types1_19_4;
/*     */ import com.viaversion.viaversion.api.type.types.version.Types1_20;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ClientboundPackets1_19_4;
/*     */ import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
/*     */ import com.viaversion.viaversion.util.Key;
/*     */ import java.util.Set;
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
/*     */ public final class EntityPackets1_20
/*     */   extends EntityRewriter<ClientboundPackets1_19_4, Protocol1_19_4To1_20>
/*     */ {
/*  42 */   private final Set<String> newTrimPatterns = Sets.newHashSet((Object[])new String[] { "host_armor_trim_smithing_template", "raiser_armor_trim_smithing_template", "silence_armor_trim_smithing_template", "shaper_armor_trim_smithing_template", "wayfinder_armor_trim_smithing_template" });
/*     */   
/*  44 */   private static final Quaternion Y_FLIPPED_ROTATION = new Quaternion(0.0F, 1.0F, 0.0F, 0.0F);
/*     */   
/*     */   public EntityPackets1_20(Protocol1_19_4To1_20 protocol) {
/*  47 */     super((BackwardsProtocol)protocol);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerPackets() {
/*  52 */     registerTrackerWithData1_19((ClientboundPacketType)ClientboundPackets1_19_4.SPAWN_ENTITY, (EntityType)Entity1_19_4Types.FALLING_BLOCK);
/*  53 */     registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_19_4.ENTITY_METADATA, Types1_20.METADATA_LIST, Types1_19_4.METADATA_LIST);
/*  54 */     registerRemoveEntities((ClientboundPacketType)ClientboundPackets1_19_4.REMOVE_ENTITIES);
/*     */     
/*  56 */     ((Protocol1_19_4To1_20)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.JOIN_GAME, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/*  59 */             map((Type)Type.INT);
/*  60 */             map((Type)Type.BOOLEAN);
/*  61 */             map((Type)Type.UNSIGNED_BYTE);
/*  62 */             map((Type)Type.BYTE);
/*  63 */             map(Type.STRING_ARRAY);
/*  64 */             map(Type.NBT);
/*  65 */             map(Type.STRING);
/*  66 */             map(Type.STRING);
/*  67 */             map((Type)Type.LONG);
/*  68 */             map((Type)Type.VAR_INT);
/*  69 */             map((Type)Type.VAR_INT);
/*  70 */             map((Type)Type.VAR_INT);
/*  71 */             map((Type)Type.BOOLEAN);
/*  72 */             map((Type)Type.BOOLEAN);
/*  73 */             map((Type)Type.BOOLEAN);
/*  74 */             map((Type)Type.BOOLEAN);
/*  75 */             map(Type.OPTIONAL_GLOBAL_POSITION);
/*  76 */             read((Type)Type.VAR_INT);
/*     */             
/*  78 */             handler(EntityPackets1_20.this.dimensionDataHandler());
/*  79 */             handler(EntityPackets1_20.this.biomeSizeTracker());
/*  80 */             handler(EntityPackets1_20.this.worldDataTrackerHandlerByKey());
/*  81 */             handler(wrapper -> {
/*     */                   ListTag values;
/*     */                   
/*     */                   CompoundTag registry = (CompoundTag)wrapper.get(Type.NBT, 0);
/*     */                   
/*     */                   if (registry.contains("minecraft:trim_pattern")) {
/*     */                     values = (ListTag)((CompoundTag)registry.get("minecraft:trim_pattern")).get("value");
/*     */                   } else {
/*     */                     CompoundTag trimPatternRegistry = Protocol1_19_4To1_20.MAPPINGS.getTrimPatternRegistry().clone();
/*     */                     
/*     */                     registry.put("minecraft:trim_pattern", (Tag)trimPatternRegistry);
/*     */                     
/*     */                     values = (ListTag)trimPatternRegistry.get("value");
/*     */                   } 
/*     */                   for (Tag entry : values) {
/*     */                     CompoundTag element = (CompoundTag)((CompoundTag)entry).get("element");
/*     */                     StringTag templateItem = (StringTag)element.get("template_item");
/*     */                     if (EntityPackets1_20.this.newTrimPatterns.contains(Key.stripMinecraftNamespace(templateItem.getValue()))) {
/*     */                       templateItem.setValue("minecraft:spire_armor_trim_smithing_template");
/*     */                     }
/*     */                   } 
/*     */                 });
/*     */           }
/*     */         });
/* 105 */     ((Protocol1_19_4To1_20)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.RESPAWN, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 108 */             map(Type.STRING);
/* 109 */             map(Type.STRING);
/* 110 */             map((Type)Type.LONG);
/* 111 */             map((Type)Type.UNSIGNED_BYTE);
/* 112 */             map((Type)Type.BYTE);
/* 113 */             map((Type)Type.BOOLEAN);
/* 114 */             map((Type)Type.BOOLEAN);
/* 115 */             map((Type)Type.BYTE);
/* 116 */             map(Type.OPTIONAL_GLOBAL_POSITION);
/* 117 */             read((Type)Type.VAR_INT);
/* 118 */             handler(EntityPackets1_20.this.worldDataTrackerHandlerByKey());
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerRewrites() {
/* 125 */     filter().handler((event, meta) -> meta.setMetaType(Types1_19_4.META_TYPES.byId(meta.metaType().typeId())));
/* 126 */     registerMetaTypeHandler(Types1_19_4.META_TYPES.itemType, Types1_19_4.META_TYPES.blockStateType, Types1_19_4.META_TYPES.optionalBlockStateType, Types1_19_4.META_TYPES.particleType, Types1_19_4.META_TYPES.componentType, Types1_19_4.META_TYPES.optionalComponentType);
/*     */ 
/*     */     
/* 129 */     filter().filterFamily((EntityType)Entity1_19_4Types.MINECART_ABSTRACT).index(11).handler((event, meta) -> {
/*     */           int blockState = ((Integer)meta.value()).intValue();
/*     */           
/*     */           meta.setValue(Integer.valueOf(((Protocol1_19_4To1_20)this.protocol).getMappingData().getNewBlockStateId(blockState)));
/*     */         });
/*     */     
/* 135 */     filter().filterFamily((EntityType)Entity1_19_4Types.ITEM_DISPLAY).handler((event, meta) -> {
/*     */           if (event.trackedEntity().hasSentMetadata() || event.hasExtraMeta()) {
/*     */             return;
/*     */           }
/*     */           
/*     */           if (event.metaAtIndex(12) == null) {
/*     */             event.createExtraMeta(new Metadata(12, Types1_19_4.META_TYPES.quaternionType, Y_FLIPPED_ROTATION));
/*     */           }
/*     */         });
/* 144 */     filter().filterFamily((EntityType)Entity1_19_4Types.ITEM_DISPLAY).index(12).handler((event, meta) -> {
/*     */           Quaternion quaternion = (Quaternion)meta.value();
/*     */           meta.setValue(rotateY180(quaternion));
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityType typeFromId(int type) {
/* 152 */     return Entity1_19_4Types.getTypeFromId(type);
/*     */   }
/*     */   
/*     */   private Quaternion rotateY180(Quaternion quaternion) {
/* 156 */     return new Quaternion(-quaternion.z(), quaternion.w(), quaternion.x(), -quaternion.y());
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viabackwards\protocol\protocol1_19_4to1_20\packets\EntityPackets1_20.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */