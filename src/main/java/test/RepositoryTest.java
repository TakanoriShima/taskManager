package test;

import repository.TaskRepository;
import repository.UserRepository;

public class RepositoryTest {

    public static void main(String[] args) {
        UserRepository userRepo = new UserRepository();
        TaskRepository taskRepo = new TaskRepository();

        System.out.println("=== UserRepository.findAll ===");
        System.out.println(userRepo.findAll());

        System.out.println("=== UserRepository.findById(1) ===");
        System.out.println(userRepo.findById(1)); // データ0ならnullでOK

        System.out.println("=== UserRepository.findByUsername(\"admin\") ===");
        System.out.println(userRepo.findByUsername("admin")); // データ0ならnullでOK

        System.out.println("=== TaskRepository.findAll ===");
        System.out.println(taskRepo.findAll());

        System.out.println("=== TaskRepository.findById(1) ===");
        System.out.println(taskRepo.findById(1)); // データ0ならnullでOK

        System.out.println("=== TaskRepository.findByUserId(1) ===");
        System.out.println(taskRepo.findByUserId(1)); // データ0なら空リストでOK
    }
}

