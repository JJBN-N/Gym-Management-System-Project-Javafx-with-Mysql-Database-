package bd.edu.seu.gms.gms.Interfaces;

import bd.edu.seu.gms.gms.Models.Package;
import java.util.List;

public interface PackageInterface {
    boolean addPackage(Package packageObj);
    boolean updatePackage(Package packageObj);
    boolean deletePackage(int packageId);
    List<Package> getAllPackages();
    Package getPackageById(int packageId);
    List<Package> getPackagesByType(String type);
    List<Package> getActivePackages();
}