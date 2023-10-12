/*    */ package rip.diavlo.base.commands;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import rip.diavlo.base.api.command.Command;
/*    */ import rip.diavlo.base.api.command.CommandExecutionException;
/*    */ import rip.diavlo.base.utils.ChatUtil;
/*    */ 
/*    */ 
/*    */ public final class LitebansCommand
/*    */   implements Command
/*    */ {
/*    */   public String[] getAliases() {
/* 13 */     return new String[] { "litebans" };
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(String[] arguments) throws CommandExecutionException {
/* 19 */     if (mc.isSingleplayer()) {
/* 20 */       ChatUtil.print(".litebans solo se puede usar en §c§lMULTIJUGADOR", true);
/*    */       
/*    */       return;
/*    */     } 
/* 24 */     if (arguments.length < 2) {
/* 25 */       ChatUtil.print(".litebans <texto>", true);
/*    */       
/*    */       return;
/*    */     } 
/* 29 */     String message = String.join(" ", Arrays.<CharSequence>copyOfRange((CharSequence[])arguments, 1, arguments.length));
/*    */     
/* 31 */     mc.thePlayer.sendChatMessage("/litebans broadcast " + message);
/* 32 */     mc.thePlayer.sendChatMessage("/litebans broadcast " + message);
/* 33 */     mc.thePlayer.sendChatMessage("/litebans broadcast " + message);
/*    */     
/* 35 */     ChatUtil.print("Titulo ejecutado!", true);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUsage() {
/* 41 */     return "<texto> || Manda un broadcast de Litebans con un texto [OP]";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\commands\LitebansCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */