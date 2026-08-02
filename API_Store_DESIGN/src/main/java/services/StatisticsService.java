package services;

import dto.DashboardStatistics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repositories.CustomerRepository;
import repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public DashboardStatistics getOverallStatistics() {
        long totalCustomers = customerRepository.countTotalCustomers();
        long totalUsers = userRepository.countTotalUsers();
        long totalActiveUsers = userRepository.countTotalActiveUsers();

        return DashboardStatistics.builder()
                .totalCustomers(totalCustomers)
                .totalUsers(totalUsers)
                .totalActiveUsers(totalActiveUsers)
                .build();
    }
}
