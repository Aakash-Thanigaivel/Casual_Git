package com.example.sqlstatecheck.controller;

import com.example.sqlstatecheck.dto.RequestDto;
import com.example.sqlstatecheck.dto.ResponseDto;
import com.example.sqlstatecheck.service.SqlStateCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class SqlStateCheckController {

    @Autowired
    private SqlStateCheckService sqlStateCheckService;

    @PostMapping("/sqlstate-check")
    public ResponseDto checkSqlState(@Valid @RequestBody RequestDto requestDto) {
        return sqlStateCheckService.processRequest(requestDto);
    }
}