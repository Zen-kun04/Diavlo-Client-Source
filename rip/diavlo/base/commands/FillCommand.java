/*    */ package rip.diavlo.base.commands;
/*    */ 
/*    */ import rip.diavlo.base.api.command.Command;
/*    */ import rip.diavlo.base.api.command.CommandExecutionException;
/*    */ import rip.diavlo.base.utils.ChatUtil;
/*    */ 
/*    */ public final class FillCommand
/*    */   implements Command
/*    */ {
/*    */   public String[] getAliases() {
/* 11 */     return new String[] { "fill" };
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] arguments) throws CommandExecutionException {
/* 16 */     mc.thePlayer.sendChatMessage("/fill ~-15 ~-15 ~-15 ~15 ~15 ~15 air");
/* 17 */     mc.thePlayer.sendChatMessage("/fill ~-15 ~-15 ~-15 ~15 ~15 ~15 lava");
/*    */     
/* 19 */     ChatUtil.print("Fill ejecutado", true);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUsage() {
/* 25 */     return " || Realiza un hueco de lava de 15x15 [OP]";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\commands\FillCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */