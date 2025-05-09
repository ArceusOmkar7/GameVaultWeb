package gamevaultbase.servlets.user;

import gamevaultbase.entities.Order;
import gamevaultbase.entities.User;
import gamevaultbase.servlets.base.UserBaseServlet;
import java.io.IOException;
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
        List<Order> userOrders = null;
        String errorMessage = null;

        try {
            // Use the more efficient findByUserId method
            userOrders = getOrderManagement().getOrderStorage().findByUserId(currentUser.getUserId());
        } catch (Exception e) {
            System.err.println("Error retrieving orders for user " + currentUser.getUserId() + ": " + e.getMessage());
            e.printStackTrace();
            errorMessage = "An error occurred while retrieving your order history.";
            userOrders = Collections.emptyList(); // Empty list instead of null
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
        // Calculate total spent
        double totalSpent = 0.0;
        if (userOrders != null) {
            for (Order order : userOrders) {
                totalSpent += order.getTotalAmount();
            }
        }
        request.setAttribute("totalSpent", totalSpent);
        // Calculate games owned
        int totalGames = 0;
        if (currentUser != null) {
            try {
                gamevaultbase.management.GameManagement gameManagement = (gamevaultbase.management.GameManagement) getServletContext()
                        .getAttribute("gameManagement");
                totalGames = gameManagement.getOwnedGames(currentUser.getUserId()).size();
            } catch (Exception e) {
                totalGames = 0;
            }
        }
        request.setAttribute("totalGames", totalGames);
        forwardToJsp(request, response, "/WEB-INF/jsp/orders.jsp");
    }

    @Override
    protected void processUserPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Orders view should be GET only
        redirectTo(request, response, "/orders");
    }
}
