/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class ChatComponentSelector
/*    */   extends ChatComponentStyle
/*    */ {
/*    */   private final String selector;
/*    */   
/*    */   public ChatComponentSelector(String selectorIn) {
/*  9 */     this.selector = selectorIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSelector() {
/* 14 */     return this.selector;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUnformattedTextForChat() {
/* 19 */     return this.selector;
/*    */   }
/*    */ 
/*    */   
/*    */   public ChatComponentSelector createCopy() {
/* 24 */     ChatComponentSelector chatcomponentselector = new ChatComponentSelector(this.selector);
/* 25 */     chatcomponentselector.setChatStyle(getChatStyle().createShallowCopy());
/*    */     
/* 27 */     for (IChatComponent ichatcomponent : getSiblings())
/*    */     {
/* 29 */       chatcomponentselector.appendSibling(ichatcomponent.createCopy());
/*    */     }
/*    */     
/* 32 */     return chatcomponentselector;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object p_equals_1_) {
/* 37 */     if (this == p_equals_1_)
/*    */     {
/* 39 */       return true;
/*    */     }
/* 41 */     if (!(p_equals_1_ instanceof ChatComponentSelector))
/*    */     {
/* 43 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 47 */     ChatComponentSelector chatcomponentselector = (ChatComponentSelector)p_equals_1_;
/* 48 */     return (this.selector.equals(chatcomponentselector.selector) && super.equals(p_equals_1_));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 54 */     return "SelectorComponent{pattern='" + this.selector + '\'' + ", siblings=" + this.siblings + ", style=" + getChatStyle() + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\ChatComponentSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */