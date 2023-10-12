/*    */ package rip.diavlo.base.commands;
/*    */ 
/*    */ import rip.diavlo.base.api.command.Command;
/*    */ import rip.diavlo.base.api.command.CommandExecutionException;
/*    */ 
/*    */ public final class ClearCommand
/*    */   implements Command
/*    */ {
/*    */   public String[] getAliases() {
/* 10 */     return new String[] { "clear" };
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(String[] arguments) throws CommandExecutionException {
/* 16 */     mc.ingameGUI.getChatGUI().clearChatMessages();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUsage() {
/* 21 */     return " || Limpia el chat.";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\commands\ClearCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */