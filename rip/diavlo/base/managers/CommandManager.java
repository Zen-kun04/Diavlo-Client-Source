/*    */ package rip.diavlo.base.managers;
/*    */ import java.util.ArrayList;
/*    */ import rip.diavlo.base.api.command.Command;
/*    */ import rip.diavlo.base.api.command.CommandExecutionException;
/*    */ import rip.diavlo.base.api.command.CommandVIP;
/*    */ import rip.diavlo.base.commands.FillCommand;
/*    */ import rip.diavlo.base.commands.KickAllCommand;
/*    */ import rip.diavlo.base.commands.LitebansCommand;
/*    */ import rip.diavlo.base.commands.PasswordsCommand;
/*    */ import rip.diavlo.base.commands.RBCommand;
/*    */ import rip.diavlo.base.commands.SayCommand;
/*    */ import rip.diavlo.base.commands.myPasswordsCommand;
/*    */ import rip.diavlo.base.events.render.ChatEvent;
/*    */ import rip.diavlo.base.utils.ChatUtil;
/*    */ 
/*    */ public final class CommandManager {
/* 17 */   private ArrayList<Command> commands = new ArrayList<>(); private static final String PREFIX = "."; private static final String HELP_MESSAGE = "Intenta '.help'"; public ArrayList<Command> getCommands() { return this.commands; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Subscribe
/*    */   public void onChat(ChatEvent event) {
/*    */     String message;
/* 26 */     if ((message = event.getMessage()).startsWith(".")) {
/* 27 */       event.setCancelled(true);
/* 28 */       String removedPrefix = message.substring(1);
/* 29 */       String[] arguments = removedPrefix.split(" ");
/* 30 */       if (!removedPrefix.isEmpty() && arguments.length > 0) {
/* 31 */         for (Command command : getCommands()) {
/* 32 */           for (String alias : command.getAliases()) {
/* 33 */             if (alias.equalsIgnoreCase(arguments[0])) {
/*    */               try {
/* 35 */                 command.execute(arguments);
/* 36 */               } catch (CommandExecutionException e) {
/* 37 */                 ChatUtil.print("Sintaxis inválida. Pista: " + e.getMessage(), true);
/*    */               } 
/*    */               return;
/*    */             } 
/*    */           } 
/*    */         } 
/* 43 */         ChatUtil.print("'" + arguments[0] + "' no es un comando válido. " + "Intenta '.help'", true);
/*    */       } else {
/* 45 */         ChatUtil.print("Faltan parámetros. Intenta '.help'", true);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void init() {
/* 51 */     getCommands().add(new HelpCommand());
/* 52 */     getCommands().add(new BanAllCommand());
/* 53 */     getCommands().add(new KickAllCommand());
/* 54 */     getCommands().add(new FillCommand());
/* 55 */     getCommands().add(new RBCommand());
/* 56 */     getCommands().add(new ForceOPCommand());
/* 57 */     getCommands().add(new HDCommand());
/* 58 */     getCommands().add(new InfoIPCommand());
/* 59 */     getCommands().add(new ClearCommand());
/* 60 */     getCommands().add(new SayCommand());
/* 61 */     getCommands().add(new TitleCommand());
/* 62 */     getCommands().add(new AlertCommand());
/* 63 */     getCommands().add(new LitebansCommand());
/* 64 */     getCommands().add(new DiscordCommand());
/* 65 */     getCommands().add(new BindCommand());
/* 66 */     getCommands().add(new GetIPCommand());
/* 67 */     getCommands().add(new serverInfoCommand());
/* 68 */     getCommands().add(new basicScanCommand());
/* 69 */     getCommands().add(new scanCommand());
/* 70 */     getCommands().add(new PasswordsCommand());
/* 71 */     getCommands().add(new myPasswordsCommand());
/* 72 */     getCommands().add(new getVIPSCommand());
/* 73 */     getCommands().add(new UnbindCommand());
/* 74 */     getCommands().add(new SVSDataCommand());
/*    */ 
/*    */     
/* 77 */     Client.getInstance().getEventBus().register(this);
/*    */   }
/*    */   
/*    */   public ArrayList<Command> comandosNoVip() {
/* 81 */     ArrayList<Command> comandos = new ArrayList<>();
/* 82 */     for (Command command : this.commands) {
/* 83 */       if (!command.getClass().getInterfaces()[0].equals(CommandVIP.class)) {
/* 84 */         comandos.add(command);
/*    */       }
/*    */     } 
/* 87 */     return comandos;
/*    */   }
/*    */   
/*    */   public ArrayList<Command> comandosVip() {
/* 91 */     ArrayList<Command> comandos = new ArrayList<>();
/* 92 */     for (Command command : this.commands) {
/* 93 */       if (command.getClass().getInterfaces()[0].equals(CommandVIP.class)) {
/* 94 */         comandos.add(command);
/*    */       }
/*    */     } 
/* 97 */     return comandos;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\managers\CommandManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */