package com.epam.izh.rd.online.repository;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class SimpleFileRepository implements FileRepository {

    /**
     * Метод рекурсивно подсчитывает количество файлов в директории
     *
     * @param path путь до директори
     * @return файлов, в том числе скрытых
     */
    @Override
    public long countFilesInDirectory(String path) {
        //       path = "C:\\Users\\akobr\\IdeaProjects\\java-data-handling-template\\src\\main\\resources\\"+path;
        File cur = new File("");
        String pathFull = cur.getAbsolutePath();
        pathFull = pathFull + "\\src\\main\\resources\\" + path;
        File dir = new File(pathFull);
        long count = 0L;
        if (dir.isDirectory()) {
            StringBuilder pathBuilder = new StringBuilder(path);
            for (File item : Objects.requireNonNull(dir.listFiles())) {
                if (item.isFile()) {
                    count++;
                }
                if (item.isDirectory()) {
                    String path1 = pathBuilder.toString();
                    pathBuilder.append("\\").append(item.getName());
                    count = count + countFilesInDirectory(pathBuilder.toString());
                    pathBuilder = new StringBuilder(path1);
                }
            }
        }
        return count;
    }

    /**
     * Метод рекурсивно подсчитывает количество папок в директории, считая корень
     *
     * @param path путь до директории
     * @return число папок
     */
    @Override
    public long countDirsInDirectory(String path) {
        File cur = new File("");
        String pathFull = cur.getAbsolutePath();
        pathFull = pathFull + "\\src\\main\\resources\\" + path;
        File dir = new File(pathFull);
        long count = 0L;
        if (dir.isDirectory()) {
            count++;
            StringBuilder pathBuilder = new StringBuilder(path);
            for (File item : Objects.requireNonNull(dir.listFiles())) {

                if (item.isDirectory()) {
                    String path1 = pathBuilder.toString();
                    pathBuilder.append("\\").append(item.getName());
                    count = count + countDirsInDirectory(pathBuilder.toString());
                    pathBuilder = new StringBuilder(path1);
                }
            }
        }
        return count;
    }

    /**
     * Метод копирует все файлы с расширением .txt
     *
     * @param from путь откуда
     * @param to   путь куда
     */
    @Override
    public void copyTXTFiles(String from, String to) {
        File cur = new File("");
        String pathFull = cur.getAbsolutePath();
        File dir = new File(pathFull + "\\" + to.substring(0, to.lastIndexOf("/") + 1));
        File start = new File(pathFull + "\\" + from);
        File dest = new File(pathFull + "\\" + to);
        try {
            Files.createDirectory(dir.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (start.isDirectory()) {
            for (File item : Objects.requireNonNull(start.listFiles())) {
                if (item.isFile() && item.getName().endsWith("txt")) {
                    try {
                        Files.copy(item.toPath(), dest.toPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (start.isFile() && start.getName().endsWith("txt")) {
            try {
                Files.copy(start.toPath(), dest.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Метод создает файл на диске с расширением txt
     *
     * @param path путь до нового файла
     * @param name имя файла
     * @return был ли создан файл
     */
    @Override
    public boolean createFile(String path, String name) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(path);

        assert resource != null;
        File dir = new File(resource.getPath() + "\\");
        File file = new File(resource.getPath() + "\\" + name);
        try {
            if (Files.notExists(dir.toPath())) {
                Files.createDirectory(dir.toPath());
            }
            if (Files.notExists(file.toPath())) {
                Files.createFile(file.toPath());
            }
        } catch (IOException e) {
            e.printStackTrace();

        }

        return true;
    }

    /**
     * Метод считывает тело файла .txt из папки src/main/resources
     *
     * @param fileName имя файла
     * @return контент
     */
    @Override
    public String readFileFromResources(String fileName) {
        File cur = new File("");
        String pathFull = cur.getAbsolutePath();
        pathFull = pathFull + "\\src\\main\\resources\\" + fileName;
        File file = new File(pathFull);
        String data = null;
        try {
            data = Files.readString(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
