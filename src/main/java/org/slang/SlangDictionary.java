package org.slang;

import java.io.*;
import java.util.*;

public class SlangDictionary {
    private Map<String, SlangWord> slangWords;
    private String filePath;
    public SlangDictionary(String filePath) {
        this.filePath = filePath;
        this.slangWords = new HashMap<>();
    }

    public void addSlangWord(SlangWord slangWord) {
        slangWords.put(slangWord.getWord(), slangWord);
    }

    public SlangWord searchBySlangWord(String slangWord) {
        return slangWords.get(slangWord);
    }


    public List<SlangWord> searchByDefinition(String keyword) {
        List<SlangWord> matchingWords = new ArrayList<>();
        for (SlangWord slangWord : slangWords.values()) {
            for (String meaning : slangWord.getAllMeanings()) {
                if (meaning.contains(keyword)) {
                    matchingWords.add(slangWord);
                    break;
                }
            }
        }
        return matchingWords;
    }

    public void deleteSlangWord(SlangWord slangWord) {
        slangWords.remove(slangWord.getWord());
    }

    public void resetSlangWords() {
        slangWords.clear();
    }

    public String getMeaning(String word) {
        SlangWord slangWord = slangWords.get(word);
        if (slangWord != null) {
            // Get the first meaning
            return slangWord.getMeaning(1);
        }
        return null;
    }

    public void updateSlangWord(SlangWord slangWord, String newWord, List<String> newMeanings) {
        slangWords.remove(slangWord.getWord()); // Remove the old slang word

        // Create a new SlangWord with the updated values
        SlangWord updatedSlangWord = new SlangWord(newWord);
        for (int i = 0; i < newMeanings.size(); i++) {
            updatedSlangWord.addMeaning(i + 1, newMeanings.get(i));
        }

        // Add the updated SlangWord to the dictionary
        slangWords.put(updatedSlangWord.getWord(), updatedSlangWord);
    }


    public SlangWord getRandomSlangWord() {
        if (slangWords.isEmpty()) {
            return null;
        }

        List<SlangWord> values = new ArrayList<>(slangWords.values());
        Random random = new Random();
        int index = random.nextInt(values.size());
        return values.get(index);
    }

    public List<SlangWord> getRandomSlangWords(int count) {
        if (slangWords.isEmpty() || count <= 0) {
            return new ArrayList<>();
        }

        List<SlangWord> values = new ArrayList<>(slangWords.values());
        Random random = new Random();
        List<SlangWord> randomSlangWords = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            int index = random.nextInt(values.size());
            randomSlangWords.add(values.get(index));
        }

        return randomSlangWords;
    }

    public void loadDataFromFile() {
        loadDataFromFile(filePath);
    }

    public void loadDataFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("`");
                if (parts.length >= 2) {
                    String word = parts[0];
                    String[] meanings = parts[1].split("\\| ");
                    SlangWord slangWord = new SlangWord(word);
                    for (int i = 0; i < meanings.length; i++) {
                        slangWord.addMeaning(i + 1, meanings[i]);
                    }
                    addSlangWord(slangWord);
                }
            }
            System.out.println(slangWords.size());
            System.out.println("Slang words loaded successfully!");
        } catch (IOException e) {
            System.out.println("Error loading data from file: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (SlangWord slangWord : slangWords.values()) {
                writer.write(slangWord.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving slang dictionary to file: " + e.getMessage());
        }
    }

    public void resetToDefault(String defaultFilePath) {
        slangWords.clear();
        loadDataFromFile(defaultFilePath);
        saveToFile();
    }

}
