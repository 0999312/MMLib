package cn.mcmod_mmf.mmlib.utils;

public class MathUtil {
    public static double convertTicksToSeconds(double ticks) {
        return ticks / 20;
    }

    public static double convertSecondsToTicks(double seconds) {
        return seconds * 20;
    }

    public static float lerpValues(double percentCompleted, double startValue, double endValue) {
        // current tick / position should be between 0 and 1 and represent the
        // percentage of the lerping that has completed
        return (float) lerp(percentCompleted, startValue, endValue);
    }

    public static double lerp(double pct, double start, double end) {
        return start + pct * (end - start);
    }

    public static double sinValueIn(double a, double x) {
        return a * Math.sin(Math.toRadians(x));
    }

    public static double cosValueIn(double a, double x) {
        return a * Math.cos(Math.toRadians(x));
    }

}
