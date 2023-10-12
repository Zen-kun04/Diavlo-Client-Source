/*     */ package net.minecraft.util;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import net.minecraft.event.ClickEvent;
/*     */ import net.minecraft.event.HoverEvent;
/*     */ 
/*     */ public class ChatStyle {
/*     */   private ChatStyle parentStyle;
/*     */   private EnumChatFormatting color;
/*     */   private Boolean bold;
/*     */   private Boolean italic;
/*     */   private Boolean underlined;
/*     */   private Boolean strikethrough;
/*     */   private Boolean obfuscated;
/*     */   private ClickEvent chatClickEvent;
/*     */   private HoverEvent chatHoverEvent;
/*     */   private String insertion;
/*     */   
/*  21 */   private static final ChatStyle rootStyle = new ChatStyle()
/*     */     {
/*     */       public EnumChatFormatting getColor()
/*     */       {
/*  25 */         return null;
/*     */       }
/*     */       
/*     */       public boolean getBold() {
/*  29 */         return false;
/*     */       }
/*     */       
/*     */       public boolean getItalic() {
/*  33 */         return false;
/*     */       }
/*     */       
/*     */       public boolean getStrikethrough() {
/*  37 */         return false;
/*     */       }
/*     */       
/*     */       public boolean getUnderlined() {
/*  41 */         return false;
/*     */       }
/*     */       
/*     */       public boolean getObfuscated() {
/*  45 */         return false;
/*     */       }
/*     */       
/*     */       public ClickEvent getChatClickEvent() {
/*  49 */         return null;
/*     */       }
/*     */       
/*     */       public HoverEvent getChatHoverEvent() {
/*  53 */         return null;
/*     */       }
/*     */       
/*     */       public String getInsertion() {
/*  57 */         return null;
/*     */       }
/*     */       
/*     */       public ChatStyle setColor(EnumChatFormatting color) {
/*  61 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       
/*     */       public ChatStyle setBold(Boolean boldIn) {
/*  65 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       
/*     */       public ChatStyle setItalic(Boolean italic) {
/*  69 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       
/*     */       public ChatStyle setStrikethrough(Boolean strikethrough) {
/*  73 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       
/*     */       public ChatStyle setUnderlined(Boolean underlined) {
/*  77 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       
/*     */       public ChatStyle setObfuscated(Boolean obfuscated) {
/*  81 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       
/*     */       public ChatStyle setChatClickEvent(ClickEvent event) {
/*  85 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       
/*     */       public ChatStyle setChatHoverEvent(HoverEvent event) {
/*  89 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       
/*     */       public ChatStyle setParentStyle(ChatStyle parent) {
/*  93 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       
/*     */       public String toString() {
/*  97 */         return "Style.ROOT";
/*     */       }
/*     */       
/*     */       public ChatStyle createShallowCopy() {
/* 101 */         return this;
/*     */       }
/*     */       
/*     */       public ChatStyle createDeepCopy() {
/* 105 */         return this;
/*     */       }
/*     */       
/*     */       public String getFormattingCode() {
/* 109 */         return "";
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   public EnumChatFormatting getColor() {
/* 115 */     return (this.color == null) ? getParent().getColor() : this.color;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getBold() {
/* 120 */     return (this.bold == null) ? getParent().getBold() : this.bold.booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getItalic() {
/* 125 */     return (this.italic == null) ? getParent().getItalic() : this.italic.booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getStrikethrough() {
/* 130 */     return (this.strikethrough == null) ? getParent().getStrikethrough() : this.strikethrough.booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getUnderlined() {
/* 135 */     return (this.underlined == null) ? getParent().getUnderlined() : this.underlined.booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getObfuscated() {
/* 140 */     return (this.obfuscated == null) ? getParent().getObfuscated() : this.obfuscated.booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 145 */     return (this.bold == null && this.italic == null && this.strikethrough == null && this.underlined == null && this.obfuscated == null && this.color == null && this.chatClickEvent == null && this.chatHoverEvent == null);
/*     */   }
/*     */ 
/*     */   
/*     */   public ClickEvent getChatClickEvent() {
/* 150 */     return (this.chatClickEvent == null) ? getParent().getChatClickEvent() : this.chatClickEvent;
/*     */   }
/*     */ 
/*     */   
/*     */   public HoverEvent getChatHoverEvent() {
/* 155 */     return (this.chatHoverEvent == null) ? getParent().getChatHoverEvent() : this.chatHoverEvent;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getInsertion() {
/* 160 */     return (this.insertion == null) ? getParent().getInsertion() : this.insertion;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChatStyle setColor(EnumChatFormatting color) {
/* 165 */     this.color = color;
/* 166 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChatStyle setBold(Boolean boldIn) {
/* 171 */     this.bold = boldIn;
/* 172 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChatStyle setItalic(Boolean italic) {
/* 177 */     this.italic = italic;
/* 178 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChatStyle setStrikethrough(Boolean strikethrough) {
/* 183 */     this.strikethrough = strikethrough;
/* 184 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChatStyle setUnderlined(Boolean underlined) {
/* 189 */     this.underlined = underlined;
/* 190 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChatStyle setObfuscated(Boolean obfuscated) {
/* 195 */     this.obfuscated = obfuscated;
/* 196 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChatStyle setChatClickEvent(ClickEvent event) {
/* 201 */     this.chatClickEvent = event;
/* 202 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChatStyle setChatHoverEvent(HoverEvent event) {
/* 207 */     this.chatHoverEvent = event;
/* 208 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChatStyle setInsertion(String insertion) {
/* 213 */     this.insertion = insertion;
/* 214 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChatStyle setParentStyle(ChatStyle parent) {
/* 219 */     this.parentStyle = parent;
/* 220 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFormattingCode() {
/* 225 */     if (isEmpty())
/*     */     {
/* 227 */       return (this.parentStyle != null) ? this.parentStyle.getFormattingCode() : "";
/*     */     }
/*     */ 
/*     */     
/* 231 */     StringBuilder stringbuilder = new StringBuilder();
/*     */     
/* 233 */     if (getColor() != null)
/*     */     {
/* 235 */       stringbuilder.append(getColor());
/*     */     }
/*     */     
/* 238 */     if (getBold())
/*     */     {
/* 240 */       stringbuilder.append(EnumChatFormatting.BOLD);
/*     */     }
/*     */     
/* 243 */     if (getItalic())
/*     */     {
/* 245 */       stringbuilder.append(EnumChatFormatting.ITALIC);
/*     */     }
/*     */     
/* 248 */     if (getUnderlined())
/*     */     {
/* 250 */       stringbuilder.append(EnumChatFormatting.UNDERLINE);
/*     */     }
/*     */     
/* 253 */     if (getObfuscated())
/*     */     {
/* 255 */       stringbuilder.append(EnumChatFormatting.OBFUSCATED);
/*     */     }
/*     */     
/* 258 */     if (getStrikethrough())
/*     */     {
/* 260 */       stringbuilder.append(EnumChatFormatting.STRIKETHROUGH);
/*     */     }
/*     */     
/* 263 */     return stringbuilder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ChatStyle getParent() {
/* 269 */     return (this.parentStyle == null) ? rootStyle : this.parentStyle;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 274 */     return "Style{hasParent=" + ((this.parentStyle != null) ? 1 : 0) + ", color=" + this.color + ", bold=" + this.bold + ", italic=" + this.italic + ", underlined=" + this.underlined + ", obfuscated=" + this.obfuscated + ", clickEvent=" + getChatClickEvent() + ", hoverEvent=" + getChatHoverEvent() + ", insertion=" + getInsertion() + '}';
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object p_equals_1_) {
/* 279 */     if (this == p_equals_1_)
/*     */     {
/* 281 */       return true;
/*     */     }
/* 283 */     if (!(p_equals_1_ instanceof ChatStyle))
/*     */     {
/* 285 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 292 */     ChatStyle chatstyle = (ChatStyle)p_equals_1_;
/*     */     
/* 294 */     if (getBold() == chatstyle.getBold() && getColor() == chatstyle.getColor() && getItalic() == chatstyle.getItalic() && getObfuscated() == chatstyle.getObfuscated() && getStrikethrough() == chatstyle.getStrikethrough() && getUnderlined() == chatstyle.getUnderlined()) {
/*     */ 
/*     */ 
/*     */       
/* 298 */       if (getChatClickEvent() != null)
/*     */       
/* 300 */       { if (!getChatClickEvent().equals(chatstyle.getChatClickEvent()))
/*     */         
/*     */         { 
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
/* 336 */           boolean flag = false;
/* 337 */           return flag; }  } else if (chatstyle.getChatClickEvent() != null) { return false; }  if (getChatHoverEvent() != null) { if (!getChatHoverEvent().equals(chatstyle.getChatHoverEvent()))
/*     */           return false;  } else if (chatstyle.getChatHoverEvent() != null) { return false; }
/* 339 */        if (getInsertion() != null) { if (getInsertion().equals(chatstyle.getInsertion())) { boolean flag = true;
/* 340 */           return flag; }
/*     */          }
/*     */       else if (chatstyle.getInsertion() == null)
/*     */       { return true; }
/*     */     
/*     */     } 
/* 346 */     return false; } public int hashCode() { int i = this.color.hashCode();
/* 347 */     i = 31 * i + this.bold.hashCode();
/* 348 */     i = 31 * i + this.italic.hashCode();
/* 349 */     i = 31 * i + this.underlined.hashCode();
/* 350 */     i = 31 * i + this.strikethrough.hashCode();
/* 351 */     i = 31 * i + this.obfuscated.hashCode();
/* 352 */     i = 31 * i + this.chatClickEvent.hashCode();
/* 353 */     i = 31 * i + this.chatHoverEvent.hashCode();
/* 354 */     i = 31 * i + this.insertion.hashCode();
/* 355 */     return i; }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChatStyle createShallowCopy() {
/* 360 */     ChatStyle chatstyle = new ChatStyle();
/* 361 */     chatstyle.bold = this.bold;
/* 362 */     chatstyle.italic = this.italic;
/* 363 */     chatstyle.strikethrough = this.strikethrough;
/* 364 */     chatstyle.underlined = this.underlined;
/* 365 */     chatstyle.obfuscated = this.obfuscated;
/* 366 */     chatstyle.color = this.color;
/* 367 */     chatstyle.chatClickEvent = this.chatClickEvent;
/* 368 */     chatstyle.chatHoverEvent = this.chatHoverEvent;
/* 369 */     chatstyle.parentStyle = this.parentStyle;
/* 370 */     chatstyle.insertion = this.insertion;
/* 371 */     return chatstyle;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChatStyle createDeepCopy() {
/* 376 */     ChatStyle chatstyle = new ChatStyle();
/* 377 */     chatstyle.setBold(Boolean.valueOf(getBold()));
/* 378 */     chatstyle.setItalic(Boolean.valueOf(getItalic()));
/* 379 */     chatstyle.setStrikethrough(Boolean.valueOf(getStrikethrough()));
/* 380 */     chatstyle.setUnderlined(Boolean.valueOf(getUnderlined()));
/* 381 */     chatstyle.setObfuscated(Boolean.valueOf(getObfuscated()));
/* 382 */     chatstyle.setColor(getColor());
/* 383 */     chatstyle.setChatClickEvent(getChatClickEvent());
/* 384 */     chatstyle.setChatHoverEvent(getChatHoverEvent());
/* 385 */     chatstyle.setInsertion(getInsertion());
/* 386 */     return chatstyle;
/*     */   }
/*     */   
/*     */   public static class Serializer
/*     */     implements JsonDeserializer<ChatStyle>, JsonSerializer<ChatStyle>
/*     */   {
/*     */     public ChatStyle deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
/* 393 */       if (p_deserialize_1_.isJsonObject()) {
/*     */         
/* 395 */         ChatStyle chatstyle = new ChatStyle();
/* 396 */         JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
/*     */         
/* 398 */         if (jsonobject == null)
/*     */         {
/* 400 */           return null;
/*     */         }
/*     */ 
/*     */         
/* 404 */         if (jsonobject.has("bold"))
/*     */         {
/* 406 */           chatstyle.bold = Boolean.valueOf(jsonobject.get("bold").getAsBoolean());
/*     */         }
/*     */         
/* 409 */         if (jsonobject.has("italic"))
/*     */         {
/* 411 */           chatstyle.italic = Boolean.valueOf(jsonobject.get("italic").getAsBoolean());
/*     */         }
/*     */         
/* 414 */         if (jsonobject.has("underlined"))
/*     */         {
/* 416 */           chatstyle.underlined = Boolean.valueOf(jsonobject.get("underlined").getAsBoolean());
/*     */         }
/*     */         
/* 419 */         if (jsonobject.has("strikethrough"))
/*     */         {
/* 421 */           chatstyle.strikethrough = Boolean.valueOf(jsonobject.get("strikethrough").getAsBoolean());
/*     */         }
/*     */         
/* 424 */         if (jsonobject.has("obfuscated"))
/*     */         {
/* 426 */           chatstyle.obfuscated = Boolean.valueOf(jsonobject.get("obfuscated").getAsBoolean());
/*     */         }
/*     */         
/* 429 */         if (jsonobject.has("color"))
/*     */         {
/* 431 */           chatstyle.color = (EnumChatFormatting)p_deserialize_3_.deserialize(jsonobject.get("color"), EnumChatFormatting.class);
/*     */         }
/*     */         
/* 434 */         if (jsonobject.has("insertion"))
/*     */         {
/* 436 */           chatstyle.insertion = jsonobject.get("insertion").getAsString();
/*     */         }
/*     */         
/* 439 */         if (jsonobject.has("clickEvent")) {
/*     */           
/* 441 */           JsonObject jsonobject1 = jsonobject.getAsJsonObject("clickEvent");
/*     */           
/* 443 */           if (jsonobject1 != null) {
/*     */             
/* 445 */             JsonPrimitive jsonprimitive = jsonobject1.getAsJsonPrimitive("action");
/* 446 */             ClickEvent.Action clickevent$action = (jsonprimitive == null) ? null : ClickEvent.Action.getValueByCanonicalName(jsonprimitive.getAsString());
/* 447 */             JsonPrimitive jsonprimitive1 = jsonobject1.getAsJsonPrimitive("value");
/* 448 */             String s = (jsonprimitive1 == null) ? null : jsonprimitive1.getAsString();
/*     */             
/* 450 */             if (clickevent$action != null && s != null && clickevent$action.shouldAllowInChat())
/*     */             {
/* 452 */               chatstyle.chatClickEvent = new ClickEvent(clickevent$action, s);
/*     */             }
/*     */           } 
/*     */         } 
/*     */         
/* 457 */         if (jsonobject.has("hoverEvent")) {
/*     */           
/* 459 */           JsonObject jsonobject2 = jsonobject.getAsJsonObject("hoverEvent");
/*     */           
/* 461 */           if (jsonobject2 != null) {
/*     */             
/* 463 */             JsonPrimitive jsonprimitive2 = jsonobject2.getAsJsonPrimitive("action");
/* 464 */             HoverEvent.Action hoverevent$action = (jsonprimitive2 == null) ? null : HoverEvent.Action.getValueByCanonicalName(jsonprimitive2.getAsString());
/* 465 */             IChatComponent ichatcomponent = (IChatComponent)p_deserialize_3_.deserialize(jsonobject2.get("value"), IChatComponent.class);
/*     */             
/* 467 */             if (hoverevent$action != null && ichatcomponent != null && hoverevent$action.shouldAllowInChat())
/*     */             {
/* 469 */               chatstyle.chatHoverEvent = new HoverEvent(hoverevent$action, ichatcomponent);
/*     */             }
/*     */           } 
/*     */         } 
/*     */         
/* 474 */         return chatstyle;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 479 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonElement serialize(ChatStyle p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
/* 485 */       if (p_serialize_1_.isEmpty())
/*     */       {
/* 487 */         return null;
/*     */       }
/*     */ 
/*     */       
/* 491 */       JsonObject jsonobject = new JsonObject();
/*     */       
/* 493 */       if (p_serialize_1_.bold != null)
/*     */       {
/* 495 */         jsonobject.addProperty("bold", p_serialize_1_.bold);
/*     */       }
/*     */       
/* 498 */       if (p_serialize_1_.italic != null)
/*     */       {
/* 500 */         jsonobject.addProperty("italic", p_serialize_1_.italic);
/*     */       }
/*     */       
/* 503 */       if (p_serialize_1_.underlined != null)
/*     */       {
/* 505 */         jsonobject.addProperty("underlined", p_serialize_1_.underlined);
/*     */       }
/*     */       
/* 508 */       if (p_serialize_1_.strikethrough != null)
/*     */       {
/* 510 */         jsonobject.addProperty("strikethrough", p_serialize_1_.strikethrough);
/*     */       }
/*     */       
/* 513 */       if (p_serialize_1_.obfuscated != null)
/*     */       {
/* 515 */         jsonobject.addProperty("obfuscated", p_serialize_1_.obfuscated);
/*     */       }
/*     */       
/* 518 */       if (p_serialize_1_.color != null)
/*     */       {
/* 520 */         jsonobject.add("color", p_serialize_3_.serialize(p_serialize_1_.color));
/*     */       }
/*     */       
/* 523 */       if (p_serialize_1_.insertion != null)
/*     */       {
/* 525 */         jsonobject.add("insertion", p_serialize_3_.serialize(p_serialize_1_.insertion));
/*     */       }
/*     */       
/* 528 */       if (p_serialize_1_.chatClickEvent != null) {
/*     */         
/* 530 */         JsonObject jsonobject1 = new JsonObject();
/* 531 */         jsonobject1.addProperty("action", p_serialize_1_.chatClickEvent.getAction().getCanonicalName());
/* 532 */         jsonobject1.addProperty("value", p_serialize_1_.chatClickEvent.getValue());
/* 533 */         jsonobject.add("clickEvent", (JsonElement)jsonobject1);
/*     */       } 
/*     */       
/* 536 */       if (p_serialize_1_.chatHoverEvent != null) {
/*     */         
/* 538 */         JsonObject jsonobject2 = new JsonObject();
/* 539 */         jsonobject2.addProperty("action", p_serialize_1_.chatHoverEvent.getAction().getCanonicalName());
/* 540 */         jsonobject2.add("value", p_serialize_3_.serialize(p_serialize_1_.chatHoverEvent.getValue()));
/* 541 */         jsonobject.add("hoverEvent", (JsonElement)jsonobject2);
/*     */       } 
/*     */       
/* 544 */       return (JsonElement)jsonobject;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\ChatStyle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */