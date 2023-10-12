/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Arrays;
/*     */ import java.util.IllegalFormatException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class ChatComponentTranslation
/*     */   extends ChatComponentStyle
/*     */ {
/*     */   private final String key;
/*     */   private final Object[] formatArgs;
/*  17 */   private final Object syncLock = new Object();
/*  18 */   private long lastTranslationUpdateTimeInMilliseconds = -1L;
/*  19 */   List<IChatComponent> children = Lists.newArrayList();
/*  20 */   public static final Pattern stringVariablePattern = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");
/*     */ 
/*     */   
/*     */   public ChatComponentTranslation(String translationKey, Object... args) {
/*  24 */     this.key = translationKey;
/*  25 */     this.formatArgs = args;
/*     */     
/*  27 */     for (Object object : args) {
/*     */       
/*  29 */       if (object instanceof IChatComponent)
/*     */       {
/*  31 */         ((IChatComponent)object).getChatStyle().setParentStyle(getChatStyle());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   synchronized void ensureInitialized() {
/*  38 */     synchronized (this.syncLock) {
/*     */       
/*  40 */       long i = StatCollector.getLastTranslationUpdateTimeInMilliseconds();
/*     */       
/*  42 */       if (i == this.lastTranslationUpdateTimeInMilliseconds) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/*  47 */       this.lastTranslationUpdateTimeInMilliseconds = i;
/*  48 */       this.children.clear();
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/*  53 */       initializeFromFormat(StatCollector.translateToLocal(this.key));
/*     */     }
/*  55 */     catch (ChatComponentTranslationFormatException chatcomponenttranslationformatexception) {
/*     */       
/*  57 */       this.children.clear();
/*     */ 
/*     */       
/*     */       try {
/*  61 */         initializeFromFormat(StatCollector.translateToFallback(this.key));
/*     */       }
/*  63 */       catch (ChatComponentTranslationFormatException var5) {
/*     */         
/*  65 */         throw chatcomponenttranslationformatexception;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initializeFromFormat(String format) {
/*  72 */     boolean flag = false;
/*  73 */     Matcher matcher = stringVariablePattern.matcher(format);
/*  74 */     int i = 0;
/*  75 */     int j = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  81 */       for (; matcher.find(j); j = l) {
/*     */         
/*  83 */         int k = matcher.start();
/*  84 */         int l = matcher.end();
/*     */         
/*  86 */         if (k > j) {
/*     */           
/*  88 */           ChatComponentText chatcomponenttext = new ChatComponentText(String.format(format.substring(j, k), new Object[0]));
/*  89 */           chatcomponenttext.getChatStyle().setParentStyle(getChatStyle());
/*  90 */           this.children.add(chatcomponenttext);
/*     */         } 
/*     */         
/*  93 */         String s2 = matcher.group(2);
/*  94 */         String s = format.substring(k, l);
/*     */         
/*  96 */         if ("%".equals(s2) && "%%".equals(s)) {
/*     */           
/*  98 */           ChatComponentText chatcomponenttext2 = new ChatComponentText("%");
/*  99 */           chatcomponenttext2.getChatStyle().setParentStyle(getChatStyle());
/* 100 */           this.children.add(chatcomponenttext2);
/*     */         }
/*     */         else {
/*     */           
/* 104 */           if (!"s".equals(s2))
/*     */           {
/* 106 */             throw new ChatComponentTranslationFormatException(this, "Unsupported format: '" + s + "'");
/*     */           }
/*     */           
/* 109 */           String s1 = matcher.group(1);
/* 110 */           int i1 = (s1 != null) ? (Integer.parseInt(s1) - 1) : i++;
/*     */           
/* 112 */           if (i1 < this.formatArgs.length)
/*     */           {
/* 114 */             this.children.add(getFormatArgumentAsComponent(i1));
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 119 */       if (j < format.length())
/*     */       {
/* 121 */         ChatComponentText chatcomponenttext1 = new ChatComponentText(String.format(format.substring(j), new Object[0]));
/* 122 */         chatcomponenttext1.getChatStyle().setParentStyle(getChatStyle());
/* 123 */         this.children.add(chatcomponenttext1);
/*     */       }
/*     */     
/* 126 */     } catch (IllegalFormatException illegalformatexception) {
/*     */       
/* 128 */       throw new ChatComponentTranslationFormatException(this, illegalformatexception);
/*     */     } 
/*     */   }
/*     */   
/*     */   private IChatComponent getFormatArgumentAsComponent(int index) {
/*     */     IChatComponent ichatcomponent;
/* 134 */     if (index >= this.formatArgs.length)
/*     */     {
/* 136 */       throw new ChatComponentTranslationFormatException(this, index);
/*     */     }
/*     */ 
/*     */     
/* 140 */     Object object = this.formatArgs[index];
/*     */ 
/*     */     
/* 143 */     if (object instanceof IChatComponent) {
/*     */       
/* 145 */       ichatcomponent = (IChatComponent)object;
/*     */     }
/*     */     else {
/*     */       
/* 149 */       ichatcomponent = new ChatComponentText((object == null) ? "null" : object.toString());
/* 150 */       ichatcomponent.getChatStyle().setParentStyle(getChatStyle());
/*     */     } 
/*     */     
/* 153 */     return ichatcomponent;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IChatComponent setChatStyle(ChatStyle style) {
/* 159 */     super.setChatStyle(style);
/*     */     
/* 161 */     for (Object object : this.formatArgs) {
/*     */       
/* 163 */       if (object instanceof IChatComponent)
/*     */       {
/* 165 */         ((IChatComponent)object).getChatStyle().setParentStyle(getChatStyle());
/*     */       }
/*     */     } 
/*     */     
/* 169 */     if (this.lastTranslationUpdateTimeInMilliseconds > -1L)
/*     */     {
/* 171 */       for (IChatComponent ichatcomponent : this.children)
/*     */       {
/* 173 */         ichatcomponent.getChatStyle().setParentStyle(style);
/*     */       }
/*     */     }
/*     */     
/* 177 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<IChatComponent> iterator() {
/* 182 */     ensureInitialized();
/* 183 */     return Iterators.concat(createDeepCopyIterator(this.children), createDeepCopyIterator(this.siblings));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUnformattedTextForChat() {
/* 188 */     ensureInitialized();
/* 189 */     StringBuilder stringbuilder = new StringBuilder();
/*     */     
/* 191 */     for (IChatComponent ichatcomponent : this.children)
/*     */     {
/* 193 */       stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
/*     */     }
/*     */     
/* 196 */     return stringbuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ChatComponentTranslation createCopy() {
/* 201 */     Object[] aobject = new Object[this.formatArgs.length];
/*     */     
/* 203 */     for (int i = 0; i < this.formatArgs.length; i++) {
/*     */       
/* 205 */       if (this.formatArgs[i] instanceof IChatComponent) {
/*     */         
/* 207 */         aobject[i] = ((IChatComponent)this.formatArgs[i]).createCopy();
/*     */       }
/*     */       else {
/*     */         
/* 211 */         aobject[i] = this.formatArgs[i];
/*     */       } 
/*     */     } 
/*     */     
/* 215 */     ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(this.key, aobject);
/* 216 */     chatcomponenttranslation.setChatStyle(getChatStyle().createShallowCopy());
/*     */     
/* 218 */     for (IChatComponent ichatcomponent : getSiblings())
/*     */     {
/* 220 */       chatcomponenttranslation.appendSibling(ichatcomponent.createCopy());
/*     */     }
/*     */     
/* 223 */     return chatcomponenttranslation;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object p_equals_1_) {
/* 228 */     if (this == p_equals_1_)
/*     */     {
/* 230 */       return true;
/*     */     }
/* 232 */     if (!(p_equals_1_ instanceof ChatComponentTranslation))
/*     */     {
/* 234 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 238 */     ChatComponentTranslation chatcomponenttranslation = (ChatComponentTranslation)p_equals_1_;
/* 239 */     return (Arrays.equals(this.formatArgs, chatcomponenttranslation.formatArgs) && this.key.equals(chatcomponenttranslation.key) && super.equals(p_equals_1_));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 245 */     int i = super.hashCode();
/* 246 */     i = 31 * i + this.key.hashCode();
/* 247 */     i = 31 * i + Arrays.hashCode(this.formatArgs);
/* 248 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 253 */     return "TranslatableComponent{key='" + this.key + '\'' + ", args=" + Arrays.toString(this.formatArgs) + ", siblings=" + this.siblings + ", style=" + getChatStyle() + '}';
/*     */   }
/*     */ 
/*     */   
/*     */   public String getKey() {
/* 258 */     return this.key;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] getFormatArgs() {
/* 263 */     return this.formatArgs;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\ChatComponentTranslation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */