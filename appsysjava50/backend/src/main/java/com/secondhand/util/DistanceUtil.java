package com.secondhand.util;

/**
 * 距离计算工具类
 *
 * @author secondhand
 * @version 1.0.0
 */
public class DistanceUtil {

    private static final double EARTH_RADIUS = 6371.0;

    /**
     * 计算两点之间的距离（单位：千米）
     *
     * @param lat1 点1纬度
     * @param lon1 点1经度
     * @param lat2 点2纬度
     * @param lon2 点2经度
     * @return 距离（千米）
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return Math.round(EARTH_RADIUS * c * 100.0) / 100.0;
    }

    /**
     * 判断距离是否小于5公里
     */
    public static boolean isWithin5km(double lat1, double lon1, double lat2, double lon2) {
        return calculateDistance(lat1, lon1, lat2, lon2) <= 5.0;
    }

}
