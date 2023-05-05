import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Programm {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите данные в формате: Фамилия Имя Отчество ДатаРождения НомерТелефона Пол");

        String input = scanner.nextLine().trim();

        try {
            String[] parts = checkInput(input);
            String lastName = parts[0];
            String firstName = parts[1];
            String middleName = parts[2];
            String birthDateString = parts[3];
            String phoneNumberString = parts[4];
            String genderString = parts[5];

            Date birthDate = parseDate(birthDateString);
            Gender gender = parseGender(genderString);
            Boolean checkFIO = (checkStr(lastName) && checkStr(firstName) && checkStr(middleName));

            if (!checkFIO || !checkTel(phoneNumberString) || birthDate == null || gender == null) {
                throw new IllegalArgumentException("Ошибка парсинга данных");
            }

            String fileName = lastName + ".txt";
            String data = lastName + firstName + middleName + " " + birthDate.toString() + " " + phoneNumberString
                    + " " + gender.toString();

            saveToFile(fileName, data);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String[] checkInput(String usrInput) {
        String[] result = null;
        try {
            if (usrInput == null) {
                throw new IllegalArgumentException("Ошибка ввода данных, пустая строка.");
            }

            result = usrInput.split(" ");

            if (result.length != 6) {
                throw new IllegalArgumentException(
                        "Ошибка ввода данных, количество переданных параметров недостаточно.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    private static Boolean checkStr(String str) {
        if (str == null || str.isEmpty() || str.trim().isEmpty())
            throw new IllegalArgumentException("Параметр не содержит значения: " + str);

        return true;
    }

    private static Boolean checkTel(String phone) {
        final String patternTel = "\\d{10}";

        if (!phone.matches(patternTel)) {
            throw new IllegalArgumentException("Неверный формат телефона в строке " + phone);
        }

        return true;
    }

    private static Date parseDate(String date) {
        Date result;
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            format.setLenient(false);
            result = format.parse(date);
        } catch (ParseException e) {
            System.out.println("Ошибка: неверный формат даты рождения. Требуется формат dd.MM.yyyy.");
            return null;
        }

        return result;
    }

    private static Gender parseGender(String genderSymbol) {
        switch (genderSymbol) {
            case "m":
                return Gender.MALE;
            case "f":
                return Gender.FEMALE;
            default:
                throw new IllegalArgumentException("Неверный формат пола. Требуется символ 'm' или 'f'.");
        }
    }

    public static void saveToFile(String fileName, String data) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(data);
            writer.write(System.lineSeparator());

            writer.flush();
            writer.close();
            System.out.println("Данные успешно записаны в файл " + fileName);
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    enum Gender {
        MALE,
        FEMALE
    }
}