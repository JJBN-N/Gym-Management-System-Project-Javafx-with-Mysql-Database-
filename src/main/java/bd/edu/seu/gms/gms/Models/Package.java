package bd.edu.seu.gms.gms.Models;

public class Package {
    private int id;
    private String name;
    private String type;
    private int durationDays;
    private double price;
    private String description;
    private String features;
    private String status;

    public Package() {}

    public Package(int id, String name, String type, int durationDays, double price,
                   String description, String features, String status) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.durationDays = durationDays;
        this.price = price;
        this.description = description;
        this.features = features;
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getDurationDays() { return durationDays; }
    public void setDurationDays(int durationDays) { this.durationDays = durationDays; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFeatures() { return features; }
    public void setFeatures(String features) { this.features = features; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return name + " - $" + price;
    }
}