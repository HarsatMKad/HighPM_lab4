package org.example;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Main {
    public static void main(String[] args) throws IOException {
        String filePath = "data/books.json";
        String jsonContent = Files.readString(Paths.get(filePath));

        Gson gson = new Gson();
        Client[] clientList = gson.fromJson(jsonContent, Client[].class);

        long count = Stream.of(clientList).count();
        System.out.println("1)client count: " + count);

        Map<String, Long> books = Arrays.stream(clientList)
                .flatMap(c -> Arrays.stream(c.favoriteBooks))
                .collect(Collectors.groupingBy(Book::getName, Collectors.counting()));
        System.out.println("\n2)books: " + books);


        List<Book> sortBooks = Arrays.stream(clientList)
                .flatMap(c -> Arrays.stream(c.favoriteBooks))
                .sorted(Comparator.comparingInt(Book::getPublishingYear))
                .toList();

        System.out.println("\n3)sort books: ");
        for (Book book : sortBooks) {
            System.out.println(book.name + " " + book.publishingYear);
        }

        List<Client> clientsWithFilter = Arrays.stream(clientList)
                .filter(c -> Arrays.stream(c.favoriteBooks)
                        .anyMatch(b -> b.author.equalsIgnoreCase("Jane Austen")))
                .toList();
        System.out.println("\n4)clients with Jane Austen: ");
        for (Client clinet : clientsWithFilter) {
            System.out.println(clinet.name);
        }

        Optional<Client> clientWithMaxBooks = Arrays.stream(clientList)
                .max((client1, client2) -> Integer.compare(client1.favoriteBooks.length, client2.favoriteBooks.length));

        if (clientWithMaxBooks.isPresent()) {
            System.out.println("\n5) client with max books: " + clientWithMaxBooks.get().name);
        } else {
            System.out.println("\n5) client with max books not exist");
        }

        System.out.println("\n6) group clients:");
        double averageBookCount = Arrays.stream(clientList)
                .mapToInt(client -> client.favoriteBooks.length)
                .average().orElse(-1);

        int roundAvgCount = (int) Math.round(averageBookCount);

        List<Client> lessThenAvg = Arrays.stream(clientList)
                .filter(c -> c.subscribed)
                .filter(c -> c.favoriteBooks.length <= roundAvgCount).toList();

        List<Client> moreThenAvg = Arrays.stream(clientList)
                .filter(c -> c.subscribed)
                .filter(c -> c.favoriteBooks.length >= roundAvgCount).toList();

        List<Client> equalThenAvg = Arrays.stream(clientList)
                .filter(c -> c.subscribed)
                .filter(c -> c.favoriteBooks.length == roundAvgCount).toList();

        String moreSmsText = "you are a bookworm";
        String lessSmsText = "read more";
        String equalSmsText = "fine";

        for (Client clinet : lessThenAvg) {
            Sms lessSms = new Sms(clinet.phone, lessSmsText);
            System.out.println(lessSms.phoneNumber + " " + lessSms.message);
        }

        for (Client clinet : equalThenAvg) {
            Sms lessSms = new Sms(clinet.phone, equalSmsText);
            System.out.println(lessSms.phoneNumber + " " + lessSms.message);
        }

        for (Client clinet : moreThenAvg) {
            Sms lessSms = new Sms(clinet.phone, moreSmsText);
            System.out.println(lessSms.phoneNumber + " " + lessSms.message);
        }
    }
}