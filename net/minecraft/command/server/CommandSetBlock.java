/*     */ package net.minecraft.command.server;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.command.CommandBase;
/*     */ import net.minecraft.command.CommandException;
/*     */ import net.minecraft.command.CommandResultStats;
/*     */ import net.minecraft.command.ICommand;
/*     */ import net.minecraft.command.ICommandSender;
/*     */ import net.minecraft.command.WrongUsageException;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.nbt.JsonToNBT;
/*     */ import net.minecraft.nbt.NBTException;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class CommandSetBlock
/*     */   extends CommandBase {
/*     */   public String getCommandName() {
/*  24 */     return "setblock";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRequiredPermissionLevel() {
/*  29 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  34 */     return "commands.setblock.usage";
/*     */   }
/*     */ 
/*     */   
/*     */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*  39 */     if (args.length < 4)
/*     */     {
/*  41 */       throw new WrongUsageException("commands.setblock.usage", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  45 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
/*  46 */     BlockPos blockpos = parseBlockPos(sender, args, 0, false);
/*  47 */     Block block = CommandBase.getBlockByText(sender, args[3]);
/*  48 */     int i = 0;
/*     */     
/*  50 */     if (args.length >= 5)
/*     */     {
/*  52 */       i = parseInt(args[4], 0, 15);
/*     */     }
/*     */     
/*  55 */     World world = sender.getEntityWorld();
/*     */     
/*  57 */     if (!world.isBlockLoaded(blockpos))
/*     */     {
/*  59 */       throw new CommandException("commands.setblock.outOfWorld", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  63 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  64 */     boolean flag = false;
/*     */     
/*  66 */     if (args.length >= 7 && block.hasTileEntity()) {
/*     */       
/*  68 */       String s = getChatComponentFromNthArg(sender, args, 6).getUnformattedText();
/*     */ 
/*     */       
/*     */       try {
/*  72 */         nbttagcompound = JsonToNBT.getTagFromJson(s);
/*  73 */         flag = true;
/*     */       }
/*  75 */       catch (NBTException nbtexception) {
/*     */         
/*  77 */         throw new CommandException("commands.setblock.tagError", new Object[] { nbtexception.getMessage() });
/*     */       } 
/*     */     } 
/*     */     
/*  81 */     if (args.length >= 6)
/*     */     {
/*  83 */       if (args[5].equals("destroy")) {
/*     */         
/*  85 */         world.destroyBlock(blockpos, true);
/*     */         
/*  87 */         if (block == Blocks.air) {
/*     */           
/*  89 */           notifyOperators(sender, (ICommand)this, "commands.setblock.success", new Object[0]);
/*     */           
/*     */           return;
/*     */         } 
/*  93 */       } else if (args[5].equals("keep") && !world.isAirBlock(blockpos)) {
/*     */         
/*  95 */         throw new CommandException("commands.setblock.noChange", new Object[0]);
/*     */       } 
/*     */     }
/*     */     
/*  99 */     TileEntity tileentity1 = world.getTileEntity(blockpos);
/*     */     
/* 101 */     if (tileentity1 != null) {
/*     */       
/* 103 */       if (tileentity1 instanceof IInventory)
/*     */       {
/* 105 */         ((IInventory)tileentity1).clear();
/*     */       }
/*     */       
/* 108 */       world.setBlockState(blockpos, Blocks.air.getDefaultState(), (block == Blocks.air) ? 2 : 4);
/*     */     } 
/*     */     
/* 111 */     IBlockState iblockstate = block.getStateFromMeta(i);
/*     */     
/* 113 */     if (!world.setBlockState(blockpos, iblockstate, 2))
/*     */     {
/* 115 */       throw new CommandException("commands.setblock.noChange", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/* 119 */     if (flag) {
/*     */       
/* 121 */       TileEntity tileentity = world.getTileEntity(blockpos);
/*     */       
/* 123 */       if (tileentity != null) {
/*     */         
/* 125 */         nbttagcompound.setInteger("x", blockpos.getX());
/* 126 */         nbttagcompound.setInteger("y", blockpos.getY());
/* 127 */         nbttagcompound.setInteger("z", blockpos.getZ());
/* 128 */         tileentity.readFromNBT(nbttagcompound);
/*     */       } 
/*     */     } 
/*     */     
/* 132 */     world.notifyNeighborsRespectDebug(blockpos, iblockstate.getBlock());
/* 133 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
/* 134 */     notifyOperators(sender, (ICommand)this, "commands.setblock.success", new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 142 */     return (args.length > 0 && args.length <= 3) ? func_175771_a(args, 0, pos) : ((args.length == 4) ? getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys()) : ((args.length == 6) ? getListOfStringsMatchingLastWord(args, new String[] { "replace", "destroy", "keep" }) : null));
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\server\CommandSetBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */