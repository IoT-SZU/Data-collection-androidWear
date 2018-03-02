package com.example.tommy.androidwear.Utils;

import java.util.ArrayList;

/**
 * Created by chenlin on 16/01/2018.
 */
public class HighPass {
    private static float[] b = {0.040237f, -0.145578f, 0.211410f, -0.145578f, 0.040237f};

    private static float[] in;
    private static float[] outData;

    public static void highPass (ArrayList<Float> signal) {
        in = new float[b.length];
        outData = new float[signal.size()];
        float y;

        for (int i = 0; i < signal.size(); i++) {
            System.arraycopy(in, 0, in, 1, in.length - 1);  //in[1]=in[0],in[2]=in[1]...
            in[0] = signal.get(i);

            //calculate y based on a and b coefficients
            //and in and out.
            y = 0;
            for(int j = 0 ; j < b.length ; j++){
                y += b[j] * in[j];
            }
            outData[i] = y;
        }
        for (int i = 0 ; i < signal.size();i++){
            signal.set(i,outData[i]);
        }
    }
}
