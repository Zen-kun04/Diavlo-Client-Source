/*     */ package com.viaversion.viaversion.protocols.protocol1_20_2to1_20.rewriter;
/*     */ 
/*     */ import com.viaversion.viaversion.api.data.ParticleMappings;
/*     */ import com.viaversion.viaversion.api.data.entity.EntityTracker;
/*     */ import com.viaversion.viaversion.api.minecraft.blockentity.BlockEntity;
/*     */ import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
/*     */ import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
/*     */ import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
/*     */ import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
/*     */ import com.viaversion.viaversion.api.minecraft.item.Item;
/*     */ import com.viaversion.viaversion.api.minecraft.metadata.ChunkPosition;
/*     */ import com.viaversion.viaversion.api.protocol.Protocol;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
/*     */ import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.types.Chunk1_18Type;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ClientboundPackets1_19_4;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.rewriter.RecipeRewriter1_19_4;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.Protocol1_20_2To1_20;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.packet.ServerboundPackets1_20_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.type.ChunkType1_20_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.util.PotionEffects;
/*     */ import com.viaversion.viaversion.rewriter.BlockRewriter;
/*     */ import com.viaversion.viaversion.rewriter.ItemRewriter;
/*     */ import com.viaversion.viaversion.util.MathUtil;
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
/*     */ public final class BlockItemPacketRewriter1_20_2
/*     */   extends ItemRewriter<ClientboundPackets1_19_4, ServerboundPackets1_20_2, Protocol1_20_2To1_20>
/*     */ {
/*     */   public BlockItemPacketRewriter1_20_2(Protocol1_20_2To1_20 protocol) {
/*  53 */     super((Protocol)protocol);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerPackets() {
/*  58 */     BlockRewriter<ClientboundPackets1_19_4> blockRewriter = new BlockRewriter(this.protocol, Type.POSITION1_14);
/*  59 */     blockRewriter.registerBlockAction((ClientboundPacketType)ClientboundPackets1_19_4.BLOCK_ACTION);
/*  60 */     blockRewriter.registerBlockChange((ClientboundPacketType)ClientboundPackets1_19_4.BLOCK_CHANGE);
/*  61 */     blockRewriter.registerVarLongMultiBlockChange1_20((ClientboundPacketType)ClientboundPackets1_19_4.MULTI_BLOCK_CHANGE);
/*  62 */     blockRewriter.registerEffect((ClientboundPacketType)ClientboundPackets1_19_4.EFFECT, 1010, 2001);
/*     */     
/*  64 */     ((Protocol1_20_2To1_20)this.protocol).registerServerbound((ServerboundPacketType)ServerboundPackets1_20_2.SET_BEACON_EFFECT, wrapper -> {
/*     */           if (((Boolean)wrapper.passthrough((Type)Type.BOOLEAN)).booleanValue()) {
/*     */             wrapper.write((Type)Type.VAR_INT, Integer.valueOf(((Integer)wrapper.read((Type)Type.VAR_INT)).intValue() + 1));
/*     */           }
/*     */           
/*     */           if (((Boolean)wrapper.passthrough((Type)Type.BOOLEAN)).booleanValue()) {
/*     */             wrapper.write((Type)Type.VAR_INT, Integer.valueOf(((Integer)wrapper.read((Type)Type.VAR_INT)).intValue() + 1));
/*     */           }
/*     */         });
/*     */     
/*  74 */     ((Protocol1_20_2To1_20)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.UNLOAD_CHUNK, wrapper -> {
/*     */           int x = ((Integer)wrapper.read((Type)Type.INT)).intValue();
/*     */           
/*     */           int z = ((Integer)wrapper.read((Type)Type.INT)).intValue();
/*     */           wrapper.write(Type.CHUNK_POSITION, new ChunkPosition(x, z));
/*     */         });
/*  80 */     ((Protocol1_20_2To1_20)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.NBT_QUERY, wrapper -> {
/*     */           wrapper.passthrough((Type)Type.VAR_INT);
/*     */           
/*     */           wrapper.write(Type.NAMELESS_NBT, wrapper.read(Type.NBT));
/*     */         });
/*  85 */     ((Protocol1_20_2To1_20)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.BLOCK_ENTITY_DATA, wrapper -> {
/*     */           wrapper.passthrough(Type.POSITION1_14);
/*     */           
/*     */           wrapper.passthrough((Type)Type.VAR_INT);
/*     */           wrapper.write(Type.NAMELESS_NBT, handleBlockEntity((CompoundTag)wrapper.read(Type.NBT)));
/*     */         });
/*  91 */     ((Protocol1_20_2To1_20)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.CHUNK_DATA, wrapper -> {
/*     */           EntityTracker tracker = ((Protocol1_20_2To1_20)this.protocol).getEntityRewriter().tracker(wrapper.user());
/*     */           
/*     */           Chunk1_18Type chunk1_18Type = new Chunk1_18Type(tracker.currentWorldSectionHeight(), MathUtil.ceilLog2(((Protocol1_20_2To1_20)this.protocol).getMappingData().getBlockStateMappings().size()), MathUtil.ceilLog2(tracker.biomesSent()));
/*     */           
/*     */           Chunk chunk = (Chunk)wrapper.read((Type)chunk1_18Type);
/*     */           
/*     */           ChunkType1_20_2 chunkType1_20_2 = new ChunkType1_20_2(tracker.currentWorldSectionHeight(), MathUtil.ceilLog2(((Protocol1_20_2To1_20)this.protocol).getMappingData().getBlockStateMappings().mappedSize()), MathUtil.ceilLog2(tracker.biomesSent()));
/*     */           
/*     */           wrapper.write((Type)chunkType1_20_2, chunk);
/*     */           
/*     */           for (ChunkSection section : chunk.getSections()) {
/*     */             DataPalette blockPalette = section.palette(PaletteType.BLOCKS);
/*     */             
/*     */             for (int i = 0; i < blockPalette.size(); i++) {
/*     */               int id = blockPalette.idByIndex(i);
/*     */               
/*     */               blockPalette.setIdByIndex(i, ((Protocol1_20_2To1_20)this.protocol).getMappingData().getNewBlockStateId(id));
/*     */             } 
/*     */           } 
/*     */           
/*     */           for (BlockEntity blockEntity : chunk.blockEntities()) {
/*     */             handleBlockEntity(blockEntity.tag());
/*     */           }
/*     */         });
/*     */     
/* 117 */     ((Protocol1_20_2To1_20)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.WINDOW_ITEMS, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 120 */             map((Type)Type.UNSIGNED_BYTE);
/* 121 */             map((Type)Type.VAR_INT);
/* 122 */             handler(wrapper -> {
/*     */                   Item[] items = (Item[])wrapper.read(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
/*     */                   for (Item item : items) {
/*     */                     BlockItemPacketRewriter1_20_2.this.handleItemToClient(item);
/*     */                   }
/*     */                   wrapper.write(Type.ITEM1_20_2_VAR_INT_ARRAY, items);
/*     */                   wrapper.write(Type.ITEM1_20_2, BlockItemPacketRewriter1_20_2.this.handleItemToClient((Item)wrapper.read(Type.FLAT_VAR_INT_ITEM)));
/*     */                 });
/*     */           }
/*     */         });
/* 132 */     ((Protocol1_20_2To1_20)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.SET_SLOT, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 135 */             map((Type)Type.UNSIGNED_BYTE);
/* 136 */             map((Type)Type.VAR_INT);
/* 137 */             map((Type)Type.SHORT);
/* 138 */             handler(wrapper -> wrapper.write(Type.ITEM1_20_2, BlockItemPacketRewriter1_20_2.this.handleItemToClient((Item)wrapper.read(Type.FLAT_VAR_INT_ITEM))));
/*     */           }
/*     */         });
/* 141 */     ((Protocol1_20_2To1_20)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.ADVANCEMENTS, wrapper -> {
/*     */           wrapper.passthrough((Type)Type.BOOLEAN);
/*     */           
/*     */           int size = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */           
/*     */           for (int i = 0; i < size; i++) {
/*     */             wrapper.passthrough(Type.STRING);
/*     */             
/*     */             if (((Boolean)wrapper.passthrough((Type)Type.BOOLEAN)).booleanValue()) {
/*     */               wrapper.passthrough(Type.STRING);
/*     */             }
/*     */             
/*     */             if (((Boolean)wrapper.passthrough((Type)Type.BOOLEAN)).booleanValue()) {
/*     */               wrapper.passthrough(Type.COMPONENT);
/*     */               
/*     */               wrapper.passthrough(Type.COMPONENT);
/*     */               
/*     */               wrapper.write(Type.ITEM1_20_2, handleItemToClient((Item)wrapper.read(Type.FLAT_VAR_INT_ITEM)));
/*     */               
/*     */               wrapper.passthrough((Type)Type.VAR_INT);
/*     */               int flags = ((Integer)wrapper.passthrough((Type)Type.INT)).intValue();
/*     */               if ((flags & 0x1) != 0) {
/*     */                 wrapper.passthrough(Type.STRING);
/*     */               }
/*     */               wrapper.passthrough((Type)Type.FLOAT);
/*     */               wrapper.passthrough((Type)Type.FLOAT);
/*     */             } 
/*     */             wrapper.read(Type.STRING_ARRAY);
/*     */             int requirements = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */             for (int array = 0; array < requirements; array++) {
/*     */               wrapper.passthrough(Type.STRING_ARRAY);
/*     */             }
/*     */             wrapper.passthrough((Type)Type.BOOLEAN);
/*     */           } 
/*     */         });
/* 176 */     ((Protocol1_20_2To1_20)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.ENTITY_EQUIPMENT, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 179 */             map((Type)Type.VAR_INT);
/* 180 */             handler(wrapper -> {
/*     */                   byte slot;
/*     */                   do {
/*     */                     slot = ((Byte)wrapper.passthrough((Type)Type.BYTE)).byteValue();
/*     */                     wrapper.write(Type.ITEM1_20_2, BlockItemPacketRewriter1_20_2.this.handleItemToClient((Item)wrapper.read(Type.FLAT_VAR_INT_ITEM)));
/*     */                   } while ((slot & Byte.MIN_VALUE) != 0);
/*     */                 });
/*     */           }
/*     */         });
/* 189 */     ((Protocol1_20_2To1_20)this.protocol).registerServerbound((ServerboundPacketType)ServerboundPackets1_20_2.CLICK_WINDOW, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 192 */             map((Type)Type.UNSIGNED_BYTE);
/* 193 */             map((Type)Type.VAR_INT);
/* 194 */             map((Type)Type.SHORT);
/* 195 */             map((Type)Type.BYTE);
/* 196 */             map((Type)Type.VAR_INT);
/*     */             
/* 198 */             handler(wrapper -> {
/*     */                   int length = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */                   
/*     */                   for (int i = 0; i < length; i++) {
/*     */                     wrapper.passthrough((Type)Type.SHORT);
/*     */                     
/*     */                     wrapper.write(Type.FLAT_VAR_INT_ITEM, BlockItemPacketRewriter1_20_2.this.handleItemToServer((Item)wrapper.read(Type.ITEM1_20_2)));
/*     */                   } 
/*     */                   
/*     */                   wrapper.write(Type.FLAT_VAR_INT_ITEM, BlockItemPacketRewriter1_20_2.this.handleItemToServer((Item)wrapper.read(Type.ITEM1_20_2)));
/*     */                 });
/*     */           }
/*     */         });
/* 211 */     ((Protocol1_20_2To1_20)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.TRADE_LIST, wrapper -> {
/*     */           wrapper.passthrough((Type)Type.VAR_INT);
/*     */           
/*     */           int size = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */           
/*     */           for (int i = 0; i < size; i++) {
/*     */             wrapper.write(Type.ITEM1_20_2, handleItemToClient((Item)wrapper.read(Type.FLAT_VAR_INT_ITEM)));
/*     */             wrapper.write(Type.ITEM1_20_2, handleItemToClient((Item)wrapper.read(Type.FLAT_VAR_INT_ITEM)));
/*     */             wrapper.write(Type.ITEM1_20_2, handleItemToClient((Item)wrapper.read(Type.FLAT_VAR_INT_ITEM)));
/*     */             wrapper.passthrough((Type)Type.BOOLEAN);
/*     */             wrapper.passthrough((Type)Type.INT);
/*     */             wrapper.passthrough((Type)Type.INT);
/*     */             wrapper.passthrough((Type)Type.INT);
/*     */             wrapper.passthrough((Type)Type.INT);
/*     */             wrapper.passthrough((Type)Type.FLOAT);
/*     */             wrapper.passthrough((Type)Type.INT);
/*     */           } 
/*     */         });
/* 229 */     ((Protocol1_20_2To1_20)this.protocol).registerServerbound((ServerboundPacketType)ServerboundPackets1_20_2.CREATIVE_INVENTORY_ACTION, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 232 */             map((Type)Type.SHORT);
/* 233 */             handler(wrapper -> wrapper.write(Type.FLAT_VAR_INT_ITEM, BlockItemPacketRewriter1_20_2.this.handleItemToServer((Item)wrapper.read(Type.ITEM1_20_2))));
/*     */           }
/*     */         });
/* 236 */     ((Protocol1_20_2To1_20)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.SPAWN_PARTICLE, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 239 */             map((Type)Type.VAR_INT);
/* 240 */             map((Type)Type.BOOLEAN);
/* 241 */             map((Type)Type.DOUBLE);
/* 242 */             map((Type)Type.DOUBLE);
/* 243 */             map((Type)Type.DOUBLE);
/* 244 */             map((Type)Type.FLOAT);
/* 245 */             map((Type)Type.FLOAT);
/* 246 */             map((Type)Type.FLOAT);
/* 247 */             map((Type)Type.FLOAT);
/* 248 */             map((Type)Type.INT);
/* 249 */             handler(wrapper -> {
/*     */                   int id = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
/*     */                   
/*     */                   ParticleMappings mappings = Protocol1_20_2To1_20.MAPPINGS.getParticleMappings();
/*     */                   if (mappings.isBlockParticle(id)) {
/*     */                     int data = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
/*     */                     wrapper.write((Type)Type.VAR_INT, Integer.valueOf(((Protocol1_20_2To1_20)BlockItemPacketRewriter1_20_2.this.protocol).getMappingData().getNewBlockStateId(data)));
/*     */                   } else if (mappings.isItemParticle(id)) {
/*     */                     wrapper.write(Type.ITEM1_20_2, BlockItemPacketRewriter1_20_2.this.handleItemToClient((Item)wrapper.read(Type.FLAT_VAR_INT_ITEM)));
/*     */                   } 
/*     */                 });
/*     */           }
/*     */         });
/* 262 */     (new RecipeRewriter1_19_4<ClientboundPackets1_19_4>(this.protocol)
/*     */       {
/*     */         public void handleCraftingShapeless(PacketWrapper wrapper) throws Exception {
/* 265 */           wrapper.passthrough(Type.STRING);
/* 266 */           wrapper.passthrough((Type)Type.VAR_INT);
/* 267 */           handleIngredients(wrapper);
/*     */           
/* 269 */           Item result = (Item)wrapper.read(itemType());
/* 270 */           rewrite(result);
/* 271 */           wrapper.write(Type.ITEM1_20_2, result);
/*     */         }
/*     */ 
/*     */         
/*     */         public void handleSmelting(PacketWrapper wrapper) throws Exception {
/* 276 */           wrapper.passthrough(Type.STRING);
/* 277 */           wrapper.passthrough((Type)Type.VAR_INT);
/* 278 */           handleIngredient(wrapper);
/*     */           
/* 280 */           Item result = (Item)wrapper.read(itemType());
/* 281 */           rewrite(result);
/* 282 */           wrapper.write(Type.ITEM1_20_2, result);
/*     */           
/* 284 */           wrapper.passthrough((Type)Type.FLOAT);
/* 285 */           wrapper.passthrough((Type)Type.VAR_INT);
/*     */         }
/*     */ 
/*     */         
/*     */         public void handleCraftingShaped(PacketWrapper wrapper) throws Exception {
/* 290 */           int ingredients = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue() * ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/* 291 */           wrapper.passthrough(Type.STRING);
/* 292 */           wrapper.passthrough((Type)Type.VAR_INT);
/* 293 */           for (int i = 0; i < ingredients; i++) {
/* 294 */             handleIngredient(wrapper);
/*     */           }
/*     */           
/* 297 */           Item result = (Item)wrapper.read(itemType());
/* 298 */           rewrite(result);
/* 299 */           wrapper.write(Type.ITEM1_20_2, result);
/*     */           
/* 301 */           wrapper.passthrough((Type)Type.BOOLEAN);
/*     */         }
/*     */ 
/*     */         
/*     */         public void handleStonecutting(PacketWrapper wrapper) throws Exception {
/* 306 */           wrapper.passthrough(Type.STRING);
/* 307 */           handleIngredient(wrapper);
/*     */           
/* 309 */           Item result = (Item)wrapper.read(itemType());
/* 310 */           rewrite(result);
/* 311 */           wrapper.write(Type.ITEM1_20_2, result);
/*     */         }
/*     */ 
/*     */         
/*     */         public void handleSmithing(PacketWrapper wrapper) throws Exception {
/* 316 */           handleIngredient(wrapper);
/* 317 */           handleIngredient(wrapper);
/*     */           
/* 319 */           Item result = (Item)wrapper.read(itemType());
/* 320 */           rewrite(result);
/* 321 */           wrapper.write(Type.ITEM1_20_2, result);
/*     */         }
/*     */ 
/*     */         
/*     */         public void handleSmithingTransform(PacketWrapper wrapper) throws Exception {
/* 326 */           handleIngredient(wrapper);
/* 327 */           handleIngredient(wrapper);
/* 328 */           handleIngredient(wrapper);
/*     */           
/* 330 */           Item result = (Item)wrapper.read(itemType());
/* 331 */           rewrite(result);
/* 332 */           wrapper.write(Type.ITEM1_20_2, result);
/*     */         }
/*     */ 
/*     */         
/*     */         protected void handleIngredient(PacketWrapper wrapper) throws Exception {
/* 337 */           Item[] items = (Item[])wrapper.read(itemArrayType());
/* 338 */           wrapper.write(Type.ITEM1_20_2_VAR_INT_ARRAY, items);
/* 339 */           for (Item item : items) {
/* 340 */             rewrite(item);
/*     */           }
/*     */         }
/* 343 */       }).register((ClientboundPacketType)ClientboundPackets1_19_4.DECLARE_RECIPES);
/*     */   }
/*     */ 
/*     */   
/*     */   public Item handleItemToClient(Item item) {
/* 348 */     if (item == null) {
/* 349 */       return null;
/*     */     }
/*     */     
/* 352 */     if (item.tag() != null) {
/* 353 */       to1_20_2Effects(item);
/*     */     }
/*     */     
/* 356 */     return super.handleItemToClient(item);
/*     */   }
/*     */ 
/*     */   
/*     */   public Item handleItemToServer(Item item) {
/* 361 */     if (item == null) {
/* 362 */       return null;
/*     */     }
/*     */     
/* 365 */     if (item.tag() != null) {
/* 366 */       to1_20_1Effects(item);
/*     */     }
/*     */     
/* 369 */     return super.handleItemToServer(item);
/*     */   }
/*     */   
/*     */   public static void to1_20_2Effects(Item item) {
/* 373 */     Tag customPotionEffectsTag = item.tag().remove("CustomPotionEffects");
/* 374 */     if (customPotionEffectsTag instanceof ListTag) {
/* 375 */       ListTag effectsTag = (ListTag)customPotionEffectsTag;
/* 376 */       item.tag().put("custom_potion_effects", customPotionEffectsTag);
/*     */       
/* 378 */       for (Tag tag : effectsTag) {
/* 379 */         if (!(tag instanceof CompoundTag)) {
/*     */           continue;
/*     */         }
/*     */         
/* 383 */         CompoundTag effectTag = (CompoundTag)tag;
/* 384 */         Tag idTag = effectTag.remove("Id");
/* 385 */         if (idTag instanceof NumberTag) {
/* 386 */           String key = PotionEffects.idToKey(((NumberTag)idTag).asInt());
/* 387 */           if (key != null) {
/* 388 */             effectTag.put("id", (Tag)new StringTag(key));
/*     */           }
/*     */         } 
/*     */         
/* 392 */         renameTag(effectTag, "Amplifier", "amplifier");
/* 393 */         renameTag(effectTag, "Duration", "duration");
/* 394 */         renameTag(effectTag, "Ambient", "ambient");
/* 395 */         renameTag(effectTag, "ShowParticles", "show_particles");
/* 396 */         renameTag(effectTag, "ShowIcon", "show_icon");
/* 397 */         renameTag(effectTag, "HiddenEffect", "hidden_effect");
/* 398 */         renameTag(effectTag, "FactorCalculationData", "factor_calculation_data");
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void to1_20_1Effects(Item item) {
/* 404 */     Tag customPotionEffectsTag = item.tag().remove("custom_potion_effects");
/* 405 */     if (customPotionEffectsTag instanceof ListTag) {
/* 406 */       ListTag effectsTag = (ListTag)customPotionEffectsTag;
/* 407 */       item.tag().put("CustomPotionEffects", (Tag)effectsTag);
/*     */       
/* 409 */       for (Tag tag : effectsTag) {
/* 410 */         if (!(tag instanceof CompoundTag)) {
/*     */           continue;
/*     */         }
/*     */         
/* 414 */         CompoundTag effectTag = (CompoundTag)tag;
/* 415 */         Tag idTag = effectTag.remove("id");
/* 416 */         if (idTag instanceof StringTag) {
/* 417 */           int id = PotionEffects.keyToId(((StringTag)idTag).getValue());
/* 418 */           effectTag.put("Id", (Tag)new IntTag(id));
/*     */         } 
/*     */         
/* 421 */         renameTag(effectTag, "amplifier", "Amplifier");
/* 422 */         renameTag(effectTag, "duration", "Duration");
/* 423 */         renameTag(effectTag, "ambient", "Ambient");
/* 424 */         renameTag(effectTag, "show_particles", "ShowParticles");
/* 425 */         renameTag(effectTag, "show_icon", "ShowIcon");
/* 426 */         renameTag(effectTag, "hidden_effect", "HiddenEffect");
/* 427 */         renameTag(effectTag, "factor_calculation_data", "FactorCalculationData");
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void renameTag(CompoundTag tag, String entryName, String toEntryName) {
/* 433 */     Tag entry = tag.remove(entryName);
/* 434 */     if (entry != null) {
/* 435 */       tag.put(toEntryName, entry);
/*     */     }
/*     */   }
/*     */   
/*     */   private CompoundTag handleBlockEntity(CompoundTag tag) {
/* 440 */     if (tag == null) {
/* 441 */       return null;
/*     */     }
/*     */     
/* 444 */     IntTag primaryEffect = (IntTag)tag.remove("Primary");
/* 445 */     if (primaryEffect != null && primaryEffect.asInt() != 0) {
/* 446 */       tag.put("primary_effect", (Tag)new StringTag(PotionEffects.idToKeyOrLuck(primaryEffect.asInt())));
/*     */     }
/*     */     
/* 449 */     IntTag secondaryEffect = (IntTag)tag.remove("Secondary");
/* 450 */     if (secondaryEffect != null && secondaryEffect.asInt() != 0) {
/* 451 */       tag.put("secondary_effect", (Tag)new StringTag(PotionEffects.idToKeyOrLuck(secondaryEffect.asInt())));
/*     */     }
/* 453 */     return tag;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_20_2to1_20\rewriter\BlockItemPacketRewriter1_20_2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */