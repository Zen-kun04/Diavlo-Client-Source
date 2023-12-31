/*    */ package net.minecraft.command;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.ChatComponentTranslation;
/*    */ import net.minecraft.world.EnumDifficulty;
/*    */ 
/*    */ public class CommandDifficulty
/*    */   extends CommandBase
/*    */ {
/*    */   public String getCommandName() {
/* 13 */     return "difficulty";
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRequiredPermissionLevel() {
/* 18 */     return 2;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 23 */     return "commands.difficulty.usage";
/*    */   }
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 28 */     if (args.length <= 0)
/*    */     {
/* 30 */       throw new WrongUsageException("commands.difficulty.usage", new Object[0]);
/*    */     }
/*    */ 
/*    */     
/* 34 */     EnumDifficulty enumdifficulty = getDifficultyFromCommand(args[0]);
/* 35 */     MinecraftServer.getServer().setDifficultyForAllWorlds(enumdifficulty);
/* 36 */     notifyOperators(sender, this, "commands.difficulty.success", new Object[] { new ChatComponentTranslation(enumdifficulty.getDifficultyResourceKey(), new Object[0]) });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected EnumDifficulty getDifficultyFromCommand(String p_180531_1_) throws CommandException, NumberInvalidException {
/* 42 */     return (!p_180531_1_.equalsIgnoreCase("peaceful") && !p_180531_1_.equalsIgnoreCase("p")) ? ((!p_180531_1_.equalsIgnoreCase("easy") && !p_180531_1_.equalsIgnoreCase("e")) ? ((!p_180531_1_.equalsIgnoreCase("normal") && !p_180531_1_.equalsIgnoreCase("n")) ? ((!p_180531_1_.equalsIgnoreCase("hard") && !p_180531_1_.equalsIgnoreCase("h")) ? EnumDifficulty.getDifficultyEnum(parseInt(p_180531_1_, 0, 3)) : EnumDifficulty.HARD) : EnumDifficulty.NORMAL) : EnumDifficulty.EASY) : EnumDifficulty.PEACEFUL;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 47 */     return (args.length == 1) ? getListOfStringsMatchingLastWord(args, new String[] { "peaceful", "easy", "normal", "hard" }) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandDifficulty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */