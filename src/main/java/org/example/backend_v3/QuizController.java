package org.example.backend_v3;

import org.apache.poi.xwpf.usermodel.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class QuizController {

    @GetMapping("/questions")
    public List<Question> getQuestions() {
        return loadQuestions("test.docx");
    }

    private List<Question> loadQuestions(String filePath) {
        List<Question> questions = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             XWPFDocument document = new XWPFDocument(fis)) {

            List<String> lines = new ArrayList<>();
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                String text = paragraph.getText().trim();
                if (!text.isEmpty()) {
                    lines.add(text);
                }
            }

            String questionText = "";
            List<String> options = new ArrayList<>();
            String correctAnswer = "";

            for (String line : lines) {
                if (line.matches("^[*]?[A-D]\\).*")) { // Variant boshi *A) yoki A)
                    if (line.startsWith("*")) { // *A) bo‘lsa
                        correctAnswer = line.substring(1, 2); // A yoki B yoki C yoki D
                        options.add(line.substring(1).trim()); // * ni olib tashlaymiz
                    } else {
                        options.add(line.trim());
                    }
                } else {
                    if (!questionText.isEmpty() && !options.isEmpty()) {
                        questions.add(new Question(questionText, options, correctAnswer));
                    }
                    questionText = line;
                    options = new ArrayList<>();
                    correctAnswer = "";
                }
            }

            if (!questionText.isEmpty() && !options.isEmpty()) {
                questions.add(new Question(questionText, options, correctAnswer));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return questions;
    }
}

class Question {
    private final String questionText;
    private final List<String> options;
    private final String correctOption;

    public Question(String questionText, List<String> options, String correctOption) {
        this.questionText = questionText;
        this.options = options;
        this.correctOption = correctOption;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectOption() {
        return correctOption;
    }
}
