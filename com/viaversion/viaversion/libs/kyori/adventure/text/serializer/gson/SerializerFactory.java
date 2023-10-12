/*    */ package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;
/*    */ 
/*    */ import com.viaversion.viaversion.libs.gson.Gson;
/*    */ import com.viaversion.viaversion.libs.gson.TypeAdapter;
/*    */ import com.viaversion.viaversion.libs.gson.TypeAdapterFactory;
/*    */ import com.viaversion.viaversion.libs.gson.reflect.TypeToken;
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.text.BlockNBTComponent;
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.text.event.ClickEvent;
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextColor;
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextDecoration;
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class SerializerFactory
/*    */   implements TypeAdapterFactory
/*    */ {
/* 41 */   static final Class<Key> KEY_TYPE = Key.class;
/* 42 */   static final Class<Component> COMPONENT_TYPE = Component.class;
/* 43 */   static final Class<Style> STYLE_TYPE = Style.class;
/* 44 */   static final Class<ClickEvent.Action> CLICK_ACTION_TYPE = ClickEvent.Action.class;
/* 45 */   static final Class<HoverEvent.Action> HOVER_ACTION_TYPE = HoverEvent.Action.class;
/* 46 */   static final Class<HoverEvent.ShowItem> SHOW_ITEM_TYPE = HoverEvent.ShowItem.class;
/* 47 */   static final Class<HoverEvent.ShowEntity> SHOW_ENTITY_TYPE = HoverEvent.ShowEntity.class;
/* 48 */   static final Class<String> STRING_TYPE = String.class;
/* 49 */   static final Class<TextColorWrapper> COLOR_WRAPPER_TYPE = TextColorWrapper.class;
/* 50 */   static final Class<TextColor> COLOR_TYPE = TextColor.class;
/* 51 */   static final Class<TextDecoration> TEXT_DECORATION_TYPE = TextDecoration.class;
/* 52 */   static final Class<BlockNBTComponent.Pos> BLOCK_NBT_POS_TYPE = BlockNBTComponent.Pos.class;
/*    */   
/*    */   private final boolean downsampleColors;
/*    */   private final LegacyHoverEventSerializer legacyHoverSerializer;
/*    */   private final boolean emitLegacyHover;
/*    */   
/*    */   SerializerFactory(boolean downsampleColors, LegacyHoverEventSerializer legacyHoverSerializer, boolean emitLegacyHover) {
/* 59 */     this.downsampleColors = downsampleColors;
/* 60 */     this.legacyHoverSerializer = legacyHoverSerializer;
/* 61 */     this.emitLegacyHover = emitLegacyHover;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
/* 67 */     Class<? super T> rawType = type.getRawType();
/* 68 */     if (COMPONENT_TYPE.isAssignableFrom(rawType))
/* 69 */       return (TypeAdapter)ComponentSerializerImpl.create(gson); 
/* 70 */     if (KEY_TYPE.isAssignableFrom(rawType))
/* 71 */       return (TypeAdapter)KeySerializer.INSTANCE; 
/* 72 */     if (STYLE_TYPE.isAssignableFrom(rawType))
/* 73 */       return (TypeAdapter)StyleSerializer.create(this.legacyHoverSerializer, this.emitLegacyHover, gson); 
/* 74 */     if (CLICK_ACTION_TYPE.isAssignableFrom(rawType))
/* 75 */       return (TypeAdapter)ClickEventActionSerializer.INSTANCE; 
/* 76 */     if (HOVER_ACTION_TYPE.isAssignableFrom(rawType))
/* 77 */       return (TypeAdapter)HoverEventActionSerializer.INSTANCE; 
/* 78 */     if (SHOW_ITEM_TYPE.isAssignableFrom(rawType))
/* 79 */       return (TypeAdapter)ShowItemSerializer.create(gson); 
/* 80 */     if (SHOW_ENTITY_TYPE.isAssignableFrom(rawType))
/* 81 */       return (TypeAdapter)ShowEntitySerializer.create(gson); 
/* 82 */     if (COLOR_WRAPPER_TYPE.isAssignableFrom(rawType))
/* 83 */       return TextColorWrapper.Serializer.INSTANCE; 
/* 84 */     if (COLOR_TYPE.isAssignableFrom(rawType))
/* 85 */       return this.downsampleColors ? (TypeAdapter)TextColorSerializer.DOWNSAMPLE_COLOR : (TypeAdapter)TextColorSerializer.INSTANCE; 
/* 86 */     if (TEXT_DECORATION_TYPE.isAssignableFrom(rawType))
/* 87 */       return (TypeAdapter)TextDecorationSerializer.INSTANCE; 
/* 88 */     if (BLOCK_NBT_POS_TYPE.isAssignableFrom(rawType)) {
/* 89 */       return (TypeAdapter)BlockNBTComponentPosSerializer.INSTANCE;
/*    */     }
/* 91 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\text\serializer\gson\SerializerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */