package com.quizapp.quizapp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class QuestionGenerator {

    public static void windQuestion(List<Question> questions) {
        ArrayList<String> multiChoiceData = multiChoiWind();
        String correctAnswer = multiChoiceData.get(0);
        String[] options = {
                multiChoiceData.get(0),
                multiChoiceData.get(1),
                multiChoiceData.get(2),
                multiChoiceData.get(3)
        };

        // Shuffle the options
        String[] shuffledOptions = shuffleOptions(options);
        String questionText = multiChoiceData.get(4);

        // Add the question to the list
        questions.add(new MultipleChoiceQuestion(questionText, shuffledOptions, correctAnswer));
    }
    /**
     * Forms a type of multiple choice question about wind on ocean.
     * @return ret A String ArrayList of a few Strings needed to create this multiple choice question.
     */
    public static ArrayList<String> multiChoiWind() {
        double randomW = Double.parseDouble(new DecimalFormat("#.##").format(ThreadLocalRandom.current().nextDouble(1.00,10.00)).replace(",","."));
        double randomS = Double.parseDouble(new DecimalFormat("#.##").format(ThreadLocalRandom.current().nextDouble(0.10,5.00)).replace(",","."));
        double wave = randomW;
        double speed = randomS;
        String questionText = String.format("Wind gusts create ripples on the ocean " +
                "that have a wavelength of %,.2f cm and propagate at %,.2f m/s. What is their frequency?", wave, speed);

        double correctAnswer = speed / (wave / 100);
        double randomAnswer1 = Double.parseDouble(new DecimalFormat("#.##").format(ThreadLocalRandom.current().nextDouble(1.00,500.00)).replace(",","."));
        double randomAnswer2 = Double.parseDouble(new DecimalFormat("#.##").format(ThreadLocalRandom.current().nextDouble(1.00,500.00)).replace(",","."));
        double randomAnswer3 = Double.parseDouble(new DecimalFormat("#.##").format(ThreadLocalRandom.current().nextDouble(1.00,500.00)).replace(",","."));

        String answer = String.format("%,.2f", correctAnswer);
        ArrayList<String> ret = new ArrayList<>();
        ret.add(answer);
        ret.add(Double.toString(randomAnswer1));
        ret.add(Double.toString(randomAnswer2));
        ret.add(Double.toString(randomAnswer3));
        ret.add(questionText);
        return ret;
    }

    /**
     * Shuffles the options of a multiple choice question in random orders.
     * @param options A String Array of options, in a non-random order.
     * @return shuffleOptions A String Array of options, in shuffled order.
     */
    public static String[] shuffleOptions(String[] options) {
        List<String> shuffle = Arrays.asList(options);
        Collections.shuffle(shuffle);
        String[] shuffledOptions = new String[shuffle.size()];
        for (int i = 0; i < shuffle.size(); i++) {
            shuffledOptions[i] = shuffle.get(i);
        }
        return shuffledOptions;
    }
}
