/*    */ package rip.diavlo.base.commands;
/*    */ 
/*    */ import net.minecraft.util.ChatComponentText;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import rip.diavlo.base.api.command.Command;
/*    */ import rip.diavlo.base.api.command.CommandExecutionException;
/*    */ 
/*    */ public final class ForceOPCommand
/*    */   implements Command {
/*    */   public String[] getAliases() {
/* 11 */     return new String[] { "fakeop" };
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] arguments) throws CommandExecutionException {
/* 16 */     mc.thePlayer.addChatMessage((IChatComponent)new ChatComponentText("§7§o[Server: Opped§7§o " + mc.thePlayer.getName() + "§7§o§7]"));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUsage() {
/* 22 */     return "|| Simula la accion de la consola dandote OP.";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\commands\ForceOPCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */