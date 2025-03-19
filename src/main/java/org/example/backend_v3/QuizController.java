package org.example.backend_v3;

//import org.example.backend_v3.Question; // Import qilish kerak
import org.springframework.core.io.ClassPathResource;
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
        return loadQuestions();
    }

    private List<Question> loadQuestions() {
        List<Question> questions = new ArrayList<>();
        try (InputStream is = new ClassPathResource("test.docx").getInputStream();
             XWPFDocument document = new XWPFDocument(is)) {

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
                if (line.matches("^[*]?[A-D]\\).*")) {
                    if (line.startsWith("*")) {
                        correctAnswer = line.substring(1, 2);
                        options.add(line.substring(1).trim());
                    } else {
                        options.add(line.trim());
                    }
                } else {
                    if (!questionText.isEmpty() && options.size() >= 3) {
                        questions.add(new Question(questionText, options, correctAnswer));
                    }
                    questionText = line;
                    options = new ArrayList<>();
                    correctAnswer = "";
                }
            }

            if (!questionText.isEmpty() && options.size() >= 3) {
                questions.add(new Question(questionText, options, correctAnswer));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return questions;
    }
}
