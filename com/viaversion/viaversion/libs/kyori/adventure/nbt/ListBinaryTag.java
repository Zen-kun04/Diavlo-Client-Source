/*     */ package com.viaversion.viaversion.libs.kyori.adventure.nbt;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Stream;
/*     */ import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface ListBinaryTag
/*     */   extends ListTagSetter<ListBinaryTag, BinaryTag>, BinaryTag, Iterable<BinaryTag>
/*     */ {
/*     */   @NotNull
/*     */   static ListBinaryTag empty() {
/*  47 */     return ListBinaryTagImpl.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   static ListBinaryTag from(@NotNull Iterable<? extends BinaryTag> tags) {
/*  61 */     return builder().add(tags).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   static Builder<BinaryTag> builder() {
/*  71 */     return new ListTagBuilder<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   static <T extends BinaryTag> Builder<T> builder(@NotNull BinaryTagType<T> type) {
/*  84 */     if (type == BinaryTagTypes.END) throw new IllegalArgumentException("Cannot create a list of " + BinaryTagTypes.END); 
/*  85 */     return new ListTagBuilder<>(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   static ListBinaryTag listBinaryTag(@NotNull BinaryTagType<? extends BinaryTag> type, @NotNull List<BinaryTag> tags) {
/* 100 */     if (tags.isEmpty()) return empty(); 
/* 101 */     if (type == BinaryTagTypes.END) throw new IllegalArgumentException("Cannot create a list of " + BinaryTagTypes.END); 
/* 102 */     return new ListBinaryTagImpl(type, tags);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @ScheduledForRemoval(inVersion = "5.0.0")
/*     */   @NotNull
/*     */   static ListBinaryTag of(@NotNull BinaryTagType<? extends BinaryTag> type, @NotNull List<BinaryTag> tags) {
/* 120 */     return listBinaryTag(type, tags);
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   default BinaryTagType<ListBinaryTag> type() {
/* 125 */     return BinaryTagTypes.LIST;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @NotNull
/*     */   default BinaryTagType<? extends BinaryTag> listType() {
/* 137 */     return elementType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   BinaryTagType<? extends BinaryTag> elementType();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int size();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   BinaryTag get(int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   ListBinaryTag set(int paramInt, @NotNull BinaryTag paramBinaryTag, @Nullable Consumer<? super BinaryTag> paramConsumer);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   ListBinaryTag remove(int paramInt, @Nullable Consumer<? super BinaryTag> paramConsumer);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default byte getByte(int index) {
/* 195 */     return getByte(index, (byte)0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default byte getByte(int index, byte defaultValue) {
/* 207 */     BinaryTag tag = get(index);
/* 208 */     if (tag.type().numeric()) {
/* 209 */       return ((NumberBinaryTag)tag).byteValue();
/*     */     }
/* 211 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default short getShort(int index) {
/* 222 */     return getShort(index, (short)0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default short getShort(int index, short defaultValue) {
/* 234 */     BinaryTag tag = get(index);
/* 235 */     if (tag.type().numeric()) {
/* 236 */       return ((NumberBinaryTag)tag).shortValue();
/*     */     }
/* 238 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default int getInt(int index) {
/* 249 */     return getInt(index, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default int getInt(int index, int defaultValue) {
/* 261 */     BinaryTag tag = get(index);
/* 262 */     if (tag.type().numeric()) {
/* 263 */       return ((NumberBinaryTag)tag).intValue();
/*     */     }
/* 265 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default long getLong(int index) {
/* 276 */     return getLong(index, 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default long getLong(int index, long defaultValue) {
/* 288 */     BinaryTag tag = get(index);
/* 289 */     if (tag.type().numeric()) {
/* 290 */       return ((NumberBinaryTag)tag).longValue();
/*     */     }
/* 292 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default float getFloat(int index) {
/* 303 */     return getFloat(index, 0.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default float getFloat(int index, float defaultValue) {
/* 315 */     BinaryTag tag = get(index);
/* 316 */     if (tag.type().numeric()) {
/* 317 */       return ((NumberBinaryTag)tag).floatValue();
/*     */     }
/* 319 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default double getDouble(int index) {
/* 330 */     return getDouble(index, 0.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default double getDouble(int index, double defaultValue) {
/* 342 */     BinaryTag tag = get(index);
/* 343 */     if (tag.type().numeric()) {
/* 344 */       return ((NumberBinaryTag)tag).doubleValue();
/*     */     }
/* 346 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default byte[] getByteArray(int index) {
/* 357 */     BinaryTag tag = get(index);
/* 358 */     if (tag.type() == BinaryTagTypes.BYTE_ARRAY) {
/* 359 */       return ((ByteArrayBinaryTag)tag).value();
/*     */     }
/* 361 */     return new byte[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default byte[] getByteArray(int index, byte[] defaultValue) {
/* 373 */     BinaryTag tag = get(index);
/* 374 */     if (tag.type() == BinaryTagTypes.BYTE_ARRAY) {
/* 375 */       return ((ByteArrayBinaryTag)tag).value();
/*     */     }
/* 377 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   default String getString(int index) {
/* 388 */     return getString(index, "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   default String getString(int index, @NotNull String defaultValue) {
/* 400 */     BinaryTag tag = get(index);
/* 401 */     if (tag.type() == BinaryTagTypes.STRING) {
/* 402 */       return ((StringBinaryTag)tag).value();
/*     */     }
/* 404 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   default ListBinaryTag getList(int index) {
/* 415 */     return getList(index, null, empty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   default ListBinaryTag getList(int index, @Nullable BinaryTagType<?> elementType) {
/* 427 */     return getList(index, elementType, empty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   default ListBinaryTag getList(int index, @NotNull ListBinaryTag defaultValue) {
/* 439 */     return getList(index, null, defaultValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   default ListBinaryTag getList(int index, @Nullable BinaryTagType<?> elementType, @NotNull ListBinaryTag defaultValue) {
/* 454 */     BinaryTag tag = get(index);
/* 455 */     if (tag.type() == BinaryTagTypes.LIST) {
/* 456 */       ListBinaryTag list = (ListBinaryTag)tag;
/* 457 */       if (elementType == null || list.elementType() == elementType) {
/* 458 */         return list;
/*     */       }
/*     */     } 
/* 461 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   default CompoundBinaryTag getCompound(int index) {
/* 472 */     return getCompound(index, CompoundBinaryTag.empty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   default CompoundBinaryTag getCompound(int index, @NotNull CompoundBinaryTag defaultValue) {
/* 484 */     BinaryTag tag = get(index);
/* 485 */     if (tag.type() == BinaryTagTypes.COMPOUND) {
/* 486 */       return (CompoundBinaryTag)tag;
/*     */     }
/* 488 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default int[] getIntArray(int index) {
/* 499 */     BinaryTag tag = get(index);
/* 500 */     if (tag.type() == BinaryTagTypes.INT_ARRAY) {
/* 501 */       return ((IntArrayBinaryTag)tag).value();
/*     */     }
/* 503 */     return new int[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default int[] getIntArray(int index, int[] defaultValue) {
/* 515 */     BinaryTag tag = get(index);
/* 516 */     if (tag.type() == BinaryTagTypes.INT_ARRAY) {
/* 517 */       return ((IntArrayBinaryTag)tag).value();
/*     */     }
/* 519 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default long[] getLongArray(int index) {
/* 530 */     BinaryTag tag = get(index);
/* 531 */     if (tag.type() == BinaryTagTypes.LONG_ARRAY) {
/* 532 */       return ((LongArrayBinaryTag)tag).value();
/*     */     }
/* 534 */     return new long[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default long[] getLongArray(int index, long[] defaultValue) {
/* 546 */     BinaryTag tag = get(index);
/* 547 */     if (tag.type() == BinaryTagTypes.LONG_ARRAY) {
/* 548 */       return ((LongArrayBinaryTag)tag).value();
/*     */     }
/* 550 */     return defaultValue;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   Stream<BinaryTag> stream();
/*     */   
/*     */   public static interface Builder<T extends BinaryTag> extends ListTagSetter<Builder<T>, T> {
/*     */     @NotNull
/*     */     ListBinaryTag build();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\nbt\ListBinaryTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */