package cs601.project4;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CreateEventServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateEventServlet.class);
    final static String CONNECTION_STRING = "jdbc:mysql://localhost:3306/project4?user=root&password=2281997163";
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String eventName = req.getParameter("eventName");
        String address1 = req.getParameter("add1");
        String address2 = req.getParameter("add2");
        String city = req.getParameter("city");
        String state = req.getParameter("state");
        String zipcode = req.getParameter("zipcode");
        int capacity = Integer.parseInt(req.getParameter("capacity"));
        double price = Double.parseDouble(req.getParameter("price"));
        String description = req.getParameter("description");


        String session = "";
        for(Cookie cookie: req.getCookies()) {
            if(cookie.getName().equals("session")) {
                session = cookie.getValue();
            }
        }
        try(Connection conn = DriverManager.getConnection(CONNECTION_STRING)) {
            PreparedStatement userQuery = conn.prepareStatement("SELECT user_id FROM User_session WHERE session=? ");
            userQuery.setString(1,session);
            ResultSet idResult = userQuery.executeQuery();
            idResult.next();
            int userId = idResult.getInt("user_id");

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            df.setTimeZone(TimeZone.getTimeZone(req.getParameter("timezone")));
            final Date startTime;
            try {
                startTime = df.parse(req.getParameter("starttime"));
            } catch (ParseException e) {
                e.printStackTrace();
                resp.sendError(400, "start_time invalid");
                return;
            }
            final Date endTime;
            try {
                endTime = df.parse(req.getParameter("endtime"));
            } catch (ParseException e) {
                e.printStackTrace();
                resp.sendError(400, "end_time invalid");
                return;
            }


            PreparedStatement insertQuery = conn.prepareStatement("INSERT into Events(eventname, createbyuserid, createdate, " +
                    " address1, address2, city, state, zipcode, capacity, price, description, start_time, end_time) VALUES " +
                    " (?,?,current_timestamp(),?,?,?,?,?,?,?,?,?,?) ");
            insertQuery.setString(1, eventName);
            insertQuery.setInt(2, userId);
            insertQuery.setString(3, address1);
            insertQuery.setString(4, address2);
            insertQuery.setString(5, city);
            insertQuery.setString(6, state);
            insertQuery.setString(7, zipcode);
            insertQuery.setInt(8, capacity);
            insertQuery.setDouble(9, price);
            insertQuery.setString(10, description);
            insertQuery.setTimestamp(11, new Timestamp(startTime.getTime()));
            insertQuery.setTimestamp(12, new Timestamp(endTime.getTime()));
            insertQuery.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        resp.setStatus(302);
        resp.setHeader("location", "/myEvents");

//        LOGGER.info(req.getParameterMap().toString());
//        JsonObject json = new JsonObject();
//        json.addProperty("success", "success");
//        resp.getWriter().write(json.toString());
    }
}