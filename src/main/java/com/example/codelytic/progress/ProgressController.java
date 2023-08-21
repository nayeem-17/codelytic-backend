package com.example.codelytic.progress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProgressController {
    @Autowired
    private ProgressService progressService;
}
