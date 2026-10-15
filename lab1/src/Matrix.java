public class Matrix {
    public Complex[][] matrix;
    private int rows;
    private int cols;

    public Matrix(Complex[][] matrix) {
        if (matrix == null || matrix.length == 0 ||
                matrix[0] == null || matrix[0].length == 0) {
            throw new IllegalArgumentException("Матрица не должна быть пустой");
        }
        this.rows = matrix.length;
        this.cols = matrix[0].length;
        for (int i = 0; i < rows; i++) {
            if (matrix[i] == null || matrix[i].length != cols) {
                throw new IllegalArgumentException("Некорректные размеры матрицы");
            }
        }
        this.matrix = matrix;
    }

    public Complex[][] sMatrix(Complex[][] matrix2) {
        if (rows != matrix2.length || cols != matrix2[0].length) {
            throw new IllegalArgumentException("Матрицы должны иметь одинаковые размеры");
        }
        Complex[][] result = new Complex[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = Complex.summ(matrix[i][j], matrix2[i][j]);
            }
        }
        return result;
    }

    public Complex[][] mMatrix(Complex[][] matrix2) {
        if (cols != matrix2.length) {
            throw new IllegalArgumentException("Несовместимые размеры матриц");
        }
        Complex[][] result = new Complex[rows][matrix2[0].length];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                result[i][j] = new Complex(0);
                for (int k = 0; k < cols; k++) {
                    result[i][j] = Complex.summ(
                            result[i][j],
                            Complex.mult(matrix[i][k], matrix2[k][j])
                    );
                }
            }
        }
        return result;
    }

    public Complex[][] transpose() {
        Complex[][] result = new Complex[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[j][i] = matrix[i][j];
            }
        }
        return result;
    }

    public Complex determinant(Complex[][] matrix2) {
        if (matrix2.length != matrix2[0].length) {
            throw new IllegalArgumentException("Матрица должна быть квадратной");
        }
        int n = matrix2.length;
        Complex[][] copy = new Complex[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                copy[i][j] = new Complex(
                        matrix2[i][j].getTruepart(),
                        matrix2[i][j].getFalsepart()
                );
            }
        }
        int sign = 1;
        for (int r = 0; r < n; r++) {
            if (copy[r][r].getTruepart() == 0 &&
                    copy[r][r].getFalsepart() == 0) {
                int k = r + 1;
                while (k < n &&
                        copy[k][r].getTruepart() == 0 &&
                        copy[k][r].getFalsepart() == 0) {
                    k++;
                }
                if (k == n) {
                    return new Complex(0);
                }
                Complex[] temp = copy[r];
                copy[r] = copy[k];
                copy[k] = temp;
                sign = -sign;
            }
            for (int i = r + 1; i < n; i++) {
                if (copy[i][r].getTruepart() != 0 ||
                        copy[i][r].getFalsepart() != 0) {
                    Complex k = Complex.del(copy[i][r], copy[r][r]);
                    for (int j = r; j < n; j++) {
                        copy[i][j] = Complex.razn(
                                copy[i][j],
                                Complex.mult(k, copy[r][j])
                        );
                    }
                }
            }
        }
        Complex result = new Complex(sign);
        for (int i = 0; i < n; i++) {
            result = Complex.mult(result, copy[i][i]);
        }
        return result;
    }

    public Complex[][] inverse() {
        if (rows != cols) {
            throw new IllegalArgumentException("Матрица должна бытьй");
        }
        int n = rows;
        Complex[][] a = new Complex[n][n];
        Complex[][] b = new Complex[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = new Complex(
                        matrix[i][j].getTruepart(),
                        matrix[i][j].getFalsepart()
                );
                b[i][j] = new Complex(i == j ? 1 : 0);
            }
        }
        for (int i = 0; i < n; i++) {
            if (a[i][i].getTruepart() == 0 &&
                    a[i][i].getFalsepart() == 0) {
                int k = i + 1;
                while (k < n &&
                        a[k][i].getTruepart() == 0 &&
                        a[k][i].getFalsepart() == 0) {
                    k++;
                }
                if (k == n) {
                    throw new ArithmeticException("Матрица необратима");
                }
                Complex[] temp = a[i];
                a[i] = a[k];
                a[k] = temp;
                temp = b[i];
                b[i] = b[k];
                b[k] = temp;
            }
            Complex divisor = a[i][i];
            for (int j = 0; j < n; j++) {
                a[i][j] = Complex.del(a[i][j], divisor);
                b[i][j] = Complex.del(b[i][j], divisor);
            }
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    Complex factor = a[k][i];
                    for (int j = 0; j < n; j++) {
                        a[k][j] = Complex.razn(
                                a[k][j],
                                Complex.mult(factor, a[i][j])
                        );
                        b[k][j] = Complex.razn(
                                b[k][j],
                                Complex.mult(factor, b[i][j])
                        );
                    }
                }
            }
        }
        return b;
    }

    public Complex[][] dMatrix(Complex[][] matrix2) {
        if (matrix2.length == 0 ||
                matrix2.length != matrix2[0].length) {
            throw new IllegalArgumentException("Вторая матрица должна быть квадратной");
        }
        if (cols != matrix2.length) {
            throw new IllegalArgumentException("Несовместимые размеры матриц");
        }
        Matrix m2 = new Matrix(matrix2);
        return mMatrix(m2.inverse());
    }
}