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
/*     */ import net.minecraft.command.NumberInvalidException;
/*     */ import net.minecraft.command.WrongUsageException;
/*     */ import net.minecraft.nbt.JsonToNBT;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTException;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTUtil;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class CommandTestForBlock extends CommandBase {
/*     */   public String getCommandName() {
/*  24 */     return "testforblock";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRequiredPermissionLevel() {
/*  29 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  34 */     return "commands.testforblock.usage";
/*     */   }
/*     */ 
/*     */   
/*     */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*  39 */     if (args.length < 4)
/*     */     {
/*  41 */       throw new WrongUsageException("commands.testforblock.usage", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  45 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
/*  46 */     BlockPos blockpos = parseBlockPos(sender, args, 0, false);
/*  47 */     Block block = Block.getBlockFromName(args[3]);
/*     */     
/*  49 */     if (block == null)
/*     */     {
/*  51 */       throw new NumberInvalidException("commands.setblock.notFound", new Object[] { args[3] });
/*     */     }
/*     */ 
/*     */     
/*  55 */     int i = -1;
/*     */     
/*  57 */     if (args.length >= 5)
/*     */     {
/*  59 */       i = parseInt(args[4], -1, 15);
/*     */     }
/*     */     
/*  62 */     World world = sender.getEntityWorld();
/*     */     
/*  64 */     if (!world.isBlockLoaded(blockpos))
/*     */     {
/*  66 */       throw new CommandException("commands.testforblock.outOfWorld", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  70 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  71 */     boolean flag = false;
/*     */     
/*  73 */     if (args.length >= 6 && block.hasTileEntity()) {
/*     */       
/*  75 */       String s = getChatComponentFromNthArg(sender, args, 5).getUnformattedText();
/*     */ 
/*     */       
/*     */       try {
/*  79 */         nbttagcompound = JsonToNBT.getTagFromJson(s);
/*  80 */         flag = true;
/*     */       }
/*  82 */       catch (NBTException nbtexception) {
/*     */         
/*  84 */         throw new CommandException("commands.setblock.tagError", new Object[] { nbtexception.getMessage() });
/*     */       } 
/*     */     } 
/*     */     
/*  88 */     IBlockState iblockstate = world.getBlockState(blockpos);
/*  89 */     Block block1 = iblockstate.getBlock();
/*     */     
/*  91 */     if (block1 != block)
/*     */     {
/*  93 */       throw new CommandException("commands.testforblock.failed.tile", new Object[] { Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()), block1.getLocalizedName(), block.getLocalizedName() });
/*     */     }
/*     */ 
/*     */     
/*  97 */     if (i > -1) {
/*     */       
/*  99 */       int j = iblockstate.getBlock().getMetaFromState(iblockstate);
/*     */       
/* 101 */       if (j != i)
/*     */       {
/* 103 */         throw new CommandException("commands.testforblock.failed.data", new Object[] { Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()), Integer.valueOf(j), Integer.valueOf(i) });
/*     */       }
/*     */     } 
/*     */     
/* 107 */     if (flag) {
/*     */       
/* 109 */       TileEntity tileentity = world.getTileEntity(blockpos);
/*     */       
/* 111 */       if (tileentity == null)
/*     */       {
/* 113 */         throw new CommandException("commands.testforblock.failed.tileEntity", new Object[] { Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()) });
/*     */       }
/*     */       
/* 116 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 117 */       tileentity.writeToNBT(nbttagcompound1);
/*     */       
/* 119 */       if (!NBTUtil.func_181123_a((NBTBase)nbttagcompound, (NBTBase)nbttagcompound1, true))
/*     */       {
/* 121 */         throw new CommandException("commands.testforblock.failed.nbt", new Object[] { Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()) });
/*     */       }
/*     */     } 
/*     */     
/* 125 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
/* 126 */     notifyOperators(sender, (ICommand)this, "commands.testforblock.success", new Object[] { Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 135 */     return (args.length > 0 && args.length <= 3) ? func_175771_a(args, 0, pos) : ((args.length == 4) ? getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys()) : null);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\server\CommandTestForBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */