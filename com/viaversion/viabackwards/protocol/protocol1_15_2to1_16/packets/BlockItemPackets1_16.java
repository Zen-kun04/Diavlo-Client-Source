/*     */ package com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.packets;
/*     */ 
/*     */ import com.viaversion.viabackwards.ViaBackwards;
/*     */ import com.viaversion.viabackwards.api.BackwardsProtocol;
/*     */ import com.viaversion.viabackwards.api.rewriters.EnchantmentRewriter;
/*     */ import com.viaversion.viabackwards.api.rewriters.ItemRewriter;
/*     */ import com.viaversion.viabackwards.api.rewriters.MapColorRewriter;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.Protocol1_15_2To1_16;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.data.MapColorRewrites;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_16_1to1_16_2.storage.BiomeStorage;
/*     */ import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
/*     */ import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
/*     */ import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
/*     */ import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
/*     */ import com.viaversion.viaversion.api.minecraft.item.Item;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
/*     */ import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import com.viaversion.viaversion.api.type.types.UUIDIntArrayType;
/*     */ import com.viaversion.viaversion.libs.gson.JsonElement;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.LongArrayTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
/*     */ import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
/*     */ import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
/*     */ import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.types.Chunk1_15Type;
/*     */ import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
/*     */ import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.packets.InventoryPackets;
/*     */ import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.types.Chunk1_16Type;
/*     */ import com.viaversion.viaversion.rewriter.BlockRewriter;
/*     */ import com.viaversion.viaversion.rewriter.RecipeRewriter;
/*     */ import com.viaversion.viaversion.util.CompactArrayUtil;
/*     */ import com.viaversion.viaversion.util.Key;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class BlockItemPackets1_16
/*     */   extends ItemRewriter<ClientboundPackets1_16, ServerboundPackets1_14, Protocol1_15_2To1_16>
/*     */ {
/*     */   private EnchantmentRewriter enchantmentRewriter;
/*     */   
/*     */   public BlockItemPackets1_16(Protocol1_15_2To1_16 protocol) {
/*  63 */     super((BackwardsProtocol)protocol);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerPackets() {
/*  68 */     BlockRewriter<ClientboundPackets1_16> blockRewriter = new BlockRewriter(this.protocol, Type.POSITION1_14);
/*     */     
/*  70 */     RecipeRewriter<ClientboundPackets1_16> recipeRewriter = new RecipeRewriter(this.protocol);
/*     */     
/*  72 */     ((Protocol1_15_2To1_16)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_16.DECLARE_RECIPES, wrapper -> {
/*     */           int size = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */           
/*     */           int newSize = size;
/*     */           
/*     */           for (int i = 0; i < size; i++) {
/*     */             String originalType = (String)wrapper.read(Type.STRING);
/*     */             
/*     */             String type = Key.stripMinecraftNamespace(originalType);
/*     */             
/*     */             if (type.equals("smithing")) {
/*     */               newSize--;
/*     */               wrapper.read(Type.STRING);
/*     */               wrapper.read(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
/*     */               wrapper.read(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
/*     */               wrapper.read(Type.FLAT_VAR_INT_ITEM);
/*     */             } else {
/*     */               wrapper.write(Type.STRING, originalType);
/*     */               wrapper.passthrough(Type.STRING);
/*     */               recipeRewriter.handleRecipeType(wrapper, type);
/*     */             } 
/*     */           } 
/*     */           wrapper.set((Type)Type.VAR_INT, 0, Integer.valueOf(newSize));
/*     */         });
/*  96 */     registerSetCooldown((ClientboundPacketType)ClientboundPackets1_16.COOLDOWN);
/*  97 */     registerWindowItems((ClientboundPacketType)ClientboundPackets1_16.WINDOW_ITEMS, Type.FLAT_VAR_INT_ITEM_ARRAY);
/*  98 */     registerSetSlot((ClientboundPacketType)ClientboundPackets1_16.SET_SLOT, Type.FLAT_VAR_INT_ITEM);
/*  99 */     registerTradeList((ClientboundPacketType)ClientboundPackets1_16.TRADE_LIST);
/* 100 */     registerAdvancements((ClientboundPacketType)ClientboundPackets1_16.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
/*     */     
/* 102 */     blockRewriter.registerAcknowledgePlayerDigging((ClientboundPacketType)ClientboundPackets1_16.ACKNOWLEDGE_PLAYER_DIGGING);
/* 103 */     blockRewriter.registerBlockAction((ClientboundPacketType)ClientboundPackets1_16.BLOCK_ACTION);
/* 104 */     blockRewriter.registerBlockChange((ClientboundPacketType)ClientboundPackets1_16.BLOCK_CHANGE);
/* 105 */     blockRewriter.registerMultiBlockChange((ClientboundPacketType)ClientboundPackets1_16.MULTI_BLOCK_CHANGE);
/*     */     
/* 107 */     ((Protocol1_15_2To1_16)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_16.ENTITY_EQUIPMENT, wrapper -> {
/*     */           byte slot;
/*     */           
/*     */           int entityId = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */           
/*     */           List<EquipmentData> equipmentData = new ArrayList<>();
/*     */           
/*     */           do {
/*     */             slot = ((Byte)wrapper.read((Type)Type.BYTE)).byteValue();
/*     */             
/*     */             Item item = handleItemToClient((Item)wrapper.read(Type.FLAT_VAR_INT_ITEM));
/*     */             
/*     */             int rawSlot = slot & Byte.MAX_VALUE;
/*     */             
/*     */             equipmentData.add(new EquipmentData(rawSlot, item));
/*     */           } while ((slot & Byte.MIN_VALUE) != 0);
/*     */           EquipmentData firstData = equipmentData.get(0);
/*     */           wrapper.write((Type)Type.VAR_INT, Integer.valueOf(firstData.slot));
/*     */           wrapper.write(Type.FLAT_VAR_INT_ITEM, firstData.item);
/*     */           for (int i = 1; i < equipmentData.size(); i++) {
/*     */             PacketWrapper equipmentPacket = wrapper.create((PacketType)ClientboundPackets1_15.ENTITY_EQUIPMENT);
/*     */             EquipmentData data = equipmentData.get(i);
/*     */             equipmentPacket.write((Type)Type.VAR_INT, Integer.valueOf(entityId));
/*     */             equipmentPacket.write((Type)Type.VAR_INT, Integer.valueOf(data.slot));
/*     */             equipmentPacket.write(Type.FLAT_VAR_INT_ITEM, data.item);
/*     */             equipmentPacket.send(Protocol1_15_2To1_16.class);
/*     */           } 
/*     */         });
/* 135 */     ((Protocol1_15_2To1_16)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_16.UPDATE_LIGHT, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 138 */             map((Type)Type.VAR_INT);
/* 139 */             map((Type)Type.VAR_INT);
/* 140 */             map((Type)Type.BOOLEAN, (Type)Type.NOTHING);
/*     */           }
/*     */         });
/*     */     
/* 144 */     ((Protocol1_15_2To1_16)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_16.CHUNK_DATA, wrapper -> {
/*     */           Chunk chunk = (Chunk)wrapper.read((Type)new Chunk1_16Type());
/*     */           wrapper.write((Type)new Chunk1_15Type(), chunk);
/*     */           for (int i = 0; i < (chunk.getSections()).length; i++) {
/*     */             ChunkSection section = chunk.getSections()[i];
/*     */             if (section != null) {
/*     */               DataPalette palette = section.palette(PaletteType.BLOCKS);
/*     */               for (int j = 0; j < palette.size(); j++) {
/*     */                 int mappedBlockStateId = ((Protocol1_15_2To1_16)this.protocol).getMappingData().getNewBlockStateId(palette.idByIndex(j));
/*     */                 palette.setIdByIndex(j, mappedBlockStateId);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */           CompoundTag heightMaps = chunk.getHeightMap();
/*     */           for (Tag heightMapTag : heightMaps.values()) {
/*     */             LongArrayTag heightMap = (LongArrayTag)heightMapTag;
/*     */             int[] heightMapData = new int[256];
/*     */             CompactArrayUtil.iterateCompactArrayWithPadding(9, heightMapData.length, heightMap.getValue(), ());
/*     */             heightMap.setValue(CompactArrayUtil.createCompactArray(9, heightMapData.length, ()));
/*     */           } 
/*     */           if (chunk.isBiomeData()) {
/*     */             if (wrapper.user().getProtocolInfo().getServerProtocolVersion() >= ProtocolVersion.v1_16_2.getVersion()) {
/*     */               BiomeStorage biomeStorage = (BiomeStorage)wrapper.user().get(BiomeStorage.class);
/*     */               for (int j = 0; j < 1024; j++) {
/*     */                 int biome = chunk.getBiomeData()[j];
/*     */                 int legacyBiome = biomeStorage.legacyBiome(biome);
/*     */                 if (legacyBiome == -1) {
/*     */                   ViaBackwards.getPlatform().getLogger().warning("Biome sent that does not exist in the biome registry: " + biome);
/*     */                   legacyBiome = 1;
/*     */                 } 
/*     */                 chunk.getBiomeData()[j] = legacyBiome;
/*     */               } 
/*     */             } else {
/*     */               for (int j = 0; j < 1024; j++) {
/*     */                 int biome = chunk.getBiomeData()[j];
/*     */                 switch (biome) {
/*     */                   case 170:
/*     */                   case 171:
/*     */                   case 172:
/*     */                   case 173:
/*     */                     chunk.getBiomeData()[j] = 8;
/*     */                     break;
/*     */                 } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               } 
/*     */             } 
/*     */           }
/*     */           if (chunk.getBlockEntities() == null) {
/*     */             return;
/*     */           }
/*     */           for (CompoundTag blockEntity : chunk.getBlockEntities()) {
/*     */             handleBlockEntity(blockEntity);
/*     */           }
/*     */         });
/* 202 */     blockRewriter.registerEffect((ClientboundPacketType)ClientboundPackets1_16.EFFECT, 1010, 2001);
/*     */     
/* 204 */     registerSpawnParticle((ClientboundPacketType)ClientboundPackets1_16.SPAWN_PARTICLE, Type.FLAT_VAR_INT_ITEM, (Type)Type.DOUBLE);
/*     */     
/* 206 */     ((Protocol1_15_2To1_16)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_16.WINDOW_PROPERTY, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 209 */             map((Type)Type.UNSIGNED_BYTE);
/* 210 */             map((Type)Type.SHORT);
/* 211 */             map((Type)Type.SHORT);
/* 212 */             handler(wrapper -> {
/*     */                   short property = ((Short)wrapper.get((Type)Type.SHORT, 0)).shortValue();
/*     */                   if (property >= 4 && property <= 6) {
/*     */                     short enchantmentId = ((Short)wrapper.get((Type)Type.SHORT, 1)).shortValue();
/*     */                     if (enchantmentId > 11) {
/*     */                       enchantmentId = (short)(enchantmentId - 1);
/*     */                       wrapper.set((Type)Type.SHORT, 1, Short.valueOf(enchantmentId));
/*     */                     } else if (enchantmentId == 11) {
/*     */                       wrapper.set((Type)Type.SHORT, 1, Short.valueOf((short)9));
/*     */                     } 
/*     */                   } 
/*     */                 });
/*     */           }
/*     */         });
/* 226 */     ((Protocol1_15_2To1_16)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_16.MAP_DATA, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 229 */             map((Type)Type.VAR_INT);
/* 230 */             map((Type)Type.BYTE);
/* 231 */             map((Type)Type.BOOLEAN);
/* 232 */             map((Type)Type.BOOLEAN);
/* 233 */             handler(MapColorRewriter.getRewriteHandler(MapColorRewrites::getMappedColor));
/*     */           }
/*     */         });
/*     */     
/* 237 */     ((Protocol1_15_2To1_16)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_16.BLOCK_ENTITY_DATA, wrapper -> {
/*     */           wrapper.passthrough(Type.POSITION1_14);
/*     */           
/*     */           wrapper.passthrough((Type)Type.UNSIGNED_BYTE);
/*     */           CompoundTag tag = (CompoundTag)wrapper.passthrough(Type.NBT);
/*     */           handleBlockEntity(tag);
/*     */         });
/* 244 */     registerClickWindow((ServerboundPacketType)ServerboundPackets1_14.CLICK_WINDOW, Type.FLAT_VAR_INT_ITEM);
/* 245 */     registerCreativeInvAction((ServerboundPacketType)ServerboundPackets1_14.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
/*     */     
/* 247 */     ((Protocol1_15_2To1_16)this.protocol).registerServerbound((ServerboundPacketType)ServerboundPackets1_14.EDIT_BOOK, wrapper -> handleItemToServer((Item)wrapper.passthrough(Type.FLAT_VAR_INT_ITEM)));
/*     */   }
/*     */   
/*     */   private void handleBlockEntity(CompoundTag tag) {
/* 251 */     StringTag idTag = (StringTag)tag.get("id");
/* 252 */     if (idTag == null)
/*     */       return; 
/* 254 */     String id = idTag.getValue();
/* 255 */     if (id.equals("minecraft:conduit")) {
/* 256 */       Tag targetUuidTag = tag.remove("Target");
/* 257 */       if (!(targetUuidTag instanceof IntArrayTag)) {
/*     */         return;
/*     */       }
/* 260 */       UUID targetUuid = UUIDIntArrayType.uuidFromIntArray((int[])targetUuidTag.getValue());
/* 261 */       tag.put("target_uuid", (Tag)new StringTag(targetUuid.toString()));
/* 262 */     } else if (id.equals("minecraft:skull")) {
/* 263 */       Tag skullOwnerTag = tag.remove("SkullOwner");
/* 264 */       if (!(skullOwnerTag instanceof CompoundTag))
/*     */         return; 
/* 266 */       CompoundTag skullOwnerCompoundTag = (CompoundTag)skullOwnerTag;
/* 267 */       Tag ownerUuidTag = skullOwnerCompoundTag.remove("Id");
/* 268 */       if (ownerUuidTag instanceof IntArrayTag) {
/* 269 */         UUID ownerUuid = UUIDIntArrayType.uuidFromIntArray((int[])ownerUuidTag.getValue());
/* 270 */         skullOwnerCompoundTag.put("Id", (Tag)new StringTag(ownerUuid.toString()));
/*     */       } 
/*     */ 
/*     */       
/* 274 */       CompoundTag ownerTag = new CompoundTag();
/* 275 */       for (Map.Entry<String, Tag> entry : (Iterable<Map.Entry<String, Tag>>)skullOwnerCompoundTag) {
/* 276 */         ownerTag.put(entry.getKey(), entry.getValue());
/*     */       }
/* 278 */       tag.put("Owner", (Tag)ownerTag);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerRewrites() {
/* 284 */     this.enchantmentRewriter = new EnchantmentRewriter(this);
/* 285 */     this.enchantmentRewriter.registerEnchantment("minecraft:soul_speed", "§7Soul Speed");
/*     */   }
/*     */ 
/*     */   
/*     */   public Item handleItemToClient(Item item) {
/* 290 */     if (item == null) return null;
/*     */     
/* 292 */     super.handleItemToClient(item);
/*     */     
/* 294 */     CompoundTag tag = item.tag();
/* 295 */     if (item.identifier() == 771 && tag != null) {
/* 296 */       Tag ownerTag = tag.get("SkullOwner");
/* 297 */       if (ownerTag instanceof CompoundTag) {
/* 298 */         CompoundTag ownerCompundTag = (CompoundTag)ownerTag;
/* 299 */         Tag idTag = ownerCompundTag.get("Id");
/* 300 */         if (idTag instanceof IntArrayTag) {
/* 301 */           UUID ownerUuid = UUIDIntArrayType.uuidFromIntArray((int[])idTag.getValue());
/* 302 */           ownerCompundTag.put("Id", (Tag)new StringTag(ownerUuid.toString()));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 308 */     if ((item.identifier() == 758 || item.identifier() == 759) && tag != null) {
/* 309 */       Tag pagesTag = tag.get("pages");
/* 310 */       if (pagesTag instanceof com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag) {
/* 311 */         for (Tag page : pagesTag) {
/* 312 */           if (!(page instanceof StringTag)) {
/*     */             continue;
/*     */           }
/*     */           
/* 316 */           StringTag pageTag = (StringTag)page;
/* 317 */           JsonElement jsonElement = ((Protocol1_15_2To1_16)this.protocol).getTranslatableRewriter().processText(pageTag.getValue());
/* 318 */           pageTag.setValue(jsonElement.toString());
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 323 */     InventoryPackets.newToOldAttributes(item);
/* 324 */     this.enchantmentRewriter.handleToClient(item);
/* 325 */     return item;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item handleItemToServer(Item item) {
/* 330 */     if (item == null) return null;
/*     */     
/* 332 */     int identifier = item.identifier();
/* 333 */     super.handleItemToServer(item);
/*     */     
/* 335 */     CompoundTag tag = item.tag();
/* 336 */     if (identifier == 771 && tag != null) {
/* 337 */       Tag ownerTag = tag.get("SkullOwner");
/* 338 */       if (ownerTag instanceof CompoundTag) {
/* 339 */         CompoundTag ownerCompundTag = (CompoundTag)ownerTag;
/* 340 */         Tag idTag = ownerCompundTag.get("Id");
/* 341 */         if (idTag instanceof StringTag) {
/* 342 */           UUID ownerUuid = UUID.fromString((String)idTag.getValue());
/* 343 */           ownerCompundTag.put("Id", (Tag)new IntArrayTag(UUIDIntArrayType.uuidToIntArray(ownerUuid)));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 348 */     InventoryPackets.oldToNewAttributes(item);
/* 349 */     this.enchantmentRewriter.handleToServer(item);
/* 350 */     return item;
/*     */   }
/*     */   
/*     */   private static final class EquipmentData {
/*     */     private final int slot;
/*     */     private final Item item;
/*     */     
/*     */     private EquipmentData(int slot, Item item) {
/* 358 */       this.slot = slot;
/* 359 */       this.item = item;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viabackwards\protocol\protocol1_15_2to1_16\packets\BlockItemPackets1_16.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */