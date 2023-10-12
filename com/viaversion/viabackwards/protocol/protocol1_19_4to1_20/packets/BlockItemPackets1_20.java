/*     */ package com.viaversion.viabackwards.protocol.protocol1_19_4to1_20.packets;
/*     */ 
/*     */ import com.viaversion.viabackwards.api.BackwardsProtocol;
/*     */ import com.viaversion.viabackwards.api.rewriters.ItemRewriter;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_19_4to1_20.Protocol1_19_4To1_20;
/*     */ import com.viaversion.viabackwards.protocol.protocol1_19_4to1_20.storage.BackSignEditStorage;
/*     */ import com.viaversion.viaversion.api.connection.StorableObject;
/*     */ import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
/*     */ import com.viaversion.viaversion.api.minecraft.Position;
/*     */ import com.viaversion.viaversion.api.minecraft.blockentity.BlockEntity;
/*     */ import com.viaversion.viaversion.api.minecraft.item.Item;
/*     */ import com.viaversion.viaversion.api.protocol.Protocol;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ClientboundPackets1_19_4;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ServerboundPackets1_19_4;
/*     */ import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.rewriter.RecipeRewriter1_19_4;
/*     */ import com.viaversion.viaversion.rewriter.BlockRewriter;
/*     */ import com.viaversion.viaversion.util.Key;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
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
/*     */ public final class BlockItemPackets1_20
/*     */   extends ItemRewriter<ClientboundPackets1_19_4, ServerboundPackets1_19_4, Protocol1_19_4To1_20>
/*     */ {
/*  46 */   private static final Set<String> NEW_TRIM_PATTERNS = new HashSet<>(Arrays.asList(new String[] { "host", "raiser", "shaper", "silence", "wayfinder" }));
/*     */   
/*     */   public BlockItemPackets1_20(Protocol1_19_4To1_20 protocol) {
/*  49 */     super((BackwardsProtocol)protocol);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerPackets() {
/*  54 */     final BlockRewriter<ClientboundPackets1_19_4> blockRewriter = new BlockRewriter(this.protocol, Type.POSITION1_14);
/*  55 */     blockRewriter.registerBlockAction((ClientboundPacketType)ClientboundPackets1_19_4.BLOCK_ACTION);
/*  56 */     blockRewriter.registerBlockChange((ClientboundPacketType)ClientboundPackets1_19_4.BLOCK_CHANGE);
/*  57 */     blockRewriter.registerEffect((ClientboundPacketType)ClientboundPackets1_19_4.EFFECT, 1010, 2001);
/*  58 */     blockRewriter.registerBlockEntityData((ClientboundPacketType)ClientboundPackets1_19_4.BLOCK_ENTITY_DATA, this::handleBlockEntity);
/*     */     
/*  60 */     ((Protocol1_19_4To1_20)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.CHUNK_DATA, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           protected void register() {
/*  63 */             handler(blockRewriter.chunkDataHandler1_19(com.viaversion.viaversion.protocols.protocol1_18to1_17_1.types.Chunk1_18Type::new, x$0 -> rec$.handleBlockEntity(x$0)));
/*  64 */             create((Type)Type.BOOLEAN, Boolean.valueOf(true));
/*     */           }
/*     */         });
/*     */     
/*  68 */     ((Protocol1_19_4To1_20)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.UPDATE_LIGHT, wrapper -> {
/*     */           wrapper.passthrough((Type)Type.VAR_INT);
/*     */           
/*     */           wrapper.passthrough((Type)Type.VAR_INT);
/*     */           wrapper.write((Type)Type.BOOLEAN, Boolean.valueOf(true));
/*     */         });
/*  74 */     ((Protocol1_19_4To1_20)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.MULTI_BLOCK_CHANGE, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/*  77 */             map((Type)Type.LONG);
/*  78 */             create((Type)Type.BOOLEAN, Boolean.valueOf(false));
/*  79 */             handler(wrapper -> {
/*     */                   for (BlockChangeRecord record : (BlockChangeRecord[])wrapper.passthrough(Type.VAR_LONG_BLOCK_CHANGE_RECORD_ARRAY)) {
/*     */                     record.setBlockId(((Protocol1_19_4To1_20)BlockItemPackets1_20.this.protocol).getMappingData().getNewBlockStateId(record.getBlockId()));
/*     */                   }
/*     */                 });
/*     */           }
/*     */         });
/*     */     
/*  87 */     registerOpenWindow((ClientboundPacketType)ClientboundPackets1_19_4.OPEN_WINDOW);
/*  88 */     registerSetCooldown((ClientboundPacketType)ClientboundPackets1_19_4.COOLDOWN);
/*  89 */     registerWindowItems1_17_1((ClientboundPacketType)ClientboundPackets1_19_4.WINDOW_ITEMS);
/*  90 */     registerSetSlot1_17_1((ClientboundPacketType)ClientboundPackets1_19_4.SET_SLOT);
/*  91 */     registerEntityEquipmentArray((ClientboundPacketType)ClientboundPackets1_19_4.ENTITY_EQUIPMENT);
/*  92 */     registerClickWindow1_17_1((ServerboundPacketType)ServerboundPackets1_19_4.CLICK_WINDOW);
/*  93 */     registerTradeList1_19((ClientboundPacketType)ClientboundPackets1_19_4.TRADE_LIST);
/*  94 */     registerCreativeInvAction((ServerboundPacketType)ServerboundPackets1_19_4.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
/*  95 */     registerWindowPropertyEnchantmentHandler((ClientboundPacketType)ClientboundPackets1_19_4.WINDOW_PROPERTY);
/*  96 */     registerSpawnParticle1_19((ClientboundPacketType)ClientboundPackets1_19_4.SPAWN_PARTICLE);
/*     */     
/*  98 */     ((Protocol1_19_4To1_20)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.ADVANCEMENTS, wrapper -> {
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
/*     */               handleItemToClient((Item)wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
/*     */               
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
/*     */             wrapper.read((Type)Type.BOOLEAN);
/*     */           } 
/*     */         });
/* 133 */     ((Protocol1_19_4To1_20)this.protocol).registerClientbound((ClientboundPacketType)ClientboundPackets1_19_4.OPEN_SIGN_EDITOR, wrapper -> {
/*     */           Position position = (Position)wrapper.passthrough(Type.POSITION1_14);
/*     */           boolean frontSide = ((Boolean)wrapper.read((Type)Type.BOOLEAN)).booleanValue();
/*     */           if (frontSide) {
/*     */             wrapper.user().remove(BackSignEditStorage.class);
/*     */           } else {
/*     */             wrapper.user().put((StorableObject)new BackSignEditStorage(position));
/*     */           } 
/*     */         });
/* 142 */     ((Protocol1_19_4To1_20)this.protocol).registerServerbound((ServerboundPacketType)ServerboundPackets1_19_4.UPDATE_SIGN, wrapper -> {
/*     */           Position position = (Position)wrapper.passthrough(Type.POSITION1_14);
/*     */           BackSignEditStorage backSignEditStorage = (BackSignEditStorage)wrapper.user().remove(BackSignEditStorage.class);
/* 145 */           boolean frontSide = (backSignEditStorage == null || !backSignEditStorage.position().equals(position));
/*     */           
/*     */           wrapper.write((Type)Type.BOOLEAN, Boolean.valueOf(frontSide));
/*     */         });
/* 149 */     (new RecipeRewriter1_19_4(this.protocol)).register((ClientboundPacketType)ClientboundPackets1_19_4.DECLARE_RECIPES);
/*     */   }
/*     */ 
/*     */   
/*     */   public Item handleItemToClient(Item item) {
/* 154 */     if (item == null) {
/* 155 */       return null;
/*     */     }
/*     */     
/* 158 */     super.handleItemToClient(item);
/*     */     
/*     */     Tag trimTag;
/*     */     
/* 162 */     if (item.tag() != null && trimTag = item.tag().get("Trim") instanceof CompoundTag) {
/* 163 */       Tag patternTag = ((CompoundTag)trimTag).get("pattern");
/* 164 */       if (patternTag instanceof StringTag) {
/* 165 */         StringTag patternStringTag = (StringTag)patternTag;
/* 166 */         String pattern = Key.stripMinecraftNamespace(patternStringTag.getValue());
/* 167 */         if (NEW_TRIM_PATTERNS.contains(pattern)) {
/* 168 */           item.tag().remove("Trim");
/* 169 */           item.tag().put(this.nbtTagName + "|Trim", trimTag);
/*     */         } 
/*     */       } 
/*     */     } 
/* 173 */     return item;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item handleItemToServer(Item item) {
/* 178 */     if (item == null) {
/* 179 */       return null;
/*     */     }
/*     */     
/* 182 */     super.handleItemToServer(item);
/*     */     
/*     */     Tag trimTag;
/*     */     
/* 186 */     if (item.tag() != null && (trimTag = item.tag().remove(this.nbtTagName + "|Trim")) != null) {
/* 187 */       item.tag().put("Trim", trimTag);
/*     */     }
/* 189 */     return item;
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleBlockEntity(BlockEntity blockEntity) {
/* 194 */     if (blockEntity.typeId() != 7 && blockEntity.typeId() != 8) {
/*     */       return;
/*     */     }
/*     */     
/* 198 */     CompoundTag tag = blockEntity.tag();
/* 199 */     CompoundTag frontText = (CompoundTag)tag.remove("front_text");
/* 200 */     tag.remove("back_text");
/*     */     
/* 202 */     if (frontText != null) {
/* 203 */       writeMessages(frontText, tag, false);
/* 204 */       writeMessages(frontText, tag, true);
/*     */       
/* 206 */       Tag color = frontText.remove("color");
/* 207 */       if (color != null) {
/* 208 */         tag.put("Color", color);
/*     */       }
/*     */       
/* 211 */       Tag glowing = frontText.remove("has_glowing_text");
/* 212 */       if (glowing != null) {
/* 213 */         tag.put("GlowingText", glowing);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeMessages(CompoundTag frontText, CompoundTag tag, boolean filtered) {
/* 219 */     ListTag messages = (ListTag)frontText.get(filtered ? "filtered_messages" : "messages");
/* 220 */     if (messages == null) {
/*     */       return;
/*     */     }
/*     */     
/* 224 */     int i = 0;
/* 225 */     for (Tag message : messages)
/* 226 */       tag.put((filtered ? "FilteredText" : "Text") + ++i, message); 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viabackwards\protocol\protocol1_19_4to1_20\packets\BlockItemPackets1_20.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */