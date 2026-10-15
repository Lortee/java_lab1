public class Complex {
    private double truepart;
    private double mnimpart;

    public Complex(double truepart, double mnimpart) {
        this.truepart = truepart;
        this.mnimpart = mnimpart;
    }

    public Complex(double truepart) {
        this.truepart = truepart;
        this.mnimpart = 0;
    }

    public double getTruepart() {
        return truepart;
    }

    public double getFalsepart() {
        return mnimpart;
    }

    public static Complex summ(Complex num1, Complex num2) {
        return new Complex(
                num1.getTruepart() + num2.getTruepart(),
                num1.getFalsepart() + num2.getFalsepart()
        );
    }

    public static Complex razn(Complex num1, Complex num2) {
        return new Complex(
                num1.getTruepart() - num2.getTruepart(),
                num1.getFalsepart() - num2.getFalsepart()
        );
    }

    public static Complex mult(Complex num1, Complex num2) {
        double a = num1.getTruepart();
        double b = num1.getFalsepart();
        double c = num2.getTruepart();
        double d = num2.getFalsepart();

        return new Complex(a * c - b * d, a * d + b * c);
    }

    public static Complex del(Complex num1, Complex num2) {
        double a = num1.getTruepart();
        double b = num1.getFalsepart();
        double c = num2.getTruepart();
        double d = num2.getFalsepart();
        double znam = c * c + d * d;

        if (znam == 0) {
            throw new ArithmeticException("Деление на ноль");
        }

        return new Complex(
                (a * c + b * d) / znam,
                (b * c - a * d) / znam
        );
    }
}