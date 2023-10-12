/*      */ package net.minecraft.network;
/*      */ 
/*      */ import com.google.common.base.Charsets;
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.ByteBufAllocator;
/*      */ import io.netty.buffer.ByteBufInputStream;
/*      */ import io.netty.buffer.ByteBufOutputStream;
/*      */ import io.netty.buffer.ByteBufProcessor;
/*      */ import io.netty.handler.codec.DecoderException;
/*      */ import io.netty.handler.codec.EncoderException;
/*      */ import io.netty.util.ReferenceCounted;
/*      */ import java.io.DataInput;
/*      */ import java.io.DataOutput;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.nio.channels.GatheringByteChannel;
/*      */ import java.nio.channels.ScatteringByteChannel;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.UUID;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.nbt.CompressedStreamTools;
/*      */ import net.minecraft.nbt.NBTSizeTracker;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.IChatComponent;
/*      */ 
/*      */ public class PacketBuffer
/*      */   extends ByteBuf {
/*      */   public PacketBuffer(ByteBuf wrapped) {
/*   34 */     this.buf = wrapped;
/*      */   }
/*      */   private final ByteBuf buf;
/*      */   
/*      */   public static int getVarIntSize(int input) {
/*   39 */     for (int i = 1; i < 5; i++) {
/*      */       
/*   41 */       if ((input & -1 << i * 7) == 0)
/*      */       {
/*   43 */         return i;
/*      */       }
/*      */     } 
/*      */     
/*   47 */     return 5;
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeByteArray(byte[] array) {
/*   52 */     writeVarIntToBuffer(array.length);
/*   53 */     writeBytes(array);
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] readByteArray() {
/*   58 */     byte[] abyte = new byte[readVarIntFromBuffer()];
/*   59 */     readBytes(abyte);
/*   60 */     return abyte;
/*      */   }
/*      */ 
/*      */   
/*      */   public BlockPos readBlockPos() {
/*   65 */     return BlockPos.fromLong(readLong());
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeBlockPos(BlockPos pos) {
/*   70 */     writeLong(pos.toLong());
/*      */   }
/*      */ 
/*      */   
/*      */   public IChatComponent readChatComponent() throws IOException {
/*   75 */     return IChatComponent.Serializer.jsonToComponent(readStringFromBuffer(32767));
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeChatComponent(IChatComponent component) throws IOException {
/*   80 */     writeString(IChatComponent.Serializer.componentToJson(component));
/*      */   }
/*      */ 
/*      */   
/*      */   public <T extends Enum<T>> T readEnumValue(Class<T> enumClass) {
/*   85 */     return (T)((Enum[])enumClass.getEnumConstants())[readVarIntFromBuffer()];
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeEnumValue(Enum<?> value) {
/*   90 */     writeVarIntToBuffer(value.ordinal());
/*      */   }
/*      */   
/*      */   public int readVarIntFromBuffer() {
/*      */     byte b0;
/*   95 */     int i = 0;
/*   96 */     int j = 0;
/*      */ 
/*      */     
/*      */     do {
/*  100 */       b0 = readByte();
/*  101 */       i |= (b0 & Byte.MAX_VALUE) << j++ * 7;
/*      */       
/*  103 */       if (j > 5)
/*      */       {
/*  105 */         throw new RuntimeException("VarInt too big");
/*      */       }
/*      */     }
/*  108 */     while ((b0 & 0x80) == 128);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  114 */     return i;
/*      */   }
/*      */   
/*      */   public long readVarLong() {
/*      */     byte b0;
/*  119 */     long i = 0L;
/*  120 */     int j = 0;
/*      */ 
/*      */     
/*      */     do {
/*  124 */       b0 = readByte();
/*  125 */       i |= (b0 & Byte.MAX_VALUE) << j++ * 7;
/*      */       
/*  127 */       if (j > 10)
/*      */       {
/*  129 */         throw new RuntimeException("VarLong too big");
/*      */       }
/*      */     }
/*  132 */     while ((b0 & 0x80) == 128);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  138 */     return i;
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeUuid(UUID uuid) {
/*  143 */     writeLong(uuid.getMostSignificantBits());
/*  144 */     writeLong(uuid.getLeastSignificantBits());
/*      */   }
/*      */ 
/*      */   
/*      */   public UUID readUuid() {
/*  149 */     return new UUID(readLong(), readLong());
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeVarIntToBuffer(int input) {
/*  154 */     while ((input & 0xFFFFFF80) != 0) {
/*      */       
/*  156 */       writeByte(input & 0x7F | 0x80);
/*  157 */       input >>>= 7;
/*      */     } 
/*      */     
/*  160 */     writeByte(input);
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeVarLong(long value) {
/*  165 */     while ((value & 0xFFFFFFFFFFFFFF80L) != 0L) {
/*      */       
/*  167 */       writeByte((int)(value & 0x7FL) | 0x80);
/*  168 */       value >>>= 7L;
/*      */     } 
/*      */     
/*  171 */     writeByte((int)value);
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeNBTTagCompoundToBuffer(NBTTagCompound nbt) {
/*  176 */     if (nbt == null) {
/*      */       
/*  178 */       writeByte(0);
/*      */     } else {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/*  184 */         CompressedStreamTools.write(nbt, (DataOutput)new ByteBufOutputStream(this));
/*      */       }
/*  186 */       catch (IOException ioexception) {
/*      */         
/*  188 */         throw new EncoderException(ioexception);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public NBTTagCompound readNBTTagCompoundFromBuffer() throws IOException {
/*  195 */     int i = readerIndex();
/*  196 */     byte b0 = readByte();
/*      */     
/*  198 */     if (b0 == 0)
/*      */     {
/*  200 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  204 */     readerIndex(i);
/*  205 */     return CompressedStreamTools.read((DataInput)new ByteBufInputStream(this), new NBTSizeTracker(2097152L));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeItemStackToBuffer(ItemStack stack) {
/*  211 */     if (stack == null) {
/*      */       
/*  213 */       writeShort(-1);
/*      */     }
/*      */     else {
/*      */       
/*  217 */       writeShort(Item.getIdFromItem(stack.getItem()));
/*  218 */       writeByte(stack.stackSize);
/*  219 */       writeShort(stack.getMetadata());
/*  220 */       NBTTagCompound nbttagcompound = null;
/*      */       
/*  222 */       if (stack.getItem().isDamageable() || stack.getItem().getShareTag())
/*      */       {
/*  224 */         nbttagcompound = stack.getTagCompound();
/*      */       }
/*      */       
/*  227 */       writeNBTTagCompoundToBuffer(nbttagcompound);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public ItemStack readItemStackFromBuffer() throws IOException {
/*  233 */     ItemStack itemstack = null;
/*  234 */     int i = readShort();
/*      */     
/*  236 */     if (i >= 0) {
/*      */       
/*  238 */       int j = readByte();
/*  239 */       int k = readShort();
/*  240 */       itemstack = new ItemStack(Item.getItemById(i), j, k);
/*  241 */       itemstack.setTagCompound(readNBTTagCompoundFromBuffer());
/*      */     } 
/*      */     
/*  244 */     return itemstack;
/*      */   }
/*      */ 
/*      */   
/*      */   public String readStringFromBuffer(int maxLength) {
/*  249 */     int i = readVarIntFromBuffer();
/*      */     
/*  251 */     if (i > maxLength * 4)
/*      */     {
/*  253 */       throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + i + " > " + (maxLength * 4) + ")");
/*      */     }
/*  255 */     if (i < 0)
/*      */     {
/*  257 */       throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
/*      */     }
/*      */ 
/*      */     
/*  261 */     String s = new String(readBytes(i).array(), Charsets.UTF_8);
/*      */     
/*  263 */     if (s.length() > maxLength)
/*      */     {
/*  265 */       throw new DecoderException("The received string length is longer than maximum allowed (" + i + " > " + maxLength + ")");
/*      */     }
/*      */ 
/*      */     
/*  269 */     return s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PacketBuffer writeString(String string) {
/*  276 */     byte[] abyte = string.getBytes(Charsets.UTF_8);
/*      */     
/*  278 */     if (abyte.length > 32767)
/*      */     {
/*  280 */       throw new EncoderException("String too big (was " + string.length() + " bytes encoded, max " + '翿' + ")");
/*      */     }
/*      */ 
/*      */     
/*  284 */     writeVarIntToBuffer(abyte.length);
/*  285 */     writeBytes(abyte);
/*  286 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int capacity() {
/*  292 */     return this.buf.capacity();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf capacity(int p_capacity_1_) {
/*  297 */     return this.buf.capacity(p_capacity_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public int maxCapacity() {
/*  302 */     return this.buf.maxCapacity();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBufAllocator alloc() {
/*  307 */     return this.buf.alloc();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteOrder order() {
/*  312 */     return this.buf.order();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf order(ByteOrder p_order_1_) {
/*  317 */     return this.buf.order(p_order_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf unwrap() {
/*  322 */     return this.buf.unwrap();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isDirect() {
/*  327 */     return this.buf.isDirect();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readerIndex() {
/*  332 */     return this.buf.readerIndex();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readerIndex(int p_readerIndex_1_) {
/*  337 */     return this.buf.readerIndex(p_readerIndex_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public int writerIndex() {
/*  342 */     return this.buf.writerIndex();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writerIndex(int p_writerIndex_1_) {
/*  347 */     return this.buf.writerIndex(p_writerIndex_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setIndex(int p_setIndex_1_, int p_setIndex_2_) {
/*  352 */     return this.buf.setIndex(p_setIndex_1_, p_setIndex_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public int readableBytes() {
/*  357 */     return this.buf.readableBytes();
/*      */   }
/*      */ 
/*      */   
/*      */   public int writableBytes() {
/*  362 */     return this.buf.writableBytes();
/*      */   }
/*      */ 
/*      */   
/*      */   public int maxWritableBytes() {
/*  367 */     return this.buf.maxWritableBytes();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isReadable() {
/*  372 */     return this.buf.isReadable();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isReadable(int p_isReadable_1_) {
/*  377 */     return this.buf.isReadable(p_isReadable_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWritable() {
/*  382 */     return this.buf.isWritable();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWritable(int p_isWritable_1_) {
/*  387 */     return this.buf.isWritable(p_isWritable_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf clear() {
/*  392 */     return this.buf.clear();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf markReaderIndex() {
/*  397 */     return this.buf.markReaderIndex();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf resetReaderIndex() {
/*  402 */     return this.buf.resetReaderIndex();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf markWriterIndex() {
/*  407 */     return this.buf.markWriterIndex();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf resetWriterIndex() {
/*  412 */     return this.buf.resetWriterIndex();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf discardReadBytes() {
/*  417 */     return this.buf.discardReadBytes();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf discardSomeReadBytes() {
/*  422 */     return this.buf.discardSomeReadBytes();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf ensureWritable(int p_ensureWritable_1_) {
/*  427 */     return this.buf.ensureWritable(p_ensureWritable_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public int ensureWritable(int p_ensureWritable_1_, boolean p_ensureWritable_2_) {
/*  432 */     return this.buf.ensureWritable(p_ensureWritable_1_, p_ensureWritable_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getBoolean(int p_getBoolean_1_) {
/*  437 */     return this.buf.getBoolean(p_getBoolean_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getByte(int p_getByte_1_) {
/*  442 */     return this.buf.getByte(p_getByte_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public short getUnsignedByte(int p_getUnsignedByte_1_) {
/*  447 */     return this.buf.getUnsignedByte(p_getUnsignedByte_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public short getShort(int p_getShort_1_) {
/*  452 */     return this.buf.getShort(p_getShort_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedShort(int p_getUnsignedShort_1_) {
/*  457 */     return this.buf.getUnsignedShort(p_getUnsignedShort_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMedium(int p_getMedium_1_) {
/*  462 */     return this.buf.getMedium(p_getMedium_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedMedium(int p_getUnsignedMedium_1_) {
/*  467 */     return this.buf.getUnsignedMedium(p_getUnsignedMedium_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getInt(int p_getInt_1_) {
/*  472 */     return this.buf.getInt(p_getInt_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getUnsignedInt(int p_getUnsignedInt_1_) {
/*  477 */     return this.buf.getUnsignedInt(p_getUnsignedInt_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getLong(int p_getLong_1_) {
/*  482 */     return this.buf.getLong(p_getLong_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public char getChar(int p_getChar_1_) {
/*  487 */     return this.buf.getChar(p_getChar_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getFloat(int p_getFloat_1_) {
/*  492 */     return this.buf.getFloat(p_getFloat_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public double getDouble(int p_getDouble_1_) {
/*  497 */     return this.buf.getDouble(p_getDouble_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int p_getBytes_1_, ByteBuf p_getBytes_2_) {
/*  502 */     return this.buf.getBytes(p_getBytes_1_, p_getBytes_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int p_getBytes_1_, ByteBuf p_getBytes_2_, int p_getBytes_3_) {
/*  507 */     return this.buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int p_getBytes_1_, ByteBuf p_getBytes_2_, int p_getBytes_3_, int p_getBytes_4_) {
/*  512 */     return this.buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_, p_getBytes_4_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int p_getBytes_1_, byte[] p_getBytes_2_) {
/*  517 */     return this.buf.getBytes(p_getBytes_1_, p_getBytes_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int p_getBytes_1_, byte[] p_getBytes_2_, int p_getBytes_3_, int p_getBytes_4_) {
/*  522 */     return this.buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_, p_getBytes_4_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int p_getBytes_1_, ByteBuffer p_getBytes_2_) {
/*  527 */     return this.buf.getBytes(p_getBytes_1_, p_getBytes_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int p_getBytes_1_, OutputStream p_getBytes_2_, int p_getBytes_3_) throws IOException {
/*  532 */     return this.buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBytes(int p_getBytes_1_, GatheringByteChannel p_getBytes_2_, int p_getBytes_3_) throws IOException {
/*  537 */     return this.buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBoolean(int p_setBoolean_1_, boolean p_setBoolean_2_) {
/*  542 */     return this.buf.setBoolean(p_setBoolean_1_, p_setBoolean_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setByte(int p_setByte_1_, int p_setByte_2_) {
/*  547 */     return this.buf.setByte(p_setByte_1_, p_setByte_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setShort(int p_setShort_1_, int p_setShort_2_) {
/*  552 */     return this.buf.setShort(p_setShort_1_, p_setShort_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setMedium(int p_setMedium_1_, int p_setMedium_2_) {
/*  557 */     return this.buf.setMedium(p_setMedium_1_, p_setMedium_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setInt(int p_setInt_1_, int p_setInt_2_) {
/*  562 */     return this.buf.setInt(p_setInt_1_, p_setInt_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setLong(int p_setLong_1_, long p_setLong_2_) {
/*  567 */     return this.buf.setLong(p_setLong_1_, p_setLong_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setChar(int p_setChar_1_, int p_setChar_2_) {
/*  572 */     return this.buf.setChar(p_setChar_1_, p_setChar_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setFloat(int p_setFloat_1_, float p_setFloat_2_) {
/*  577 */     return this.buf.setFloat(p_setFloat_1_, p_setFloat_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setDouble(int p_setDouble_1_, double p_setDouble_2_) {
/*  582 */     return this.buf.setDouble(p_setDouble_1_, p_setDouble_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int p_setBytes_1_, ByteBuf p_setBytes_2_) {
/*  587 */     return this.buf.setBytes(p_setBytes_1_, p_setBytes_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int p_setBytes_1_, ByteBuf p_setBytes_2_, int p_setBytes_3_) {
/*  592 */     return this.buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int p_setBytes_1_, ByteBuf p_setBytes_2_, int p_setBytes_3_, int p_setBytes_4_) {
/*  597 */     return this.buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_, p_setBytes_4_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int p_setBytes_1_, byte[] p_setBytes_2_) {
/*  602 */     return this.buf.setBytes(p_setBytes_1_, p_setBytes_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int p_setBytes_1_, byte[] p_setBytes_2_, int p_setBytes_3_, int p_setBytes_4_) {
/*  607 */     return this.buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_, p_setBytes_4_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int p_setBytes_1_, ByteBuffer p_setBytes_2_) {
/*  612 */     return this.buf.setBytes(p_setBytes_1_, p_setBytes_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public int setBytes(int p_setBytes_1_, InputStream p_setBytes_2_, int p_setBytes_3_) throws IOException {
/*  617 */     return this.buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_);
/*      */   }
/*      */ 
/*      */   
/*      */   public int setBytes(int p_setBytes_1_, ScatteringByteChannel p_setBytes_2_, int p_setBytes_3_) throws IOException {
/*  622 */     return this.buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setZero(int p_setZero_1_, int p_setZero_2_) {
/*  627 */     return this.buf.setZero(p_setZero_1_, p_setZero_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean readBoolean() {
/*  632 */     return this.buf.readBoolean();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte readByte() {
/*  637 */     return this.buf.readByte();
/*      */   }
/*      */ 
/*      */   
/*      */   public short readUnsignedByte() {
/*  642 */     return this.buf.readUnsignedByte();
/*      */   }
/*      */ 
/*      */   
/*      */   public short readShort() {
/*  647 */     return this.buf.readShort();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedShort() {
/*  652 */     return this.buf.readUnsignedShort();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readMedium() {
/*  657 */     return this.buf.readMedium();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedMedium() {
/*  662 */     return this.buf.readUnsignedMedium();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readInt() {
/*  667 */     return this.buf.readInt();
/*      */   }
/*      */ 
/*      */   
/*      */   public long readUnsignedInt() {
/*  672 */     return this.buf.readUnsignedInt();
/*      */   }
/*      */ 
/*      */   
/*      */   public long readLong() {
/*  677 */     return this.buf.readLong();
/*      */   }
/*      */ 
/*      */   
/*      */   public char readChar() {
/*  682 */     return this.buf.readChar();
/*      */   }
/*      */ 
/*      */   
/*      */   public float readFloat() {
/*  687 */     return this.buf.readFloat();
/*      */   }
/*      */ 
/*      */   
/*      */   public double readDouble() {
/*  692 */     return this.buf.readDouble();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(int p_readBytes_1_) {
/*  697 */     return this.buf.readBytes(p_readBytes_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readSlice(int p_readSlice_1_) {
/*  702 */     return this.buf.readSlice(p_readSlice_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf p_readBytes_1_) {
/*  707 */     return this.buf.readBytes(p_readBytes_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf p_readBytes_1_, int p_readBytes_2_) {
/*  712 */     return this.buf.readBytes(p_readBytes_1_, p_readBytes_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf p_readBytes_1_, int p_readBytes_2_, int p_readBytes_3_) {
/*  717 */     return this.buf.readBytes(p_readBytes_1_, p_readBytes_2_, p_readBytes_3_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(byte[] p_readBytes_1_) {
/*  722 */     return this.buf.readBytes(p_readBytes_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(byte[] p_readBytes_1_, int p_readBytes_2_, int p_readBytes_3_) {
/*  727 */     return this.buf.readBytes(p_readBytes_1_, p_readBytes_2_, p_readBytes_3_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(ByteBuffer p_readBytes_1_) {
/*  732 */     return this.buf.readBytes(p_readBytes_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(OutputStream p_readBytes_1_, int p_readBytes_2_) throws IOException {
/*  737 */     return this.buf.readBytes(p_readBytes_1_, p_readBytes_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public int readBytes(GatheringByteChannel p_readBytes_1_, int p_readBytes_2_) throws IOException {
/*  742 */     return this.buf.readBytes(p_readBytes_1_, p_readBytes_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf skipBytes(int p_skipBytes_1_) {
/*  747 */     return this.buf.skipBytes(p_skipBytes_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBoolean(boolean p_writeBoolean_1_) {
/*  752 */     return this.buf.writeBoolean(p_writeBoolean_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeByte(int p_writeByte_1_) {
/*  757 */     return this.buf.writeByte(p_writeByte_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeShort(int p_writeShort_1_) {
/*  762 */     return this.buf.writeShort(p_writeShort_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeMedium(int p_writeMedium_1_) {
/*  767 */     return this.buf.writeMedium(p_writeMedium_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeInt(int p_writeInt_1_) {
/*  772 */     return this.buf.writeInt(p_writeInt_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeLong(long p_writeLong_1_) {
/*  777 */     return this.buf.writeLong(p_writeLong_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeChar(int p_writeChar_1_) {
/*  782 */     return this.buf.writeChar(p_writeChar_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeFloat(float p_writeFloat_1_) {
/*  787 */     return this.buf.writeFloat(p_writeFloat_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeDouble(double p_writeDouble_1_) {
/*  792 */     return this.buf.writeDouble(p_writeDouble_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf p_writeBytes_1_) {
/*  797 */     return this.buf.writeBytes(p_writeBytes_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf p_writeBytes_1_, int p_writeBytes_2_) {
/*  802 */     return this.buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf p_writeBytes_1_, int p_writeBytes_2_, int p_writeBytes_3_) {
/*  807 */     return this.buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_, p_writeBytes_3_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(byte[] p_writeBytes_1_) {
/*  812 */     return this.buf.writeBytes(p_writeBytes_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(byte[] p_writeBytes_1_, int p_writeBytes_2_, int p_writeBytes_3_) {
/*  817 */     return this.buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_, p_writeBytes_3_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuffer p_writeBytes_1_) {
/*  822 */     return this.buf.writeBytes(p_writeBytes_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeBytes(InputStream p_writeBytes_1_, int p_writeBytes_2_) throws IOException {
/*  827 */     return this.buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeBytes(ScatteringByteChannel p_writeBytes_1_, int p_writeBytes_2_) throws IOException {
/*  832 */     return this.buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeZero(int p_writeZero_1_) {
/*  837 */     return this.buf.writeZero(p_writeZero_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public int indexOf(int p_indexOf_1_, int p_indexOf_2_, byte p_indexOf_3_) {
/*  842 */     return this.buf.indexOf(p_indexOf_1_, p_indexOf_2_, p_indexOf_3_);
/*      */   }
/*      */ 
/*      */   
/*      */   public int bytesBefore(byte p_bytesBefore_1_) {
/*  847 */     return this.buf.bytesBefore(p_bytesBefore_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public int bytesBefore(int p_bytesBefore_1_, byte p_bytesBefore_2_) {
/*  852 */     return this.buf.bytesBefore(p_bytesBefore_1_, p_bytesBefore_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public int bytesBefore(int p_bytesBefore_1_, int p_bytesBefore_2_, byte p_bytesBefore_3_) {
/*  857 */     return this.buf.bytesBefore(p_bytesBefore_1_, p_bytesBefore_2_, p_bytesBefore_3_);
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByte(ByteBufProcessor p_forEachByte_1_) {
/*  862 */     return this.buf.forEachByte(p_forEachByte_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByte(int p_forEachByte_1_, int p_forEachByte_2_, ByteBufProcessor p_forEachByte_3_) {
/*  867 */     return this.buf.forEachByte(p_forEachByte_1_, p_forEachByte_2_, p_forEachByte_3_);
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByteDesc(ByteBufProcessor p_forEachByteDesc_1_) {
/*  872 */     return this.buf.forEachByteDesc(p_forEachByteDesc_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByteDesc(int p_forEachByteDesc_1_, int p_forEachByteDesc_2_, ByteBufProcessor p_forEachByteDesc_3_) {
/*  877 */     return this.buf.forEachByteDesc(p_forEachByteDesc_1_, p_forEachByteDesc_2_, p_forEachByteDesc_3_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf copy() {
/*  882 */     return this.buf.copy();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf copy(int p_copy_1_, int p_copy_2_) {
/*  887 */     return this.buf.copy(p_copy_1_, p_copy_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf slice() {
/*  892 */     return this.buf.slice();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf slice(int p_slice_1_, int p_slice_2_) {
/*  897 */     return this.buf.slice(p_slice_1_, p_slice_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf duplicate() {
/*  902 */     return this.buf.duplicate();
/*      */   }
/*      */ 
/*      */   
/*      */   public int nioBufferCount() {
/*  907 */     return this.buf.nioBufferCount();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer nioBuffer() {
/*  912 */     return this.buf.nioBuffer();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer nioBuffer(int p_nioBuffer_1_, int p_nioBuffer_2_) {
/*  917 */     return this.buf.nioBuffer(p_nioBuffer_1_, p_nioBuffer_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer internalNioBuffer(int p_internalNioBuffer_1_, int p_internalNioBuffer_2_) {
/*  922 */     return this.buf.internalNioBuffer(p_internalNioBuffer_1_, p_internalNioBuffer_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer[] nioBuffers() {
/*  927 */     return this.buf.nioBuffers();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer[] nioBuffers(int p_nioBuffers_1_, int p_nioBuffers_2_) {
/*  932 */     return this.buf.nioBuffers(p_nioBuffers_1_, p_nioBuffers_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasArray() {
/*  937 */     return this.buf.hasArray();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] array() {
/*  942 */     return this.buf.array();
/*      */   }
/*      */ 
/*      */   
/*      */   public int arrayOffset() {
/*  947 */     return this.buf.arrayOffset();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasMemoryAddress() {
/*  952 */     return this.buf.hasMemoryAddress();
/*      */   }
/*      */ 
/*      */   
/*      */   public long memoryAddress() {
/*  957 */     return this.buf.memoryAddress();
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString(Charset p_toString_1_) {
/*  962 */     return this.buf.toString(p_toString_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString(int p_toString_1_, int p_toString_2_, Charset p_toString_3_) {
/*  967 */     return this.buf.toString(p_toString_1_, p_toString_2_, p_toString_3_);
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  972 */     return this.buf.hashCode();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean equals(Object p_equals_1_) {
/*  977 */     return this.buf.equals(p_equals_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public int compareTo(ByteBuf p_compareTo_1_) {
/*  982 */     return this.buf.compareTo(p_compareTo_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/*  987 */     return this.buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retain(int p_retain_1_) {
/*  992 */     return this.buf.retain(p_retain_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retain() {
/*  997 */     return this.buf.retain();
/*      */   }
/*      */ 
/*      */   
/*      */   public int refCnt() {
/* 1002 */     return this.buf.refCnt();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean release() {
/* 1007 */     return this.buf.release();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean release(int p_release_1_) {
/* 1012 */     return this.buf.release(p_release_1_);
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\PacketBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */