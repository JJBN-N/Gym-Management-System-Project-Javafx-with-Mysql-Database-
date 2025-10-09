package bd.edu.seu.gms.gms.Interfaces;

import bd.edu.seu.gms.gms.Models.Membership;
import java.util.List;

public interface MembershipInterface {
    boolean createMembership(Membership membership);
    boolean updateMembership(Membership membership);
    boolean deleteMembership(int membershipId);
    boolean renewMembership(int membershipId, int packageId);
    List<Membership> getAllMemberships();
    Membership getMembershipById(int membershipId);
    List<Membership> getMembershipsByMemberId(int memberId);
    List<Membership> getActiveMemberships();
    List<Membership> getExpiredMemberships();
    List<Membership> getMembershipsDueForRenewal(int daysBeforeExpiry);
    double getTotalRevenue();
    double getPendingDues();
}