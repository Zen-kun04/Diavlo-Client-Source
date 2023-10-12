/*    */ package net.minecraft.command.server;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommand;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.command.WrongUsageException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.server.S05PacketSpawnPosition;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ public class CommandSetDefaultSpawnpoint extends CommandBase {
/*    */   public String getCommandName() {
/* 16 */     return "setworldspawn";
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRequiredPermissionLevel() {
/* 21 */     return 2;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 26 */     return "commands.setworldspawn.usage";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*    */     BlockPos blockpos;
/* 33 */     if (args.length == 0) {
/*    */       
/* 35 */       blockpos = getCommandSenderAsPlayer(sender).getPosition();
/*    */     }
/*    */     else {
/*    */       
/* 39 */       if (args.length != 3 || sender.getEntityWorld() == null)
/*    */       {
/* 41 */         throw new WrongUsageException("commands.setworldspawn.usage", new Object[0]);
/*    */       }
/*    */       
/* 44 */       blockpos = parseBlockPos(sender, args, 0, true);
/*    */     } 
/*    */     
/* 47 */     sender.getEntityWorld().setSpawnPoint(blockpos);
/* 48 */     MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers((Packet)new S05PacketSpawnPosition(blockpos));
/* 49 */     notifyOperators(sender, (ICommand)this, "commands.setworldspawn.success", new Object[] { Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()) });
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 54 */     return (args.length > 0 && args.length <= 3) ? func_175771_a(args, 0, pos) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\server\CommandSetDefaultSpawnpoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */