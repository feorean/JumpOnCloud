package io.jumpon.api.library;

import java.util.concurrent.atomic.AtomicLong;

public abstract class Sequencer {
	 
   private static AtomicLong sequenceNumber;
   
   public static long next() {
     return sequenceNumber.getAndIncrement();
   }

   public static void setInitialValue (long initialValue ) {
	   
	   sequenceNumber = new AtomicLong(initialValue);
	   
   }
   
}
