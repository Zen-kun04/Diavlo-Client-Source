package com.sun.jna;

public interface TypeMapper {
  FromNativeConverter getFromNativeConverter(Class<?> paramClass);
  
  ToNativeConverter getToNativeConverter(Class<?> paramClass);
}


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\sun\jna\TypeMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */