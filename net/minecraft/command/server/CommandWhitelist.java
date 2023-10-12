/*     */ package net.minecraft.command.server;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.util.List;
/*     */ import net.minecraft.command.CommandBase;
/*     */ import net.minecraft.command.CommandException;
/*     */ import net.minecraft.command.ICommand;
/*     */ import net.minecraft.command.ICommandSender;
/*     */ import net.minecraft.command.WrongUsageException;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ 
/*     */ public class CommandWhitelist extends CommandBase {
/*     */   public String getCommandName() {
/*  18 */     return "whitelist";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRequiredPermissionLevel() {
/*  23 */     return 3;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  28 */     return "commands.whitelist.usage";
/*     */   }
/*     */ 
/*     */   
/*     */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*  33 */     if (args.length < 1)
/*     */     {
/*  35 */       throw new WrongUsageException("commands.whitelist.usage", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  39 */     MinecraftServer minecraftserver = MinecraftServer.getServer();
/*     */     
/*  41 */     if (args[0].equals("on")) {
/*     */       
/*  43 */       minecraftserver.getConfigurationManager().setWhiteListEnabled(true);
/*  44 */       notifyOperators(sender, (ICommand)this, "commands.whitelist.enabled", new Object[0]);
/*     */     }
/*  46 */     else if (args[0].equals("off")) {
/*     */       
/*  48 */       minecraftserver.getConfigurationManager().setWhiteListEnabled(false);
/*  49 */       notifyOperators(sender, (ICommand)this, "commands.whitelist.disabled", new Object[0]);
/*     */     }
/*  51 */     else if (args[0].equals("list")) {
/*     */       
/*  53 */       sender.addChatMessage((IChatComponent)new ChatComponentTranslation("commands.whitelist.list", new Object[] { Integer.valueOf((minecraftserver.getConfigurationManager().getWhitelistedPlayerNames()).length), Integer.valueOf((minecraftserver.getConfigurationManager().getAvailablePlayerDat()).length) }));
/*  54 */       String[] astring = minecraftserver.getConfigurationManager().getWhitelistedPlayerNames();
/*  55 */       sender.addChatMessage((IChatComponent)new ChatComponentText(joinNiceString((Object[])astring)));
/*     */     }
/*  57 */     else if (args[0].equals("add")) {
/*     */       
/*  59 */       if (args.length < 2)
/*     */       {
/*  61 */         throw new WrongUsageException("commands.whitelist.add.usage", new Object[0]);
/*     */       }
/*     */       
/*  64 */       GameProfile gameprofile = minecraftserver.getPlayerProfileCache().getGameProfileForUsername(args[1]);
/*     */       
/*  66 */       if (gameprofile == null)
/*     */       {
/*  68 */         throw new CommandException("commands.whitelist.add.failed", new Object[] { args[1] });
/*     */       }
/*     */       
/*  71 */       minecraftserver.getConfigurationManager().addWhitelistedPlayer(gameprofile);
/*  72 */       notifyOperators(sender, (ICommand)this, "commands.whitelist.add.success", new Object[] { args[1] });
/*     */     }
/*  74 */     else if (args[0].equals("remove")) {
/*     */       
/*  76 */       if (args.length < 2)
/*     */       {
/*  78 */         throw new WrongUsageException("commands.whitelist.remove.usage", new Object[0]);
/*     */       }
/*     */       
/*  81 */       GameProfile gameprofile1 = minecraftserver.getConfigurationManager().getWhitelistedPlayers().getBannedProfile(args[1]);
/*     */       
/*  83 */       if (gameprofile1 == null)
/*     */       {
/*  85 */         throw new CommandException("commands.whitelist.remove.failed", new Object[] { args[1] });
/*     */       }
/*     */       
/*  88 */       minecraftserver.getConfigurationManager().removePlayerFromWhitelist(gameprofile1);
/*  89 */       notifyOperators(sender, (ICommand)this, "commands.whitelist.remove.success", new Object[] { args[1] });
/*     */     }
/*  91 */     else if (args[0].equals("reload")) {
/*     */       
/*  93 */       minecraftserver.getConfigurationManager().loadWhiteList();
/*  94 */       notifyOperators(sender, (ICommand)this, "commands.whitelist.reloaded", new Object[0]);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 101 */     if (args.length == 1)
/*     */     {
/* 103 */       return getListOfStringsMatchingLastWord(args, new String[] { "on", "off", "list", "add", "remove", "reload" });
/*     */     }
/*     */ 
/*     */     
/* 107 */     if (args.length == 2) {
/*     */       
/* 109 */       if (args[0].equals("remove"))
/*     */       {
/* 111 */         return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getConfigurationManager().getWhitelistedPlayerNames());
/*     */       }
/*     */       
/* 114 */       if (args[0].equals("add"))
/*     */       {
/* 116 */         return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getPlayerProfileCache().getUsernames());
/*     */       }
/*     */     } 
/*     */     
/* 120 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\server\CommandWhitelist.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */