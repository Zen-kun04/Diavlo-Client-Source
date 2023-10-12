/*    */ package rip.diavlo.base.commands;
/*    */ 
/*    */ import org.lwjgl.input.Keyboard;
/*    */ import rip.diavlo.base.Client;
/*    */ import rip.diavlo.base.api.command.Command;
/*    */ import rip.diavlo.base.api.command.CommandExecutionException;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ import rip.diavlo.base.utils.ChatUtil;
/*    */ import rip.diavlo.base.utils.NotificationUtils;
/*    */ 
/*    */ public class BindCommand
/*    */   implements Command {
/*    */   public String[] getAliases() {
/* 14 */     return new String[] { "bind" };
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args) throws CommandExecutionException {
/* 19 */     if (args.length != 3) {
/*    */       
/* 21 */       ChatUtil.print(".bind" + getUsage(), true);
/*    */       
/*    */       return;
/*    */     } 
/* 25 */     if (Client.getInstance().getModuleManager().getModules().stream().filter(module -> module.getName().equalsIgnoreCase(args[1])).findAny().get() != null) {
/*    */       
/* 27 */       Module m = Client.getInstance().getModuleManager().getModules().stream().filter(module -> module.getName().equalsIgnoreCase(args[1])).findFirst().get();
/* 28 */       int key = Keyboard.getKeyIndex(args[2].toUpperCase());
/* 29 */       m.setKey(key);
/* 30 */       NotificationUtils.drawNotification(m.getName() + " §aasignado§f a la tecla  §c" + args[2].toUpperCase());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUsage() {
/* 37 */     return " <Hack> <Tecla> || Asigna un hack a una tecla del teclado.";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\commands\BindCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */