/*    */ package rip.diavlo.base.utils;
/*    */ 
/*    */ import net.minecraft.util.ChatComponentText;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import rip.diavlo.base.Client;
/*    */ 
/*    */ public class ChatUtil implements MinecraftUtil {
/*    */   public static String translateAlternateColorCodes(char character, String string) {
/*  9 */     return string.replaceAll(String.valueOf(character), "§");
/*    */   }
/*    */   
/*    */   public static void display(String message) {
/* 13 */     print(message, true);
/*    */   }
/*    */   
/*    */   public static void print(String message, boolean prefix) {
/* 17 */     if (mc.thePlayer == null)
/*    */       return; 
/* 19 */     mc.thePlayer.addChatMessage((IChatComponent)new ChatComponentText((prefix ? ("§4" + Client.clientName + " §7> §f") : "") + translateAlternateColorCodes('&', message)));
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\utils\ChatUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */