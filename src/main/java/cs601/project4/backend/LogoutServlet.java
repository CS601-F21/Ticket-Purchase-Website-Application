package cs601.project4.backend;

import cs601.project4.database.DBCPDataSource;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * LogoutServlet. Allow users to log out and redirect users to the login page.
 */
public class LogoutServlet extends HttpServlet {

    /**
     * When user click "Sign out" button, set the user's active as 0 and redirect the user to the login page.
     *
     * @param req  Http request
     * @param resp Http response
     * @throws ServletException exception that a servlet can throw when it encounters difficulty
     * @throws IOException  exceptions produced by failed or interrupted I/O operations.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String session = "";
        if (req.getCookies() == null) {
            session = "invalid";
        } else {
            for (Cookie cookie : req.getCookies()) {
                if (cookie.getName().equals("session")) {
                    session = cookie.getValue();
                }
            }
        }
        try (Connection conn = DBCPDataSource.getConnection()){
            PreparedStatement updateActive = conn.prepareStatement("UPDATE User_session SET active=0 WHERE session=?");
            updateActive.setString(1,session);
            updateActive.executeUpdate();

            resp.setStatus(302);
            resp.setHeader("location", "/login");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
