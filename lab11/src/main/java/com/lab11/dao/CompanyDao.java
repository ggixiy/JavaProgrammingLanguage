package com.lab11.dao;

import com.lab11.db.DatabaseConnection;
import com.lab11.model.Employee;
import com.lab11.model.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompanyDao {

    private static final String SELECT_EMPLOYEES = """
            SELECT e.employee_id, e.last_name, e.first_name, e.position,
                   e.department_id, TRIM(d.name) AS department_name
            FROM employees e
            JOIN departments d ON d.department_id = e.department_id
            """;

    private static final String SELECT_TASKS = """
            SELECT t.task_id, t.description, t.employee_id,
                   e.last_name || ' ' || e.first_name AS employee_name
            FROM tasks t
            JOIN employees e ON e.employee_id = t.employee_id
            """;

    private final DatabaseConnection db;

    public CompanyDao(DatabaseConnection db) {
        this.db = db;
    }

    public List<Employee> findAllEmployees() throws SQLException {
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_EMPLOYEES + " ORDER BY e.employee_id");
             ResultSet rs = ps.executeQuery()) {
            return readEmployees(rs);
        }
    }

    public List<Task> findAllTasks() throws SQLException {
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_TASKS + " ORDER BY t.task_id");
             ResultSet rs = ps.executeQuery()) {
            return readTasks(rs);
        }
    }

    public List<Employee> findEmployeesByDepartment(int departmentId) throws SQLException {
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     SELECT_EMPLOYEES + " WHERE e.department_id = ? ORDER BY e.employee_id")) {
            ps.setInt(1, departmentId);
            try (ResultSet rs = ps.executeQuery()) {
                return readEmployees(rs);
            }
        }
    }

    public int addTask(int employeeId, String description) throws SQLException {
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO tasks (description, employee_id) VALUES (?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, description);
            ps.setInt(2, employeeId);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt("task_id");
            }
        }
    }

    public List<Task> findTasksByEmployee(int employeeId) throws SQLException {
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     SELECT_TASKS + " WHERE t.employee_id = ? ORDER BY t.task_id")) {
            ps.setInt(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                return readTasks(rs);
            }
        }
    }

    public boolean deleteEmployee(int employeeId) throws SQLException {
        try (Connection c = db.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement delTasks = c.prepareStatement("DELETE FROM tasks WHERE employee_id = ?");
                 PreparedStatement delEmp = c.prepareStatement("DELETE FROM employees WHERE employee_id = ?")) {
                delTasks.setInt(1, employeeId);
                delTasks.executeUpdate();
                delEmp.setInt(1, employeeId);
                int deleted = delEmp.executeUpdate();
                c.commit();
                return deleted > 0;
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }

    public boolean employeeExists(int employeeId) throws SQLException {
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT 1 FROM employees WHERE employee_id = ?")) {
            ps.setInt(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static List<Employee> readEmployees(ResultSet rs) throws SQLException {
        List<Employee> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new Employee(
                    rs.getInt("employee_id"),
                    rs.getString("last_name"),
                    rs.getString("first_name"),
                    rs.getString("position"),
                    rs.getInt("department_id"),
                    rs.getString("department_name")));
        }
        return list;
    }

    private static List<Task> readTasks(ResultSet rs) throws SQLException {
        List<Task> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new Task(
                    rs.getInt("task_id"),
                    rs.getString("description"),
                    rs.getInt("employee_id"),
                    rs.getString("employee_name")));
        }
        return list;
    }
}
