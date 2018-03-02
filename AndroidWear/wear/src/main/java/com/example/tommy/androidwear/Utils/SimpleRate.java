package com.example.tommy.androidwear.Utils;

/**
 * Created by Tommy on 2017/11/8.
 */

public class SimpleRate {

    /**
     * 50Hz
     */
    private int SENSOR_RATE_NORMAL = 20000;
    /**
     * 80Hz
     */
    private int SENSOR_RATE_MIDDLE = 12500;
    /**
     * 100Hz
     */
    private int SENSOR_RATE_FAST = 10000;

    /**
     * 600Hz
     */
    private int SENSOR_RATE_SPECIFIC = 1667;

    public SimpleRate() {
    };

    /**
     * 50Hz
     * @return
     */
    public int get_SENSOR_RATE_NORMAL() {
        return this.SENSOR_RATE_NORMAL;
    }

    /**
     * 80Hz
     *
     * @return
     */
    public int get_SENSOR_RATE_MIDDLE() {
        return this.SENSOR_RATE_MIDDLE;
    }

    /**
     * 100Hz
     *
     * @return
     */
    public int get_SENSOR_RATE_FAST() {
        return this.SENSOR_RATE_FAST;
    }

    public int get_SENSOR_RATE_SPECIFIC() {
        return this.SENSOR_RATE_SPECIFIC;
    }

}