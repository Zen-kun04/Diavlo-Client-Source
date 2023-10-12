/*     */ package com.viaversion.viabackwards.protocol.protocol1_10to1_11.packets;
/*     */ 
/*     */ import com.viaversion.viabackwards.api.BackwardsProtocol;
/*     */ import com.viaversion.viabackwards.api.data.MappedLegacyBlockItem;
/*     */ import com.viaversion.viabackwards.api.rewriters.LegacyBlockItemRewriter;
/*     */ import com.viaversion.viabackwards.api.rewriters.LegacyEnchantmentRewriter;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_10to1_11.Protocol1_10To1_11;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_10to1_11.storage.ChestedHorseStorage;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_10to1_11.storage.WindowTracker;
/*     */ import com.viaversion.viaversion.api.connection.UserConnection;
/*     */ import com.viaversion.viaversion.api.data.entity.EntityTracker;
/*     */ import com.viaversion.viaversion.api.data.entity.StoredEntityData;
/*     */ import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
/*     */ import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
/*     */ import com.viaversion.viaversion.api.minecraft.entities.Entity1_11Types;
/*     */ import com.viaversion.viaversion.api.minecraft.entities.EntityType;
/*     */ import com.viaversion.viaversion.api.minecraft.item.DataItem;
/*     */ import com.viaversion.viaversion.api.minecraft.item.Item;
/*     */ import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
/*     */ import com.viaversion.viaversion.protocols.protocol1_11to1_10.EntityIdRewriter;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
/*     */ import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.types.Chunk1_9_3_4Type;
/*     */ import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
/*     */ import java.util.Arrays;
/*     */ import java.util.Optional;
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
/*     */ public class BlockItemPackets1_11
/*     */   extends LegacyBlockItemRewriter<ClientboundPackets1_9_3, ServerboundPackets1_9_3, Protocol1_10To1_11>
/*     */ {
/*     */   private LegacyEnchantmentRewriter enchantmentRewriter;
/*     */   
/*     */   public BlockItemPackets1_11(Protocol1_10To1_11 protocol) {
/*  57 */     super((BackwardsProtocol)protocol);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerPackets() {
/*  62 */     ((Protocol1_10To1_11)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_9_3.SET_SLOT, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/*  65 */             map((Type)Type.UNSIGNED_BYTE);
/*  66 */             map((Type)Type.SHORT);
/*  67 */             map(Type.ITEM);
/*     */             
/*  69 */             handler(BlockItemPackets1_11.this.itemToClientHandler(Type.ITEM));
/*     */ 
/*     */             
/*  72 */             handler(new PacketHandler()
/*     */                 {
/*     */                   public void handle(PacketWrapper wrapper) throws Exception {
/*  75 */                     if (BlockItemPackets1_11.this.isLlama(wrapper.user())) {
/*  76 */                       Optional<ChestedHorseStorage> horse = BlockItemPackets1_11.this.getChestedHorse(wrapper.user());
/*  77 */                       if (!horse.isPresent())
/*     */                         return; 
/*  79 */                       ChestedHorseStorage storage = horse.get();
/*  80 */                       int currentSlot = ((Short)wrapper.get((Type)Type.SHORT, 0)).shortValue();
/*  81 */                       wrapper.set((Type)Type.SHORT, 0, Short.valueOf(Integer.valueOf(currentSlot = BlockItemPackets1_11.this.getNewSlotId(storage, currentSlot)).shortValue()));
/*  82 */                       wrapper.set(Type.ITEM, 0, BlockItemPackets1_11.this.getNewItem(storage, currentSlot, (Item)wrapper.get(Type.ITEM, 0)));
/*     */                     } 
/*     */                   }
/*     */                 });
/*     */           }
/*     */         });
/*     */     
/*  89 */     ((Protocol1_10To1_11)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_9_3.WINDOW_ITEMS, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/*  92 */             map((Type)Type.UNSIGNED_BYTE);
/*  93 */             map(Type.ITEM_ARRAY);
/*     */             
/*  95 */             handler(wrapper -> {
/*     */                   Item[] stacks = (Item[])wrapper.get(Type.ITEM_ARRAY, 0);
/*     */                   
/*     */                   for (int i = 0; i < stacks.length; i++) {
/*     */                     stacks[i] = BlockItemPackets1_11.this.handleItemToClient(stacks[i]);
/*     */                   }
/*     */                   if (BlockItemPackets1_11.this.isLlama(wrapper.user())) {
/*     */                     Optional<ChestedHorseStorage> horse = BlockItemPackets1_11.this.getChestedHorse(wrapper.user());
/*     */                     if (!horse.isPresent()) {
/*     */                       return;
/*     */                     }
/*     */                     ChestedHorseStorage storage = horse.get();
/*     */                     stacks = Arrays.<Item>copyOf(stacks, !storage.isChested() ? 38 : 53);
/*     */                     for (int j = stacks.length - 1; j >= 0; j--) {
/*     */                       stacks[BlockItemPackets1_11.this.getNewSlotId(storage, j)] = stacks[j];
/*     */                       stacks[j] = BlockItemPackets1_11.this.getNewItem(storage, j, stacks[j]);
/*     */                     } 
/*     */                     wrapper.set(Type.ITEM_ARRAY, 0, stacks);
/*     */                   } 
/*     */                 });
/*     */           }
/*     */         });
/* 117 */     registerEntityEquipment((ClientboundPacketType)ClientboundPackets1_9_3.ENTITY_EQUIPMENT, Type.ITEM);
/*     */ 
/*     */     
/* 120 */     ((Protocol1_10To1_11)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_9_3.PLUGIN_MESSAGE, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 123 */             map(Type.STRING);
/*     */             
/* 125 */             handler(wrapper -> {
/*     */                   if (((String)wrapper.get(Type.STRING, 0)).equalsIgnoreCase("MC|TrList")) {
/*     */                     wrapper.passthrough((Type)Type.INT);
/*     */                     
/*     */                     int size = ((Short)wrapper.passthrough((Type)Type.UNSIGNED_BYTE)).shortValue();
/*     */                     
/*     */                     for (int i = 0; i < size; i++) {
/*     */                       wrapper.write(Type.ITEM, BlockItemPackets1_11.this.handleItemToClient((Item)wrapper.read(Type.ITEM)));
/*     */                       
/*     */                       wrapper.write(Type.ITEM, BlockItemPackets1_11.this.handleItemToClient((Item)wrapper.read(Type.ITEM)));
/*     */                       
/*     */                       boolean secondItem = ((Boolean)wrapper.passthrough((Type)Type.BOOLEAN)).booleanValue();
/*     */                       if (secondItem) {
/*     */                         wrapper.write(Type.ITEM, BlockItemPackets1_11.this.handleItemToClient((Item)wrapper.read(Type.ITEM)));
/*     */                       }
/*     */                       wrapper.passthrough((Type)Type.BOOLEAN);
/*     */                       wrapper.passthrough((Type)Type.INT);
/*     */                       wrapper.passthrough((Type)Type.INT);
/*     */                     } 
/*     */                   } 
/*     */                 });
/*     */           }
/*     */         });
/* 148 */     ((Protocol1_10To1_11)this.protocol).registerServerbound((ServerboundPacketType)ServerboundPackets1_9_3.CLICK_WINDOW, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 151 */             map((Type)Type.UNSIGNED_BYTE);
/* 152 */             map((Type)Type.SHORT);
/* 153 */             map((Type)Type.BYTE);
/* 154 */             map((Type)Type.SHORT);
/* 155 */             map((Type)Type.VAR_INT);
/* 156 */             map(Type.ITEM);
/*     */             
/* 158 */             handler(BlockItemPackets1_11.this.itemToServerHandler(Type.ITEM));
/*     */ 
/*     */             
/* 161 */             handler(wrapper -> {
/*     */                   if (BlockItemPackets1_11.this.isLlama(wrapper.user())) {
/*     */                     Optional<ChestedHorseStorage> horse = BlockItemPackets1_11.this.getChestedHorse(wrapper.user());
/*     */                     
/*     */                     if (!horse.isPresent()) {
/*     */                       return;
/*     */                     }
/*     */                     ChestedHorseStorage storage = horse.get();
/*     */                     int clickSlot = ((Short)wrapper.get((Type)Type.SHORT, 0)).shortValue();
/*     */                     int correctSlot = BlockItemPackets1_11.this.getOldSlotId(storage, clickSlot);
/*     */                     wrapper.set((Type)Type.SHORT, 0, Short.valueOf(Integer.valueOf(correctSlot).shortValue()));
/*     */                   } 
/*     */                 });
/*     */           }
/*     */         });
/* 176 */     registerCreativeInvAction((ServerboundPacketType)ServerboundPackets1_9_3.CREATIVE_INVENTORY_ACTION, Type.ITEM);
/*     */     
/* 178 */     ((Protocol1_10To1_11)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_9_3.CHUNK_DATA, wrapper -> {
/*     */           ClientWorld clientWorld = (ClientWorld)wrapper.user().get(ClientWorld.class);
/*     */           
/*     */           Chunk1_9_3_4Type type = new Chunk1_9_3_4Type(clientWorld);
/*     */           
/*     */           Chunk chunk = (Chunk)wrapper.passthrough((Type)type);
/*     */           
/*     */           handleChunk(chunk);
/*     */           
/*     */           for (CompoundTag tag : chunk.getBlockEntities()) {
/*     */             Tag idTag = tag.get("id");
/*     */             if (!(idTag instanceof StringTag)) {
/*     */               continue;
/*     */             }
/*     */             String id = (String)idTag.getValue();
/*     */             if (id.equals("minecraft:sign")) {
/*     */               ((StringTag)idTag).setValue("Sign");
/*     */             }
/*     */           } 
/*     */         });
/* 198 */     ((Protocol1_10To1_11)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_9_3.BLOCK_CHANGE, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 201 */             map(Type.POSITION);
/* 202 */             map((Type)Type.VAR_INT);
/*     */             
/* 204 */             handler(wrapper -> {
/*     */                   int idx = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
/*     */                   
/*     */                   wrapper.set((Type)Type.VAR_INT, 0, Integer.valueOf(BlockItemPackets1_11.this.handleBlockID(idx)));
/*     */                 });
/*     */           }
/*     */         });
/* 211 */     ((Protocol1_10To1_11)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_9_3.MULTI_BLOCK_CHANGE, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 214 */             map((Type)Type.INT);
/* 215 */             map((Type)Type.INT);
/* 216 */             map(Type.BLOCK_CHANGE_RECORD_ARRAY);
/*     */             
/* 218 */             handler(wrapper -> {
/*     */                   for (BlockChangeRecord record : (BlockChangeRecord[])wrapper.get(Type.BLOCK_CHANGE_RECORD_ARRAY, 0)) {
/*     */                     record.setBlockId(BlockItemPackets1_11.this.handleBlockID(record.getBlockId()));
/*     */                   }
/*     */                 });
/*     */           }
/*     */         });
/*     */     
/* 226 */     ((Protocol1_10To1_11)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_9_3.BLOCK_ENTITY_DATA, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 229 */             map(Type.POSITION);
/* 230 */             map((Type)Type.UNSIGNED_BYTE);
/* 231 */             map(Type.NBT);
/*     */             
/* 233 */             handler(wrapper -> {
/*     */                   if (((Short)wrapper.get((Type)Type.UNSIGNED_BYTE, 0)).shortValue() == 10) {
/*     */                     wrapper.cancel();
/*     */                   }
/*     */                   
/*     */                   if (((Short)wrapper.get((Type)Type.UNSIGNED_BYTE, 0)).shortValue() == 1) {
/*     */                     CompoundTag tag = (CompoundTag)wrapper.get(Type.NBT, 0);
/*     */                     
/*     */                     EntityIdRewriter.toClientSpawner(tag, true);
/*     */                   } 
/*     */                 });
/*     */           }
/*     */         });
/*     */     
/* 247 */     ((Protocol1_10To1_11)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_9_3.OPEN_WINDOW, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 250 */             map((Type)Type.UNSIGNED_BYTE);
/* 251 */             map(Type.STRING);
/* 252 */             map(Type.COMPONENT);
/* 253 */             map((Type)Type.UNSIGNED_BYTE);
/*     */             
/* 255 */             handler(wrapper -> {
/*     */                   int entityId = -1;
/*     */                   
/*     */                   if (((String)wrapper.get(Type.STRING, 0)).equals("EntityHorse")) {
/*     */                     entityId = ((Integer)wrapper.passthrough((Type)Type.INT)).intValue();
/*     */                   }
/*     */                   
/*     */                   String inventory = (String)wrapper.get(Type.STRING, 0);
/*     */                   
/*     */                   WindowTracker windowTracker = (WindowTracker)wrapper.user().get(WindowTracker.class);
/*     */                   
/*     */                   windowTracker.setInventory(inventory);
/*     */                   
/*     */                   windowTracker.setEntityId(entityId);
/*     */                   
/*     */                   if (BlockItemPackets1_11.this.isLlama(wrapper.user())) {
/*     */                     wrapper.set((Type)Type.UNSIGNED_BYTE, 1, Short.valueOf((short)17));
/*     */                   }
/*     */                 });
/*     */           }
/*     */         });
/* 276 */     ((Protocol1_10To1_11)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_9_3.CLOSE_WINDOW, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register()
/*     */           {
/* 280 */             handler(wrapper -> {
/*     */                   WindowTracker windowTracker = (WindowTracker)wrapper.user().get(WindowTracker.class);
/*     */                   
/*     */                   windowTracker.setInventory(null);
/*     */                   
/*     */                   windowTracker.setEntityId(-1);
/*     */                 });
/*     */           }
/*     */         });
/* 289 */     ((Protocol1_10To1_11)this.protocol).registerServerbound((ServerboundPacketType)ServerboundPackets1_9_3.CLOSE_WINDOW, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register()
/*     */           {
/* 293 */             handler(wrapper -> {
/*     */                   WindowTracker windowTracker = (WindowTracker)wrapper.user().get(WindowTracker.class);
/*     */                   
/*     */                   windowTracker.setInventory(null);
/*     */                   windowTracker.setEntityId(-1);
/*     */                 });
/*     */           }
/*     */         });
/* 301 */     ((Protocol1_10To1_11)this.protocol).getEntityRewriter().filter().handler((event, meta) -> {
/*     */           if (meta.metaType().type().equals(Type.ITEM)) {
/*     */             meta.setValue(handleItemToClient((Item)meta.getValue()));
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerRewrites() {
/* 310 */     MappedLegacyBlockItem data = (MappedLegacyBlockItem)this.replacementData.computeIfAbsent(52, s -> new MappedLegacyBlockItem(52, (short)-1, null, false));
/* 311 */     data.setBlockEntityHandler((b, tag) -> {
/*     */           EntityIdRewriter.toClientSpawner(tag, true);
/*     */           
/*     */           return tag;
/*     */         });
/* 316 */     this.enchantmentRewriter = new LegacyEnchantmentRewriter(this.nbtTagName);
/* 317 */     this.enchantmentRewriter.registerEnchantment(71, "§cCurse of Vanishing");
/* 318 */     this.enchantmentRewriter.registerEnchantment(10, "§cCurse of Binding");
/*     */     
/* 320 */     this.enchantmentRewriter.setHideLevelForEnchants(new int[] { 71, 10 });
/*     */   }
/*     */ 
/*     */   
/*     */   public Item handleItemToClient(Item item) {
/* 325 */     if (item == null) return null; 
/* 326 */     super.handleItemToClient(item);
/*     */     
/* 328 */     CompoundTag tag = item.tag();
/* 329 */     if (tag == null) return item;
/*     */ 
/*     */     
/* 332 */     EntityIdRewriter.toClientItem(item, true);
/*     */     
/* 334 */     if (tag.get("ench") instanceof com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag) {
/* 335 */       this.enchantmentRewriter.rewriteEnchantmentsToClient(tag, false);
/*     */     }
/* 337 */     if (tag.get("StoredEnchantments") instanceof com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag) {
/* 338 */       this.enchantmentRewriter.rewriteEnchantmentsToClient(tag, true);
/*     */     }
/* 340 */     return item;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item handleItemToServer(Item item) {
/* 345 */     if (item == null) return null; 
/* 346 */     super.handleItemToServer(item);
/*     */     
/* 348 */     CompoundTag tag = item.tag();
/* 349 */     if (tag == null) return item;
/*     */ 
/*     */     
/* 352 */     EntityIdRewriter.toServerItem(item, true);
/*     */     
/* 354 */     if (tag.contains(this.nbtTagName + "|ench")) {
/* 355 */       this.enchantmentRewriter.rewriteEnchantmentsToServer(tag, false);
/*     */     }
/* 357 */     if (tag.contains(this.nbtTagName + "|StoredEnchantments")) {
/* 358 */       this.enchantmentRewriter.rewriteEnchantmentsToServer(tag, true);
/*     */     }
/* 360 */     return item;
/*     */   }
/*     */   
/*     */   private boolean isLlama(UserConnection user) {
/* 364 */     WindowTracker tracker = (WindowTracker)user.get(WindowTracker.class);
/* 365 */     if (tracker.getInventory() != null && tracker.getInventory().equals("EntityHorse")) {
/* 366 */       EntityTracker entTracker = user.getEntityTracker(Protocol1_10To1_11.class);
/* 367 */       StoredEntityData entityData = entTracker.entityData(tracker.getEntityId());
/* 368 */       return (entityData != null && entityData.type().is((EntityType)Entity1_11Types.EntityType.LIAMA));
/*     */     } 
/* 370 */     return false;
/*     */   }
/*     */   
/*     */   private Optional<ChestedHorseStorage> getChestedHorse(UserConnection user) {
/* 374 */     WindowTracker tracker = (WindowTracker)user.get(WindowTracker.class);
/* 375 */     if (tracker.getInventory() != null && tracker.getInventory().equals("EntityHorse")) {
/* 376 */       EntityTracker entTracker = user.getEntityTracker(Protocol1_10To1_11.class);
/* 377 */       StoredEntityData entityData = entTracker.entityData(tracker.getEntityId());
/* 378 */       if (entityData != null)
/* 379 */         return Optional.of((ChestedHorseStorage)entityData.get(ChestedHorseStorage.class)); 
/*     */     } 
/* 381 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   private int getNewSlotId(ChestedHorseStorage storage, int slotId) {
/* 385 */     int totalSlots = !storage.isChested() ? 38 : 53;
/* 386 */     int strength = storage.isChested() ? storage.getLiamaStrength() : 0;
/* 387 */     int startNonExistingFormula = 2 + 3 * strength;
/* 388 */     int offsetForm = 15 - 3 * strength;
/*     */     
/* 390 */     if (slotId >= startNonExistingFormula && totalSlots > slotId + offsetForm)
/* 391 */       return offsetForm + slotId; 
/* 392 */     if (slotId == 1)
/* 393 */       return 0; 
/* 394 */     return slotId;
/*     */   }
/*     */   
/*     */   private int getOldSlotId(ChestedHorseStorage storage, int slotId) {
/* 398 */     int strength = storage.isChested() ? storage.getLiamaStrength() : 0;
/* 399 */     int startNonExistingFormula = 2 + 3 * strength;
/* 400 */     int endNonExistingFormula = 2 + 3 * (storage.isChested() ? 5 : 0);
/* 401 */     int offsetForm = endNonExistingFormula - startNonExistingFormula;
/*     */     
/* 403 */     if (slotId == 1 || (slotId >= startNonExistingFormula && slotId < endNonExistingFormula))
/* 404 */       return 0; 
/* 405 */     if (slotId >= endNonExistingFormula)
/* 406 */       return slotId - offsetForm; 
/* 407 */     if (slotId == 0)
/* 408 */       return 1; 
/* 409 */     return slotId;
/*     */   }
/*     */   
/*     */   private Item getNewItem(ChestedHorseStorage storage, int slotId, Item current) {
/* 413 */     int strength = storage.isChested() ? storage.getLiamaStrength() : 0;
/* 414 */     int startNonExistingFormula = 2 + 3 * strength;
/* 415 */     int endNonExistingFormula = 2 + 3 * (storage.isChested() ? 5 : 0);
/*     */     
/* 417 */     if (slotId >= startNonExistingFormula && slotId < endNonExistingFormula)
/* 418 */       return (Item)new DataItem(166, (byte)1, (short)0, getNamedTag("§4SLOT DISABLED")); 
/* 419 */     if (slotId == 1)
/* 420 */       return null; 
/* 421 */     return current;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viabackwards\protocol\protocol1_10to1_11\packets\BlockItemPackets1_11.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */