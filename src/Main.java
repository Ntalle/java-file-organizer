import java.util.Scanner;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;


public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("File Organizer");
        System.out.println("Enter the path of the directory to organize:");

        String directoryPath = scanner.nextLine();

        Path directory = Path.of(directoryPath);

        boolean pathExists = Files.exists(directory);
        boolean isDirectory = Files.isDirectory(directory);

        Map<String, String> fileExtensions = new HashMap<>();

        fileExtensions.put(".txt", "Documents");
        fileExtensions.put(".pdf", "Documents");
        fileExtensions.put(".jpg", "Images");
        fileExtensions.put(".jpeg", "Images");
        fileExtensions.put(".png", "Images");
        fileExtensions.put(".gif", "Images");
        fileExtensions.put(".mp3", "Audio");
        fileExtensions.put(".wav", "Audio");
        fileExtensions.put(".mp4", "Videos");
        fileExtensions.put(".avi", "Videos");
        fileExtensions.put(".mkv", "Videos");


        if (pathExists) {
            if (!isDirectory) {
                System.out.println("The specified path is not a directory.");
                scanner.close();
                return;
            }

            System.out.println("The specified path is a valid directory.");

            try (Stream<Path> files = Files.list(directory)) {
                files.forEach(filePath -> {
                    if (Files.isRegularFile(filePath)) {
                        System.out.println("File: " + filePath.getFileName());

                        String fileName = filePath.getFileName().toString();
                        int lastDotIndex = fileName.lastIndexOf('.');
                        String fileExtension = "";
                        String category = "Other";

                        if (lastDotIndex != -1) {
                            fileExtension = fileName.substring(lastDotIndex).toLowerCase(Locale.ROOT);
                            System.out.println("File Extension: " + fileExtension);

                            String mappedCategory = fileExtensions.get(fileExtension);
                            if (mappedCategory != null) {
                                category = mappedCategory;
                            }
                        } else {
                            System.out.println("File Extension: None");
                        }

                        System.out.println("Category: " + category);

                        try {
                            Path categoryDirectory = directory.resolve(category);
                            Path targetPath = categoryDirectory.resolve(fileName);

                            Files.createDirectories(categoryDirectory);
                            Files.move(filePath, targetPath);
                            System.out.println("Moved to: " + targetPath);
                        } catch (IOException e) {
                            System.out.println("Error moving file: " + fileName);
                        }
                    } else {
                        System.out.println("Directory: " + filePath.getFileName());
                    }
                });
            } catch (IOException e) {
                System.out.println("Could not read the directory.");
            }
        } else {
            System.out.println("The specified path does not exist.");
            scanner.close();
            return;
        }

        scanner.close();
    }
}