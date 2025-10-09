package bd.edu.seu.gms.gms.Interfaces;

import bd.edu.seu.gms.gms.Models.Member;
import java.util.List;

public interface MemberInterface {
    boolean addMember(Member member);
    boolean updateMember(Member member);
    boolean deleteMember(int memberId);
    List<Member> getAllMembers();
    Member getMemberById(int memberId);
    Member getMemberByMemberId(String memberId);
    List<Member> searchMembers(String keyword);
    int getTotalMembers();
    int getActiveMembers();
}