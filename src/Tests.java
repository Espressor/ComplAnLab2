public class Tests {

    public static void main(String[] args) {
        SimpleComplex c1 = new SimpleComplex(1, 2);
        SimpleComplex c2 = new SimpleComplex(-3, 4);

        System.out.println("Square root of " + c1 + " = " + c1.power(0.5));
        System.out.println("Square root of " + c2 + " = " + c2.power(0.5));
    }
}
