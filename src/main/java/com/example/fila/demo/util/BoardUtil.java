package com.example.fila.demo.util;

import com.example.fila.demo.constant.BoardTile;
import com.example.fila.demo.dto.BoardSizeRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
public class BoardUtil {

    // create board with multi 2D array
    public static List<List<String>> createEmpty(BoardSizeRequest request) {
        List<List<String>> rows = new ArrayList<>();

        for (int rowIndex = 0; rowIndex < request.getRows(); rowIndex++) {
            List<String> row = new ArrayList<>();
            for (int columnIndex = 0; columnIndex < request.getColumn(); columnIndex++) {
                row.add(BoardTile.EMPTY.toString());

            }
            rows.add(row);
        }

        return rows;
    }

    public static String getRandomAvailableTile(List<List<String>> rows) {
        log.info("param random tile: {}",rows);
        List<String> available = new ArrayList<>();

        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            List<String> row = rows.get(rowIndex);

            for (int columnIndex = 0; columnIndex < rows.size(); columnIndex++) {
                String tileValue = row.get(columnIndex);
                if (tileValue.isEmpty()) { // set available tile if some value form array by index is empty
                    available.add(rowIndex + "-" + columnIndex);
                }
            }
        }

        if (available.isEmpty()) {
            return null;
        }

        int randomNum = new Random().nextInt(available.size()); // set random number between 0 and value of available tile array
        return available.get(randomNum);

    }

    // get all line with reserved index
    public static List<List<String>> getAllLines(List<List<String>> rows, Integer boarSize) {
        List<List<String>> lines = new ArrayList<>();

        for (int rowIndex = 0; rowIndex < boarSize; rowIndex++) {
            lines.add(rows.get(rowIndex));
        }

        for (int columnIndex = 0; columnIndex < boarSize; columnIndex++) {
            List<String> columnLine = new ArrayList<>();
            for (List<String> row : rows) {
                columnLine.add(row.get(columnIndex));
            }
            lines.add(columnLine);
        }

        List<String> diagonal1 = Arrays.asList(rows.get(0).get(0), rows.get(1).get(1), rows.get(2).get(2));
        log.info("diagonal 1 val: {}",diagonal1);
        lines.add(diagonal1);

        List<String> diagonal2 = Arrays.asList(rows.get(0).get(2), rows.get(1).get(1), rows.get(2).get(0));
        log.info("diagonal 2 val: {}",diagonal2);
        lines.add(diagonal2);

        return lines;
    }
}
