package gamevaultbase.services.admin;

import java.util.Map;

/**
 * Interface for dashboard services that generate data for admin dashboard
 * components.
 * Each implementation should handle a specific chart or statistic.
 */
public interface DashboardService {

    /**
     * Generate data for a dashboard component.
     * 
     * @return A map containing the data needed for the component.
     */
    Map<String, Object> generateData();
}