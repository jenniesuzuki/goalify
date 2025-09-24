package br.com.fiap.goalify.goal;

import br.com.fiap.goalify.config.MessageHelper;
import br.com.fiap.goalify.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoalService {

    private final GoalRepository goalRepository;
    private final MessageHelper messageHelper;

    public GoalService(GoalRepository goalRepository, MessageHelper messageHelper) {
        this.goalRepository = goalRepository;
        this.messageHelper = messageHelper;
    }

    public List<Goal> getAllGoals(){
        return goalRepository.findAll();
    }

    public Goal save(Goal goal) {
        return goalRepository.save(goal);
    }

    public void deleteById(Long id) {
        goalRepository.delete(getTask(id));
    }

    public void pick(Long id, User user) {
        var task = getTask(id);
        task.setUser(user);
        goalRepository.save(task);
    }

    public void drop(Long id, User user) {
        var task = getTask(id);
        task.setUser(null);
        goalRepository.save(task);
    }

    public void incrementTaskStatus(Long id, User user) {
        var task = getTask(id);
        task.setStatus(task.getStatus() + 10);
        if(task.getStatus() > 100) task.setStatus(100);
        goalRepository.save(task);
    }

    public void decrementTaskStatus(Long id, User user) {
        var task = getTask(id);
        task.setStatus(task.getStatus() - 10);
        if(task.getStatus() < 0) task.setStatus(0);
        goalRepository.save(task);
    }

    private Goal getTask(Long id) {
        return goalRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(messageHelper.get("goal.notfound"))
        );
    }
}
