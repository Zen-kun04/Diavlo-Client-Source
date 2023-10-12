/*    */ package net.minecraft.command;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.nbt.JsonToNBT;
/*    */ import net.minecraft.nbt.NBTException;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class CommandBlockData
/*    */   extends CommandBase
/*    */ {
/*    */   public String getCommandName() {
/* 15 */     return "blockdata";
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRequiredPermissionLevel() {
/* 20 */     return 2;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 25 */     return "commands.blockdata.usage";
/*    */   }
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*    */     NBTTagCompound nbttagcompound2;
/* 30 */     if (args.length < 4)
/*    */     {
/* 32 */       throw new WrongUsageException("commands.blockdata.usage", new Object[0]);
/*    */     }
/*    */ 
/*    */     
/* 36 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
/* 37 */     BlockPos blockpos = parseBlockPos(sender, args, 0, false);
/* 38 */     World world = sender.getEntityWorld();
/*    */     
/* 40 */     if (!world.isBlockLoaded(blockpos))
/*    */     {
/* 42 */       throw new CommandException("commands.blockdata.outOfWorld", new Object[0]);
/*    */     }
/*    */ 
/*    */     
/* 46 */     TileEntity tileentity = world.getTileEntity(blockpos);
/*    */     
/* 48 */     if (tileentity == null)
/*    */     {
/* 50 */       throw new CommandException("commands.blockdata.notValid", new Object[0]);
/*    */     }
/*    */ 
/*    */     
/* 54 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 55 */     tileentity.writeToNBT(nbttagcompound);
/* 56 */     NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttagcompound.copy();
/*    */ 
/*    */ 
/*    */     
/*    */     try {
/* 61 */       nbttagcompound2 = JsonToNBT.getTagFromJson(getChatComponentFromNthArg(sender, args, 3).getUnformattedText());
/*    */     }
/* 63 */     catch (NBTException nbtexception) {
/*    */       
/* 65 */       throw new CommandException("commands.blockdata.tagError", new Object[] { nbtexception.getMessage() });
/*    */     } 
/*    */     
/* 68 */     nbttagcompound.merge(nbttagcompound2);
/* 69 */     nbttagcompound.setInteger("x", blockpos.getX());
/* 70 */     nbttagcompound.setInteger("y", blockpos.getY());
/* 71 */     nbttagcompound.setInteger("z", blockpos.getZ());
/*    */     
/* 73 */     if (nbttagcompound.equals(nbttagcompound1))
/*    */     {
/* 75 */       throw new CommandException("commands.blockdata.failed", new Object[] { nbttagcompound.toString() });
/*    */     }
/*    */ 
/*    */     
/* 79 */     tileentity.readFromNBT(nbttagcompound);
/* 80 */     tileentity.markDirty();
/* 81 */     world.markBlockForUpdate(blockpos);
/* 82 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
/* 83 */     notifyOperators(sender, this, "commands.blockdata.success", new Object[] { nbttagcompound.toString() });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 92 */     return (args.length > 0 && args.length <= 3) ? func_175771_a(args, 0, pos) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandBlockData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */