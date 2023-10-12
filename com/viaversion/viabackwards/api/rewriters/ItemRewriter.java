/*     */ package com.viaversion.viabackwards.api.rewriters;
/*     */ 
/*     */ import com.viaversion.viabackwards.api.BackwardsProtocol;
/*     */ import com.viaversion.viabackwards.api.data.MappedItem;
/*     */ import com.viaversion.viaversion.api.minecraft.item.Item;
/*     */ import com.viaversion.viaversion.api.protocol.Protocol;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
/*     */ import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import com.viaversion.viaversion.libs.gson.JsonElement;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
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
/*     */ 
/*     */ public class ItemRewriter<C extends ClientboundPacketType, S extends ServerboundPacketType, T extends BackwardsProtocol<C, ?, ?, S>>
/*     */   extends ItemRewriterBase<C, S, T>
/*     */ {
/*     */   public ItemRewriter(T protocol) {
/*  40 */     super(protocol, true);
/*     */   }
/*     */   
/*     */   public ItemRewriter(T protocol, Type<Item> itemType, Type<Item[]> itemArrayType) {
/*  44 */     super(protocol, itemType, itemArrayType, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public Item handleItemToClient(Item item) {
/*  49 */     if (item == null) {
/*  50 */       return null;
/*     */     }
/*     */     
/*  53 */     CompoundTag display = (item.tag() != null) ? (CompoundTag)item.tag().get("display") : null;
/*  54 */     if (((BackwardsProtocol)this.protocol).getTranslatableRewriter() != null && display != null) {
/*     */       
/*  56 */       Tag name = display.get("Name");
/*  57 */       if (name instanceof StringTag) {
/*  58 */         StringTag nameStringTag = (StringTag)name;
/*  59 */         String newValue = ((BackwardsProtocol)this.protocol).getTranslatableRewriter().processText(nameStringTag.getValue()).toString();
/*  60 */         if (!newValue.equals(name.getValue())) {
/*  61 */           saveStringTag(display, nameStringTag, "Name");
/*     */         }
/*     */         
/*  64 */         nameStringTag.setValue(newValue);
/*     */       } 
/*     */       
/*  67 */       Tag lore = display.get("Lore");
/*  68 */       if (lore instanceof ListTag) {
/*  69 */         ListTag loreListTag = (ListTag)lore;
/*  70 */         boolean changed = false;
/*  71 */         for (Tag loreEntryTag : loreListTag) {
/*  72 */           if (!(loreEntryTag instanceof StringTag)) {
/*     */             continue;
/*     */           }
/*     */           
/*  76 */           StringTag loreEntry = (StringTag)loreEntryTag;
/*  77 */           String newValue = ((BackwardsProtocol)this.protocol).getTranslatableRewriter().processText(loreEntry.getValue()).toString();
/*  78 */           if (!changed && !newValue.equals(loreEntry.getValue())) {
/*     */             
/*  80 */             changed = true;
/*  81 */             saveListTag(display, loreListTag, "Lore");
/*     */           } 
/*     */           
/*  84 */           loreEntry.setValue(newValue);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  89 */     MappedItem data = (((BackwardsProtocol)this.protocol).getMappingData() != null) ? ((BackwardsProtocol)this.protocol).getMappingData().getMappedItem(item.identifier()) : null;
/*  90 */     if (data == null)
/*     */     {
/*  92 */       return super.handleItemToClient(item);
/*     */     }
/*     */     
/*  95 */     if (item.tag() == null) {
/*  96 */       item.setTag(new CompoundTag());
/*     */     }
/*     */ 
/*     */     
/* 100 */     item.tag().put(this.nbtTagName + "|id", (Tag)new IntTag(item.identifier()));
/* 101 */     item.setIdentifier(data.getId());
/*     */ 
/*     */     
/* 104 */     if (data.customModelData() != null && !item.tag().contains("CustomModelData")) {
/* 105 */       item.tag().put("CustomModelData", (Tag)new IntTag(data.customModelData().intValue()));
/*     */     }
/*     */ 
/*     */     
/* 109 */     if (display == null) {
/* 110 */       item.tag().put("display", (Tag)(display = new CompoundTag()));
/*     */     }
/* 112 */     if (!display.contains("Name")) {
/* 113 */       display.put("Name", (Tag)new StringTag(data.getJsonName()));
/* 114 */       display.put(this.nbtTagName + "|customName", (Tag)new ByteTag());
/*     */     } 
/* 116 */     return item;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item handleItemToServer(Item item) {
/* 121 */     if (item == null) return null;
/*     */     
/* 123 */     super.handleItemToServer(item);
/* 124 */     if (item.tag() != null) {
/* 125 */       IntTag originalId = (IntTag)item.tag().remove(this.nbtTagName + "|id");
/* 126 */       if (originalId != null) {
/* 127 */         item.setIdentifier(originalId.asInt());
/*     */       }
/*     */     } 
/* 130 */     return item;
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerAdvancements(C packetType, final Type<Item> type) {
/* 135 */     ((BackwardsProtocol)this.protocol).registerClientbound((ClientboundPacketType)packetType, (PacketHandler)new PacketHandlers()
/*     */         {
/*     */           public void register() {
/* 138 */             handler(wrapper -> {
/*     */                   wrapper.passthrough((Type)Type.BOOLEAN);
/*     */                   int size = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */                   for (int i = 0; i < size; i++) {
/*     */                     wrapper.passthrough(Type.STRING);
/*     */                     if (((Boolean)wrapper.passthrough((Type)Type.BOOLEAN)).booleanValue())
/*     */                       wrapper.passthrough(Type.STRING); 
/*     */                     if (((Boolean)wrapper.passthrough((Type)Type.BOOLEAN)).booleanValue()) {
/*     */                       JsonElement title = (JsonElement)wrapper.passthrough(Type.COMPONENT);
/*     */                       JsonElement description = (JsonElement)wrapper.passthrough(Type.COMPONENT);
/*     */                       TranslatableRewriter<C> translatableRewriter = ((BackwardsProtocol)ItemRewriter.this.protocol).getTranslatableRewriter();
/*     */                       if (translatableRewriter != null) {
/*     */                         translatableRewriter.processText(title);
/*     */                         translatableRewriter.processText(description);
/*     */                       } 
/*     */                       ItemRewriter.this.handleItemToClient((Item)wrapper.passthrough(type));
/*     */                       wrapper.passthrough((Type)Type.VAR_INT);
/*     */                       int flags = ((Integer)wrapper.passthrough((Type)Type.INT)).intValue();
/*     */                       if ((flags & 0x1) != 0)
/*     */                         wrapper.passthrough(Type.STRING); 
/*     */                       wrapper.passthrough((Type)Type.FLOAT);
/*     */                       wrapper.passthrough((Type)Type.FLOAT);
/*     */                     } 
/*     */                     wrapper.passthrough(Type.STRING_ARRAY);
/*     */                     int arrayLength = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
/*     */                     for (int array = 0; array < arrayLength; array++)
/*     */                       wrapper.passthrough(Type.STRING_ARRAY); 
/*     */                   } 
/*     */                 });
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viabackwards\api\rewriters\ItemRewriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */