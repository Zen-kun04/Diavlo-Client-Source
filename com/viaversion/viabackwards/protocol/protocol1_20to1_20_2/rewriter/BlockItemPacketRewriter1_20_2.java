/*     */ package com.viaversion.viabackwards.protocol.protocol1_20to1_20_2.rewriter;
/*     */ 
/*     */ import com.viaversion.viabackwards.api.BackwardsProtocol;
/*     */ import com.viaversion.viabackwards.api.rewriters.ItemRewriter;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_20to1_20_2.Protocol1_20To1_20_2;
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
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
/*     */ import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.types.Chunk1_18Type;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ServerboundPackets1_19_4;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.packet.ClientboundPackets1_20_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.packet.ServerboundPackets1_20_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.rewriter.RecipeRewriter1_20_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.type.ChunkType1_20_2;
/*     */ import com.viaversion.viaversion.protocols.protocol1_20_2to1_20.util.PotionEffects;
/*     */ import com.viaversion.viaversion.rewriter.BlockRewriter;
/*     */ import com.viaversion.viaversion.util.Key;
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
/*     */ public final class BlockItemPacketRewriter1_20_2
/*     */   extends ItemRewriter<ClientboundPackets1_20_2, ServerboundPackets1_19_4, Protocol1_20To1_20_2>
/*     */ {
/*     */   public BlockItemPacketRewriter1_20_2(Protocol1_20To1_20_2 protocol) {
/*  52 */     super((BackwardsProtocol)protocol, Type.ITEM1_20_2, Type.ITEM1_20_2_VAR_INT_ARRAY);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerPackets() {
/*  57 */     BlockRewriter<ClientboundPackets1_20_2> blockRewriter = new BlockRewriter(this.protocol, Type.POSITION1_14);
/*  58 */     blockRewriter.registerBlockAction((ClientboundPacketType)ClientboundPackets1_20_2.BLOCK_ACTION);
/*  59 */     blockRewriter.registerBlockChange((ClientboundPacketType)ClientboundPackets1_20_2.BLOCK_CHANGE);
/*  60 */     blockRewriter.registerVarLongMultiBlockChange1_20((ClientboundPacketType)ClientboundPackets1_20_2.MULTI_BLOCK_CHANGE);
/*  61 */     blockRewriter.registerEffect((ClientboundPacketType)ClientboundPackets1_20_2.EFFECT, 1010, 2001);
/*     */     
/*  63 */     ((Protocol1_20To1_20_2)this.protocol).cancelClientbound((ClientboundPacketType)ClientboundPackets1_20_2.CHUNK_BATCH_START);
/*  64 */     ((Protocol1_20To1_20_2)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_20_2.CHUNK_BATCH_FINISHED, null, wrapper -> {
/*     */           wrapper.cancel();
/*     */           
/*     */           PacketWrapper receivedPacket = wrapper.create((PacketType)ServerboundPackets1_20_2.CHUNK_BATCH_RECEIVED);
/*     */           
/*     */           receivedPacket.write((Type)Type.FLOAT, Float.valueOf(500.0F));
/*     */           receivedPacket.sendToServer(Protocol1_20To1_20_2.class);
/*     */         });
/*  72 */     ((Protocol1_20To1_20_2)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_20_2.UNLOAD_CHUNK, wrapper -> {
/*     */           ChunkPosition chunkPosition = (ChunkPosition)wrapper.read(Type.CHUNK_POSITION);
/*     */           
/*     */           wrapper.write((Type)Type.INT, Integer.valueOf(chunkPosition.chunkX()));
/*     */           wrapper.write((Type)Type.INT, Integer.valueOf(chunkPosition.chunkZ()));
/*     */         });
/*  78 */     ((Protocol1_20To1_20_2)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_20_2.NBT_QUERY, wrapper -> {
/*     */           wrapper.passthrough((Type)Type.VAR_INT);
/*     */           
/*     */           wrapper.write(Type.NBT, wrapper.read(Type.NAMELESS_NBT));
/*     */         });
/*  83 */     ((Protocol1_20To1_20_2)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_20_2.BLOCK_ENTITY_DATA, wrapper -> {
/*     */           wrapper.passthrough(Type.POSITION1_14);
/*     */           
/*     */           wrapper.passthrough((Type)Type.VAR_INT);
/*     */           wrapper.write(Type.NBT, handleBlockEntity((CompoundTag)wrapper.read(Type.NAMELESS_NBT)));
/*     */         });
/*  89 */     ((Protocol1_20To1_20_2)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_20_2.CHUNK_DATA, wrapper -> {
/*     */           EntityTracker tracker = ((Protocol1_20To1_20_2)this.protocol).getEntityRewriter().tracker(wrapper.user());
/*     */           
/*     */           ChunkType1_20_2 chunkType1_20_2 = new ChunkType1_20_2(tracker.currentWorldSectionHeight(), MathUtil.ceilLog2(((Protocol1_20To1_20_2)this.protocol).getMappingData().getBlockStateMappings().size()), MathUtil.ceilLog2(tracker.biomesSent()));
/*     */           
/*     */           Chunk chunk = (Chunk)wrapper.read((Type)chunkType1_20_2);
/*     */           
/*     */           Chunk1_18Type chunk1_18Type = new Chunk1_18Type(tracker.currentWorldSectionHeight(), MathUtil.ceilLog2(((Protocol1_20To1_20_2)this.protocol).getMappingData().getBlockStateMappings().mappedSize()), MathUtil.ceilLog2(tracker.biomesSent()));
/*     */           
/*     */           wrapper.write((Type)chunk1_18Type, chunk);
/*     */           
/*     */           for (ChunkSection section : chunk.getSections()) {
/*     */             DataPalette blockPalette = section.palette(PaletteType.BLOCKS);
/*     */             
/*     */             for (int i = 0; i < blockPalette.size(); i++) {
/*     */               int id = blockPalette.idByIndex(i);
/*     */               
/*     */               blockPalette.setIdByIndex(i, ((Protocol1_20To1_20_2)this.protocol).getMappingData().getNewBlockStateId(id));
/*     */             } 
/*     */           } 
/*     */           
/*     */           for (BlockEntity blockEntity : chunk.blockEntities()) {
/*     */             handleBlockEntity(blockEntity.tag());
/*     */           }
/*     */         });
/* 114 */     ((Protocol1_20To1_20_2)this.protocol).registerServerbound((ServerboundPacketType)ServerboundPackets1_19_4.SET_BEACON_EFFECT, wrapper -> {
/*     */           if (((Boolean)wrapper.passthrough((Type)Type.BOOLEAN)).booleanValue()) {
/*     */             wrapper.write((Type)Type.VAR_INT, Integer.valueOf(((Integer)wrapper.read((Type)Type.VAR_INT)).intValue() - 1));
/*     */           }
/*     */ 
/*     */           
/*     */           if (((Boolean)wrapper.passthrough((Type)Type.BOOLEAN)).booleanValue()) {
/*     */             wrapper.write((Type)Type.VAR_INT, Integer.valueOf(((Integer)wrapper.read((Type)Type.VAR_INT)).intValue() - 1));
/*     */           }
/*     */         });
/*     */     
/* 125 */     ((Protocol1_20To1_20_2)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_20_2.WINDOW_ITEMS, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 128 */             map((Type)Type.UNSIGNED_BYTE);
/* 129 */             map((Type)Type.VAR_INT);
/* 130 */             handler(wrapper -> {
/*     */                   Item[] items = (Item[])wrapper.read(Type.ITEM1_20_2_VAR_INT_ARRAY);
/*     */                   
/*     */                   for (Item item : items) {
/*     */                     BlockItemPacketRewriter1_20_2.this.handleItemToClient(item);
/*     */                   }
/*     */                   wrapper.write(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT, items);
/*     */                   wrapper.write(Type.FLAT_VAR_INT_ITEM, BlockItemPacketRewriter1_20_2.this.handleItemToClient((Item)wrapper.read(Type.ITEM1_20_2)));
/*     */                 });
/*     */           }
/*     */         });
/* 141 */     ((Protocol1_20To1_20_2)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_20_2.SET_SLOT, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 144 */             map((Type)Type.UNSIGNED_BYTE);
/* 145 */             map((Type)Type.VAR_INT);
/* 146 */             map((Type)Type.SHORT);
/* 147 */             handler(wrapper -> wrapper.write(Type.FLAT_VAR_INT_ITEM, BlockItemPacketRewriter1_20_2.this.handleItemToClient((Item)wrapper.read(Type.ITEM1_20_2))));
/*     */           }
/*     */         });
/* 150 */     ((Protocol1_20To1_20_2)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_20_2.ADVANCEMENTS, wrapper -> {
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
/*     */               wrapper.write(Type.FLAT_VAR_INT_ITEM, handleItemToClient((Item)wrapper.read(Type.ITEM1_20_2)));
/*     */               
/*     */               wrapper.passthrough((Type)Type.VAR_INT);
/*     */               int flags = ((Integer)wrapper.passthrough((Type)Type.INT)).intValue();
/*     */               if ((flags & 0x1) != 0) {
/*     */                 wrapper.passthrough(Type.STRING);
/*     */               }
/*     */               wrapper.passthrough((Type)Type.FLOAT);
/*     */               wrapper.passthrough((Type)Type.FLOAT);
/*     */             } 
/*     */             wrapper.write(Type.STRING_ARRAY, new String[0]);
/*     */             int requirements = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */             for (int array = 0; array < requirements; array++) {
/*     */               wrapper.passthrough(Type.STRING_ARRAY);
/*     */             }
/*     */             wrapper.passthrough((Type)Type.BOOLEAN);
/*     */           } 
/*     */         });
/* 185 */     ((Protocol1_20To1_20_2)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_20_2.ENTITY_EQUIPMENT, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 188 */             map((Type)Type.VAR_INT);
/* 189 */             handler(wrapper -> {
/*     */                   byte slot;
/*     */                   do {
/*     */                     slot = ((Byte)wrapper.passthrough((Type)Type.BYTE)).byteValue();
/*     */                     wrapper.write(Type.FLAT_VAR_INT_ITEM, BlockItemPacketRewriter1_20_2.this.handleItemToClient((Item)wrapper.read(Type.ITEM1_20_2)));
/*     */                   } while ((slot & Byte.MIN_VALUE) != 0);
/*     */                 });
/*     */           }
/*     */         });
/* 198 */     ((Protocol1_20To1_20_2)this.protocol).registerServerbound((ServerboundPacketType)ServerboundPackets1_19_4.CLICK_WINDOW, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 201 */             map((Type)Type.UNSIGNED_BYTE);
/* 202 */             map((Type)Type.VAR_INT);
/* 203 */             map((Type)Type.SHORT);
/* 204 */             map((Type)Type.BYTE);
/* 205 */             map((Type)Type.VAR_INT);
/*     */             
/* 207 */             handler(wrapper -> {
/*     */                   int length = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */                   
/*     */                   for (int i = 0; i < length; i++) {
/*     */                     wrapper.passthrough((Type)Type.SHORT);
/*     */                     
/*     */                     wrapper.write(Type.ITEM1_20_2, BlockItemPacketRewriter1_20_2.this.handleItemToServer((Item)wrapper.read(Type.FLAT_VAR_INT_ITEM)));
/*     */                   } 
/*     */                   
/*     */                   wrapper.write(Type.ITEM1_20_2, BlockItemPacketRewriter1_20_2.this.handleItemToServer((Item)wrapper.read(Type.FLAT_VAR_INT_ITEM)));
/*     */                 });
/*     */           }
/*     */         });
/* 220 */     ((Protocol1_20To1_20_2)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_20_2.TRADE_LIST, wrapper -> {
/*     */           wrapper.passthrough((Type)Type.VAR_INT);
/*     */           
/*     */           int size = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */           for (int i = 0; i < size; i++) {
/*     */             wrapper.write(Type.FLAT_VAR_INT_ITEM, handleItemToClient((Item)wrapper.read(Type.ITEM1_20_2)));
/*     */             wrapper.write(Type.FLAT_VAR_INT_ITEM, handleItemToClient((Item)wrapper.read(Type.ITEM1_20_2)));
/*     */             wrapper.write(Type.FLAT_VAR_INT_ITEM, handleItemToClient((Item)wrapper.read(Type.ITEM1_20_2)));
/*     */             wrapper.passthrough((Type)Type.BOOLEAN);
/*     */             wrapper.passthrough((Type)Type.INT);
/*     */             wrapper.passthrough((Type)Type.INT);
/*     */             wrapper.passthrough((Type)Type.INT);
/*     */             wrapper.passthrough((Type)Type.INT);
/*     */             wrapper.passthrough((Type)Type.FLOAT);
/*     */             wrapper.passthrough((Type)Type.INT);
/*     */           } 
/*     */         });
/* 237 */     ((Protocol1_20To1_20_2)this.protocol).registerServerbound((ServerboundPacketType)ServerboundPackets1_19_4.CREATIVE_INVENTORY_ACTION, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 240 */             map((Type)Type.SHORT);
/* 241 */             handler(wrapper -> wrapper.write(Type.ITEM1_20_2, BlockItemPacketRewriter1_20_2.this.handleItemToServer((Item)wrapper.read(Type.FLAT_VAR_INT_ITEM))));
/*     */           }
/*     */         });
/* 244 */     ((Protocol1_20To1_20_2)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_20_2.SPAWN_PARTICLE, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 247 */             map((Type)Type.VAR_INT);
/* 248 */             map((Type)Type.BOOLEAN);
/* 249 */             map((Type)Type.DOUBLE);
/* 250 */             map((Type)Type.DOUBLE);
/* 251 */             map((Type)Type.DOUBLE);
/* 252 */             map((Type)Type.FLOAT);
/* 253 */             map((Type)Type.FLOAT);
/* 254 */             map((Type)Type.FLOAT);
/* 255 */             map((Type)Type.FLOAT);
/* 256 */             map((Type)Type.INT);
/* 257 */             handler(wrapper -> {
/*     */                   int id = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
/*     */                   
/*     */                   ParticleMappings mappings = Protocol1_20To1_20_2.MAPPINGS.getParticleMappings();
/*     */                   if (mappings.isBlockParticle(id)) {
/*     */                     int data = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
/*     */                     wrapper.write((Type)Type.VAR_INT, Integer.valueOf(((Protocol1_20To1_20_2)BlockItemPacketRewriter1_20_2.this.protocol).getMappingData().getNewBlockStateId(data)));
/*     */                   } else if (mappings.isItemParticle(id)) {
/*     */                     wrapper.write(Type.FLAT_VAR_INT_ITEM, BlockItemPacketRewriter1_20_2.this.handleItemToClient((Item)wrapper.read(Type.ITEM1_20_2)));
/*     */                   } 
/*     */                 });
/*     */           }
/*     */         });
/* 270 */     (new RecipeRewriter1_20_2<ClientboundPackets1_20_2>(this.protocol)
/*     */       {
/*     */         public void handleCraftingShapeless(PacketWrapper wrapper) throws Exception {
/* 273 */           wrapper.passthrough(Type.STRING);
/* 274 */           wrapper.passthrough((Type)Type.VAR_INT);
/* 275 */           handleIngredients(wrapper);
/*     */           
/* 277 */           Item result = (Item)wrapper.read(itemType());
/* 278 */           rewrite(result);
/* 279 */           wrapper.write(Type.FLAT_VAR_INT_ITEM, result);
/*     */         }
/*     */ 
/*     */         
/*     */         public void handleSmelting(PacketWrapper wrapper) throws Exception {
/* 284 */           wrapper.passthrough(Type.STRING);
/* 285 */           wrapper.passthrough((Type)Type.VAR_INT);
/* 286 */           handleIngredient(wrapper);
/*     */           
/* 288 */           Item result = (Item)wrapper.read(itemType());
/* 289 */           rewrite(result);
/* 290 */           wrapper.write(Type.FLAT_VAR_INT_ITEM, result);
/*     */           
/* 292 */           wrapper.passthrough((Type)Type.FLOAT);
/* 293 */           wrapper.passthrough((Type)Type.VAR_INT);
/*     */         }
/*     */ 
/*     */         
/*     */         public void handleCraftingShaped(PacketWrapper wrapper) throws Exception {
/* 298 */           int ingredients = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue() * ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/* 299 */           wrapper.passthrough(Type.STRING);
/* 300 */           wrapper.passthrough((Type)Type.VAR_INT);
/* 301 */           for (int i = 0; i < ingredients; i++) {
/* 302 */             handleIngredient(wrapper);
/*     */           }
/*     */           
/* 305 */           Item result = (Item)wrapper.read(itemType());
/* 306 */           rewrite(result);
/* 307 */           wrapper.write(Type.FLAT_VAR_INT_ITEM, result);
/*     */           
/* 309 */           wrapper.passthrough((Type)Type.BOOLEAN);
/*     */         }
/*     */ 
/*     */         
/*     */         public void handleStonecutting(PacketWrapper wrapper) throws Exception {
/* 314 */           wrapper.passthrough(Type.STRING);
/* 315 */           handleIngredient(wrapper);
/*     */           
/* 317 */           Item result = (Item)wrapper.read(itemType());
/* 318 */           rewrite(result);
/* 319 */           wrapper.write(Type.FLAT_VAR_INT_ITEM, result);
/*     */         }
/*     */ 
/*     */         
/*     */         public void handleSmithing(PacketWrapper wrapper) throws Exception {
/* 324 */           handleIngredient(wrapper);
/* 325 */           handleIngredient(wrapper);
/*     */           
/* 327 */           Item result = (Item)wrapper.read(itemType());
/* 328 */           rewrite(result);
/* 329 */           wrapper.write(Type.FLAT_VAR_INT_ITEM, result);
/*     */         }
/*     */ 
/*     */         
/*     */         public void handleSmithingTransform(PacketWrapper wrapper) throws Exception {
/* 334 */           handleIngredient(wrapper);
/* 335 */           handleIngredient(wrapper);
/* 336 */           handleIngredient(wrapper);
/*     */           
/* 338 */           Item result = (Item)wrapper.read(itemType());
/* 339 */           rewrite(result);
/* 340 */           wrapper.write(Type.FLAT_VAR_INT_ITEM, result);
/*     */         }
/*     */ 
/*     */         
/*     */         protected void handleIngredient(PacketWrapper wrapper) throws Exception {
/* 345 */           Item[] items = (Item[])wrapper.read(itemArrayType());
/* 346 */           wrapper.write(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT, items);
/* 347 */           for (Item item : items) {
/* 348 */             rewrite(item);
/*     */           }
/*     */         }
/* 351 */       }).register((ClientboundPacketType)ClientboundPackets1_20_2.DECLARE_RECIPES);
/*     */   }
/*     */ 
/*     */   
/*     */   public Item handleItemToClient(Item item) {
/* 356 */     if (item == null) {
/* 357 */       return null;
/*     */     }
/* 359 */     if (item.tag() != null) {
/* 360 */       com.viaversion.viaversion.protocols.protocol1_20_2to1_20.rewriter.BlockItemPacketRewriter1_20_2.to1_20_1Effects(item);
/*     */     }
/*     */     
/* 363 */     return super.handleItemToClient(item);
/*     */   }
/*     */ 
/*     */   
/*     */   public Item handleItemToServer(Item item) {
/* 368 */     if (item == null) {
/* 369 */       return null;
/*     */     }
/* 371 */     if (item.tag() != null) {
/* 372 */       com.viaversion.viaversion.protocols.protocol1_20_2to1_20.rewriter.BlockItemPacketRewriter1_20_2.to1_20_2Effects(item);
/*     */     }
/*     */     
/* 375 */     return super.handleItemToServer(item);
/*     */   }
/*     */   
/*     */   private CompoundTag handleBlockEntity(CompoundTag tag) {
/* 379 */     if (tag == null) {
/* 380 */       return null;
/*     */     }
/*     */     
/* 383 */     StringTag primaryEffect = (StringTag)tag.remove("primary_effect");
/* 384 */     if (primaryEffect != null) {
/* 385 */       String effectKey = Key.stripMinecraftNamespace(primaryEffect.getValue());
/* 386 */       tag.put("Primary", (Tag)new IntTag(PotionEffects.keyToId(effectKey)));
/*     */     } 
/*     */     
/* 389 */     StringTag secondaryEffect = (StringTag)tag.remove("secondary_effect");
/* 390 */     if (secondaryEffect != null) {
/* 391 */       String effectKey = Key.stripMinecraftNamespace(secondaryEffect.getValue());
/* 392 */       tag.put("Secondary", (Tag)new IntTag(PotionEffects.keyToId(effectKey)));
/*     */     } 
/* 394 */     return tag;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viabackwards\protocol\protocol1_20to1_20_2\rewriter\BlockItemPacketRewriter1_20_2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */