/*    */ package rip.diavlo.base.commands;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.concurrent.Executors;
/*    */ import java.util.concurrent.ScheduledExecutorService;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import rip.diavlo.base.api.command.Command;
/*    */ import rip.diavlo.base.api.command.CommandExecutionException;
/*    */ import rip.diavlo.base.utils.ChatUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SayCommand
/*    */   implements Command
/*    */ {
/*    */   public String[] getAliases() {
/* 17 */     return new String[] { "say" };
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] arguments) throws CommandExecutionException {
/* 22 */     if (arguments.length < 3) {
/* 23 */       ChatUtil.print("Usa: .say <delay> <repeticiones> <msg>", true);
/*    */       
/*    */       return;
/*    */     } 
/* 27 */     String message = String.join(" ", Arrays.<CharSequence>copyOfRange((CharSequence[])arguments, 3, arguments.length));
/*    */     
/* 29 */     ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
/*    */     
/* 31 */     int delay = 0;
/*    */     try {
/* 33 */       delay = Integer.parseInt(arguments[1]);
/* 34 */     } catch (NumberFormatException e) {
/* 35 */       ChatUtil.print("El primer argumento debe de ser un numero!", true);
/*    */       
/*    */       return;
/*    */     } 
/* 39 */     int repeat = 1;
/*    */     try {
/* 41 */       repeat = Integer.parseInt(arguments[2]);
/* 42 */     } catch (NumberFormatException e) {
/* 43 */       ChatUtil.print("El segundo argumento debe de ser un numero!", true);
/*    */ 
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 49 */     int actualDelay = delay;
/*    */     
/* 51 */     for (int i = 0; i <= repeat; i++) {
/*    */       try {
/* 53 */         executorService.schedule(() -> mc.thePlayer.sendChatMessage("/execute @e 0 0 0 say " + message), actualDelay, TimeUnit.MILLISECONDS);
/*    */       }
/* 55 */       catch (Exception exception) {}
/*    */       
/* 57 */       actualDelay += delay;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUsage() {
/* 63 */     return "<delay> <repeticiones> <texto> || Ejecuta /say x veces.";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\commands\SayCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */