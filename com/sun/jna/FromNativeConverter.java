package com.sun.jna;

public interface FromNativeConverter {
  Object fromNative(Object paramObject, FromNativeContext paramFromNativeContext);
  
  Class<?> nativeType();
}


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\sun\jna\FromNativeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */