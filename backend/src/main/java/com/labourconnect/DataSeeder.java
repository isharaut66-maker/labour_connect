package com.labourconnect;

import com.labourconnect.entity.*;
import com.labourconnect.repository.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabourerProfileRepository profileRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private JobApplicationRepository applicationRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            System.out.println("Seeding database...");

            String defaultPassword = BCrypt.hashpw("password", BCrypt.gensalt());

            // Admin
            User admin = new User();
            admin.setName("Admin User");
            admin.setEmail("admin@labourconnect.com");
            admin.setPassword(defaultPassword);
            admin.setRole("ADMIN");
            userRepository.save(admin);

            // Clients
            User client1 = new User();
            client1.setName("Ravi Sharma");
            client1.setEmail("client@labourconnect.com");
            client1.setPassword(defaultPassword);
            client1.setRole("CLIENT");
            userRepository.save(client1);

            // Labourers
            String[] names = {"Raj Kumar", "Amit Patil", "Suresh Pawar", "Vijay Shinde", "Rohit More"};
            String[] skills = {"Mason", "Carpenter", "Electrician", "Plumber", "Painter"};
            String[] locations = {"Mumbai", "Pune", "Nashik", "Mumbai", "Pune"};
            
            for (int i = 0; i < 5; i++) {
                User l = new User();
                l.setName(names[i]);
                l.setEmail("labourer" + (i + 1) + "@labourconnect.com");
                l.setPassword(defaultPassword);
                l.setRole("LABOURER");
                l = userRepository.save(l);

                LabourerProfile p = new LabourerProfile();
                p.setUser(l);
                p.setSkill(skills[i]);
                p.setExperience((i * 2) + 1);
                p.setDailyRate(500.0 + (i * 100));
                p.setLocation(locations[i]);
                p.setAvailability("Available");
                p.setRating(4.0 + (i * 0.1));
                profileRepository.save(p);
            }

            // Contractor
            User contractor = new User();
            contractor.setName("BuildTech Solutions");
            contractor.setEmail("contractor@labourconnect.com");
            contractor.setPassword(defaultPassword);
            contractor.setRole("CONTRACTOR");
            userRepository.save(contractor);

            // Seed Projects
            Project proj1 = new Project();
            proj1.setClient(client1);
            proj1.setTitle("House Renovation");
            proj1.setDescription("Need a complete home renovation for 2BHK");
            proj1.setProjectType("Residential");
            proj1.setLocation("Pune");
            proj1.setBudget(50000.0);
            proj1.setStartDate(LocalDate.now().plusDays(5));
            proj1.setStatus("OPEN");
            projectRepository.save(proj1);

            System.out.println("Seeding completed. Use email 'client@labourconnect.com' and password 'password'");
        }
    }
}
