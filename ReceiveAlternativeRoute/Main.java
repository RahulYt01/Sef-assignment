package ReceiveAlternativeRoute;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        Bus bus = new Bus("BUS-042", "IN_SERVICE");
        Location location = new Location();
        TrafficData trafficData = new TrafficData("TD-001");
        BusDriver driver = new BusDriver("USR-001", "Rahul", "LIC-9988");
        ControlCentreOperator operator = new ControlCentreOperator("OPR-01", trafficData);
        ObstructionUpdate report = new ObstructionUpdate("ALERT-501", "Obstruction detected");
        Route route = new Route("RT-086", "Route 86");
        NavigationSystem navSys = new NavigationSystem();
        RouteController routeController = new RouteController();

        // reportStatus()
        String status = bus.reportStatus();
        System.out.println(status);

        // alt fragment
        String inputMethod = "touchscreen";

        if (inputMethod.equals("touchscreen")) {
            report.selectObstructionType("ROADWORK");
        } else {
            report.submitVoiceInput(new byte[] {});
        }

        // generateMessage()
        String message = report.generateMessage();
        System.out.println(message);

        // updateCoordinates()
        location.updateCoordinates(-37.8136, 144.9631);

        // detectTrafficDisruption()
        boolean disrupted = trafficData.detectTrafficDisruption();

        // alt fragment
        if (disrupted) {
            int delay = trafficData.estimateDelay();
            System.out.println("Delay: " + delay);
        }

        // confirmAffectedRoute()
        boolean confirmed = report.confirmAffectedRoute(route);
        System.out.println(confirmed);

        // sendToDriver()
        report.sendToDriver(driver);

        // monitorBus()
        operator.monitorBus(bus);

        // markResolved()
        report.markResolved();

        // Receiving alternative route depending on traffic or deviation detected

        System.out.println("\nDavid Testing");
        while (bus.reportStatus().equals("IN_SERVICE")) {

            if (routeController.checkDeviation(navSys, location)) {
                DeviationAlert devAlert = new DeviationAlert("ALERT-502", "Deviation detected",
                        navSys.calcDistFromRoute(location));
                String devMessage = devAlert.generateMessage();
                System.out.println(devMessage);
                System.out.println(navSys.calcNewRoute(location, route) + "\n");

            }
            if (routeController.checkTraffic(trafficData)) {
                TrafficAlert trafficAlert = new TrafficAlert("ALERT-503", "Traffic detected",
                        trafficData.estimateDelay());
                String trafficMessage = trafficAlert.generateMessage();
                System.out.println(trafficMessage);
                System.out.println(navSys.calcNewRoute(location, route) + "\n");
            }

            // Have to change status to non operational otherwise inf loop
            bus.updateStatus("NOT_IN_SERVICE");
            System.out.println("BUS NO LONGER OPERATIONAL");
        }

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

    public void updateLocation(Location location) {
        System.out.println("Location updated");
    }

    public void assignRoute(Route route) {
        System.out.println("Route assigned");
    }

    public String getBusID() {
        return busID;
    }
}

class Location {

    private double latitude;
    private double longitude;
    private String signalStatus;

    public void updateCoordinates(double lat, double lng) {
        latitude = lat;
        longitude = lng;
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

class ControlCentreOperator {

    private String operatorID;
    private TrafficData trafficData;

    public ControlCentreOperator(String operatorID, TrafficData trafficData) {
        this.operatorID = operatorID;
        this.trafficData = trafficData;
    }

    public void monitorBus(Bus bus) {

        boolean obstructionActive = true;

        // loop fragment
        while (obstructionActive) {

            trafficData.updateTrafficData();

            obstructionActive = false;
        }

        System.out.println("Monitoring complete");
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

    public String calcNewRoute(Location location, Route route) {
        routeCalculated = true;
        return "Generating new route";
    }

    public double calcDistFromRoute(Location location) {
        distance = 150;
        return distance;
    }

}

class RouteController {
    private static final double DEVIATION_THRESHOLD = 100.0; // meters

    public Boolean checkTraffic(TrafficData trafficData) {
        if (trafficData.detectTrafficDisruption()) {
            return true;
        }
        return false;
    }

    public Boolean checkDeviation(NavigationSystem navSys, Location location) {
        if (navSys.calcDistFromRoute(location) > DEVIATION_THRESHOLD) {
            return true;
        }
        return false;
    }
}
