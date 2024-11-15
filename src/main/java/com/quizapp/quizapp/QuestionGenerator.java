package com.quizapp.quizapp;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class QuestionGenerator {
    // Multiple choices questions.
    /**
     * Forms a type of multiple choice question about wind on ocean.
     * @return ret A String ArrayList of a few Strings needed to create this multiple choice question.
     */
    public static ArrayList<String> multiChoiceWind() {
        // Create random numbers for the two variables of the question.
        double randomW = Double.parseDouble(new DecimalFormat("#.##").format(ThreadLocalRandom.current().nextDouble(1.00,10.00)).replace(",","."));
        double randomS = Double.parseDouble(new DecimalFormat("#.##").format(ThreadLocalRandom.current().nextDouble(0.10,5.00)).replace(",","."));
        // Choose two random values for the two variables.
        double wave = randomW;
        double speed = randomS;
        // Set up the question.
        String questionText = String.format("Wind gusts create ripples on the ocean " +
                "that have a wavelength of %,.2f cm and propagate at %,.2f m/s. What is their frequency?", wave, speed);
        // Set up the correct answer, and the three incorrect choices.
        double correctAnswer = speed / (wave / 100);
        double randomAnswer1 = Double.parseDouble(new DecimalFormat("#.##").format(ThreadLocalRandom.current().nextDouble(1.00,500.00)).replace(",","."));
        double randomAnswer2 = Double.parseDouble(new DecimalFormat("#.##").format(ThreadLocalRandom.current().nextDouble(1.00,500.00)).replace(",","."));
        double randomAnswer3 = Double.parseDouble(new DecimalFormat("#.##").format(ThreadLocalRandom.current().nextDouble(1.00,500.00)).replace(",","."));
        String answer = String.format("%,.2f", correctAnswer);
        // Set up help to show the steps and answer of the question.
        String help = String.format("The frequency of a wave can be calculated using the formula:\n" + "f = v/λ\n" +
                "Where: f is the frequency, v is the velocity of the wave, and λ is the wavelength of the wave.\n\n" +
                "Given that the velocity (v) of the wave is %,.2f m/s and the wavelength (λ) is %,.2f cm, " +
                "we can substitute these values into the formula to find the frequency:\n" +
                "f = (%,.2f m/s)/(%,.2f cm)\n" +
                "Don't forget to convert the unit of the wavelength! (100cm = 1m)\n\n" +
                "So, the frequency of the ripples on the ocean is %,.2f Hz.", speed, wave, speed, wave, correctAnswer);
        // Set up hint.
        String hint = "Think about the formula:\nf = v/λ";
        // Set up the return value for this method, including all needed values to create a wind question.
        ArrayList<String> ret = new ArrayList<>();
        ret.add(answer + " Hz");
        ret.add(randomAnswer1 + " Hz");
        ret.add(randomAnswer2 + " Hz");
        ret.add(randomAnswer3 + " Hz");
        ret.add(questionText);
        ret.add(help);
        ret.add(hint);
        return ret;
    }

    /**
     * Adds a wind multiple choice question into the questions that will be displayed to the user.
     * @param questions the ArrayList of all questions.
     */
    public static void windQuestion(List<Question> questions) {
        // Bring the values collected from multiChoiceWind().
        ArrayList<String> multiChoiceData = multiChoiceWind();
        // Separate each value.
        String correctAnswer = multiChoiceData.get(0);
        String questionText = multiChoiceData.get(4);
        String help = multiChoiceData.get(5);
        String hint = multiChoiceData.get(6);
        String[] options = {
                multiChoiceData.get(0),
                multiChoiceData.get(1),
                multiChoiceData.get(2),
                multiChoiceData.get(3)
        };
        // Shuffle the options.
        String[] shuffledOptions = shuffleOptions(options);
        // Add the question to the list.
        questions.add(new MultipleChoiceQuestion(questionText, shuffledOptions, correctAnswer, help, hint));
    }





    /**
     * Shuffles the options of a multiple choice question in random orders.
     * @param options A String Array of options, in a non-random order.
     * @return shuffleOptions A String Array of options, in shuffled order.
     */
    public static String[] shuffleOptions(String[] options) {
        // Put options to be shuffled in a List.
        List<String> shuffle = Arrays.asList(options);
        // Use Collections to shuffle.
        Collections.shuffle(shuffle);
        // Put shuffled options back to a String Array.
        String[] shuffledOptions = new String[shuffle.size()];
        for (int i = 0; i < shuffle.size(); i++) {
            shuffledOptions[i] = shuffle.get(i);
        }
        return shuffledOptions;
    }

    // Short answer questions.
    /**
     * Adds a terms short answer question into the questions that will be displayed to the user.
     * @param questions the ArrayList of all questions.
     */
    public static void termsQuestion(List<Question> questions) {
        // Create a map so the questions and answers always stay connected.
        Map<String, String> terms = new HashMap<>();
        terms.put("What is the term for the maximum displacement of an object from its equilibrium position in simple harmonic motion?", "Amplitude");
        terms.put("What is the term for the number of oscillations per unit time?", "Frequency");
        terms.put("What is the term for the time to complete a cycle?", "Period");
        // Shuffle the order of questions and answers.
        ArrayList<List<String>> shuffledQAndA = shuffleShorts(terms);
        // Set up each value needed to create the terms question.
        List<String> question = shuffledQAndA.get(0);
        List<String> correctAnswer = shuffledQAndA.get(1);
        String help = "The maximum displacement of an object from its equilibrium position in simple harmonic motion: Amplitude\n" +
                "The number of oscillations per unit time: Frequency\n" +
                "The time to complete a cycle: Period";
        String hint = "- The maximum displacement from equilibrium is often denoted by the letter 'A'.\n" +
                "- The number of oscillations per second is measured in Hertz (Hz).\n" +
                "- The time to complete one full cycle of motion is the inverse of frequency.";
        // Add the question to the list.
        questions.add(new ShortAnswerQuestion(question, correctAnswer, help, hint));
    }

    /**
     * Adds a slide short answer question into the questions that will be displayed to the user.
     * @param questions the ArrayList of all questions.
     */
    public static void slideQuestion(List<Question> questions) {
        // Create random numbers for the three variables of the question.
        double randomM = Double.parseDouble(new DecimalFormat("#.##").format(ThreadLocalRandom.current().nextDouble(0.10,0.99)).replace(",","."));
        double randomK = Double.parseDouble(new DecimalFormat("#.##").format(ThreadLocalRandom.current().nextDouble(10.00,50.00)).replace(",","."));
        double randomD = Double.parseDouble(new DecimalFormat("#.##").format(ThreadLocalRandom.current().nextDouble(10.00,100.00)).replace(",","."));
        // Choose three random values for the three variables.
        double mass = randomM;
        double spring = randomK;
        double distance = randomD;
        // Calculate answers for each question.
        double dInM = distance / 100;
        double eTot = 0.5 * spring * dInM * dInM;
        double potE = 0.5 * spring * (dInM / 2) * (dInM / 2);
        double correctAnswerA = Math.sqrt((spring / mass) * dInM * dInM);
        String stringCorrectAnswerA = String.format("%,.2f m/s", correctAnswerA);
        double correctAnswerB = Math.sqrt((eTot - potE) * 2 / mass);
        String stringCorrectAnswerB = String.format("%,.2f m/s", correctAnswerB);
        double correctAnswerC = Math.sqrt(eTot / spring);
        String stringCorrectAnswerC = String.format("%,.2f m", correctAnswerC);
        // Set up the questions and the correct answers.
        List<String> question = new ArrayList<>();
        question.add(String.format("A block of mass %,.2f kg can slide over a frictionless horizontal surface. " +
                "It is attached to a spring whose stiffness constant is k = %,.2f N/m. " +
                "The block is pulled %,.2f cm and let go. \na) What is its maximum speed? (Don't forget the units!)", mass, spring, distance));
        question.add(String.format("b) What is its speed when the extension is %,.2f cm?", distance / 2));
        question.add("c) At what position in meters is the kinetic energy equal to the potential energy?");
        List<String> correctAnswer = new ArrayList<>();
        correctAnswer.add(stringCorrectAnswerA);
        correctAnswer.add(stringCorrectAnswerB);
        correctAnswer.add(stringCorrectAnswerC);
        // Set up help to show the steps and answer of the question.
        String help = String.format("a) The maximum speed of the block is given by:\n" + "KEmax = PEmax\n" +
                "\u00BDmV\u00B2max = \u00BDkX\u00B2max\n" +
                "Where: m is the mass, Vmax is the maximum speed of the block, k is the spring constant and Xmax is the maximum extension.\n\n"
                + "Given that the mass (m) of the block is %,.2f kg, its spring constant (k) is %,.2f N/m and maximum extension (X) is %,.2f cm, "
                + "we can substitute these values into the formula to find the maximum speed:\n" +
                "Vmax = \u221A(((%,.2f N/m)/(%,.2f kg)) * (%,.2f cm)\u00B2)\n" +
                "Don't forget to convert the unit of the extension distance! (100cm = 1m)\n\n" +
                "So, the maximum speed of the block is %,.2f m/s.\n\n\n" +

                "b) The total energy of the spring is given by:\n" + "E = \u00BDkX\u00B2max\n" +
                "Substitute given values into the formula to find the total energy of the spring:\n" +
                "E = \u00BD(%,.2f N/m) * (%,.2f cm)\u00B2 = %,.2f J\n" +
                "Don't forget to convert the unit of the extension distance! (100cm = 1m)\n" +
                "The potential energy at %,.2f cm is:\n" + "PE = \u00BD(%,.2f N/m) * (%,.2f cm)\u00B2 = %,.2f J\n" +
                "The kinetic energy is given by:\n" + "KE = E - PE\nKE = %,.2f - %,.2f = %,.2f J" +
                "The speed when the extension is %,.2f is:\n" + "V = \u221A((%,.2f J) * 2 / (%,.2f kg))\n\n" +
                "So, the speed of the block when the extension is %,.2f cm is %,.2f m/s.\n\n\n" +

                "c) Here, the kinetic energy is equal to the potential energy. As the E = %,.2f J:\n" +
                "EK = EP = \u00BDE = \u00BD%,.2f J = %,.2f J\n" +
                "The value of position is:\n" + "X = \u221A((%,.2f J) * 2 / (%,.2f N/m))\n\n" +
                "So, the kinetic energy is equal to the potential energy when X = %,.2f m.", mass, spring, distance, spring, mass, distance, correctAnswerA, spring, distance, eTot, distance/2, spring, distance/2, potE, eTot, potE, eTot-potE, distance/2, eTot-potE, mass, distance/2, correctAnswerB, eTot, eTot, eTot/2, eTot/2, spring, correctAnswerC);
        String hint = "Here are some useful formulas:\n" +
                "KE = \u00BDmV\u00B2\nPE = \u00BDkX\u00B2\nE = EK + EP";
        // Add the question to the list.
        questions.add(new ShortAnswerQuestion(question, correctAnswer, help, hint));
    }

    /**
     * Shuffles the questions of a short answer question in random orders.
     * @param qAndA A String + String map of questions and answers, in a non-random order.
     * @return shuffleQAndA A String List ArrayList of Lists of questions and answers, in shuffled order.
     */
    public static ArrayList<List<String>> shuffleShorts(Map<String, String> qAndA) {
        // Put questions and answers to be shuffled in an ArrayList.
        List<Map.Entry<String, String>> shuffle = new ArrayList<>(qAndA.entrySet());
        // Use Collections to shuffle.
        Collections.shuffle(shuffle);
        // Put shuffled questions and answers in two corresponding String Lists.
        List<String> shuffledQ = new ArrayList<>();
        for (Map.Entry<String, String> shuffledQAndA : shuffle) {
            shuffledQ.add(shuffledQAndA.getKey());
        }
        List<String> shuffledA = new ArrayList<>();
        for (Map.Entry<String, String> shuffledQAndA : shuffle) {
            shuffledA.add(shuffledQAndA.getValue());
        }
        // Gather the two Lists together in an ArrayList.
        ArrayList<List<String>> shuffledQAndA = new ArrayList<>();
        shuffledQAndA.add(shuffledQ);
        shuffledQAndA.add(shuffledA);
        return shuffledQAndA;
    }
}
