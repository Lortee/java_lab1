import java.util.Scanner;

public class Main {
    public static Complex[][] readMatrix(Scanner sc, String name) {
        while (true) {
            System.out.println("Введите количество строк и столбцов матрицы " + name + ":");
            System.out.println("Для возврата введите 'назад'.");
            String line = sc.nextLine().trim();

            if (line.equalsIgnoreCase("назад")) {
                return null;
            }

            String[] sizes = line.split("\\s+");
            int rows;
            int cols;

            try {
                if (sizes.length != 2) {
                    System.out.println("Введите два числа через пробел.");
                    continue;
                }

                rows = Integer.parseInt(sizes[0]);
                cols = Integer.parseInt(sizes[1]);
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат размеров.");
                continue;
            }

            if (rows <= 0 || cols <= 0) {
                System.out.println("Размеры должны быть положительными.");
                continue;
            }

            Complex[][] matrix = new Complex[rows][cols];
            boolean valid = true;

            System.out.println("Введите элементы построчно.");
            System.out.println("Примеры: 2+3i  4-i  5");

            for (int i = 0; i < rows; i++) {
                System.out.println("Строка " + (i + 1) + ":");
                String row = sc.nextLine().trim();

                if (row.equalsIgnoreCase("назад")) {
                    return null;
                }

                String[] elements = row.split("\\s+");

                if (elements.length != cols) {
                    System.out.println("Неверное количество элементов.");
                    valid = false;
                    break;
                }

                try {
                    for (int j = 0; j < cols; j++) {
                        matrix[i][j] = parseComplex(elements[j]);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка ввода комплексного числа.");
                    valid = false;
                    break;
                }
            }

            if (valid) {
                return matrix;
            }
        }
    }

    public static Complex parseComplex(String s) {
        s = s.replace(" ", "").replace("−", "-");

        if (!s.contains("i")) {
            return new Complex(Double.parseDouble(s), 0);
        }

        s = s.replace("i", "");

        if (s.isEmpty() || s.equals("+")) {
            return new Complex(0, 1);
        }

        if (s.equals("-")) {
            return new Complex(0, -1);
        }

        int pos = -1;

        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == '+' || s.charAt(i) == '-') {
                pos = i;
            }
        }

        if (pos == -1) {
            return new Complex(0, Double.parseDouble(s));
        }

        double real = Double.parseDouble(s.substring(0, pos));
        String imag = s.substring(pos);

        if (imag.equals("+")) imag = "1";
        if (imag.equals("-")) imag = "-1";

        return new Complex(real, Double.parseDouble(imag));
    }

    public static String formatComplex(Complex c) {
        double real = c.getTruepart();
        double imag = c.getFalsepart();

        if (imag == 0) return String.valueOf(real);

        if (real == 0) {
            if (imag == 1) return "i";
            if (imag == -1) return "-i";
            return imag + "i";
        }

        if (imag == 1) return real + "+i";
        if (imag == -1) return real + "-i";

        return real + (imag > 0 ? "+" : "") + imag + "i";
    }

    public static void printMatrix(Complex[][] matrix) {
        for (Complex[] row : matrix) {
            for (Complex c : row) {
                System.out.print(formatComplex(c) + "\t");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Complex[][] lastResult = null;
        boolean running = true;

        while (running) {
            System.out.println("\n=== МАТРИЦЫ КОМПЛЕКСНЫХ ЧИСЕЛ ===");
            System.out.println("    1 - Сложение матриц");
            System.out.println("    2 - Умножение матриц");
            System.out.println("    3 - Деление матриц A / B");
            System.out.println("    4 - Транспонирование матрицы");
            System.out.println("    5 - Определитель матрицы");
            System.out.println("    6 - Показать последний результат");
            System.out.println("    выход - Завершить работу");
            System.out.print("Выберите операцию: ");

            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("выход")) {
                running = false;
                continue;
            }

            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Введите номер операции или 'выход'.");
                continue;
            }

            if (choice == 6) {
                if (lastResult == null) {
                    System.out.println("Результатов пока нет.");
                } else {
                    System.out.println("Последний результат:");
                    printMatrix(lastResult);
                }
                continue;
            }

            if (choice < 1 || choice > 5) {
                System.out.println("Неизвестная операция.");
                continue;
            }

            try {
                Complex[][] a;

                if (lastResult != null) {
                    System.out.println("Использовать последний результат как матрицу A?");
                    System.out.println("1 - Да");
                    System.out.println("2 - Ввести новую матрицу");

                    String answer = sc.nextLine().trim();

                    if (answer.equals("1")) {
                        a = lastResult;
                        System.out.println("Матрица A:");
                        printMatrix(a);
                    } else if (answer.equals("2")) {
                        a = readMatrix(sc, "A");
                        if (a == null) continue;
                    } else {
                        System.out.println("Неверный выбор.");
                        continue;
                    }
                } else {
                    a = readMatrix(sc, "A");
                    if (a == null) continue;
                }

                Matrix m1 = new Matrix(a);
                Complex[][] result = null;

                switch (choice) {
                    case 1: {
                        Complex[][] b = readMatrix(sc, "B");
                        if (b == null) continue;

                        result = m1.sMatrix(b);
                        break;
                    }

                    case 2: {
                        Complex[][] b = readMatrix(sc, "B");
                        if (b == null) continue;

                        result = m1.mMatrix(b);
                        break;
                    }

                    case 3: {
                        Complex[][] b = readMatrix(sc, "B");
                        if (b == null) continue;

                        result = m1.dMatrix(b);
                        break;
                    }

                    case 4:
                        result = m1.transpose();
                        break;

                    case 5: {
                        Complex det = m1.determinant(a);
                        result = new Complex[][]{{det}};
                        System.out.println("Определитель: " + formatComplex(det));
                        break;
                    }
                }

                if (result != null) {
                    lastResult = result;
                    System.out.println("Результат операции:");
                    printMatrix(lastResult);
                    System.out.println("Результат сохранён.");
                }

            } catch (IllegalArgumentException | ArithmeticException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }

            System.out.println("Возвращение в главное меню...");
        }

        sc.close();
        System.out.println("Программа завершена.");
    }
}