package gamevaultbase.servlets;

import gamevaultbase.entities.Order;
import gamevaultbase.entities.User;
import gamevaultbase.management.OrderManagement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Mapped in web.xml as /orders
public class OrdersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("loggedInUser") : null;

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login?message=Please login to view your orders.&messageType=error");
            return;
        }

        OrderManagement orderManagement = (OrderManagement) getServletContext().getAttribute("orderManagement");
        List<Order> userOrders = Collections.emptyList();
        String errorMessage = null;

        if (orderManagement == null) {
            System.err.println("FATAL: OrderManagement not found in ServletContext!");
            errorMessage = "Application error: Order service unavailable.";
        } else {
            try {
                // Fetch ALL orders and then filter (less efficient, but simpler without dedicated storage method)
                // Ideally, OrderStorage would have a findByUserId method.
                List<Order> allOrders = orderManagement.getAllOrders();
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
        request.getRequestDispatcher("/WEB-INF/jsp/orders.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Viewing orders is typically GET
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Displays the order history for the logged-in user.";
    }
}