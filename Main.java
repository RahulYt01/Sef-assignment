import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        Bus bus = new Bus("BUS011", "IN_SERVICE");
        Location location = new Location();
        TrafficData trafficData = new TrafficData("TD-001");
        BusDriver driver = new BusDriver("USR-001", "Rahul", "Rahul");
        ControlCentreOperator operator = new ControlCentreOperator("011", trafficData);
        ObstructionUpdate report = new ObstructionUpdate("AL324", "Obstruction detected");
        Route route = new Route("324-086", "A46");

        // reportStatus()
        String status = bus.reportStatus();
        System.out.println(status);

        // alt fragment
        String inputMethod = "touchscreen";

        if (inputMethod.equals("touchscreen")) {
            report.selectObstructionType("ROADWORK");
        } else {
            report.submitVoiceInput(new byte[]{});
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

    public Location updateCoordinates(double lat, double lng) {
    latitude = lat;
    longitude = lng;
    return this;
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

    public String updateTrafficData() {
        System.out.println("Traffic data updated");
        return "updated";
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

        while (obstructionActive) {

            String updated = trafficData.updateTrafficData();
            System.out.println(updated);

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
