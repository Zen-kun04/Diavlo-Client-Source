/*     */ package com.viaversion.viabackwards.protocol.protocol1_19_3to1_19_4.packets;
/*     */ 
/*     */ import com.viaversion.viabackwards.api.BackwardsProtocol;
/*     */ import com.viaversion.viabackwards.api.entities.storage.EntityData;
/*     */ import com.viaversion.viabackwards.api.entities.storage.WrappedMetadata;
/*     */ import com.viaversion.viabackwards.api.rewriters.EntityRewriter;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_19_3to1_19_4.Protocol1_19_3To1_19_4;
/*     */ import com.viaversion.viaversion.api.minecraft.entities.Entity1_19_4Types;
/*     */ import com.viaversion.viaversion.api.minecraft.entities.EntityType;
/*     */ import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import com.viaversion.viaversion.api.type.types.version.Types1_19_3;
/*     */ import com.viaversion.viaversion.api.type.types.version.Types1_19_4;
/*     */ import com.viaversion.viaversion.libs.gson.JsonElement;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1.ClientboundPackets1_19_3;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ClientboundPackets1_19_4;
/*     */ import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class EntityPackets1_19_4
/*     */   extends EntityRewriter<ClientboundPackets1_19_4, Protocol1_19_3To1_19_4>
/*     */ {
/*     */   public EntityPackets1_19_4(Protocol1_19_3To1_19_4 protocol) {
/*  38 */     super((BackwardsProtocol)protocol, Types1_19_3.META_TYPES.optionalComponentType, Types1_19_3.META_TYPES.booleanType);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerPackets() {
/*  43 */     registerTrackerWithData1_19((ClientboundPacketType)ClientboundPackets1_19_4.SPAWN_ENTITY, (EntityType)Entity1_19_4Types.FALLING_BLOCK);
/*  44 */     registerRemoveEntities((ClientboundPacketType)ClientboundPackets1_19_4.REMOVE_ENTITIES);
/*  45 */     registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_19_4.ENTITY_METADATA, Types1_19_4.METADATA_LIST, Types1_19_3.METADATA_LIST);
/*     */     
/*  47 */     ((Protocol1_19_3To1_19_4)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.JOIN_GAME, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/*  50 */             map((Type)Type.INT);
/*  51 */             map((Type)Type.BOOLEAN);
/*  52 */             map((Type)Type.UNSIGNED_BYTE);
/*  53 */             map((Type)Type.BYTE);
/*  54 */             map(Type.STRING_ARRAY);
/*  55 */             map(Type.NBT);
/*  56 */             map(Type.STRING);
/*  57 */             map(Type.STRING);
/*  58 */             handler(EntityPackets1_19_4.this.dimensionDataHandler());
/*  59 */             handler(EntityPackets1_19_4.this.biomeSizeTracker());
/*  60 */             handler(EntityPackets1_19_4.this.worldDataTrackerHandlerByKey());
/*  61 */             handler(wrapper -> {
/*     */                   CompoundTag registry = (CompoundTag)wrapper.get(Type.NBT, 0);
/*     */                   
/*     */                   registry.remove("minecraft:trim_pattern");
/*     */                   
/*     */                   registry.remove("minecraft:trim_material");
/*     */                   registry.remove("minecraft:damage_type");
/*     */                   CompoundTag biomeRegistry = (CompoundTag)registry.get("minecraft:worldgen/biome");
/*     */                   ListTag biomes = (ListTag)biomeRegistry.get("value");
/*     */                   for (Tag biomeTag : biomes) {
/*     */                     CompoundTag biomeData = (CompoundTag)((CompoundTag)biomeTag).get("element");
/*     */                     NumberTag hasPrecipitation = (NumberTag)biomeData.get("has_precipitation");
/*     */                     biomeData.put("precipitation", (Tag)new StringTag((hasPrecipitation.asByte() == 1) ? "rain" : "none"));
/*     */                   } 
/*     */                 });
/*     */           }
/*     */         });
/*  78 */     ((Protocol1_19_3To1_19_4)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.PLAYER_POSITION, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           protected void register() {
/*  81 */             map((Type)Type.DOUBLE);
/*  82 */             map((Type)Type.DOUBLE);
/*  83 */             map((Type)Type.DOUBLE);
/*  84 */             map((Type)Type.FLOAT);
/*  85 */             map((Type)Type.FLOAT);
/*  86 */             map((Type)Type.BYTE);
/*  87 */             map((Type)Type.VAR_INT);
/*  88 */             create((Type)Type.BOOLEAN, Boolean.valueOf(false));
/*     */           }
/*     */         });
/*     */     
/*  92 */     ((Protocol1_19_3To1_19_4)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.DAMAGE_EVENT, (ClientboundPacketType)ClientboundPackets1_19_3.ENTITY_STATUS, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/*  95 */             map((Type)Type.VAR_INT, (Type)Type.INT);
/*  96 */             read((Type)Type.VAR_INT);
/*  97 */             read((Type)Type.VAR_INT);
/*  98 */             read((Type)Type.VAR_INT);
/*  99 */             handler(wrapper -> {
/*     */                   if (((Boolean)wrapper.read((Type)Type.BOOLEAN)).booleanValue()) {
/*     */                     wrapper.read((Type)Type.DOUBLE);
/*     */                     
/*     */                     wrapper.read((Type)Type.DOUBLE);
/*     */                     wrapper.read((Type)Type.DOUBLE);
/*     */                   } 
/*     */                 });
/* 107 */             create((Type)Type.BYTE, Byte.valueOf((byte)2));
/*     */           }
/*     */         });
/*     */     
/* 111 */     ((Protocol1_19_3To1_19_4)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.HIT_ANIMATION, (ClientboundPacketType)ClientboundPackets1_19_3.ENTITY_ANIMATION, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 114 */             map((Type)Type.VAR_INT);
/* 115 */             read((Type)Type.FLOAT);
/* 116 */             create((Type)Type.UNSIGNED_BYTE, Short.valueOf((short)1));
/*     */           }
/*     */         });
/*     */     
/* 120 */     ((Protocol1_19_3To1_19_4)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.RESPAWN, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 123 */             map(Type.STRING);
/* 124 */             map(Type.STRING);
/* 125 */             handler(EntityPackets1_19_4.this.worldDataTrackerHandlerByKey());
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerRewrites() {
/* 132 */     filter().handler((event, meta) -> {
/*     */           int id = meta.metaType().typeId();
/*     */           if (id >= 25) {
/*     */             return;
/*     */           }
/*     */           if (id >= 15) {
/*     */             id--;
/*     */           }
/*     */           meta.setMetaType(Types1_19_3.META_TYPES.byId(id));
/*     */         });
/* 142 */     registerMetaTypeHandler(Types1_19_3.META_TYPES.itemType, Types1_19_3.META_TYPES.blockStateType, null, Types1_19_3.META_TYPES.particleType, Types1_19_3.META_TYPES.componentType, Types1_19_3.META_TYPES.optionalComponentType);
/*     */ 
/*     */     
/* 145 */     filter().filterFamily((EntityType)Entity1_19_4Types.MINECART_ABSTRACT).index(11).handler((event, meta) -> {
/*     */           int blockState = ((Integer)meta.value()).intValue();
/*     */           
/*     */           meta.setValue(Integer.valueOf(((Protocol1_19_3To1_19_4)this.protocol).getMappingData().getNewBlockStateId(blockState)));
/*     */         });
/* 150 */     filter().filterFamily((EntityType)Entity1_19_4Types.BOAT).index(11).handler((event, meta) -> {
/*     */           int boatType = ((Integer)meta.value()).intValue();
/*     */           
/*     */           if (boatType > 4) {
/*     */             meta.setValue(Integer.valueOf(boatType - 1));
/*     */           }
/*     */         });
/* 157 */     filter().type((EntityType)Entity1_19_4Types.TEXT_DISPLAY).index(22).handler((event, meta) -> {
/*     */           event.setIndex(2);
/*     */           
/*     */           meta.setMetaType(Types1_19_3.META_TYPES.optionalComponentType);
/*     */           
/*     */           event.createExtraMeta(new Metadata(3, Types1_19_3.META_TYPES.booleanType, Boolean.valueOf(true)));
/*     */           JsonElement element = (JsonElement)meta.value();
/*     */           ((Protocol1_19_3To1_19_4)this.protocol).getTranslatableRewriter().processText(element);
/*     */         });
/* 166 */     filter().filterFamily((EntityType)Entity1_19_4Types.DISPLAY).handler((event, meta) -> {
/*     */           if (event.index() > 7) {
/*     */             event.cancel();
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 174 */     filter().type((EntityType)Entity1_19_4Types.INTERACTION).removeIndex(8);
/* 175 */     filter().type((EntityType)Entity1_19_4Types.INTERACTION).removeIndex(9);
/* 176 */     filter().type((EntityType)Entity1_19_4Types.INTERACTION).removeIndex(10);
/*     */     
/* 178 */     filter().type((EntityType)Entity1_19_4Types.SNIFFER).removeIndex(17);
/* 179 */     filter().type((EntityType)Entity1_19_4Types.SNIFFER).removeIndex(18);
/*     */     
/* 181 */     filter().filterFamily((EntityType)Entity1_19_4Types.ABSTRACT_HORSE).addIndex(18);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMappingDataLoaded() {
/* 186 */     mapTypes();
/*     */     
/* 188 */     EntityData.MetaCreator displayMetaCreator = storage -> {
/*     */         storage.add(new Metadata(0, Types1_19_3.META_TYPES.byteType, Byte.valueOf((byte)32)));
/*     */         storage.add(new Metadata(5, Types1_19_3.META_TYPES.booleanType, Boolean.valueOf(true)));
/*     */         storage.add(new Metadata(15, Types1_19_3.META_TYPES.byteType, Byte.valueOf((byte)17)));
/*     */       };
/* 193 */     mapEntityTypeWithData((EntityType)Entity1_19_4Types.TEXT_DISPLAY, (EntityType)Entity1_19_4Types.ARMOR_STAND).spawnMetadata(displayMetaCreator);
/* 194 */     mapEntityTypeWithData((EntityType)Entity1_19_4Types.ITEM_DISPLAY, (EntityType)Entity1_19_4Types.ARMOR_STAND).spawnMetadata(displayMetaCreator);
/* 195 */     mapEntityTypeWithData((EntityType)Entity1_19_4Types.BLOCK_DISPLAY, (EntityType)Entity1_19_4Types.ARMOR_STAND).spawnMetadata(displayMetaCreator);
/*     */     
/* 197 */     mapEntityTypeWithData((EntityType)Entity1_19_4Types.INTERACTION, (EntityType)Entity1_19_4Types.ARMOR_STAND).spawnMetadata(displayMetaCreator);
/*     */     
/* 199 */     mapEntityTypeWithData((EntityType)Entity1_19_4Types.SNIFFER, (EntityType)Entity1_19_4Types.RAVAGER).jsonName();
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityType typeFromId(int type) {
/* 204 */     return Entity1_19_4Types.getTypeFromId(type);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viabackwards\protocol\protocol1_19_3to1_19_4\packets\EntityPackets1_19_4.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */