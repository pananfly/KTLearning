// IPlayerInterface.aidl
package com.pananfly.servicevideoplayer;

// Declare any non-default types here with import statements
import android.view.Surface;

interface IPlayerInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    boolean play(String url);

    boolean setSurface(in Surface surface);

    boolean stop();
}
