package gamevaultbase.servlets;

import gamevaultbase.entities.Order;
import gamevaultbase.entities.User;
import gamevaultbase.servlets.base.UserBaseServlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "OrdersServlet", urlPatterns = { "/orders" })
public class OrdersServlet extends UserBaseServlet {

    @Override
    protected void processUserGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User currentUser = getLoggedInUser(request);
        List<Order> userOrders = Collections.emptyList();
        String errorMessage = null;

        try {
            // Fetch ALL orders and then filter (less efficient, but simpler without
            // dedicated storage method)
            // Ideally, OrderStorage would have a findByUserId method.
            List<Order> allOrders = getOrderManagement().getAllOrders();
            if (allOrders != null) {
                userOrders = new ArrayList<>();
                for (Order order : allOrders) {
                    if (order.getUserId() == currentUser.getUserId()) {
                        userOrders.add(order);
                    }
                }
                // Optional: Sort orders by date descending
                userOrders.sort((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()));
            }
        } catch (Exception e) {
            System.err.println("Error retrieving orders for user " + currentUser.getUserId() + ": " + e.getMessage());
            e.printStackTrace();
            errorMessage = "An error occurred while retrieving your order history.";
        }

        // Pass messages if any
        String message = request.getParameter("message");
        String messageType = request.getParameter("messageType");
        if (message != null) {
            request.setAttribute("message", message);
            request.setAttribute("messageType", messageType);
        }

        request.setAttribute("orderList", userOrders);
        request.setAttribute("errorMessage", errorMessage);
        forwardToJsp(request, response, "/WEB-INF/jsp/orders.jsp");
    }
}