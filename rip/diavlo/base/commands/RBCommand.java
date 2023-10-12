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
/*    */ public final class RBCommand
/*    */   implements Command
/*    */ {
/*    */   public String[] getAliases() {
/* 17 */     return new String[] { "rainbow" };
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(String[] arguments) throws CommandExecutionException {
/* 23 */     if (mc.isSingleplayer()) {
/* 24 */       ChatUtil.print(".rainbow solo se puede usar en §c§lMULTIJUGADOR", true);
/*    */       
/*    */       return;
/*    */     } 
/* 28 */     if (arguments.length < 3) {
/* 29 */       ChatUtil.print("Use: .rainbow <delay> <times> <texto>", true);
/*    */       
/*    */       return;
/*    */     } 
/* 33 */     String message = String.join(" ", Arrays.<CharSequence>copyOfRange((CharSequence[])arguments, 3, arguments.length));
/*    */     
/* 35 */     ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
/*    */     
/* 37 */     String[] colors = { "&1", "&2", "&3", "&4", "&5", "&6", "&9", "&a", "&b" };
/*    */     
/* 39 */     int delay = 0;
/*    */     try {
/* 41 */       delay = Integer.parseInt(arguments[1]);
/* 42 */     } catch (NumberFormatException e) {
/* 43 */       ChatUtil.print("El primer argumento debe de ser un numero!", true);
/*    */       
/*    */       return;
/*    */     } 
/* 47 */     int repeat = 1;
/*    */     try {
/* 49 */       repeat = Integer.parseInt(arguments[2]);
/* 50 */     } catch (NumberFormatException e) {
/* 51 */       ChatUtil.print("El segundo argumento debe de ser un numero!", true);
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 56 */     int actualDelay = delay;
/*    */     
/* 58 */     for (int i = 0; i <= repeat; i++) {
/* 59 */       for (String specialCharacter : colors) {
/*    */         try {
/* 61 */           executorService.schedule(() -> mc.thePlayer.sendChatMessage("/essentials:bc " + specialCharacter + message), actualDelay, TimeUnit.MILLISECONDS);
/*    */         }
/* 63 */         catch (Exception exception) {}
/*    */         
/* 65 */         actualDelay += delay;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUsage() {
/* 72 */     return "<delay> <repeats> <txt> || Mandar un mensaje con efecto de rainbow";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\commands\RBCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */