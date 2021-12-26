package com.epam.izh.rd.online.service;

import com.epam.izh.rd.online.repository.FileRepository;
import com.epam.izh.rd.online.repository.SimpleFileRepository;

import java.io.File;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleRegExpService implements RegExpService {
    private FileRepository fileRepository = new SimpleFileRepository();

    /**
     * Метод должен читать файл sensitive_data.txt (из директории resources) и маскировать в нем конфиденциальную информацию.
     * Номер счета должен содержать только первые 4 и последние 4 цифры (1234 **** **** 5678). Метод должен содержать регулярное
     * выражение для поиска счета.
     *
     * @return обработанный текст
     */
    @Override
    public String maskSensitiveData() {
        String input = fileRepository.readFileFromResources("sensitive_data.txt");
        input = input.replaceAll("\\r\\n", "");
        Pattern pattern = Pattern.compile("\\d{4}\\s\\d{4}\\s\\d{4}\\s\\d{4}");
        Matcher matcher = pattern.matcher(input);
        String result = input;
        while (matcher.find()) {
            StringBuilder cardNumber = new StringBuilder(matcher.group());
            cardNumber.replace(5, 9, "****");
            cardNumber.replace(10, 14, "****");
            String maskNumber = cardNumber.toString();
            result = result.replaceAll(matcher.group(), maskNumber);
        }

        return result;
    }

    /**
     * Метод должен считыввать файл sensitive_data.txt (из директории resources) и заменять плейсхолдер ${payment_amount} и ${balance} на заданные числа. Метод должен
     * содержать регулярное выражение для поиска плейсхолдеров
     *
     * @return обработанный текст
     */
    @Override
    public String replacePlaceholders(double paymentAmount, double balance) {
        String input = fileRepository.readFileFromResources("sensitive_data.txt");
        input = input.replaceAll("\\r\\n", "");
        Pattern pattern = Pattern.compile("\\$\\{(.*?)}");
        Matcher matcher = pattern.matcher(input);
        //    String toReplace = String.format("%.0f",paymentAmount);
        while (matcher.find()) {
            String find = matcher.group();
            if (find.equals("${payment_amount}")) {
                input = input.replace(find, String.format("%.0f", paymentAmount));
            } else if (find.equals("${balance}")) {
                input = input.replace(find, String.format("%.0f", balance));
            }

        }

        return input;
    }
}
