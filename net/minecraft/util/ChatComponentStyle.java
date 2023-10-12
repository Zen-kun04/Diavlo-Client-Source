/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public abstract class ChatComponentStyle
/*     */   implements IChatComponent
/*     */ {
/*  12 */   protected List<IChatComponent> siblings = Lists.newArrayList();
/*     */   
/*     */   private ChatStyle style;
/*     */   
/*     */   public IChatComponent appendSibling(IChatComponent component) {
/*  17 */     component.getChatStyle().setParentStyle(getChatStyle());
/*  18 */     this.siblings.add(component);
/*  19 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<IChatComponent> getSiblings() {
/*  24 */     return this.siblings;
/*     */   }
/*     */ 
/*     */   
/*     */   public IChatComponent appendText(String text) {
/*  29 */     return appendSibling(new ChatComponentText(text));
/*     */   }
/*     */ 
/*     */   
/*     */   public IChatComponent setChatStyle(ChatStyle style) {
/*  34 */     this.style = style;
/*     */     
/*  36 */     for (IChatComponent ichatcomponent : this.siblings)
/*     */     {
/*  38 */       ichatcomponent.getChatStyle().setParentStyle(getChatStyle());
/*     */     }
/*     */     
/*  41 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChatStyle getChatStyle() {
/*  46 */     if (this.style == null) {
/*     */       
/*  48 */       this.style = new ChatStyle();
/*     */       
/*  50 */       for (IChatComponent ichatcomponent : this.siblings)
/*     */       {
/*  52 */         ichatcomponent.getChatStyle().setParentStyle(this.style);
/*     */       }
/*     */     } 
/*     */     
/*  56 */     return this.style;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<IChatComponent> iterator() {
/*  61 */     return Iterators.concat((Iterator)Iterators.forArray((Object[])new ChatComponentStyle[] { this }, ), createDeepCopyIterator(this.siblings));
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getUnformattedText() {
/*  66 */     StringBuilder stringbuilder = new StringBuilder();
/*     */     
/*  68 */     for (IChatComponent ichatcomponent : this)
/*     */     {
/*  70 */       stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
/*     */     }
/*     */     
/*  73 */     return stringbuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getFormattedText() {
/*  78 */     StringBuilder stringbuilder = new StringBuilder();
/*     */     
/*  80 */     for (IChatComponent ichatcomponent : this) {
/*     */       
/*  82 */       stringbuilder.append(ichatcomponent.getChatStyle().getFormattingCode());
/*  83 */       stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
/*  84 */       stringbuilder.append(EnumChatFormatting.RESET);
/*     */     } 
/*     */     
/*  87 */     return stringbuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static Iterator<IChatComponent> createDeepCopyIterator(Iterable<IChatComponent> components) {
/*  92 */     Iterator<IChatComponent> iterator = Iterators.concat(Iterators.transform(components.iterator(), new Function<IChatComponent, Iterator<IChatComponent>>()
/*     */           {
/*     */             public Iterator<IChatComponent> apply(IChatComponent p_apply_1_)
/*     */             {
/*  96 */               return p_apply_1_.iterator();
/*     */             }
/*     */           }));
/*  99 */     iterator = Iterators.transform(iterator, new Function<IChatComponent, IChatComponent>()
/*     */         {
/*     */           public IChatComponent apply(IChatComponent p_apply_1_)
/*     */           {
/* 103 */             IChatComponent ichatcomponent = p_apply_1_.createCopy();
/* 104 */             ichatcomponent.setChatStyle(ichatcomponent.getChatStyle().createDeepCopy());
/* 105 */             return ichatcomponent;
/*     */           }
/*     */         });
/* 108 */     return iterator;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object p_equals_1_) {
/* 113 */     if (this == p_equals_1_)
/*     */     {
/* 115 */       return true;
/*     */     }
/* 117 */     if (!(p_equals_1_ instanceof ChatComponentStyle))
/*     */     {
/* 119 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 123 */     ChatComponentStyle chatcomponentstyle = (ChatComponentStyle)p_equals_1_;
/* 124 */     return (this.siblings.equals(chatcomponentstyle.siblings) && getChatStyle().equals(chatcomponentstyle.getChatStyle()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 130 */     return 31 * this.style.hashCode() + this.siblings.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 135 */     return "BaseComponent{style=" + this.style + ", siblings=" + this.siblings + '}';
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\ChatComponentStyle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */