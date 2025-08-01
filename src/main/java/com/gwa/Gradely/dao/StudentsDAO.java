package com.gwa.Gradely.dao;

import com.gwa.Gradely.beans.StudentBean;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author RalphTheGreat
 */
@Repository
public class StudentsDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Finds a student by their student ID. Used mainly during login to retrieve
     * the full student record.
     *
     * @param studentId - the student ID to look up
     * @return StudentBean if found, otherwise null
     */
    public StudentBean findByStudentId(String studentId) {
        String sql = "SELECT id, first_name, middle_name, last_name, student_id, email, password, created_at, updated_at "
                + "FROM students WHERE student_id = ?";

        try {
            // Executes the SQL query with the given parameter and maps the result to a StudentBean object
            return jdbcTemplate.queryForObject(sql, new Object[]{studentId}, new StudentRowMapper());
        } catch (EmptyResultDataAccessException e) {
            // If no result is found, return null (no student with that ID)
            return null;
        }
    }

    /**
     * Inner static class that maps SQL result set rows to StudentBean objects.
     * Implements Spring's RowMapper interface.
     */
    private static class StudentRowMapper implements RowMapper<StudentBean> {

        /**
         * This method is automatically called by JdbcTemplate for each row
         * returned by the query.
         *
         * @param rs - the ResultSet containing the row data
         * @param rowNum - the current row number
         * @return a fully populated StudentBean object
         */
        @Override
        public StudentBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            StudentBean student = new StudentBean();

            // Set each field in the StudentBean using values from the ResultSet
            student.setId(rs.getLong("id"));
            student.setFirstName(rs.getString("first_name"));
            student.setMiddleName(rs.getString("middle_name"));
            student.setLastName(rs.getString("last_name"));
            student.setStudentId(rs.getString("student_id"));
            student.setEmail(rs.getString("email"));
            student.setPassword(rs.getString("password")); // hashed password (BCrypt)
            student.setCreatedAt(rs.getTimestamp("created_at"));
            student.setUpdatedAt(rs.getTimestamp("updated_at"));

            return student;
        }
    }
}
