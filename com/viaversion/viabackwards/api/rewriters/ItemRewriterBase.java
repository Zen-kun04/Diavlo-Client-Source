/*     */ package com.viaversion.viabackwards.api.rewriters;
/*     */ 
/*     */ import com.viaversion.viabackwards.api.BackwardsProtocol;
/*     */ import com.viaversion.viaversion.api.minecraft.item.Item;
/*     */ import com.viaversion.viaversion.api.protocol.Protocol;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
/*     */ import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
/*     */ import com.viaversion.viaversion.api.type.Type;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
/*     */ import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
/*     */ import com.viaversion.viaversion.rewriter.ItemRewriter;
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
/*     */ 
/*     */ 
/*     */ public abstract class ItemRewriterBase<C extends ClientboundPacketType, S extends ServerboundPacketType, T extends BackwardsProtocol<C, ?, ?, S>>
/*     */   extends ItemRewriter<C, S, T>
/*     */ {
/*     */   protected final String nbtTagName;
/*     */   protected final boolean jsonNameFormat;
/*     */   
/*     */   protected ItemRewriterBase(T protocol, boolean jsonNameFormat) {
/*  39 */     this(protocol, Type.FLAT_VAR_INT_ITEM, Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT, jsonNameFormat);
/*     */   }
/*     */   
/*     */   public ItemRewriterBase(T protocol, Type<Item> itemType, Type<Item[]> itemArrayType, boolean jsonNameFormat) {
/*  43 */     super((Protocol)protocol, itemType, itemArrayType);
/*  44 */     this.jsonNameFormat = jsonNameFormat;
/*  45 */     this.nbtTagName = "VB|" + protocol.getClass().getSimpleName();
/*     */   }
/*     */ 
/*     */   
/*     */   public Item handleItemToServer(Item item) {
/*  50 */     if (item == null) return null; 
/*  51 */     super.handleItemToServer(item);
/*     */     
/*  53 */     restoreDisplayTag(item);
/*  54 */     return item;
/*     */   }
/*     */   
/*     */   protected boolean hasBackupTag(CompoundTag displayTag, String tagName) {
/*  58 */     return displayTag.contains(this.nbtTagName + "|o" + tagName);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveStringTag(CompoundTag displayTag, StringTag original, String name) {
/*  63 */     String backupName = this.nbtTagName + "|o" + name;
/*  64 */     if (!displayTag.contains(backupName)) {
/*  65 */       displayTag.put(backupName, (Tag)new StringTag(original.getValue()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveListTag(CompoundTag displayTag, ListTag original, String name) {
/*  71 */     String backupName = this.nbtTagName + "|o" + name;
/*  72 */     if (!displayTag.contains(backupName)) {
/*     */       
/*  74 */       ListTag listTag = new ListTag();
/*  75 */       for (Tag tag : original.getValue()) {
/*  76 */         listTag.add(tag.clone());
/*     */       }
/*     */       
/*  79 */       displayTag.put(backupName, (Tag)listTag);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void restoreDisplayTag(Item item) {
/*  84 */     if (item.tag() == null)
/*     */       return; 
/*  86 */     CompoundTag display = (CompoundTag)item.tag().get("display");
/*  87 */     if (display != null) {
/*     */       
/*  89 */       if (display.remove(this.nbtTagName + "|customName") != null) {
/*  90 */         display.remove("Name");
/*     */       } else {
/*  92 */         restoreStringTag(display, "Name");
/*     */       } 
/*     */ 
/*     */       
/*  96 */       restoreListTag(display, "Lore");
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void restoreStringTag(CompoundTag tag, String tagName) {
/* 101 */     StringTag original = (StringTag)tag.remove(this.nbtTagName + "|o" + tagName);
/* 102 */     if (original != null) {
/* 103 */       tag.put(tagName, (Tag)new StringTag(original.getValue()));
/*     */     }
/*     */   }
/*     */   
/*     */   protected void restoreListTag(CompoundTag tag, String tagName) {
/* 108 */     ListTag original = (ListTag)tag.remove(this.nbtTagName + "|o" + tagName);
/* 109 */     if (original != null) {
/* 110 */       tag.put(tagName, (Tag)new ListTag(original.getValue()));
/*     */     }
/*     */   }
/*     */   
/*     */   public String getNbtTagName() {
/* 115 */     return this.nbtTagName;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viabackwards\api\rewriters\ItemRewriterBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */