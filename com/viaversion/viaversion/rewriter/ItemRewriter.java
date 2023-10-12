/*     */ package com.viaversion.viaversion.rewriter;
/*     */ 
/*     */ import com.viaversion.viaversion.api.data.Mappings;
/*     */ import com.viaversion.viaversion.api.data.ParticleMappings;
/*     */ import com.viaversion.viaversion.api.minecraft.item.Item;
/*     */ import com.viaversion.viaversion.api.protocol.Protocol;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
/*     */ import com.viaversion.viaversion.api.rewriter.ItemRewriter;
/*     */ import com.viaversion.viaversion.api.rewriter.RewriterBase;
/*     */ import com.viaversion.viaversion.api.type.Type;
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
/*     */ public abstract class ItemRewriter<C extends ClientboundPacketType, S extends ServerboundPacketType, T extends Protocol<C, ?, ?, S>>
/*     */   extends RewriterBase<T>
/*     */   implements ItemRewriter<T>
/*     */ {
/*     */   private final Type<Item> itemType;
/*     */   private final Type<Item[]> itemArrayType;
/*     */   
/*     */   protected ItemRewriter(T protocol) {
/*  38 */     this(protocol, Type.FLAT_VAR_INT_ITEM, Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
/*     */   }
/*     */   
/*     */   public ItemRewriter(T protocol, Type<Item> itemType, Type<Item[]> itemArrayType) {
/*  42 */     super((Protocol)protocol);
/*  43 */     this.itemType = itemType;
/*  44 */     this.itemArrayType = itemArrayType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Item handleItemToClient(Item item) {
/*  51 */     if (item == null) return null; 
/*  52 */     if (this.protocol.getMappingData() != null && this.protocol.getMappingData().getItemMappings() != null) {
/*  53 */       item.setIdentifier(this.protocol.getMappingData().getNewItemId(item.identifier()));
/*     */     }
/*  55 */     return item;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item handleItemToServer(Item item) {
/*  60 */     if (item == null) return null; 
/*  61 */     if (this.protocol.getMappingData() != null && this.protocol.getMappingData().getItemMappings() != null) {
/*  62 */       item.setIdentifier(this.protocol.getMappingData().getOldItemId(item.identifier()));
/*     */     }
/*  64 */     return item;
/*     */   }
/*     */   
/*     */   public void registerWindowItems(C packetType, final Type<Item[]> type) {
/*  68 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/*  71 */             map((Type)Type.UNSIGNED_BYTE);
/*  72 */             map(type);
/*  73 */             handler(ItemRewriter.this.itemArrayToClientHandler(type));
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void registerWindowItems1_17_1(C packetType) {
/*  79 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/*  82 */             map((Type)Type.UNSIGNED_BYTE);
/*  83 */             map((Type)Type.VAR_INT);
/*  84 */             handler(wrapper -> {
/*     */                   Item[] items = (Item[])wrapper.passthrough(ItemRewriter.this.itemArrayType);
/*     */                   for (Item item : items) {
/*     */                     ItemRewriter.this.handleItemToClient(item);
/*     */                   }
/*     */                   ItemRewriter.this.handleItemToClient((Item)wrapper.passthrough(ItemRewriter.this.itemType));
/*     */                 });
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerOpenWindow(C packetType) {
/*  97 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 100 */             map((Type)Type.VAR_INT);
/* 101 */             handler(wrapper -> {
/*     */                   int windowType = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
/*     */                   int mappedId = ItemRewriter.this.protocol.getMappingData().getMenuMappings().getNewId(windowType);
/*     */                   if (mappedId == -1) {
/*     */                     wrapper.cancel();
/*     */                     return;
/*     */                   } 
/*     */                   wrapper.write((Type)Type.VAR_INT, Integer.valueOf(mappedId));
/*     */                 });
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerSetSlot(C packetType, final Type<Item> type) {
/* 116 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 119 */             map((Type)Type.UNSIGNED_BYTE);
/* 120 */             map((Type)Type.SHORT);
/* 121 */             map(type);
/* 122 */             handler(ItemRewriter.this.itemToClientHandler(type));
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void registerSetSlot1_17_1(C packetType) {
/* 128 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 131 */             map((Type)Type.UNSIGNED_BYTE);
/* 132 */             map((Type)Type.VAR_INT);
/* 133 */             map((Type)Type.SHORT);
/* 134 */             map(ItemRewriter.this.itemType);
/* 135 */             handler(ItemRewriter.this.itemToClientHandler(ItemRewriter.this.itemType));
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerEntityEquipment(C packetType, final Type<Item> type) {
/* 142 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 145 */             map((Type)Type.VAR_INT);
/* 146 */             map((Type)Type.VAR_INT);
/* 147 */             map(type);
/*     */             
/* 149 */             handler(ItemRewriter.this.itemToClientHandler(type));
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerEntityEquipmentArray(C packetType) {
/* 156 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 159 */             map((Type)Type.VAR_INT);
/*     */             
/* 161 */             handler(wrapper -> {
/*     */                   byte slot;
/*     */                   do {
/*     */                     slot = ((Byte)wrapper.passthrough((Type)Type.BYTE)).byteValue();
/*     */                     ItemRewriter.this.handleItemToClient((Item)wrapper.passthrough(ItemRewriter.this.itemType));
/*     */                   } while ((slot & Byte.MIN_VALUE) != 0);
/*     */                 });
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerCreativeInvAction(S packetType, final Type<Item> type) {
/* 174 */     this.protocol.registerServerbound((ServerboundPacketType)packetType, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 177 */             map((Type)Type.SHORT);
/* 178 */             map(type);
/* 179 */             handler(ItemRewriter.this.itemToServerHandler(type));
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void registerClickWindow(S packetType, final Type<Item> type) {
/* 185 */     this.protocol.registerServerbound((ServerboundPacketType)packetType, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 188 */             map((Type)Type.UNSIGNED_BYTE);
/* 189 */             map((Type)Type.SHORT);
/* 190 */             map((Type)Type.BYTE);
/* 191 */             map((Type)Type.SHORT);
/* 192 */             map((Type)Type.VAR_INT);
/* 193 */             map(type);
/*     */             
/* 195 */             handler(ItemRewriter.this.itemToServerHandler(type));
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void registerClickWindow1_17_1(S packetType) {
/* 201 */     this.protocol.registerServerbound((ServerboundPacketType)packetType, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 204 */             map((Type)Type.UNSIGNED_BYTE);
/* 205 */             map((Type)Type.VAR_INT);
/* 206 */             map((Type)Type.SHORT);
/* 207 */             map((Type)Type.BYTE);
/* 208 */             map((Type)Type.VAR_INT);
/*     */             
/* 210 */             handler(wrapper -> {
/*     */                   int length = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */                   for (int i = 0; i < length; i++) {
/*     */                     wrapper.passthrough((Type)Type.SHORT);
/*     */                     ItemRewriter.this.handleItemToServer((Item)wrapper.passthrough(ItemRewriter.this.itemType));
/*     */                   } 
/*     */                   ItemRewriter.this.handleItemToServer((Item)wrapper.passthrough(ItemRewriter.this.itemType));
/*     */                 });
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerSetCooldown(C packetType) {
/* 226 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, wrapper -> {
/*     */           int itemId = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
/*     */           wrapper.write((Type)Type.VAR_INT, Integer.valueOf(this.protocol.getMappingData().getNewItemId(itemId)));
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerTradeList(C packetType) {
/* 234 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, wrapper -> {
/*     */           wrapper.passthrough((Type)Type.VAR_INT);
/*     */           int size = ((Short)wrapper.passthrough((Type)Type.UNSIGNED_BYTE)).shortValue();
/*     */           for (int i = 0; i < size; i++) {
/*     */             handleItemToClient((Item)wrapper.passthrough(this.itemType));
/*     */             handleItemToClient((Item)wrapper.passthrough(this.itemType));
/*     */             if (((Boolean)wrapper.passthrough((Type)Type.BOOLEAN)).booleanValue()) {
/*     */               handleItemToClient((Item)wrapper.passthrough(this.itemType));
/*     */             }
/*     */             wrapper.passthrough((Type)Type.BOOLEAN);
/*     */             wrapper.passthrough((Type)Type.INT);
/*     */             wrapper.passthrough((Type)Type.INT);
/*     */             wrapper.passthrough((Type)Type.INT);
/*     */             wrapper.passthrough((Type)Type.INT);
/*     */             wrapper.passthrough((Type)Type.FLOAT);
/*     */             wrapper.passthrough((Type)Type.INT);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerTradeList1_19(C packetType) {
/* 259 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, wrapper -> {
/*     */           wrapper.passthrough((Type)Type.VAR_INT);
/*     */           int size = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */           for (int i = 0; i < size; i++) {
/*     */             handleItemToClient((Item)wrapper.passthrough(this.itemType));
/*     */             handleItemToClient((Item)wrapper.passthrough(this.itemType));
/*     */             handleItemToClient((Item)wrapper.passthrough(this.itemType));
/*     */             wrapper.passthrough((Type)Type.BOOLEAN);
/*     */             wrapper.passthrough((Type)Type.INT);
/*     */             wrapper.passthrough((Type)Type.INT);
/*     */             wrapper.passthrough((Type)Type.INT);
/*     */             wrapper.passthrough((Type)Type.INT);
/*     */             wrapper.passthrough((Type)Type.FLOAT);
/*     */             wrapper.passthrough((Type)Type.INT);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerAdvancements(C packetType, Type<Item> type) {
/* 280 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, wrapper -> {
/*     */           wrapper.passthrough((Type)Type.BOOLEAN);
/*     */           int size = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */           for (int i = 0; i < size; i++) {
/*     */             wrapper.passthrough(Type.STRING);
/*     */             if (((Boolean)wrapper.passthrough((Type)Type.BOOLEAN)).booleanValue()) {
/*     */               wrapper.passthrough(Type.STRING);
/*     */             }
/*     */             if (((Boolean)wrapper.passthrough((Type)Type.BOOLEAN)).booleanValue()) {
/*     */               wrapper.passthrough(Type.COMPONENT);
/*     */               wrapper.passthrough(Type.COMPONENT);
/*     */               handleItemToClient((Item)wrapper.passthrough(type));
/*     */               wrapper.passthrough((Type)Type.VAR_INT);
/*     */               int flags = ((Integer)wrapper.passthrough((Type)Type.INT)).intValue();
/*     */               if ((flags & 0x1) != 0) {
/*     */                 wrapper.passthrough(Type.STRING);
/*     */               }
/*     */               wrapper.passthrough((Type)Type.FLOAT);
/*     */               wrapper.passthrough((Type)Type.FLOAT);
/*     */             } 
/*     */             wrapper.passthrough(Type.STRING_ARRAY);
/*     */             int arrayLength = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */             for (int array = 0; array < arrayLength; array++) {
/*     */               wrapper.passthrough(Type.STRING_ARRAY);
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
/*     */   public void registerAdvancements1_20_2(C packetType) {
/* 315 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, wrapper -> {
/*     */           wrapper.passthrough((Type)Type.BOOLEAN);
/*     */           int size = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */           for (int i = 0; i < size; i++) {
/*     */             wrapper.passthrough(Type.STRING);
/*     */             if (((Boolean)wrapper.passthrough((Type)Type.BOOLEAN)).booleanValue()) {
/*     */               wrapper.passthrough(Type.STRING);
/*     */             }
/*     */             if (((Boolean)wrapper.passthrough((Type)Type.BOOLEAN)).booleanValue()) {
/*     */               wrapper.passthrough(Type.COMPONENT);
/*     */               wrapper.passthrough(Type.COMPONENT);
/*     */               handleItemToClient((Item)wrapper.passthrough(this.itemType));
/*     */               wrapper.passthrough((Type)Type.VAR_INT);
/*     */               int flags = ((Integer)wrapper.passthrough((Type)Type.INT)).intValue();
/*     */               if ((flags & 0x1) != 0) {
/*     */                 wrapper.passthrough(Type.STRING);
/*     */               }
/*     */               wrapper.passthrough((Type)Type.FLOAT);
/*     */               wrapper.passthrough((Type)Type.FLOAT);
/*     */             } 
/*     */             int requirements = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */             for (int array = 0; array < requirements; array++) {
/*     */               wrapper.passthrough(Type.STRING_ARRAY);
/*     */             }
/*     */             wrapper.passthrough((Type)Type.BOOLEAN);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerWindowPropertyEnchantmentHandler(C packetType) {
/* 350 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 353 */             map((Type)Type.UNSIGNED_BYTE);
/* 354 */             handler(wrapper -> {
/*     */                   Mappings mappings = ItemRewriter.this.protocol.getMappingData().getEnchantmentMappings();
/*     */                   if (mappings == null) {
/*     */                     return;
/*     */                   }
/*     */                   short property = ((Short)wrapper.passthrough((Type)Type.SHORT)).shortValue();
/*     */                   if (property >= 4 && property <= 6) {
/*     */                     short enchantmentId = (short)mappings.getNewId(((Short)wrapper.read((Type)Type.SHORT)).shortValue());
/*     */                     wrapper.write((Type)Type.SHORT, Short.valueOf(enchantmentId));
/*     */                   } 
/*     */                 });
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerSpawnParticle(C packetType, Type<Item> itemType, final Type<?> coordType) {
/* 372 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 375 */             map((Type)Type.INT);
/* 376 */             map((Type)Type.BOOLEAN);
/* 377 */             map(coordType);
/* 378 */             map(coordType);
/* 379 */             map(coordType);
/* 380 */             map((Type)Type.FLOAT);
/* 381 */             map((Type)Type.FLOAT);
/* 382 */             map((Type)Type.FLOAT);
/* 383 */             map((Type)Type.FLOAT);
/* 384 */             map((Type)Type.INT);
/* 385 */             handler(ItemRewriter.this.getSpawnParticleHandler());
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void registerSpawnParticle1_19(C packetType) {
/* 391 */     this.protocol.registerClientbound((ClientboundPacketType)packetType, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 394 */             map((Type)Type.VAR_INT);
/* 395 */             map((Type)Type.BOOLEAN);
/* 396 */             map((Type)Type.DOUBLE);
/* 397 */             map((Type)Type.DOUBLE);
/* 398 */             map((Type)Type.DOUBLE);
/* 399 */             map((Type)Type.FLOAT);
/* 400 */             map((Type)Type.FLOAT);
/* 401 */             map((Type)Type.FLOAT);
/* 402 */             map((Type)Type.FLOAT);
/* 403 */             map((Type)Type.INT);
/* 404 */             handler(ItemRewriter.this.getSpawnParticleHandler((Type<Integer>)Type.VAR_INT));
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public PacketHandler getSpawnParticleHandler() {
/* 410 */     return getSpawnParticleHandler((Type<Integer>)Type.INT);
/*     */   }
/*     */   
/*     */   public PacketHandler getSpawnParticleHandler(Type<Integer> idType) {
/* 414 */     return wrapper -> {
/*     */         int id = ((Integer)wrapper.get(idType, 0)).intValue();
/*     */         if (id == -1) {
/*     */           return;
/*     */         }
/*     */         ParticleMappings mappings = this.protocol.getMappingData().getParticleMappings();
/*     */         if (mappings.isBlockParticle(id)) {
/*     */           int data = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
/*     */           wrapper.write((Type)Type.VAR_INT, Integer.valueOf(this.protocol.getMappingData().getNewBlockStateId(data)));
/*     */         } else if (mappings.isItemParticle(id)) {
/*     */           handleItemToClient((Item)wrapper.passthrough(this.itemType));
/*     */         } 
/*     */         int mappedId = this.protocol.getMappingData().getNewParticleId(id);
/*     */         if (mappedId != id) {
/*     */           wrapper.set(idType, 0, Integer.valueOf(mappedId));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PacketHandler itemArrayToClientHandler(Type<Item[]> type) {
/* 436 */     return wrapper -> {
/*     */         Item[] items = (Item[])wrapper.get(type, 0);
/*     */         for (Item item : items) {
/*     */           handleItemToClient(item);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public PacketHandler itemToClientHandler(Type<Item> type) {
/* 445 */     return wrapper -> handleItemToClient((Item)wrapper.get(type, 0));
/*     */   }
/*     */   
/*     */   public PacketHandler itemToServerHandler(Type<Item> type) {
/* 449 */     return wrapper -> handleItemToServer((Item)wrapper.get(type, 0));
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\rewriter\ItemRewriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */