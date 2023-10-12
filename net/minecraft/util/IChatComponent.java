/*     */ package net.minecraft.util;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Map;
/*     */ 
/*     */ public interface IChatComponent extends Iterable<IChatComponent> {
/*     */   IChatComponent setChatStyle(ChatStyle paramChatStyle);
/*     */   
/*     */   ChatStyle getChatStyle();
/*     */   
/*     */   IChatComponent appendText(String paramString);
/*     */   
/*     */   IChatComponent appendSibling(IChatComponent paramIChatComponent);
/*     */   
/*     */   String getUnformattedTextForChat();
/*     */   
/*     */   String getUnformattedText();
/*     */   
/*     */   String getFormattedText();
/*     */   
/*     */   List<IChatComponent> getSiblings();
/*     */   
/*     */   IChatComponent createCopy();
/*     */   
/*     */   public static class Serializer implements JsonDeserializer<IChatComponent>, JsonSerializer<IChatComponent> {
/*     */     public IChatComponent deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
/*     */       IChatComponent ichatcomponent;
/*  35 */       if (p_deserialize_1_.isJsonPrimitive())
/*     */       {
/*  37 */         return new ChatComponentText(p_deserialize_1_.getAsString());
/*     */       }
/*  39 */       if (!p_deserialize_1_.isJsonObject()) {
/*     */         
/*  41 */         if (p_deserialize_1_.isJsonArray()) {
/*     */           
/*  43 */           JsonArray jsonarray1 = p_deserialize_1_.getAsJsonArray();
/*  44 */           IChatComponent ichatcomponent1 = null;
/*     */           
/*  46 */           for (JsonElement jsonelement : jsonarray1) {
/*     */             
/*  48 */             IChatComponent ichatcomponent2 = deserialize(jsonelement, jsonelement.getClass(), p_deserialize_3_);
/*     */             
/*  50 */             if (ichatcomponent1 == null) {
/*     */               
/*  52 */               ichatcomponent1 = ichatcomponent2;
/*     */               
/*     */               continue;
/*     */             } 
/*  56 */             ichatcomponent1.appendSibling(ichatcomponent2);
/*     */           } 
/*     */ 
/*     */           
/*  60 */           return ichatcomponent1;
/*     */         } 
/*     */ 
/*     */         
/*  64 */         throw new JsonParseException("Don't know how to turn " + p_deserialize_1_.toString() + " into a Component");
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*  69 */       JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
/*     */ 
/*     */       
/*  72 */       if (jsonobject.has("text")) {
/*     */         
/*  74 */         ichatcomponent = new ChatComponentText(jsonobject.get("text").getAsString());
/*     */       }
/*  76 */       else if (jsonobject.has("translate")) {
/*     */         
/*  78 */         String s = jsonobject.get("translate").getAsString();
/*     */         
/*  80 */         if (jsonobject.has("with"))
/*     */         {
/*  82 */           JsonArray jsonarray = jsonobject.getAsJsonArray("with");
/*  83 */           Object[] aobject = new Object[jsonarray.size()];
/*     */           
/*  85 */           for (int i = 0; i < aobject.length; i++) {
/*     */             
/*  87 */             aobject[i] = deserialize(jsonarray.get(i), p_deserialize_2_, p_deserialize_3_);
/*     */             
/*  89 */             if (aobject[i] instanceof ChatComponentText) {
/*     */               
/*  91 */               ChatComponentText chatcomponenttext = (ChatComponentText)aobject[i];
/*     */               
/*  93 */               if (chatcomponenttext.getChatStyle().isEmpty() && chatcomponenttext.getSiblings().isEmpty())
/*     */               {
/*  95 */                 aobject[i] = chatcomponenttext.getChatComponentText_TextValue();
/*     */               }
/*     */             } 
/*     */           } 
/*     */           
/* 100 */           ichatcomponent = new ChatComponentTranslation(s, aobject);
/*     */         }
/*     */         else
/*     */         {
/* 104 */           ichatcomponent = new ChatComponentTranslation(s, new Object[0]);
/*     */         }
/*     */       
/* 107 */       } else if (jsonobject.has("score")) {
/*     */         
/* 109 */         JsonObject jsonobject1 = jsonobject.getAsJsonObject("score");
/*     */         
/* 111 */         if (!jsonobject1.has("name") || !jsonobject1.has("objective"))
/*     */         {
/* 113 */           throw new JsonParseException("A score component needs a least a name and an objective");
/*     */         }
/*     */         
/* 116 */         ichatcomponent = new ChatComponentScore(JsonUtils.getString(jsonobject1, "name"), JsonUtils.getString(jsonobject1, "objective"));
/*     */         
/* 118 */         if (jsonobject1.has("value"))
/*     */         {
/* 120 */           ((ChatComponentScore)ichatcomponent).setValue(JsonUtils.getString(jsonobject1, "value"));
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 125 */         if (!jsonobject.has("selector"))
/*     */         {
/* 127 */           throw new JsonParseException("Don't know how to turn " + p_deserialize_1_.toString() + " into a Component");
/*     */         }
/*     */         
/* 130 */         ichatcomponent = new ChatComponentSelector(JsonUtils.getString(jsonobject, "selector"));
/*     */       } 
/*     */       
/* 133 */       if (jsonobject.has("extra")) {
/*     */         
/* 135 */         JsonArray jsonarray2 = jsonobject.getAsJsonArray("extra");
/*     */         
/* 137 */         if (jsonarray2.size() <= 0)
/*     */         {
/* 139 */           throw new JsonParseException("Unexpected empty array of components");
/*     */         }
/*     */         
/* 142 */         for (int j = 0; j < jsonarray2.size(); j++)
/*     */         {
/* 144 */           ichatcomponent.appendSibling(deserialize(jsonarray2.get(j), p_deserialize_2_, p_deserialize_3_));
/*     */         }
/*     */       } 
/*     */       
/* 148 */       ichatcomponent.setChatStyle((ChatStyle)p_deserialize_3_.deserialize(p_deserialize_1_, ChatStyle.class));
/* 149 */       return ichatcomponent;
/*     */     }
/*     */     
/*     */     private static final Gson GSON;
/*     */     
/*     */     private void serializeChatStyle(ChatStyle style, JsonObject object, JsonSerializationContext ctx) {
/* 155 */       JsonElement jsonelement = ctx.serialize(style);
/*     */       
/* 157 */       if (jsonelement.isJsonObject()) {
/*     */         
/* 159 */         JsonObject jsonobject = (JsonObject)jsonelement;
/*     */         
/* 161 */         for (Map.Entry<String, JsonElement> entry : (Iterable<Map.Entry<String, JsonElement>>)jsonobject.entrySet())
/*     */         {
/* 163 */           object.add(entry.getKey(), entry.getValue());
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonElement serialize(IChatComponent p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
/* 170 */       if (p_serialize_1_ instanceof ChatComponentText && p_serialize_1_.getChatStyle().isEmpty() && p_serialize_1_.getSiblings().isEmpty())
/*     */       {
/* 172 */         return (JsonElement)new JsonPrimitive(((ChatComponentText)p_serialize_1_).getChatComponentText_TextValue());
/*     */       }
/*     */ 
/*     */       
/* 176 */       JsonObject jsonobject = new JsonObject();
/*     */       
/* 178 */       if (!p_serialize_1_.getChatStyle().isEmpty())
/*     */       {
/* 180 */         serializeChatStyle(p_serialize_1_.getChatStyle(), jsonobject, p_serialize_3_);
/*     */       }
/*     */       
/* 183 */       if (!p_serialize_1_.getSiblings().isEmpty()) {
/*     */         
/* 185 */         JsonArray jsonarray = new JsonArray();
/*     */         
/* 187 */         for (IChatComponent ichatcomponent : p_serialize_1_.getSiblings())
/*     */         {
/* 189 */           jsonarray.add(serialize(ichatcomponent, ichatcomponent.getClass(), p_serialize_3_));
/*     */         }
/*     */         
/* 192 */         jsonobject.add("extra", (JsonElement)jsonarray);
/*     */       } 
/*     */       
/* 195 */       if (p_serialize_1_ instanceof ChatComponentText) {
/*     */         
/* 197 */         jsonobject.addProperty("text", ((ChatComponentText)p_serialize_1_).getChatComponentText_TextValue());
/*     */       }
/* 199 */       else if (p_serialize_1_ instanceof ChatComponentTranslation) {
/*     */         
/* 201 */         ChatComponentTranslation chatcomponenttranslation = (ChatComponentTranslation)p_serialize_1_;
/* 202 */         jsonobject.addProperty("translate", chatcomponenttranslation.getKey());
/*     */         
/* 204 */         if (chatcomponenttranslation.getFormatArgs() != null && (chatcomponenttranslation.getFormatArgs()).length > 0)
/*     */         {
/* 206 */           JsonArray jsonarray1 = new JsonArray();
/*     */           
/* 208 */           for (Object object : chatcomponenttranslation.getFormatArgs()) {
/*     */             
/* 210 */             if (object instanceof IChatComponent) {
/*     */               
/* 212 */               jsonarray1.add(serialize((IChatComponent)object, object.getClass(), p_serialize_3_));
/*     */             }
/*     */             else {
/*     */               
/* 216 */               jsonarray1.add((JsonElement)new JsonPrimitive(String.valueOf(object)));
/*     */             } 
/*     */           } 
/*     */           
/* 220 */           jsonobject.add("with", (JsonElement)jsonarray1);
/*     */         }
/*     */       
/* 223 */       } else if (p_serialize_1_ instanceof ChatComponentScore) {
/*     */         
/* 225 */         ChatComponentScore chatcomponentscore = (ChatComponentScore)p_serialize_1_;
/* 226 */         JsonObject jsonobject1 = new JsonObject();
/* 227 */         jsonobject1.addProperty("name", chatcomponentscore.getName());
/* 228 */         jsonobject1.addProperty("objective", chatcomponentscore.getObjective());
/* 229 */         jsonobject1.addProperty("value", chatcomponentscore.getUnformattedTextForChat());
/* 230 */         jsonobject.add("score", (JsonElement)jsonobject1);
/*     */       }
/*     */       else {
/*     */         
/* 234 */         if (!(p_serialize_1_ instanceof ChatComponentSelector))
/*     */         {
/* 236 */           throw new IllegalArgumentException("Don't know how to serialize " + p_serialize_1_ + " as a Component");
/*     */         }
/*     */         
/* 239 */         ChatComponentSelector chatcomponentselector = (ChatComponentSelector)p_serialize_1_;
/* 240 */         jsonobject.addProperty("selector", chatcomponentselector.getSelector());
/*     */       } 
/*     */       
/* 243 */       return (JsonElement)jsonobject;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public static String componentToJson(IChatComponent component) {
/* 249 */       return GSON.toJson(component);
/*     */     }
/*     */ 
/*     */     
/*     */     public static IChatComponent jsonToComponent(String json) {
/* 254 */       return (IChatComponent)GSON.fromJson(json, IChatComponent.class);
/*     */     }
/*     */ 
/*     */     
/*     */     static {
/* 259 */       GsonBuilder gsonbuilder = new GsonBuilder();
/* 260 */       gsonbuilder.registerTypeHierarchyAdapter(IChatComponent.class, new Serializer());
/* 261 */       gsonbuilder.registerTypeHierarchyAdapter(ChatStyle.class, new ChatStyle.Serializer());
/* 262 */       gsonbuilder.registerTypeAdapterFactory(new EnumTypeAdapterFactory());
/* 263 */       GSON = gsonbuilder.create();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\IChatComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */