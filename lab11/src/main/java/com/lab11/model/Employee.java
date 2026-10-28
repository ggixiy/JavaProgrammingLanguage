package com.lab11.model;

public record Employee(int id, String lastName, String firstName, String position,
                       int departmentId, String departmentName) {

    @Override
    public String toString() {
        return String.format("%-3d %-12s %-9s %-20s %-2d %s",
                id, lastName, firstName, position == null ? "—" : position,
                departmentId, departmentName == null ? "" : departmentName);
    }

    public static String header() {
        return String.format("%-3s %-12s %-9s %-20s %s", "№", "Прізвище", "Ім'я", "Посада", "Відділ");
    }
}
