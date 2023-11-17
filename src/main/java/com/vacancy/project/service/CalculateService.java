package com.vacancy.project.service;

import com.vacancy.project.dto.RequestDto;

import java.sql.SQLException;
import java.util.Map;


public interface CalculateService {
    Object[][] saveValue(RequestDto dto) throws SQLException;

    Map<String, String> getStory();

    String getStoryByCoord(String x, int y);
}
