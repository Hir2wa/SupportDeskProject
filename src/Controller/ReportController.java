package Controller;

import model.Report;
import util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportController {
    private Connection connection;

    public ReportController() {
        try {
            this.connection = DbConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean createReport(Report report) {
        String sql = "INSERT INTO reports (reported_by, comment_id, issue_id, reason) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, report.getReportedBy());

            if (report.getCommentId() != null) {
                pstmt.setInt(2, report.getCommentId());
            } else {
                pstmt.setNull(2, java.sql.Types.INTEGER);
            }

            if (report.getIssueId() != null) {
                pstmt.setInt(3, report.getIssueId());
            } else {
                pstmt.setNull(3, java.sql.Types.INTEGER);
            }

            pstmt.setString(4, report.getReason());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        report.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean reportIssue(int reportedBy, int issueId, String reason) {
        Report report = new Report(reportedBy, null, issueId, reason);
        return createReport(report);
    }

    public boolean reportComment(int reportedBy, int commentId, String reason) {
        Report report = new Report(reportedBy, commentId, null, reason);
        return createReport(report);
    }

    public List<Report> getAllReports() {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT * FROM reports ORDER BY created_at DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Report report = new Report();
                report.setId(rs.getInt("id"));
                report.setReportedBy(rs.getInt("reported_by"));

                int commentId = rs.getInt("comment_id");
                if (!rs.wasNull()) {
                    report.setCommentId(commentId);
                }

                int issueId = rs.getInt("issue_id");
                if (!rs.wasNull()) {
                    report.setIssueId(issueId);
                }

                report.setReason(rs.getString("reason"));
                report.setCreatedAt(rs.getTimestamp("created_at"));

                reports.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reports;
    }

    public List<Report> fetchReportsBy(Integer issueId, Integer commentId, Integer reportedBy) {
        List<Report> reports = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM reports WHERE 1=1");

        if (issueId != null) {
            sqlBuilder.append(" AND issue_id = ?");
        }
        if (commentId != null) {
            sqlBuilder.append(" AND comment_id = ?");
        }
        if (reportedBy != null) {
            sqlBuilder.append(" AND reported_by = ?");
        }

        sqlBuilder.append(" ORDER BY created_at DESC");

        try (PreparedStatement pstmt = connection.prepareStatement(sqlBuilder.toString())) {
            int index = 1;
            if (issueId != null) {
                pstmt.setInt(index++, issueId);
            }
            if (commentId != null) {
                pstmt.setInt(index++, commentId);
            }
            if (reportedBy != null) {
                pstmt.setInt(index++, reportedBy);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Report report = new Report();
                    report.setId(rs.getInt("id"));
                    report.setReportedBy(rs.getInt("reported_by"));

                    int commentIdResult = rs.getInt("comment_id");
                    if (!rs.wasNull()) {
                        report.setCommentId(commentIdResult);
                    }

                    int issueIdResult = rs.getInt("issue_id");
                    if (!rs.wasNull()) {
                        report.setIssueId(issueIdResult);
                    }

                    report.setReason(rs.getString("reason"));
                    report.setCreatedAt(rs.getTimestamp("created_at"));

                    reports.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reports;
    }

    public boolean deleteReport(int reportId) {
        String sql = "DELETE FROM reports WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reportId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hasUserReportedIssue(int userId, int issueId) {
        String sql = "SELECT COUNT(*) AS count FROM reports WHERE reported_by = ? AND issue_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, issueId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean hasUserReportedComment(int userId, int commentId) {
        String sql = "SELECT COUNT(*) AS count FROM reports WHERE reported_by = ? AND comment_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, commentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean reportComment(int userId, Integer commentId, String reason) {
        // Logic for reporting a comment, using commentId
        if (commentId == null) {
            throw new IllegalArgumentException("commentId cannot be null");
        }
        // Your logic to report the comment
        Report report = new Report(userId, commentId, null, reason);
        return createReport(report); // Now we call createReport to handle the insertion
    }
}
