/*     */ package net.minecraft.command;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class CommandExecuteAt
/*     */   extends CommandBase
/*     */ {
/*     */   public String getCommandName() {
/*  17 */     return "execute";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRequiredPermissionLevel() {
/*  22 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  27 */     return "commands.execute.usage";
/*     */   }
/*     */ 
/*     */   
/*     */   public void processCommand(final ICommandSender sender, String[] args) throws CommandException {
/*  32 */     if (args.length < 5)
/*     */     {
/*  34 */       throw new WrongUsageException("commands.execute.usage", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  38 */     final Entity entity = getEntity(sender, args[0], Entity.class);
/*  39 */     final double d0 = parseDouble(entity.posX, args[1], false);
/*  40 */     final double d1 = parseDouble(entity.posY, args[2], false);
/*  41 */     final double d2 = parseDouble(entity.posZ, args[3], false);
/*  42 */     final BlockPos blockpos = new BlockPos(d0, d1, d2);
/*  43 */     int i = 4;
/*     */     
/*  45 */     if ("detect".equals(args[4]) && args.length > 10) {
/*     */       
/*  47 */       World world = entity.getEntityWorld();
/*  48 */       double d3 = parseDouble(d0, args[5], false);
/*  49 */       double d4 = parseDouble(d1, args[6], false);
/*  50 */       double d5 = parseDouble(d2, args[7], false);
/*  51 */       Block block = getBlockByText(sender, args[8]);
/*  52 */       int k = parseInt(args[9], -1, 15);
/*  53 */       BlockPos blockpos1 = new BlockPos(d3, d4, d5);
/*  54 */       IBlockState iblockstate = world.getBlockState(blockpos1);
/*     */       
/*  56 */       if (iblockstate.getBlock() != block || (k >= 0 && iblockstate.getBlock().getMetaFromState(iblockstate) != k))
/*     */       {
/*  58 */         throw new CommandException("commands.execute.failed", new Object[] { "detect", entity.getName() });
/*     */       }
/*     */       
/*  61 */       i = 10;
/*     */     } 
/*     */     
/*  64 */     String s = buildString(args, i);
/*  65 */     ICommandSender icommandsender = new ICommandSender()
/*     */       {
/*     */         public String getName()
/*     */         {
/*  69 */           return entity.getName();
/*     */         }
/*     */         
/*     */         public IChatComponent getDisplayName() {
/*  73 */           return entity.getDisplayName();
/*     */         }
/*     */         
/*     */         public void addChatMessage(IChatComponent component) {
/*  77 */           sender.addChatMessage(component);
/*     */         }
/*     */         
/*     */         public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
/*  81 */           return sender.canCommandSenderUseCommand(permLevel, commandName);
/*     */         }
/*     */         
/*     */         public BlockPos getPosition() {
/*  85 */           return blockpos;
/*     */         }
/*     */         
/*     */         public Vec3 getPositionVector() {
/*  89 */           return new Vec3(d0, d1, d2);
/*     */         }
/*     */         
/*     */         public World getEntityWorld() {
/*  93 */           return entity.worldObj;
/*     */         }
/*     */         
/*     */         public Entity getCommandSenderEntity() {
/*  97 */           return entity;
/*     */         }
/*     */         
/*     */         public boolean sendCommandFeedback() {
/* 101 */           MinecraftServer minecraftserver = MinecraftServer.getServer();
/* 102 */           return (minecraftserver == null || minecraftserver.worldServers[0].getGameRules().getBoolean("commandBlockOutput"));
/*     */         }
/*     */         
/*     */         public void setCommandStat(CommandResultStats.Type type, int amount) {
/* 106 */           entity.setCommandStat(type, amount);
/*     */         }
/*     */       };
/* 109 */     ICommandManager icommandmanager = MinecraftServer.getServer().getCommandManager();
/*     */ 
/*     */     
/*     */     try {
/* 113 */       int j = icommandmanager.executeCommand(icommandsender, s);
/*     */       
/* 115 */       if (j < 1)
/*     */       {
/* 117 */         throw new CommandException("commands.execute.allInvocationsFailed", new Object[] { s });
/*     */       }
/*     */     }
/* 120 */     catch (Throwable var23) {
/*     */       
/* 122 */       throw new CommandException("commands.execute.failed", new Object[] { s, entity.getName() });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 129 */     return (args.length == 1) ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : ((args.length > 1 && args.length <= 4) ? func_175771_a(args, 1, pos) : ((args.length > 5 && args.length <= 8 && "detect".equals(args[4])) ? func_175771_a(args, 5, pos) : ((args.length == 9 && "detect".equals(args[4])) ? getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys()) : null)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUsernameIndex(String[] args, int index) {
/* 134 */     return (index == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandExecuteAt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */