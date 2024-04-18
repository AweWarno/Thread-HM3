package dev.fedichkin;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Main {

    public static AtomicInteger countStringLen3 = new AtomicInteger(0);
    public static AtomicInteger countStringLen4 = new AtomicInteger(0);
    public static AtomicInteger countStringLen5 = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Runnable isPalindrome = () -> {
            Stream.of(texts).forEach(item -> {
                if (item.equals(new StringBuilder(item).reverse().toString())) {
                    incrementCount(item);
                }
            });
        };


        Runnable isOnlyOneTypeLetter = () -> {
            Stream.of(texts).forEach(item -> {
                if (item.replaceAll(String.valueOf(item.charAt(0)), "").isEmpty()) {
                    incrementCount(item);
                }
            });
        };

        Runnable isLettersAscendingOrder = () -> {
            Stream.of(texts).forEach(item -> {
                boolean result = true;
                for (int i = 0; i < item.length() - 1; i++) {
                    if (result && (item.charAt(i) == item.charAt(i + 1) || item.charAt(i) == item.charAt(i + 1) + 1)) {}
                    else {
                        result = false;
                    }
                }

                if (result) {
                    incrementCount(item);
                }
            });
        };

        Thread thread1 = new Thread(isPalindrome);
        Thread thread2 = new Thread(isLettersAscendingOrder);
        Thread thread3 = new Thread(isOnlyOneTypeLetter);

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        System.out.printf("Красивых слов с длиной 3: %d шт \n", countStringLen3.get());
        System.out.printf("Красивых слов с длиной 4: %d шт \n", countStringLen4.get());
        System.out.printf("Красивых слов с длиной 5: %d шт \n", countStringLen5.get());
    }

    private static synchronized void incrementCount(String item) {
        switch (item.length()) {
            case 3:
                countStringLen3.getAndIncrement();
                break;
            case 4:
                countStringLen4.getAndIncrement();
                break;
            case 5:
                countStringLen5.getAndIncrement();
                break;
            default:
                break;
        }
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