/*     */ package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;
/*     */ 
/*     */ import com.viaversion.viaversion.libs.gson.Gson;
/*     */ import com.viaversion.viaversion.libs.gson.JsonElement;
/*     */ import com.viaversion.viaversion.libs.gson.JsonObject;
/*     */ import com.viaversion.viaversion.libs.gson.JsonParseException;
/*     */ import com.viaversion.viaversion.libs.gson.TypeAdapter;
/*     */ import com.viaversion.viaversion.libs.gson.reflect.TypeToken;
/*     */ import com.viaversion.viaversion.libs.gson.stream.JsonReader;
/*     */ import com.viaversion.viaversion.libs.gson.stream.JsonToken;
/*     */ import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.BlockNBTComponent;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.BuildableComponent;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentBuilder;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentLike;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.EntityNBTComponent;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.KeybindComponent;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.NBTComponent;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.ScoreComponent;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.SelectorComponent;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.StorageNBTComponent;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.TextComponent;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.TranslatableComponent;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ComponentSerializerImpl
/*     */   extends TypeAdapter<Component>
/*     */ {
/*  75 */   static final Type COMPONENT_LIST_TYPE = (new TypeToken<List<Component>>() {  }).getType(); private final Gson gson;
/*     */   
/*     */   static TypeAdapter<Component> create(Gson gson) {
/*  78 */     return (new ComponentSerializerImpl(gson)).nullSafe();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ComponentSerializerImpl(Gson gson) {
/*  84 */     this.gson = gson;
/*     */   }
/*     */   public BuildableComponent<?, ?> read(JsonReader in) throws IOException {
/*     */     BuildableComponent<?, ?> buildableComponent;
/*     */     StorageNBTComponent.Builder builder;
/*  89 */     JsonToken token = in.peek();
/*  90 */     if (token == JsonToken.STRING || token == JsonToken.NUMBER || token == JsonToken.BOOLEAN)
/*  91 */       return (BuildableComponent<?, ?>)Component.text(readString(in)); 
/*  92 */     if (token == JsonToken.BEGIN_ARRAY) {
/*  93 */       ComponentBuilder<?, ?> parent = null;
/*  94 */       in.beginArray();
/*  95 */       while (in.hasNext()) {
/*  96 */         BuildableComponent<?, ?> child = read(in);
/*  97 */         if (parent == null) {
/*  98 */           parent = child.toBuilder(); continue;
/*     */         } 
/* 100 */         parent.append((Component)child);
/*     */       } 
/*     */       
/* 103 */       if (parent == null) {
/* 104 */         throw notSureHowToDeserialize(in.getPath());
/*     */       }
/* 106 */       in.endArray();
/* 107 */       return parent.build();
/* 108 */     }  if (token != JsonToken.BEGIN_OBJECT) {
/* 109 */       throw notSureHowToDeserialize(in.getPath());
/*     */     }
/*     */ 
/*     */     
/* 113 */     JsonObject style = new JsonObject();
/* 114 */     List<Component> extra = Collections.emptyList();
/*     */ 
/*     */     
/* 117 */     String text = null;
/* 118 */     String translate = null;
/* 119 */     String translateFallback = null;
/* 120 */     List<Component> translateWith = null;
/* 121 */     String scoreName = null;
/* 122 */     String scoreObjective = null;
/* 123 */     String scoreValue = null;
/* 124 */     String selector = null;
/* 125 */     String keybind = null;
/* 126 */     String nbt = null;
/* 127 */     boolean nbtInterpret = false;
/* 128 */     BlockNBTComponent.Pos nbtBlock = null;
/* 129 */     String nbtEntity = null;
/* 130 */     Key nbtStorage = null;
/* 131 */     Component separator = null;
/*     */     
/* 133 */     in.beginObject();
/* 134 */     while (in.hasNext()) {
/* 135 */       String fieldName = in.nextName();
/* 136 */       if (fieldName.equals("text")) {
/* 137 */         text = readString(in); continue;
/* 138 */       }  if (fieldName.equals("translate")) {
/* 139 */         translate = in.nextString(); continue;
/* 140 */       }  if (fieldName.equals("fallback")) {
/* 141 */         translateFallback = in.nextString(); continue;
/* 142 */       }  if (fieldName.equals("with")) {
/* 143 */         translateWith = (List<Component>)this.gson.fromJson(in, COMPONENT_LIST_TYPE); continue;
/* 144 */       }  if (fieldName.equals("score")) {
/* 145 */         in.beginObject();
/* 146 */         while (in.hasNext()) {
/* 147 */           String scoreFieldName = in.nextName();
/* 148 */           if (scoreFieldName.equals("name")) {
/* 149 */             scoreName = in.nextString(); continue;
/* 150 */           }  if (scoreFieldName.equals("objective")) {
/* 151 */             scoreObjective = in.nextString(); continue;
/* 152 */           }  if (scoreFieldName.equals("value")) {
/* 153 */             scoreValue = in.nextString(); continue;
/*     */           } 
/* 155 */           in.skipValue();
/*     */         } 
/*     */         
/* 158 */         if (scoreName == null || scoreObjective == null) {
/* 159 */           throw new JsonParseException("A score component requires a name and objective");
/*     */         }
/* 161 */         in.endObject(); continue;
/* 162 */       }  if (fieldName.equals("selector")) {
/* 163 */         selector = in.nextString(); continue;
/* 164 */       }  if (fieldName.equals("keybind")) {
/* 165 */         keybind = in.nextString(); continue;
/* 166 */       }  if (fieldName.equals("nbt")) {
/* 167 */         nbt = in.nextString(); continue;
/* 168 */       }  if (fieldName.equals("interpret")) {
/* 169 */         nbtInterpret = in.nextBoolean(); continue;
/* 170 */       }  if (fieldName.equals("block")) {
/* 171 */         nbtBlock = (BlockNBTComponent.Pos)this.gson.fromJson(in, SerializerFactory.BLOCK_NBT_POS_TYPE); continue;
/* 172 */       }  if (fieldName.equals("entity")) {
/* 173 */         nbtEntity = in.nextString(); continue;
/* 174 */       }  if (fieldName.equals("storage")) {
/* 175 */         nbtStorage = (Key)this.gson.fromJson(in, SerializerFactory.KEY_TYPE); continue;
/* 176 */       }  if (fieldName.equals("extra")) {
/* 177 */         extra = (List<Component>)this.gson.fromJson(in, COMPONENT_LIST_TYPE); continue;
/* 178 */       }  if (fieldName.equals("separator")) {
/* 179 */         buildableComponent = read(in); continue;
/*     */       } 
/* 181 */       style.add(fieldName, (JsonElement)this.gson.fromJson(in, JsonElement.class));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 186 */     if (text != null) {
/* 187 */       TextComponent.Builder builder1 = Component.text().content(text);
/* 188 */     } else if (translate != null) {
/* 189 */       if (translateWith != null) {
/* 190 */         TranslatableComponent.Builder builder1 = Component.translatable().key(translate).fallback(translateFallback).args(translateWith);
/*     */       } else {
/* 192 */         TranslatableComponent.Builder builder1 = Component.translatable().key(translate).fallback(translateFallback);
/*     */       } 
/* 194 */     } else if (scoreName != null && scoreObjective != null) {
/* 195 */       if (scoreValue == null) {
/* 196 */         ScoreComponent.Builder builder1 = Component.score().name(scoreName).objective(scoreObjective);
/*     */       } else {
/* 198 */         ScoreComponent.Builder builder1 = Component.score().name(scoreName).objective(scoreObjective).value(scoreValue);
/*     */       } 
/* 200 */     } else if (selector != null) {
/* 201 */       SelectorComponent.Builder builder1 = Component.selector().pattern(selector).separator((ComponentLike)buildableComponent);
/* 202 */     } else if (keybind != null) {
/* 203 */       KeybindComponent.Builder builder1 = Component.keybind().keybind(keybind);
/* 204 */     } else if (nbt != null) {
/* 205 */       if (nbtBlock != null) {
/* 206 */         BlockNBTComponent.Builder builder1 = ((BlockNBTComponent.Builder)nbt(Component.blockNBT(), nbt, nbtInterpret, (Component)buildableComponent)).pos(nbtBlock);
/* 207 */       } else if (nbtEntity != null) {
/* 208 */         EntityNBTComponent.Builder builder1 = ((EntityNBTComponent.Builder)nbt(Component.entityNBT(), nbt, nbtInterpret, (Component)buildableComponent)).selector(nbtEntity);
/* 209 */       } else if (nbtStorage != null) {
/* 210 */         builder = ((StorageNBTComponent.Builder)nbt(Component.storageNBT(), nbt, nbtInterpret, (Component)buildableComponent)).storage(nbtStorage);
/*     */       } else {
/* 212 */         throw notSureHowToDeserialize(in.getPath());
/*     */       } 
/*     */     } else {
/* 215 */       throw notSureHowToDeserialize(in.getPath());
/*     */     } 
/*     */     
/* 218 */     builder.style((Style)this.gson.fromJson((JsonElement)style, SerializerFactory.STYLE_TYPE))
/* 219 */       .append(extra);
/* 220 */     in.endObject();
/* 221 */     return builder.build();
/*     */   }
/*     */   
/*     */   private static String readString(JsonReader in) throws IOException {
/* 225 */     JsonToken peek = in.peek();
/* 226 */     if (peek == JsonToken.STRING || peek == JsonToken.NUMBER)
/* 227 */       return in.nextString(); 
/* 228 */     if (peek == JsonToken.BOOLEAN) {
/* 229 */       return String.valueOf(in.nextBoolean());
/*     */     }
/* 231 */     throw new JsonParseException("Token of type " + peek + " cannot be interpreted as a string");
/*     */   }
/*     */ 
/*     */   
/*     */   private static <C extends NBTComponent<C, B>, B extends com.viaversion.viaversion.libs.kyori.adventure.text.NBTComponentBuilder<C, B>> B nbt(B builder, String nbt, boolean interpret, @Nullable Component separator) {
/* 236 */     return (B)builder
/* 237 */       .nbtPath(nbt)
/* 238 */       .interpret(interpret)
/* 239 */       .separator((ComponentLike)separator);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(JsonWriter out, Component value) throws IOException {
/* 244 */     out.beginObject();
/*     */     
/* 246 */     if (value.hasStyling()) {
/* 247 */       JsonElement style = this.gson.toJsonTree(value.style(), SerializerFactory.STYLE_TYPE);
/* 248 */       if (style.isJsonObject()) {
/* 249 */         for (Map.Entry<String, JsonElement> entry : (Iterable<Map.Entry<String, JsonElement>>)style.getAsJsonObject().entrySet()) {
/* 250 */           out.name(entry.getKey());
/* 251 */           this.gson.toJson(entry.getValue(), out);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 256 */     if (!value.children().isEmpty()) {
/* 257 */       out.name("extra");
/* 258 */       this.gson.toJson(value.children(), COMPONENT_LIST_TYPE, out);
/*     */     } 
/*     */     
/* 261 */     if (value instanceof TextComponent) {
/* 262 */       out.name("text");
/* 263 */       out.value(((TextComponent)value).content());
/* 264 */     } else if (value instanceof TranslatableComponent) {
/* 265 */       TranslatableComponent translatable = (TranslatableComponent)value;
/* 266 */       out.name("translate");
/* 267 */       out.value(translatable.key());
/* 268 */       String fallback = translatable.fallback();
/* 269 */       if (fallback != null) {
/* 270 */         out.name("fallback");
/* 271 */         out.value(fallback);
/*     */       } 
/* 273 */       if (!translatable.args().isEmpty()) {
/* 274 */         out.name("with");
/* 275 */         this.gson.toJson(translatable.args(), COMPONENT_LIST_TYPE, out);
/*     */       } 
/* 277 */     } else if (value instanceof ScoreComponent) {
/* 278 */       ScoreComponent score = (ScoreComponent)value;
/* 279 */       out.name("score");
/* 280 */       out.beginObject();
/* 281 */       out.name("name");
/* 282 */       out.value(score.name());
/* 283 */       out.name("objective");
/* 284 */       out.value(score.objective());
/* 285 */       if (score.value() != null) {
/* 286 */         out.name("value");
/* 287 */         out.value(score.value());
/*     */       } 
/* 289 */       out.endObject();
/* 290 */     } else if (value instanceof SelectorComponent) {
/* 291 */       SelectorComponent selector = (SelectorComponent)value;
/* 292 */       out.name("selector");
/* 293 */       out.value(selector.pattern());
/* 294 */       serializeSeparator(out, selector.separator());
/* 295 */     } else if (value instanceof KeybindComponent) {
/* 296 */       out.name("keybind");
/* 297 */       out.value(((KeybindComponent)value).keybind());
/* 298 */     } else if (value instanceof NBTComponent) {
/* 299 */       NBTComponent<?, ?> nbt = (NBTComponent<?, ?>)value;
/* 300 */       out.name("nbt");
/* 301 */       out.value(nbt.nbtPath());
/* 302 */       out.name("interpret");
/* 303 */       out.value(nbt.interpret());
/* 304 */       serializeSeparator(out, nbt.separator());
/* 305 */       if (value instanceof BlockNBTComponent) {
/* 306 */         out.name("block");
/* 307 */         this.gson.toJson(((BlockNBTComponent)value).pos(), SerializerFactory.BLOCK_NBT_POS_TYPE, out);
/* 308 */       } else if (value instanceof EntityNBTComponent) {
/* 309 */         out.name("entity");
/* 310 */         out.value(((EntityNBTComponent)value).selector());
/* 311 */       } else if (value instanceof StorageNBTComponent) {
/* 312 */         out.name("storage");
/* 313 */         this.gson.toJson(((StorageNBTComponent)value).storage(), SerializerFactory.KEY_TYPE, out);
/*     */       } else {
/* 315 */         throw notSureHowToSerialize(value);
/*     */       } 
/*     */     } else {
/* 318 */       throw notSureHowToSerialize(value);
/*     */     } 
/*     */     
/* 321 */     out.endObject();
/*     */   }
/*     */   
/*     */   private void serializeSeparator(JsonWriter out, @Nullable Component separator) throws IOException {
/* 325 */     if (separator != null) {
/* 326 */       out.name("separator");
/* 327 */       write(out, separator);
/*     */     } 
/*     */   }
/*     */   
/*     */   static JsonParseException notSureHowToDeserialize(Object element) {
/* 332 */     return new JsonParseException("Don't know how to turn " + element + " into a Component");
/*     */   }
/*     */   
/*     */   private static IllegalArgumentException notSureHowToSerialize(Component component) {
/* 336 */     return new IllegalArgumentException("Don't know how to serialize " + component + " as a Component");
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\text\serializer\gson\ComponentSerializerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */