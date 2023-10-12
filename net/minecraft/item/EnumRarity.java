/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ 
/*    */ public enum EnumRarity
/*    */ {
/*  7 */   COMMON(EnumChatFormatting.WHITE, "Common"),
/*  8 */   UNCOMMON(EnumChatFormatting.YELLOW, "Uncommon"),
/*  9 */   RARE(EnumChatFormatting.AQUA, "Rare"),
/* 10 */   EPIC(EnumChatFormatting.LIGHT_PURPLE, "Epic");
/*    */   
/*    */   public final EnumChatFormatting rarityColor;
/*    */   
/*    */   public final String rarityName;
/*    */   
/*    */   EnumRarity(EnumChatFormatting color, String name) {
/* 17 */     this.rarityColor = color;
/* 18 */     this.rarityName = name;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\EnumRarity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */