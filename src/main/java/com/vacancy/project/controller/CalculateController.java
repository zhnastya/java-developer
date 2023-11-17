package com.vacancy.project.controller;

import com.vacancy.project.dto.RequestDto;
import com.vacancy.project.service.CalculateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class CalculateController {
    private final CalculateService service;

    @PostMapping
    public Object[][] save(@Valid @RequestBody RequestDto dto) throws SQLException {
        return service.saveValue(dto);
    }

    @GetMapping("/history")
    public Map<String, String> getStory() {
        return service.getStory();
    }

    @GetMapping("/history?x={x}&y={y}")
    public String getStoryByCoord(@PathVariable String x,
                                  @PathVariable int y) {
        return service.getStoryByCoord(x, y);
    }
}
