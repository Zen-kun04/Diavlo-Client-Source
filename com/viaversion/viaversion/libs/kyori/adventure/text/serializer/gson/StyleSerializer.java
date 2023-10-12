/*     */ package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;
/*     */ 
/*     */ import com.viaversion.viaversion.libs.gson.Gson;
/*     */ import com.viaversion.viaversion.libs.gson.JsonElement;
/*     */ import com.viaversion.viaversion.libs.gson.JsonObject;
/*     */ import com.viaversion.viaversion.libs.gson.JsonParseException;
/*     */ import com.viaversion.viaversion.libs.gson.JsonPrimitive;
/*     */ import com.viaversion.viaversion.libs.gson.JsonSyntaxException;
/*     */ import com.viaversion.viaversion.libs.gson.TypeAdapter;
/*     */ import com.viaversion.viaversion.libs.gson.stream.JsonReader;
/*     */ import com.viaversion.viaversion.libs.gson.stream.JsonToken;
/*     */ import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.event.ClickEvent;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEventSource;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextColor;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextDecoration;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.util.Codec;
/*     */ import java.io.IOException;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Set;
/*     */ import org.jetbrains.annotations.Nullable;
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
/*     */ final class StyleSerializer
/*     */   extends TypeAdapter<Style>
/*     */ {
/*  62 */   private static final TextDecoration[] DECORATIONS = new TextDecoration[] { TextDecoration.BOLD, TextDecoration.ITALIC, TextDecoration.UNDERLINED, TextDecoration.STRIKETHROUGH, TextDecoration.OBFUSCATED };
/*     */ 
/*     */   
/*     */   private final LegacyHoverEventSerializer legacyHover;
/*     */ 
/*     */   
/*     */   private final boolean emitLegacyHover;
/*     */   
/*     */   private final Gson gson;
/*     */ 
/*     */   
/*     */   static {
/*  74 */     Set<TextDecoration> knownDecorations = EnumSet.allOf(TextDecoration.class);
/*  75 */     for (TextDecoration decoration : DECORATIONS) {
/*  76 */       knownDecorations.remove(decoration);
/*     */     }
/*  78 */     if (!knownDecorations.isEmpty()) {
/*  79 */       throw new IllegalStateException("Gson serializer is missing some text decorations: " + knownDecorations);
/*     */     }
/*     */   }
/*     */   
/*     */   static TypeAdapter<Style> create(LegacyHoverEventSerializer legacyHover, boolean emitLegacyHover, Gson gson) {
/*  84 */     return (new StyleSerializer(legacyHover, emitLegacyHover, gson)).nullSafe();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private StyleSerializer(LegacyHoverEventSerializer legacyHover, boolean emitLegacyHover, Gson gson) {
/*  92 */     this.legacyHover = legacyHover;
/*  93 */     this.emitLegacyHover = emitLegacyHover;
/*  94 */     this.gson = gson;
/*     */   }
/*     */ 
/*     */   
/*     */   public Style read(JsonReader in) throws IOException {
/*  99 */     in.beginObject();
/* 100 */     Style.Builder style = Style.style();
/*     */     
/* 102 */     while (in.hasNext()) {
/* 103 */       String fieldName = in.nextName();
/* 104 */       if (fieldName.equals("font")) {
/* 105 */         style.font((Key)this.gson.fromJson(in, SerializerFactory.KEY_TYPE)); continue;
/* 106 */       }  if (fieldName.equals("color")) {
/* 107 */         TextColorWrapper color = (TextColorWrapper)this.gson.fromJson(in, SerializerFactory.COLOR_WRAPPER_TYPE);
/* 108 */         if (color.color != null) {
/* 109 */           style.color(color.color); continue;
/* 110 */         }  if (color.decoration != null)
/* 111 */           style.decoration(color.decoration, TextDecoration.State.TRUE);  continue;
/*     */       } 
/* 113 */       if (TextDecoration.NAMES.keys().contains(fieldName)) {
/* 114 */         style.decoration((TextDecoration)TextDecoration.NAMES.value(fieldName), readBoolean(in)); continue;
/* 115 */       }  if (fieldName.equals("insertion")) {
/* 116 */         style.insertion(in.nextString()); continue;
/* 117 */       }  if (fieldName.equals("clickEvent")) {
/* 118 */         in.beginObject();
/* 119 */         ClickEvent.Action action = null;
/* 120 */         String value = null;
/* 121 */         while (in.hasNext()) {
/* 122 */           String clickEventField = in.nextName();
/* 123 */           if (clickEventField.equals("action")) {
/* 124 */             action = (ClickEvent.Action)this.gson.fromJson(in, SerializerFactory.CLICK_ACTION_TYPE); continue;
/* 125 */           }  if (clickEventField.equals("value")) {
/* 126 */             value = (in.peek() == JsonToken.NULL) ? null : in.nextString(); continue;
/*     */           } 
/* 128 */           in.skipValue();
/*     */         } 
/*     */         
/* 131 */         if (action != null && action.readable() && value != null) {
/* 132 */           style.clickEvent(ClickEvent.clickEvent(action, value));
/*     */         }
/* 134 */         in.endObject(); continue;
/* 135 */       }  if (fieldName.equals("hoverEvent")) {
/* 136 */         JsonObject hoverEventObject = (JsonObject)this.gson.fromJson(in, JsonObject.class);
/* 137 */         if (hoverEventObject != null) {
/* 138 */           JsonPrimitive serializedAction = hoverEventObject.getAsJsonPrimitive("action");
/* 139 */           if (serializedAction == null) {
/*     */             continue;
/*     */           }
/*     */ 
/*     */           
/* 144 */           HoverEvent.Action<Object> action = (HoverEvent.Action<Object>)this.gson.fromJson((JsonElement)serializedAction, SerializerFactory.HOVER_ACTION_TYPE);
/* 145 */           if (action.readable()) {
/*     */             Object value;
/* 147 */             Class<?> actionType = action.type();
/* 148 */             if (hoverEventObject.has("contents")) {
/* 149 */               JsonElement rawValue = hoverEventObject.get("contents");
/* 150 */               if (isNullOrEmpty(rawValue)) {
/* 151 */                 value = null;
/* 152 */               } else if (SerializerFactory.COMPONENT_TYPE.isAssignableFrom(actionType)) {
/* 153 */                 value = this.gson.fromJson(rawValue, SerializerFactory.COMPONENT_TYPE);
/* 154 */               } else if (SerializerFactory.SHOW_ITEM_TYPE.isAssignableFrom(actionType)) {
/* 155 */                 value = this.gson.fromJson(rawValue, SerializerFactory.SHOW_ITEM_TYPE);
/* 156 */               } else if (SerializerFactory.SHOW_ENTITY_TYPE.isAssignableFrom(actionType)) {
/* 157 */                 value = this.gson.fromJson(rawValue, SerializerFactory.SHOW_ENTITY_TYPE);
/*     */               } else {
/* 159 */                 value = null;
/*     */               } 
/* 161 */             } else if (hoverEventObject.has("value")) {
/* 162 */               JsonElement element = hoverEventObject.get("value");
/* 163 */               if (isNullOrEmpty(element)) {
/* 164 */                 value = null;
/* 165 */               } else if (SerializerFactory.COMPONENT_TYPE.isAssignableFrom(actionType)) {
/* 166 */                 Component rawValue = (Component)this.gson.fromJson(element, SerializerFactory.COMPONENT_TYPE);
/* 167 */                 value = legacyHoverEventContents(action, rawValue);
/* 168 */               } else if (SerializerFactory.STRING_TYPE.isAssignableFrom(actionType)) {
/* 169 */                 value = this.gson.fromJson(element, SerializerFactory.STRING_TYPE);
/*     */               } else {
/* 171 */                 value = null;
/*     */               } 
/*     */             } else {
/* 174 */               value = null;
/*     */             } 
/*     */             
/* 177 */             if (value != null)
/* 178 */               style.hoverEvent((HoverEventSource)HoverEvent.hoverEvent(action, value)); 
/*     */           } 
/*     */         } 
/*     */         continue;
/*     */       } 
/* 183 */       in.skipValue();
/*     */     } 
/*     */ 
/*     */     
/* 187 */     in.endObject();
/* 188 */     return style.build();
/*     */   }
/*     */   
/*     */   private static boolean isNullOrEmpty(@Nullable JsonElement element) {
/* 192 */     return (element == null || element.isJsonNull() || (element.isJsonArray() && element.getAsJsonArray().size() == 0) || (element.isJsonObject() && element.getAsJsonObject().size() == 0));
/*     */   }
/*     */   
/*     */   private boolean readBoolean(JsonReader in) throws IOException {
/* 196 */     JsonToken peek = in.peek();
/* 197 */     if (peek == JsonToken.BOOLEAN)
/* 198 */       return in.nextBoolean(); 
/* 199 */     if (peek == JsonToken.STRING || peek == JsonToken.NUMBER) {
/* 200 */       return Boolean.parseBoolean(in.nextString());
/*     */     }
/* 202 */     throw new JsonParseException("Token of type " + peek + " cannot be interpreted as a boolean");
/*     */   }
/*     */ 
/*     */   
/*     */   private Object legacyHoverEventContents(HoverEvent.Action<?> action, Component rawValue) {
/* 207 */     if (action == HoverEvent.Action.SHOW_TEXT)
/* 208 */       return rawValue; 
/* 209 */     if (this.legacyHover != null) {
/*     */       try {
/* 211 */         if (action == HoverEvent.Action.SHOW_ENTITY)
/* 212 */           return this.legacyHover.deserializeShowEntity(rawValue, decoder()); 
/* 213 */         if (action == HoverEvent.Action.SHOW_ITEM) {
/* 214 */           return this.legacyHover.deserializeShowItem(rawValue);
/*     */         }
/* 216 */       } catch (IOException ex) {
/* 217 */         throw new JsonParseException(ex);
/*     */       } 
/*     */     }
/*     */     
/* 221 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   private Codec.Decoder<Component, String, JsonParseException> decoder() {
/* 225 */     return string -> (Component)this.gson.fromJson(string, SerializerFactory.COMPONENT_TYPE);
/*     */   }
/*     */   
/*     */   private Codec.Encoder<Component, String, JsonParseException> encoder() {
/* 229 */     return component -> this.gson.toJson(component, SerializerFactory.COMPONENT_TYPE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(JsonWriter out, Style value) throws IOException {
/* 234 */     out.beginObject();
/*     */     
/* 236 */     for (int i = 0, length = DECORATIONS.length; i < length; i++) {
/* 237 */       TextDecoration decoration = DECORATIONS[i];
/* 238 */       TextDecoration.State state = value.decoration(decoration);
/* 239 */       if (state != TextDecoration.State.NOT_SET) {
/* 240 */         String name = (String)TextDecoration.NAMES.key(decoration);
/* 241 */         assert name != null;
/* 242 */         out.name(name);
/* 243 */         out.value((state == TextDecoration.State.TRUE));
/*     */       } 
/*     */     } 
/*     */     
/* 247 */     TextColor color = value.color();
/* 248 */     if (color != null) {
/* 249 */       out.name("color");
/* 250 */       this.gson.toJson(color, SerializerFactory.COLOR_TYPE, out);
/*     */     } 
/*     */     
/* 253 */     String insertion = value.insertion();
/* 254 */     if (insertion != null) {
/* 255 */       out.name("insertion");
/* 256 */       out.value(insertion);
/*     */     } 
/*     */     
/* 259 */     ClickEvent clickEvent = value.clickEvent();
/* 260 */     if (clickEvent != null) {
/* 261 */       out.name("clickEvent");
/* 262 */       out.beginObject();
/* 263 */       out.name("action");
/* 264 */       this.gson.toJson(clickEvent.action(), SerializerFactory.CLICK_ACTION_TYPE, out);
/* 265 */       out.name("value");
/* 266 */       out.value(clickEvent.value());
/* 267 */       out.endObject();
/*     */     } 
/*     */     
/* 270 */     HoverEvent<?> hoverEvent = value.hoverEvent();
/* 271 */     if (hoverEvent != null && (hoverEvent.action() != HoverEvent.Action.SHOW_ACHIEVEMENT || this.emitLegacyHover)) {
/* 272 */       out.name("hoverEvent");
/* 273 */       out.beginObject();
/* 274 */       out.name("action");
/* 275 */       HoverEvent.Action<?> action = hoverEvent.action();
/* 276 */       this.gson.toJson(action, SerializerFactory.HOVER_ACTION_TYPE, out);
/* 277 */       if (action != HoverEvent.Action.SHOW_ACHIEVEMENT) {
/* 278 */         out.name("contents");
/* 279 */         if (action == HoverEvent.Action.SHOW_ITEM) {
/* 280 */           this.gson.toJson(hoverEvent.value(), SerializerFactory.SHOW_ITEM_TYPE, out);
/* 281 */         } else if (action == HoverEvent.Action.SHOW_ENTITY) {
/* 282 */           this.gson.toJson(hoverEvent.value(), SerializerFactory.SHOW_ENTITY_TYPE, out);
/* 283 */         } else if (action == HoverEvent.Action.SHOW_TEXT) {
/* 284 */           this.gson.toJson(hoverEvent.value(), SerializerFactory.COMPONENT_TYPE, out);
/*     */         } else {
/* 286 */           throw new JsonParseException("Don't know how to serialize " + hoverEvent.value());
/*     */         } 
/*     */       } 
/* 289 */       if (this.emitLegacyHover) {
/* 290 */         out.name("value");
/* 291 */         serializeLegacyHoverEvent(hoverEvent, out);
/*     */       } 
/*     */       
/* 294 */       out.endObject();
/*     */     } 
/*     */     
/* 297 */     Key font = value.font();
/* 298 */     if (font != null) {
/* 299 */       out.name("font");
/* 300 */       this.gson.toJson(font, SerializerFactory.KEY_TYPE, out);
/*     */     } 
/*     */     
/* 303 */     out.endObject();
/*     */   }
/*     */   
/*     */   private void serializeLegacyHoverEvent(HoverEvent<?> hoverEvent, JsonWriter out) throws IOException {
/* 307 */     if (hoverEvent.action() == HoverEvent.Action.SHOW_TEXT) {
/* 308 */       this.gson.toJson(hoverEvent.value(), SerializerFactory.COMPONENT_TYPE, out);
/* 309 */     } else if (hoverEvent.action() == HoverEvent.Action.SHOW_ACHIEVEMENT) {
/* 310 */       this.gson.toJson(hoverEvent.value(), String.class, out);
/* 311 */     } else if (this.legacyHover != null) {
/* 312 */       Component serialized = null;
/*     */       try {
/* 314 */         if (hoverEvent.action() == HoverEvent.Action.SHOW_ENTITY) {
/* 315 */           serialized = this.legacyHover.serializeShowEntity((HoverEvent.ShowEntity)hoverEvent.value(), encoder());
/* 316 */         } else if (hoverEvent.action() == HoverEvent.Action.SHOW_ITEM) {
/* 317 */           serialized = this.legacyHover.serializeShowItem((HoverEvent.ShowItem)hoverEvent.value());
/*     */         } 
/* 319 */       } catch (IOException ex) {
/* 320 */         throw new JsonSyntaxException(ex);
/*     */       } 
/* 322 */       if (serialized != null) {
/* 323 */         this.gson.toJson(serialized, SerializerFactory.COMPONENT_TYPE, out);
/*     */       } else {
/* 325 */         out.nullValue();
/*     */       } 
/*     */     } else {
/* 328 */       out.nullValue();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\text\serializer\gson\StyleSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */