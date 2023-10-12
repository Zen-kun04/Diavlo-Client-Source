/*     */ package net.minecraft.command;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class CommandHandler
/*     */   implements ICommandManager {
/*  19 */   private static final Logger logger = LogManager.getLogger();
/*  20 */   private final Map<String, ICommand> commandMap = Maps.newHashMap();
/*  21 */   private final Set<ICommand> commandSet = Sets.newHashSet();
/*     */ 
/*     */   
/*     */   public int executeCommand(ICommandSender sender, String rawCommand) {
/*  25 */     rawCommand = rawCommand.trim();
/*     */     
/*  27 */     if (rawCommand.startsWith("/"))
/*     */     {
/*  29 */       rawCommand = rawCommand.substring(1);
/*     */     }
/*     */     
/*  32 */     String[] astring = rawCommand.split(" ");
/*  33 */     String s = astring[0];
/*  34 */     astring = dropFirstString(astring);
/*  35 */     ICommand icommand = this.commandMap.get(s);
/*  36 */     int i = getUsernameIndex(icommand, astring);
/*  37 */     int j = 0;
/*     */     
/*  39 */     if (icommand == null) {
/*     */       
/*  41 */       ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.generic.notFound", new Object[0]);
/*  42 */       chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
/*  43 */       sender.addChatMessage((IChatComponent)chatcomponenttranslation);
/*     */     }
/*  45 */     else if (icommand.canCommandSenderUseCommand(sender)) {
/*     */       
/*  47 */       if (i > -1)
/*     */       {
/*  49 */         List<Entity> list = PlayerSelector.matchEntities(sender, astring[i], Entity.class);
/*  50 */         String s1 = astring[i];
/*  51 */         sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());
/*     */         
/*  53 */         for (Entity entity : list) {
/*     */           
/*  55 */           astring[i] = entity.getUniqueID().toString();
/*     */           
/*  57 */           if (tryExecute(sender, astring, icommand, rawCommand))
/*     */           {
/*  59 */             j++;
/*     */           }
/*     */         } 
/*     */         
/*  63 */         astring[i] = s1;
/*     */       }
/*     */       else
/*     */       {
/*  67 */         sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, 1);
/*     */         
/*  69 */         if (tryExecute(sender, astring, icommand, rawCommand))
/*     */         {
/*  71 */           j++;
/*     */         }
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/*  77 */       ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation("commands.generic.permission", new Object[0]);
/*  78 */       chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.RED);
/*  79 */       sender.addChatMessage((IChatComponent)chatcomponenttranslation1);
/*     */     } 
/*     */     
/*  82 */     sender.setCommandStat(CommandResultStats.Type.SUCCESS_COUNT, j);
/*  83 */     return j;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean tryExecute(ICommandSender sender, String[] args, ICommand command, String input) {
/*     */     try {
/*  90 */       command.processCommand(sender, args);
/*  91 */       return true;
/*     */     }
/*  93 */     catch (WrongUsageException wrongusageexception) {
/*     */       
/*  95 */       ChatComponentTranslation chatcomponenttranslation2 = new ChatComponentTranslation("commands.generic.usage", new Object[] { new ChatComponentTranslation(wrongusageexception.getMessage(), wrongusageexception.getErrorObjects()) });
/*  96 */       chatcomponenttranslation2.getChatStyle().setColor(EnumChatFormatting.RED);
/*  97 */       sender.addChatMessage((IChatComponent)chatcomponenttranslation2);
/*     */     }
/*  99 */     catch (CommandException commandexception) {
/*     */       
/* 101 */       ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation(commandexception.getMessage(), commandexception.getErrorObjects());
/* 102 */       chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.RED);
/* 103 */       sender.addChatMessage((IChatComponent)chatcomponenttranslation1);
/*     */     }
/* 105 */     catch (Throwable var9) {
/*     */       
/* 107 */       ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.generic.exception", new Object[0]);
/* 108 */       chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
/* 109 */       sender.addChatMessage((IChatComponent)chatcomponenttranslation);
/* 110 */       logger.warn("Couldn't process command: '" + input + "'");
/*     */     } 
/*     */     
/* 113 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ICommand registerCommand(ICommand command) {
/* 118 */     this.commandMap.put(command.getCommandName(), command);
/* 119 */     this.commandSet.add(command);
/*     */     
/* 121 */     for (String s : command.getCommandAliases()) {
/*     */       
/* 123 */       ICommand icommand = this.commandMap.get(s);
/*     */       
/* 125 */       if (icommand == null || !icommand.getCommandName().equals(s))
/*     */       {
/* 127 */         this.commandMap.put(s, command);
/*     */       }
/*     */     } 
/*     */     
/* 131 */     return command;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String[] dropFirstString(String[] input) {
/* 136 */     String[] astring = new String[input.length - 1];
/* 137 */     System.arraycopy(input, 1, astring, 0, input.length - 1);
/* 138 */     return astring;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getTabCompletionOptions(ICommandSender sender, String input, BlockPos pos) {
/* 143 */     String[] astring = input.split(" ", -1);
/* 144 */     String s = astring[0];
/*     */     
/* 146 */     if (astring.length == 1) {
/*     */       
/* 148 */       List<String> list = Lists.newArrayList();
/*     */       
/* 150 */       for (Map.Entry<String, ICommand> entry : this.commandMap.entrySet()) {
/*     */         
/* 152 */         if (CommandBase.doesStringStartWith(s, entry.getKey()) && ((ICommand)entry.getValue()).canCommandSenderUseCommand(sender))
/*     */         {
/* 154 */           list.add(entry.getKey());
/*     */         }
/*     */       } 
/*     */       
/* 158 */       return list;
/*     */     } 
/*     */ 
/*     */     
/* 162 */     if (astring.length > 1) {
/*     */       
/* 164 */       ICommand icommand = this.commandMap.get(s);
/*     */       
/* 166 */       if (icommand != null && icommand.canCommandSenderUseCommand(sender))
/*     */       {
/* 168 */         return icommand.addTabCompletionOptions(sender, dropFirstString(astring), pos);
/*     */       }
/*     */     } 
/*     */     
/* 172 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ICommand> getPossibleCommands(ICommandSender sender) {
/* 178 */     List<ICommand> list = Lists.newArrayList();
/*     */     
/* 180 */     for (ICommand icommand : this.commandSet) {
/*     */       
/* 182 */       if (icommand.canCommandSenderUseCommand(sender))
/*     */       {
/* 184 */         list.add(icommand);
/*     */       }
/*     */     } 
/*     */     
/* 188 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, ICommand> getCommands() {
/* 193 */     return this.commandMap;
/*     */   }
/*     */ 
/*     */   
/*     */   private int getUsernameIndex(ICommand command, String[] args) {
/* 198 */     if (command == null)
/*     */     {
/* 200 */       return -1;
/*     */     }
/*     */ 
/*     */     
/* 204 */     for (int i = 0; i < args.length; i++) {
/*     */       
/* 206 */       if (command.isUsernameIndex(args, i) && PlayerSelector.matchesMultiplePlayers(args[i]))
/*     */       {
/* 208 */         return i;
/*     */       }
/*     */     } 
/*     */     
/* 212 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */