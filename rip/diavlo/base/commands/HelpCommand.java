/*    */ package rip.diavlo.base.commands;
/*    */ 
/*    */ import rip.diavlo.base.Client;
/*    */ import rip.diavlo.base.api.command.Command;
/*    */ import rip.diavlo.base.api.command.CommandExecutionException;
/*    */ import rip.diavlo.base.utils.ChatUtil;
/*    */ 
/*    */ public final class HelpCommand
/*    */   implements Command
/*    */ {
/*    */   public String[] getAliases() {
/* 12 */     return new String[] { "help" };
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] arguments) throws CommandExecutionException {
/* 17 */     ChatUtil.print("", false);
/*    */     
/* 19 */     ChatUtil.print("§c§l-------------------------------", false);
/* 20 */     ChatUtil.print("              §4§lComandos §a§lGratis:", false);
/* 21 */     ChatUtil.print("§c§l-------------------------------", false);
/*    */     
/* 23 */     for (Command command : Client.getInstance().getCommandManager().comandosNoVip()) {
/* 24 */       ChatUtil.print(" §c» §a§l." + command.getAliases()[0] + " §f" + command.getUsage(), false);
/* 25 */       ChatUtil.print("", false);
/*    */     } 
/*    */     
/* 28 */     ChatUtil.print("", false);
/*    */     
/* 30 */     ChatUtil.print("§c§l-------------------------------", false);
/* 31 */     ChatUtil.print("                §4§lComandos §6§lVIP:", false);
/* 32 */     ChatUtil.print("§c§l-------------------------------", false);
/*    */     
/* 34 */     for (Command command : Client.getInstance().getCommandManager().comandosVip()) {
/* 35 */       ChatUtil.print(" §c» §6§l." + command.getAliases()[0] + " §f " + command.getUsage(), false);
/* 36 */       ChatUtil.print("", false);
/*    */     } 
/*    */     
/* 39 */     ChatUtil.print("", false);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUsage() {
/* 45 */     return " || Muestra los comandos.";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\commands\HelpCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */