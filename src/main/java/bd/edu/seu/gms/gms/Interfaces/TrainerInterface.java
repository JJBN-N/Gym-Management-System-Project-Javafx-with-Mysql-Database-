package bd.edu.seu.gms.gms.Interfaces;

import bd.edu.seu.gms.gms.Models.Trainer;
import java.util.List;

public interface TrainerInterface {
    boolean addTrainer(Trainer trainer);
    boolean updateTrainer(Trainer trainer);
    boolean deleteTrainer(int trainerId);
    Trainer getTrainerById(int trainerId);
    Trainer getTrainerByUserId(int userId);
    List<Trainer> getAllTrainers();
    List<Trainer> getActiveTrainers();
    boolean updateTrainerStatus(int trainerId, String status);
}