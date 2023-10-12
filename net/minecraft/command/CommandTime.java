/*     */ package net.minecraft.command;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.WorldServer;
/*     */ 
/*     */ public class CommandTime
/*     */   extends CommandBase
/*     */ {
/*     */   public String getCommandName() {
/*  12 */     return "time";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRequiredPermissionLevel() {
/*  17 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  22 */     return "commands.time.usage";
/*     */   }
/*     */ 
/*     */   
/*     */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*  27 */     if (args.length > 1) {
/*     */       
/*  29 */       if (args[0].equals("set")) {
/*     */         int l;
/*     */ 
/*     */         
/*  33 */         if (args[1].equals("day")) {
/*     */           
/*  35 */           l = 1000;
/*     */         }
/*  37 */         else if (args[1].equals("night")) {
/*     */           
/*  39 */           l = 13000;
/*     */         }
/*     */         else {
/*     */           
/*  43 */           l = parseInt(args[1], 0);
/*     */         } 
/*     */         
/*  46 */         setTime(sender, l);
/*  47 */         notifyOperators(sender, this, "commands.time.set", new Object[] { Integer.valueOf(l) });
/*     */         
/*     */         return;
/*     */       } 
/*  51 */       if (args[0].equals("add")) {
/*     */         
/*  53 */         int k = parseInt(args[1], 0);
/*  54 */         addTime(sender, k);
/*  55 */         notifyOperators(sender, this, "commands.time.added", new Object[] { Integer.valueOf(k) });
/*     */         
/*     */         return;
/*     */       } 
/*  59 */       if (args[0].equals("query")) {
/*     */         
/*  61 */         if (args[1].equals("daytime")) {
/*     */           
/*  63 */           int j = (int)(sender.getEntityWorld().getWorldTime() % 2147483647L);
/*  64 */           sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, j);
/*  65 */           notifyOperators(sender, this, "commands.time.query", new Object[] { Integer.valueOf(j) });
/*     */           
/*     */           return;
/*     */         } 
/*  69 */         if (args[1].equals("gametime")) {
/*     */           
/*  71 */           int i = (int)(sender.getEntityWorld().getTotalWorldTime() % 2147483647L);
/*  72 */           sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, i);
/*  73 */           notifyOperators(sender, this, "commands.time.query", new Object[] { Integer.valueOf(i) });
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*  79 */     throw new WrongUsageException("commands.time.usage", new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/*  84 */     return (args.length == 1) ? getListOfStringsMatchingLastWord(args, new String[] { "set", "add", "query" }) : ((args.length == 2 && args[0].equals("set")) ? getListOfStringsMatchingLastWord(args, new String[] { "day", "night" }) : ((args.length == 2 && args[0].equals("query")) ? getListOfStringsMatchingLastWord(args, new String[] { "daytime", "gametime" }) : null));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setTime(ICommandSender sender, int time) {
/*  89 */     for (int i = 0; i < (MinecraftServer.getServer()).worldServers.length; i++)
/*     */     {
/*  91 */       (MinecraftServer.getServer()).worldServers[i].setWorldTime(time);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addTime(ICommandSender sender, int time) {
/*  97 */     for (int i = 0; i < (MinecraftServer.getServer()).worldServers.length; i++) {
/*     */       
/*  99 */       WorldServer worldserver = (MinecraftServer.getServer()).worldServers[i];
/* 100 */       worldserver.setWorldTime(worldserver.getWorldTime() + time);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandTime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */