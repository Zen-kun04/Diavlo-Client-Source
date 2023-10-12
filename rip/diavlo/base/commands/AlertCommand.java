/*    */ package rip.diavlo.base.commands;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import rip.diavlo.base.api.command.Command;
/*    */ import rip.diavlo.base.api.command.CommandExecutionException;
/*    */ import rip.diavlo.base.utils.ChatUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class AlertCommand
/*    */   implements Command
/*    */ {
/*    */   public String[] getAliases() {
/* 18 */     return new String[] { "alert" };
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] arguments) throws CommandExecutionException {
/* 23 */     if (mc.isSingleplayer()) {
/* 24 */       ChatUtil.print(".alert solo se puede usar en §c§lMULTIJUGADOR", true);
/*    */       
/*    */       return;
/*    */     } 
/* 28 */     if (arguments.length < 2) {
/* 29 */       ChatUtil.print(".alert <texto>", true);
/*    */       return;
/*    */     } 
/* 32 */     String message = String.join(" ", Arrays.<CharSequence>copyOfRange((CharSequence[])arguments, 1, arguments.length));
/*    */ 
/*    */     
/* 35 */     mc.thePlayer.sendChatMessage("/alertwar " + message);
/* 36 */     mc.thePlayer.sendChatMessage("/alert " + message);
/* 37 */     ChatUtil.print("Alerta Ejecutada!", true);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUsage() {
/* 42 */     return "<texto> || Envia una alerta a todos los jugadores [OP].";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\commands\AlertCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */