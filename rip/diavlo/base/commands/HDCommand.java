/*    */ package rip.diavlo.base.commands;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.concurrent.Executors;
/*    */ import java.util.concurrent.ScheduledExecutorService;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import org.apache.commons.lang3.RandomStringUtils;
/*    */ import rip.diavlo.base.api.command.Command;
/*    */ import rip.diavlo.base.api.command.CommandExecutionException;
/*    */ import rip.diavlo.base.utils.ChatUtil;
/*    */ 
/*    */ 
/*    */ public final class HDCommand
/*    */   implements Command
/*    */ {
/*    */   public String[] getAliases() {
/* 17 */     return new String[] { "hd" };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(String[] arguments) throws CommandExecutionException {
/* 24 */     if (mc.isSingleplayer()) {
/* 25 */       ChatUtil.print(".hd solo se puede usar en §c§lMULTIJUGADOR", true);
/*    */       
/*    */       return;
/*    */     } 
/* 29 */     if (arguments.length < 2) {
/* 30 */       ChatUtil.print(".hd <texto>", true);
/*    */ 
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 36 */     String message = String.join(" ", Arrays.<CharSequence>copyOfRange((CharSequence[])arguments, 1, arguments.length));
/*    */     
/* 38 */     ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
/*    */     
/* 40 */     String[] colors = { "&1", "&2", "&3", "&4", "&5", "&6", "&9", "&a", "&b" };
/*    */     
/* 42 */     int delay = 8000;
/*    */     
/* 44 */     int repeat = 10;
/*    */ 
/*    */ 
/*    */     
/* 48 */     int actualDelay = 0;
/*    */     
/* 50 */     for (int i = 0; i < repeat; i++) {
/* 51 */       for (String specialCharacter : colors) {
/* 52 */         executorService.schedule(() -> mc.thePlayer.sendChatMessage("/hd create " + RandomStringUtils.randomAlphanumeric(5) + " " + message), actualDelay, TimeUnit.MILLISECONDS);
/*    */         
/* 54 */         actualDelay += delay;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUsage() {
/* 61 */     return "<texto> || Realiza Hologramas con un texto [OP]";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\commands\HDCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */