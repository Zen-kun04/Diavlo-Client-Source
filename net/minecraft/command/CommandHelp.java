/*     */ package net.minecraft.command;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.event.ClickEvent;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ public class CommandHelp
/*     */   extends CommandBase
/*     */ {
/*     */   public String getCommandName() {
/*  20 */     return "help";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRequiredPermissionLevel() {
/*  25 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  30 */     return "commands.help.usage";
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getCommandAliases() {
/*  35 */     return Arrays.asList(new String[] { "?" });
/*     */   }
/*     */ 
/*     */   
/*     */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*  40 */     List<ICommand> list = getSortedPossibleCommands(sender);
/*  41 */     int i = 7;
/*  42 */     int j = (list.size() - 1) / 7;
/*  43 */     int k = 0;
/*     */ 
/*     */     
/*     */     try {
/*  47 */       k = (args.length == 0) ? 0 : (parseInt(args[0], 1, j + 1) - 1);
/*     */     }
/*  49 */     catch (NumberInvalidException numberinvalidexception) {
/*     */       
/*  51 */       Map<String, ICommand> map = getCommands();
/*  52 */       ICommand icommand = map.get(args[0]);
/*     */       
/*  54 */       if (icommand != null)
/*     */       {
/*  56 */         throw new WrongUsageException(icommand.getCommandUsage(sender), new Object[0]);
/*     */       }
/*     */       
/*  59 */       if (MathHelper.parseIntWithDefault(args[0], -1) != -1)
/*     */       {
/*  61 */         throw numberinvalidexception;
/*     */       }
/*     */       
/*  64 */       throw new CommandNotFoundException();
/*     */     } 
/*     */     
/*  67 */     int l = Math.min((k + 1) * 7, list.size());
/*  68 */     ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation("commands.help.header", new Object[] { Integer.valueOf(k + 1), Integer.valueOf(j + 1) });
/*  69 */     chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
/*  70 */     sender.addChatMessage((IChatComponent)chatcomponenttranslation1);
/*     */     
/*  72 */     for (int i1 = k * 7; i1 < l; i1++) {
/*     */       
/*  74 */       ICommand icommand1 = list.get(i1);
/*  75 */       ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(icommand1.getCommandUsage(sender), new Object[0]);
/*  76 */       chatcomponenttranslation.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + icommand1.getCommandName() + " "));
/*  77 */       sender.addChatMessage((IChatComponent)chatcomponenttranslation);
/*     */     } 
/*     */     
/*  80 */     if (k == 0 && sender instanceof net.minecraft.entity.player.EntityPlayer) {
/*     */       
/*  82 */       ChatComponentTranslation chatcomponenttranslation2 = new ChatComponentTranslation("commands.help.footer", new Object[0]);
/*  83 */       chatcomponenttranslation2.getChatStyle().setColor(EnumChatFormatting.GREEN);
/*  84 */       sender.addChatMessage((IChatComponent)chatcomponenttranslation2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<ICommand> getSortedPossibleCommands(ICommandSender p_71534_1_) {
/*  90 */     List<ICommand> list = MinecraftServer.getServer().getCommandManager().getPossibleCommands(p_71534_1_);
/*  91 */     Collections.sort(list);
/*  92 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<String, ICommand> getCommands() {
/*  97 */     return MinecraftServer.getServer().getCommandManager().getCommands();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 102 */     if (args.length == 1) {
/*     */       
/* 104 */       Set<String> set = getCommands().keySet();
/* 105 */       return getListOfStringsMatchingLastWord(args, set.<String>toArray(new String[set.size()]));
/*     */     } 
/*     */ 
/*     */     
/* 109 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandHelp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */