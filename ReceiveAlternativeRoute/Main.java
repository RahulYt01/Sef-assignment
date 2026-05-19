package ReceiveAlternativeRoute;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        Bus bus = new Bus("BUS-042", "IN_SERVICE");
        GPS gps = new GPS();
        TrafficData trafficData = new TrafficData("TD-001");
        BusDriver driver = new BusDriver("USR-001", "David", "LIC-9988");
        Route route = new Route("RT-086", "Route 86");
        NavigationSystem navSys = new NavigationSystem();
        RouteController routeController = new RouteController();
        AlertService alertService = new AlertService();

        // Receiving alternative route depending on traffic or deviation detected

        System.out.println("David Testing\n");

        // Bus monitors route while is it in service
        navSys.monitorRoute(bus, driver, routeController, gps, trafficData, route, alertService);
    }

}

class AlertService {

    public void createRerouteAlert(String alertID, String message,
            double deviationDist, String newRoute, BusDriver driver) {
        DeviationAlert alert = new DeviationAlert(alertID, message, deviationDist);
        System.out.println(alert.generateMessage());
        sendNotification(driver, newRoute);
    }

    public void createTrafficAlert(String alertID, String message,
            int delay, String newRoute, BusDriver driver) {
        TrafficAlert alert = new TrafficAlert(alertID, message, delay);
        System.out.println(alert.generateMessage());
        sendNotification(driver, newRoute);
    }

    public void sendNotification(BusDriver driver, String newRoute) {
        System.out.println("[AlertService] Notifying " + driver.getName());
        System.out.println("[AlertService] " + newRoute);
    }
}

abstract class Alert {

    private String alertID;
    private LocalDateTime createdTime;
    private String message;
    private boolean resolved;

    public Alert(String alertID, String message) {
        this.alertID = alertID;
        this.message = message;
        this.createdTime = LocalDateTime.now();
        this.resolved = false;
    }

    public String generateMessage() {
        return message;
    }

    public void sendToDriver(BusDriver driver) {
        System.out.println("Alert sent to " + driver.getName());
    }

    public void markResolved() {
        resolved = true;
        System.out.println("Alert resolved");
    }
}

class ObstructionUpdate extends Alert {

    private String obstructionType;

    public ObstructionUpdate(String alertID, String message) {
        super(alertID, message);
    }

    public void selectObstructionType(String type) {
        obstructionType = type;
        System.out.println("Type selected: " + type);
    }

    public void submitVoiceInput(byte[] audio) {
        obstructionType = "VOICE_INPUT";
        System.out.println("Voice input submitted");
    }

    public boolean confirmAffectedRoute(Route route) {
        System.out.println("Route confirmed: " + route.getRouteID());
        return true;
    }
}

class DeviationAlert extends Alert {
    private double deviationDist;

    public DeviationAlert(String alertID, String message, double deviationDist) {
        super(alertID, message);
        this.deviationDist = deviationDist;
    }

    @Override
    public String generateMessage() {
        return super.generateMessage() + " | " + deviationDist + "m";
    }
}

class TrafficAlert extends Alert {
    private int estimatedDelay;

    public TrafficAlert(String alertID, String message, int estimatedDelay) {
        super(alertID, message);
        this.estimatedDelay = estimatedDelay;
    }

    @Override
    public String generateMessage() {
        return super.generateMessage() + " | Estimated delay of " + estimatedDelay;
    }
}

class BusDriver {

    private String userID;
    private String name;
    private String licenceNumber;

    public BusDriver(String userID, String name, String licenceNumber) {
        this.userID = userID;
        this.name = name;
        this.licenceNumber = licenceNumber;
    }

    public Route viewAssignedRoute() {
        return new Route("RT-001", "Route 1");
    }

    public String getName() {
        return name;
    }

    public void displayNewRoute() {
        System.out.println("Displaying new route");
    }
}

class Bus {

    private String busID;
    private String operationalStatus;

    public Bus(String busID, String operationalStatus) {
        this.busID = busID;
        this.operationalStatus = operationalStatus;
    }

    public String reportStatus() {
        return operationalStatus;
    }

    public void updateStatus(String operationalStatus) {
        this.operationalStatus = operationalStatus;
    }

    public void updateLocation(GPS location) {
        System.out.println("Location updated");
    }

    public void assignRoute(Route route) {
        System.out.println("Route assigned");
    }

    public String getBusID() {
        return busID;
    }
}

class Coordinate {
    private double latitude;
    private double longitude;

    public Coordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}

class GPS {
    private Coordinate coordinate;
    private String signalStatus;

    public Coordinate getLocation() {
        return coordinate;
    }

    public void updateLocation(double lat, double lng) {
        System.out.println("Coordinates updated");
    }

    public String checkStatus() {
        return signalStatus;
    }

}

class TrafficData {

    private String dataID;
    private String congestionLevel;

    public TrafficData(String dataID) {
        this.dataID = dataID;
        this.congestionLevel = "HIGH";
    }

    public boolean detectTrafficDisruption() {
        return true;
    }

    public int estimateDelay() {
        return 15;
    }

    public void updateTrafficData() {
        System.out.println("Traffic data updated");
    }
}

class Route {

    private String routeID;
    private String routeName;

    public Route(String routeID, String routeName) {
        this.routeID = routeID;
        this.routeName = routeName;
    }

    public void addBusStop(String stopName) {
        System.out.println("Bus stop added");
    }

    public double calculateProgressLocation() {
        return 0.0;
    }

    public String getRouteID() {
        return routeID;
    }
}

class NavigationSystem {
    Boolean routeCalculated = false;
    double distance;

    public String calcNewRoute(Coordinate location, Route route) {
        routeCalculated = true;
        return "Generating new route";
    }

    public double calcDistFromRoute(Coordinate location) {
        distance = 150;
        return distance;
    }

    public void monitorRoute(Bus bus, BusDriver driver, RouteController routeController, GPS gps,
            TrafficData trafficData, Route route, AlertService alertService) {
        while (bus.reportStatus().equals("IN_SERVICE")) {

            // Monitor every 1 second, but if actually implemented would be 5 seconds
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            Coordinate currLocation = gps.getLocation();

            // Check for deviation then create alert if true and display new route
            if (routeController.checkDeviation(this, gps)) {
                String detourRoute = calcNewRoute(currLocation, route);
                driver.displayNewRoute();
                alertService.createRerouteAlert("ALERT-502", "Deviation detected", calcDistFromRoute(currLocation),
                        detourRoute, driver);
            }

            System.out.println("\n");

            // Check for traffic affecting route
            if (routeController.checkTraffic(trafficData)) {
                String detourRoute = calcNewRoute(currLocation, route);
                driver.displayNewRoute();
                alertService.createTrafficAlert("ALERT-503", "Traffic detected", trafficData.estimateDelay(),
                        detourRoute, driver);

            }

            System.out.println("\n");

            bus.updateStatus("NOT_IN_SERVICE");
            System.out.println("BUS NO LONGER OPERATIONAL");

        }
    }
}

class RouteController {
    private static final double DEVIATION_THRESHOLD = 100.0; // meters

    public Boolean checkTraffic(TrafficData trafficData) {
        return trafficData.detectTrafficDisruption();
    }

    public Boolean checkDeviation(NavigationSystem navSys, GPS gps) {
        return navSys.calcDistFromRoute(gps.getLocation()) > DEVIATION_THRESHOLD;

    }
}
