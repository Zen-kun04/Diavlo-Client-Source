/*    */ package rip.diavlo.base.commands;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import rip.diavlo.base.api.command.Command;
/*    */ import rip.diavlo.base.api.command.CommandExecutionException;
/*    */ import rip.diavlo.base.utils.ChatUtil;
/*    */ 
/*    */ 
/*    */ public final class TitleCommand
/*    */   implements Command
/*    */ {
/*    */   public String[] getAliases() {
/* 13 */     return new String[] { "title" };
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] arguments) throws CommandExecutionException {
/* 18 */     if (mc.isSingleplayer()) {
/* 19 */       ChatUtil.print(".title solo se puede usar en §c§lMULTIJUGADOR", true);
/*    */       
/*    */       return;
/*    */     } 
/* 23 */     if (arguments.length < 2) {
/* 24 */       ChatUtil.print(".title <texto>", true);
/*    */       
/*    */       return;
/*    */     } 
/* 28 */     String message = String.join(" ", Arrays.<CharSequence>copyOfRange((CharSequence[])arguments, 1, arguments.length));
/*    */ 
/*    */     
/* 31 */     mc.thePlayer.sendChatMessage("/title @a title {\"text\":\"" + message + "\",\"bold\":true,\"color\":\"dark_red\"}");
/* 32 */     mc.thePlayer.sendChatMessage("/title @a subtitle {\"text\":\"Usando Diavlo Client -> discord.gg/programadores\",\"color\":\"red\"}");
/*    */     
/* 34 */     ChatUtil.print("Titulo ejecutado!", true);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUsage() {
/* 40 */     return "<texto> || Envia un Title en la pantalla a todos los jugadores [OP].";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\commands\TitleCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */