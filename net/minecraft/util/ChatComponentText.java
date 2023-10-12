/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class ChatComponentText
/*    */   extends ChatComponentStyle
/*    */ {
/*    */   private final String text;
/*    */   
/*    */   public ChatComponentText(String msg) {
/*  9 */     this.text = msg;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getChatComponentText_TextValue() {
/* 14 */     return this.text;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUnformattedTextForChat() {
/* 19 */     return this.text;
/*    */   }
/*    */ 
/*    */   
/*    */   public ChatComponentText createCopy() {
/* 24 */     ChatComponentText chatcomponenttext = new ChatComponentText(this.text);
/* 25 */     chatcomponenttext.setChatStyle(getChatStyle().createShallowCopy());
/*    */     
/* 27 */     for (IChatComponent ichatcomponent : getSiblings())
/*    */     {
/* 29 */       chatcomponenttext.appendSibling(ichatcomponent.createCopy());
/*    */     }
/*    */     
/* 32 */     return chatcomponenttext;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object p_equals_1_) {
/* 37 */     if (this == p_equals_1_)
/*    */     {
/* 39 */       return true;
/*    */     }
/* 41 */     if (!(p_equals_1_ instanceof ChatComponentText))
/*    */     {
/* 43 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 47 */     ChatComponentText chatcomponenttext = (ChatComponentText)p_equals_1_;
/* 48 */     return (this.text.equals(chatcomponenttext.getChatComponentText_TextValue()) && super.equals(p_equals_1_));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 54 */     return "TextComponent{text='" + this.text + '\'' + ", siblings=" + this.siblings + ", style=" + getChatStyle() + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\ChatComponentText.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */