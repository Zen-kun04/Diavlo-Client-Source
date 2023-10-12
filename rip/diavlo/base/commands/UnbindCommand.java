/*    */ package rip.diavlo.base.commands;
/*    */ 
/*    */ import rip.diavlo.base.Client;
/*    */ import rip.diavlo.base.api.command.Command;
/*    */ import rip.diavlo.base.api.command.CommandExecutionException;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ import rip.diavlo.base.utils.ChatUtil;
/*    */ import rip.diavlo.base.utils.NotificationUtils;
/*    */ 
/*    */ public class UnbindCommand
/*    */   implements Command
/*    */ {
/*    */   public String[] getAliases() {
/* 14 */     return new String[] { "unbind" };
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args) throws CommandExecutionException {
/* 19 */     if (args.length != 2) {
/*    */       
/* 21 */       ChatUtil.print(".unbind" + getUsage(), true);
/*    */       
/*    */       return;
/*    */     } 
/* 25 */     if (Client.getInstance().getModuleManager().getModules().stream().filter(module -> module.getName().equalsIgnoreCase(args[1])).findAny().get() != null) {
/*    */       
/* 27 */       Module m = Client.getInstance().getModuleManager().getModules().stream().filter(module -> module.getName().equalsIgnoreCase(args[1])).findFirst().get();
/* 28 */       NotificationUtils.drawNotification(m.getName() + " §cdesasignado");
/* 29 */       m.setKey(0);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUsage() {
/* 36 */     return " <Hack> || Desasigna la tecla actual para activar el hack.";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\commands\UnbindCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */