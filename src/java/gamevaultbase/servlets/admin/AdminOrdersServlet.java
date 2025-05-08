package gamevaultbase.servlets.admin;

import gamevaultbase.entities.Order;
import gamevaultbase.entities.User;
import gamevaultbase.servlets.base.AdminBaseServlet;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for handling the orders interface in the admin dashboard.
 * Provides functionality to view, filter, and sort orders from the admin panel.
 */
@WebServlet(name = "AdminOrdersServlet", urlPatterns = { "/admin/orders" })
public class AdminOrdersServlet extends AdminBaseServlet {

    @Override
    protected void processAdminGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get filter parameters
        String userIdFilter = request.getParameter("userIdFilter");
        String minAmountFilter = request.getParameter("minAmountFilter");
        String maxAmountFilter = request.getParameter("maxAmountFilter");
        String startDateFilter = request.getParameter("startDateFilter");
        String endDateFilter = request.getParameter("endDateFilter");
        String sortBy = request.getParameter("sortBy");

        // Get all orders
        List<Order> orders = getOrderManagement().getAllOrders();
        double totalRevenue = 0.0;

        // Apply filters
        if (userIdFilter != null && !userIdFilter.trim().isEmpty()) {
            try {
                int userId = Integer.parseInt(userIdFilter.trim());
                orders = orders.stream()
                        .filter(order -> order.getUserId() == userId)
                        .collect(Collectors.toList());
            } catch (NumberFormatException e) {
                // Invalid user ID, ignore filter
            }
        }

        if (minAmountFilter != null && !minAmountFilter.trim().isEmpty()) {
            try {
                double minAmount = Double.parseDouble(minAmountFilter.trim());
                orders = orders.stream()
                        .filter(order -> order.getTotalAmount() >= minAmount)
                        .collect(Collectors.toList());
            } catch (NumberFormatException e) {
                // Invalid amount, ignore filter
            }
        }

        if (maxAmountFilter != null && !maxAmountFilter.trim().isEmpty()) {
            try {
                double maxAmount = Double.parseDouble(maxAmountFilter.trim());
                orders = orders.stream()
                        .filter(order -> order.getTotalAmount() <= maxAmount)
                        .collect(Collectors.toList());
            } catch (NumberFormatException e) {
                // Invalid amount, ignore filter
            }
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (startDateFilter != null && !startDateFilter.trim().isEmpty()) {
            try {
                Date startDate = dateFormat.parse(startDateFilter.trim());
                orders = orders.stream()
                        .filter(order -> order.getOrderDate() != null && order.getOrderDate().after(startDate))
                        .collect(Collectors.toList());
            } catch (ParseException e) {
                // Invalid date, ignore filter
            }
        }

        if (endDateFilter != null && !endDateFilter.trim().isEmpty()) {
            try {
                Date endDate = dateFormat.parse(endDateFilter.trim());
                orders = orders.stream()
                        .filter(order -> order.getOrderDate() != null && order.getOrderDate().before(endDate))
                        .collect(Collectors.toList());
            } catch (ParseException e) {
                // Invalid date, ignore filter
            }
        }

        // Apply sorting
        if (sortBy != null && !sortBy.isEmpty()) {
            switch (sortBy) {
                case "date_desc":
                    orders.sort((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()));
                    break;
                case "date_asc":
                    orders.sort((o1, o2) -> o1.getOrderDate().compareTo(o2.getOrderDate()));
                    break;
                case "amount_desc":
                    orders.sort((o1, o2) -> Double.compare(o2.getTotalAmount(), o1.getTotalAmount()));
                    break;
                case "amount_asc":
                    orders.sort((o1, o2) -> Double.compare(o1.getTotalAmount(), o2.getTotalAmount()));
                    break;
                case "user_id":
                    orders.sort((o1, o2) -> Integer.compare(o1.getUserId(), o2.getUserId()));
                    break;
                case "order_id":
                default:
                    orders.sort((o1, o2) -> Integer.compare(o1.getOrderId(), o2.getOrderId()));
                    break;
            }
        } else {
            // Default sort by date (newest first)
            orders.sort((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()));
        }

        // Calculate total revenue
        for (Order order : orders) {
            totalRevenue += order.getTotalAmount();
        }

        // Get users for reference
        List<User> users = getUserManagement().getAllUsers();

        // Set attributes for the JSP
        request.setAttribute("orders", orders);
        request.setAttribute("users", users);
        request.setAttribute("totalOrders", orders.size());
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("userIdFilter", userIdFilter);
        request.setAttribute("minAmountFilter", minAmountFilter);
        request.setAttribute("maxAmountFilter", maxAmountFilter);
        request.setAttribute("startDateFilter", startDateFilter);
        request.setAttribute("endDateFilter", endDateFilter);
        request.setAttribute("sortBy", sortBy);

        // Forward to the orders management JSP
        forwardToJsp(request, response, "/WEB-INF/jsp/adminOrders.jsp");
    }

    @Override
    protected void processAdminPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // No POST actions for orders currently, just redirect to GET
        redirectTo(request, response, "/admin/orders");
    }
}