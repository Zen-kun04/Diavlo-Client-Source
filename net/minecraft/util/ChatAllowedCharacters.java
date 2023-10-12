/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class ChatAllowedCharacters
/*    */ {
/*  5 */   public static final char[] allowedCharactersArray = new char[] { '/', '\n', '\r', '\t', Character.MIN_VALUE, '\f', '`', '?', '*', '\\', '<', '>', '|', '"', ':' };
/*    */ 
/*    */   
/*    */   public static boolean isAllowedCharacter(char character) {
/*  9 */     return (character != '§' && character >= ' ' && character != '');
/*    */   }
/*    */ 
/*    */   
/*    */   public static String filterAllowedCharacters(String input) {
/* 14 */     StringBuilder stringbuilder = new StringBuilder();
/*    */     
/* 16 */     for (char c0 : input.toCharArray()) {
/*    */       
/* 18 */       if (isAllowedCharacter(c0))
/*    */       {
/* 20 */         stringbuilder.append(c0);
/*    */       }
/*    */     } 
/*    */     
/* 24 */     return stringbuilder.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\ChatAllowedCharacters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */