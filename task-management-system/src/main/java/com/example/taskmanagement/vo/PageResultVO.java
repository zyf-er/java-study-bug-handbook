package com.example.taskmanagement.vo;

import java.util.List;

public record PageResultVO<T>(
        long current,
        long size,
        long total,
        List<T> records
) {
}

