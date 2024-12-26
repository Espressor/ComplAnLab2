/**
 * A simple complex number class (only required methods)
 *
 * @param real
 * @param imaginary
 */
public record SimpleComplex(double real, double imaginary) implements Cloneable {

    public SimpleComplex add(SimpleComplex other) {
        return new SimpleComplex(real + other.real, imaginary + other.imaginary);
    }

    public SimpleComplex add(double real) {
        return new SimpleComplex(this.real + real, imaginary);
    }

    public SimpleComplex subtract(SimpleComplex other) {
        return new SimpleComplex(real - other.real, imaginary - other.imaginary);
    }

    public SimpleComplex multiply(SimpleComplex other) {
        double realPart = real * other.real - imaginary * other.imaginary;
        double imaginaryPart = real * other.imaginary + imaginary * other.real;
        return new SimpleComplex(realPart, imaginaryPart);
    }

    public SimpleComplex divide(SimpleComplex other) {
        double realPart = (real * other.real + imaginary * other.imaginary) / other.squaredAbs();
        double imaginaryPart = (real * other.imaginary - imaginary * other.real) / other.squaredAbs();
        return new SimpleComplex(realPart, imaginaryPart);
    }

    public SimpleComplex multiply(double real) {
        return new SimpleComplex(this.real * real, real * imaginary);
    }

    public SimpleComplex divide(double real) {
        return new SimpleComplex(this.real / real, imaginary / real);
    }

    public SimpleComplex conjugated() {
        return new SimpleComplex(real, -imaginary);
    }

    public SimpleComplex square() {
        return multiply(this);
    }

    public SimpleComplex power(double power) {
        double R = abs();
        double theta = Math.atan2(imaginary, real);
        if (imaginary < 0) theta += 2 * Math.PI;
        double newR = Math.pow(R, power);
        double newTheta = theta * power;
        return new SimpleComplex(newR * Math.cos(newTheta), newR * Math.sin(newTheta));
    }

    public double abs() {
        return Math.sqrt(real * real + imaginary * imaginary);
    }

    public double squaredAbs() {
        return real * real + imaginary * imaginary;
    }

    public SimpleComplex exp() {
        //e^z = e^(Rez)(CosImz + iSinImz)
        double R = Math.exp(real);
        return new SimpleComplex(R * Math.cos(imaginary), R * Math.sin(imaginary));
    }

    public SimpleComplex cos() {
        //Cos z = (e^iz + e^-iz)/2 = [e^-Im (CosRe + iSinRe) + e^Im (CosRe - iSinRe)]/2
        double R1 = Math.exp(-imaginary) / 2;
        double R2 = Math.exp(imaginary) / 2;
        double cosRe = Math.cos(real);
        double sinRe = Math.sin(real);
        return new SimpleComplex(R1 * cosRe + R2 * cosRe, R1 * sinRe - R2 * sinRe);
    }

    @Override
    public SimpleComplex clone() {
        return new SimpleComplex(real, imaginary);
    }

    @Override
    public String toString() {
        return "(" + real + " + " + imaginary + "i)";
    }
}
