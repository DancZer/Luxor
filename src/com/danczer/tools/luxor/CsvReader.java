package com.danczer.tools.luxor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CsvReader {

    private Path _filePath;

    private HashMap<Character, List<Integer>> _weekValues = new HashMap<>();

    private Integer _lastNumber = -1;

    public CsvReader(Path filePath) {
        _filePath = filePath;
    }

    public void ReadWeek(int year, int week) throws Exception {

        String[] weekValues = null;

        for (String line : Files.readAllLines(_filePath)) {
            String[] values = ParseLine(line);

            if (values.length < 3) {
                throw new Exception("Invalid line!");
            }

            int y = Integer.parseInt(values[0]);
            int w = Integer.parseInt(values[1]);

            if (y == year && w == week) {
                weekValues = values;
                break;
            }
        }

        if (weekValues != null) {
            CategorizeNumbers(weekValues);

            _weekValues.values().forEach(Collections::sort);
        }
    }

    public void PrintValues(){
        for (Character c : new Character[]{'L','U','X','O','R'}) {
            System.out.print(c);
            System.out.print(" : ");

            List<Integer> values = _weekValues.get(c);

            if(values == null){
                System.out.println();
                continue;
            }

            for (Integer value: values) {
                System.out.print(value);
                System.out.print(", ");
            }

            System.out.println();
        }

        System.out.println("Last number: "+_lastNumber);
    }

    private String[] ParseLine(String line) {
        return line.split(";");
    }

    private void CategorizeNumbers(String[] weekValues) throws Exception {
        for (int i = 7; i < weekValues.length; i++) {
            int value = Integer.parseInt(weekValues[i]);

            _lastNumber = value;

            PutNumber(value);
        }
    }

    private void PutNumber(int number) throws Exception {
        Character cat = GetCategory(number);

        List<Integer> categoryNumbers;

        if (_weekValues.containsKey(cat)) {
            categoryNumbers = _weekValues.get(cat);
        } else {
            categoryNumbers = new ArrayList<>();
            _weekValues.put(cat, categoryNumbers);
        }

        categoryNumbers.add(number);
    }

    private Character GetCategory(int number) throws Exception {
        if(number < 1){
            throw new Exception("Invalid value: "+number);
        }else if(number <= 15){
            return 'L';
        }else if(number <= 30){
            return 'U';
        }else if(number <= 45){
            return 'X';
        }else if(number <= 60){
            return 'O';
        }else if(number <= 75){
            return 'R';
        }else{
            throw new Exception("Invalid value: "+number);
        }
    }
}
