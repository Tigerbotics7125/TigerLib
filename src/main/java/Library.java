import edu.wpi.first.math.util.Units;

public class Library {
    
    /**
     * @return true
     */
    public static boolean returnTrue() {
        return true;
    }

    /**
     * @return 360 degrees in radians
     */
    public static double getRadians() {
        return Units.degreesToRadians(360);
    }

}
