package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    private static BlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);
    private static BlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);
    private static BlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);
    private static int counterA = 0;
    private static String maxA = null;
    private static int counterB = 0;
    private static String maxB = null;
    private static int counterC = 0;
    private static String maxC = null;
    public static void main(String[] args) throws InterruptedException {



        Thread generatorThread = new Thread(() -> {
            String[] texts = new String[10_000];
            for (int i = 0; i < texts.length; i++) {
                texts[i] = generateText("abc", 100_000);
                try {
                    queueA.put(texts[i]);
                    queueB.put(texts[i]);
                    queueC.put(texts[i]);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        generatorThread.start();

        Thread threadA = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                int counter = 0;
                try {
                    String s = queueA.take();
                    for (int j = 0; j < s.length(); j++) {
                        if (s.charAt(j) == 'a') counter++;
                    }
                    if (counter > counterA) {
                        counterA = counter;
                        maxA = s;
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        threadA.start();

        Thread threadB = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                int counter = 0;
                try {
                    String s = queueB.take();
                    for (int j = 0; j < s.length(); j++) {
                        if (s.charAt(j) == 'b') counter++;
                    }
                    if (counter > counterB) {
                        counterB = counter;
                        maxB = s;
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        threadB.start();

        Thread threadC = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                int counter = 0;
                try {
                    String s = queueC.take();
                    for (int j = 0; j < s.length(); j++) {
                        if (s.charAt(j) == 'c') counter++;
                    }
                    if (counter > counterC) {
                        counterC = counter;
                        maxC = s;
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        threadC.start();

        generatorThread.join();
        threadA.join();
        threadB.join();
        threadC.join();

        System.out.println(maxA.substring(0, 100) + "... -> содержит " + counterA + " букв 'a'");
        System.out.println(maxB.substring(0, 100) + "... -> содержит " + counterB + " букв 'b'");
        System.out.println(maxC.substring(0, 100) + "... -> содержит " + counterC + " букв 'c'");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}