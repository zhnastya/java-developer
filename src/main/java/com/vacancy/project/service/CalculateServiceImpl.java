package com.vacancy.project.service;

import com.vacancy.project.dto.RequestDto;
import com.vacancy.project.exeptions.NotFoundExeption;
import com.vacancy.project.exeptions.ValidationExeption;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CalculateServiceImpl implements CalculateService {
    private final Object[][] matrix = new Object[4][4];
    private final Map<String, String> history = new HashMap<>();


    @Override
    public Object[][] saveValue(RequestDto dto) {
        if (!dto.getExp().startsWith("=")) throw new ValidationExeption("Вы ввели неверный формат выражения");
        Object[][] objects = convertExpression(dto.getExp().substring(1), dto.getLine(), dto.getColumn());
        updateValue();
        return objects;
    }

    @Override
    public Map<String, String> getStory() {
        return history;
    }

    @Override
    public String getStoryByCoord(String x, int y) {
        return history.get(x + y);
    }

    private void updateValue() {
        for (Map.Entry<String, String> entry : history.entrySet()) {
            convertExpression(entry.getValue(), entry.getKey().substring(0, 1),
                    Integer.parseInt(entry.getKey().substring(1)));
        }
    }

    private Object[][] convertExpression(String value, String x, int y) {
        Pattern patternWord = Pattern.compile("[А-Г]");
        Iterator<Character> charIterable = value.chars().mapToObj(c -> (char) c)
                .toList().iterator();
        history.put(x + y, value);

        while (charIterable.hasNext()) {
            String c = String.valueOf(charIterable.next());
            Matcher matcherWord = patternWord.matcher(c);
            if (matcherWord.matches()) {
                try {
                    String num = String.valueOf(charIterable.next());
                    Object objects = matrix[mapper(c)][Integer.parseInt(num)];
                    String coor = c + num;
                    if (objects == null) throw new NotFoundExeption("Значение с координатой - " + coor + " не найдено");
                    value = value.replace(coor, String.valueOf(objects));
                } catch (NoSuchElementException | NumberFormatException e) {
                    throw new ValidationExeption("Вы ввели неверный параметр запроса - " + c);
                }
            }
        }

        //Создаю временную базу для рассчета выражений
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite::memory:");
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("select " + value + " amount");
            rs.next();
            matrix[mapper(x)][y] = rs.getBigDecimal(1);
            stat.close();
            conn.close();
            return matrix;
        } catch (ClassNotFoundException | SQLException exception) {
            throw new ValidationExeption("Вы ввели неверный формат выражения");
        }
    }

    private int mapper(String c) {
        return switch (c) {
            case "А" -> 0;
            case "Б" -> 1;
            case "В" -> 2;
            case "Г" -> 3;
            default -> throw new ValidationExeption("Unexpected value: " + c);
        };
    }
}
