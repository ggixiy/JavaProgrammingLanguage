package com.lab11.model;

public record Task(int id, String description, int employeeId, String employeeName) {

    @Override
    public String toString() {
        return String.format("%-3d %-36s %-3d %s",
                id, description, employeeId, employeeName == null ? "" : employeeName);
    }

    public static String header() {
        return String.format("%-3s %-36s %s", "№", "Опис завдання", "Співробітник");
    }
}
