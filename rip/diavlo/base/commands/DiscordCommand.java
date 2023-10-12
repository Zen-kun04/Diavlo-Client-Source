/*    */ package rip.diavlo.base.commands;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import rip.diavlo.base.api.command.Command;
/*    */ import rip.diavlo.base.api.command.CommandExecutionException;
/*    */ import rip.diavlo.base.utils.ChatUtil;
/*    */ 
/*    */ 
/*    */ public final class DiscordCommand
/*    */   implements Command
/*    */ {
/*    */   public String[] getAliases() {
/* 13 */     return new String[] { "discord" };
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(String[] arguments) throws CommandExecutionException {
/* 19 */     if (mc.isSingleplayer()) {
/* 20 */       ChatUtil.print(".discord solo se puede usar en §c§lMULTIJUGADOR", true);
/*    */       
/*    */       return;
/*    */     } 
/* 24 */     if (arguments.length < 2) {
/* 25 */       ChatUtil.print(".discord <texto>", true);
/*    */       return;
/*    */     } 
/* 28 */     String message = String.join(" ", Arrays.<CharSequence>copyOfRange((CharSequence[])arguments, 1, arguments.length));
/*    */ 
/*    */     
/* 31 */     mc.thePlayer.sendChatMessage("/discordsrv broadcast " + message);
/* 32 */     mc.thePlayer.sendChatMessage("/discordsrv broadcast " + message);
/* 33 */     mc.thePlayer.sendChatMessage("/discordsrv broadcast " + message);
/*    */     
/* 35 */     ChatUtil.print("Discord spam ejecutado!", true);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUsage() {
/* 41 */     return "<texto> || Envia un mensaje al discord del servidor [OP].";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\commands\DiscordCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */