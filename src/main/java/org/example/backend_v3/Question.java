package org.example.backend_v3;

import java.util.List;

public record Question(String questionText, List<String> options, String correctOption) {}
